(ns jml.l1.compilel1
  (:import [org.objectweb.asm Opcodes Type ClassWriter]
           [org.objectweb.asm.commons Method GeneratorAdapter]))




;; CODE

(lang.makeFn/invoke
 "lang2/generateCode"
 (into-array
  lang.Code
  [(lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "MultInt")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8844" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "PlusInt")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8846" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "SubInt")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8848" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "Arg")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8850" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "Cast")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8852" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "Math")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8854" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "GetField")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8856" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "PutField")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8858" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "GetStatic")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8860" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "InvokeStatic")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8862" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "InvokeVirtual")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8864" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "InvokeConstructor")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8866" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "New")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8868" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "Bool")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8870" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "Int")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8872" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "String")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8874" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "Nil")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8876" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "Dup")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8878" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "Pop")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8880" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "Print")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8882" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "Return")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8884" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "NewArray")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8886" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "ArrayLength")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8888" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "ArrayLoad")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8890" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "ArrayStore")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8892" (Type/getType "Z") 153)
   (lang.Code/Nil)
   (lang.Code/Jump "exit_label_8893")
   (lang.Code/Label "true_label_8892")
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "owner"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "arrayStore" "(Lorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/Opcodes;")
    "ACONST_NULL"
    (Type/getType "I"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "visitInsn" "(I)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8893")
   (lang.Code/Jump "exit_label_8891")
   (lang.Code/Label "true_label_8890")
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "owner"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "arrayLoad" "(Lorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8891")
   (lang.Code/Jump "exit_label_8889")
   (lang.Code/Label "true_label_8888")
   (lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "arrayLength" "()V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8889")
   (lang.Code/Jump "exit_label_8887")
   (lang.Code/Label "true_label_8886")
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "owner"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "newArray" "(Lorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8887")
   (lang.Code/Jump "exit_label_8885")
   (lang.Code/Label "true_label_8884")
   (lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "returnValue" "()V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8885")
   (lang.Code/Jump "exit_label_8883")
   (lang.Code/Label "true_label_8882")
   (lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "dup" "()V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/String "java.lang.System")
   (lang.Code/InvokeStatic
    (Type/getType "Ljava/lang/Class;")
    (Method. "forName" "(Ljava/lang/String;)Ljava/lang/Class;"))
   (lang.Code/InvokeStatic
    (Type/getType "Lorg/objectweb/asm/Type;")
    (Method. "getType" "(Ljava/lang/Class;)Lorg/objectweb/asm/Type;"))
   (lang.Code/String "out")
   (lang.Code/String "java.lang.System")
   (lang.Code/InvokeStatic
    (Type/getType "Ljava/lang/Class;")
    (Method. "forName" "(Ljava/lang/String;)Ljava/lang/Class;"))
   (lang.Code/String "out")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/Class;")
    (Method.
     "getDeclaredField"
     "(Ljava/lang/String;)Ljava/lang/reflect/Field;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/reflect/Field;")
    (Method. "getType" "()Ljava/lang/Class;"))
   (lang.Code/InvokeStatic
    (Type/getType "Lorg/objectweb/asm/Type;")
    (Method. "getType" "(Ljava/lang/Class;)Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "getStatic"
     "(Lorg/objectweb/asm/Type;Ljava/lang/String;Lorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "swap" "()V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/String "java.lang.System")
   (lang.Code/InvokeStatic
    (Type/getType "Ljava/lang/Class;")
    (Method. "forName" "(Ljava/lang/String;)Ljava/lang/Class;"))
   (lang.Code/String "out")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/Class;")
    (Method.
     "getDeclaredField"
     "(Ljava/lang/String;)Ljava/lang/reflect/Field;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/reflect/Field;")
    (Method. "getType" "()Ljava/lang/Class;"))
   (lang.Code/InvokeStatic
    (Type/getType "Lorg/objectweb/asm/Type;")
    (Method. "getType" "(Ljava/lang/Class;)Lorg/objectweb/asm/Type;"))
   (lang.Code/Int 1)
   (lang.Code/NewArray (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/StoreLocal
    "arg-types"
    (Type/getType "[Lorg/objectweb/asm/Type;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "arg-types")
   (lang.Code/Int 0)
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/Type;")
    "INT_TYPE"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/ArrayStore (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/Pop)
   (lang.Code/New (Type/getType "Lorg/objectweb/asm/commons/Method;"))
   (lang.Code/String "println")
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/Type;")
    "VOID_TYPE"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/LoadLocal "arg-types")
   (lang.Code/InvokeConstructor
    (Type/getType "Lorg/objectweb/asm/commons/Method;")
    (Method.
     "<init>"
     "(Ljava/lang/String;Lorg/objectweb/asm/Type;[Lorg/objectweb/asm/Type;)V"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "invokeVirtual"
     "(Lorg/objectweb/asm/Type;Lorg/objectweb/asm/commons/Method;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8883")
   (lang.Code/Jump "exit_label_8881")
   (lang.Code/Label "true_label_8880")
   (lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "pop" "()V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8881")
   (lang.Code/Jump "exit_label_8879")
   (lang.Code/Label "true_label_8878")
   (lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "dup" "()V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8879")
   (lang.Code/Jump "exit_label_8877")
   (lang.Code/Label "true_label_8876")
   (lang.Code/Arg 0)
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/Opcodes;")
    "ACONST_NULL"
    (Type/getType "I"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "visitInsn" "(I)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8877")
   (lang.Code/Jump "exit_label_8875")
   (lang.Code/Label "true_label_8874")
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "stringValue"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "push" "(Ljava/lang/String;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8875")
   (lang.Code/Jump "exit_label_8873")
   (lang.Code/Label "true_label_8872")
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "intValue"
    (Type/getType "I"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "push" "(I)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8873")
   (lang.Code/Jump "exit_label_8871")
   (lang.Code/Label "true_label_8870")
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "boolValue"
    (Type/getType "Z"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "push" "(Z)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8871")
   (lang.Code/Jump "exit_label_8869")
   (lang.Code/Label "true_label_8868")
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "owner"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "newInstance" "(Lorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "dup" "()V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8869")
   (lang.Code/Jump "exit_label_8867")
   (lang.Code/Label "true_label_8866")
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "owner"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "method"
    (Type/getType "Lorg/objectweb/asm/commons/Method;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "invokeConstructor"
     "(Lorg/objectweb/asm/Type;Lorg/objectweb/asm/commons/Method;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8867")
   (lang.Code/Jump "exit_label_8865")
   (lang.Code/Label "true_label_8864")
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "owner"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "method"
    (Type/getType "Lorg/objectweb/asm/commons/Method;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "invokeVirtual"
     "(Lorg/objectweb/asm/Type;Lorg/objectweb/asm/commons/Method;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8865")
   (lang.Code/Jump "exit_label_8863")
   (lang.Code/Label "true_label_8862")
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "owner"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "method"
    (Type/getType "Lorg/objectweb/asm/commons/Method;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "invokeStatic"
     "(Lorg/objectweb/asm/Type;Lorg/objectweb/asm/commons/Method;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8863")
   (lang.Code/Jump "exit_label_8861")
   (lang.Code/Label "true_label_8860")
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "owner"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "name"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "resultType"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "getStatic"
     "(Lorg/objectweb/asm/Type;Ljava/lang/String;Lorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8861")
   (lang.Code/Jump "exit_label_8859")
   (lang.Code/Label "true_label_8858")
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "owner"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "name"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "fieldType"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "putField"
     "(Lorg/objectweb/asm/Type;Ljava/lang/String;Lorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8859")
   (lang.Code/Jump "exit_label_8857")
   (lang.Code/Label "true_label_8856")
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "owner"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "name"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "fieldType"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "getField"
     "(Lorg/objectweb/asm/Type;Ljava/lang/String;Lorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8857")
   (lang.Code/Jump "exit_label_8855")
   (lang.Code/Label "true_label_8854")
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "op"
    (Type/getType "I"))
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "opType"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "math" "(ILorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8855")
   (lang.Code/Jump "exit_label_8853")
   (lang.Code/Label "true_label_8852")
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "toType"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "checkCast" "(Lorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8853")
   (lang.Code/Jump "exit_label_8851")
   (lang.Code/Label "true_label_8850")
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "argIndex"
    (Type/getType "I"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "loadArg" "(I)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8851")
   (lang.Code/Jump "exit_label_8849")
   (lang.Code/Label "true_label_8848")
   (lang.Code/Arg 0)
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    "SUB"
    (Type/getType "I"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/Type;")
    "INT_TYPE"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeStatic
    (Type/getType "Llang/Code;")
    (Method. "Math" "(ILorg/objectweb/asm/Type;)Llang/Code;"))
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/generateCode;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/commons/GeneratorAdapter;Llang/Code;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8849")
   (lang.Code/Jump "exit_label_8847")
   (lang.Code/Label "true_label_8846")
   (lang.Code/Arg 0)
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    "ADD"
    (Type/getType "I"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/Type;")
    "INT_TYPE"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeStatic
    (Type/getType "Llang/Code;")
    (Method. "Math" "(ILorg/objectweb/asm/Type;)Llang/Code;"))
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/generateCode;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/commons/GeneratorAdapter;Llang/Code;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8847")
   (lang.Code/Jump "exit_label_8845")
   (lang.Code/Label "true_label_8844")
   (lang.Code/Arg 0)
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    "MUL"
    (Type/getType "I"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/Type;")
    "INT_TYPE"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "math" "(ILorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8845")
   (lang.Code/Return)])
 (Type/getType "V")
 (into-array
  Type
  [(Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
   (Type/getType "Llang/Code;")]))


