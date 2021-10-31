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
      (and (symbol? expr-type) (= "Array" (namespace expr-type)))
      (case (name expr-type)
        "int" (Type/getType "[I")
        "byte" (Type/getType "[B")
        ;; TODO add other primitive types
        (Type/getType (format "[L%s;" (string/replace (name expr-type) "." "/"))))

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

(defn negate-op-type [op]
  (cond
    (= op GeneratorAdapter/EQ) GeneratorAdapter/NE
    (= op GeneratorAdapter/NE) GeneratorAdapter/EQ
    (= op GeneratorAdapter/GT) GeneratorAdapter/LE
    (= op GeneratorAdapter/LT) GeneratorAdapter/GE
    (= op GeneratorAdapter/GE) GeneratorAdapter/LT
    (= op GeneratorAdapter/LE) GeneratorAdapter/GT
    (= op :unknown) :unknown
    :else (throw (ex-info "Unexpected op to negate " {:op op}))))

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



(defn desugar-while [[tag _ pred & body :as node]]
  (if (= tag :while)
    (let [loop-label (gensym "while-loop_")
          exit-label (gensym "while-exit_")
          {:keys [compare-op compare-type
                  arg1 arg2]}     (resolve-cmp-op pred)]
      (concat [[:label {:value loop-label}]
               arg1
               arg2
               [:jump-cmp {:value exit-label
                             ;; doing (not compare-op) because we really want :jump-not-equal, not :jump-equal
                             :compare-op (negate-op-type compare-op)
                             :compare-type compare-type}]]
              [(-> (interpose [:pop] body)
                   vec
                   ;; TODO reconsider this, I'm assuming purely side effecting while loops,
                   ;; i.e. we need to pop last expression from the stack as we won't be consuming it in any way
                   (conj [:pop]))]
              [[:jump {:value loop-label}]
               [:label {:value exit-label}]
               [:nil]]))
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
                      (list 'store-local {:name (name k) :local-type t}
                            ;; For let's with multiple binding, we need to replace refences to other locals inside v
                            (replace-sexprs v bindings-map))) bindings)
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
               (and (seq? x) (= (first x) 'set!))
               ;; piggybacking on the fact that desugar-let replaced all references to local bindings with (load-local {:name "x"})              ;; we can thus just grab the {:name "x"} part.
               [:store-local    (second (second x))  (first (rest (rest x)))]
               (and (seq? x) (symbol? (first x)) (= (first x) 'cond))
               (de-sexpr (expand-cond x))
               (and (seq? x) (symbol? (first x)) (= (first x) 'do))
               (into [:do] (interpose [:pop] (rest x)))
               (and (seq? x) (symbol? (first x)) (= (first x) 'array-store))
               (into [:array-store {}]  (rest x))
               (and (seq? x) (symbol? (first x)) (= (first x) 'new-array))
               (into [:new-array {:owner (second x)}] (rest (rest x)))
               (and (seq? x) (symbol? (first x)) (= (first x) 'array-load))
               (into [:array-load {}]  (rest x))
               (and (seq? x) (symbol? (first x)) (string/ends-with? (name (first x)) ".")) ;; constructor dot syntax
               (into [:invoke-constructor {:owner (symbol  (subs (name (first x)) 0 (dec (count (name (first  x))))))}]
                     (concat [[:new {:owner (symbol (subs (name (first x)) 0 (dec (count (name (first x))))))}]]
                             (rest x)))
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

          (= op :while)
          (into [] (mapcat linearize*  (desugar-while code)))

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

(defn de-alias-type [ty {:keys [aliases] :as env}]
  (if (and (symbol? ty) (=  (namespace ty) "Array"))
    (if-let [array-item-ty (get aliases (symbol (name ty)))]
      (symbol "Array" (name array-item-ty))
      ty)
    (get aliases ty ty)))


(defn strip-interop-dot [sym]
  (let [ns (namespace sym)
        n (name sym)]
    (symbol ns (subs n 0 (dec (count n))))))

(defn add-interop-dot [sym]
  (let [ns (namespace sym)
        n (name sym)]
    (symbol ns (str n "."))))


(defn replace-aliases [sexpr {:keys [aliases] :as env}]
  (walk/postwalk (fn [x]
                   ;; TODO this should probably be merged with de-alias-type into single function
                   (cond (not (symbol? x)) x
                         (and (symbol? x) (string/ends-with? (name x) "."))
                         (add-interop-dot (de-alias-type (strip-interop-dot x) env))
                         (simple-symbol? x) (de-alias-type x env)
                         (contains? aliases (symbol (namespace x)))
                         (symbol (name (get aliases (symbol (namespace x)))) (name x))
                         :else (de-alias-type x env)))
                 sexpr))


(defn enrich-env-with-local-types [expr env]
  (assoc env :local-types
         (->> (tree-seq vector? #(into [] (rest (rest %))) expr)
              (filter (fn [e]
                        (when (not (seqable? e))
                          (throw (ex-info (format
                                           "SyntaxError? - Expected [:op props children] structured expr, but found: '%s'"
                                           e)
                                          {:parent-expr expr
                                           :expr e
                                           :from "enrich-env-with-local-types"
                                           :env env})))
                        (= :store-local (first e))))
              (map (fn [[_ props & _]]
                     (when (:local-type props)
                       [(:name props) (de-alias-type  (:local-type props) env)])))
              (remove nil?)
              (into {}))))


(defn process-defn-for-types [[_ fn-name types & body] env]
  (let [arg-names (mapv first (partition 2 (butlast types)))
        arg-types (mapv (fn [[_ ty]] (de-alias-type ty env))  (partition 2 (butlast types)))
        return-type (de-alias-type (last types) env)
        env (assoc-in env
                      [:functions fn-name]
                      {:type :fn
                       :class-name (string/replace (name fn-name) "." "/")
                       :arg-types arg-types
                       :return-type return-type})
        expr (-> (cons 'do body)
                 replace-let-exprs
                 (replace-args arg-names)
                 (replace-aliases  env)
                 de-sexpr)
        env (enrich-env-with-local-types expr env)]
    (assoc-in env [:functions fn-name :code]
              (time (type-checker/augment {:expr expr
                                           :env (assoc env :arg-types arg-types)
                                           :type return-type})))))



(defn process-enum [[_ enum-name & variants] env]
  {:class-name (string/replace (str enum-name) "." "/")
   :variants
   (mapv (fn [variant]
           (if (symbol? variant)
             {:name (str variant)
              :fields []}
             {:name (str (first variant))
              :fields (mapv (fn [[name type]]
                              {:name (str name) :type  (resolve-type (de-alias-type  type env))})
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
                                      (map (fn [[n t]] [(str n) (de-alias-type t env)]))
                                      (map vec)
                                      (into {"tagName" 'java.lang.String}))})
      (update :functions merge (->> variants
                                    (mapv (fn [variant]
                                            (if (symbol? variant)
                                              [variant {:arg-types []
                                                        :return-type enum-name}]
                                              [(first variant) {:arg-types  (mapv (fn [[_ ty]] (de-alias-type ty env))
                                                                                  (partition 2 (rest variant)))
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
                                          (update :arg-types (fn [arg-types]
                                                               (map #(resolve-type (de-alias-type %  env)) arg-types)))
                                          (update :return-type #(resolve-type (de-alias-type % env)))
                                          (update :code (fn [code] (concat (linearize* code) [[:return]])))))
                     env)
              defenum (do (backend/make-enum (process-enum s-expr env))
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
 (defn lang.invokeConstructorExample [java.lang.Integer]
   (java.lang.Integer. 2)))

(jml
 (defn lang.printer [x int void]
   (print x)
   (print x )))

(lang.printer/invoke 42)

(jml
 (defalias Type org.objectweb.asm.Type)
 (defalias Class java.lang.Class)

 (defn lang.createArray [i int int]
   (let [a (new-array int 2) Array/int
         b 2 int]
     (array-store a 0 1)
     (while (< (array-load a 0) i)
        242
       (array-store  a  0 (print  (mult-int (array-load a 0) 2))))

     (array-load a 0))))

#_(lang.createArray/invoke 15)


(jml
 (defalias Type org.objectweb.asm.Type)
 (defalias Class java.lang.Class)

 (defn lang.doubleNTimes[n int int]
   (let [x 1 int
         i 0 int]
     (while (<= i n)
       (print i)
       (store-local {:name "i"
                     :local-type int} (plus-int i 1))
       (store-local {:name "x"
                     :local-type int} (mult-int x 2)))
     x)))

#_(lang.doubleNTimes/invoke 10)

#_backend/fn-desc

(jml
 (defn lang.loopTest [ n int int]
   (let [i 0 int]

     (while (< i n)
       (print i)

       (store-local {:name "i"
                     :local-type int}
                    (plus-int i 1)) #_
       (let [i (plus-int i 1) int]))
     i)))


(lang.loopTest/invoke 5)


(jml
 (defn lang.setLoopTest [ n int int]
   (let [i 0 int]

     (while (< i n)
       (print i)
       (set! i (plus-int i 1)))
     i)))

(lang.setLoopTest/invoke 10)




;;
(jml
 (defalias GeneratorAdapter org.objectweb.asm.commons.GeneratorAdapter)
 (defalias Type org.objectweb.asm.Type)
 (defalias Opcodes org.objectweb.asm.Opcodes)
 (defalias Class java.lang.Class)

 (defn lang.printCode [gen org.objectweb.asm.commons.GeneratorAdapter void]
   (.push gen 42)

   (.dup gen)
   (.getStatic gen
               (Type/getType (java.lang.Class/forName "java.lang.System"))
               "out"
               (Type/getType  (.getType (.getDeclaredField (java.lang.Class/forName "java.lang.System")
                                                           "out"))))
   (.swap gen)
   (.invokeVirtual gen (Type/getType
                        (.getType (.getDeclaredField (Class/forName "java.lang.System")
                                                     "out")))
                   (let [arg-types (new-array org.objectweb.asm.Type 1) Array/org.objectweb.asm.Type]
                     (array-store arg-types 0 Type/INT_TYPE)
                     (org.objectweb.asm.commons.Method. "println" Type/VOID_TYPE arg-types)))
   (.returnValue gen)))

;;  org/objectweb/asm/commons/Method.<init>:(Ljava/lang/String;Lorg/objectweb/asm/Type;[Lorg/objectweb/asm/Type;)V
;;(Ljava/lang/String;Lorg/objectweb/asm/Type;[Lorg.objectweb.asm.Type)V"
(backend/make-fn-with-callback
 "lang2.MyAwesomeCode"
 #(lang.printCode/invoke %))


(lang2.MyAwesomeCode/invoke)

(jml


 (defalias GeneratorAdapter org.objectweb.asm.commons.GeneratorAdapter)
 (defalias ClassWriter  org.objectweb.asm.ClassWriter)
 (defalias Type org.objectweb.asm.Type)
 (defalias Label org.objectweb.asm.Label)
 (defalias Opcodes org.objectweb.asm.Opcodes)
 (defalias Class java.lang.Class)
 (defalias Method org.objectweb.asm.commons.Method)
 (defalias String java.lang.String)
 (defalias Map java.util.Map)

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
         opType Type)
   (GetStatic owner Type
              name String
              resultType Type)
   (InvokeStatic owner Type
                 method Method)
   (InvokeVirtual owner Type
                  method Method)
   (InvokeConstructor owner Type
                      method Method)
   (New owner Type
        method Method)
   (Bool boolValue boolean)
   (Int intValue int)
   (String stringValue String)
   (GetField owner Type
             name String
             fieldType Type)
   (PutField owner Type
             name String
             fieldType Type)
   (Label stringValue String)
   (JumpNotEqual stringValue String
                 compareType Type)
   (JumpEqual stringValue String
              compareType Type)
   (JumpCmp stringValue String
            compareType Type
            compareOp int)
   (Jump stringValue String)

   (LocalMeta localObj int
              localType Type))

 (defn lang.generateCode [gen GeneratorAdapter code lang.Code void]

   (cond

     (.equals (.-tagName code) "MultInt")
     (.math gen GeneratorAdapter/MUL org.objectweb.asm.Type/INT_TYPE)

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
     (do
       (.newInstance gen (.-owner code))
       (.dup gen))

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
       (.getStatic gen
                   (Type/getType (java.lang.Class/forName "java.lang.System"))
                   "out"
                   (Type/getType  (.getType (.getDeclaredField (java.lang.Class/forName "java.lang.System")
                                                               "out"))))
       (.swap gen)
       (.invokeVirtual gen (Type/getType
                            (.getType (.getDeclaredField (Class/forName "java.lang.System")
                                                         "out")))
                       (let [arg-types (new-array Type 1) Array/Type]
                         (array-store arg-types 0 Type/INT_TYPE)
                         (Method. "println" Type/VOID_TYPE arg-types))))
     (.equals (.-tagName code) "Return")
     (.returnValue gen)

     :else nil))



 (defn lang.generateCodeWithEnv
   [gen GeneratorAdapter code lang.Code env Map Map]
   (cond
     (.equals (.-tagName code) "Label")
     (if (.containsKey env (.-stringValue code))
       (let [label (.get env (.-stringValue code)) Label]
         (.mark gen label))

       (let [label (.newLabel gen) Label]
         (.mark gen label)
         (.put env (.-stringValue code) label)
         nil))

     (.equals (.-tagName code) "JumpNotEqual")
     (if (.containsKey env (.-stringValue code))
       (let [label (.get env (.-stringValue code)) Label]
         (.ifCmp gen (.-compareType code) GeneratorAdapter/NE label)
         nil)
       (let [label (.newLabel gen) Label]
         (.ifCmp gen (.-compareType code) GeneratorAdapter/NE label)
         (.put env (.-stringValue code) label)
         nil))

     (.equals (.-tagName code) "JumpEqual")
     (if (.containsKey env (.-stringValue code))
       (let [label (.get env (.-stringValue code)) Label]
         (.ifCmp gen (.-compareType code) GeneratorAdapter/EQ label)
         nil)
       (let [label (.newLabel gen) Label]
         (.ifCmp gen (.-compareType code) GeneratorAdapter/EQ label)
         (.put env (.-stringValue code) label)
         nil))


     (.equals (.-tagName code) "JumpCompare")
     (if (.containsKey env (.-stringValue code))
       (let [label (.get env (.-stringValue code)) Label]
         (.ifCmp gen (.-compareType code) (.-compareOp code) label)
         nil)
       (let [label (.newLabel gen) Label]
         (.ifCmp gen (.-compareType code) (.-compareOp code) label)
         (.put env (.-stringValue code) label)
         nil))


     (.equals (.-tagName code) "Jump")
     (if (.containsKey env (.-stringValue code))
       (let [label (.get env (.-stringValue code)) Label]
         (.goTo gen label))
       (let [label (.newLabel gen) Label]
         (.goTo gen label)
         (.put env (.-stringValue code) label)
         nil))

     (.equals (.-tagName code) "StoreLocal")
     (if (.containsKey env (.concat  "local-" (.-name code)))
       (let [local (.get env (.concat  "local-" (.-name code))) lang.Code]
         (.storeLocal gen (.-localObj local) (.-localType local))
         (lang.generateCode/invoke gen (lang.Code/Nil))
         nil)
       (let [local2 (.newLocal gen (.-localType code)) int]
         (.storeLocal gen local2 (.-localType code))
         (lang.generateCode/invoke gen (lang.Code/Nil))
         (.put env (.concat "local-" (.-name code)) (lang.Code/LocalMeta local2 (.-localType code)))
         nil))

     (.equals (.-tagName code) "LoadLocal")
     (if (.containsKey env (.concat  "local-" (.-name code)))
       (let [local (.get env (.concat  "local-" (.-name code))) lang.Code]
         (.loadLocal gen (.-localObj local) (.-localType local))
         nil)
       ;; Make this error
       nil)


     :else
     (lang.generateCode/invoke gen code))
   env)




 (defn lang.generateDefaultConstructor [writer ClassWriter void]
   (let [signature nil String
         exceptions nil Array/Type
         gen (GeneratorAdapter.
              Opcodes/ACC_PUBLIC
              (Method/getMethod "void <init>()")
              signature
              exceptions
              writer)
         GeneratorAdapter]
     (.visitCode gen)
     (.loadThis gen)
     (.invokeConstructor gen (Type/getType (Class/forName "java.lang.Object"))  (Method/getMethod "void <init>()"))
     (.returnValue gen)
     (.endMethod gen)))

 (defn lang.initializeClass [writer ClassWriter className String void]
   (let [signature nil String
         interfaces nil Array/String]
     (.visit writer Opcodes/V1_8 Opcodes/ACC_PUBLIC className signature "java/lang/Object" interfaces)))


 ;; (lang.generateAllTheCode gen code)
 ;; Make it loop over the array of code and generate with env

 (defn lang.generateAllTheCode [gen GeneratorAdapter code Array/lang.Code void]
   (let [i 0 int
         env (java.util.HashMap.) Map]
     ;; TODO it would be nice to be able to call (.-length code)
     ;; but our get-field logic only really works for enum fields fields for 2 reasons
     ;; - type-checker looks  up field types in env using  (get-in env [:data-types owner field]) and that's only populated for our enum fields
     ;; - we could use something like type-checker/get-static-field-type as a backup for that lookup, but clojure.reflect/reflect     ;;   doesn't find `length` as a field of java.lang.Array
     ;; for example, below expr returns empty list
     ;; (type-checker/get-methods-jvm (class (into-array [1])) "length" [] [])
     ;; below fails (and we're not filtering out static members as far as I can see)
     ;; (type-checker/get-static-field-type  (class (into-array [1])) "length")
     (while (< i (java.lang.reflect.Array/getLength code))
       (lang.generateCodeWithEnv/invoke gen (array-load code i) env)
       (set! i (plus-int i 1)))))


 (defn lang.generateInvokeMethod [writer ClassWriter
                                  code Array/lang.Code
                                  returnType Type
                                  argTypes Array/Type
                                  void]
   (let [method (Method. "invoke" returnType argTypes) Method
         signature nil String
         exceptions nil Array/Type
         gen (GeneratorAdapter.
              (plus-int Opcodes/ACC_PUBLIC Opcodes/ACC_STATIC)
              method signature exceptions writer) GeneratorAdapter]
     (lang.generateAllTheCode/invoke gen code)
     (.endMethod gen)))




 (defn lang.defineClass [writer ClassWriter class-name String java.lang.Class]
     (let [byteArray (.toByteArray writer) Array/byte
           aThing nil java.lang.Object]
       (.defineClass (clojure.lang.DynamicClassLoader.)
                     (.replace class-name "/" ".")
                     byteArray
                     aThing)))


 (defn lang.makeFn [class-name String
                    code Array/lang.Code
                    returnType Type
                    argTypes Array/Type
                    java.lang.Class]
   (let [writer (ClassWriter. (plus-int ClassWriter/COMPUTE_FRAMES ClassWriter/COMPUTE_MAXS)) ClassWriter]
     (lang.initializeClass/invoke writer class-name)
     (lang.generateDefaultConstructor/invoke writer)
     (lang.generateInvokeMethod/invoke writer code returnType argTypes)
     (.visitEnd writer)
     (lang.defineClass/invoke writer class-name)))


 (defn lang.testGeneratingCode [gen GeneratorAdapter void]
   (lang.generateCode/invoke gen (lang.Code/Int 42))
   (lang.generateCode/invoke gen (lang.Code/Int 4))
   (lang.generateCode/invoke gen (lang.Code/Print))
   (lang.generateCode/invoke gen (lang.Code/MultInt))
   (lang.generateCode/invoke gen (lang.Code/Print))
   (lang.generateCode/invoke gen (lang.Code/Return)))

 (defn lang.parseThatInt [s String int]
   (java.lang.Integer/parseInt s))

 ;; this doesn't actually work because the type isn't resolved yet :(
 (defn lang.factorial [n int int]
   (if (= n 1)
     1
     (mult-int n (lang.factorial/invoke (sub-int n 1))))))





(do
  (lang.makeFn/invoke "lang/TestClassFn"
                        (into-array lang.Code  [(lang.Code/Int 42)
                                                (lang.Code/Arg 0)
                                                (lang.Code/Print)
                                                (lang.Code/MultInt)
                                                (lang.Code/Print)
                                                (lang.Code/Return)])
                        Type/INT_TYPE
                        (into-array Type [Type/INT_TYPE]))


    (lang.TestClassFn/invoke 5))

(do

  (backend/make-fn-with-callback
   "lang2.MyAwesomeCode"
   #(lang.testGeneratingCode/invoke %))

  [(lang2.MyAwesomeCode/invoke)



   (lang.factorial/invoke 5)]



  )
