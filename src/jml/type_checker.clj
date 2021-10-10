(ns jml.type-checker
  (:require [clojure.reflect :as reflect]
            [clojure.string :as string])
  (:import [org.objectweb.asm Opcodes Type]
           [org.objectweb.asm.commons Method]))


(declare check)
(declare synth)
(declare augment)

(defn matches-type [actual-type {:keys [type expr env]}]
  (if (= actual-type type)
    env
    (throw (ex-info "Type error" {:expected type
                                  :found actual-type
                                  :expr expr
                                  :env env}))))

(defn symbol->type [sym]
  (case sym
    void Type/VOID_TYPE
    int Type/INT_TYPE
    long Type/LONG_TYPE
    boolean Type/BOOLEAN_TYPE
    (if (string/includes? (name sym) "<>")
      (Type/getType (format "[L%s;"  (string/replace
                                     (string/replace  (name sym) "<>" "")
                                     "." "/")))
      (Type/getType ^Class  (Class/forName (name sym))))))

(defn to-type-array [syms]
  (into-array Type (map symbol->type syms)))


(defn array-type-equiv? [target actual]
  (and (string/includes? (name actual) "<>")
       (= (namespace target) "Array")
       (= (str (name target) "<>")
          (name actual))))


(defn jvm-type-equiv? [[target actual]]
  ;; Checking primatives and subclassing relation
  (or (= target actual)
      (array-type-equiv?  actual target)
      ;; Ugly hack to autocase
      (= actual 'java.lang.Object)
      (try
        (.isAssignableFrom (Class/forName (name target)) (Class/forName (name actual)))
        (catch Exception e false))))

(defn create-method [{:keys [name arg-types return-type]}]
  (Method. name (symbol->type return-type) (to-type-array arg-types)))

(defn get-methods-jvm [klass method-name method-args ancestors]
  (->> (reflect/reflect klass :ancestors ancestors)
       :members
       (filter (comp #{(symbol method-name)} :name))
       (filter (fn [{:keys [parameter-types]}]
                 (= (count parameter-types) (count method-args))))
       (filter (fn [{:keys [parameter-types]}]
                 (or (nil? method-args)
                     (every? jvm-type-equiv? (map vector parameter-types method-args)))))))

(defn get-class-ancestors [klass]
  ;; keeps calling `klass`'s getSuperclass until we get all the way to java.lang.Object
  ;; needed because it turns out just passing `true` to (reflect/reflect klass :ancestors `true`)
  ;; doesn't really do anything, one needs to pass a list of ancestor classes
  (take-while #(not= % Object) (iterate #(.getSuperclass %) klass)))


(defn get-method-info-jvm
  ([klass method-name method-args]
   (get-method-info-jvm klass method-name method-args false))
  ([klass method-name method-args ancestors]
   (let [methods (get-methods-jvm klass method-name method-args ancestors)
         methods (if (and (empty? methods) (false? ancestors))
                   (get-methods-jvm klass method-name method-args (get-class-ancestors klass))
                   methods)
         _ (when-not (= 1 (count methods))
             (throw (ex-info "Method overloaded. Need to be more or less specific"
                             {:class (.getName klass)
                              :method-name method-name
                              :methods methods
                              :method-args method-args
                              :ancestors ancestors
                              :available-methods
                              (->> (reflect/reflect klass :ancestors ancestors)
                                   :members
                                  (filter (comp #{(symbol method-name)} :name)))})))
         {:keys [return-type parameter-types declaring-class]} (first methods)
         is-ctor? (not return-type)
         return-type (or return-type declaring-class)]
     {:return-type return-type
      :parameter-types parameter-types
      :method (create-method {:name (if is-ctor? "<init>"  method-name)
                              :arg-types parameter-types
                              :return-type (if is-ctor? 'void return-type)})})))



(defn get-static-field-type [klass name]
  (let [fields
        (->> (reflect/reflect klass :ancestors true)
             :members
             (filter (comp #{name} :name)))
        _ (when-not (= (count fields) 1)
            (throw (ex-info "Could not find static field"
                            {:class-name (.getName klass)
                             :field-name name})))]
    (:type (first fields))))



(defn get-field-type [owner field env]
  (if-let [val (get-in env [:data-types owner field])]
    val
    (throw (ex-info "Field not found" {:owner owner :field field :env env}))))

(defn augment-then-synth [context]
  (synth (assoc context :expr (augment (dissoc context :type)))))




(defn check [{:keys [expr type env] :as context}]
  (case (first expr)
    :int (matches-type 'int context)
    :bool (matches-type 'boolean context)
    :string (matches-type 'java.lang.String context)
    :dup (matches-type 'void context)
    :new (matches-type (:owner (second expr)) context)
    :new-array (matches-type (synth context) context)
    :array-store (do ;; check if the first argument's type if Array of right type (:owner (second expr))
                   (matches-type (synth (assoc context :expr (nth expr 2)))
                                 (assoc context :type
                                        (symbol "Array"
                                                (name  (:owner (second expr))))))
                   (matches-type 'void context))
    :array-load (do  ;; check if the first argument's type if Array of right type (:owner (second expr))
                  (matches-type (synth (assoc context :expr (nth expr 2)))
                                (assoc context :type
                                       (symbol "Array"
                                               (name  (:owner (second expr))))))
                  (matches-type (:owner (second expr)) context))

    :plus-int (do
                ;; TODO: Do we know the type of the subexpressions here?
                (matches-type (augment-then-synth (assoc context :expr (nth expr 2)))
                              (assoc context :expr (nth expr 2)))
                (matches-type (augment-then-synth (assoc context :expr (nth expr 3)))
                              (assoc context :expr (nth expr 3)))
                (matches-type 'int context))

    :mult-int (do
                ;; TODO: Do we know the type of the subexpressions here?
                (matches-type (augment-then-synth (assoc context :expr (nth expr 2)))
                              (assoc context :expr (nth expr 2)))
                (matches-type (augment-then-synth (assoc context :expr (nth expr 3)))
                              (assoc context :expr (nth expr 3)))
                (matches-type 'int context))
    :sub-int (do
               ;; TODO: Do we know the type of the subexpressions here?
               (matches-type (augment-then-synth (assoc context :expr (nth expr 2)))
                             (assoc context :expr (nth expr 2)))
               (matches-type (augment-then-synth (assoc context :expr (nth expr 3)))
                             (assoc context :expr (nth expr 3)))
               (matches-type 'int context))
    :arg (matches-type (get-in env [:arg-types (:value (second expr))]) context)
    :if (do
          (let [pred (augment-then-synth (assoc  (dissoc context :type) :expr (nth expr 2)))
                branch1 (augment-then-synth (assoc context :expr (nth expr 3)))
                branch2 (augment-then-synth (assoc context :expr (nth expr 4)))]
            (matches-type pred (assoc context :expr (nth expr 2) :type 'boolean))
            (matches-type branch1 (assoc context :expr (nth expr 3)))
            (matches-type branch2 (assoc context :expr (nth expr 4)))))

    :while    
    (let [[_ _ pred & body] expr
          pred (augment-then-synth (assoc  (dissoc context :type) :expr pred))
          body-last  (augment-then-synth (assoc context :expr (last body)))]

      (matches-type pred (assoc context :expr (nth expr 2) :type 'boolean)))
    := (matches-type 'boolean context)
    :> (matches-type 'boolean context)
    :< (matches-type 'boolean context)
    :>= (matches-type 'boolean context)
    :<= (matches-type 'boolean context)
    :invoke-static (do
                     (matches-type (:return-type (second expr)) context)
                     (doseq [[type expr] (map vector (:arg-types (second expr)) (rest (rest expr)))]
                       (check (assoc context :expr expr :type type))))
    :invoke-virtual (do
                      (matches-type (:return-type (second expr)) context)
                      (check (assoc context :expr (nth expr 2) :type (:owner (second expr))))
                      (doseq [[type expr] (map vector (:arg-types (second expr)) (rest (rest (rest expr))))]
                        (check (assoc context :expr expr :type type))))
    :invoke-constructor (do
                          (matches-type (:owner (second expr)) context)
                          (check (assoc context :expr (nth expr 2) :type (:owner (second expr))))
                          (doseq [[type expr] (map vector (:arg-types (second expr)) (rest (rest expr)))]
                            (check (assoc context :expr expr :type type))))

    :get-field (matches-type (:field-type (second expr)) context)
    :get-static-field (matches-type (:field-type (second expr)) context)
    :do (matches-type (augment-then-synth (assoc context :expr (last expr))) context)
    :nil (matches-type 'void context)
    :pop (matches-type 'void context)
    :print (matches-type 'void context)
    ;; Need to actually be augmenting env
    :store-local (matches-type 'void context)
    :load-local (matches-type (synth context) context)
    (throw (ex-info "No matching check" {:expr expr :type type :env env}))))



(defn synth [{:keys [expr env]}]
  (case (first expr)
    :int 'int
    :bool 'boolean
    :string 'java.lang.String
    :sub-int 'int
    :mult-int 'int
    :plus-int 'int
    :new (:owner (second expr))
    :dup 'void
    :new-array (symbol "Array" (name (:owner (second expr))))
    :array-store 'void
    :array-load  (:owner (second expr))
    :arg (get-in env [:arg-types (:value (second expr))])
    :get-field (get-field-type (:owner (second expr)) (:name (second expr)) env)
    :invoke-virtual (if-let [return-type (:return-type (second expr))]
                      return-type
                      (throw (ex-info "Can't synth invoke virtual" {:expr expr :env env})))

    :invoke-static (if-let [return-type (:return-type (second expr))]
                     return-type
                     (throw (ex-info "Can't synth invoke static" {:expr expr :env env})))
    :invoke-constructor (:owner (second expr))
    :get-static-field (get-static-field-type (Class/forName (name (:owner (second expr)))) (symbol (:name (second expr))))
    ;; Fix by looking in env
    :load-local (get-in env [:local-types  (:name (second expr))])
    := 'boolean
    :> 'boolean
    :< 'boolean
    :>= 'boolean
    :<= 'boolean
    :do (augment-then-synth {:expr (last expr) :env env})
    :nil 'void
    ;; Is this right?
    :if (synth {:expr (last expr) :env env})
    :while 'void
    :print 'void
    :pop 'void
    :store-local 'void
    (throw (ex-info "No matching synth" {:expr expr :env env}))))





(defn get-method-info [{:keys [env] :as context} owner-name method-name args ]

  (if-let [{:keys [return-type arg-types]} (get-in env [:functions owner-name])]
    {:method (create-method {:name method-name
                             :arg-types arg-types
                             :return-type return-type})
     :return-type return-type}
    ;; TODO: get-method-info-jvm

    (let [augmented-args (mapv #(augment (assoc context :expr %)) args)
          arg-types (mapv #(synth (assoc context :expr %)) augmented-args)]
      (try

        (get-method-info-jvm (Class/forName (name owner-name))
                             method-name
                             arg-types)
        (catch Exception e
          (throw (ex-info "Can't find method info" {:context context
                                                    :owner-name owner-name
                                                    :method-name method-name
                                                    :args args
                                                    :arg-types arg-types}
                          e)))))))






(defn remove-dot [x]
  (if (string/starts-with? x ".")
    (subs x 1)
    x))

(defn wrap-void-types [expr]
  (if (= (get (second expr) :return-type) 'void)
    [:do expr [:nil]]
    expr))

(defn augment-sub-expr [{:keys [expr env type] :as parent-context}]
  (fn [expr]
    (let [result
          (augment (assoc (dissoc parent-context :type) :expr expr))]
      (check {:expr result
              :env env
              :type (synth {:expr result :env env})})
      result)))

(defn augment [{:keys [expr env type] :as context}]

  (when (and type (not= (first expr) :do))
    (check context))

  (case (first expr)
    :array-store (let [[_ attrs arr idx val] expr
                       item-type (synth (assoc context :expr arr))]
                   (into [:array-store {:owner (symbol (name item-type))}]
                         (mapv (augment-sub-expr context) [arr idx val])))
    :array-load  (let [[_ attrs arr idx] expr
                       item-type (synth (assoc context :expr arr))]
                   (into [:array-load {:owner (symbol (name item-type))}]
                         (mapv (augment-sub-expr context) [arr idx])))
    :get-field (let [[_ attrs child] expr]
                 (let [child-type (synth (assoc context :expr child))
                       field-type (synth (assoc context :expr (assoc-in expr [1 :owner] child-type)))]
                   (-> expr
                       (assoc-in [1 :owner] child-type)
                       (assoc-in [1 :field-type] field-type))))
    :invoke-virtual (let [[tag attrs & [this & args]] expr]
                      (if (:return-type attrs)
                        expr
                        (let [method-name (remove-dot (name (:name attrs)))
                              augmented-this (augment (assoc context :expr this))
                              this-type (synth (assoc context :expr augmented-this))
                              ;; Duplicated in get-method-info
                              augmented-args (mapv #(augment (assoc context :expr %)) args)
                              arg-types (mapv #(synth (assoc context :expr %)) augmented-args)
                              {:keys [method return-type]} (get-method-info context
                                                                            this-type
                                                                            method-name
                                                                            args)]
                          (wrap-void-types
                           (into [tag
                                  (-> attrs
                                      (assoc :owner this-type)
                                      ;; Should we do parameters from reflect?
                                      ;; Not sure
                                      ;; Could be object instead of string for example
                                      (assoc :arg-types arg-types)
                                      (assoc :return-type return-type)
                                      (assoc :name method-name)
                                      (assoc :method method))]
                                 (concat [augmented-this] augmented-args))))))
    :invoke-static (let [[tag attrs & [& args]] expr]
                     ;; We already augmented
                     (if (:return-type attrs)
                       expr
                       (let [this (symbol (namespace (:name attrs)))
                             method-name (name (:name attrs))
                             this-type this
                             augmented-args (mapv #(augment (assoc context :expr %)) args)
                             arg-types      (mapv #(synth (assoc context :expr %)) augmented-args)
                             {:keys [method return-type]} (get-method-info context
                                                                           this
                                                                           method-name
                                                                           args)]
                         (wrap-void-types
                          (into [tag
                                 (-> attrs
                                     (assoc :owner this-type)
                                     ;; Should we do parameters from reflect?
                                     ;; Not sure
                                     ;; Could be object instead of string for example
                                     (assoc :arg-types arg-types)
                                     (assoc :return-type return-type)
                                     (assoc :name method-name)
                                     (assoc :method method))]
                                augmented-args)))))
    :invoke-constructor (let [[tag attrs & [& args]] expr]
                          ;; We already augmented
                          (if (:return-type attrs)
                            expr
                            (let [this  (:owner attrs)
                                  method-name (name this)
                                  this-type this
                                  augmented-args (mapv #(augment (assoc context :expr %))  args)
                                  arg-types (mapv #(synth (assoc context :expr %)) augmented-args)
                                  {:keys [method return-type]} (get-method-info context
                                                                                this
                                                                                method-name
                                                                                (rest args))]
                              (wrap-void-types
                               (into [tag
                                      (-> attrs
                                          (assoc :owner this-type)
                                          ;; Should we do parameters from reflect?
                                          ;; Not sure
                                          ;; Could be object instead of string for example
                                          (assoc :arg-types arg-types)
                                          (assoc :return-type return-type)
                                          (assoc :name method-name)
                                          (assoc :method method))]
                                     augmented-args)))))

    :get-static-field (let [[tag attrs & [& args]] expr]
                        (if (:owner attrs)
                          expr
                          (let [owner (symbol (namespace (:name attrs)))
                                field-type (synth (assoc context :expr (-> expr
                                                                           (assoc-in [1 :owner] owner)
                                                                           (assoc-in [1 :name] (name (:name attrs))))))]
                            (-> expr
                                (assoc-in [1 :owner] owner)
                                (assoc-in [1 :field-type] field-type)
                                (assoc-in [1 :name] (name (:name attrs)))))))
    :do (do
          (let [result
                (into [(first expr) (second expr)]
                      ;; Need to reduce?
                      (mapv (augment-sub-expr context)
                            (rest (rest expr))))]

            (when type
              (check (assoc context :expr (last result))))
            result))
    :if  (into [(first expr) (second expr) (let [result
                                                 (augment (assoc (dissoc context :type) :expr (nth expr 2)))]
                                             (check {:expr result
                                                     :env env
                                                     :type 'boolean})
                                             result)]
               (mapv (augment-sub-expr context)
                     (rest (rest (rest expr)))))
    :while  (into [(first expr) (second expr) (let [result
                                                    (augment (assoc (dissoc context :type) :expr  (nth expr 2)))]
                                                (check {:expr result
                                                        :env env
                                                        :type 'boolean})
                                                result)]
                  (mapv (augment-sub-expr context)
                        (rest (rest (rest expr)))))
    :pop [:pop]

    (do
      (into [(first expr) (second expr)]
            (mapv (augment-sub-expr context)
                  (rest (rest expr)))))))













(comment




  (augment {:expr '[:invoke-static
                    {:name lang.myGenerateCode/invoke}
                    [:arg {:value 0}]]
            :env '{:arg-types [org.objectweb.asm.commons.GeneratorAdapter lang.Code java.util.Map]
                   :data-types {lang.Code {"stringValue" java.lang.String}}
                   :functions {lang.myGenerateCode {:arg-types [org.objectweb.asm.commons.GeneratorAdapter]
                                                    :return-type void}}}})


  ;; TODO
  ;; Run this code!

  (augment {:expr (:code example)
            :env '{:arg-types [org.objectweb.asm.commons.GeneratorAdapter lang.Code java.util.Map]
                   :data-types {lang.Code {"stringValue" java.lang.String
                                           "tagName" java.lang.String}}
                   :functions {lang.myGenerateCode {:arg-types [org.objectweb.asm.commons.GeneratorAdapter]
                                                    :return-type void}}}})


  (synth {:expr [:int 2]})

  ;; Error

  (check {:expr [:int 2] :type 'java.lang.String})

  ;; Error

  (check {:expr [:arg {:value 0}] :type 'java.lang.String :env {:arg-types ['int]}})


  (check {:expr [:arg {:value 0}] :type 'java.lang.String :env {:arg-types '[java.lang.String]}})
  (synth {:expr [:arg {:value 0}] :env {:arg-types ['java.lang.String]}})

  ;; Error

  (augment {:expr [:int 2] :type 'java.lang.String})

  (augment {:expr [:int 2] :type 'int})


  (synth {:expr '[:get-field {:name "stringValue" :owner lang.Code} [:arg {:value 0}]]
          :env '{:arg-types [lang.Code]
                 :data-types {lang.Code {"stringValue" java.lang.String}}}})

  (augment {:expr '[:get-field {:name "stringValue"} [:arg {:value 0}]]
            :env '{:arg-types [lang.Code]
                   :data-types {lang.Code {"stringValue" java.lang.String}}}})




  (augment {:expr '[:invoke-virtual
                    {:name .get}
                    [:arg {:value 2}]
                    [:get-field {:name "stringValue"} [:arg {:value 1}]]]
            :env '{:arg-types [org.objectweb.asm.commons.GeneratorAdapter lang.Code java.util.Map]
                   :data-types {lang.Code {"stringValue" java.lang.String}}}})


  (augment {:expr '[:store-local
                    {:local-type org.objectweb.asm.Label, :name "label"}
                    [:invoke-virtual {:name .newLabel} [:arg {:value 0}]]]
            :env '{:arg-types [org.objectweb.asm.commons.GeneratorAdapter lang.Code java.util.Map]
                   :data-types {lang.Code {"stringValue" java.lang.String}}}})


  )
