(ns jml.l1.bootstrap
  (:require [jml.core :as core]
            [jml.l1.backend :as l1]
            [clojure.string :as string])
  (:import [org.objectweb.asm Type]
           [org.objectweb.asm.commons Method]))




(defn un-objectify [x]
  (cond (instance? Type x)
        (list 'Type/getType (.toString x))
        (instance? Method x)
        (list 'Method. (.getName x) (.getDescriptor x))
        :else x))

(defn un-objectify-walk [code]
  (clojure.walk/postwalk un-objectify code))


(defn generate-code [command]
  (un-objectify-walk
   (let [code (second command)]
     (case (first command)
       :mult-int '(lang.Code/MultInt)
       :plus-int '(lang.Code/PlusInt)
       :sub-int '(lang.Code/SubInt)
       :dup '(lang.Code/Dup)
       :pop '(lang.Code/Pop)
       :print '(lang.Code/Print)
       :return '(lang.Code/Return)
       :nil '(lang.Code/Nil)
       :arg (list 'lang.Code/Arg (:value code))
       :math (list 'lang.Code/Math (:op code) (:op-type code))
       :get-static-field (list 'lang.Code/GetStatic (:owner code) (:name code) (:field-type code))
       :invoke-static (list 'lang.Code/InvokeStatic (:owner code) (:method code))
       :invoke-virtual (list 'lang.Code/InvokeVirtual (:owner code) (:method code))
       :invoke-constructor (list 'lang.Code/InvokeConstructor (:owner code) (:method code))
       :new (list 'lang.Code/New (:owner code)) 
       :bool (list 'lang.Code/Bool (:value code))
       :int (list 'lang.Code/Int (:value code))
       :string (list 'lang.Code/String (:value code))
       :get-field (list 'lang.Code/GetField (:owner code) (:name code) (:field-type code))
       :put-field (list 'lang.Code/PutField (:owner code) (:name code) (:field-type code))
       :label (list 'lang.Code/Label (name (:value code)))
       :jump-not-equal (list 'lang.Code/JumpNotEqual (:value code) (:compare-type code))
       :jump-equal (list 'lang.Code/JumpEqual (:value code) (:compare-type code))
       :jump-cmp (list 'lang.Code/JumpCmp (name (:value code)) (:compare-type code) (:compare-op code))
       :jump (list 'lang.Code/Jump (name (:value code)))
       :store-local (list 'lang.Code/StoreLocal (:name code) (:local-type code))
       :load-local (list 'lang.Code/LoadLocal (:name code))
       :cast (list 'lang.Code/Cast (:to-type code))
       :new-array (list 'lang.Code/NewArray (:owner code))  
       :array-length (list 'lang.Code/ArrayLength)
       :array-load (list 'lang.Code/ArrayLoad (:owner code))  
       :array-store (list 'lang.Code/ArrayStore (:owner code))))))


(defn make-make-fn [{:keys [class-name arg-types return-type code]}]
  (list 'lang.makeFn/invoke
        (string/replace class-name "lang" "lang2")
        (list 'into-array 'lang.Code (mapv generate-code code))
        (un-objectify return-type)
        (list 'into-array 'Type (mapv un-objectify arg-types))))


(defn make-make-fn2 [{:keys [class-name arg-types return-type code]}]
  (list 'lang2.makeFn/invoke
        (string/replace class-name "lang" "lang3")
        (list 'into-array 'lang.Code (mapv generate-code code))
        (un-objectify return-type)
        (list 'into-array 'Type (mapv un-objectify arg-types))))

(def example
  "(Type/getType \"Llang/generateCode;\")
  (Lorg/objectweb/asm/commons/GeneratorAdapter;Llang/Code;)V")



(def path "./src/jml/l1/compilerl1.clj")
(def compile-file (slurp path))
(spit path
      (str
       (subs compile-file 0 (+ (string/index-of compile-file ";; CODE") (count ";; CODE")))
       "\n\n"
       (string/replace 
        (string/join "\n\n"
                     (map (fn [function]
                            (with-out-str
                              (clojure.pprint/pprint function)))
                          (map make-make-fn
                               (filter (comp #{:fn} :type)
                                       (vals
                                        (:functions-with-ir
                                         (core/get-ir-multiple*
                                          @core/raw-source)))))))
        #"Llang/([a-z].*?;)" "Llang2/$1")))



(def path "./src/jml/l2/compilel2.clj")
(def compile-file (slurp path))
(spit path
      (str
       (subs compile-file 0 (+ (string/index-of compile-file ";; CODE") (count ";; CODE")))
       "\n\n"
       (string/replace 
        (string/join "\n\n"
                     (map (fn [function]
                            (with-out-str
                              (clojure.pprint/pprint function)))
                          (map make-make-fn2
                               (filter (comp #{:fn} :type)
                                       (vals
                                        (:functions-with-ir
                                         (core/get-ir-multiple*
                                          @core/raw-source)))))))
        #"Llang/([a-z].*?;)" "Llang3/$1")))






