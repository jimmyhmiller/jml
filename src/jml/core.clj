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
      :get-field
      (.getField gen (:owner code) (:name code) (:field-type code))
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
      (.returnValue gen))))


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
          (.loadLocal gen local (:local-type code))
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


(defn de-sexpr [expr]
  (walk/postwalk
   (fn [x] (if (reduced? x)
             @x
             x))
   (walk/prewalk
    (fn [x]
      (cond
        (and (seq? x) (= (first x) 'arg))
        (reduced [:arg {:value (second x)}])
        (map? x) (reduced x)
        (seq? x) (vec x)
        (and (vector? x) (keyword? (first x))) (reduced x)
        (symbol? x) (keyword x)
        (int? x) (reduced [:int x])
        (boolean? x) (reduced [:bool x])
        :else x))
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



(defn make-struct-field [^ClassWriter writer {:keys [name type]}]
  (let [field (.visitField writer Opcodes/ACC_PUBLIC name (.getDescriptor ^Type type) nil nil)]
    (.visitEnd field)))

(defn make-field-assignment [^GeneratorAdapter gen this-type index {:keys [name type]}]
  (.loadThis gen)
  (.loadArg gen (int index))
  (.putField gen this-type name type))

(defn make-struct-constructor [writer {:keys [class-name fields]}]
  (let [gen (GeneratorAdapter. Opcodes/ACC_PUBLIC
                               (Method. "<init>" Type/VOID_TYPE (into-array Type (map :type fields))) nil nil writer)
        ;; This is probably not always true, especially with namespacing.
        this-type (Type/getType (str "L" class-name ";"))]
    (.visitCode gen)
    (.loadThis gen)
    (.invokeConstructor gen (Type/getType Object) INIT)
    (doall (map-indexed (fn [i field] (make-field-assignment gen this-type i field)) fields))
    (.returnValue gen)
    (.endMethod gen)))

(defn make-struct [{:keys [class-name fields] :as description}]
  (let [writer (ClassWriter. (int (+ ClassWriter/COMPUTE_FRAMES ClassWriter/COMPUTE_MAXS)))]
    (initialize-class writer class-name)
    (generate-default-constructor writer)
    (run! (partial make-struct-field writer) fields)
    (make-struct-constructor writer description)
    (.visitEnd writer)
    (jml.decompile/print-and-load-bytecode writer class-name)
    class-name))



;; TODO
;; Make Enums
;; Syntax for static invoke
;; Sytax for method invoke
;; Syntax for function call
;; Have a type environment
;; Make function syntax?
;; Need Loop construct
;; Let would be nice




(make-struct {:class-name "Point"
              :fields [{:name "x" :type Type/INT_TYPE}
                       {:name "y" :type Type/INT_TYPE}]})




(def p (Point.))
(def p2 (Point. 1 2))


(set! (.x ^Point p) 2)
(set! (.y ^Point p) 4)

(.y ^Point p2)
(.x ^Point p2)



(make-fn {:class-name "Thing"
          :code
          '(plus-int 1 2)
          :return-type Type/INT_TYPE})


(make-fn {:class-name "BoolReturn"
          :code true
          :return-type Type/BOOLEAN_TYPE})

(BoolReturn/invoke)

(make-fn {:class-name "ArgReturn"
          :code
          '(arg 0)
          :arg-types [Type/INT_TYPE]
          :return-type Type/INT_TYPE})

(make-fn {:class-name "TwoArgAdd3Return"
          :code
          '(plus-int 3
                     (plus-int (arg 0) (arg 1)))
          :arg-types [Type/INT_TYPE Type/INT_TYPE]
          :return-type Type/INT_TYPE})

(linearize  ('invoke-static {:owner (Type/getType (Class/forName "Thing"))
                           :method (Method. "invoke" Type/INT_TYPE (into-array Type []))}))

(make-fn {:class-name "CallOther"
          :code
          (list 'invoke-static {:owner (Type/getType (Class/forName "Thing"))
                                :method (Method. "invoke" Type/INT_TYPE (into-array Type []))})
          :arg-types []
          :return-type Type/INT_TYPE})


(linearize '(return
            (if (= true (arg 0)) ;; but just `true` would work too
              42
              0)))

(make-fn {:class-name "IfReturn"
          :code
          '(return
            (if (= true (arg 0)) ;; but just `true` would work too
              42
              0))
          :arg-types [Type/BOOLEAN_TYPE]
          :return-type Type/INT_TYPE})


(IfReturn/invoke true)


(make-fn {:class-name "IfGreaterThanZeroReturn"
          :code
          '(return
            (if (> (arg 0) 0)
              42
              0))
          :arg-types [Type/INT_TYPE]
          :return-type Type/INT_TYPE})


(IfGreaterThanZeroReturn/invoke 2)



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

(LoopThing/invoke 10)

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





(RecursionThing/invoke 10)




{:add-thing (Thing/invoke)
 :bool-return (BoolReturn/invoke)
 :arg-return (ArgReturn/invoke 2)
 :arg-2-return (TwoArgAdd3Return/invoke 2 3)
 :call-other (CallOther/invoke)}


(comment

  (require '[clojure.reflect :as reflect])
  (reflect/reflect GeneratorAdapter)

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
  )
