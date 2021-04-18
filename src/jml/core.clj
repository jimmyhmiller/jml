(ns jml.core
  (:require [jml.decompile]
            [jml.backend :as backend]
            [clojure.walk :as walk]
            [clojure.string :as string]
            [clojure.reflect :as reflect])
  (:import [org.objectweb.asm Opcodes Type ClassWriter]
           [org.objectweb.asm.commons Method GeneratorAdapter]))


(defn resolve-type [expr-type]
  (let [t (type expr-type)]
    (cond   ;;if expr is already of asm.Type
      (= t Type)
      expr-type

      (or (symbol? expr-type) (keyword? expr-type) (string? expr-type))
      (case (symbol expr-type)
        asm-type (Type/getType Type) ;; :asm-type because having { :type :type } is a bit odd, don't you think?
        void Type/VOID_TYPE
        int Type/INT_TYPE
        long Type/LONG_TYPE
        boolean Type/BOOLEAN_TYPE
        bool Type/BOOLEAN_TYPE
        string (Type/getType String)
        (let [resolved (resolve (symbol expr-type))]
          (if resolved
            (Type/getType ^Class resolved)
            (throw (ex-info "Cannot resolve type  from symbol" {:type expr-type})))))

      (= t java.lang.Class)
      (Type/getType expr-type)

      :else
      (throw (ex-info (format  "[resolve-type] Unknown type %s" expr-type) {:expr expr-type})))))

(defn java-array? [obj]
  (.isArray (class obj)))

(defn resolve-method-type [expr]
  (if (= (type expr) org.objectweb.asm.commons.Method)
    expr
    (let [[_ method-name return-type arg-types] expr
          arg-types (if (java-array? arg-types) arg-types
                        (into-array Type (map resolve-type arg-types)))]
      (Method. method-name (resolve-type return-type) arg-types))))


(defn resolve-props-type [props]
  (if (map? props)
    (let [{:keys [type owner field-type result-type method]} props]
      (cond-> props
        type  (update :type resolve-type)
        owner (update :owner resolve-type)
        field-type (update :field-type resolve-type)
        method (update :method resolve-method-type)))
    props))



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


(defn full-symbol-name [x]
  (if (simple-symbol? x)
    x
    (str (namespace x) "/" (name x))))