(lang.makeFn/invoke
 "lang2/generateAllTheCode"
 (into-array
  lang.Code
  [(lang.Code/New (Type/getType "Ljava/util/HashMap;"))
   (lang.Code/InvokeConstructor
    (Type/getType "Ljava/util/HashMap;")
    (Method. "<init>" "()V"))
   (lang.Code/StoreLocal "env" (Type/getType "LMap;"))
   (lang.Code/Pop)
   (lang.Code/Int 0)
   (lang.Code/StoreLocal "i8922" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Arg 1)
   (lang.Code/StoreLocal "arr8923" (Type/getType "[Llang/Code;"))
   (lang.Code/Pop)
   (lang.Code/Label "while-loop_8924")
   (lang.Code/LoadLocal "i8922")
   (lang.Code/LoadLocal "arr8923")
   (lang.Code/ArrayLength)
   (lang.Code/JumpCmp "while-exit_8925" (Type/getType "I") 156)
   (lang.Code/LoadLocal "arr8923")
   (lang.Code/LoadLocal "i8922")
   (lang.Code/ArrayLoad (Type/getType "Llang/Code;"))
   (lang.Code/StoreLocal "c" (Type/getType "Llang/Code;"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/LoadLocal "c")
   (lang.Code/LoadLocal "env")
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/generateCodeWithEnv;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/commons/GeneratorAdapter;Llang/Code;Ljava/util/HashMap;)Ljava/util/HashMap;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "i8922")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i8922" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Jump "while-loop_8924")
   (lang.Code/Label "while-exit_8925")
   (lang.Code/Nil)
   (lang.Code/Return)])
 (Type/getType "V")
 (into-array
  Type
  [(Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
   (Type/getType "[Llang/Code;")]))


