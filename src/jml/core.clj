(ns jml.core
  (:require [jml.decompile]
            [clojure.walk :as walk]
            [clojure.string :as string]
            [clojure.reflect :as reflect])
  (:import [org.objectweb.asm Opcodes Type ClassWriter]
           [org.objectweb.asm.commons Method GeneratorAdapter]))


(set! *warn-on-reflection* true)

(def INIT (Method/getMethod "void <init>()"))


(defn generate-code! [^GeneratorAdapter gen command ]
  (let [code (second command)]
    (case (first command)

      :plus-int
      (recur gen [:math {:op GeneratorAdapter/ADD
                         :op-type Type/INT_TYPE}])

      :sub-int
      (recur gen [:math {:op GeneratorAdapter/SUB
                         :op-type Type/INT_TYPE}])

      :arg
      (.loadArg ^GeneratorAdapter gen (int (:value code)))
      :math
      (.math gen (:op code) (:op-type code))
      :get-static-field
      (.getStatic gen (:owner code) (:name code) (:result-type code))
      :invoke-static
      (.invokeStatic gen (:owner code) (:method code))
      :invoke-virtual
      (.invokeVirtual gen (:owner code) (:method code))
      :invoke-constructor
      (.invokeConstructor gen (:owner code) (:method code))
      :new
      (.newInstance gen (:owner code))
      :bool
      ;; Should assert bool?
      (.push gen (boolean (:value code)))
      :int
      (.push gen (int (:value code)))
      :string
      (.push gen ^String (:value code))
      :get-field
      (.getField gen (:owner code) (:name code) (:field-type code))
      :put-field
      (.putField gen (:owner code) (:name code) (:field-type code))
      :dup
      (.dup gen)
      :pop
      (.pop gen)

      :print (do
               (.dup gen)
               (.getStatic gen
                           (Type/getType (Class/forName "java.lang.System"))
                           "out"
                           (Type/getType ^java.lang.Class (.getGenericType (.getDeclaredField (Class/forName "java.lang.System")
                                                                                              "out"))))
               (.swap gen)
               (.invokeVirtual gen (Type/getType ^java.lang.Class
                                                 (.getGenericType (.getDeclaredField (Class/forName "java.lang.System")
                                                                                     "out")))
                               (Method. "println" Type/VOID_TYPE  (into-array Type [Type/INT_TYPE]))))
      ;; We need some environment or two phases for labels
      :return
      (.returnValue gen))))




(defn generate-code-with-env! [^GeneratorAdapter gen command env]

  (let [code (second command)]
    (case (first command)
      :label
      (if-let [label (get env (:value code))]
        (do
          (.mark gen label)
          env)
        (let [label (.newLabel gen)]
          (.mark gen label)
          (assoc env (:value code) label)))

      ;; :jump-equal and :jump-not-equal could be removed, since :jump-cmp replaces them
      :jump-not-equal
      (if-let [label (get env (:value code))]
        (do
          (.ifCmp gen (:compare-type code) GeneratorAdapter/NE label)
          env)
        (let [label (.newLabel gen)]
          (.ifCmp gen (:compare-type code) GeneratorAdapter/NE label)
          (assoc env (:value code) label)))


      :jump-equal
      (if-let [label (get env (:value code))]
        (do
          (.ifCmp gen (:compare-type code) GeneratorAdapter/EQ label)
          env)
        (let [label (.newLabel gen)]
          (.ifCmp gen (:compare-type code) GeneratorAdapter/EQ label)
          (assoc env (:value code) label)))

      :jump-cmp
      (if-let [label (get env (:value code))]
        (do
          (.ifCmp gen (:compare-type code) (:compare-op code) label)
          env)
        (let [label (.newLabel gen)]
          (.ifCmp gen (:compare-type code) (:compare-op code) label)
          (assoc env (:value code) label)))

      :jump
      (if-let [label (get env (:value code))]
        (do
          (.goTo gen label)
          env)
        (let [label (.newLabel gen)]
          (.goTo gen label)
          (assoc env (:value code) label)))


      :store-local
      (if-let [local (get env (str "local-" (:index code)))]
        (do
          (.storeLocal gen local (:local-type code))
          env)
        (let [local (.newLocal gen (:local-type code))]
          (.storeLocal gen local (:local-type code))
          (assoc env (str "local-" (:index code)) local)))

      :load-local
      (if-let [local (get env (str "local-" (:index code)))]
        (do
          (.loadLocal gen local (:local-type code))
          env)
        (throw (ex-info "This local was loaded before stored" {:command command :env env})))

      (do (generate-code! gen command)
          env))))


