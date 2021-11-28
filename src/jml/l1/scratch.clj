(do

  (jml
   (defn lang.invokeConstructorExample [java.lang.Integer]
     (java.lang.Integer. 2)))

  (jml
   (defn lang.printer [x int void]
     (print x)
     (print x )))

  (lang.printer/invoke 42)

  (jml
   (defalias Type org.objectweb.asm.Type)
   (defalias Class java.lang.Class)

   (defn lang.createArray [i int int]
     (let [a (new-array int 2) Array/int
           b 2 int]
       (array-store a 0 1)
       (while (< (array-load a 0) i)
         242
         (array-store  a  0 (print  (mult-int (array-load a 0) 2))))

       (array-load a 0))))

  #_(lang.createArray/invoke 15)


  (jml
   (defalias Type org.objectweb.asm.Type)
   (defalias Class java.lang.Class)

   (defn lang.doubleNTimes[n int int]
     (let [x 1 int
           i 0 int]
       (while (<= i n)
         (print i)
         (store-local {:name "i"
                       :local-type int} (plus-int i 1))
         (store-local {:name "x"
                       :local-type int} (mult-int x 2)))
       x)))

  #_(lang.doubleNTimes/invoke 10)


  (jml
   (defn lang.foreachTest [n int void]
     (let [xs (new-array int n) Array/int
           i 0 int]
       (while (< i n)
         (array-store xs i i)
                                        ; (print (array-load xs i))
         (set! i (plus-int i 1)))
       (foreach [x xs int]

                (print x)))))

  (lang.foreachTest/invoke 10)



  (jml
   (defn lang.setLoopTest [ n int int]
     (let [i 0 int]

       (while (< i n)
         (print i)
         (set! i (plus-int i 1)))
       i)))

  (lang.setLoopTest/invoke 10)




  ;;
  (jml
   (defalias GeneratorAdapter org.objectweb.asm.commons.GeneratorAdapter)
   (defalias Type org.objectweb.asm.Type)
   (defalias Opcodes org.objectweb.asm.Opcodes)
   (defalias Class java.lang.Class)

   (defn lang.printCode [gen org.objectweb.asm.commons.GeneratorAdapter void]
     (.push gen 42)

     (.dup gen)
     (.getStatic gen
                 (Type/getType (java.lang.Class/forName "java.lang.System"))
                 "out"
                 (Type/getType  (.getType (.getDeclaredField (java.lang.Class/forName "java.lang.System")
                                                             "out"))))
     (.swap gen)
     (.invokeVirtual gen (Type/getType
                          (.getType (.getDeclaredField (Class/forName "java.lang.System")
                                                       "out")))
                     (let [arg-types (new-array org.objectweb.asm.Type 1) Array/org.objectweb.asm.Type]
                       (array-store arg-types 0 Type/INT_TYPE)
                       (org.objectweb.asm.commons.Method. "println" Type/VOID_TYPE arg-types)))
     (.returnValue gen)))

  ;;  org/objectweb/asm/commons/Method.<init>:(Ljava/lang/String;Lorg/objectweb/asm/Type;[Lorg/objectweb/asm/Type;)V
  ;;(Ljava/lang/String;Lorg/objectweb/asm/Type;[Lorg.objectweb.asm.Type)V"
  (backend/make-fn-with-callback
   "lang2.MyAwesomeCode"
   #(lang.printCode/invoke %))


  (lang2.MyAwesomeCode/invoke)


  (jml
   (defalias Type org.objectweb.asm.Type)

   (defenum lang.Field
     (Field name String
            type Type))

   (defn lang.fieldToType [f lang.Field Type]
     (.-type f)))




  (lang.makeStruct/invoke "lang2/MyTestStruct"
                          (into-array lang.Field [(lang.Field/Field "x" Type/INT_TYPE)]))


  (lang.makeEnum/invoke "lang2/Field"
                        (into-array lang.EnumVariant
                                    [(lang.EnumVariant/Variant
                                      "Field"
                                      (into-array lang.Field
                                                  [(lang.Field/Field "name" (Type/getType String))
                                                   (lang.Field/Field "type" (Type/getType Type))]))]))



  (lang.makeEnum/invoke "lang2/EnumVariant"
                        (into-array lang.EnumVariant
                                    [(lang.EnumVariant/Variant
                                      "Variant"
                                      (into-array lang.Field
                                                  [(lang.Field/Field "name" (Type/getType String))
                                                   (lang.Field/Field "fields" (array-type 'lang2.Field))]))]))



  [(lang2.EnumVariant/Variant
    "Variant"
    (into-array lang2.Field
                [(lang2.Field/Field "name" (Type/getType String))
                 (lang2.Field/Field "fields" (array-type 'lang2.Field))]))]

  (lang2.EnumVariant/Variant
   "Field"
   (into-array lang2.Field
               [(lang2.Field/Field "name" (Type/getType String))
                (lang2.Field/Field "type" (Type/getType Type))]))



  (do
    (lang.makeFn/invoke "lang/TestClassFn"
                        (into-array lang.Code  [(lang.Code/Int 42)
                                                (lang.Code/Arg 0)
                                                (lang.Code/Print)
                                                (lang.Code/MultInt)
                                                (lang.Code/Print)
                                                (lang.Code/Return)])
                        Type/INT_TYPE
                        (into-array Type [Type/INT_TYPE]))


    (lang.TestClassFn/invoke 5))



  (do

    (backend/make-fn-with-callback
     "lang2.MyAwesomeCode"
     #(lang.testGeneratingCode/invoke %))

    [(lang2.MyAwesomeCode/invoke)



     (lang.factorial/invoke 5)]  )
  )