(lang.makeFn/invoke
 "lang2/makeStructConstructor"
 (into-array
  lang.Code
  [(lang.Code/String "void <init>()")
   (lang.Code/InvokeStatic
    (Type/getType "Lorg/objectweb/asm/commons/Method;")
    (Method.
     "getMethod"
     "(Ljava/lang/String;)Lorg/objectweb/asm/commons/Method;"))
   (lang.Code/StoreLocal
    "INIT"
    (Type/getType "Lorg/objectweb/asm/commons/Method;"))
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/StoreLocal
    "signature"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/StoreLocal
    "exceptions"
    (Type/getType "Ljava/lang/Object;"))
   (lang.Code/Pop)
   (lang.Code/New (Type/getType "Lorg/objectweb/asm/commons/Method;"))
   (lang.Code/String "<init>")
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/Type;")
    "VOID_TYPE"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/Arg 2)
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/fieldsToTypes;")
    (Method. "invoke" "([Llang/Field;)[Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeConstructor
    (Type/getType "Lorg/objectweb/asm/commons/Method;")
    (Method.
     "<init>"
     "(Ljava/lang/String;Lorg/objectweb/asm/Type;[Lorg/objectweb/asm/Type;)V"))
   (lang.Code/StoreLocal
    "ctor-method"
    (Type/getType "Lorg/objectweb/asm/commons/Method;"))
   (lang.Code/Pop)
   (lang.Code/New
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/Opcodes;")
    "ACC_PUBLIC"
    (Type/getType "I"))
   (lang.Code/LoadLocal "ctor-method")
   (lang.Code/LoadLocal "signature")
   (lang.Code/LoadLocal "exceptions")
   (lang.Code/Arg 0)
   (lang.Code/InvokeConstructor
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "<init>"
     "(ILorg/objectweb/asm/commons/Method;Ljava/lang/String;[Lorg/objectweb/asm/Type;Lorg/objectweb/asm/ClassVisitor;)V"))
   (lang.Code/StoreLocal
    "gen"
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;"))
   (lang.Code/Pop)
   (lang.Code/String "L")
   (lang.Code/Arg 1)
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "concat" "(Ljava/lang/String;)Ljava/lang/String;"))
   (lang.Code/String ";")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "concat" "(Ljava/lang/String;)Ljava/lang/String;"))
   (lang.Code/InvokeStatic
    (Type/getType "Lorg/objectweb/asm/Type;")
    (Method. "getType" "(Ljava/lang/String;)Lorg/objectweb/asm/Type;"))
   (lang.Code/StoreLocal
    "this-type"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "visitCode" "()V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "loadThis" "()V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/String "java.lang.Object")
   (lang.Code/InvokeStatic
    (Type/getType "Ljava/lang/Class;")
    (Method. "forName" "(Ljava/lang/String;)Ljava/lang/Class;"))
   (lang.Code/InvokeStatic
    (Type/getType "Lorg/objectweb/asm/Type;")
    (Method. "getType" "(Ljava/lang/Class;)Lorg/objectweb/asm/Type;"))
   (lang.Code/LoadLocal "INIT")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "invokeConstructor"
     "(Lorg/objectweb/asm/Type;Lorg/objectweb/asm/commons/Method;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Int 0)
   (lang.Code/StoreLocal "i" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Int 0)
   (lang.Code/StoreLocal "i8930" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Arg 2)
   (lang.Code/StoreLocal "arr8931" (Type/getType "[Llang/Field;"))
   (lang.Code/Pop)
   (lang.Code/Label "while-loop_8932")
   (lang.Code/LoadLocal "i8930")
   (lang.Code/LoadLocal "arr8931")
   (lang.Code/ArrayLength)
   (lang.Code/JumpCmp "while-exit_8933" (Type/getType "I") 156)
   (lang.Code/LoadLocal "arr8931")
   (lang.Code/LoadLocal "i8930")
   (lang.Code/ArrayLoad (Type/getType "Llang/Field;"))
   (lang.Code/StoreLocal "f" (Type/getType "Llang/Field;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/LoadLocal "this-type")
   (lang.Code/LoadLocal "i")
   (lang.Code/LoadLocal "f")
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/makeFieldAssignment;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/commons/GeneratorAdapter;Lorg/objectweb/asm/Type;ILlang/Field;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "i")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "i8930")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i8930" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Jump "while-loop_8932")
   (lang.Code/Label "while-exit_8933")
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "returnValue" "()V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "endMethod" "()V"))
   (lang.Code/Nil)
   (lang.Code/Return)])
 (Type/getType "V")
 (into-array
  Type
  [(Type/getType "Lorg/objectweb/asm/ClassWriter;")
   (Type/getType "Ljava/lang/String;")
   (Type/getType "[Llang/Field;")]))


