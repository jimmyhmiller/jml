(ns jml.type-checker
  (:require [clojure.reflect :as reflect])
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
  (Type/getType ^Class (Class/forName (name sym))))

(defn to-type-array [syms]
  (into-array Type (map symbol->type syms)))


(defn jvm-type-equiv? [[target actual]]
  ;; We need to handle a subtyping relation
  (or (= target actual)
      (= target 'java.lang.Object)))

(defn create-method [{:keys [name arg-types return-type]}]
  (Method. name (symbol->type return-type) (to-type-array arg-types)))

(defn get-method-info [klass method-name method-args]
  (let [methods (->> (reflect/reflect klass)
                     :members
                     (filter (comp #{(symbol method-name)} :name))
                     (filter (fn [{:keys [parameter-types]}]
                               (or (nil? method-args)
                                   (every? jvm-type-equiv? (map vector parameter-types method-args))))))
        _ (when-not (= 1 (count methods))
            (throw (ex-info "Method overloaded. Need to be more specific"
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
  (get-in env [:data-types owner field]))


(defn check [{:keys [expr type env] :as context}]
  (case (first expr)
    :int (matches-type 'int context)
    :bool (matches-type 'bool context)
    :string (matches-type 'java.lang.String context)
    :arg (matches-type (get-in env [:arg-types (:value (second expr))]) context)))



(defn synth [{:keys [expr env]}]
  (case (first expr)
    :int 'int
    :bool 'boolean
    :string 'java.lang.String
    :arg (get-in env [:arg-types (:value (second expr))])
    :get-field (get-field-type (:owner (second expr)) (:name (second expr)) env)))



(defn augment [{:keys [expr env type] :as context}]
  (when type
    (check context))

  (case (first expr)
    :get-field (let [[_ attrs child] expr]
                 (let [child-type (synth (assoc context :expr child))
                       field-type (synth (assoc context :expr (assoc-in expr [1 :owner] child-type)))]
                   (-> expr
                       (assoc-in [1 :owner] child-type)
                       (assoc-in [1 :field-type] field-type))))
    :invoke-virtual (let [[tag attrs & [this & args]] expr]
                      (let [method-name (subs (name (:name attrs)) 1)
                            this-type (synth (assoc context :expr this))
                            augmented-args (mapv #(augment  (assoc context :expr %)) args)
                            arg-types (mapv #(synth (assoc context :expr %)) augmented-args)
                            {:keys [method return-type]} (get-method-info (Class/forName (name this-type))
                                                                          method-name
                                                                          arg-types)]
                        (into [tag
                               (-> attrs
                                   (assoc :owner this-type)
                                   ;; Should we do parameters from reflect?
                                   ;; Not sure
                                   (assoc :arg-types arg-types)
                                   (assoc :return-type return-type)
                                   (assoc :name method-name)
                                   (assoc :method method))]
                              augmented-args)))
    expr))



(synth {:expr [:int 2]})

(check {:expr [:int 2] :type 'java.lang.String})

(check {:expr [:arg {:value 0}] :type 'java.lang.String :env {:arg-types ['int]}})
(check {:expr [:arg {:value 0}] :type 'java.lang.String :env {:arg-types '[java.lang.String]}})
(synth {:expr [:arg {:value 0}] :env {:arg-types ['java.lang.String]}})

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
    [[:if
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
       [[:store-local
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
         [:arg {:value 1}]
         [:invoke-virtual
          {:name .get}
          [:arg {:value 2}]
          [:get-field {:name "stringValue"} [:arg {:value 1}]]]]
        [[:store-local
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
