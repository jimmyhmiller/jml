(ns jml.type-checker
  (:require [clojure.reflect :as reflect]
            [clojure.string :as string])
  (:import [org.objectweb.asm Opcodes Type]
           [org.objectweb.asm.commons Method]))


(declare check)
(declare synth)

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
    (Type/getType ^Class (Class/forName (name sym)))))

(defn to-type-array [syms]
  (into-array Type (map symbol->type syms)))



(defn jvm-type-equiv? [[target actual]]
  ;; Checking primatives and subclassing relation
  (or (= target actual)
      ;; Ugly hack to autocase
      (= actual 'java.lang.Object)
      (try
        (.isAssignableFrom (Class/forName (name target)) (Class/forName (name actual)))
        (catch Exception e false))))

(defn create-method [{:keys [name arg-types return-type]}]
  (Method. name (symbol->type return-type) (to-type-array arg-types)))

(defn get-method-info-jvm [klass method-name method-args]
  (let [methods (->> (reflect/reflect klass)
                     :members
                     (filter (comp #{(symbol method-name)} :name))
                     (filter (fn [{:keys [parameter-types]}]
                               (= (count parameter-types) (count method-args))))
                     (filter (fn [{:keys [parameter-types]}]
                               (or (nil? method-args)
                                   (every? jvm-type-equiv? (map vector parameter-types method-args))))))
        _ (when-not (= 1 (count methods))
            (throw (ex-info "Method overloaded. Need to be more or less specific"
                            {:class (.getName klass)
                             :method-name method-name
                             :methods methods
                             :method-args method-args})))
        {:keys [return-type parameter-types]} (first methods)]
    {:return-type return-type
     :parameter-types parameter-types
     :method (create-method {:name method-name
                             :arg-types parameter-types
                             :return-type return-type})}))



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
    := (matches-type 'boolean context)
    :invoke-static (do
                     (matches-type (:return-type (second expr)) context)
                     (doseq [[type expr] (map vector (:arg-types (second expr)) (rest (rest expr)))]
                       (check (assoc context :expr expr :type type))))
    :invoke-virtual (do
                      (matches-type (:return-type (second expr)) context)
                      (check (assoc context :expr (nth expr 2) :type (:owner (second expr))))
                      (doseq [[type expr] (map vector (:arg-types (second expr)) (rest (rest (rest expr))))]
                        (check (assoc context :expr expr :type type))))
    :get-field (matches-type (:field-type (second expr)) context)
    :do (matches-type (augment-then-synth (assoc context :expr (last expr))) context)
    :nil (matches-type 'void context)
    :pop (matches-type 'void context)
    ;; Need to actually be augmenting env
    :store-local (matches-type 'void context)
    :load-local env
    (throw (ex-info "No matching check" {:expr expr :type type :env env}))))



(defn synth [{:keys [expr env]}]
  (case (first expr)
    :int 'int
    :bool 'boolean
    :string 'java.lang.String
    :sub-int 'int
    :mult-int 'int
    :arg (get-in env [:arg-types (:value (second expr))])
    :get-field (get-field-type (:owner (second expr)) (:name (second expr)) env)
    :invoke-virtual (if-let [return-type (:return-type (second expr))]
                      return-type
                      (throw (ex-info "Can't synth invoke virtual" {:expr expr :env env})))

    :invoke-static (if-let [return-type (:return-type (second expr))]
                     return-type
                     (throw (ex-info "Can't synth invoke static" {:expr expr :env env})))
    ;; Fix by looking in env
    :load-local 'java.lang.Object
    := 'boolean
    :do (augment-then-synth {:expr (last expr) :env env})
    :nil 'void
    ;; Is this right?
    :if (synth {:expr (last expr) :env env})
    :pop 'void
    :store-local 'void
    (throw (ex-info "No matching synth" {:expr expr :env env}))))



(declare augment)

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

(def context2 nil)
context



(defn augment [{:keys [expr env type] :as context}]


  (when-not context2
    (def context2 context))
  (when (and type (not= (first expr) :do))
    (check context))

  (case (first expr)
    :get-field (let [[_ attrs child] expr]
                 (let [child-type (synth (assoc context :expr child))
                       field-type (synth (assoc context :expr (assoc-in expr [1 :owner] child-type)))]
                   (-> expr
                       (assoc-in [1 :owner] child-type)
                       (assoc-in [1 :field-type] field-type))))
    :invoke-virtual (let [[tag attrs & [this & args]] expr]
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
                               (concat [augmented-this] augmented-args)))))
    :invoke-static (let [[tag attrs & [& args]] expr]
                     ;; We already augmented
                     (if (:return-type attrs)
                       expr
                       (let [this (symbol (namespace (:name attrs)))
                             method-name (name (:name attrs))
                             this-type this
                             augmented-args (mapv #(augment (assoc context :expr %)) args)
                             arg-types (mapv #(synth (assoc context :expr %)) augmented-args)
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
    :do (do
          (let [result
                (into [(first expr) (second expr)]
                      ;; Need to reduce?
                      (mapv (fn [expr] (let [result
                                             (augment (assoc (dissoc context :type) :expr expr))]
                                         (check {:expr result
                                                 :env env
                                                 :type (synth {:expr result :env env})})
                                         result))
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
               (mapv (fn [expr] (let [result
                                      (augment (assoc (dissoc context :type) :expr expr))]
                                  (check {:expr result
                                          :env env
                                          :type (synth {:expr result :env env})})
                                  result))
                     (rest (rest (rest expr)))))
    :pop [:pop]

    

    (do
      (into [(first expr) (second expr)]
            (mapv (fn [expr]
                    (let [result
                          (augment (assoc (dissoc context :type) :expr expr))]
                      (check {:expr result
                              :env env
                              :type (synth {:expr result :env env})})
                      result))
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



  (comment



    (def example
      '{:type :fn,
        :arg-names (gen code env),
        :arg-types
        (org.objectweb.asm.commons.GeneratorAdapter
         lang.Code
         java.util.Map),
        :return-type java.util.Map,
        :code
        [:do
         [:if
          [:invoke-virtual
           {:name .equals}
           [:get-field {:name "tagName"} [:arg {:value 1}]]
           [:string "Label"]]
          [:if
           [:invoke-virtual
            {:name .containsKey}
            [:arg {:value 2}]
            [:get-field {:name "stringValue"} [:arg {:value 1}]]]
           [:invoke-virtual
            {:name .mark}
            [:arg {:value 0}]
            [:invoke-virtual
             {:name .get}
             [:arg {:value 2}]
             [:get-field {:name "stringValue"} [:arg {:value 1}]]]]
           [:do
            [:store-local
             {:local-type org.objectweb.asm.Label, :name "label"}
             [:invoke-virtual {:name .newLabel} [:arg {:value 0}]]]
            [:pop]
            [:invoke-virtual
             {:name .mark}
             [:arg {:value 0}]
             [:invoke-virtual
              {:name .get}
              [:arg {:value 2}]
              [:get-field {:name "stringValue"} [:arg {:value 1}]]]]
            [:pop]
            [:invoke-virtual
             {:name .put}
             [:arg {:value 2}]
             [:get-field {:name "stringValue"} [:arg {:value 1}]]
             [:load-local {:name "label"}]]]]
          [:if
           [:invoke-virtual
            {:name .equals}
            [:get-field {:name "tagName"} [:arg {:value 1}]]
            [:string "Jump"]]
           [:if
            [:invoke-virtual
             {:name .containsKey}
             [:arg {:value 2}]
             [:get-field {:name "stringValue"} [:arg {:value 1}]]]
            [:invoke-virtual
             {:name .goTo}
             [:arg {:value 0}]
             [:invoke-virtual
              {:name .get}
              [:arg {:value 2}]
              [:get-field {:name "stringValue"} [:arg {:value 1}]]]]
            [:do
             [:store-local
              {:name "label", :local-type org.objectweb.asm.Label}
              [:invoke-virtual {:name .newLabel} [:arg {:value 0}]]]
             [:pop]
             [:invoke-virtual
              {:name .goTo}
              [:arg {:value 0}]
              [:load-local {:name "label"}]]
             [:pop]
             [:invoke-virtual
              {:name .put}
              [:arg {:value 2}]
              [:get-field {:name "stringValue"} [:arg {:value 1}]]
              [:load-local {:name "label"}]]]]
           [:invoke-static
            {:name lang.myGenerateCode/invoke}
            [:arg {:value 0}]]]]
         [:arg {:value 2}]]})







    )
  
  )