(lang.makeFn/invoke
 "lang2/initializeClass"
 (into-array
  lang.Code
  [(lang.Code/Nil)
   (lang.Code/StoreLocal
    "signature"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/StoreLocal
    "interfaces"
    (Type/getType "[Ljava/lang/String;"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/Opcodes;")
    "V1_8"
    (Type/getType "I"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/Opcodes;")
    "ACC_PUBLIC"
    (Type/getType "I"))
   (lang.Code/Arg 1)
   (lang.Code/LoadLocal "signature")
   (lang.Code/String "java/lang/Object")
   (lang.Code/LoadLocal "interfaces")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/ClassWriter;")
    (Method.
     "visit"
     "(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;)V"))
   (lang.Code/Nil)
   (lang.Code/Return)])
 (Type/getType "V")
 (into-array
  Type
  [(Type/getType "Lorg/objectweb/asm/ClassWriter;")
   (Type/getType "Ljava/lang/String;")]))


(lang.makeFn/invoke
 "lang2/makeFieldAssignmentOnStack"
 (into-array
  lang.Code
  [(lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "dup" "()V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/Arg 2)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "loadArg" "(I)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/Arg 3)
   (lang.Code/GetField
    (Type/getType "Llang/Field;")
    "name"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/Arg 3)
   (lang.Code/GetField
    (Type/getType "Llang/Field;")
    "type"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "putField"
     "(Lorg/objectweb/asm/Type;Ljava/lang/String;Lorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Return)])
 (Type/getType "V")
 (into-array
  Type
  [(Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
   (Type/getType "Lorg/objectweb/asm/Type;")
   (Type/getType "I")
   (Type/getType "Llang/Field;")]))


(lang.makeFn/invoke
 "lang2/makeEnum"
 (into-array
  lang.Code
  [(lang.Code/New (Type/getType "Lorg/objectweb/asm/ClassWriter;"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/ClassWriter;")
    "COMPUTE_FRAMES"
    (Type/getType "I"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/ClassWriter;")
    "COMPUTE_MAXS"
    (Type/getType "I"))
   (lang.Code/PlusInt)
   (lang.Code/InvokeConstructor
    (Type/getType "Lorg/objectweb/asm/ClassWriter;")
    (Method. "<init>" "(I)V"))
   (lang.Code/StoreLocal
    "writer"
    (Type/getType "Lorg/objectweb/asm/ClassWriter;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/Arg 0)
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/initializeClass;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/ClassWriter;Ljava/lang/String;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/generateDefaultConstructor;")
    (Method. "invoke" "(Lorg/objectweb/asm/ClassWriter;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/String "tagName")
   (lang.Code/String "java.lang.String")
   (lang.Code/InvokeStatic
    (Type/getType "Ljava/lang/Class;")
    (Method. "forName" "(Ljava/lang/String;)Ljava/lang/Class;"))
   (lang.Code/InvokeStatic
    (Type/getType "Lorg/objectweb/asm/Type;")
    (Method. "getType" "(Ljava/lang/Class;)Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeStatic
    (Type/getType "Llang/Field;")
    (Method.
     "Field"
     "(Ljava/lang/String;Lorg/objectweb/asm/Type;)Llang/Field;"))
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/makeField;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/ClassWriter;Llang/Field;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/New (Type/getType "Ljava/util/HashSet;"))
   (lang.Code/InvokeConstructor
    (Type/getType "Ljava/util/HashSet;")
    (Method. "<init>" "()V"))
   (lang.Code/StoreLocal
    "seen-fields"
    (Type/getType "Ljava/util/HashSet;"))
   (lang.Code/Pop)
   (lang.Code/Int 0)
   (lang.Code/StoreLocal "i8944" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Arg 1)
   (lang.Code/StoreLocal
    "arr8945"
    (Type/getType "[Llang/EnumVariant;"))
   (lang.Code/Pop)
   (lang.Code/Label "while-loop_8948")
   (lang.Code/LoadLocal "i8944")
   (lang.Code/LoadLocal "arr8945")
   (lang.Code/ArrayLength)
   (lang.Code/JumpCmp "while-exit_8949" (Type/getType "I") 156)
   (lang.Code/LoadLocal "arr8945")
   (lang.Code/LoadLocal "i8944")
   (lang.Code/ArrayLoad (Type/getType "Llang/EnumVariant;"))
   (lang.Code/StoreLocal "v" (Type/getType "Llang/EnumVariant;"))
   (lang.Code/Pop)
   (lang.Code/Int 0)
   (lang.Code/StoreLocal "i8942" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "v")
   (lang.Code/GetField
    (Type/getType "Llang/EnumVariant;")
    "fields"
    (Type/getType "[Llang/Field;"))
   (lang.Code/StoreLocal "arr8943" (Type/getType "[Llang/Field;"))
   (lang.Code/Pop)
   (lang.Code/Label "while-loop_8950")
   (lang.Code/LoadLocal "i8942")
   (lang.Code/LoadLocal "arr8943")
   (lang.Code/ArrayLength)
   (lang.Code/JumpCmp "while-exit_8951" (Type/getType "I") 156)
   (lang.Code/LoadLocal "arr8943")
   (lang.Code/LoadLocal "i8942")
   (lang.Code/ArrayLoad (Type/getType "Llang/Field;"))
   (lang.Code/StoreLocal "f" (Type/getType "Llang/Field;"))
   (lang.Code/Pop)
   (lang.Code/Bool true)
   (lang.Code/LoadLocal "seen-fields")
   (lang.Code/LoadLocal "f")
   (lang.Code/GetField
    (Type/getType "Llang/Field;")
    "name"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashSet;")
    (Method. "contains" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8952" (Type/getType "Z") 153)
   (lang.Code/LoadLocal "seen-fields")
   (lang.Code/LoadLocal "f")
   (lang.Code/GetField
    (Type/getType "Llang/Field;")
    "name"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashSet;")
    (Method. "add" "(Ljava/lang/Object;)Z"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/LoadLocal "f")
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/makeField;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/ClassWriter;Llang/Field;)V"))
   (lang.Code/Nil)
   (lang.Code/Jump "exit_label_8953")
   (lang.Code/Label "true_label_8952")
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8953")
   (lang.Code/Pop)
   (lang.Code/LoadLocal "i8942")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i8942" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Jump "while-loop_8950")
   (lang.Code/Label "while-exit_8951")
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "i8944")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i8944" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Jump "while-loop_8948")
   (lang.Code/Label "while-exit_8949")
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Int 0)
   (lang.Code/StoreLocal "i8946" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Arg 1)
   (lang.Code/StoreLocal
    "arr8947"
    (Type/getType "[Llang/EnumVariant;"))
   (lang.Code/Pop)
   (lang.Code/Label "while-loop_8954")
   (lang.Code/LoadLocal "i8946")
   (lang.Code/LoadLocal "arr8947")
   (lang.Code/ArrayLength)
   (lang.Code/JumpCmp "while-exit_8955" (Type/getType "I") 156)
   (lang.Code/LoadLocal "arr8947")
   (lang.Code/LoadLocal "i8946")
   (lang.Code/ArrayLoad (Type/getType "Llang/EnumVariant;"))
   (lang.Code/StoreLocal "v" (Type/getType "Llang/EnumVariant;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/Arg 0)
   (lang.Code/LoadLocal "v")
   (lang.Code/GetField
    (Type/getType "Llang/EnumVariant;")
    "name"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/LoadLocal "v")
   (lang.Code/GetField
    (Type/getType "Llang/EnumVariant;")
    "fields"
    (Type/getType "[Llang/Field;"))
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/makeEnumFactory;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/ClassWriter;Ljava/lang/String;Ljava/lang/String;[Llang/Field;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "i8946")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i8946" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Jump "while-loop_8954")
   (lang.Code/Label "while-exit_8955")
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/ClassWriter;")
    (Method. "visitEnd" "()V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/Arg 0)
   (lang.Code/InvokeStatic
    (Type/getType "Ljml/decompile$print_and_load_bytecode;")
    (Method.
     "invokeStatic"
     "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/Return)])
 (Type/getType "V")
 (into-array
  Type
  [(Type/getType "Ljava/lang/String;")
   (Type/getType "[Llang/EnumVariant;")]))


(lang.makeFn/invoke
 "lang2/generateInvokeMethod"
 (into-array
  lang.Code
  [(lang.Code/New (Type/getType "Lorg/objectweb/asm/commons/Method;"))
   (lang.Code/String "invoke")
   (lang.Code/Arg 2)
   (lang.Code/Arg 3)
   (lang.Code/InvokeConstructor
    (Type/getType "Lorg/objectweb/asm/commons/Method;")
    (Method.
     "<init>"
     "(Ljava/lang/String;Lorg/objectweb/asm/Type;[Lorg/objectweb/asm/Type;)V"))
   (lang.Code/StoreLocal
    "method"
    (Type/getType "Lorg/objectweb/asm/commons/Method;"))
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/StoreLocal
    "signature"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/StoreLocal
    "exceptions"
    (Type/getType "[Lorg/objectweb/asm/Type;"))
   (lang.Code/Pop)
   (lang.Code/New
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/Opcodes;")
    "ACC_PUBLIC"
    (Type/getType "I"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/Opcodes;")
    "ACC_STATIC"
    (Type/getType "I"))
   (lang.Code/PlusInt)
   (lang.Code/LoadLocal "method")
   (lang.Code/LoadLocal "signature")
   (lang.Code/LoadLocal "exceptions")
   (lang.Code/Arg 0)
   (lang.Code/InvokeConstructor
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "<init>"
     "(ILorg/objectweb/asm/commons/Method;Ljava/lang/String;[Lorg/objectweb/asm/Type;Lorg/objectweb/asm/ClassVisitor;)V"))
   (lang.Code/StoreLocal
    "gen"
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/Arg 1)
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/generateAllTheCode;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/commons/GeneratorAdapter;[Llang/Code;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "endMethod" "()V"))
   (lang.Code/Nil)
   (lang.Code/Return)])
 (Type/getType "V")
 (into-array
  Type
  [(Type/getType "Lorg/objectweb/asm/ClassWriter;")
   (Type/getType "[Llang/Code;")
   (Type/getType "Lorg/objectweb/asm/Type;")
   (Type/getType "[Lorg/objectweb/asm/Type;")]))


(lang.makeFn/invoke
 "lang2/makeStruct"
 (into-array
  lang.Code
  [(lang.Code/New (Type/getType "Lorg/objectweb/asm/ClassWriter;"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/ClassWriter;")
    "COMPUTE_FRAMES"
    (Type/getType "I"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/ClassWriter;")
    "COMPUTE_MAXS"
    (Type/getType "I"))
   (lang.Code/PlusInt)
   (lang.Code/InvokeConstructor
    (Type/getType "Lorg/objectweb/asm/ClassWriter;")
    (Method. "<init>" "(I)V"))
   (lang.Code/StoreLocal
    "writer"
    (Type/getType "Lorg/objectweb/asm/ClassWriter;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/Arg 0)
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/initializeClass;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/ClassWriter;Ljava/lang/String;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/generateDefaultConstructor;")
    (Method. "invoke" "(Lorg/objectweb/asm/ClassWriter;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Int 0)
   (lang.Code/StoreLocal "i8934" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Arg 1)
   (lang.Code/StoreLocal "arr8935" (Type/getType "[Llang/Field;"))
   (lang.Code/Pop)
   (lang.Code/Label "while-loop_8936")
   (lang.Code/LoadLocal "i8934")
   (lang.Code/LoadLocal "arr8935")
   (lang.Code/ArrayLength)
   (lang.Code/JumpCmp "while-exit_8937" (Type/getType "I") 156)
   (lang.Code/LoadLocal "arr8935")
   (lang.Code/LoadLocal "i8934")
   (lang.Code/ArrayLoad (Type/getType "Llang/Field;"))
   (lang.Code/StoreLocal "f" (Type/getType "Llang/Field;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/LoadLocal "f")
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/makeField;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/ClassWriter;Llang/Field;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "i8934")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i8934" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Jump "while-loop_8936")
   (lang.Code/Label "while-exit_8937")
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/makeStructConstructor;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/ClassWriter;Ljava/lang/String;[Llang/Field;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/ClassWriter;")
    (Method. "visitEnd" "()V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/Arg 0)
   (lang.Code/InvokeStatic
    (Type/getType "Ljml/decompile$print_and_load_bytecode;")
    (Method.
     "invokeStatic"
     "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/Return)])
 (Type/getType "Ljava/lang/String;")
 (into-array
  Type
  [(Type/getType "Ljava/lang/String;") (Type/getType "[Llang/Field;")]))


(lang.makeFn/invoke
 "lang2/generateCodeWithEnv"
 (into-array
  lang.Code
  [(lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "Label")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8894" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "JumpNotEqual")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8896" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "JumpEqual")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8898" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "JumpCmp")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8900" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "Jump")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8902" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "StoreLocal")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8904" (Type/getType "Z") 153)
   (lang.Code/Bool true)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "tagName"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/String "LoadLocal")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "equals" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8906" (Type/getType "Z") 153)
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/generateCode;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/commons/GeneratorAdapter;Llang/Code;)V"))
   (lang.Code/Nil)
   (lang.Code/Jump "exit_label_8907")
   (lang.Code/Label "true_label_8906")
   (lang.Code/Bool true)
   (lang.Code/Arg 2)
   (lang.Code/String "local-")
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "name"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "concat" "(Ljava/lang/String;)Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method. "containsKey" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8908" (Type/getType "Z") 153)
   (lang.Code/Nil)
   (lang.Code/Jump "exit_label_8909")
   (lang.Code/Label "true_label_8908")
   (lang.Code/Arg 2)
   (lang.Code/String "local-")
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "name"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "concat" "(Ljava/lang/String;)Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method. "get" "(Ljava/lang/Object;)Ljava/lang/Object;"))
   (lang.Code/Cast (Type/getType "Llang/Code;"))
   (lang.Code/StoreLocal "local" (Type/getType "Llang/Code;"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/LoadLocal "local")
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "localObj"
    (Type/getType "I"))
   (lang.Code/LoadLocal "local")
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "localType"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "loadLocal" "(ILorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8909")
   (lang.Code/Label "exit_label_8907")
   (lang.Code/Jump "exit_label_8905")
   (lang.Code/Label "true_label_8904")
   (lang.Code/Bool true)
   (lang.Code/Arg 2)
   (lang.Code/String "local-")
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "name"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "concat" "(Ljava/lang/String;)Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method. "containsKey" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8910" (Type/getType "Z") 153)
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "localType"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "newLocal" "(Lorg/objectweb/asm/Type;)I"))
   (lang.Code/StoreLocal "local2" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/LoadLocal "local2")
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "localType"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "storeLocal" "(ILorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/InvokeStatic
    (Type/getType "Llang/Code;")
    (Method. "Nil" "()Llang/Code;"))
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/generateCode;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/commons/GeneratorAdapter;Llang/Code;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Arg 2)
   (lang.Code/String "local-")
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "name"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "concat" "(Ljava/lang/String;)Ljava/lang/String;"))
   (lang.Code/LoadLocal "local2")
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "localType"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeStatic
    (Type/getType "Llang/Code;")
    (Method. "LocalMeta" "(ILorg/objectweb/asm/Type;)Llang/Code;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method.
     "put"
     "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/Jump "exit_label_8911")
   (lang.Code/Label "true_label_8910")
   (lang.Code/Arg 2)
   (lang.Code/String "local-")
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "name"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "concat" "(Ljava/lang/String;)Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method. "get" "(Ljava/lang/Object;)Ljava/lang/Object;"))
   (lang.Code/Cast (Type/getType "Llang/Code;"))
   (lang.Code/StoreLocal "local" (Type/getType "Llang/Code;"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/LoadLocal "local")
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "localObj"
    (Type/getType "I"))
   (lang.Code/LoadLocal "local")
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "localType"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "storeLocal" "(ILorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/InvokeStatic
    (Type/getType "Llang/Code;")
    (Method. "Nil" "()Llang/Code;"))
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/generateCode;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/commons/GeneratorAdapter;Llang/Code;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8911")
   (lang.Code/Label "exit_label_8905")
   (lang.Code/Jump "exit_label_8903")
   (lang.Code/Label "true_label_8902")
   (lang.Code/Bool true)
   (lang.Code/Arg 2)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "stringValue"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method. "containsKey" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8912" (Type/getType "Z") 153)
   (lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "newLabel" "()Lorg/objectweb/asm/Label;"))
   (lang.Code/StoreLocal
    "label"
    (Type/getType "Lorg/objectweb/asm/Label;"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/LoadLocal "label")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "goTo" "(Lorg/objectweb/asm/Label;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Arg 2)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "stringValue"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/LoadLocal "label")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method.
     "put"
     "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/Jump "exit_label_8913")
   (lang.Code/Label "true_label_8912")
   (lang.Code/Arg 2)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "stringValue"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method. "get" "(Ljava/lang/Object;)Ljava/lang/Object;"))
   (lang.Code/Cast (Type/getType "Lorg/objectweb/asm/Label;"))
   (lang.Code/StoreLocal
    "label"
    (Type/getType "Lorg/objectweb/asm/Label;"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/LoadLocal "label")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "goTo" "(Lorg/objectweb/asm/Label;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8913")
   (lang.Code/Label "exit_label_8903")
   (lang.Code/Jump "exit_label_8901")
   (lang.Code/Label "true_label_8900")
   (lang.Code/Bool true)
   (lang.Code/Arg 2)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "stringValue"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method. "containsKey" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8914" (Type/getType "Z") 153)
   (lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "newLabel" "()Lorg/objectweb/asm/Label;"))
   (lang.Code/StoreLocal
    "label"
    (Type/getType "Lorg/objectweb/asm/Label;"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "compareType"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "compareOp"
    (Type/getType "I"))
   (lang.Code/LoadLocal "label")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "ifCmp"
     "(Lorg/objectweb/asm/Type;ILorg/objectweb/asm/Label;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Arg 2)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "stringValue"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/LoadLocal "label")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method.
     "put"
     "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/Jump "exit_label_8915")
   (lang.Code/Label "true_label_8914")
   (lang.Code/Arg 2)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "stringValue"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method. "get" "(Ljava/lang/Object;)Ljava/lang/Object;"))
   (lang.Code/Cast (Type/getType "Lorg/objectweb/asm/Label;"))
   (lang.Code/StoreLocal
    "label"
    (Type/getType "Lorg/objectweb/asm/Label;"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "compareType"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "compareOp"
    (Type/getType "I"))
   (lang.Code/LoadLocal "label")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "ifCmp"
     "(Lorg/objectweb/asm/Type;ILorg/objectweb/asm/Label;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8915")
   (lang.Code/Label "exit_label_8901")
   (lang.Code/Jump "exit_label_8899")
   (lang.Code/Label "true_label_8898")
   (lang.Code/Bool true)
   (lang.Code/Arg 2)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "stringValue"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method. "containsKey" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8916" (Type/getType "Z") 153)
   (lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "newLabel" "()Lorg/objectweb/asm/Label;"))
   (lang.Code/StoreLocal
    "label"
    (Type/getType "Lorg/objectweb/asm/Label;"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "compareType"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    "EQ"
    (Type/getType "I"))
   (lang.Code/LoadLocal "label")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "ifCmp"
     "(Lorg/objectweb/asm/Type;ILorg/objectweb/asm/Label;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Arg 2)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "stringValue"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/LoadLocal "label")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method.
     "put"
     "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/Jump "exit_label_8917")
   (lang.Code/Label "true_label_8916")
   (lang.Code/Arg 2)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "stringValue"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method. "get" "(Ljava/lang/Object;)Ljava/lang/Object;"))
   (lang.Code/Cast (Type/getType "Lorg/objectweb/asm/Label;"))
   (lang.Code/StoreLocal
    "label"
    (Type/getType "Lorg/objectweb/asm/Label;"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "compareType"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    "EQ"
    (Type/getType "I"))
   (lang.Code/LoadLocal "label")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "ifCmp"
     "(Lorg/objectweb/asm/Type;ILorg/objectweb/asm/Label;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8917")
   (lang.Code/Label "exit_label_8899")
   (lang.Code/Jump "exit_label_8897")
   (lang.Code/Label "true_label_8896")
   (lang.Code/Bool true)
   (lang.Code/Arg 2)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "stringValue"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method. "containsKey" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8918" (Type/getType "Z") 153)
   (lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "newLabel" "()Lorg/objectweb/asm/Label;"))
   (lang.Code/StoreLocal
    "label"
    (Type/getType "Lorg/objectweb/asm/Label;"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "compareType"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    "NE"
    (Type/getType "I"))
   (lang.Code/LoadLocal "label")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "ifCmp"
     "(Lorg/objectweb/asm/Type;ILorg/objectweb/asm/Label;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Arg 2)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "stringValue"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/LoadLocal "label")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method.
     "put"
     "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/Jump "exit_label_8919")
   (lang.Code/Label "true_label_8918")
   (lang.Code/Arg 2)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "stringValue"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method. "get" "(Ljava/lang/Object;)Ljava/lang/Object;"))
   (lang.Code/Cast (Type/getType "Lorg/objectweb/asm/Label;"))
   (lang.Code/StoreLocal
    "label"
    (Type/getType "Lorg/objectweb/asm/Label;"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "compareType"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    "NE"
    (Type/getType "I"))
   (lang.Code/LoadLocal "label")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "ifCmp"
     "(Lorg/objectweb/asm/Type;ILorg/objectweb/asm/Label;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8919")
   (lang.Code/Label "exit_label_8897")
   (lang.Code/Jump "exit_label_8895")
   (lang.Code/Label "true_label_8894")
   (lang.Code/Bool true)
   (lang.Code/Arg 2)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "stringValue"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method. "containsKey" "(Ljava/lang/Object;)Z"))
   (lang.Code/JumpCmp "true_label_8920" (Type/getType "Z") 153)
   (lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "newLabel" "()Lorg/objectweb/asm/Label;"))
   (lang.Code/StoreLocal
    "label"
    (Type/getType "Lorg/objectweb/asm/Label;"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/LoadLocal "label")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "mark" "(Lorg/objectweb/asm/Label;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Arg 2)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "stringValue"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/LoadLocal "label")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method.
     "put"
     "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/Jump "exit_label_8921")
   (lang.Code/Label "true_label_8920")
   (lang.Code/Arg 2)
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Code;")
    "stringValue"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/util/HashMap;")
    (Method. "get" "(Ljava/lang/Object;)Ljava/lang/Object;"))
   (lang.Code/Cast (Type/getType "Lorg/objectweb/asm/Label;"))
   (lang.Code/StoreLocal
    "label"
    (Type/getType "Lorg/objectweb/asm/Label;"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/LoadLocal "label")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "mark" "(Lorg/objectweb/asm/Label;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_8921")
   (lang.Code/Label "exit_label_8895")
   (lang.Code/Pop)
   (lang.Code/Arg 2)
   (lang.Code/Return)])
 (Type/getType "Ljava/util/HashMap;")
 (into-array
  Type
  [(Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
   (Type/getType "Llang/Code;")
   (Type/getType "Ljava/util/HashMap;")]))


(lang.makeFn/invoke
 "lang2/generateDefaultConstructor"
 (into-array
  lang.Code
  [(lang.Code/Nil)
   (lang.Code/StoreLocal
    "signature"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/StoreLocal
    "exceptions"
    (Type/getType "[Lorg/objectweb/asm/Type;"))
   (lang.Code/Pop)
   (lang.Code/New
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/Opcodes;")
    "ACC_PUBLIC"
    (Type/getType "I"))
   (lang.Code/String "void <init>()")
   (lang.Code/InvokeStatic
    (Type/getType "Lorg/objectweb/asm/commons/Method;")
    (Method.
     "getMethod"
     "(Ljava/lang/String;)Lorg/objectweb/asm/commons/Method;"))
   (lang.Code/LoadLocal "signature")
   (lang.Code/LoadLocal "exceptions")
   (lang.Code/Arg 0)
   (lang.Code/InvokeConstructor
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "<init>"
     "(ILorg/objectweb/asm/commons/Method;Ljava/lang/String;[Lorg/objectweb/asm/Type;Lorg/objectweb/asm/ClassVisitor;)V"))
   (lang.Code/StoreLocal
    "gen"
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "visitCode" "()V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "loadThis" "()V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/String "java.lang.Object")
   (lang.Code/InvokeStatic
    (Type/getType "Ljava/lang/Class;")
    (Method. "forName" "(Ljava/lang/String;)Ljava/lang/Class;"))
   (lang.Code/InvokeStatic
    (Type/getType "Lorg/objectweb/asm/Type;")
    (Method. "getType" "(Ljava/lang/Class;)Lorg/objectweb/asm/Type;"))
   (lang.Code/String "void <init>()")
   (lang.Code/InvokeStatic
    (Type/getType "Lorg/objectweb/asm/commons/Method;")
    (Method.
     "getMethod"
     "(Ljava/lang/String;)Lorg/objectweb/asm/commons/Method;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "invokeConstructor"
     "(Lorg/objectweb/asm/Type;Lorg/objectweb/asm/commons/Method;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "returnValue" "()V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "endMethod" "()V"))
   (lang.Code/Nil)
   (lang.Code/Return)])
 (Type/getType "V")
 (into-array Type [(Type/getType "Lorg/objectweb/asm/ClassWriter;")]))


(lang.makeFn/invoke
 "lang2/makeEnumFactory"
 (into-array
  lang.Code
  [(lang.Code/String "void <init>()")
   (lang.Code/InvokeStatic
    (Type/getType "Lorg/objectweb/asm/commons/Method;")
    (Method.
     "getMethod"
     "(Ljava/lang/String;)Lorg/objectweb/asm/commons/Method;"))
   (lang.Code/StoreLocal
    "INIT"
    (Type/getType "Lorg/objectweb/asm/commons/Method;"))
   (lang.Code/Pop)
   (lang.Code/String "L")
   (lang.Code/Arg 1)
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "concat" "(Ljava/lang/String;)Ljava/lang/String;"))
   (lang.Code/String ";")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method. "concat" "(Ljava/lang/String;)Ljava/lang/String;"))
   (lang.Code/InvokeStatic
    (Type/getType "Lorg/objectweb/asm/Type;")
    (Method. "getType" "(Ljava/lang/String;)Lorg/objectweb/asm/Type;"))
   (lang.Code/StoreLocal
    "this-type"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/Pop)
   (lang.Code/New (Type/getType "Lorg/objectweb/asm/commons/Method;"))
   (lang.Code/Arg 2)
   (lang.Code/LoadLocal "this-type")
   (lang.Code/Arg 3)
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/fieldsToTypes;")
    (Method. "invoke" "([Llang/Field;)[Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeConstructor
    (Type/getType "Lorg/objectweb/asm/commons/Method;")
    (Method.
     "<init>"
     "(Ljava/lang/String;Lorg/objectweb/asm/Type;[Lorg/objectweb/asm/Type;)V"))
   (lang.Code/StoreLocal
    "method"
    (Type/getType "Lorg/objectweb/asm/commons/Method;"))
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/StoreLocal
    "signature"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/StoreLocal
    "exceptions"
    (Type/getType "Ljava/lang/Object;"))
   (lang.Code/Pop)
   (lang.Code/New
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/Opcodes;")
    "ACC_PUBLIC"
    (Type/getType "I"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/Opcodes;")
    "ACC_STATIC"
    (Type/getType "I"))
   (lang.Code/PlusInt)
   (lang.Code/LoadLocal "method")
   (lang.Code/LoadLocal "signature")
   (lang.Code/LoadLocal "exceptions")
   (lang.Code/Arg 0)
   (lang.Code/InvokeConstructor
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "<init>"
     "(ILorg/objectweb/asm/commons/Method;Ljava/lang/String;[Lorg/objectweb/asm/Type;Lorg/objectweb/asm/ClassVisitor;)V"))
   (lang.Code/StoreLocal
    "gen"
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "visitCode" "()V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/LoadLocal "this-type")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "newInstance" "(Lorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "dup" "()V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/LoadLocal "this-type")
   (lang.Code/LoadLocal "INIT")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "invokeConstructor"
     "(Lorg/objectweb/asm/Type;Lorg/objectweb/asm/commons/Method;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "dup" "()V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/Arg 2)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "push" "(Ljava/lang/String;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/LoadLocal "this-type")
   (lang.Code/String "tagName")
   (lang.Code/String "java.lang.String")
   (lang.Code/InvokeStatic
    (Type/getType "Ljava/lang/Class;")
    (Method. "forName" "(Ljava/lang/String;)Ljava/lang/Class;"))
   (lang.Code/InvokeStatic
    (Type/getType "Lorg/objectweb/asm/Type;")
    (Method. "getType" "(Ljava/lang/Class;)Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "putField"
     "(Lorg/objectweb/asm/Type;Ljava/lang/String;Lorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Int 0)
   (lang.Code/StoreLocal "i" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Int 0)
   (lang.Code/StoreLocal "i8938" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Arg 3)
   (lang.Code/StoreLocal "arr8939" (Type/getType "[Llang/Field;"))
   (lang.Code/Pop)
   (lang.Code/Label "while-loop_8940")
   (lang.Code/LoadLocal "i8938")
   (lang.Code/LoadLocal "arr8939")
   (lang.Code/ArrayLength)
   (lang.Code/JumpCmp "while-exit_8941" (Type/getType "I") 156)
   (lang.Code/LoadLocal "arr8939")
   (lang.Code/LoadLocal "i8938")
   (lang.Code/ArrayLoad (Type/getType "Llang/Field;"))
   (lang.Code/StoreLocal "f" (Type/getType "Llang/Field;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/LoadLocal "this-type")
   (lang.Code/LoadLocal "i")
   (lang.Code/LoadLocal "f")
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/makeFieldAssignmentOnStack;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/commons/GeneratorAdapter;Lorg/objectweb/asm/Type;ILlang/Field;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "i")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "i8938")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i8938" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Jump "while-loop_8940")
   (lang.Code/Label "while-exit_8941")
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "returnValue" "()V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "endMethod" "()V"))
   (lang.Code/Nil)
   (lang.Code/Return)])
 (Type/getType "V")
 (into-array
  Type
  [(Type/getType "Lorg/objectweb/asm/ClassWriter;")
   (Type/getType "Ljava/lang/String;")
   (Type/getType "Ljava/lang/String;")
   (Type/getType "[Llang/Field;")]))


