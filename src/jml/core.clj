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


(defn array-type [expr-type]
  (Type/getType
   (format "[L%s;" (string/replace (name expr-type) "." "/"))))

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
         (array-type expr-type))

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


(declare desugar-foreach)

(defn replace-let-exprs [expr]
  (walk/postwalk
   (fn [x]
     (cond
       (and (seq? x) (symbol? (first x)) (= (first x) 'foreach))
       (desugar-foreach x)
       (and (seq? x) (symbol? (first x)) (=  (first x) 'let))
       (desugar-let x)
       :else x))
   expr))

(defn desugar-foreach [[_ [x xs ty] & body]]
  (let [loop-index-sym (gensym "i")
        arr-sym (gensym "arr")
        loop-pred (list '<  loop-index-sym (list 'array-length arr-sym))]
    (replace-let-exprs
     (concat '(let)  [[loop-index-sym 0 'int
                       arr-sym xs (symbol "Array" (name ty))                   ]]
             (list (concat (list  'while loop-pred)

                           (list (concat '(let) [[x (list 'array-load arr-sym loop-index-sym) ty]]
                                         (concat body
                                                 [(list 'set! loop-index-sym
                                                        (list 'plus-int loop-index-sym 1))])))))))))

#_(desugar-foreach '(foreach [y (new-array int 10) int]
                             (print y)))



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

(defn get-ir-multiple* [s-exprs]
  (reduce (fn [env s-expr]
            (case (first s-expr)
              defn (let [[_ fn-name & _] s-expr
                         env (process-defn-for-types s-expr env)
                         function (get-in env [:functions fn-name])]
                     (assoc-in env [:functions-with-ir fn-name]
                               (-> function
                                   (update :arg-types (fn [arg-types]
                                                        (map #(resolve-type (de-alias-type %  env)) arg-types)))
                                   (update :return-type #(resolve-type (de-alias-type % env)))
                                   (update :code (fn [code] (concat (linearize* code) [[:return]]))))))
              defenum (process-enum-for-types s-expr (update env :enums conj (process-enum s-expr env)))
              defalias (process-alias s-expr env)
              (throw (ex-info "unhandled" {:s-expr s-expr}))))
          {:functions {}
           :data-types {}
           :aliases {}}
          s-exprs))

;; Only works one jml block
;; We could name them
(def raw-source (atom nil))

(defmacro jml [& s-exprs]
  (reset! raw-source s-exprs)
  (run-multiple* s-exprs))





