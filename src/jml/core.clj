(ns jml.core
  (:require [jml.decompile]
            [jml.backend :as backend]
            [clojure.walk :as walk]
            [clojure.string :as string]
            [clojure.reflect :as reflect]
            [clj-java-decompiler.core]
            [jml.type-checker :as type-checker])
  (:import [org.objectweb.asm Opcodes Type ClassWriter]
           [org.objectweb.asm.commons Method GeneratorAdapter]))



;; TODO: get rid of this
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
        (Type/getType (str "L"  (string/replace expr-type "." "/") ";"))
        #_(let [_ (println expr-type)
              resolved (resolve (symbol expr-type))]
          (if resolved
            (Type/getType ^Class resolved)
            (throw (ex-info "Cannot resolve type  from symbol" {:type expr-type})))))

      (= t java.lang.Class)
      (Type/getType expr-type)

      :else
      (throw (ex-info (format  "[resolve-type] Unknown type %s" expr-type) {:expr expr-type})))))


;; TODO: get rid of this
(defn resolve-props-type [props]

  (if (map? props)

    (let [{:keys [type owner field-type result-type method local-type]} props]
      (cond-> props
        type  (update :type resolve-type)
        local-type (update :local-type resolve-type)
        owner (update :owner resolve-type)
        field-type (update :field-type resolve-type)))

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
  (let [[op _ arg1 arg2] pred
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


(defn desugar-if [[tag _ pred t-branch f-branch :as node]]
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
   (fn [x]

     (cond
       (and (vector? x)
            (not (map-entry? x))
            (keyword? (first x))
            (not (map? (second x)))
            (not (vector? (second x))))
       [(first x) {:value (second x)}]
       (and (vector? x)
              (not (map-entry? x))
              (keyword? (first x))
              (not (map? (second x))))
       (into [(first x) {}] (rest x))
       :else x))
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
               (into [:do] (interpose [:pop] (rest x)))
               (and (seq? x) (symbol? (first x)) (= (first x) 'new))
               (into [:new {:value (second x)}] (rest (rest x)))
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
     expr))))


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


(defn log [& args]
  (apply prn args)
  (last args))



(defn replace-args [sexpr arg-names]
  (replace-sexprs sexpr (into {} (map vector arg-names (map (fn [i] (list 'arg i)) (range))))))


(defn replace-aliases [sexpr aliases]
  (walk/postwalk (fn [x]
                   (cond (not (symbol? x)) x
                         (simple-symbol? x) x
                         (contains? aliases (symbol (namespace x)))
                         (symbol (name (get aliases (symbol (namespace x)))) (name x))
                         :else x))
                 sexpr))


(def my-expr (atom {}))

(@my-expr 'lang.generateCode)

(defn process-defn-for-types [[_ fn-name types & body] env]

  (let [arg-names (mapv first (partition 2 (butlast types)))
        arg-types (mapv second (partition 2 (butlast types)))
        env (assoc-in env
                      [:functions fn-name]
                      {:type :fn
                       :class-name (string/replace (name fn-name) "." "/")
                       :arg-types arg-types
                       :return-type (last types)})]
    (assoc-in env [:functions fn-name :code]
              (time (type-checker/augment {:expr (let [expr (-> (cons 'do body)
                                                                replace-let-exprs
                                                                (replace-args arg-names)
                                                                (replace-aliases (:aliases env))
                                                                de-sexpr)]
                                                   (swap! my-expr assoc fn-name expr)
                                                   expr)
                                           :env (assoc env :arg-types arg-types)
                                           :type (last types)})))))



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


(defn process-enum-for-types [[_ enum-name & variants] env]
  (-> env
      (update :data-types merge {enum-name
                                 (->> variants
                                      (remove symbol?)
                                      (mapcat (fn [variant]
                                                (rest variant)))
                                      (partition 2)
                                      (map (fn [[n t]] [(str n) t]))
                                      (map vec)
                                      (into {"tagName" 'java.lang.String}))})
      (update :functions merge (->> variants
                                    (mapv (fn [variant]
                                            (if (symbol? variant)
                                              [variant {:arg-types []
                                                        :return-type enum-name}]
                                              [(first variant) {:arg-types (mapv second (partition 2 (rest variant)))
                                                                :return-type enum-name}])))
                                    (into {})))))



(defn process-alias [[_ alias source] env]
  (assoc-in env [:aliases alias] source))


(defn run-multiple* [s-exprs]
  (reduce (fn [env s-expr]
            (case (first s-expr)
              defn (let [[_ fn-name & _] s-expr
                         env (process-defn-for-types s-expr env)
                         function (get-in env [:functions fn-name])]
                     (backend/make-fn (-> function
                                          (update :arg-types #(map resolve-type %))
                                          (update :return-type resolve-type)
                                          (update :code (fn [code] (concat (linearize* code) [[:return]])))))
                     env)
              defenum (do (backend/make-enum (process-enum s-expr))
                          (process-enum-for-types s-expr env))
              defalias (process-alias s-expr env)
              (throw (ex-info "unhandled" {:s-expr s-expr}))))
          {:functions {}
           :data-types {}
           :aliases {}}
          s-exprs)
  nil)


(defmacro jml [& s-exprs]
  (run-multiple* s-exprs))

(jml


 (defalias GeneratorAdapter org.objectweb.asm.commons.GeneratorAdapter)
 (defalias Type org.objectweb.asm.Type)
 (defalias Opcodes org.objectweb.asm.Opcodes)

 (defenum lang.Code
   MultInt
   PlusInt
   SubInt
   Dup
   Pop
   Print
   Return
   Nil
   (Arg argIndex int)
   (Math op int
         opType org.objectweb.asm.Type)
   (GetStatic owner org.objectweb.asm.Type
              name java.lang.String
              resultType org.objectweb.asm.Type)
   (InvokeStatic owner org.objectweb.asm.Type
                 method org.objectweb.asm.commons.Method)
   (InvokeVirtual owner org.objectweb.asm.Type
                  method org.objectweb.asm.commons.Method)
   (InvokeConstructor owner org.objectweb.asm.Type
                      method org.objectweb.asm.commons.Method)
   (New owner org.objectweb.asm.Type
        method org.objectweb.asm.commons.Method)
   (Bool boolValue boolean)
   (Int intValue int)
   (String stringValue java.lang.String)
   (GetField owner org.objectweb.asm.Type
             name java.lang.String
             fieldType org.objectweb.asm.Type)
   (PutField owner org.objectweb.asm.Type
             name java.lang.String
             fieldType org.objectweb.asm.Type)
   (Label stringValue java.lang.String)
   (JumpNotEqual stringValue java.lang.String
                 compareType org.objectweb.asm.Type)
   (JumpEqual stringValue java.lang.String
              compareType org.objectweb.asm.Type)
   (JumpCmp stringValue java.lang.String
            compareType org.objectweb.asm.Type)
   (Jump stringValue java.lang.String))

 (defn lang.generateCode [gen org.objectweb.asm.commons.GeneratorAdapter code lang.Code void]

   (cond

     (.equals (.-tagName code) "MultInt")
     (.math gen
            GeneratorAdapter/MUL
            org.objectweb.asm.Type/INT_TYPE)

     (.equals (.-tagName code) "PlusInt")
     (lang.generateCode/invoke gen (lang.Code/Math GeneratorAdapter/ADD Type/INT_TYPE))

     (.equals (.-tagName code) "SubInt")
     (lang.generateCode/invoke gen (lang.Code/Math GeneratorAdapter/SUB Type/INT_TYPE))

     (.String/equals (.-tagName code) "Arg")
     (.loadArg gen  (.-argIndex code))

     (.equals (.-tagName code) "Math")
     (.math gen (.-op code) (.-opType code))

     (.equals (.-tagName code) "GetField")
     (.getField gen (.-owner code) (.-name code) (.-fieldType code))

     (.equals (.-tagName code) "PutField")
     (.putField gen (.-owner code) (.-name code) (.-fieldType code))

     (.equals (.-tagName code) "GetStaticField")
     (.getStatic gen (.-owner code) (.-name code) (.-resultType code))

     (.equals (.-tagName code) "InvokeStatic")
     (.invokeStatic gen (.-owner code) (.-method code))

     (.equals (.-tagName code) "InvokeVirtual")
     (.invokeVirtual gen (.-owner code) (.-method code))

     (.equals (.-tagName code) "InvokeConstructor")
     (.invokeConstructor gen (.-owner code) (.-method code))

     (.equals (.-tagName code) "New")
     (.newInstance gen (.-owner code))

     (.equals (.-tagName code) "Bool")
     (.push gen (.-boolValue code))

     (.equals (.-tagName code) "Int")
     (.push gen (.-intValue code))

     (.equals (.-tagName code) "String")
     (.push gen ^String (.-stringValue code))

     (.equals (.-tagName code) "Nil")
     (.visitInsn gen Opcodes/ACONST_NULL)

     (.equals (.-tagName code) "Dup")
     (.dup gen)

     (.equals (.-tagName code) "Pop")
     (.pop gen)

     ;; print
     (.equals (.-tagName code) "Print")
     (do
       (.dup gen)
       ;; Arguably it's a little hacky, but since those are std jvm types I doubt there will be name conflicts
       (.getStatic gen
                   (Type/getType "Ljava/lang/System;")
                   "out"
                   (Type/getType "Ljava/io/PrintStream;"))
       (.swap gen)
       (.invokeVirtual gen
                       (Type/getType "Ljava/io/PrintStream;")

                       (new org.objectweb.asm.commons.Method "println"
                            Type/VOID_TYPE
                            (let [arr (java.lang.reflect.Array/newInstance (class org.objectweb.asm.Type) 1) java.lang.Object]
                              (java.lang.reflect.Array/set arr 0 Type/INT_TYPE)
                              arr))
                     #_(org.objectweb.asm.commons.Method/getMethod "void println(int)")))

     (.equals (.-tagName code) "Return")
     (.returnValue gen)

     :else nil))

 (reflect/reflect  java.lang.reflect.Array )

 (defn lang.myGenerateCode [gen org.objectweb.asm.commons.GeneratorAdapter void]
   (lang.generateCode/invoke gen (lang.Code/Int 42))
   (lang.generateCode/invoke gen (lang.Code/Int 2))
   (lang.generateCode/invoke gen (lang.Code/Print))
   (lang.generateCode/invoke gen (lang.Code/MultInt))
   (lang.generateCode/invoke gen (lang.Code/Print))
   (lang.generateCode/invoke gen (lang.Code/Return)))


 (defn lang.generateCodeWithEnv [gen org.objectweb.asm.commons.GeneratorAdapter code lang.Code env java.util.Map java.util.Map]
   (cond
     (.equals (.-tagName code) "Label")
     (if (.containsKey env (.-stringValue code))
       (.mark
        gen
        (.get env (.-stringValue code)))

       (let [label (.newLabel gen) org.objectweb.asm.Label]
         (.mark
          gen
          (.get env (.-stringValue code)))
         (.put env (.-stringValue code) label)
         nil))

     (.equals (.-tagName code) "Jump")
     (if (.containsKey env (.-stringValue code))
       (.goTo
        gen
        (.get env (.-stringValue code)))

       (let [label (.newLabel gen) org.objectweb.asm.Label]
         (.goTo gen label)
         (.put env (.-stringValue code) label)
         nil))
     :else
     (lang.myGenerateCode/invoke gen))
   env)

 (defn lang.parseThatInt [s java.lang.String int]
   (java.lang.Integer/parseInt s))

 ;; this doesn't actually work because the type isn't resolved yet :(
 (defn lang.factorial [n int int]
   (if (= n 1)
     1
     (mult-int n (lang.factorial/invoke (sub-int n 1)))))

 )








(do

  (backend/make-fn-with-callback
   "lang2.MyAwesomeCode"
   #(lang.myGenerateCode/invoke %))

  [(lang2.MyAwesomeCode/invoke)



   (lang.factorial/invoke 5)]



  )