(lang.makeFn/invoke
 "lang2/defineClass"
 (into-array
  lang.Code
  [(lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/ClassWriter;")
    (Method. "toByteArray" "()[B"))
   (lang.Code/StoreLocal "byteArray" (Type/getType "[B"))
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/StoreLocal "aThing" (Type/getType "Ljava/lang/Object;"))
   (lang.Code/Pop)
   (lang.Code/New (Type/getType "Lclojure/lang/DynamicClassLoader;"))
   (lang.Code/InvokeConstructor
    (Type/getType "Lclojure/lang/DynamicClassLoader;")
    (Method. "<init>" "()V"))
   (lang.Code/Arg 1)
   (lang.Code/String "/")
   (lang.Code/String ".")
   (lang.Code/InvokeVirtual
    (Type/getType "Ljava/lang/String;")
    (Method.
     "replace"
     "(Ljava/lang/CharSequence;Ljava/lang/CharSequence;)Ljava/lang/String;"))
   (lang.Code/LoadLocal "byteArray")
   (lang.Code/LoadLocal "aThing")
   (lang.Code/InvokeVirtual
    (Type/getType "Lclojure/lang/DynamicClassLoader;")
    (Method.
     "defineClass"
     "(Ljava/lang/String;[BLjava/lang/Object;)Ljava/lang/Class;"))
   (lang.Code/Return)])
 (Type/getType "Ljava/lang/Class;")
 (into-array
  Type
  [(Type/getType "Lorg/objectweb/asm/ClassWriter;")
   (Type/getType "Ljava/lang/String;")]))


