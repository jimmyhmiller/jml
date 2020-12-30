(ns jml.core
  (:import [org.objectweb.asm Opcodes Type ClassWriter]
           [org.objectweb.asm.commons Method GeneratorAdapter]))


(set! *warn-on-reflection* true)

(def INIT (Method/getMethod "void <init>()"))

(defn generate-code! [^GeneratorAdapter gen command]
  (let [code (second command)]
    (case (first command)

      :plus-int
      (recur gen [:math {:op GeneratorAdapter/ADD
                         :op-type Type/INT_TYPE}])

      :sub-int
      (recur gen [:math {:op GeneratorAdapter/SUB
                         :op-type Type/INT_TYPE}])

      :arg
      (.loadArg ^GeneratorAdapter gen (:value code))
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
      :store-local
      (.storeLocal gen (:index code) (:local-type code))
      :load-local
      (.loadLocal gen (:index code) (:local-type code))
      :bool
      ;; Should assert bool?
      (.push gen (boolean (:value code)))
      :int
      (.push gen (int (:value code)))
      :put-field
      (.putField gen (:owner code) (:name code) (:field-type code))
      :dup
      (.dup gen)
      :pop
      (.pop gen)
      ;; We need some environment or two phases for labels
      :label
      (.newLabel gen)
      :ifNeq
      (.ifCmp gen (:compare-type code) GeneratorAdapter/NE (:label code))
      :return
      (.returnValue gen))))


(defn generate-default-constructor [^ClassWriter writer]
  (let [gen (GeneratorAdapter. Opcodes/ACC_PUBLIC INIT nil nil writer)]
    (.visitCode gen)
    (.loadThis gen)
    (.invokeConstructor gen (Type/getType Object) INIT)
    (.returnValue gen)
    (.endMethod gen)))

(defn initialize-class [^ClassWriter writer class-name]
  (.visit writer Opcodes/V1_8 Opcodes/ACC_PUBLIC class-name nil "java/lang/Object" nil))


(defn linearize [code]
  (let [[op props & children] code
        children (if (vector? props)
                   (cons props children)
                   children)
        props (cond (map? props)
                    props

                    (vector? props)
                    {}

                    :else {:value props})]

    (cond (not (keyword? op))
          code

          (empty? children)
          [[op props]]

          :else (conj (into [] (mapcat linearize children)) [op props]))))


(defn generate-invoke-method [^ClassWriter writer {:keys [code return-type arg-types]}]
  (let [method (Method. "invoke" return-type (into-array Type arg-types))
        gen (GeneratorAdapter. (int (+ Opcodes/ACC_PUBLIC Opcodes/ACC_STATIC)) method nil nil writer)]
    (run! (fn [line] (generate-code! gen line)) code)
    (.endMethod ^GeneratorAdapter gen)))

(defn make-fn [{:keys [class-name code] :as description}]
  (let [writer (ClassWriter. ClassWriter/COMPUTE_FRAMES)]
    (initialize-class writer class-name)
    (generate-default-constructor writer)
    (generate-invoke-method writer (assoc description :code (conj (linearize code) [:return])))
    (.visitEnd writer)
    (.defineClass ^clojure.lang.DynamicClassLoader
                  (clojure.lang.DynamicClassLoader.)
                  (.replace ^String class-name \/ \.) (.toByteArray ^ClassWriter  writer) nil)
    class-name))



;; if statement example

(do
  (def writer (ClassWriter. ClassWriter/COMPUTE_FRAMES))
  (initialize-class writer "Stuff")
  (generate-default-constructor writer)
  (def method (Method. "invoke" Type/INT_TYPE (into-array Type [Type/BOOLEAN_TYPE])))
  (def gen ^GeneratorAdapter (GeneratorAdapter. (int (+ Opcodes/ACC_PUBLIC Opcodes/ACC_STATIC)) method nil nil writer))


  (let [gen ^GeneratorAdapter gen
        else-label (.newLabel gen)]

    (doto gen
      (.push true) ;; compare to
      (.loadArg (int 0))
      (.ifCmp Type/BOOLEAN_TYPE GeneratorAdapter/NE else-label)
      (.push (int 1))
      (.returnValue)
      (.mark else-label)
      (.push (int 0))
      (.returnValue)))

  (.endMethod ^GeneratorAdapter gen)

  (.visitEnd ^ClassWriter writer)
  (.defineClass ^clojure.lang.DynamicClassLoader
                (clojure.lang.DynamicClassLoader.)
                (.replace "Stuff" \/ \.) (.toByteArray ^ClassWriter  writer) nil)

  (Stuff/invoke true))




(make-fn {:class-name "Thing"
          :code
          [:plus-int
           [:int 1]
           [:int 32]]
          :return-type Type/INT_TYPE})


(make-fn {:class-name "BoolReturn"
          :code
          [:bool true]
          :return-type Type/BOOLEAN_TYPE})

(make-fn {:class-name "ArgReturn"
          :code
          [:arg 0]
          :arg-types [Type/INT_TYPE]
          :return-type Type/INT_TYPE})

(make-fn {:class-name "TwoArgAdd3Return"
          :code
          [:plus-int [:int 3]
           [:plus-int [:arg 0] [:arg 1]]]
          :arg-types [Type/INT_TYPE Type/INT_TYPE]
          :return-type Type/INT_TYPE})


(make-fn {:class-name "CallOther"
          :code
          [:invoke-static {:owner (Type/getType (Class/forName "Thing"))
                           :method (Method. "invoke" Type/INT_TYPE (into-array Type []))}]
          :arg-types []
          :return-type Type/INT_TYPE})


{:add-thing (Thing/invoke)
 :bool-return (BoolReturn/invoke)
 :arg-return (ArgReturn/invoke 2)
 :arg-2-return (TwoArgAdd3Return/invoke 2 3)
 :call-other (CallOther/invoke)}



;; We need our own notion of type
;; We need a type environment
;; We need locals
;; We need conditionals
;; Function call syntax
;; Need to namespace things


(reflect/reflect GeneratorAdapter)



(require '[clojure.reflect :as reflect])

(reflect/reflect (Thing.))

(require '[clj-java-decompiler.core :as decompiler])


(decompiler/disassemble (Thing.))

(decompiler/disassemble
 (let [x 1]
   (case x
     1 true
     2 false
     3)))


(decompiler/disassemble
 (if (= 43 54)
   12
   42))



{:already-linear (linearize [[:int {:value 42}]])
 :children-empty (linearize [:const-thing {} []])
 :simple-plus (linearize [:plus-int {}
                          [:int {:value 31}]
                          [:int {:value 32}]])
 :complicated-plus (linearize [:plus-int {}
                               [:int {:value 1}]
                               [:plus-int {} [:int {:value 2}] [:int {:value 3}]]])
 :complicated-plus' (linearize [:plus-int
                                [:int {:value 1}]
                                [:plus-int [:int {:value 2}] [:int {:value 3}]]])
 :complicated-plus'' (linearize [:plus-int
                                 [:int {:value 1}]
                                 [:plus-int [:int 2] [:int {:value 3}]]])}