(defn generate-default-constructor [^ClassWriter writer]
  (let [gen (GeneratorAdapter. Opcodes/ACC_PUBLIC INIT nil nil writer)]
    (.visitCode gen)
    (.loadThis gen)
    (.invokeConstructor gen (Type/getType Object) INIT)
    (.returnValue gen)
    (.endMethod gen)))

(defn initialize-class [^ClassWriter writer class-name]
  (.visit writer Opcodes/V1_8 Opcodes/ACC_PUBLIC class-name nil "java/lang/Object" nil))

(defn cmp-op-type [op]
  (case op
    :=  GeneratorAdapter/EQ
    :!= GeneratorAdapter/NE
    :>  GeneratorAdapter/GT
    :>= GeneratorAdapter/GE
    :<  GeneratorAdapter/LT
    :<= GeneratorAdapter/LE
    ;; What is a good default here? EQ? Error?
    :unknown))


(defn resolve-cmp-op [pred]
  (let [[op arg1 arg2] pred
        ;; if it's greater or less than op, it should probably be INT_TYPE comparison, otherwise BOOL_TYPE. Obviously needs fixing
        cmp-type (if (#{:> :>= :< :<=} op) Type/INT_TYPE Type/BOOLEAN_TYPE)
        cmp-op (cmp-op-type op)
        ;; I'm going to assume that if it's not one of operators above,
        ;; then `pred` must be a single boolean value and I'll compare it for equality to [:bool true]
        [arg1 arg2] (if (or (= cmp-op :unknown)
                            (some nil? [arg1 arg2]))
                      [[:bool true]
                       pred]
                      [arg1 arg2])
        cmp-op (if (= cmp-op :unknown) GeneratorAdapter/EQ cmp-op)]
    {:compare-op cmp-op
     :compare-type cmp-type
     :arg1 arg1
     :arg2 arg2}))



(defn desugar-if [[tag pred t-branch f-branch :as node]]
  (if (= tag :if)
    (let [true-label (gensym "true_label_")
          exit-label (gensym "exit_label_")
          {:keys [compare-op compare-type
                  arg1 arg2]}     (resolve-cmp-op pred)]
      [:do
       arg1
       arg2
       [:jump-cmp {:value true-label :compare-op compare-op :compare-type compare-type}]
       f-branch
       [:jump {:value exit-label}]
       [:label {:value true-label}]
       t-branch
       [:label {:value exit-label}]])
    node))


(defn de-sexpr [expr]
  (walk/postwalk
   (fn [x] (if (reduced? x)
             @x
             x))
   (walk/prewalk
    (fn [x]
      (let [result
            (cond
              (and (seq? x) (= (first x) 'arg))
              (reduced [:arg {:value (second x)}])
              (and (seq? x) (symbol? (first x)) (string/starts-with? (name (first x)) ".-"))
              (vec (concat (rest x) [[:get-field {:name (subs (name (first x)) 2)}]] ))
              ;; Clean up to be consistent
              (and (seq? x) (symbol? (first x)) (string/starts-with? (full-symbol-name (first x)) "."))
              (vec (concat (rest x) [[:invoke-virtual {:name (first x)}]] ))
              (map? x) (reduced x)
              (seq? x) (vec x)
              (and (vector? x) (keyword? (first x))) (reduced x)
              (symbol? x) (keyword x)
              (int? x) (reduced [:int x])
              (boolean? x) (reduced [:bool x])
              (string? x) (reduced [:string x])
              :else x)]
        (if (instance? clojure.lang.IObj result)
          (with-meta result (meta x))
          result)))
    expr)))




(defn java-array? [obj]
  (.isArray (class obj)))

(defn resolve-type [expr-type]
  (let [t (type expr-type)]
    (cond   ;;if expr is already of asm.Type
      (= t Type)
      expr-type

      (symbol? expr-type)
      (case expr-type
        void Type/VOID_TYPE
        int Type/INT_TYPE
        long Type/LONG_TYPE
        boolean Type/BOOLEAN_TYPE
        (Type/getType ^Class (resolve expr-type)))

      (keyword? expr-type)
      (case expr-type
        :asm-type (Type/getType Type) ;; :asm-type because having { :type :type } is a bit odd, don't you think?
        :string (Type/getType String)
        :object (Type/getType Object)
        :void Type/VOID_TYPE
        :int Type/INT_TYPE
        :long Type/LONG_TYPE
        :bool Type/BOOLEAN_TYPE

        (throw (ex-info (format  "[resolve-type] Unknown type expr-type %s" expr-type) {:expr expr-type})))

      (= t java.lang.String)
      (Type/getType (resolve (symbol expr-type)))

      (= t java.lang.Class)
      (Type/getType expr-type)

      :else
      (throw (ex-info (format  "[resolve-type] Unknown type %s" expr-type) {:expr expr-type})))))


(defn resolve-method-type [expr]
  (if (= (type expr) org.objectweb.asm.commons.Method)
    expr
    (let [[_ method-name return-type arg-types] expr
          arg-types (if (java-array? arg-types) arg-types
                        (into-array Type (map resolve-type arg-types)))]
      (Method. method-name (resolve-type return-type) arg-types))))


(defn resolve-props-type [{:keys [type owner field-type result-type method] :as props}]
  (cond-> props
    type  (update :type resolve-type)
    owner (update :owner resolve-type)
    field-type (update :field-type resolve-type)
    method (update :method resolve-method-type)))





(defn linearize* [code]
  (let [[op props & children] code
        children (if (vector? props)
                   (cons props children)
                   children)
        props (cond (map? props)
                    props


                    (vector? props)
                    {}

                    :else {:value props})
        props (resolve-props-type props)]

    (cond (vector? op)
          (into [] (mapcat linearize* code))

          (not (keyword? op))
          code

          (empty? children)
          [[op props]]

          (= op :if)
          (recur (desugar-if code))

          (= op :do)
          (into [] (mapcat linearize* children))

          :else (conj (into [] (mapcat linearize* children)) [op props]))))



(defn linearize [expr]
  (linearize* (de-sexpr expr)))


(defn generate-invoke-method [^ClassWriter writer {:keys [code return-type arg-types]}]
  (let [method (Method. "invoke" (resolve-type return-type) (into-array Type (map resolve-type  arg-types)))
        gen (GeneratorAdapter. (int (+ Opcodes/ACC_PUBLIC Opcodes/ACC_STATIC)) method nil nil writer)]
    (reduce (fn [env line] (generate-code-with-env! gen line env)) {} code)
    (.endMethod ^GeneratorAdapter gen)))


(defn get-field-type [class-asm-type field-name]
  (let [klass (Class/forName
               (.getClassName ^Type class-asm-type))]

    (->> (reflect/reflect klass)
         :members
         (filter (comp #{(symbol field-name)} :name))
         first
         :type
         resolve-type)))

(defn get-method-types [class-asm-type method-name method-args]
  (let [klass (Class/forName
               (.getClassName ^Type class-asm-type))
        methods (->> (reflect/reflect klass)
                     :members
                     (filter (comp #{(symbol method-name)} :name))
                     (filter (fn [{:keys [parameter-types]}]
                               (or (nil? method-args)
                                   (= method-args parameter-types)))))
        _ (assert (= 1 (count methods)) (format "Method overloaded, need to deal with this %s %s" (.getName klass) method-name))
        {:keys [return-type parameter-types]}
        (first methods)]
    {:owner (resolve-type klass)
     :method (Method. method-name (resolve-type return-type)
                      (into-array Type (map resolve-type parameter-types)))}))


(defn infer-type-get-field [arg-types [op-type value] instruction]
  (case op-type
    :arg (-> instruction
             (assoc-in [1 :owner] (get arg-types (:value value)))
             (assoc-in [1 :field-type] (get-field-type (get arg-types (:value value))
                                                       (:name (second instruction)))))
    instruction))



(defn get-owner [sym]
  (resolve-type (subs (namespace sym) 1)))


(defn get-method-name [sym]
  (first (string/split (name sym) #"\$")))

(defn get-method-arg-types [sym]
  (if (string/includes? (name sym) "$")
    (mapv symbol (rest (string/split (name sym) #"\$")))
    nil))


(defn infer-interop-types [arg-types linearized-code]
  (reduce (fn [code current-instruction]
            (conj
             code
             (case (first current-instruction)
               :get-field (infer-type-get-field arg-types (last code) current-instruction)
               :invoke-virtual (do
                                 (assoc current-instruction 1 (get-method-types
                                                                 (get-owner (:name (second current-instruction)))
                                                                 (get-method-name (:name (second current-instruction)))
                                                                 (get-method-arg-types  (:name (second current-instruction))))))
               current-instruction)
             ))
          []
          linearized-code))

(defn make-fn [{:keys [class-name code arg-types] :as description}]
  (let [writer (ClassWriter. (int (+ ClassWriter/COMPUTE_FRAMES ClassWriter/COMPUTE_MAXS)))
        code (conj (linearize code) [:return])]
    (initialize-class writer class-name)
    (generate-default-constructor writer)
    (generate-invoke-method writer (assoc description :code (infer-interop-types arg-types code)))
    (.visitEnd writer)

    (jml.decompile/print-and-load-all writer class-name)))



(defn make-field [^ClassWriter writer {:keys [name type]}]
  (let [field (.visitField writer Opcodes/ACC_PUBLIC name (.getDescriptor ^Type (resolve-type type)) nil nil)]
    (.visitEnd field)))

(defn make-field-assignment [^GeneratorAdapter gen this-type index {:keys [name type]}]
  (.loadThis gen)
  (.loadArg gen (int index))
  (.putField gen this-type name type))


(defn make-field-assignment-on-stack [^GeneratorAdapter gen this-type index {:keys [name type]}]
  (.dup gen)
  (.loadArg gen (int index))
  (.putField gen this-type name type))


(defn run-indexed! [f coll]
  (doall (map-indexed f coll)))

(defn make-struct-constructor [writer {:keys [class-name fields]}]
  (let [gen (GeneratorAdapter. Opcodes/ACC_PUBLIC
                               (Method. "<init>" Type/VOID_TYPE (into-array Type (map :type fields))) nil nil writer)
        ;; This is probably not always true, especially with namespacing.
        this-type (Type/getType (str "L" class-name ";"))]
    (.visitCode gen)
    (.loadThis gen)
    (.invokeConstructor gen (Type/getType Object) INIT)
    (run-indexed! (fn [i field] (make-field-assignment gen this-type i field)) fields)
    (.returnValue gen)
    (.endMethod gen)))

(defn make-struct [description]
  (let [{:keys [class-name fields] :as description} (update description :fields (partial map resolve-props-type))
        writer (ClassWriter. (int (+ ClassWriter/COMPUTE_FRAMES ClassWriter/COMPUTE_MAXS)))]
    (initialize-class writer class-name)
    (generate-default-constructor writer)
    (run! (partial make-field writer) fields)
    (make-struct-constructor writer description)
    (.visitEnd writer)
    (jml.decompile/print-and-load-bytecode writer class-name)
    class-name))


(defn gen-field-to-string [^GeneratorAdapter gen this-type sb-type sb-append length index {:keys [name type]}]
  ;; assumes there is already a StringBuilder on stack, ready to append to..
  (.loadThis gen)
  (.getField gen this-type name type)

  (.invokeVirtual gen sb-type
                  (Method. "append"
                           (Type/getType java.lang.StringBuilder)
                           (into-array Type [(if (= (.getSort ^Type type) Type/OBJECT)
                                               (Type/getType Object)
                                               type)])))
  (when-not (= length (inc index))
    (.dup gen)
    (.push gen " ")
    (.invokeVirtual gen sb-type sb-append))
  (.dup gen))


(defn make-table-switch-gen [^GeneratorAdapter gen {:keys [class-name variants]}]
  (let [variants-indexed (into {} (map-indexed vector variants))
        this-type (Type/getType (str "L" class-name ";"))
        sb-type (Type/getType (Class/forName "java.lang.StringBuilder"))
        sb-ctor (Method/getMethod "void <init> (String)")
        sb-to-string (Method/getMethod "String toString ()")
        sb-append    (Method/getMethod "java.lang.StringBuilder append (String)")]
    (proxy [org.objectweb.asm.commons.TableSwitchGenerator] []
      (generateCase [int-key end]
        (let [{:keys [name fields]} (variants-indexed int-key)]
          (.newInstance gen sb-type)
          (.dup gen)
          (.push gen (str "(" class-name "/" name (when-not (empty? fields) " ")))
          (.invokeConstructor gen sb-type sb-ctor)

          (run-indexed! (partial gen-field-to-string gen this-type sb-type sb-append (count fields)) (map resolve-props-type fields))

          (.push gen ")")
          (.invokeVirtual gen sb-type sb-append)
          (.invokeVirtual gen sb-type sb-to-string)

          (.returnValue gen))
        )
      (generateDefault []
        (.push gen "Variant not found")
        (.returnValue gen)))))


(defn make-to-string [writer {:keys [class-name variants] :as description}]
  (let [this-type (Type/getType (str "L" class-name ";"))
        method (Method. "toString" (Type/getType String) (into-array Type []))
        gen (GeneratorAdapter. (int (+ Opcodes/ACC_PUBLIC)) method nil nil writer)
        tsg (make-table-switch-gen gen description)]

    (.visitCode gen)
    (.loadThis gen)
    (.getField gen this-type "tag" Type/INT_TYPE)

    (.tableSwitch gen
                  (int-array (range (count variants)))
                  tsg)

    (.endMethod ^GeneratorAdapter gen)))




(defn make-enum-factory [writer class-name {:keys [name fields tagName tag]}]
  (let [ ;; This is probably not always true, especially with namespacing.
        this-type (Type/getType (str "L" class-name ";"))
        fields (map resolve-props-type fields)
        method (Method. name this-type (into-array Type (map :type fields)))
        gen (GeneratorAdapter. (int (+ Opcodes/ACC_PUBLIC Opcodes/ACC_STATIC)) method nil nil writer)]
    (.visitCode gen)
    (.newInstance gen this-type)
    (.dup gen)
    (.invokeConstructor gen this-type INIT)

    (.dup gen)
    (.push gen (int tag))
    (.putField gen this-type "tag" Type/INT_TYPE)

    (.dup gen)
    (.push gen ^String tagName)
    (.putField gen this-type "tagName" (Type/getType String))


    (run-indexed! (fn [i field] (make-field-assignment-on-stack gen this-type i field)) fields)
    (.returnValue gen)
    (.endMethod ^GeneratorAdapter gen)))


(defn make-enum-variant [writer class-name tag {:keys [name fields] :as enum}]
  (make-enum-factory writer class-name (assoc enum :tag tag :tagName name)))



;; NOTE: All names type combinations must be unique
(defn make-enum [{:keys [class-name variants] :as description}]
  (let [writer (ClassWriter. (int (+ ClassWriter/COMPUTE_FRAMES ClassWriter/COMPUTE_MAXS)))]
    (initialize-class writer class-name)
    (generate-default-constructor writer)
    (make-field writer {:name "tag" :type Type/INT_TYPE})
    (make-field writer {:name "tagName" :type (Type/getType String)})
    (make-to-string writer description)
    (run! (partial make-field writer) (set (mapcat (comp resolve-props-type  :fields) variants)))
    (run-indexed! (partial make-enum-variant writer class-name) variants)
    (.visitEnd writer)
    ;; Should have a way to return class and not print
    (jml.decompile/print-and-load-bytecode writer class-name)))



;; TODO Get rid of evals
;; This code is pretty gross
;; remove hacks
(defn get-type [type]
  (cond (symbol? type)
        (eval type)

        :else type))

(defn process-defn [[_ name types body]]
  {:type :fn
   :class-name (clojure.core/name name)
   :arg-types (map get-type(butlast types))
   :return-type (get-type (last types))
   :code body})


(defn dispatch-on-type [{:keys [type] :as entity}]
  (case type
    :fn (make-fn entity)
    (throw (ex-info "not handled" {}))))


(defn run-multiple [& s-exprs]
  (let [env
        (reduce (fn [env s-expr]
                  (case (first s-expr)
                    ;; Put types in env
                    'defn (assoc env (second s-expr) (dispatch-on-type (process-defn s-expr)))
                    (throw (ex-info "unhandled" {}))))
                {} s-exprs)]
    ;; invoke main using reflection
    (.invoke ^java.lang.reflect.Method
             (.getMethod ^java.lang.Class (get env 'main)
                         "invoke" (into-array Class []))
             nil
             (into-array Object []))))


;; TODO
;; Resolve types over whole ast
;; Capture Function Evironment with Types
;; Invoke functions
;; Better Interop
;; Light-weight type annotations
;; Match using table switch AST tag
;; General code cleanup (no multiple files)



;; TODO
;; Syntax for static invoke
;; Sytax for method invoke
;; Syntax for function call
;; Syntax for enums
;; Have a type environment
;; Make function syntax?
;; Need Loop construct
;; Let would be nice




(def code

  (jml.core/make-enum
   {:class-name "Code"
    :variants
    [{:name "PlusInt"
      :fields []}
     {:name "SubInt"
      :fields []}
     {:name "Arg"
      :fields [{:name "argIndex" :type :int}]}
     {:name "Math"
      :fields [{:name "op" :type :int}
               {:name "opType" :type :asm-type}]}
     {:name "GetStaticField"
      :fields [{:name "owner" :type :asm-type}
               {:name "name" :type :string}
               {:name "resultType" :type :asm-type}]}
     {:name "InvokeStatic"
      :fields [{:name "owner" :type :asm-type}
               {:name "method" :type "org.objectweb.asm.commons.Method"}]}
     {:name "InvokeVirtual"
      :fields [{:name "owner" :type :asm-type}
               {:name "method" :type "org.objectweb.asm.commons.Method"}]}
     {:name "InvokeConstructor"
      :fields [{:name "owner" :type :asm-type}
               {:name "method" :type "org.objectweb.asm.commons.Method"}]}
     {:name "new"
      :fields [{:name "owner" :type :asm-type}]}
     {:name "Bool"
      :fields [{:name "boolValue" :type :bool}]}
     {:name "Int"
      :fields [{:name "intValue" :type :int}]}
     {:name "String"
      :fields [{:name "stringValue" :type :string}]}
     {:name "GetField"
      :fields [{:name "owner" :type :asm-type}
               {:name "name" :type :string}
               {:name "fieldType" :type :asm-type}]}
     {:name "PutField"
      :fields [{:name "owner" :type :asm-type}
               {:name "name" :type :string}
               {:name "fieldType" :type :asm-type}]}
     {:name "Dup"
      :fields []}
     {:name "Pop"
      :fields []}
     {:name "Print"
      :fields []}
     {:name "Return"
      :fields []}]}))



(do

  (def self-compile
    ;; Need math operators
    '(if (.String/equals (.-tagName (arg 0)) "Arg")
       (.GeneratorAdapter/loadArg (arg 1)  (.-argIndex (arg 0)))
       (if (.String/equals (.-tagName (arg 0)) "Math")
         (.GeneratorAdapter/math (arg 1) (.-op (arg 0)) (.-opType (arg 0)))
         (if (.String/equals (.-tagName (arg 0)) "GetStaticField")
           (.GeneratorAdapter/getStatic (arg 1) (.-owner (arg 0)) (.-name (arg 0)) (.-resultType (arg 0)))
           (if (.String/equals (.-tagName (arg 0)) "InvokeStatic")
             (.GeneratorAdapter/invokeStatic (arg 1) (.-owner (arg 0)) (.-method (arg 0)))
             (if (.String/equals (.-tagName (arg 0)) "InvokeVirtual")
               (.GeneratorAdapter/invokeVirtual (arg 1) (.-owner (arg 0)) (.-method (arg 0)))
               (if (.String/equals (.-tagName (arg 0)) "InvokeConstructor")
                 (.GeneratorAdapter/invokeConstructor (arg 1) (.-owner (arg 0)) (.-method (arg 0)))
                 (if (.String/equals (.-tagName (arg 0)) "New")
                   (.GeneratorAdapter/newInstance (arg 1) (.-owner (arg 0)))
                   (if (.String/equals (.-tagName (arg 0)) "Bool")
                     (.GeneratorAdapter/push$boolean (arg 1) (.-boolValue (arg 0)))
                     (if (.String/equals (.-tagName (arg 0)) "Int")
                       (.GeneratorAdapter/push$int (arg 1) (.-intValue (arg 0)))
                       (if (.String/equals (.-tagName (arg 0)) "String")
                         (.GeneratorAdapter/push$java.lang.String (arg 1) ^String (.-stringValue (arg 0)))
                         (if (.String/equals (.-tagName (arg 0)) "GetField")
                           (.GeneratorAdapter/getField (arg 1) (.-owner (arg 0)) (.-name (arg 0)) (.-fieldType (arg 0)))
                           (if (.String/equals (.-tagName (arg 0)) "PutField")
                             (.GeneratorAdapter/putField (arg 1) (.-owner (arg 0)) (.-name (arg 0)) (.-fieldType (arg 0)))
                             (if (.String/equals (.-tagName (arg 0)) "Dup")
                               (.GeneratorAdapter/dup (arg 1))
                               (if (.String/equals (.-tagName (arg 0)) "Pop")
                                 (.GeneratorAdapter/pop (arg 1))
                                 (if (.String/equals (.-tagName (arg 0)) "Return")
                                   (.GeneratorAdapter/returnValue (arg 1))
                                   ;; Hack for returning void
                                   (.GeneratorAdapter/returnValue (arg 1)))))))))))))))))

    #_'(if (.String/equals (.-tagName (arg 0)) "Int")
         42
         0)
    )

  (linearize self-compile)

  (make-fn {:class-name "generateCode"
            :code
            self-compile
            :arg-types [(Type/getType (Class/forName "Code")) (Type/getType (resolve 'GeneratorAdapter))]
            :return-type Type/VOID_TYPE})


  (let [class-name "MyAwesomeCode"
        writer (ClassWriter. (int (+ ClassWriter/COMPUTE_FRAMES ClassWriter/COMPUTE_MAXS)))
        method (Method. "invoke" Type/INT_TYPE (into-array Type []))
        gen (GeneratorAdapter. (int (+ Opcodes/ACC_PUBLIC Opcodes/ACC_STATIC)) method nil nil writer)]
    (initialize-class writer class-name)

    (generate-default-constructor writer)
    (generateCode/invoke (Code/Int 22) gen)
    (generateCode/invoke (Code/Return) gen)
    (.endMethod ^GeneratorAdapter gen)
    (.visitEnd writer)
    (jml.decompile/print-and-load-all writer class-name))


 #_ [
   #_(generateCode/invoke (Code/Bool true))])


(MyAwesomeCode/invoke)