(defn expand-cond [[_ & clauses]]
  (cond
    (empty? clauses) (throw (IllegalArgumentException.
                             "cond requires an even number of forms"))
    (and  (= (count clauses) 2) (= (first clauses) :else))
    (second clauses)

    (= (count clauses) 2)
    (throw (IllegalArgumentException. "We only have two armed ifs"))

    (not (empty? clauses))
    (list 'if (first clauses)
          (if (next clauses)
            (second clauses)
            (throw (ex-info "Even number " {:clauses clauses})))
          (expand-cond (cons 'cond (next (next clauses)))))))





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
              (and (seq? x) (symbol? (first x)) (= (first x) 'cond))
              (de-sexpr (expand-cond x))
              (and (seq? x) (symbol? (first x)) (string/starts-with? (name (first x)) ".-"))
              (vec (concat (rest x) [[:get-field {:name (subs (name (first x)) 2)}]] ))
              (and (seq? x) (symbol? (first x)) (string/starts-with? (full-symbol-name (first x)) "."))
              (vec (concat (rest x) [[:invoke-virtual {:name (first x)}]] ))
              (and (seq? x) (symbol? (first x)) (string/includes? (full-symbol-name (first x)) "/"))
              (vec (concat (rest x) [[:invoke-static {:name (first x)}]] ))
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


(defn get-field-type [class-asm-type field-name]
  (try
    (let [klass (Class/forName
                 (.getClassName ^Type class-asm-type))]

      (->> (reflect/reflect klass)
           :members
           (filter (comp #{(symbol field-name)} :name))
           first
           :type
           resolve-type))
    (catch Exception e
      (throw (ex-info "Could not get field type" {:type class-asm-type
                                                  :field-name field-name})))))

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
               :invoke-virtual
               (assoc current-instruction 1 (get-method-types
                                             (get-owner (:name (second current-instruction)))
                                             (get-method-name (:name (second current-instruction)))
                                             (get-method-arg-types (:name (second current-instruction)))))
               :invoke-static

               (assoc current-instruction 1 (get-method-types
                                             (resolve-type (namespace (:name (second current-instruction))))
                                             (get-method-name (:name (second current-instruction)))
                                             (get-method-arg-types  (:name (second current-instruction)))))
               current-instruction)))
          []
          linearized-code))

(defn log [x]
  (println (cons 'do x) (type x))
  (prn x)
  x)




(defn process-defn [[_ fn-name types & body]]
  (let [arg-types (mapv resolve-type (butlast types))]
    {:type :fn
     :class-name (string/replace (name fn-name) "." "/")
     :arg-types arg-types
     :return-type (resolve-type (last types))
     :code (concat (infer-interop-types arg-types
                                        (linearize (cons 'do  body)))
                   [[:return]])}))

(defn process-enum [[_ enum-name & variants]]
  {:class-name (string/replace (str enum-name) "." "/")
   :variants
   (mapv (fn [variant]
           (if (symbol? variant)
             {:name (str variant)
              :fields []}
             {:name (str (first variant))
              :fields (mapv (fn [[name type]]
                              {:name (str name) :type (resolve-type type)})
                            (partition 2 (rest variant)))}))
         variants)})



(defn dispatch-on-type [{:keys [type] :as entity}]
  (case type
    :fn (backend/make-fn entity)
    (throw (ex-info "not handled" {}))))


(defn run-multiple* [s-exprs]
  ;; Making an env to look functions up later.
  (reduce (fn [env s-expr]
            (case (first s-expr)
              ;; Put types in env
              defn (assoc env (second s-expr) (dispatch-on-type (process-defn s-expr)))
              defenum (do (backend/make-enum (process-enum s-expr)) env)
              (throw (ex-info "unhandled" {}))))
          {} s-exprs)
  nil)


(defmacro lang [& s-exprs]
  (run-multiple* s-exprs))

(lang

 (defenum lang.Code
   PlusInt
   SubInt
   Dup
   Pop
   Print
   Return
   (Arg argIndex int)
   (Math op int
         opType asm-type)
   (GetStatic owner asm-type
              name string
              resultType asm-type)
   (InvokeStatic owner asm-type
                 method org.objectweb.asm.commons.Method)
   (InvokeVirtual owner asm-type
                  method org.objectweb.asm.commons.Method)
   (InvokeConstructor owner asm-type
                      method org.objectweb.asm.commons.Method)
   (New owner asm-type
        method org.objectweb.asm.commons.Method)
   (Bool boolValue bool)
   (Int intValue int)
   (String stringValue string)
   (GetField owner asm-type
             name string
             fieldType asm-type)
   (PutField owner asm-type
             name string
             fieldType asm-type))


 (defn lang.generateCode [org.objectweb.asm.commons.GeneratorAdapter lang.Code void]

   (cond
     (.String/equals (.-tagName (arg 1)) "Arg")
     (.GeneratorAdapter/loadArg (arg 0)  (.-argIndex (arg 1)))

     (.String/equals (.-tagName (arg 1)) "Math")
     (.GeneratorAdapter/math (arg 0) (.-op (arg 1)) (.-opType (arg 1)))

     (.String/equals (.-tagName (arg 1)) "GetStaticField")
     (.GeneratorAdapter/getStatic (arg 0) (.-owner (arg 1)) (.-name (arg 1)) (.-resultType (arg 1)))

     (.String/equals (.-tagName (arg 1)) "InvokeStatic")
     (.GeneratorAdapter/invokeStatic (arg 0) (.-owner (arg 1)) (.-method (arg 1)))

     (.String/equals (.-tagName (arg 1)) "InvokeVirtual")
     (.GeneratorAdapter/invokeVirtual (arg 0) (.-owner (arg 1)) (.-method (arg 1)))

     (.String/equals (.-tagName (arg 1)) "InvokeConstructor")
     (.GeneratorAdapter/invokeConstructor (arg 0) (.-owner (arg 1)) (.-method (arg 1)))

     (.String/equals (.-tagName (arg 1)) "New")
     (.GeneratorAdapter/newInstance (arg 0) (.-owner (arg 1)))

     (.String/equals (.-tagName (arg 1)) "Bool")
     (.GeneratorAdapter/push$boolean (arg 0) (.-boolValue (arg 1)))

     (.String/equals (.-tagName (arg 1)) "Int")
     (.GeneratorAdapter/push$int (arg 0) (.-intValue (arg 1)))

     (.String/equals (.-tagName (arg 1)) "String")
     (.GeneratorAdapter/push$java.lang.String (arg 0) ^String (.-stringValue (arg 1)))

     (.String/equals (.-tagName (arg 1)) "GetField")
     (.GeneratorAdapter/getField (arg 0) (.-owner (arg 1)) (.-name (arg 1)) (.-fieldType (arg 1)))

     (.String/equals (.-tagName (arg 1)) "PutField")
     (.GeneratorAdapter/putField (arg 0) (.-owner (arg 1)) (.-name (arg 1)) (.-fieldType (arg 1)))

     (.String/equals (.-tagName (arg 1)) "Dup")
     (.GeneratorAdapter/dup (arg 0))

     (.String/equals (.-tagName (arg 1)) "Pop")
     (.GeneratorAdapter/pop (arg 0))

     (.String/equals (.-tagName (arg 1)) "Return")
     (.GeneratorAdapter/returnValue (arg 0))

     ;; Hack for returning void
     :else (.GeneratorAdapter/returnValue (arg 0))))

 (defn lang.parseThatInt [string int]
   (Integer/parseInt$java.lang.String (arg 0)))


 (defn lang.myGenerateCode [GeneratorAdapter void]
   (lang.generateCode/invoke (arg 0) (lang.Code/Int 42))
   (lang.generateCode/invoke (arg 0) (lang.Code/Return)))

 )




(lang.parseThatInt/invoke "42")


(do

  (backend/make-fn-with-callback
   "lang2.MyAwesomeCode"
   #(lang.myGenerateCode/invoke %))

  (lang2.MyAwesomeCode/invoke))
