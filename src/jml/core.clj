(ns jml.core
  (:require [jml.decompile]
            [clojure.walk :as walk])
  (:import [org.objectweb.asm Opcodes Type ClassWriter]
           [org.objectweb.asm.commons Method GeneratorAdapter]))


(set! *warn-on-reflection* true)

(def INIT (Method/getMethod "void <init>()"))


(defn generate-code! [^GeneratorAdapter gen command ]
  (let [code (second command)]
    (case (first command)

      :plus-int
      (recur gen [:math {:op GeneratorAdapter/ADD
                         :op-type Type/INT_TYPE}])

      :sub-int
      (recur gen [:math {:op GeneratorAdapter/SUB
                         :op-type Type/INT_TYPE}])

      :arg
      (.loadArg ^GeneratorAdapter gen (int (:value code)))
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

      :print (do
               (.dup gen)
               (.getStatic gen
                           (Type/getType (Class/forName "java.lang.System"))
                           "out"
                           (Type/getType ^java.lang.Class (.getGenericType (.getDeclaredField (Class/forName "java.lang.System")
                                                                                              "out"))))
               (.swap gen)
               (.invokeVirtual gen (Type/getType ^java.lang.Class
                                                 (.getGenericType (.getDeclaredField (Class/forName "java.lang.System")
                                                                                     "out")))
                               (Method. "println" Type/VOID_TYPE  (into-array Type [Type/INT_TYPE]))))
      ;; We need some environment or two phases for labels
      :return
      (.returnValue gen)

      :do nil)))


(defn generate-code-with-env! [^GeneratorAdapter gen command env]
  (let [code (second command)]
    (case (first command)
      :label
      (if-let [label (get env (:value code))]
        (do
          (.mark gen label)
          env)
        (let [label (.newLabel gen)]
          (.mark gen label)
          (assoc env (:value code) label)))

;; :jump-equal and :jump-not-equal could be removed, since :jump-cmp replaces them
      :jump-not-equal
      (if-let [label (get env (:value code))]
        (do
          (.ifCmp gen (:compare-type code) GeneratorAdapter/NE label)
          env)
        (let [label (.newLabel gen)]
          (.ifCmp gen (:compare-type code) GeneratorAdapter/NE label)
          (assoc env (:value code) label)))


      :jump-equal
      (if-let [label (get env (:value code))]
        (do
          (.ifCmp gen (:compare-type code) GeneratorAdapter/EQ label)
          env)
        (let [label (.newLabel gen)]
          (.ifCmp gen (:compare-type code) GeneratorAdapter/EQ label)
          (assoc env (:value code) label)))

      :jump-cmp
      (if-let [label (get env (:value code))]
        (do
          (.ifCmp gen (:compare-type code) (:compare-op code) label)
          env)
        (let [label (.newLabel gen)]
          (.ifCmp gen (:compare-type code) (:compare-op code) label)
          (assoc env (:value code) label)))

      :jump
      (if-let [label (get env (:value code))]
        (do
          (.goTo gen label)
          env)
        (let [label (.newLabel gen)]
          (.goTo gen label)
          (assoc env (:value code) label)))


      :store-local
      (if-let [local (get env (str "local-" (:index code)))]
        (do
          (.storeLocal gen local (:local-type code))
          env)
        (let [local (.newLocal gen (:local-type code))]
          (.storeLocal gen local (:local-type code))
          (assoc env (str "local-" (:index code)) local)))

      :load-local
      (if-let [local (get env (str "local-" (:index code)))]
        (do
          (.loadLocal gen local(:local-type code))
          env)
        (throw (ex-info "This local was loaded before stored" {:command command :env env})))

      (do (generate-code! gen command)
          env))))


(defn generate-default-constructor [^ClassWriter writer]
  (let [gen (GeneratorAdapter. Opcodes/ACC_PUBLIC INIT nil nil writer)]
    (.visitCode gen)
    (.loadThis gen)
    (.invokeConstructor gen (Type/getType Object) INIT)
    (.returnValue gen)
    (.endMethod gen)))

(defn initialize-class [^ClassWriter writer class-name]
  (.visit writer Opcodes/V1_8 Opcodes/ACC_PUBLIC class-name nil "java/lang/Object" nil))


(defn resolve-cmp-op [pred]
  (case (first pred)
    :=  GeneratorAdapter/EQ
    :!= GeneratorAdapter/NE
    :>  GeneratorAdapter/GT
    :>= GeneratorAdapter/GE
    :<  GeneratorAdapter/LT
    :<= GeneratorAdapter/LE
    ;; What is a good default here? EQ? Error? What if it's not a operator, but a boolean value ?
    GeneratorAdapter/EQ))

