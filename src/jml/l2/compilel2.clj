(ns jml.l2.compilel2
  (:import [org.objectweb.asm Opcodes Type ClassWriter]
           [org.objectweb.asm.commons Method GeneratorAdapter]))


;; CODE

(lang2.makeFn/invoke
 "lang3/generateCode"
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
   (lang.Code/JumpCmp "true_label_9374" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9376" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9378" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9380" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9382" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9384" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9386" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9388" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9390" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9392" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9394" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9396" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9398" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9400" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9402" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9404" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9406" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9408" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9410" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9412" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9414" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9416" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9418" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9420" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9422" (Type/getType "Z") 153)
   (lang.Code/Nil)
   (lang.Code/Jump "exit_label_9423")
   (lang.Code/Label "true_label_9422")
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
   (lang.Code/Label "exit_label_9423")
   (lang.Code/Jump "exit_label_9421")
   (lang.Code/Label "true_label_9420")
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
   (lang.Code/Label "exit_label_9421")
   (lang.Code/Jump "exit_label_9419")
   (lang.Code/Label "true_label_9418")
   (lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "arrayLength" "()V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_9419")
   (lang.Code/Jump "exit_label_9417")
   (lang.Code/Label "true_label_9416")
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
   (lang.Code/Label "exit_label_9417")
   (lang.Code/Jump "exit_label_9415")
   (lang.Code/Label "true_label_9414")
   (lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "returnValue" "()V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_9415")
   (lang.Code/Jump "exit_label_9413")
   (lang.Code/Label "true_label_9412")
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
   (lang.Code/Label "exit_label_9413")
   (lang.Code/Jump "exit_label_9411")
   (lang.Code/Label "true_label_9410")
   (lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "pop" "()V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_9411")
   (lang.Code/Jump "exit_label_9409")
   (lang.Code/Label "true_label_9408")
   (lang.Code/Arg 0)
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "dup" "()V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_9409")
   (lang.Code/Jump "exit_label_9407")
   (lang.Code/Label "true_label_9406")
   (lang.Code/Arg 0)
   (lang.Code/GetStatic
    (Type/getType "Lorg/objectweb/asm/Opcodes;")
    "ACONST_NULL"
    (Type/getType "I"))
   (lang.Code/InvokeVirtual
    (Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
    (Method. "visitInsn" "(I)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_9407")
   (lang.Code/Jump "exit_label_9405")
   (lang.Code/Label "true_label_9404")
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
   (lang.Code/Label "exit_label_9405")
   (lang.Code/Jump "exit_label_9403")
   (lang.Code/Label "true_label_9402")
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
   (lang.Code/Label "exit_label_9403")
   (lang.Code/Jump "exit_label_9401")
   (lang.Code/Label "true_label_9400")
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
   (lang.Code/Label "exit_label_9401")
   (lang.Code/Jump "exit_label_9399")
   (lang.Code/Label "true_label_9398")
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
   (lang.Code/Label "exit_label_9399")
   (lang.Code/Jump "exit_label_9397")
   (lang.Code/Label "true_label_9396")
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
   (lang.Code/Label "exit_label_9397")
   (lang.Code/Jump "exit_label_9395")
   (lang.Code/Label "true_label_9394")
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
   (lang.Code/Label "exit_label_9395")
   (lang.Code/Jump "exit_label_9393")
   (lang.Code/Label "true_label_9392")
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
   (lang.Code/Label "exit_label_9393")
   (lang.Code/Jump "exit_label_9391")
   (lang.Code/Label "true_label_9390")
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
   (lang.Code/Label "exit_label_9391")
   (lang.Code/Jump "exit_label_9389")
   (lang.Code/Label "true_label_9388")
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
   (lang.Code/Label "exit_label_9389")
   (lang.Code/Jump "exit_label_9387")
   (lang.Code/Label "true_label_9386")
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
   (lang.Code/Label "exit_label_9387")
   (lang.Code/Jump "exit_label_9385")
   (lang.Code/Label "true_label_9384")
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
   (lang.Code/Label "exit_label_9385")
   (lang.Code/Jump "exit_label_9383")
   (lang.Code/Label "true_label_9382")
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
   (lang.Code/Label "exit_label_9383")
   (lang.Code/Jump "exit_label_9381")
   (lang.Code/Label "true_label_9380")
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
   (lang.Code/Label "exit_label_9381")
   (lang.Code/Jump "exit_label_9379")
   (lang.Code/Label "true_label_9378")
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
    (Type/getType "Llang3/generateCode;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/commons/GeneratorAdapter;Llang/Code;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_9379")
   (lang.Code/Jump "exit_label_9377")
   (lang.Code/Label "true_label_9376")
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
    (Type/getType "Llang3/generateCode;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/commons/GeneratorAdapter;Llang/Code;)V"))
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_9377")
   (lang.Code/Jump "exit_label_9375")
   (lang.Code/Label "true_label_9374")
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
   (lang.Code/Label "exit_label_9375")
   (lang.Code/Return)])
 (Type/getType "V")
 (into-array
  Type
  [(Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
   (Type/getType "Llang/Code;")]))