(lang.makeFn/invoke
 "lang2/makeField"
 (into-array
  lang.Code
  [(lang.Code/Nil)
   (lang.Code/StoreLocal
    "signature"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/StoreLocal "value" (Type/getType "Ljava/lang/Object;"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/Opcodes;")
    "ACC_PUBLIC"
    (Type/getType "I"))
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Field;")
    "name"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/Arg 1)
   (lang.Code/GetField
    (Type/getType "Llang/Field;")
    "type"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/Type;")
    (Method. "getDescriptor" "()Ljava/lang/String;"))
   (lang.Code/LoadLocal "signature")
   (lang.Code/LoadLocal "value")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/ClassWriter;")
    (Method.
     "visitField"
     "(ILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/Object;)Lorg/objectweb/asm/FieldVisitor;"))
   (lang.Code/StoreLocal
    "fieldViz"
    (Type/getType "Lorg/objectweb/asm/FieldVisitor;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "fieldViz")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/FieldVisitor;")
    (Method. "visitEnd" "()V"))
   (lang.Code/Nil)
   (lang.Code/Return)])
 (Type/getType "V")
 (into-array
  Type
  [(Type/getType "Lorg/objectweb/asm/ClassWriter;")
   (Type/getType "Llang/Field;")]))


(lang.makeFn/invoke
 "lang2/makeFn"
 (into-array
  lang.Code
  [(lang.Code/New (Type/getType "Lorg/objectweb/asm/ClassWriter;"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/ClassWriter;")
    "COMPUTE_FRAMES"
    (Type/getType "I"))
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/ClassWriter;")
    "COMPUTE_MAXS"
    (Type/getType "I"))
   (lang.Code/PlusInt)
   (lang.Code/InvokeConstructor
    (Type/getType "Lorg/objectweb/asm/ClassWriter;")
    (Method. "<init>" "(I)V"))
   (lang.Code/StoreLocal
    "writer"
    (Type/getType "Lorg/objectweb/asm/ClassWriter;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/Arg 0)
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/initializeClass;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/ClassWriter;Ljava/lang/String;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/generateDefaultConstructor;")
    (Method. "invoke" "(Lorg/objectweb/asm/ClassWriter;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/Arg 1)
   (lang.Code/Arg 2)
   (lang.Code/Arg 3)
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/generateInvokeMethod;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/ClassWriter;[Llang/Code;Lorg/objectweb/asm/Type;[Lorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/ClassWriter;")
    (Method. "visitEnd" "()V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/Arg 0)
   (lang.Code/InvokeStatic
    (Type/getType "Llang2/defineClass;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/ClassWriter;Ljava/lang/String;)Ljava/lang/Class;"))
   (lang.Code/Return)])
 (Type/getType "Ljava/lang/Class;")
 (into-array
  Type
  [(Type/getType "Ljava/lang/String;")
   (Type/getType "[Llang/Code;")
   (Type/getType "Lorg/objectweb/asm/Type;")
   (Type/getType "[Lorg/objectweb/asm/Type;")]))


(lang.makeFn/invoke
 "lang2/fieldsToTypes"
 (into-array
  lang.Code
  [(lang.Code/Arg 0)
   (lang.Code/ArrayLength)
   (lang.Code/NewArray (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/StoreLocal
    "types"
    (Type/getType "[Lorg/objectweb/asm/Type;"))
   (lang.Code/Pop)
   (lang.Code/Int 0)
   (lang.Code/StoreLocal "i" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Int 0)
   (lang.Code/StoreLocal "i8926" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/StoreLocal "arr8927" (Type/getType "[Llang/Field;"))
   (lang.Code/Pop)
   (lang.Code/Label "while-loop_8928")
   (lang.Code/LoadLocal "i8926")
   (lang.Code/LoadLocal "arr8927")
   (lang.Code/ArrayLength)
   (lang.Code/JumpCmp "while-exit_8929" (Type/getType "I") 156)
   (lang.Code/LoadLocal "arr8927")
   (lang.Code/LoadLocal "i8926")
   (lang.Code/ArrayLoad (Type/getType "Llang/Field;"))
   (lang.Code/StoreLocal "field" (Type/getType "Llang/Field;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "types")
   (lang.Code/LoadLocal "i")
   (lang.Code/LoadLocal "field")
   (lang.Code/GetField
    (Type/getType "Llang/Field;")
    "type"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/ArrayStore (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "i")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "i8926")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i8926" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Jump "while-loop_8928")
   (lang.Code/Label "while-exit_8929")
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "types")
   (lang.Code/Return)])
 (Type/getType "[Lorg/objectweb/asm/Type;")
 (into-array Type [(Type/getType "[Llang/Field;")]))


(lang.makeFn/invoke
 "lang2/makeFieldAssignment"
 (into-array
  lang.Code
  [(lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "loadThis" "()V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/Arg 2)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "loadArg" "(I)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/Arg 3)
   (lang.Code/GetField
    (Type/getType "Llang/Field;")
    "name"
    (Type/getType "Ljava/lang/String;"))
   (lang.Code/Arg 3)
   (lang.Code/GetField
    (Type/getType "Llang/Field;")
    "type"
    (Type/getType "Lorg/objectweb/asm/Type;"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method.
     "putField"
     "(Lorg/objectweb/asm/Type;Ljava/lang/String;Lorg/objectweb/asm/Type;)V"))
   (lang.Code/Nil)
   (lang.Code/Return)])
 (Type/getType "V")
 (into-array
  Type
  [(Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
   (Type/getType "Lorg/objectweb/asm/Type;")
   (Type/getType "I")
   (Type/getType "Llang/Field;")]))