(defn resolve-cmp-type [pred]
  (if (#{:> :>= :< :<=} (first pred))
    ;; if it's greater or less than, it should probably be INT
    Type/INT_TYPE
    ;; otherise it can be both, so for now I'll assume Bool,
    ;; but we should really check the type of operands
    Type/BOOLEAN_TYPE))

(defn desugar-if [[tag pred t-branch f-branch :as node]]
  (if (= tag :if)
    (let [true-label (gensym "true_label_")
          exit-label (gensym "exit_label_")
          cmp-op     (resolve-cmp-op pred)
          [_ pred-op1 pred-op2] pred] ;; only works for 2 ops, we should consider single value
      [:do
       pred-op1
       pred-op2
       [:jump-cmp {:value true-label :compare-op cmp-op :compare-type (resolve-cmp-type pred)}]
       f-branch
       [:jump {:value exit-label}]
       [:label {:value true-label}]
       t-branch
       [:label {:value exit-label}]])
    node))


(defn de-sexpr [expr]
  (walk/postwalk
   (fn [x]
     (cond
       (and (seq? x) (= (first x) :arg))
       [:arg (second (second x))]
       (seq? x) (do (println x) (vec x))
       (symbol? x) (keyword x)
       (int? x) [:int x]
       (boolean? x) [:bool x]
       :else x))
   expr))

(defn linearize* [code]
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

          (= op :if)
          (recur (desugar-if code))

          (= op :do)
          (into [] (mapcat linearize* children))

          :else (conj (into [] (mapcat linearize* children)) [op props]))))

(defn linearize [expr]
  (linearize* (de-sexpr expr)))








(defn generate-invoke-method [^ClassWriter writer {:keys [code return-type arg-types]}]
  (let [method (Method. "invoke" return-type (into-array Type arg-types))
        gen (GeneratorAdapter. (int (+ Opcodes/ACC_PUBLIC Opcodes/ACC_STATIC)) method nil nil writer)]
    (reduce (fn [env line] (generate-code-with-env! gen line env)) {} code)
    (.endMethod ^GeneratorAdapter gen)))

(defn make-fn [{:keys [class-name code] :as description}]
  (let [writer (ClassWriter. (int (+ ClassWriter/COMPUTE_FRAMES ClassWriter/COMPUTE_MAXS)))]
    (initialize-class writer class-name)
    (generate-default-constructor writer)
    (generate-invoke-method writer (assoc description :code (conj (linearize code) [:return])))
    (.visitEnd writer)
    (jml.decompile/print-and-load-bytecode writer class-name)
    class-name))




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


(make-fn {:class-name "IfReturn"
          :code
          '(return
            (if (= true (arg 0))
              42
              0))
          :arg-types [Type/BOOLEAN_TYPE]
          :return-type Type/INT_TYPE})


(IfReturn/invoke true)


(make-fn {:class-name "IfGreaterThanZeroReturn"
          :code
          '(return
            (if (> (arg 0) 0) ;(do true (arg 0))
              42
              0))
          :arg-types [Type/INT_TYPE]
          :return-type Type/INT_TYPE})


(IfGreaterThanZeroReturn/invoke -1)




(make-fn {:class-name "LoopThing"
          :code
          [[:int {:value 0}]
           [:store-local {:index 0 :local-type Type/INT_TYPE}]

           [:label {:value "top-of-loop"}]
           [:load-local {:index 0 :local-type Type/INT_TYPE}]
           [:arg {:value 0}]
           [:jump-equal {:value "exit" :compare-type Type/INT_TYPE}]
           [:load-local {:index 0 :local-type Type/INT_TYPE}]
           [:print]
           [:int {:value 1}]
           [:plus-int]
           [:store-local {:index 0 :local-type Type/INT_TYPE}]
           [:jump {:value "top-of-loop"}]

           [:label {:value "exit"}]
           [:load-local {:index 0 :local-type Type/INT_TYPE}]
           [:return]]
           :arg-types [Type/INT_TYPE]
          :return-type Type/INT_TYPE})

(Type/getType (Class/forName "LoopThing"))

(Type/getType "LLoopThing;")

;; Be careful about having different numbers of items on stack on different code paths.

(make-fn {:class-name "RecursionThing"
          :code
          [[:int {:value 0}]
           [:arg {:value 0}]
           [:jump-equal {:value "exit" :compare-type Type/INT_TYPE}]

           [:int {:value -1}]
           [:arg {:value 0}]
           [:plus-int]
           [:print]
           [:invoke-static {:owner (Type/getType "LRecursionThing;")
                            :method (Method. "invoke" Type/INT_TYPE
                                             (into-array Type [Type/INT_TYPE]))}]
           [:return]

           [:label {:value "exit"}]
           [:arg {:value 0}]
           [:return]]
          :arg-types [Type/INT_TYPE]
          :return-type Type/INT_TYPE})




(make-fn {:class-name "RecursionThing"
          :code
          [[:int {:value 0}]
           [:arg {:value 0}]
           [:jump-equal {:value "exit" :compare-type Type/INT_TYPE}]

           [:int {:value -1}]
           [:arg {:value 0}]
           [:plus-int]
           [:print]
           [:invoke-static {:owner (Type/getType "LRecursionThing;")
                            :method (Method. "invoke" Type/INT_TYPE
                                             (into-array Type [Type/INT_TYPE]))}]
           [:return]

           [:label {:value "exit"}]
           [:arg {:value 0}]
           [:return]]
          :arg-types [Type/INT_TYPE]
          :return-type Type/INT_TYPE})






(DoRecursion/invoke 10)

(RecursionThing/invoke 10)




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
 (defn thing [x]
   (if (zero? x)
     0
     (thing (dec x)))))



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