(lang2.makeFn/invoke
 "lang3/generateAllTheCode"
 (into-array
  lang.Code
  [(lang.Code/New (Type/getType "Ljava/util/HashMap;"))
   (lang.Code/InvokeConstructor
    (Type/getType "Ljava/util/HashMap;")
    (Method. "<init>" "()V"))
   (lang.Code/StoreLocal "env" (Type/getType "LMap;"))
   (lang.Code/Pop)
   (lang.Code/Int 0)
   (lang.Code/StoreLocal "i9452" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Arg 1)
   (lang.Code/StoreLocal "arr9453" (Type/getType "[Llang/Code;"))
   (lang.Code/Pop)
   (lang.Code/Label "while-loop_9454")
   (lang.Code/LoadLocal "i9452")
   (lang.Code/LoadLocal "arr9453")
   (lang.Code/ArrayLength)
   (lang.Code/JumpCmp "while-exit_9455" (Type/getType "I") 156)
   (lang.Code/LoadLocal "arr9453")
   (lang.Code/LoadLocal "i9452")
   (lang.Code/ArrayLoad (Type/getType "Llang/Code;"))
   (lang.Code/StoreLocal "c" (Type/getType "Llang/Code;"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/LoadLocal "c")
   (lang.Code/LoadLocal "env")
   (lang.Code/InvokeStatic
    (Type/getType "Llang3/generateCodeWithEnv;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/commons/GeneratorAdapter;Llang/Code;Ljava/util/HashMap;)Ljava/util/HashMap;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "i9452")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i9452" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Jump "while-loop_9454")
   (lang.Code/Label "while-exit_9455")
   (lang.Code/Nil)
   (lang.Code/Return)])
 (Type/getType "V")
 (into-array
  Type
  [(Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
   (Type/getType "[Llang/Code;")]))


(lang2.makeFn/invoke
 "lang3/makeStructConstructor"
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
    (Type/getType "Llang3/fieldsToTypes;")
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
   (lang.Code/StoreLocal "i9460" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Arg 2)
   (lang.Code/StoreLocal "arr9461" (Type/getType "[Llang/Field;"))
   (lang.Code/Pop)
   (lang.Code/Label "while-loop_9462")
   (lang.Code/LoadLocal "i9460")
   (lang.Code/LoadLocal "arr9461")
   (lang.Code/ArrayLength)
   (lang.Code/JumpCmp "while-exit_9463" (Type/getType "I") 156)
   (lang.Code/LoadLocal "arr9461")
   (lang.Code/LoadLocal "i9460")
   (lang.Code/ArrayLoad (Type/getType "Llang/Field;"))
   (lang.Code/StoreLocal "f" (Type/getType "Llang/Field;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/LoadLocal "this-type")
   (lang.Code/LoadLocal "i")
   (lang.Code/LoadLocal "f")
   (lang.Code/InvokeStatic
    (Type/getType "Llang3/makeFieldAssignment;")
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
   (lang.Code/LoadLocal "i9460")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i9460" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Jump "while-loop_9462")
   (lang.Code/Label "while-exit_9463")
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


(lang2.makeFn/invoke
 "lang3/initializeClass"
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


(lang2.makeFn/invoke
 "lang3/makeFieldAssignmentOnStack"
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


(lang2.makeFn/invoke
 "lang3/makeEnum"
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
    (Type/getType "Llang3/initializeClass;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/ClassWriter;Ljava/lang/String;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/InvokeStatic
    (Type/getType "Llang3/generateDefaultConstructor;")
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
    (Type/getType "Llang3/makeField;")
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
   (lang.Code/StoreLocal "i9474" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Arg 1)
   (lang.Code/StoreLocal
    "arr9475"
    (Type/getType "[Llang/EnumVariant;"))
   (lang.Code/Pop)
   (lang.Code/Label "while-loop_9478")
   (lang.Code/LoadLocal "i9474")
   (lang.Code/LoadLocal "arr9475")
   (lang.Code/ArrayLength)
   (lang.Code/JumpCmp "while-exit_9479" (Type/getType "I") 156)
   (lang.Code/LoadLocal "arr9475")
   (lang.Code/LoadLocal "i9474")
   (lang.Code/ArrayLoad (Type/getType "Llang/EnumVariant;"))
   (lang.Code/StoreLocal "v" (Type/getType "Llang/EnumVariant;"))
   (lang.Code/Pop)
   (lang.Code/Int 0)
   (lang.Code/StoreLocal "i9472" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "v")
   (lang.Code/GetField
    (Type/getType "Llang/EnumVariant;")
    "fields"
    (Type/getType "[Llang/Field;"))
   (lang.Code/StoreLocal "arr9473" (Type/getType "[Llang/Field;"))
   (lang.Code/Pop)
   (lang.Code/Label "while-loop_9480")
   (lang.Code/LoadLocal "i9472")
   (lang.Code/LoadLocal "arr9473")
   (lang.Code/ArrayLength)
   (lang.Code/JumpCmp "while-exit_9481" (Type/getType "I") 156)
   (lang.Code/LoadLocal "arr9473")
   (lang.Code/LoadLocal "i9472")
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
   (lang.Code/JumpCmp "true_label_9482" (Type/getType "Z") 153)
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
    (Type/getType "Llang3/makeField;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/ClassWriter;Llang/Field;)V"))
   (lang.Code/Nil)
   (lang.Code/Jump "exit_label_9483")
   (lang.Code/Label "true_label_9482")
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_9483")
   (lang.Code/Pop)
   (lang.Code/LoadLocal "i9472")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i9472" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Jump "while-loop_9480")
   (lang.Code/Label "while-exit_9481")
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "i9474")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i9474" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Jump "while-loop_9478")
   (lang.Code/Label "while-exit_9479")
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Int 0)
   (lang.Code/StoreLocal "i9476" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Arg 1)
   (lang.Code/StoreLocal
    "arr9477"
    (Type/getType "[Llang/EnumVariant;"))
   (lang.Code/Pop)
   (lang.Code/Label "while-loop_9484")
   (lang.Code/LoadLocal "i9476")
   (lang.Code/LoadLocal "arr9477")
   (lang.Code/ArrayLength)
   (lang.Code/JumpCmp "while-exit_9485" (Type/getType "I") 156)
   (lang.Code/LoadLocal "arr9477")
   (lang.Code/LoadLocal "i9476")
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
    (Type/getType "Llang3/makeEnumFactory;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/ClassWriter;Ljava/lang/String;Ljava/lang/String;[Llang/Field;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "i9476")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i9476" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Jump "while-loop_9484")
   (lang.Code/Label "while-exit_9485")
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


(lang2.makeFn/invoke
 "lang3/generateInvokeMethod"
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
    (Type/getType "Llang3/generateAllTheCode;")
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


(lang2.makeFn/invoke
 "lang3/makeStruct"
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
    (Type/getType "Llang3/initializeClass;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/ClassWriter;Ljava/lang/String;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/InvokeStatic
    (Type/getType "Llang3/generateDefaultConstructor;")
    (Method. "invoke" "(Lorg/objectweb/asm/ClassWriter;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Int 0)
   (lang.Code/StoreLocal "i9464" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Arg 1)
   (lang.Code/StoreLocal "arr9465" (Type/getType "[Llang/Field;"))
   (lang.Code/Pop)
   (lang.Code/Label "while-loop_9466")
   (lang.Code/LoadLocal "i9464")
   (lang.Code/LoadLocal "arr9465")
   (lang.Code/ArrayLength)
   (lang.Code/JumpCmp "while-exit_9467" (Type/getType "I") 156)
   (lang.Code/LoadLocal "arr9465")
   (lang.Code/LoadLocal "i9464")
   (lang.Code/ArrayLoad (Type/getType "Llang/Field;"))
   (lang.Code/StoreLocal "f" (Type/getType "Llang/Field;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/LoadLocal "f")
   (lang.Code/InvokeStatic
    (Type/getType "Llang3/makeField;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/ClassWriter;Llang/Field;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "i9464")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i9464" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Jump "while-loop_9466")
   (lang.Code/Label "while-exit_9467")
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/InvokeStatic
    (Type/getType "Llang3/makeStructConstructor;")
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


(lang2.makeFn/invoke
 "lang3/generateCodeWithEnv"
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
   (lang.Code/JumpCmp "true_label_9424" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9426" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9428" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9430" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9432" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9434" (Type/getType "Z") 153)
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
   (lang.Code/JumpCmp "true_label_9436" (Type/getType "Z") 153)
   (lang.Code/Arg 0)
   (lang.Code/Arg 1)
   (lang.Code/InvokeStatic
    (Type/getType "Llang3/generateCode;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/commons/GeneratorAdapter;Llang/Code;)V"))
   (lang.Code/Nil)
   (lang.Code/Jump "exit_label_9437")
   (lang.Code/Label "true_label_9436")
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
   (lang.Code/JumpCmp "true_label_9438" (Type/getType "Z") 153)
   (lang.Code/Nil)
   (lang.Code/Jump "exit_label_9439")
   (lang.Code/Label "true_label_9438")
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
   (lang.Code/Label "exit_label_9439")
   (lang.Code/Label "exit_label_9437")
   (lang.Code/Jump "exit_label_9435")
   (lang.Code/Label "true_label_9434")
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
   (lang.Code/JumpCmp "true_label_9440" (Type/getType "Z") 153)
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
    (Type/getType "Llang3/generateCode;")
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
   (lang.Code/Jump "exit_label_9441")
   (lang.Code/Label "true_label_9440")
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
    (Type/getType "Llang3/generateCode;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/commons/GeneratorAdapter;Llang/Code;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/Nil)
   (lang.Code/Label "exit_label_9441")
   (lang.Code/Label "exit_label_9435")
   (lang.Code/Jump "exit_label_9433")
   (lang.Code/Label "true_label_9432")
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
   (lang.Code/JumpCmp "true_label_9442" (Type/getType "Z") 153)
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
   (lang.Code/Jump "exit_label_9443")
   (lang.Code/Label "true_label_9442")
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
   (lang.Code/Label "exit_label_9443")
   (lang.Code/Label "exit_label_9433")
   (lang.Code/Jump "exit_label_9431")
   (lang.Code/Label "true_label_9430")
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
   (lang.Code/JumpCmp "true_label_9444" (Type/getType "Z") 153)
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
   (lang.Code/Jump "exit_label_9445")
   (lang.Code/Label "true_label_9444")
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
   (lang.Code/Label "exit_label_9445")
   (lang.Code/Label "exit_label_9431")
   (lang.Code/Jump "exit_label_9429")
   (lang.Code/Label "true_label_9428")
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
   (lang.Code/JumpCmp "true_label_9446" (Type/getType "Z") 153)
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
   (lang.Code/Jump "exit_label_9447")
   (lang.Code/Label "true_label_9446")
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
   (lang.Code/Label "exit_label_9447")
   (lang.Code/Label "exit_label_9429")
   (lang.Code/Jump "exit_label_9427")
   (lang.Code/Label "true_label_9426")
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
   (lang.Code/JumpCmp "true_label_9448" (Type/getType "Z") 153)
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
   (lang.Code/Jump "exit_label_9449")
   (lang.Code/Label "true_label_9448")
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
   (lang.Code/Label "exit_label_9449")
   (lang.Code/Label "exit_label_9427")
   (lang.Code/Jump "exit_label_9425")
   (lang.Code/Label "true_label_9424")
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
   (lang.Code/JumpCmp "true_label_9450" (Type/getType "Z") 153)
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
   (lang.Code/Jump "exit_label_9451")
   (lang.Code/Label "true_label_9450")
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
   (lang.Code/Label "exit_label_9451")
   (lang.Code/Label "exit_label_9425")
   (lang.Code/Pop)
   (lang.Code/Arg 2)
   (lang.Code/Return)])
 (Type/getType "Ljava/util/HashMap;")
 (into-array
  Type
  [(Type/getType "Lorg/objectweb/asm/commons/GeneratorAdapter;")
   (Type/getType "Llang/Code;")
   (Type/getType "Ljava/util/HashMap;")]))


(lang2.makeFn/invoke
 "lang3/generateDefaultConstructor"
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


(lang2.makeFn/invoke
 "lang3/makeEnumFactory"
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
    (Type/getType "Llang3/fieldsToTypes;")
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
   (lang.Code/StoreLocal "i9468" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Arg 3)
   (lang.Code/StoreLocal "arr9469" (Type/getType "[Llang/Field;"))
   (lang.Code/Pop)
   (lang.Code/Label "while-loop_9470")
   (lang.Code/LoadLocal "i9468")
   (lang.Code/LoadLocal "arr9469")
   (lang.Code/ArrayLength)
   (lang.Code/JumpCmp "while-exit_9471" (Type/getType "I") 156)
   (lang.Code/LoadLocal "arr9469")
   (lang.Code/LoadLocal "i9468")
   (lang.Code/ArrayLoad (Type/getType "Llang/Field;"))
   (lang.Code/StoreLocal "f" (Type/getType "Llang/Field;"))
   (lang.Code/Pop)
   (lang.Code/LoadLocal "gen")
   (lang.Code/LoadLocal "this-type")
   (lang.Code/LoadLocal "i")
   (lang.Code/LoadLocal "f")
   (lang.Code/InvokeStatic
    (Type/getType "Llang3/makeFieldAssignmentOnStack;")
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
   (lang.Code/LoadLocal "i9468")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i9468" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Jump "while-loop_9470")
   (lang.Code/Label "while-exit_9471")
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


(lang2.makeFn/invoke
 "lang3/defineClass"
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


(lang2.makeFn/invoke
 "lang3/makeField"
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


(lang2.makeFn/invoke
 "lang3/makeFn"
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
    (Type/getType "Llang3/initializeClass;")
    (Method.
     "invoke"
     "(Lorg/objectweb/asm/ClassWriter;Ljava/lang/String;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/InvokeStatic
    (Type/getType "Llang3/generateDefaultConstructor;")
    (Method. "invoke" "(Lorg/objectweb/asm/ClassWriter;)V"))
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "writer")
   (lang.Code/Arg 1)
   (lang.Code/Arg 2)
   (lang.Code/Arg 3)
   (lang.Code/InvokeStatic
    (Type/getType "Llang3/generateInvokeMethod;")
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
    (Type/getType "Llang3/defineClass;")
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


(lang2.makeFn/invoke
 "lang3/fieldsToTypes"
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
   (lang.Code/StoreLocal "i9456" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Arg 0)
   (lang.Code/StoreLocal "arr9457" (Type/getType "[Llang/Field;"))
   (lang.Code/Pop)
   (lang.Code/Label "while-loop_9458")
   (lang.Code/LoadLocal "i9456")
   (lang.Code/LoadLocal "arr9457")
   (lang.Code/ArrayLength)
   (lang.Code/JumpCmp "while-exit_9459" (Type/getType "I") 156)
   (lang.Code/LoadLocal "arr9457")
   (lang.Code/LoadLocal "i9456")
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
   (lang.Code/LoadLocal "i9456")
   (lang.Code/Int 1)
   (lang.Code/PlusInt)
   (lang.Code/StoreLocal "i9456" (Type/getType "I"))
   (lang.Code/Pop)
   (lang.Code/Jump "while-loop_9458")
   (lang.Code/Label "while-exit_9459")
   (lang.Code/Nil)
   (lang.Code/Pop)
   (lang.Code/LoadLocal "types")
   (lang.Code/Return)])
 (Type/getType "[Lorg/objectweb/asm/Type;")
 (into-array Type [(Type/getType "[Llang/Field;")]))


(lang2.makeFn/invoke
 "lang3/makeFieldAssignment"
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
