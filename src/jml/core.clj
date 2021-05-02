(ns jml.core
  (:require [jml.decompile]
            [jml.backend :as backend]
            [clojure.walk :as walk]
            [clojure.string :as string]
            [clojure.reflect :as reflect]
            [clj-java-decompiler.core])
  (:import [org.objectweb.asm Opcodes Type ClassWriter]
           [org.objectweb.asm.commons Method GeneratorAdapter]))




(defn resolve-type [expr-type]
  (let [t (type expr-type)]
    (cond
      ;; if expr is already of asm.Type
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

    (let [{:keys [type owner field-type result-type method local-type]} props]
      (cond-> props
        type  (update :type resolve-type)
        local-type (update :local-type resolve-type)
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





(defn replace-sexprs [sexpr replacement-map]
  (walk/postwalk-replace replacement-map sexpr))


(defn desugar-let [[_ bindings & body]]
  (when-not (zero? (mod (count bindings) 3))
    (throw (ex-info "Invalid bindings, did you forget a type?" {:bindings bindings :body body})))
  (let [bindings (map vec (partition 3 bindings))
        bindings-map (into {} (map (fn [[k _ _]]
                                     [k (list 'load-local {:name (name k)})])
                                   bindings))
        locals (map (fn [[k v t]]
                      (list 'store-local {:name (name k) :local-type t} v)) bindings)
        body (replace-sexprs body bindings-map)]

    (concat '(do) locals body)))


(defn replace-let-exprs [expr]
  (walk/postwalk
   (fn [x]
     (if (and (seq? x) (symbol? (first x)) (=  (first x) 'let))
       (desugar-let x)
       x))
   expr))


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
              (and (seq? x) (symbol? (first x)) (= (first x) 'do))
              (vec (interpose '(pop) (rest x)))
              (and (seq? x) (symbol? (first x)) (string/starts-with? (name (first x)) ".-"))
              (into [:get-field {:name (subs (name (first x)) 2)}] (rest x)) 
              (and (seq? x) (symbol? (first x)) (string/starts-with? (full-symbol-name (first x)) "."))
              (into [:invoke-virtual {:name (first x)}] (rest x))
              (and (seq? x) (symbol? (first x)) (string/includes? (full-symbol-name (first x)) "/"))
              (into [:invoke-static {:name (first x)}] (rest x))
              (map? x) (reduced x)
              (seq? x) (vec x)
              (and (vector? x) (keyword? (first x))) (reduced x)
              (and (symbol? x) (string/includes? (full-symbol-name x) "/"))
              [:get-static-field {:name x}]
              (symbol? x) (keyword x)
              (int? x) (reduced [:int x])
              (boolean? x) (reduced [:bool x])
              (string? x) (reduced [:string x])
              (nil? x) (reduced [:nil])
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
        _ (when-not (= 1 (count methods))
            (throw (ex-info "Method overloaded. Need to be more specific"
                            {:class (.getName klass)
                             :method-name method-name
                             :methods methods
                             :method-args method-args})))
        {:keys [return-type parameter-types]}
        (first methods)]
    {:owner (resolve-type klass)
     :return-type return-type
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



(defn infer-static-field [current-instruction]
  (let [owner (resolve-type (namespace (:name (second current-instruction))))
        name (get-method-name (:name (second current-instruction)))]
    (-> current-instruction
        (assoc-in [1 :owner] owner)
        (assoc-in [1 :name] name)
        (assoc-in [1 :field-type] (get-field-type owner name)))))



(defn infer-interop-types [arg-types linearized-code]
  (reduce (fn [code current-instruction]
            (concat
             code
             (let [result
                   (case (first current-instruction)
                     :get-field (infer-type-get-field arg-types (last code) current-instruction)
                     :get-static-field (infer-static-field current-instruction)
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
                     current-instruction)]
               (if (= (:return-type (second result)) 'void)
                 [result [:nil]]
                 [result]))))
          []
          linearized-code))

(defn log [& args]
  (apply prn args)
  (last args))



(defn replace-args [sexpr arg-names]
  (replace-sexprs sexpr (into {} (map vector arg-names (map (fn [i] (list 'arg i)) (range))))))


(defn process-defn [[_ fn-name types & body]]
  (let [arg-names (map first (partition 2 (butlast types)))
        arg-types (mapv resolve-type (map second (partition 2 (butlast types))))]
    {:type :fn
     :class-name (string/replace (name fn-name) "." "/")
     :arg-types arg-types
     :return-type (resolve-type (last types))
     :code (concat (->> (replace-args (replace-let-exprs body) arg-names)
                        (cons 'do)
                        linearize
                        (infer-interop-types arg-types))
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


(defn run-multiple* [s-exprs]
  ;; Making an env to look functions up later.
  (reduce (fn [env s-expr]
            (case (first s-expr)
              ;; Put types in env
              defn (assoc env (second s-expr)
                          (backend/make-fn (process-defn s-expr)))
              defenum (do (backend/make-enum (process-enum s-expr)) env)
              (throw (ex-info "unhandled" {:s-expr s-expr}))))
          {} s-exprs)
  nil)


(defmacro jml [& s-exprs]
  (run-multiple* s-exprs))

(jml

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
             fieldType asm-type)
   (Label stringValue string)
   (JumpNotEqual stringValue string
                 compareType asm-type)
   (JumpEqual stringValue string
              compareType asm-type)
   (JumpCmp stringValue string
            compareType asm-type)
   (Jump stringValue string))

 (defn lang.generateCode [gen org.objectweb.asm.commons.GeneratorAdapter code lang.Code void]

   (cond
     (.String/equals (.-tagName code) "Arg")
     (.GeneratorAdapter/loadArg gen  (.-argIndex code))

     (.String/equals (.-tagName code) "Math")
     (.GeneratorAdapter/math gen (.-op code) (.-opType code))

     (.String/equals (.-tagName code) "GetStaticField")
     (.GeneratorAdapter/getStatic gen (.-owner code) (.-name code) (.-resultType code))

     (.String/equals (.-tagName code) "InvokeStatic")
     (.GeneratorAdapter/invokeStatic gen (.-owner code) (.-method code))

     (.String/equals (.-tagName code) "InvokeVirtual")
     (.GeneratorAdapter/invokeVirtual gen (.-owner code) (.-method code))

     (.String/equals (.-tagName code) "InvokeConstructor")
     (.GeneratorAdapter/invokeConstructor gen (.-owner code) (.-method code))

     (.String/equals (.-tagName code) "New")
     (.GeneratorAdapter/newInstance gen (.-owner code))

     (.String/equals (.-tagName code) "Bool")
     (.GeneratorAdapter/push$boolean gen (.-boolValue code))

     (.String/equals (.-tagName code) "Int")
     (.GeneratorAdapter/push$int gen (.-intValue code))

     (.String/equals (.-tagName code) "String")
     (.GeneratorAdapter/push$java.lang.String gen ^String (.-stringValue code))

     (.String/equals (.-tagName code) "GetField")
     (.GeneratorAdapter/getField gen (.-owner code) (.-name code) (.-fieldType code))

     (.String/equals (.-tagName code) "PutField")
     (.GeneratorAdapter/putField gen (.-owner code) (.-name code) (.-fieldType code))

     (.String/equals (.-tagName code) "Dup")
     (.GeneratorAdapter/dup gen)

     (.String/equals (.-tagName code) "Pop")
     (.GeneratorAdapter/pop gen)

     (.String/equals (.-tagName code) "Return")
     (.GeneratorAdapter/returnValue gen)

     ;; Hack for returning void
     ;; We should probably just not add return if the type is void?
     :else (.GeneratorAdapter/returnValue gen)))


 (defn lang.myGenerateCode [gen GeneratorAdapter void]
   (lang.generateCode/invoke gen (lang.Code/Int 42))
   (lang.generateCode/invoke gen (lang.Code/Return)))

 (defn lang.generateCodeWithEnv [gen org.objectweb.asm.commons.GeneratorAdapter code lang.Code env java.util.Map java.util.Map]
   (cond
     (.String/equals (.-tagName code) "Label")
     (if (.java.util.Map/containsKey env (.-stringValue code))
       (.org.objectweb.asm.commons.GeneratorAdapter/mark$org.objectweb.asm.Label
        gen
        (.java.util.Map/get env (.-stringValue code)))

       (do
         (store-local {:local-type org.objectweb.asm.Label
                       :name "label" }
                      (.org.objectweb.asm.commons.GeneratorAdapter/newLabel gen))
         (.org.objectweb.asm.commons.GeneratorAdapter/mark$org.objectweb.asm.Label
          gen
          (.java.util.Map/get env (.-stringValue code)))
         (.java.util.Map/put env (.-stringValue code) (load-local {:name "label"}))))

     (.String/equals (.-tagName code) "Jump")
     (if (.java.util.Map/containsKey env (.-stringValue code))
       (.org.objectweb.asm.commons.GeneratorAdapter/goTo
        code
        (.java.util.Map/get env (.-stringValue code)))

       (let [label (.org.objectweb.asm.commons.GeneratorAdapter/newLabel gen) org.objectweb.asm.Label]
         (.org.objectweb.asm.commons.GeneratorAdapter/goTo gen label)
         (.java.util.Map/put env (.-stringValue code) label)))
     :else
     (lang.myGenerateCode/invoke gen))
   env)

 (defn lang.parseThatInt [s string int]
   (Integer/parseInt$java.lang.String s))

 ;; this doesn't actually work because the type isn't resolved yet :(
 #_(defn lang.factorial [n int int]
   (if (= n 0)
     1
     (mult-int n (lang.factorial/invoke (sub-int n 1)))))

 )








(lang.parseThatInt/invoke "42")


(do

  (backend/make-fn-with-callback
   "lang2.MyAwesomeCode"
   #(lang.myGenerateCode/invoke %))

  (lang2.MyAwesomeCode/invoke))




