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
              (into [:do] (interpose [:pop] (rest x)))
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


(defn log [& args]
  (apply prn args)
  (last args))



(defn replace-args [sexpr arg-names]
  (replace-sexprs sexpr (into {} (map vector arg-names (map (fn [i] (list 'arg i)) (range))))))


(defn process-defn-for-types [[_ fn-name types & body] env]

  (let [arg-names (mapv first (partition 2 (butlast types)))
        arg-types (mapv second (partition 2 (butlast types)))]
    (assoc-in env
              [:functions fn-name]
              {:type :fn
               :class-name (string/replace (name fn-name) "." "/")
               :arg-types arg-types
               :return-type (last types)
               :thing  (de-sexpr
                        (replace-args (replace-let-exprs
                                       (cons 'do  body))
                                      arg-names) )
               :code (type-checker/augment {:expr (de-sexpr
                                                   (replace-args (replace-let-exprs
                                                                  (cons 'do  body))
                                                                 arg-names) )

                                            :env (assoc env :arg-types arg-types)})})))



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
     (.GeneratorAdapter/push gen (.-boolValue code))

     (.String/equals (.-tagName code) "Int")
     (.GeneratorAdapter/push gen (.-intValue code))

     (.String/equals (.-tagName code) "String")
     (.GeneratorAdapter/push gen ^String (.-stringValue code))

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

     ;; Can we do nil here??
     ;; It isn't actually void. But maybe that is okay?
     :else nil))


 (defn lang.myGenerateCode [gen org.objectweb.asm.commons.GeneratorAdapter void]
   (lang.generateCode/invoke gen (lang.Code/Int 42))
   (lang.generateCode/invoke gen (lang.Code/Return)))


 (defn lang.generateCodeWithEnv [gen org.objectweb.asm.commons.GeneratorAdapter code lang.Code env java.util.Map java.util.Map]
   (cond
     (.String/equals (.-tagName code) "Label")
     (if (.java.util.Map/containsKey env (.-stringValue code))
       (.org.objectweb.asm.commons.GeneratorAdapter/mark
        gen
        (.java.util.Map/get env (.-stringValue code)))

       (let [label (.org.objectweb.asm.commons.GeneratorAdapter/newLabel gen) org.objectweb.asm.Label]
         (.org.objectweb.asm.commons.GeneratorAdapter/mark
          gen
          (.java.util.Map/get env (.-stringValue code)))
         (.java.util.Map/put env (.-stringValue code) label)))

     (.String/equals (.-tagName code) "Jump")
     (if (.java.util.Map/containsKey env (.-stringValue code))
       (.org.objectweb.asm.commons.GeneratorAdapter/goTo
        gen
        (.java.util.Map/get env (.-stringValue code)))

       (let [label (.org.objectweb.asm.commons.GeneratorAdapter/newLabel gen) org.objectweb.asm.Label]
         (.org.objectweb.asm.commons.GeneratorAdapter/goTo gen label)
         (.java.util.Map/put env (.-stringValue code) label)))
     :else
     (lang.myGenerateCode/invoke gen))
   env)

 (defn lang.parseThatInt [s java.lang.String int]
   (java.lang.Integer/parseInt s))

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




