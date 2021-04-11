(ns jml.backend
  (:require [jml.decompile]
            [clojure.walk :as walk]
            [clojure.string :as string]
            [clojure.reflect :as reflect])
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
      :string
      (.push gen ^String (:value code))
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




(defn generate-invoke-method [^ClassWriter writer {:keys [code return-type arg-types]}]
  (println return-type arg-types)
  (let [method (Method. "invoke" return-type (into-array Type arg-types))
        gen (GeneratorAdapter. (int (+ Opcodes/ACC_PUBLIC Opcodes/ACC_STATIC)) method nil nil writer)]
    (reduce (fn [env line] (generate-code-with-env! gen line env)) {} code)
    (.endMethod ^GeneratorAdapter gen)))



(defn make-fn [{:keys [class-name code arg-types] :as description}]
  (let [writer (ClassWriter. (int (+ ClassWriter/COMPUTE_FRAMES ClassWriter/COMPUTE_MAXS)))]
    (initialize-class writer class-name)
    (generate-default-constructor writer)
    (generate-invoke-method writer description)
    (.visitEnd writer)

    (jml.decompile/print-and-load-all writer class-name)))



(defn make-field [^ClassWriter writer {:keys [name type]}]
  (let [field (.visitField writer Opcodes/ACC_PUBLIC name (.getDescriptor ^Type type) nil nil)]
    (.visitEnd field)))

(defn make-field-assignment [^GeneratorAdapter gen this-type index {:keys [name type]}]
  (.loadThis gen)
  (.loadArg gen (int index))
  (.putField gen this-type name type))


(defn make-field-assignment-on-stack [^GeneratorAdapter gen this-type index {:keys [name type]}]
  (.dup gen)
  (.loadArg gen (int index))
  (.putField gen this-type name type))


(defn run-indexed! [f coll]
  (doall (map-indexed f coll)))

(defn make-struct-constructor [writer {:keys [class-name fields]}]
  (let [gen (GeneratorAdapter. Opcodes/ACC_PUBLIC
                               (Method. "<init>" Type/VOID_TYPE (into-array Type (map :type fields))) nil nil writer)
        ;; This is probably not always true, especially with namespacing.
        this-type (Type/getType (str "L" class-name ";"))]
    (.visitCode gen)
    (.loadThis gen)
    (.invokeConstructor gen (Type/getType Object) INIT)
    (run-indexed! (fn [i field] (make-field-assignment gen this-type i field)) fields)
    (.returnValue gen)
    (.endMethod gen)))

(defn make-struct [description]
  (let [{:keys [class-name fields] :as description} description
        writer (ClassWriter. (int (+ ClassWriter/COMPUTE_FRAMES ClassWriter/COMPUTE_MAXS)))]
    (initialize-class writer class-name)
    (generate-default-constructor writer)
    (run! (partial make-field writer) fields)
    (make-struct-constructor writer description)
    (.visitEnd writer)
    (jml.decompile/print-and-load-bytecode writer class-name)
    class-name))


(defn gen-field-to-string [^GeneratorAdapter gen this-type sb-type sb-append length index {:keys [name type]}]
  ;; assumes there is already a StringBuilder on stack, ready to append to..
  (.loadThis gen)
  (.getField gen this-type name type)

  (.invokeVirtual gen sb-type
                  (Method. "append"
                           (Type/getType java.lang.StringBuilder)
                           (into-array Type [(if (= (.getSort ^Type type) Type/OBJECT)
                                               (Type/getType Object)
                                               type)])))
  (when-not (= length (inc index))
    (.dup gen)
    (.push gen " ")
    (.invokeVirtual gen sb-type sb-append))
  (.dup gen))


(defn make-table-switch-gen [^GeneratorAdapter gen {:keys [class-name variants]}]
  (let [variants-indexed (into {} (map-indexed vector variants))
        this-type (Type/getType (str "L" class-name ";"))
        sb-type (Type/getType (Class/forName "java.lang.StringBuilder"))
        sb-ctor (Method/getMethod "void <init> (String)")
        sb-to-string (Method/getMethod "String toString ()")
        sb-append    (Method/getMethod "java.lang.StringBuilder append (String)")]
    (proxy [org.objectweb.asm.commons.TableSwitchGenerator] []
      (generateCase [int-key end]
        (let [{:keys [name fields]} (variants-indexed int-key)]
          (.newInstance gen sb-type)
          (.dup gen)
          (.push gen (str "(" class-name "/" name (when-not (empty? fields) " ")))
          (.invokeConstructor gen sb-type sb-ctor)

          (run-indexed! (partial gen-field-to-string gen this-type sb-type sb-append (count fields))
                        fields)

          (.push gen ")")
          (.invokeVirtual gen sb-type sb-append)
          (.invokeVirtual gen sb-type sb-to-string)

          (.returnValue gen))
        )
      (generateDefault []
        (.push gen "Variant not found")
        (.returnValue gen)))))


(defn make-to-string [writer {:keys [class-name variants] :as description}]
  (let [this-type (Type/getType (str "L" class-name ";"))
        method (Method. "toString" (Type/getType String) (into-array Type []))
        gen (GeneratorAdapter. (int (+ Opcodes/ACC_PUBLIC)) method nil nil writer)
        tsg (make-table-switch-gen gen description)]

    (.visitCode gen)
    (.loadThis gen)
    (.getField gen this-type "tag" Type/INT_TYPE)

    (.tableSwitch gen
                  (int-array (range (count variants)))
                  tsg)

    (.endMethod ^GeneratorAdapter gen)))




(defn make-enum-factory [writer class-name {:keys [name fields tagName tag]}]
  (let [ ;; This is probably not always true, especially with namespacing.
        this-type (Type/getType (str "L" class-name ";"))
        method (Method. name this-type (into-array Type (map :type fields)))
        gen (GeneratorAdapter. (int (+ Opcodes/ACC_PUBLIC Opcodes/ACC_STATIC)) method nil nil writer)]
    (.visitCode gen)
    (.newInstance gen this-type)
    (.dup gen)
    (.invokeConstructor gen this-type INIT)

    (.dup gen)
    (.push gen (int tag))
    (.putField gen this-type "tag" Type/INT_TYPE)

    (.dup gen)
    (.push gen ^String tagName)
    (.putField gen this-type "tagName" (Type/getType String))


    (run-indexed! (fn [i field] (make-field-assignment-on-stack gen this-type i field)) fields)
    (.returnValue gen)
    (.endMethod ^GeneratorAdapter gen)))


(defn make-enum-variant [writer class-name tag {:keys [name fields] :as enum}]
  (make-enum-factory writer class-name (assoc enum :tag tag :tagName name)))


;; NOTE: All names type combinations must be unique
(defn make-enum [{:keys [class-name variants] :as description}]
  (let [writer (ClassWriter. (int (+ ClassWriter/COMPUTE_FRAMES ClassWriter/COMPUTE_MAXS)))]
    (initialize-class writer class-name)
    (generate-default-constructor writer)
    (make-field writer {:name "tag" :type Type/INT_TYPE})
    (make-field writer {:name "tagName" :type (Type/getType String)})
    (make-to-string writer description)
    (run! (partial make-field writer) (set (mapcat :fields variants)))
    (run-indexed! (partial make-enum-variant writer class-name) variants)
    (.visitEnd writer)
    ;; Should have a way to return class and not print
    (jml.decompile/print-and-load-bytecode writer class-name)))



(defn make-fn-with-callback [name callback]
  (let [class-name "MyAwesomeCode"
        writer (ClassWriter. (int (+ ClassWriter/COMPUTE_FRAMES ClassWriter/COMPUTE_MAXS)))
        method (Method. "invoke" Type/INT_TYPE (into-array Type []))
        gen (GeneratorAdapter. (int (+ Opcodes/ACC_PUBLIC Opcodes/ACC_STATIC)) method nil nil writer)]
    (initialize-class writer class-name)
    (generate-default-constructor writer)
    (callback gen)
    (.endMethod ^GeneratorAdapter gen)
    (.visitEnd writer)
    (jml.decompile/print-and-load-all writer class-name)))
