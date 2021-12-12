(ns jml.l1.backend
  (:require [jml.core :as core])
  (:import [org.objectweb.asm Opcodes Type ClassWriter]
           [org.objectweb.asm.commons Method GeneratorAdapter]))

(core/jml
 (defalias GeneratorAdapter org.objectweb.asm.commons.GeneratorAdapter)
 (defalias ClassWriter  org.objectweb.asm.ClassWriter)
 (defalias Type org.objectweb.asm.Type)
 (defalias Label org.objectweb.asm.Label)
 (defalias Opcodes org.objectweb.asm.Opcodes)
 (defalias Class java.lang.Class)
 (defalias Method org.objectweb.asm.commons.Method)
 (defalias String java.lang.String)
 (defalias HashMap java.util.HashMap)

 (defenum lang.Field
   (Field name String
          type Type))

 (defenum lang.EnumVariant
   (Variant name String
            fields Array/lang.Field))

 (defenum lang.Code
   MultInt
   PlusInt
   SubInt
   Dup
   Pop
   Print
   Return
   Nil
   (Arg argIndex int)
   (Math op int
         opType Type)
   (GetStatic owner Type
              name String
              resultType Type)
   (InvokeStatic owner Type
                 method Method)
   (InvokeVirtual owner Type
                  method Method)
   (InvokeConstructor owner Type
                      method Method)
   (New owner Type)
   (Bool boolValue boolean)
   (Int intValue int)
   (String stringValue String)
   (GetField owner Type
             name String
             fieldType Type)
   (PutField owner Type
             name String
             fieldType Type)
   (Label stringValue String)
   (JumpNotEqual stringValue String
                 compareType Type)
   (JumpEqual stringValue String
              compareType Type)
   (JumpCmp stringValue String
            compareType Type
            compareOp int)
   (Jump stringValue String)

   (StoreLocal name String
               localType Type)

   (LoadLocal name String)

   (Cast toType Type)

   (NewArray owner Type)
   ArrayLength
   (ArrayLoad owner Type)
   (ArrayStore owner Type)
   
   (LocalMeta localObj int
              localType Type))
 

 (defn lang.generateCode [gen GeneratorAdapter code lang.Code void]


   ;; We are missing some cases here
   ;; We know we are missing JumpCmp
   (cond

     (.equals (.-tagName code) "MultInt")
     (.math gen GeneratorAdapter/MUL org.objectweb.asm.Type/INT_TYPE)

     (.equals (.-tagName code) "PlusInt")
     (lang.generateCode/invoke gen (lang.Code/Math GeneratorAdapter/ADD Type/INT_TYPE))

     (.equals (.-tagName code) "SubInt")
     (lang.generateCode/invoke gen (lang.Code/Math GeneratorAdapter/SUB Type/INT_TYPE))

     (.String/equals (.-tagName code) "Arg")
     (.loadArg gen  (.-argIndex code))

     (.equals (.-tagName code) "Math")
     (.math gen (.-op code) (.-opType code))

     (.equals (.-tagName code) "GetField")
     (.getField gen (.-owner code) (.-name code) (.-fieldType code))

     (.equals (.-tagName code) "PutField")
     (.putField gen (.-owner code) (.-name code) (.-fieldType code))

     (.equals (.-tagName code) "GetStatic")
     (.getStatic gen (.-owner code) (.-name code) (.-resultType code))

     (.equals (.-tagName code) "InvokeStatic")
     (.invokeStatic gen (.-owner code) (.-method code))

     (.equals (.-tagName code) "InvokeVirtual")
     (.invokeVirtual gen (.-owner code) (.-method code))

     (.equals (.-tagName code) "InvokeConstructor")
     (.invokeConstructor gen (.-owner code) (.-method code))

     (.equals (.-tagName code) "New")
     (do
       (.newInstance gen (.-owner code))
       (.dup gen))

     (.equals (.-tagName code) "Bool")
     (.push gen (.-boolValue code))

     (.equals (.-tagName code) "Int")
     (.push gen (.-intValue code))

     (.equals (.-tagName code) "String")
     (.push gen ^String (.-stringValue code))

     (.equals (.-tagName code) "Nil")
     (.visitInsn gen Opcodes/ACONST_NULL)

     (.equals (.-tagName code) "Dup")
     (.dup gen)

     (.equals (.-tagName code) "Pop")
     (.pop gen)

     ;; Fix print to use objects instead of ints
     (.equals (.-tagName code) "Print")
     (do
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
                       (let [arg-types (new-array Type 1) Array/Type]
                         (array-store arg-types 0 Type/INT_TYPE)
                         (Method. "println" Type/VOID_TYPE arg-types))))
     (.equals (.-tagName code) "Return")
     (.returnValue gen)

     (.equals (.-tagName code) "NewArray")
     (.newArray gen (.-owner code))

     (.equals (.-tagName code) "ArrayLength")
     (.arrayLength gen)

     (.equals (.-tagName code) "ArrayLoad")
     (.arrayLoad gen (.-owner code))

     (.equals (.-tagName code) "ArrayStore")
     (do
       (.arrayStore gen (.-owner code))
       (.visitInsn gen Opcodes/ACONST_NULL))
     
     

     :else (do (print (.-tagName code)) nil)))



 (defn lang.generateCodeWithEnv
   [gen GeneratorAdapter code lang.Code env HashMap HashMap]
   (cond
     (.equals (.-tagName code) "Label")
     (if (.containsKey env (.-stringValue code))
       (let [label (.get env (.-stringValue code)) Label]
         (.mark gen label))

       (let [label (.newLabel gen) Label]
         (.mark gen label)
         (.put env (.-stringValue code) label)
         nil))

     (.equals (.-tagName code) "JumpNotEqual")
     (if (.containsKey env (.-stringValue code))
       (let [label (.get env (.-stringValue code)) Label]
         (.ifCmp gen (.-compareType code) GeneratorAdapter/NE label)
         nil)
       (let [label (.newLabel gen) Label]
         (.ifCmp gen (.-compareType code) GeneratorAdapter/NE label)
         (.put env (.-stringValue code) label)
         nil))

     (.equals (.-tagName code) "JumpEqual")
     (if (.containsKey env (.-stringValue code))
       (let [label (.get env (.-stringValue code)) Label]
         (.ifCmp gen (.-compareType code) GeneratorAdapter/EQ label)
         nil)
       (let [label (.newLabel gen) Label]
         (.ifCmp gen (.-compareType code) GeneratorAdapter/EQ label)
         (.put env (.-stringValue code) label)
         nil))


     (.equals (.-tagName code) "JumpCmp")
     (if (.containsKey env (.-stringValue code))
       (let [label (.get env (.-stringValue code)) Label]
         (.ifCmp gen (.-compareType code) (.-compareOp code) label)
         nil)
       (let [label (.newLabel gen) Label]
         (.ifCmp gen (.-compareType code) (.-compareOp code) label)
         (.put env (.-stringValue code) label)
         nil))


     (.equals (.-tagName code) "Jump")
     (if (.containsKey env (.-stringValue code))
       (let [label (.get env (.-stringValue code)) Label]
         (.goTo gen label))
       (let [label (.newLabel gen) Label]
         (.goTo gen label)
         (.put env (.-stringValue code) label)
         nil))

     (.equals (.-tagName code) "StoreLocal")
     (if (.containsKey env (.concat  "local-" (.-name code)))
       (let [local (.get env (.concat  "local-" (.-name code))) lang.Code]
         (.storeLocal gen (.-localObj local) (.-localType local))
         (lang.generateCode/invoke gen (lang.Code/Nil))
         nil)
       (let [local2 (.newLocal gen (.-localType code)) int]
         (.storeLocal gen local2 (.-localType code))
         (lang.generateCode/invoke gen (lang.Code/Nil))
         (.put env (.concat "local-" (.-name code)) (lang.Code/LocalMeta local2 (.-localType code)))
         nil))

     (.equals (.-tagName code) "LoadLocal")
     (if (.containsKey env (.concat  "local-" (.-name code)))
       (let [local (.get env (.concat  "local-" (.-name code))) lang.Code]
         (.loadLocal gen (.-localObj local) (.-localType local))
         nil)
       ;; Make this error
       nil)


     :else
     (lang.generateCode/invoke gen code))
   env)




 (defn lang.generateDefaultConstructor [writer ClassWriter void]
   (let [signature nil String
         exceptions nil Array/Type
         gen (GeneratorAdapter.
              Opcodes/ACC_PUBLIC
              (Method/getMethod "void <init>()")
              signature
              exceptions
              writer)
         GeneratorAdapter]
     (.visitCode gen)
     (.loadThis gen)
     (.invokeConstructor gen (Type/getType (Class/forName "java.lang.Object"))  (Method/getMethod "void <init>()"))
     (.returnValue gen)
     (.endMethod gen)))

 (defn lang.initializeClass [writer ClassWriter className String void]
   (let [signature nil String
         interfaces nil Array/String]
     (.visit writer Opcodes/V1_8 Opcodes/ACC_PUBLIC className signature "java/lang/Object" interfaces)))


 (defn lang.generateAllTheCode [gen GeneratorAdapter code Array/lang.Code void]
   (let [env (java.util.HashMap.) Map]
     (foreach [c code lang.Code]
              (lang.generateCodeWithEnv/invoke gen c env))))


 (defn lang.generateInvokeMethod [writer ClassWriter
                                  code Array/lang.Code
                                  returnType Type
                                  argTypes Array/Type
                                  void]
   (let [method (Method. "invoke" returnType argTypes) Method
         signature nil String
         exceptions nil Array/Type
         gen (GeneratorAdapter.
              (plus-int Opcodes/ACC_PUBLIC Opcodes/ACC_STATIC)
              method signature exceptions writer) GeneratorAdapter]
     (lang.generateAllTheCode/invoke gen code)
     (.endMethod gen)))




 (defn lang.defineClass [writer ClassWriter class-name String java.lang.Class]
     (let [byteArray (.toByteArray writer) Array/byte
           aThing nil java.lang.Object]
       (.defineClass (clojure.lang.DynamicClassLoader.)
                     (.replace class-name "/" ".")
                     byteArray
                     aThing)))


 (defn lang.makeFn [class-name String
                    code Array/lang.Code
                    returnType Type
                    argTypes Array/Type
                    java.lang.Class]
   (let [writer (ClassWriter. (plus-int ClassWriter/COMPUTE_FRAMES ClassWriter/COMPUTE_MAXS)) ClassWriter]
     (lang.initializeClass/invoke writer class-name)
     (lang.generateDefaultConstructor/invoke writer)
     (lang.generateInvokeMethod/invoke writer code returnType argTypes)
     (.visitEnd writer)
     (lang.defineClass/invoke writer class-name)))

 (defn lang.makeField [writer ClassWriter
                       field lang.Field
                       void]
   (let [signature nil String
         value nil java.lang.Object
         fieldViz (.visitField writer Opcodes/ACC_PUBLIC (.-name field)
                            (.getDescriptor (.-type field))
                            signature value) org.objectweb.asm.FieldVisitor]
     (.visitEnd fieldViz)))

 (defn lang.makeFieldAssignment [gen GeneratorAdapter
                                 this-type Type
                                 index int
                                 field lang.Field
                                 void]
   (.loadThis gen)
   (.loadArg gen index)
   (.putField gen this-type (.-name field) (.-type field)))

 (defn lang.makeFieldAssignmentOnStack [gen GeneratorAdapter
                                        this-type Type
                                        index int
                                        field lang.Field
                                        void]
   (.dup gen)
   (.loadArg gen index)
   (.putField gen this-type (.-name field) (.-type field)))

 (defn lang.fieldsToTypes [fields Array/lang.Field Array/Type]
   (let [types (new-array Type (array-length fields)) Array/Type
         i 0 int]
     (foreach [field fields lang.Field]
              (array-store types i (.-type field))
              (set! i (plus-int i 1)))
     types))

 (defn lang.makeStructConstructor [writer ClassWriter
                                   class-name String
                                   fields Array/lang.Field
                                   void]
   (let [INIT (Method/getMethod "void <init>()") Method
         signature nil String
         exceptions nil java.lang.Object
         ctor-method (Method. "<init>" Type/VOID_TYPE (lang.fieldsToTypes/invoke fields)) Method
         gen (GeneratorAdapter.
               Opcodes/ACC_PUBLIC
              ctor-method signature exceptions writer) GeneratorAdapter
         this-type (Type/getType (.concat (.concat "L" class-name)
                                          ";")) Type]
     (.visitCode gen)
     (.loadThis gen)
     (.invokeConstructor gen (Type/getType (Class/forName "java.lang.Object")) INIT)
     (let [i 0 int]
       (foreach [f fields lang.Field]
                (lang.makeFieldAssignment/invoke gen this-type i f)
                (set! i (plus-int i 1))))

     (.returnValue gen)
     (.endMethod gen)))


 (defn lang.makeStruct [class-name String
                        fields Array/lang.Field
                        String]
   (let [writer (ClassWriter. (plus-int  ClassWriter/COMPUTE_FRAMES ClassWriter/COMPUTE_MAXS)) ClassWriter]
     (lang.initializeClass/invoke writer class-name)
     (lang.generateDefaultConstructor/invoke writer)
     (foreach [f fields lang.Field]
              (lang.makeField/invoke writer f))
     (lang.makeStructConstructor/invoke writer class-name fields)
     (.visitEnd writer)
     (jml.decompile$print_and_load_bytecode/invokeStatic writer class-name)
     class-name))

 (defn lang.makeEnumFactory [writer ClassWriter
                             class-name String
                             name String
                             fields Array/lang.Field
                             void]
   (let [INIT (Method/getMethod "void <init>()") Method
         this-type (Type/getType (.concat (.concat "L" class-name)
                                          ";")) Type
         method (Method. name this-type (lang.fieldsToTypes/invoke fields)) Method
         signature nil String
         exceptions nil java.lang.Object
         gen (GeneratorAdapter. (plus-int Opcodes/ACC_PUBLIC Opcodes/ACC_STATIC)
                                method
                                signature
                                exceptions
                                writer) GeneratorAdapter]
     (.visitCode gen)
     (.newInstance gen this-type)
     (.dup gen)
     (.invokeConstructor gen this-type INIT)

     ;;adding tagName field
     (.dup gen)
     (.push gen name)
     (.putField gen this-type "tagName" (Type/getType (Class/forName "java.lang.String")))

     (let [i 0 int]
       (foreach [f fields lang.Field]
                (lang.makeFieldAssignmentOnStack/invoke gen this-type i f)
                (set! i (plus-int i 1))))
     (.returnValue gen)
     (.endMethod gen)))

 (defn lang.makeEnum [class-name String variants Array/lang.EnumVariant void]
   (let [writer (ClassWriter. (plus-int ClassWriter/COMPUTE_FRAMES ClassWriter/COMPUTE_MAXS)) ClassWriter]
     (lang.initializeClass/invoke writer class-name)
     (lang.generateDefaultConstructor/invoke writer)
     (lang.makeField/invoke writer (lang.Field/Field "tagName" (Type/getType (Class/forName "java.lang.String"))))
     ;; TODO make-to-string

     (let [seen-fields (java.util.HashSet.) java.util.HashSet]
       (foreach [v variants lang.EnumVariant]
                ;; TODO use set to remove duplicate fields

                (foreach [f (.-fields v) lang.Field]
                         (if (.contains seen-fields (.-name f))
                           nil
                           (do
                             (.add seen-fields (.-name f))
                             (lang.makeField/invoke writer f))))))

     (foreach [v variants lang.EnumVariant]
              (lang.makeEnumFactory/invoke writer class-name (.-name v) (.-fields v)))
     (.visitEnd writer)
     (jml.decompile$print_and_load_bytecode/invokeStatic writer class-name)
     nil)))






(comment

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
                                                     (lang.Field/Field "fields" (core/array-type 'lang2.Field))]))]))



    [(lang2.EnumVariant/Variant
      "Variant"
      (into-array lang2.Field
                  [(lang2.Field/Field "name" (Type/getType String))
                   (lang2.Field/Field "fields" (core/array-type 'lang2.Field))]))]

    (lang2.EnumVariant/Variant
     "Field"
     (into-array lang2.Field
                 [(lang2.Field/Field "name" (Type/getType String))
                  (lang2.Field/Field "type" (Type/getType Type))])))

  )
