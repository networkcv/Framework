[TOC]



# 1.类加载（Class Loading）

![](.\img\1577523754(1).jpg)

- 在Java代码中，**类的加载、连接和初始化过程都是在程序运行期间完成的**。（类从磁盘加载到内存中经历的三个阶段），造成运行虽然慢点，但是 **Java 里动态拓展的语言特性就是依赖运行期动态加载和动态连接的特性**；

  示例：可以编写一个面向接口的应用程序，然后等到运行的时候再加载其实际的实现类（就可以通过自定义类加载器，从网络和其他位置加载一个二进制流作为程序一部分）

  - 这里的**类就是指类本身**，而不是指类的对象；

  - **类的加载常见方式是**：将已经编译或者存在的字节码文件从磁盘加载到内存中；

  - **连接**：主要是将类与类之间的关系确定好，同时对字节码文件进行校验等处理；

  - Class 文件中描述的各种信息都需要加载到虚拟机之后才能运行和使用；

  - 这里的 Class 文件仅仅是**一串二进制的字节流**，不特质具体磁盘中的文件；

  - 虚拟机的类加载机制：虚拟机将描述类的数据从 Class 文件加载到内存，并对数据进行校验、转换解析和初始化，最终形成可以被虚拟机直接使用的 Java 类型。

  - 程序运行期间：程序运行期间完成典型例子就是动态代理，其实很多语言都是在编译期就完成了加载，也正因为这个特性给Java程序提供了更大的灵活性，增加了更多的可能性；

    

## （一）类加载器

- **每一个类都是由类加载器加载进入内存中**。

- Java虚拟机与程序的生命周期（JVM 本身就是一个进程）

  在如下几种情况下，Java 虚拟机将结束生命周期

  - 执行了 `System.exit()` 方法
  - 程序正常执行结束
  - 程序在执行过程中遇到了异常或错误而异常终止
  - 由于操作系统出现错误而导致虚拟机进程终止

## （二）类加载的过程

- 加载：查找并加载类的二进制数据到 Java 虚拟机中；

- 连接：

  - 验证：确保被加载的类的正确性；就是确保 Class 文件的字节流包含的信息符合当前虚拟机的要求，因为从上面可知，Class 获取方式很多，安全性未知；

  - 准备：为类的**静态变量**分配内存，并将其初始化为**默认值**，但是到达初始化之前该变量都没有初始化为真正的初始值，同时这时候还没有创建对象，静态变量或者静态代码块会顺序执行示例见下：

    ```java
    // 该语句在准备阶段对变量进行初始赋值，因为是整型所以初始赋值为 0，然后在初始化阶段重新赋值为 1；
    static int a = 1;
    ```

  - 解析：**把类中的符号引用转换为直接引用**，就是在类型的常量池中寻找类、接口、字段和方法的符号引用，把这些符号引用替换成直接引用的过程；

    **解析阶段在某些情况下可以在初始化阶段之后再开始**，这是为了支持Java语言的运行时绑定（也称动态绑定）。

- 初始化：为类的静态变量赋予正确的初始值，静态代码块也是在这一环节执行的。

​		这些阶段并不是一个执行完再开始下一阶段，而是混合交叉执行的，比如，加载阶段加载了字节码中的魔数，验证阶段就会去验证魔数的正确性。但是他们开始的时间是保持着固定的先后顺序（解析和初始化这两个阶段除外）。

![类的加载连接以及初始化](img/JVM：3-类加载器深入解析与阶段分解/类的加载连接以及初始化.png)

## （三）类的使用和卸载

包括类的使用和卸载，其中卸载一般不使用；

# 二、类的加载、连接与初始化详解

- Java 程序对类的使用方式可分为两种
  - 主动使用
  - 被动使用
  
- 所有的 Java 虚拟机都要实现  **每个类或接口被 Java 程序首次主动使用时才会初始化他们**

- **主动使用**（七种）
  
  - 使用 new 关键字创建类的实例，这里也间接证明构造器也是类的静态方法。
  - 访问某个类或接口的静态变量 getstatic（助记符），或者对该静态变量赋值 putstatic（被 final 修饰，已在编译期将结果放入常量池的静态字段除外）**只有直接定义静态字段的类才会被初始化**；
  - 调用一个类的静态方法 invoke static；
  - 使用 `java.lang.reflect` 包对类进行反射（例如使用`Class.forName(“com.test.Test”)`），(ClassType.class 不算主动使用)，会返回一个Class对象的引用，而一个类被加载的产物就是方法区中的Class对象；
  - 初始化一个类的子类，就会触发父类的初始化；
  - Java 虚拟机启动时被标明启动类的类 ：即包含 main 方法的类；
  - JDK1.7 开始提供的动态语言支持（了解）
  
  大概分四类：
  
  - 创建该类或其子类实例时
  - 访问该类静态部分（静态方法和静态成员变量），如果是通过子类间接访问则只初始化父类，不初始化子类
  - main方法所在的类
  - 对该类使用反射时
  
- **被动使用** 除了上面七种情况外，其他使用Java类的方式都被看做是对类的被动使用，都不会导致类的初始化（但是连接和加载的操作可能发生）。

## （一）类的加载详解

loading：加载，使用ClassLoader将class字节码文件加载到内存。

类的加载指的是将类的 `.class` 文件中的二进制数据读入到内存中，将其放在运行时数据区的方法区内，然后在内存中创建一个 `java.lang.Class` 对象（ JVM 规范并未说明 Class 对象必须放在哪里，HotSpot 虚拟机将其放在**方法区**中）用来封装该类在方法区内的数据结构。这个Class对象描述了这个类的具体内容，反射就是基于该对象进行的操作，**一个类不管生成多少实例，所有实例对应的 Class 对象只有一份。**

简化为：**.class文件（二进制数据）——>读取到内存——>数据放进方法区——>堆中创建对应Class对象——>并提供访问方法区的接口**



**加载 .Class 文件的方式：**

- 从本地系统中直接加载
- 通过网络下载 .class 文件
- 从 zip，jar 等归档文件中加载 .class 文件
- 从专用数据库中提取 .class 文件（很少使用）
- 将 Java 源文件动态编译为 .class 文件（一般存在于动态代理，因为动态代理的类是运行时候才产生，编译的时候是没有的，在web开发中，Jsp最后转化为Servlet，就用到了动态编译）



加载过程完成之后，虚拟机外部的二进制字节流就会按照虚拟机所需要的格式存储在方法区之中，方法区中的数据存储格式是由虚拟机实现自行定义；

==**加载阶段和连接阶段的部分内容（如一部分字节码文件格式验证动作）是交叉进行的**==

**类加载阶段，虚拟机需要完成的事情**

- 通过一个类的全限定名来获取定义此类的二进制字节流；（但是没指定从哪里获取以及怎么获取，所以较为灵活）
- 将这个字节流所代表的静态存储结构转换为方法区的运行时的数据结构；
- 在内存中生成一个代表这个类的 `java.lang.Class`对象，作为方法区这个类的各种数据的访问入口；



**测试1：分析主动使用和被动使用的区别**

```java
/**
   对于静态字段来说，只有直接定义了该字段的类才会被初始化，比如子类调用父类的静态变量，父类是直接定义该字段的类会被初始化，而子类不会被初始化。
   当一个类在初始化时，要求其父类全部都已经初始化完毕。
    
   -XX:+TraceClassLoading，用于追踪运行类的加载信息并打印出来（在类的VM option 中进行配置）

   JVM参数的格式：
      -XX:+<option>，表示开启 option 选项
      -XX:-<option>，表示关闭 option 选项
      -XX:<option>=value，表示将 option 的值设置为 value
*/
public class MyTest{
    public static void main(String[] args){
        // 下面语句输出：MyParent static block、hello world   
        // 这里的 str 是父类进行定义的，这里主动使用了父类，但是没有主动使用子类，因此子类没有被初始化，最终不会执行子类中的静态代码块；
        System.out.println(MyChild.str);  

        // 输出：MyParent static block、MyChild static block、welcome  
        // 因为 str2 是子类定义的，这里调用这句话就是对子类的主动调用，所以子类的静态代码块一定会执行，同时主动使用的时候，初始化子类的同时也会初始化父类；（初始化父类的子类，本质上对父类也是主动调用，而子类调用子类的静态变量，也是主动使用。）
        System.out.println(MyChild.str2);  
    }
}
class MyParent{
    public static String str="hello world";
    static {
        System.out.println("MyParent static block");
    }
}
class MyChild extends MyParent{
    public static String str2="welcome";
    static {
        System.out.println("MyChild static block");
    }
}
```



**测试2：分析对常量的使用和初始化**

```java
/**
    常量在编译阶段会存入到调用这个常量的方法所在的类的常量池中，以后该类对该常量的使用都会转换为对自身常量池的引用；
    本质上，调用类并没有直接引用到定义常量的类，因此并不会触发定义常量的类的初始化（所以不会执行静态代码块）
    注意：这里指的是将常量存到MyTest2的常量池中，之后MyTest2和MyParent2 就没有任何关系了。
    甚至我们可以将MyParent2的class文件删除（编译完之后），程序还可以执行。
    助记符：反编译之后可以看到
    助记符 ldc：表示将int、float或者String类型的常量值从常量池中推送至栈顶
    助记符 bipush：表示将单字节（-128-127）的常量值推送到栈顶
    助记符 sipush：表示将一个短整型值（-32768-32369）推送至栈顶
    助记符 iconst_1：表示将int型的1推送至栈顶（iconst_m1到iconst_5，(-1到5)值，6之后使用 bipush）
    助记符是由JDK内部的类实现的
*/
public class MyTest2{
    public static void main(String[] args){
        // 输出：MyParent static block、 hello world
        System.out.println(MyParent2.str1);   
        // 输出：hello world
        System.out.println(MyParent2.str);  
        System.out.println(MyParent2.s);  
        System.out.println(MyParent2.i);  
        System.out.println(MyParent2.j);  
    }
}
// 因为先编译后加载，所有该类并没有被加载
class MyParent2{
    public static String str1="hello world";
    public static final String str="hello world";
    public static final short s=7;
    public static final int i=129;
    public static final int j=1;
    static {
        System.out.println("MyParent static block");
    }
}
```

- 测试 3：常量的值不确定的时候

  ```java
  /**
          当一个常量的值在编译期间无法确定，那么其值就不会放到调用类的常量池中
          这时在程序运行时，会导致主动使用这个常量所在的类，显然会导致这个类被初始化
  */
  public class MyTest3{
      public static void main(String[] args){
      //输出MyParent static block、kjqhdun-baoje21w-jxqioj1-2jwejc9029
          System.out.println(MyParent3.str);  
      }
  }
  class MyParent3{
      public static final String str=UUID.randomUUID().toString();
      static {
          System.out.println("MyParent static block");
      }
  }
  ```

- 测试4：数组实例

  ```java
  /**
    对于数组实例来说，其类型是由JVM在运行期动态生成的，表示为 [L com.hisense.classloader.MyParent4 这种形式。动态生成的类型，其父类型就是Object
    对于数组来说，JavaDoc经构成数据的元素成为Component，实际上是将数组降低一个维度后的类型。
  
    助记符：anewarray：表示创建一个引用类型（如类、接口）的数组，并将其引用值压入栈顶
    助记符：newarray：表示创建一个指定原始类型（如int boolean float double char）的数组，并将其引用值压入栈顶
    
   * 之前我们知道数组是一个对象，而这个对象是由哪个类创建的？由数组存放类型的类创建的吗？
   * 对于数组实例来说，其类型是在JVM运行期间动态生成的，如果是引用类型的数组，那么会根据其
   * 存储类型动态生成对应的Class对象，该字节码对象的名称为 [L com.lwj.xxx
   * 如果是基础类型的数组，则对应数组类型的字节码对象名称为 int[]->[I  char[]->[C ...
   */
  public class MyTest4{
      public static void main(String[] args){
      //创建类的实例，属于主动使用，会导致类的初始化
          MyParent4 myParent4=new MyParent4();  
  /* 当创建数组类型的实例，并不表示对数组中的元素的主动使用，而仅仅表示创建了这个数组的实例而已，数组new 出来的实例类型有由JVM在运行期动态生成的。
  具体的类型以一维原始类型为例(一维是 [,二维是 [[)： int -> [I   char ->[C    boolean -> [Z    short -> [S    byte -> [B   引用类型数组 -> [L XXX
  */
  
  // 引用类型数组
          //不是主动使用
          MyParent4[] myParent4s=new MyParent4[1]; 
          //输出 [Lcom.hisense.classloader.MyParent4   
          System.out.println(myParent4s.getClass());      
          //输出 java.lang.Object    
          System.out.println(myParent4s.getClass().getSuperClass());    
  
  // 原生类型的数组
          int[] i=new int[1];
          System.out.println(i.getClass());          //输出 [ I
          System.out.println(i.getClass().getSuperClass());    //输出java.lang.Object
      }
  }
  class MyParent4{
      static {
          System.out.println("MyParent static block");
      }
  }
  ```

- 测试 5 ：关于接口初始化

  ```java
  /**
          当一个接口在初始化时，并不要求其父接口都完成了初始化
          只有在真正使用到父接口的时候（如引用接口中定义的常量），才会初始化
          
  */
  // 如果在一个接口中声明一个常量（b = 5）,而且该常量在编译期就能完全确定好具体的数值，那么就不会加载这个接口，而是直接把这个常量值直接纳入了 MyTest5 的常量池中
  // 验证，添加 VM options 之后，然后编译运行发现根本没有加载 MyParent5和 MyChild5，仅仅加载了 MyTest5，同时将两者的 class 文件删除之后仍然可以运行
  public class MyTest5{
  	public static void main(String[] args){
      	System.out.println(MyChild5.b);
      }
  }
  
  interface MyParent5{
  	public static final int a = 6;
  }
  interface MyChild5 extends MyParent5{
       //接口属性默认是 public static final
  	public static final int b = 5;
  }
  ```



![image-20191201165301445](img/JVM：3-类加载器深入解析与阶段分解/image-20191201165301445.png)

```java
public class MyTest5{
	public static void main(String[] args){
    	System.out.println(MyChild5.b);
    }
}

interface MyParent5{
	public static int a = 6;
}
// 改为class在之后，就不是 final 了，就不会纳入 MyTest5 的常量池中，所以程序运行期间肯定要加载 MyChild5同时还会加载MyParent5
class MyChild5 implements MyParent5{
	public static int b = 5;
}
```

[![image-20191201165610045](img/JVM：3-类加载器深入解析与阶段分解/image-20191201165610045.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/类加载器深入解析与阶段分解.resource/image-20191201165610045.png)

- 测试6

```java
/**
        准备阶段和初始化的顺序问题
*/
public class MyTest6{
    public static void main(String[] args){
         public static void main(String[] args){
         // 首先调用类 Singleton 的静态方法，表示对该类的主动使用
            Singleton Singleton=Singleton.getInstance();
            System.out.println(Singleton.counter1);     
            System.out.println(Singleton.counter2);
             //输出1，1
         }
    }
}
class Singleton{
    public static int counter1;
    public static int counter2=0;               
    private static Singleton singleton=new Singleton();

    private Singleton(){
        counter1++;
        counter2++;
    }

    // public static int counter2=0;       //   若改变此赋值语句的位置，输出  1，0
    public static Singleton getInstance(){
        return singleton;
    }
}
```

**类的加载的最终产品是位于内存中的 Class 对象；**

- Class 对象封装类在方法区内的数据结构，并且向 Java 程序员提供了访问方法区内的数据结构的接口；

- 这里要区分开加载和初始化，在类初始化之前就已经有对应的Class对象了，通过ClassType.class 获取Class对象的方式不算主动使用，因此也不会引起类的初始化。

  ```java
  public class Person {
      static{
          System.out.println("Person");
      }
  }
  public class Test1 {
      public static void main(String[] args) throws ClassNotFoundException {
          Class<Person> personClass = Person.class;
      }
  }
  ```

  ![image-20200320110831891](img/JVM：3-类加载器深入解析与阶段分解/image-20200320110831891.png)

上图可以看出加载了Person类，但没有进行初始化。

```java
    public static void main(String[] args) throws ClassNotFoundException {
        //Person person = new Person();
        Class.forName("_14_类型信息._02_反射.Person");
    }
```

![image-20200320111014140](img/JVM：3-类加载器深入解析与阶段分解/image-20200320111014140.png)

```java
    public static Class<?> forName(String className)
                throws ClassNotFoundException {
        Class<?> caller = Reflection.getCallerClass();
        return forName0(className, true, ClassLoader.getClassLoader(caller), caller);
    }
	private static native Class<?> forName0(String name, boolean initialize,
                                            ClassLoader loader,
                                            Class<?> caller)
    		    throws ClassNotFoundException;
```

通过设置initialize的boolean值来决定在加载的时候要不要初始化。

```java
    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader classLoader = Test1.class.getClassLoader();
        Class.forName("_14_类型信息._02_反射.Person",false,classLoader );
        //不会初始化Person
    }
```



类初始化：为新的对象分配内存，为实例变量赋默认值，为实例变量赋正确的初始值

Java编译器在它编译的每一个类都至少生成一个实例化的方法，在Java的class文件中，这个实例化方法被称为`<init>`。针对源代码中每一个类的构造方法，Java编译器都会产生一个`<init>`方法。

## （二）类的连接详解

linking：链接

- verification：校验
  - 文件格式的验证
    - 是否以0xCAFEBABE开头
    - 版本号是否合理
  - 元数据验证
    - 是否有父类
    - 是否继承final类
    - 非抽象类实现类所有的抽象方法
  - 字节码验证
    - 运行检查
    - 栈数据类型和操作码数据吻合
    - 跳转指令指定到合理的位置
  - 符号引用验证
    - 常量池中描述类是否存在
    - 访问的方法或字段是否存在足够权限
- preparation：准备，将静态变量赋值为默认值
  - 分配内存，并为类设置初始值（方法区中）
    - public static int v=1；
    - 在准备阶段中，v会被设置为0
    - 在初始化的 `<clinit>`中才会被设置为1
    - 对于static final 类型，在准备阶段就会被赋上正确的值
    - public static final int v=1；
- resolution：解析
  - 将符号引用替换为直接引用
  - 构造方法可以解析，private可以解析，但是多态不知道具体的调用者是谁，无法解析

类被加载后，就进入连接阶段。**连接阶段就是将已经读入到内存的类的二进制数据合并到虚拟机的运行时环境中去**。

### 1.类的连接-验证阶段

虽然在编译过程编译器会检查 Class 文件，但由于 Class 文件并不要求是由 Java 源代码编译得到，可以通过任何途径产生，甚至用十六进制编译器都可以产生Class文件，因此仍需要需要进行检查。相当于是虚拟机的自我保护机制，如果验证到输入的字节流不符合 Class 文件格式的约束，虚拟机抛出 `java.lang.VerifyError`或其子类异常；

- 步骤一：类文件的结构检查（文件格式验证）：保证输入的字节流能正确的解析并存储于方法区中，格式上符合描述一个Java 类型信息的要求，**只有经过该阶段的验证，字节流才会进入内存的方法区中进行存储**，所有后面的3个验证阶段是基于方法区的存储结果进行的，后面不再直接操作字节流。

  - 是否以魔数 0xCAFEBABE 开头；
  - 主、次版本号是否在当前虚拟机处理范围之内；
  - 常量池的常量中是否有不被支持的常量类型（检查常量 tag 标志）；
  - 指向常量的各种索引值中是否有指向不存在的常量或不符合类型的常量；

- 步骤二：语义检查（元数据验证）：保证描述的信息符合 Java 语言规范的要求，保证不存在不符合 Java 语言规范的元数据信息；

  - 验证该类是否有父类（除了 Object 之外要求所有都有父类）
  - 该类是否继承不被允许继承的类（被 final 修饰的类）
  - 类中字段、方法是否和父类矛盾等等；

- 步骤三：字节码验证：通过数据流和控制流分析，确定数据语义是合法的、符合逻辑的，同时对类的方法体进行校验分析，保证被校验类的方法在运行时候不会做出危害虚拟机的事情。

  因为该项检查的东西较多，因此在 jdk1.6 之后给方法体的 Code 属性的属性表中添加了一项 `StackMapTable`的属性，该属性描述了方法体中所有的基本块（按照控制流拆分的代码块）开始时本地变量表和操作栈应有的状态，因此校验时候只要看表即可，及使用类型检查不使用类型推导；

  - 保证任意时刻操作数栈的数据类型于指令代码序列都能配合工作（不能操作栈中放入 int类型数据，使用却按照 long类型来加载进入本地变量表中）
  - 保证跳转指令不会跳转到方法体之外的字节码指令上；
  - 保证方法体重的类型转换是有效的；

- 步骤四：二进制兼容性的验证（符号引用验证）：发生在虚拟机将符号引用转化为直接引用的时候，**该转换动作在连接的第三阶段- 解析阶段中发生**，是对类自身以外（常量池中的各种符号引用）的信息进行匹配性校验，一般校验以下：

  ==符号引用验证是确保解析的动作能正常执行==，如果通过不了，则报 `java.lang.IncompatibleClassChangeError`异常及其子类，包括：`java.lang.IllegalAccessError`、`java.lang.NoSuchFiledError`、`java.lang.NoSuchMethodError`等等

  - 符号引用中通过字符串描述的全限定名是否能找到对应的类；
  - 符号引用中的类、字段、方法的访问性（public、XXX）能否可以被当前类访问；

如果可以确保正确性，可以采用：`-Xverify:none`来关闭大多数的类验证措施，缩短虚拟机类加载的时间；

### 2.类的连接-准备阶段

==在准备阶段，Java虚拟机为**类的静态变量**分配内存，并设置**默认的初始值**==，这些变量使用的内存都将在方法区进行分配。这里进行内存分配的仅仅包括类变量（被 static 修饰的变量），但是不包括实例变量；**实例变量将在对象实例化时候随着对象一起分配在 Java 堆中**；

| 数值类型  | 零值     |
| --------- | -------- |
| int       | 0        |
| long      | 0L       |
| short     | (short)0 |
| char      | '\u0000' |
| byte      | (byte)0  |
| boolean   | false    |
| float     | 0.0f     |
| double    | 0.0d     |
| reference | null     |

例如对于以下Sample类，在准备阶段，将为int类型的静态变量a分配4个字节的内存空间，并且赋予默认值0，为long类型的静态变量b分配8个字节的内存空间，并且赋予默认值0L；

```java
    public class Sample{
        private static int a=1;
        public  static long b;
        public  static long c;
        static {
            b=2;
        }
    }
```

**特殊**：如果类字段的字段属性表中存在 ConstantValue 属性，则在准备阶段变量值就会被初始化为 ConstantValue 属性所指定的值，假设上面类变量 value 定义为：`public static final int value = 123;`因为有 ConstantValue **属性 final ，因此准备阶段虚拟机就会赋值**；

### 3.类的连接-解析阶段

解析阶段是虚拟机将常量池内的符号引用替换为直接引用的过程，符号引用在 Class 文件中以 `CONSTANT_Class_info`、`CONSTANT_Fieldref_info`、`CONSTANT_Methodref_info` 等类型的常量出现；

- 符号引用：符号引用以一组符号来描述所引用的目标，符号可以是任何形式的字面量，只要使用时候能够无歧义的定位到目标即可；**符号引用的目标不一定已经加载到内存中**（与虚拟机实现的内存布局有关，但是虚拟机能够接受的符号引用必须是一致的，因为符号引用的字面量形式明确定义在 Java 虚拟机规范的 Class 文件格式中）。
- 直接引用：直接引用可以是直接指向目标的指针、相对偏移量或者是一个能间接定位到目标的句柄。**直接引用的引用目标必须已经在内存中存在**（也与虚拟机实现的内存布局相关）

同时虚拟机并没有规范解析阶段发生的具体时间，只要在执行 `getstatic`、`instanceof`、`new` 、`Idc`、等十六个用于操作符号引用的字节码指令之前，先对它们所使用的的符号引用进行解析，所以**虚拟机可以决定在在类被加载器加载的时候就对常量池中的符号引用进行解析还是等到一个符号引用将要被使用前才去解析它**。

可以对同一个符号引用进行多次解析请求，虚拟机可以对第一次解析结果进行缓存（除了上面的 `invokedynamic`指令，因为该指令目的是用于动态语言支持，对应的引用称为 “动态调用点限定符”，动态表示必须等到程序实际执行到这条指令的时候解析动作才开始，其他的都是静态解析，加载结束之后就可以执行解析）（在运行时常量池中记录直接引用，并把常量标识为已解析状态，避免重复解析），同一个实体中，若某个符号引用已经成功解析，则后续引用解析请求应当一直成功，反之则后续请求都报异常。

具体的解析过程就比较繁琐了，每个直接引用都有自己的解析步骤，但都大同小异。

1. CONSTANT_Class_info 类型的符号引用的解析步骤：
   - 假设当前代码所在类为A，如果要把一个未解析过的符号引用 B 解析为一个类或接口C。
   - 先判断C不是数组类型，则将B的全限定名传递给A的类加载器先加载，然后验证，通过之后再判断它A是否对C有访问权限，如果没有则抛出 IllegalAccessError异常。
   - 如果C是数组类型，则B的描述符会是类似“[Ljava/lang/Integer”的形式，那么需要加载的元素类型就是Intger，接着由虚拟机生成一个代表此数组维度和元素类型的数组对象，后续同上。
2. CONSTANT_Fieldref_info 类型的符号引用解析步骤：
   - 首先会去解析字段所在的类或接口，成功解析后，假设该类或接口为C。
   - 从C开始判断是否包含字段名称和描述符相匹配的字段，有则返回这个字段的直接引用。
   - 否则，如果实现接口的话，会按接口实现关系，从下往上递归搜索是否包含相同字段名和描述符的字段。
   - 否则，如果不是Object的话，会按继承关系，向上查找。
   - 否则，查找失败，抛出 NoSuchFieldError 异常。
   - 如果查找过程成功，返回引用之前，同上会对字段进行权限验证，不具备则抛出 IllegalAccessError 异常。



## （三）类的初始化详解

initializing：初始化，指的是类的初始化，而不是对象初始化，将静态变量赋值为初始值

- 执行类构造器 `<clinit>`
  - static变量赋值语句
  - static{}语句
- 先调用父类的`<clinit>`，再调用子类`<clinit>`
- `<clinit>`是线程安全的

在初始化阶段，Java虚拟机执行类的初始化语句，为类的静态变量赋予初始值。在程序中，静态变量的初始化有两种途径：（1）在静态变量的声明处进行初始化；（2）在静态代码块中进行初始化。例如上面的静态变量 a,b 是被显示初始化的，静态变量 c 是没有被显式初始化的，将保持默认值 0；

==只有当程序访问的静态变量或者静态方法确实在当前类或者当前接口中定义时候，才可以认为是对类或者接口的主动使用==

==调用 ClassLoader 类的 loadClass 方法加载一个类，并不是对类的主动使用，不会导致类的初始化==

- 初始化阶段就是执行类构造器`()`方法的过程

  - **该方法由编译器自动收集类中的所有类变量的赋值动作和静态语句块（static{}块）中的语句合并产生的**，编译器收集的顺序是由语句在源文件中出现的顺序决定的，**静态语句块找那个只能访问到静态语句块之前的变量，定义在它之后的变量，在前面的静态语句块可以赋值但是不能访问**。

    ```java
    public class Test {
        static {
            i = 0;
            // 该句编译的时候就会提示“非法向前引用”
            System.out.println(i);
        }
        private static int i = 1;
    }
    ```

  - `()` 方法不需要像类的构造函数（实例构造器）`()` 方法那样需要显式的调用父类的构造器，虚拟机或保证子类该方法执行之前先执行父类该方法，所有最先执行该方法的类一定是`java.lang.Object`

  - 由上可得，父类`()` 方法先执行，因此父类中定义的静态语句块优先于子类的变量赋值操作；

    ```java
    package com.yacut;
    
    public class JVMTest {
        // 只有内部类可以声明为 static
        static class Parent{
            public static int A = 1;
            static{
                A = 2;
            }
        }
    
        static class Sub extends Parent{
            public static int B = A;
        }
    
        public static void main(String[] args) {
            System.out.println(Sub.B);
        }
    }
    
    /** output:
     *  2
     */
    ```

    - 当然 `()`方法对类和接口非必须，若类中没有静态语句块和对变量的赋值操作则编译器不会为该类生成该方法；

    - 接口：首先没有静态代码块，同时执行接口的该方法不需要先执行父接口的 `()`, **只有当父接口中定义的变量使用时候，父接口才会初始化**；同上接口的实现类初始化时候也不会执行接口的 `()` 方法；

    - JVM 保证一个类的 `()`方法在多线程环境中只有一个线程执行该类的这个方法，其他线程都会阻塞等待；**若执行该方法的线程退出该方法之后，其他线程被唤醒之后不会再次进入 `()`方法**，因为同一个类加载器下，一个类型只会初始化一次；

      ```java
      package com.yacut;
      
      public class JVMTest {
         static class DeadLoopClass{
             static {
                 // 如果不加 if，编译器会提示 "initializer does not complete normally" 并拒绝编译
                 if (true){
                     System.out.println(Thread.currentThread() + "init DeadLoopClass");
                     // 一条线程在死循环（模拟长时间操作）
                     while (true){
      
                     }
                 }
             }
         }
      
          public static void main(String[] args) {
              Runnable script = new Runnable() {
                  public void run() {
                      System.out.println(Thread.currentThread() + "start");
                      DeadLoopClass deadLoopClass = new DeadLoopClass();
                      System.out.println(Thread.currentThread() + "run over");
                  }
              };
      
              Thread thread1 = new Thread(script);
              Thread thread2 = new Thread(script);
              thread1.start();
              thread2.start();
          }
      }
      /** output:
       * Thread[Thread-1,5,main]start
       * Thread[Thread-0,5,main]start
       * Thread[Thread-1,5,main]init DeadLoopClass
       */
      ```

- 类的初始化步骤：

  - 假如这个类还没有被加载和连接，那就先进行加载和连接
  - 假如类存在直接父类，并且这个父类还没有被初始化，那就先初始化直接父类
  - 假如类中存在初始化语句，那就依次执行这些初始化语句

- 当Java虚拟机初始化一个类时，要求它的所有父类都已经被初始化，但是这条规则不适用于接口。

  - **在初始化一个类时候，并不会先初始化它所实现的接口；**

  - **在初始化一个接口的时候，并不会先初始化它的父接口；**

  - 因此，一个父接口并不会因为它的子接口或者实现类的初始化而初始化。只有当程序首次使用特定的接口的静态变量时，才会导致该接口的初始化。

  - 只有当程序访问的静态变量或静态方法确实在当前类或者当前接口中定义时候，才可以认为是对类或者接口的主动使用；

    测试代码：验证：在初始化一个类时候，并不会先初始化它所实现的接口；

  ```java
  public class MyTest5{
      public static void main(String[] args){
           public static void main(String[] args){
               // 这里主动使用了 MyChild5，因此其会被初始化，但是 MyParent5 中代码没有执行，因此没有初始化它的接口
              System.out.println(MyChild5.b)
           }
      }
  }
  interfacce MParent5{
      public static Thread thread=new thread(){
          {
          	System.out.println(" MParent5 invoke")
          }
      };
  }
  class MyChild5 implements MParent5{  
      public static int b=6;
  }
  ```

  [![image-20191201181444828](img/JVM：3-类加载器深入解析与阶段分解/image-20191201181444828.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/类加载器深入解析与阶段分解.resource/image-20191201181444828.png)

  测试示例二：验证在初始化一个接口的时候，并不会先初始化它的父接口；

  ```java
  package com.gjxaiou.class10;
  
  public class MyTest5 {
      public static void main(String[] args) {
              System.out.println(MyChild5.thread);
          }
  }
  
  interface MyParent5 {
      public static Thread thread = new Thread(){
          {
              System.out.println(" MParent5 invoke");
          }
      };
  }
  
  interface MyChild5 extends MyParent5 {
      public static Thread thread = new Thread(){
          {
              System.out.println(" MyChild5 invoke");
          }
      };
  }
  ```

  [![image-20191201182327034](img/JVM：3-类加载器深入解析与阶段分解/image-20191201182327034.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/类加载器深入解析与阶段分解.resource/image-20191201182327034.png)

- 调用ClassLoader类的loadClass方法加载一个类，并不是对类的主动使用，不会导致类的初始化。

# 三、类加载器详解

- **概况**：通过一个类的全限定名来获取描述此类的二进制字节流 --> 类加载器就是执行这个操作的；在**类层次划分、OSGi、热部署、代码加密**方面有用；
- 每一个类加载器都有一个独立的类名称空间，就是比较两个类是否相等，首先他们是由同一个类加载器加载的情况下才能比较；否则即使两个类来自同一个 Class 文件，由同一个虚拟机进行加载（但是使用的类加载器不同），这两个类肯定不相同；
- 类加载器用来把类加载到Java虚拟机中。从JDK1.2版本开始，类的加载过程采用父亲委托机制，这种机制能更好地保证Java平台的安全。在此委托机制中，除了Java虚拟机自带的根类加载器（没有父加载器）以外，其余的类加载器都有且只有一个父加载器。当Java程序请求加载器loader1加载Sample类时，loader1首先委托自己的父加载器去加载Sample类，若父加载器能加载，则有父加载器完成加载任务，否则才由加载器loader1本身加载Sample类。
- 有两种类型的类加载器（类一定是由类加载器进行加载）
  - Java虚拟机自带的加载器
    - **启动类加载器**（Bootstrap）：该加载器没有父加载器，它负责加载虚拟机中的核心类库。根类加载器加载/jre/lib/rt.jar。该类加载器的实现依赖于底层操作系统，属于虚拟机的实现的一部分，由c++实现，它并没有继承java.lang.ClassLoader类。
    - **扩展类加载器**（Extension）：它的父加载器为根类加载器。它从java.ext.dirs系统属性所指定的目录中加载类库，或者从JDK的安装目录的jre\lib\ext子目录（扩展目录）下加载类库，如果把用户创建的jar文件放在这个目录下，也会自动由扩展类加载器加载，扩展类加载器是纯Java类，是java.lang.ClassLoader的子类。
    - **系统/应用类加载器**（System/app）：也称为应用类加载器，它的父加载器为扩展类加载器，它从环境变量classpath或者系统属性java.class.path所指定的目录中加载类，他是用户自定义的类加载器的默认父加载器。系统类加载器时纯Java类，是java.lang.ClassLoader的子类。
  - 用户自定义的类加载器
    - 特点：一定是java.lang.ClassLoader的子类
    - 作用：用户可以定制类的加载方式

关系：根类加载器–>扩展类加载器–>系统应用类加载器–>自定义类加载器（后面的包含前面的）

- **类加载器并不需要等到某个类被“首次主动使用”时再加载它**

  佐证：MyTest1 中main 函数中调用的是 MyChild1.str，但是该 str 是定义在 MyParent1 中的，因为运行（在 VM options 中添加：`-XX:+TraceClassLoading`）结果显示 MyChild1 中的static 中代码没有执行，即里面的打印语句没有执行，所有 MyChild 并没有主动被主动使用；但是从输出的加载过程中可以看到 MyParent1 和 MyChild1 都进行了加载 ，说明并没有主动使用 MyChild类，但是已经被加载到 JVM 中了；

- JVM规范允许类加载器在预料某个类将要被使用时就预先加载它，如果在预先加载的过程中遇到了.class文件缺失或存在错误，类加载器必须在**程序首次主动使用**该类才报告错误（LinkageError错误），如果这个类一直没有被程序主动使用，那么类加载器就不会报告错误。

## （一）类加载器的父亲委托机制

==双亲委派模型并不是一个强制性的约束模型==，因此可以破坏

破坏场景一：双亲委派模型解决了各个类加载器的基础类统一问题（越基础的类由越上层的来加载器进行加载），虽然基础类总是被用户调用，但是**基础类可能调用用户的代码**，例如 JNDI 服务代码由启动类加载器进行加载，它是用于对资源进行集中管理和查找，因此需要调用独立厂商实现并且部署在应用程序的 ClassPath 下的 JNDI 接口提供者（SPI）的代码，但是启动类加载器不可能认识这些代码 -》使用**线程上下文类加载器（Thread Context ClassLoader）**，该类加载器通过 `java.lang.Thread` 类的 `setContextClassLoaser()` 方法设置，如果创建线程时候还未设置就从父线程中继承一个，若整个应用程序都没有设置，该类加载器默认就是应用程序类加载器，这样 JNDI 服务使用这个线程上下文类加载器区加载所需的（SPI）代码，相当于 **父类加载器请求子类加载器去完成类的加载（相当于打通了逆向层次结构），其他的包括加载`JDNI /JDBC/JCE/JAXB/JBI等`

破坏场景二：用户对于程序动态性的追求，例如 代码热替换、模块热部署等，OSGi（一种模块化规范）实现模块化热部署的关键是它自定义的类加载器机制的实现：每一个程序模块（Bundle）都有一个自己的类加载器，当需要更换一个 Bundle 时候就把 Bundle 连同类加载器一起替换来实现代码的热替换；在 OSGi 场景下类加载器是网状结构非树形，当收到类加载请求时候处理流程为：（只有开头的两个符合双亲委派模型，后面都是平级的）

- 将以 `java.*`开头的 类委派给父类加载器加载；
- 否则将委派列表名单内的类委派给父类加载器加载；
- 否则将 Import 列表中的类委派给 Export 这个类的 Bundle 的类加载器加载；
- 否则查找当前 Bundle 的 ClassPath，使用自己的类加载器进行加载；
- 否则查找类是否在自己的 Fragment Bundle 中，如果在则委派给 Fragment Bundle 的类加载器进行加载；
- 否则查找 Dynamic Import 列表中的 Bundle，委派给对应 Bundle 的类加载器进行加载；
- 否则类加载失败；

在父亲委托机制中，各个加载器按照父子关系形成了逻辑上的树形结构（但不是继承关系，是一种包含关系即每一个类加载器都有一个成员变量，该成员变量是其父加载器），除了根加载器之外，其余的类加载器都有一个父加载器

启动类加载器（根加载器）是属于虚拟机自身的一部分，不能被 Java 程序直接引用； 其他类加载器都是使用 Java 语言实现，独立于虚拟机外部，并且他们都继承抽象类：`java.lang.ClassLoader`

若有一个类能够成功加载自己编写的Test类，那么这个类加载器被称为**定义类加载器**，所有能成功返回Class对象引用的类加载器（包括定义类加载器）称为**初始类加载器**。

[![在这里插入图片描述](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/%E7%B1%BB%E5%8A%A0%E8%BD%BD%E5%99%A8%E6%B7%B1%E5%85%A5%E8%A7%A3%E6%9E%90%E4%B8%8E%E9%98%B6%E6%AE%B5%E5%88%86%E8%A7%A3.resource/20190823165459877.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/类加载器深入解析与阶段分解.resource/20190823165459877.png)

[![类加载过程](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/%E7%B1%BB%E5%8A%A0%E8%BD%BD%E5%99%A8%E6%B7%B1%E5%85%A5%E8%A7%A3%E6%9E%90%E4%B8%8E%E9%98%B6%E6%AE%B5%E5%88%86%E8%A7%A3.resource/%E7%B1%BB%E5%8A%A0%E8%BD%BD%E8%BF%87%E7%A8%8B.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/类加载器深入解析与阶段分解.resource/类加载过程.png)

- 测试7

```java
package com.gjxaiou.class10;

/**
 java.lang.String是由根加载器加载，在rt.jar包下
 */
public class MyTest7{
    public static void main(String[] args) throws ClassNotFoundException {
            // 加载之前需要获取 class 对象
            Class<?> clazz=Class.forName("java.lang.String");
            // 返回针对该类的类加载器（就是实际加载该类的加载器），其中 null 表示启动类（根类）的加载器
            System.out.println(clazz.getClassLoader());

            Class<?> clazz2=Class.forName("com.gjxaiou.class10.C");
            System.out.println(clazz2.getClassLoader()); 
    }
}
class C{
}

/** output:
 * null
 * sun.misc.Launcher$AppClassLoader@18b4aac2 :其中AppClassLoader:系统应用类加载器($前为外部类，后为内部类)
 */
```

- 测试8：

```java
package com.gjxaiou.class10;


import java.util.Random;

public class MyTest8{
    public static void main(String[] args) throws ClassNotFoundException {
        /**
         *   这里因为 x 前面有 final，所有是一个编译期常量，所有编译之后就会放在 MyTest8 类的常量池中，
         *   所以编译完之后 MyTest8 和 FinalTest 类之间就没有任何关系
         *   因此静态代码块都没有执行，因此 FinalTest 类都没有被初始化，所有将 FinalTest.class删除,代码仍然可以执行
         */
        System.out.println(FinalTest.x);
//        // 这里 y 值在编译期确定不了，得运行期使用该类。
//        System.out.println(FinalTest.y);
    }
}
class FinalTest{
    public static final int x = 3;
    public static final int y = new Random().nextInt(3);
    static {
        System.out.println("FinalTest static block");
    }
}
```

然后对该类进行反编译

[![image-20191201191720279](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/%E7%B1%BB%E5%8A%A0%E8%BD%BD%E5%99%A8%E6%B7%B1%E5%85%A5%E8%A7%A3%E6%9E%90%E4%B8%8E%E9%98%B6%E6%AE%B5%E5%88%86%E8%A7%A3.resource/image-20191201191720279.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/类加载器深入解析与阶段分解.resource/image-20191201191720279.png)

如果将 y 注释取消，进行反编译：

[![image-20191201192241853](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/%E7%B1%BB%E5%8A%A0%E8%BD%BD%E5%99%A8%E6%B7%B1%E5%85%A5%E8%A7%A3%E6%9E%90%E4%B8%8E%E9%98%B6%E6%AE%B5%E5%88%86%E8%A7%A3.resource/image-20191201192241853.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/类加载器深入解析与阶段分解.resource/image-20191201192241853.png)

```java
/**
        调用ClassLoader的loaderClass方法加载一个类，并不是对类的主动使用，不会导致类的初始化
*/
public class MyTest8{
    public static void main(String[] args){
         public static void main(String[] args){
            ClassLoader loader=ClassLoader.getSystemClassLoader();
            Class<?> clazz1=loader.loadClass("CL"); //不会初始化
            System.out.println(clazz1);
            System.out.println("-------------------");

            Class<?> clazz=Class.forName("CL");
            System.out.println(clazz);  //反射初始化
         }
    }
}
class CL{
    static {
        System.out.println("FinalTest static block);
    }
}
```

- 测试9：

  ```java
  package com.poplar.classload;
  
  /**
   * Created By poplar on 2019/11/7
   */
  public class ClassLoadTest9 {
  
      static {
          System.out.println("ClassLoadTest9");
      }
  
      public static void main(String[] args) {
          System.out.println(Child1.a);
      }
  }
  
  class Parent1 {
      static int a = 9;
  
      static {
          System.out.println("Parent1");
      }
  }
  
  class Child1 extends Parent1 {
      static int b = 0;
  
      static {
          System.out.println("Child1");
      }
  }
  
  //最后输出顺序
  //ClassLoadTest9
  // Parent1
  //9
  ```

  测试10:

  ```java
  package com.poplar.classload;
  
  /**
   * Created By poplar on 2019/11/7
   */
  public class ClassLoadTest10 {
  
      static {
          System.out.println("ClassLoadTest10");
      }
  
      public static void main(String[] args) {
          Parent2 parent2;
          parent2 = new Parent2();
          System.out.println(Parent2.a);
          System.out.println(Child2.b);
          /*执行结果：由于父类已经初始化过了所以Parent2只输出一次
           * ClassLoadTest10
           * Parent2
           * 2
           * Child2
           * 3
           */
      }
  }
  
  class Parent2 {
      static int a = 2;
  
      static {
          System.out.println("Parent2");
      }
  }
  
  class Child2 extends Parent2 {
      static int b = 3;
  
      static {
          System.out.println("Child2");
      }
  }
  ```

  测试11

  ```java
  package com.gjxaiou.class10;
  
  public class MyTest11 {
      public static void main(String[] args) {
          // 因为 a 是定义在父类中，因此是对于父类的主动使用（静态变量定义在哪（即谁拥有）就是对谁的主动使用）
          System.out.println(Child3.a);
          System.out.println("-------------");
          Child3.doSomething();
      }
  }
  
  class Parent3{
      static int a = 3;
      static{
          System.out.println("Parent static block");
      }
      static void doSomething(){
          System.out.println("do something");
      }
  }
  
  class Child3 extends Parent3{
      static {
          System.out.println("Child3 static block");
      }
  }
  
  /**
   * Parent static block
   * 3
   * -------------
   * do something
   */
  ```

  测试12：

  ```java
  package com.gjxaiou.class10;
  
  /**
   * 调用 ClassLoader 类的 loadClass 方法并不是主动使用类，不会导致类的初始化
   */
  public class MyTest12 {
      public static void main(String[] args) throws ClassNotFoundException {
  
          ClassLoader classLoader = ClassLoader.getSystemClassLoader();
          // 该行代码执行，并不会导致 G 的初始化
          Class<?> loadClass = classLoader.loadClass("com.gjxaiou.class10.G");
          System.out.println(loadClass);
          System.out.println("-------------------------------");
          //反射会导致一个类的初始化(属于七种之一)
          Class<?> clazz = Class.forName("com.gjxaiou.class10.G");
          System.out.println(clazz);
      }
  }
  
  class G {
      static {
          System.out.println("G");
      }
  }
  
  /*
  class com.gjxaiou.class10.G
  -------------------------------
  G
  class com.gjxaiou.class10.G
   */
  ```

  **类加载器的层次关系测试**

- 测试13

  ```java
  package com.gjxaiou.class10;
  
  // 输出类加载器的层次结构
  public class MyTest13 {
      public static void main(String[] args) {
          ClassLoader loader = ClassLoader.getSystemClassLoader();
          System.out.println(loader);
          System.out.println("-----------");
          while (loader != null) {
              loader = loader.getParent();
              System.out.println(loader);
          }
      }
  }
  
  /*
  sun.misc.Launcher$AppClassLoader@18b4aac2
  -----------
  sun.misc.Launcher$ExtClassLoader@1b6d3586
  null：表示根加载器
   */
  ```

- 测试14：如何通过给定的字节码路径把响应的资源打印出来

  ```java
  package com.gjxaiou.class10;
  
  import java.io.IOException;
  import java.net.URL;
  import java.util.Enumeration;
  
  public class MyTest14 {
      public static void main(String[] args) throws IOException {
          // 获取当前执行线程的上下文加载器
          ClassLoader loader = Thread.currentThread().getContextClassLoader();
          System.out.println(loader);
  
          String resourceName = "com/gjxaiou/class10/MyTest14.class";
          Enumeration<URL> urls = loader.getResources(resourceName);
          while (urls.hasMoreElements()) {
              URL url = urls.nextElement();
              System.out.println(url);
          }
      }
  }
  
  /**
   * sun.misc.Launcher$AppClassLoader@18b4aac2
   * file:/E:/Program/Java/JVM/DemoByMyself/out/production/DemoByMyself/com/gjxaiou/class10/MyTest14.class
   */
  ```

- 获取类加载器的途径：

  - `clazz.getClassLoader();` --获取当前类的加载器（clazz：表示当前类的 class 对象）
  - `Thread.currentThread().getContextClassLoader();` --获取当前线程上下文的加载器
  - `ClassLoader.getSystemClassLoader();` --获取系统的类加载器
  - `DriverManager.getCallerClassLoader();` --获取调用者的类加载器

- ClassLoader源码分析与实例剖析– ClassLoader是一个负责加载class的对象，ClassLoader类是一个抽象类，需要给出类的二进制名称，ClassLoader尝试定位或者产生一个class的数据，一个典型的策略是把二进制名字转换成文件名然后到文件系统中找到该文件。

  下面是双亲委派模型的主要实现：`java.lang.ClassLoader` 的 loadClass() 方法

  ```java
      protected Class<?> loadClass(String name, boolean resolve)
          throws ClassNotFoundException
      {
          synchronized (getClassLoadingLock(name)) {
              // 首先检查类是否被加载过
              Class<?> c = findLoadedClass(name);
              // 如果没有加载则调用父类加载器的 loadClass() 方法，
              if (c == null) {
                  long t0 = System.nanoTime();
                  try {
                      if (parent != null) {
                          c = parent.loadClass(name, false);
                          // 如果父类加载器为空则默认使用启动器类加载器作为父加载器
                      } else {
                          c = findBootstrapClassOrNull(name);
                      }
                  } catch (ClassNotFoundException e) {
                      // 如果父类加载器加载失败，抛出 ClassNotFoundException 
                  }
  
                  if (c == null) {
   					// 父类加载器无法加载的时候，再调用自己的 findClass() 方法进行加载
                      long t1 = System.nanoTime();
                      c = findClass(name);
  
                      // this is the defining class loader; record the stats
                      sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                      sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                      sun.misc.PerfCounter.getFindClasses().increment();
                  }
              }
              if (resolve) {
                  resolveClass(c);
              }
              return c;
          }
      }
  ```

注：`java.security.KeyStore$Builder$FileBuilder$1`，因为 `$`表示内部类，所以前面表示：keyStore 的内部类 Builder 的内部类 FileBuilder 的第一个匿名内部类（因为匿名内部类没有名字，使用数字代替）；

- 测试15

  ```java
  package com.gjxaiou.class10;
  
  /** 下面说明见：ClassLoader.java 的 doc 文档中
   对于数组，它对应的class对象不是由类加载器加载，而是由JVM在运行期动态的创建。然而对于数组类的类加载器来说，它返回的类加载器和数组内元素类型的类加载器是一样的（就是相当于 数组和数组中元素调用 .getClassLoader() 返回值一样）。如果数组类元素是原生类，那么数组是没有类加载器的。
   */
  public class MyTest15{
      public static void main(String[] args){
          String[] strings=new String[2];
          System.out.println(strings.getClass());
          System.out.println("-------");
          System.out.println(strings.getClass().getClassLoader());
          System.out.println("-------");
          MyTest15[] mytest15=new MyTest15[2];
          System.out.println(mytest15.getClass().getClassLoader());
          System.out.println("-------");
          int[] arr=new int[2];
          System.out.println(arr.getClass().getClassLoader());
      }
  }
  
  /**
   * class [Ljava.lang.String;
   * -------
   * null：这里的 null 表示根类加载器
   * -------
   * sun.misc.Launcher$AppClassLoader@18b4aac2
   * -------
   * null ：这里的 null 和上面的 null 不一样，这里是原生数组，因此没有类加载器
   */
  ```

并行类加载器可支持并发加载，需要在类初始化期间调用 ClassLoader.registerAaParallelCapable() 方法进行注册。ClassLoader类默认支持并发加载，但是其子类必须在初始化期间进行注册。

- 测试16

  ```java
  package com.gjxaiou.class10;
  
  import java.io.*;
  
  /**
   * 创建自定义加载器，需要继承 ClassLoader
   */
  public class MyTest16 extends ClassLoader {
      private String classLoaderName;
      // 从哪里进行加载，如果没有指定就是从项目下
      private String path;
      // 指定类的后缀名
      private final String fileExtension = ".class";
  
      public MyTest16(String classLoaderName) {
          // 默认会将系统（应用）类加载器当做该类的父加载器
          super();
          this.classLoaderName = classLoaderName;
      }
  
      public MyTest16(ClassLoader parent, String classLoaderName) {
          // 显式指定该类的父加载器
          super(parent);
          this.classLoaderName = classLoaderName;
      }
  
      public MyTest16(ClassLoader parent) {
          // 显式指定该类的父加载器
          super(parent);
      }
  
      public void setPath(String path) {
          this.path = path;
      }
  
      // 根据 className 来寻找该类，该类在检查完父类加载器之后自动被 loadClass 调用，而这里我们没有重写 loadClass 方法，因此会自动调用
      @Override
      protected Class<?> findClass(String className) {
          System.out.println("className=" + className);
          System.out.println("class loader name:" + this.classLoaderName);
          // 因为传入的格式为： com.a.b,需要转换为路径格式：com/a/b（以 Windows 为例）
          className = className.replace(".", File.separator);
          byte[] data = new byte[0];
          try {
              data = loadClassData(className);
          } catch (IOException e) {
              e.printStackTrace();
          }
          return defineClass(className, data, 0, data.length); //define方法为父类方法
      }
  
      // 根据类的名字，将类的二进制数组数组加载出来(将它的文件找到，然后以输入输出流的方式返回字节数组，该字节数组就是从文件中读取出的 class 文件的二进制信息)
      private byte[] loadClassData(String className) throws IOException {
          InputStream is = null;
          byte[] data = null;
          ByteArrayOutputStream baos = null;
          try {
              is = new FileInputStream(new File(this.path + className + this.fileExtension));
              baos = new ByteArrayOutputStream();
              int ch;
              while (-1 != (ch = is.read())) {
                  baos.write(ch);
              }
              data = baos.toByteArray();
  
          } catch (Exception e) {
          } finally {
              is.close();
              baos.close();
              return data;
          }
      }
  
      public static void test(ClassLoader classLoader) throws ClassNotFoundException,
              IllegalAccessException, InstantiationException {
          // loadClass 底层会调用上面的 findClass 和 loadClassData
          Class<?> clazz = classLoader.loadClass("com.gjxaiou.class10.MyTest7");
          //loadClass是父类方法，在方法内部调用findClass
          System.out.println(clazz.hashCode());
          // 通过 class 对象获取相应想要创建的实例
          Object object = clazz.newInstance();
  
          System.out.println(object);
      }
  
  
      public static void main(String[] args) throws IllegalAccessException, InstantiationException,
              ClassNotFoundException {
          //父亲是系统类加载器，根据父类委托机制，MyTest1由系统类加载器加载了，并不是自己定义的加载器加载的，因为上面有一个 super（） 方法
          MyTest16 loader1 = new MyTest16("loader1");
          test(loader1);
          /** 只执行 loader1
           * output:
           * 460141958
           * com.gjxaiou.class10.MyTest7@4554617c
           */
  
          //仍然是系统类加载器进行加载的，因为路径正好是classpath
          MyTest16 loader2 = new MyTest16("loader2");
          loader2.path = "/out/production/DemoByMyself/com/gjxaiou/class10/";
          test(loader2);
          /** 只执行 loader2
           * output:
           * 460141958
           * com.gjxaiou.class10.MyTest7@4554617c
           */
  
          //自定义的类加载器被执行，findClass 方法下的输出被打印。前提是当前 classpath 下不存在 MyTest7
          // .class，MyTest16的父加载器-系统类加载器会尝试从classpath中寻找MyTest7。
          MyTest16 loader3 = new MyTest16("loader3");
          // 在桌面上创建同样上面目录，然后将该类放进去(同时将左边生成的 MyTest7 删除)
          loader3.path = "C:/Users/gjx16/Desktop/demo/";
          test(loader3);
  
          //与3同时存在，输出两个class的hash不一致，findClass方法下的输出均被打印，原因是类加载器的命名空间问题。（删左边）
          MyTest16 loader4 = new MyTest16("loader4");
          loader4.path = "C:/Users/gjx16/Desktop/demo/";
          test(loader4);
  
          //将loader3作为父加载器
          MyTest16 loader5 = new MyTest16(loader3, "loader3");
          loader3.path = "C:/Users/gjx16/Desktop/demo/";
          test(loader5);
      }
  }
  ```

上面就涉及到了命名空间；

## （二）命名空间

- 每个类加载器都有自己的命名空间，==**命名空间由该加载器及所有父加载器所加载的类构成**==；
- 在同一个命名空间中，不会出现类的完整名字（包括类的包名）相同的两个类；
- 在不同的命名空间中，有可能会出现类的完整名字（包括类的包名）相同的两个类；（可以被加载多次）
- 同一命名空间内的类是互相可见的（但是相互能否访问是由权限修饰符决定），**非同一命名空间内的类是不可见的**；
- 子加载器加载的类可以见到父加载器加载的类，**父加载器加载的类不能见到子加载器加载的类**。
- 如果两个加载器之间没有直接或者间接的父子关系，那么它们相互各自加载的类相互不可见；

# 四、类的卸载

- 当一个类被加载、连接和初始化之后，它的生命周期就开始了。当此类的Class对象不再被引用，即不可触及时，Class对象就会结束生命周期，类在方法区内的数据也会被卸载。

- **一个类何时结束生命周期，取决于代表它的Class对象何时结束生命周期**。

- **由 Java 虚拟机自带的类加载器所加载的类，在虚拟机的生命周期中，始终不会被卸载**。Java 虚拟机本身会始终引用这些加载器，而这些类加载器则会始终引用他们所加载的类的 Class 对象，因此这些Class对象是可触及的。

- 由用户自定义的类加载器所加载的类是可以被卸载的。

  ```java
  /**
      自定义类加载器加载类的卸载
      -XX：+TraceClassUnloading
  */
  
  // 前面的代码同  MyTest16
     public static void main(String[] args){
          MyTest16 loader2=new MyTest16("loader2");  
          loader2.path="D:\Eclipse\workspace\HiATMP-DDMS\target\classes\";
          test(loader2);
          loader2=null;
          System.gc();   //让系统去显式执行垃圾回收
  
          输出的两个对象hashcode值不同，因为前面加载的已经被卸载了
          loader2=new MyTest16("loader6"); //  
          test(loader2);
     }
  ```

观察方式二：

在jdk/bin下面打开gvisualvm 查看当前java进程，同时需要在上面代码中的 `System.gc()`后面添加 `Thread.sleep(100000)`

- 测试17：类中使用了另一个类，加载的过程

  ```java
  package com.gjxaiou.class10;
  
  class MyCat{
      public MyCat(){
          // 把加载 MyCat（）类的 class 对象打印出来
          // this.getClass() 获取调用类所对应的唯一的 Class 对象
          // 因为类加载器加载的是类对应的 Class 对象，因此 Class 对象中有 getClassLoader() 方法
          System.out.println("MyCat is loaded..." + this.getClass().getClassLoader());
      }
  }
  ```

  ```java
  package com.gjxaiou.class10;
  
  class MySample{
      public MySample(){
          System.out.println("MySample is loaded..."+this.getClass().getClassLoader());
          
          new MyCat();
      }
  }
  ```

  ```java
  package com.gjxaiou.class10;
  
  /**
   创建自定义加载器，继承ClassLoader
   */
  public class MyTest17 {
      public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException,
              InstantiationException {
          MyTest16 loader1=new MyTest16("loader1");
          // 返回的 class 对象就是 MySample 类对应的 Class 对象，下面可以通过反射创建 MySample 的一个实例
          Class<?> clazz=loader1.loadClass("com.gjxaiou.class10.MySample");
          System.out.println(clazz.hashCode());
          //如果注释掉下面该行，就并不会实例化 MySample 对象，不会加载 MyCat（可能预先加载，不一定，通过VM options 可以看到这里是加载了）
          // 因为 newInstance（） 中没有参数，因此会调用 MySample 中的无参构造方法
          System.out.println("----------------");
          Object  object=clazz.newInstance(); //加载和实例化了MySample和MyCat
          System.out.println("---------------");
      }
  }
  /**
   *460141958
   * MySample is loaded...sun.misc.Launcher$AppClassLoader@18b4aac2
   * MyCat is loaded...sun.misc.Launcher$AppClassLoader@18b4aac2
   */
  ```

[![image-20191204141013980](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/%E7%B1%BB%E5%8A%A0%E8%BD%BD%E5%99%A8%E6%B7%B1%E5%85%A5%E8%A7%A3%E6%9E%90%E4%B8%8E%E9%98%B6%E6%AE%B5%E5%88%86%E8%A7%A3.resource/image-20191204141013980.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/类加载器深入解析与阶段分解.resource/image-20191204141013980.png)

测试17_1

```java
package com.gjxaiou.class10;
// 通过设置路径，然后删除类路径下面的 class 文件，使用自定义类加载器来加载这两个类
public class MyTest17_1 {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException,
            InstantiationException {

        MyTest16 loader1=new MyTest16("loader1");
        loader1.setPath("C:/Users/gjx16/Desktop/");
        Class<?> clazz=loader1.loadClass("com.gjxaiou.class10.MySample");
        System.out.println(clazz.hashCode());
        Object  object=clazz.newInstance();
    }
}
```

[![image-20191204150446968](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/%E7%B1%BB%E5%8A%A0%E8%BD%BD%E5%99%A8%E6%B7%B1%E5%85%A5%E8%A7%A3%E6%9E%90%E4%B8%8E%E9%98%B6%E6%AE%B5%E5%88%86%E8%A7%A3.resource/image-20191204150446968.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/类加载器深入解析与阶段分解.resource/image-20191204150446968.png)

注：如果上面的删除 MyCat.class ，然后保留 MySample.class 文件，当然指定路径在的都在，执行上面程序结果为：

因为执行到 `new MyCat()`时候，首先由加载了 MySample 类的加载器尝试加载 MyCat（），这里是应用加载器加载了 MySample 类，所以它也会直接尝试加载 MyCat 类，当然还是会根据双亲委托机制先有父类加载器进行加载，但是所有的加载器都是不能加载的，所以报错。

[![image-20191204150456819](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/%E7%B1%BB%E5%8A%A0%E8%BD%BD%E5%99%A8%E6%B7%B1%E5%85%A5%E8%A7%A3%E6%9E%90%E4%B8%8E%E9%98%B6%E6%AE%B5%E5%88%86%E8%A7%A3.resource/image-20191204150456819.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/类加载器深入解析与阶段分解.resource/image-20191204150456819.png)

如果删除 MySample.class ，然后保留 MyCat.class文件，当然指定路径下的类文件都在，执行结果为：`

[![image-20191204152649511](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/%E7%B1%BB%E5%8A%A0%E8%BD%BD%E5%99%A8%E6%B7%B1%E5%85%A5%E8%A7%A3%E6%9E%90%E4%B8%8E%E9%98%B6%E6%AE%B5%E5%88%86%E8%A7%A3.resource/image-20191204152649511.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/类加载器深入解析与阶段分解.resource/image-20191204152649511.png)

```java
public class MyTest17_1 extends ClassLoader{
    private String classLoaderName;
    private String path；
    private final String fileExtension=".class";

    public MyTest17_1(String classLoaderName){
        super();        //将系统类当做该类的父加载器
        this.classLoaderName=classLoaderName;
    }
    public MyTest17_1(ClassLoader parent,String classLoaderName){
        super(parent);      //显式指定该类的父加载器
        this.classLoaderName=classLoaderName;
    }

    public void setPath(String path){
        this.path=path;
    }
    @Override
    protect Class<?> findClass(String className){
        System.out.println("calssName="+className);
        className=className.replace(".",File.separator);
        byte[] data=loadClassData(className);
        return defineClass(className,data,0,data.length); //define方法为父类方法
    }

    private byte[] loadClassData(String name){
        InputStream is=null;
        byte[] data=null;
        ByteArrayOutputStream baos=null;
        try{
            is=new FileInputStream(new File(this.path+name+this.fileExtension));
            baos=new ByteArrayOutputStream();
            int ch;
            while(-1!=(ch=is.read())){
                baos.write(ch);
            }
            data=baos.toByteArray();
        }catch(Exception e){
        }finally{
            is.close();
            baos.close();
             return data;
        }
    }
    public static void main(String[] args){
        MyTest17_1 loader1=new MyTest17_1("loader1");
        loader1.path="C:\Users\weichengjie\Desktop";
        Class<?> clazz=loader1.loadClass("com.hisense.MySample");  
        System.out.println(clazz.hashCode());
        //MyCat是由加载MySample的加载器去加载的：
      	//如果只删除classpath下的MyCat，则会报错，NoClassDefFoundError；
        //如果只删除calsspath下的MySample，则由自定义加载器加载桌面上的MySample，由系统应用加载器加载MyCat。
        Object  object=clazz.newInstance(); 
    }

}
```

测试17_1_1：修改MyCat和MySample，重新 build

然后将所有的 class 文件还是放在桌面，同时删除类路径下面的 MySample.class 文件；

```java
class MyCat{
    public MyCat(){
        System.out.println("MyCat is loaded..."+this.getClass().getClassLoader());
        // 步骤一：在 MyCat 的构造方法中引用 MySample 的一个 Class 对象
        System.out.println("from MyCat: "+MySample.class);
    }
}

class MySample{
    public MySample(){
        System.out.println("MySample is loaded..."+this.getClass().getClassLoader());
        new MyCat();
        // 步骤二：在 MySample 中引用 MyCat
        System.out.println("from MySample :"+ MyCat.class);
    }
}

public class MyTest17_1 {
        public static void main(String[] args){
        //修改MyCat后，仍然删除classpath下的MySample，留下MyCat，程序报错
        //因为命名空间，父加载器找不到子加载器所加载的类，因此MyCat找不到        
        //MySample。
        MyTest17_1 loader1=new MyTest17_1("loader1");
        loader1.path="C:\Users\weichengjie\Desktop";
        Class<?> clazz=loader1.loadClass("com.hisense.MySample");  
        System.out.println(clazz.hashCode());
        Object  object=clazz.newInstance(); 
    }
}
```

仅仅步骤一：结果

[![image-20191204153424955](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/%E7%B1%BB%E5%8A%A0%E8%BD%BD%E5%99%A8%E6%B7%B1%E5%85%A5%E8%A7%A3%E6%9E%90%E4%B8%8E%E9%98%B6%E6%AE%B5%E5%88%86%E8%A7%A3.resource/image-20191204153424955.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/类加载器深入解析与阶段分解.resource/image-20191204153424955.png)

因为两个类是由不同的类加载器进行加载的，系统类加载器是自定义加载器的父类，所以在父加载器中的类就看不到子加载器加载的类

- 关于命名空间重要说明：
  - 命令空间由该加载器及其所有父加载器所加载的类组成，因此子加载器所加载的类能够访问父加载器所加载的类；
  - 而父加载器所加载的类无法访问子加载器所加载的类；

仅仅步骤二的结果：同时在编译之后仅仅删除 MySample.class，保持桌面上两个都有

[![image-20191204154448493](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/%E7%B1%BB%E5%8A%A0%E8%BD%BD%E5%99%A8%E6%B7%B1%E5%85%A5%E8%A7%A3%E6%9E%90%E4%B8%8E%E9%98%B6%E6%AE%B5%E5%88%86%E8%A7%A3.resource/image-20191204154448493.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/类加载器深入解析与阶段分解.resource/image-20191204154448493.png)

子加载器所加载的类能够访问父加载器所加载的类，所以可以访问；

- 测试18：获取各种类加载器加载的路径

  下面代码使用 IDEA 的 run 命令和 直接使用 `java 全限定类名`执行的结果不相同（第三个不相同），因为本质上应用类加载器路径为 `.`即项目的 `out/product/classes/`下面，IDEA 结果更多是因为它主动帮我们增加了一些其他可能的路径；

```java
package com.gjxaiou.class10;

public class MyTest18{
    public static void main(String[] args){
        //根加载器路径
        System.out.println(System.getProperty("sun.boot.class.path"));
        System.out.println("------------------");
        //扩展类加载器路径
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println("------------------");
        //应用类加载器路径
        System.out.println(System.getProperty("java.class.path"));
    }
}

/** output:
 *E:\Program\Java\JDK1.8\jre\lib\resources.jar;
 * E:\Program\Java\JDK1.8\jre\lib\rt.jar;        ☆☆☆
 * E:\Program\Java\JDK1.8\jre\lib\sunrsasign.jar;
 * E:\Program\Java\JDK1.8\jre\lib\jsse.jar;
 * E:\Program\Java\JDK1.8\jre\lib\jce.jar;
 * E:\Program\Java\JDK1.8\jre\lib\charsets.jar;
 * E:\Program\Java\JDK1.8\jre\lib\jfr.jar;
 * E:\Program\Java\JDK1.8\jre\classes            ☆☆☆该路径在磁盘上是不存在的，需要自己创建
 * ------------------
 * E:\Program\Java\JDK1.8\jre\lib\ext;           ☆☆☆
 * C:\WINDOWS\Sun\Java\lib\ext
 * ------------------
 * E:\Program\Java\JDK1.8\jre\lib\charsets.jar;
 * E:\Program\Java\JDK1.8\jre\lib\deploy.jar;
 * E:\Program\Java\JDK1.8\jre\lib\ext\access-bridge-64.jar;
 * E:\Program\Java\JDK1.8\jre\lib\ext\cldrdata.jar;
 * E:\Program\Java\JDK1.8\jre\lib\ext\dnsns.jar;
 * E:\Program\Java\JDK1.8\jre\lib\ext\jaccess.jar;
 * E:\Program\Java\JDK1.8\jre\lib\ext\jfxrt.jar;
 * E:\Program\Java\JDK1.8\jre\lib\ext\localedata.jar;
 * E:\Program\Java\JDK1.8\jre\lib\ext\nashorn.jar;
 * E:\Program\Java\JDK1.8\jre\lib\ext\sunec.jar;
 * E:\Program\Java\JDK1.8\jre\lib\ext\sunjce_provider.jar;
 * E:\Program\Java\JDK1.8\jre\lib\ext\sunmscapi.jar;
 * E:\Program\Java\JDK1.8\jre\lib\ext\sunpkcs11.jar;
 * E:\Program\Java\JDK1.8\jre\lib\ext\zipfs.jar;
 * E:\Program\Java\JDK1.8\jre\lib\javaws.jar;
 * E:\Program\Java\JDK1.8\jre\lib\jce.jar;
 * E:\Program\Java\JDK1.8\jre\lib\jfr.jar;
 * E:\Program\Java\JDK1.8\jre\lib\jfxswt.jar;
 * E:\Program\Java\JDK1.8\jre\lib\jsse.jar;
 * E:\Program\Java\JDK1.8\jre\lib\management-agent.jar;
 * E:\Program\Java\JDK1.8\jre\lib\plugin.jar;
 * E:\Program\Java\JDK1.8\jre\lib\resources.jar;
 * E:\Program\Java\JDK1.8\jre\lib\rt.jar;
 * E:\Program\Java\JVM\DemoByMyself\out\production\DemoByMyself; ☆☆☆
 * D:\JetBrains\IntelliJ IDEA 2019.2.4\lib\idea_rt.jar
 */
```

- 测试18_1：根据上面得到的根加载器的路径，将类放入该路径（这里路径为：E:\Program\Java\JDK1.8\jre\classes ，该路径本质上不存在，需要新建（上一层路径是存在的，需要新建 classes 文件夹）），然后就是由根加载器进行加载；(测试完之后删除该路径)

```java
package com.gjxaiou.class10;

public class MyTest18_1{
    public static void main(String[] args) throws ClassNotFoundException {
        MyTest16 loader1=new MyTest16("loader1");
        loader1.setPath("C:/Users/gjx16/Desktop/");

        //把MyTest7.class文件放入到根类加载器路径中，则由根类加载器加载MyTest7
        Class<?> clazz= loader1.loadClass("com.gjxaiou.class10.MyTest7");

        System.out.println("clazz:"+clazz.hashCode());
        System.out.println("class loader:"+clazz.getClassLoader());

    }
}

/** output:
 * clazz:1627674070
 * class loader:null
 */
```

- 测试19：测试拓展类加载器使用和修改加载器路径后果

```java
package com.gjxaiou.class10;

import com.sun.crypto.provider.AESKeyGenerator;

/**
 各加载器的路径是可以修改的，修改后会导致运行失败，ClassNotFoundExeception
 使用命令： java -Djava.ext.dirs=./ com.gjxaiou.class10.MyTest19
 上面命令是将拓展类加载器的路径修改为当前目录，然后执行该类（因为当前目录不存在 AESKeyGenerator 类，所有报错）
 */
public class MyTest19{
    public static void main(String[] args){
        AESKeyGenerator aesKeyGenerator=new AESKeyGenerator();
        //输出扩展类加载器
        System.out.println(aesKeyGenerator.getClass().getClassLoader());
        //输出应用类加载器
        System.out.println(MyTest19.class.getClassLoader());
    }
}

/** output
 * sun.misc.Launcher$ExtClassLoader@4b67cf4d
 * sun.misc.Launcher$AppClassLoader@18b4aac2
 */
```

- 测试20

  ```java
  package com.gjxaiou.class10;
  
  class MyPerson{
      private MyPerson myPerson;
      public void setMyPerson(Object object){
          // 这里需要进行向下类型转换，因为如果不转换直接传入 MyPerson 对象，则下面代码的第 21 行中就需要传入： MyPerson.class,但是因为 MyPerson.class 以及删除了所以直接报错，下面代码就不执行了
          this.myPerson=(MyPerson)object;
      }
  }
  ```

```java
package com.gjxaiou.class10;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyTest20{
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        MyTest16 loader1 = new MyTest16("loader1");
        MyTest16 loader2 = new MyTest16("loader2");

        // 两个类加载器尝试加载同一个 class 对象
        Class<?> clazz1 = loader1.loadClass("com.gjxaiou.class10.MyPerson");
        Class<?> clazz2 = loader2.loadClass("com.gjxaiou.class10.MyPerson");
        //clazz1和clazz均由应用类加载器加载的，第二次不会重新加载，结果为true
        System.out.println(clazz1==clazz2);

        // 分别通过反射创建它们的实例
        Object object1 = clazz1.newInstance();
        Object object2 = clazz2.newInstance();
        // 获取到 clazz1 对象中的 setMyPerson() 方法，该方法需要接受一个 Object 类型参数
        Method method = clazz1.getMethod("setMyPerson",Object.class);
        method.invoke(object1,object2);
    }
}
```

- 测试21：没有直接、间接父子关系中两个类加载器中的类相互不可见

```java
package com.gjxaiou.class10;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
//删掉 classpath 下的 MyPerson 类，桌面上当然有
public class MyTest21{
    public static void main(String[] args) throws IllegalAccessException, InstantiationException,
            ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
        MyTest16 loader1 = new MyTest16("loader1");
        MyTest16 loader2 = new MyTest16("loader2");
        loader1.setPath("C:/Users/gjx16/Desktop/");
        loader2.setPath("C:/Users/gjx16/Desktop/");
        // 这里是 loader1 真正加载了该类，loader1 就是定义类加载器
        Class<?> clazz1 = loader1.loadClass("com.gjxaiou.class10.MyPerson");
        // 这里是 loader2 真正加载了该类，loader2 就是定义类加载器
        Class<?> clazz2 = loader2.loadClass("com.gjxaiou.class10.MyPerson");
        //clazz1和clazz由loader1和loader2加载，从双亲委托机制来看它们之间没有任何关系，两者都会在 JVM 开辟内存空间，加载对应的 class
        // 对象，两者的命名空间完全独立（因为每个类加载器都有自己的命名空间），因此结果为false
        System.out.println(clazz1 == clazz2);

        Object object1=clazz1.newInstance();
        Object object2=clazz2.newInstance();

        Method method=clazz1.getMethod("setMyPerson",Object.class);
        //此处报错，loader1和loader2所处不用的命名空间
       // 调用 object1 这个对象（在一个命名空间中 class 对象所生成的实例），传入的是另一个命名空间中同样名称的 class 对象所生成的实例（以为两个不同命名空间中的 class 对象之间不可见，所以其对应生成的对象之间也是不可见的）
        method.invoke(object1,object2);
    }
}
```

[![image-20191204164559437](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/%E7%B1%BB%E5%8A%A0%E8%BD%BD%E5%99%A8%E6%B7%B1%E5%85%A5%E8%A7%A3%E6%9E%90%E4%B8%8E%E9%98%B6%E6%AE%B5%E5%88%86%E8%A7%A3.resource/image-20191204164559437.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/类加载器深入解析与阶段分解.resource/image-20191204164559437.png)

- 类加载器双亲委托模型的好处：

  - 可以确保 Java 核心库类型的安全：例如所有的 Java 应用都会引用 java.lang.Object 中的类，也就是说在运行期 java.lang.Object 中的类会被加载到虚拟机中，如果这个加载过程如果是由自己的类加载器所加载，那么很可能就会在 JVM 中存在多个版本的 java.lang.Object 中的类，而且这些类是相互不可见的（命名空间的作用）。借助于双亲委托机制，Java核心类库中的类的加载工作都是由启动根加载器去加载，从而确保了Java应用所使用的的都是同一个版本的Java核心类库，他们之间是相互兼容的；
  - 确保 Java 核心类库中提供的类不会被自定义的类所替代；不会因为加载恶意代码到内存中从而威胁 JVM；
  - 不同的类加载器可以为相同名称的类（binary name）创建额外的命名空间。相同名称的类可以并存在Java虚拟机中，只需要用不同的类加载器去加载即可。因为不同类加载器（只要两个类加载器之间没有双亲委托关系即可，可以是同一个类加载器的两个实例）所加载的类之间是不兼容的，相当于在Java虚拟机内部建立了一个又一个相互隔离的Java类空间。

- 父亲委托机制的优点是能够提高软件系统的安全性。因此在此机制下，用户自定义的类加载器不可能加载应该由父类加载器加载的可靠类，从而防止不可靠甚至恶意的代码代替由父类加载器加载的可靠代码。例如，java.lang.Object 类是由跟类加载器加载，其他任何用哪个户自定义的类加载器都不可能加载含有恶意代码的java.lang.Object 类。

  测试22：测试拓展类加载器

```java
package com.gjxaiou.class10;

public class MyTest22{
    static{
        System.out.println("MyTest22 initislizer");
    }
    public static void main(String[] args){
    // 因为这两个 class 都在类路径中，所有是系统类加载器加载
        System.out.println(MyTest22.class.getClassLoader());
        System.out.println(MyTest7.class.getClassLoader());
    }
}
/**output:
 * MyTest22 initislizer
 // 从下面结果看出：是同一个类加载器的实例来加载了这两个 class 
 * sun.misc.Launcher$AppClassLoader@18b4aac2
 * sun.misc.Launcher$AppClassLoader@18b4aac2
 */
```

- 扩展类加载器只加载jar包，需要把class文件打成jar，直接使用下面的修改路径命令执行是不行的

对应的测试程序

首先进入 out/production 下面将 MyTest7.class 打成 test.jar，命令如下

```java
E:\Program\Java\JVM\DemoByMyself\out\production\DemoByMyself>jar cvf test.jar com/gjxaiou/class10/MyTest7.class
已添加清单
正在添加: com/gjxaiou/class10/MyTest7.class(输入 = 962) (输出 = 545)(压缩了 43%)

E:\Program\Java\JVM\DemoByMyself\out\production\DemoByMyself>java -Djava.ext.dirs=./ com.gjxaiou.class10.MyTest7

/** output:
  * MyTest22 initializer
  * sun.misc.Launcher$AppClassLoader@2a139a55
  * sun.misc.Launcher$ExtClassLoader@3d4eac69
  */
```

- 测试23：命名空间以及类加载器

```java
/*
    在运行期，一个Java类是由该类的完全限定名（binary name）和用于加载该类的定义类加载器所共同决定的。如果同样名字（完全相同限定名）是由两个不同的加载器所加载，那么这些类就是不同的，即便.class文件字节码相同，并且从相同的位置加载亦如此。
    在oracle的hotspot，系统属性sun.boot.class.path如果修改错了，则运行会出错：
    Error occurred during initialization of VM
    java/lang/NoClassDeFoundError: java/lang/Object
*/
 public class MyTest23{
    public static void main(String[] args){
        System.out.println(System.getProperty("sun.boot.class.path"));
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println(System.getProperty("java.calss.path"));

        // 结果为 null，表示启动类加载器
        System.out.println(ClassLoader.class.getClassLoader);
        // 结果为 null，表示启动类加载器
        // 如果某个加载器加载了 Launcher.class(位于)类，那么该加载器同样会尝试加载 Launcher 类依赖的所有组件，其中包括 AppClassLoader 和 ExtClassLoader,这两个是 Launcher 类的内部静态类，因为这两个加载器前面没有 public 修饰，所以只能看 Launcher 类是什么加载器进行加载的
        System.out.println(Launcher.class.getClassLoader);

        //下面的系统属性指定系统类加载器，默认是AppClassLoader
        // 输出： null，相当于默认情况下没有定义，就指向 APPClassLoader
        // 可以将自定义类加载器定义为默认的系统类加载器，例如将 MyTest16定义为默认》》》
            // 首先在 MyTest16.java 中创建一个固定的构造函数给系统调用
            /*
             * public MyTest16(ClassLoader parent){
             * 		super(parent);
             * }
             */
             // 然后执行 java -Djava.system.class.loader=com.gjxaiou.class10.MyTest com.gjxaiou.class10.MyTest23 ，就是以 MyTest16 为系统加载器来执行 MyTest23 类
        	
         System.out.println(System.getProperty("java.system.class.loader"));
        // 同时可以在命令行中执行该类可以看出下面这句输出为： MyTest16
        System.out.println(ClassLoader.getSystemClassLoader());
    }
 }
```

- 类加载器本身也是类加载器，类加载器又是谁加载的呢？？（先有鸡还是现有蛋） 类加载器是由启动类加载器去加载的，启动类加载器是C++写的，内嵌在JVM中。
- 内嵌于JVM中的启动类加载器会加载 `java.lang.ClassLoader`以及其他的Java平台类(例如 java.lang.Object/java.lang.String 等等)。当JVM启动时，一块特殊的机器码会运行，它会加载扩展类加载器以及系统类加载器，这块特殊的机器码叫做启动类加载器。
- 启动类加载器并不是java类，其他的加载器都是java类。
- 启动类加载器是特定于平台的机器指令，它负责开启整个加载过程。

==P26== 为 类加载器源代码讲解（这里省略）

**tomcat **是先自己加载，自己加载不了才使用父类加载器

## （三）线程上下文类加载器

==部分视频没有看==

例如 JDBC 中，首先 Java 的核心库 rt.jar 中提供了 connection 和 Statement 的接口，因此它们由 根加载器进行加载，但是他们的具体的实现是具体厂商（mysql /orcle等）提供，因为接口会使用到实现类，因此就会使用接口的根类加载器来尝试加载实现类，但是实现作为一个 jar 包放在应用 classpath 下面，由系统类加载器进行加载，根加载器是加载不到实现部分的代码的，且父加载器加载的类是看不到子加载器加载的类的，所以访问不到实现类的；

```java
package com.gjxaiou.class10;

/**
    当前类加载器(Current ClassLoader)：用于加载当前类的类加载器
        每个类都会尝试使用自己的类加载器去加载它依赖的其它类。

    线程上下文类加载器(Context ClassLoader)：从 jdk 1.2 开始引入
    线程类（Thead）中的 getContextClassLoader() 与 setContextClassLoader(ClassLoader c)分别用来获取和设置上下文类加载器
    如果没有通过setContextClassLoader()
 方法设置，线程将继承父线程的上下文类加载器，java 应用运行时的初始线程（就是启动该应用的线程）的上下文类加载器是系统类加载器。该线程中运行的代码可以通过该类加载器加载类和资源。

    线程上下文类加载器的作用：
    SPI：Service Provide Interface：服务提供接口
    作用：父 ClassLoader 可以使用当前线程 Thread.currentThread().getContextClassLoader()
 所制定的 ClassLoader 加载的类，这就改变了父加载器加载的类无法使用子加载器或是其他没有父子关系的ClassLoader加载的类的情况，即改变了双亲委托模型。

    在双亲委托模型下，类加载是由下至上的，即下层的类加载器会委托父加载器进行加载。但是对于SPI来说，有些接口是Java核心库所提供的的（如JDBC），且Java
 核心库是由启动类记载器去加载的，而这些接口的实现却来自不同的jar包（厂商提供），Java的启动类加载器是不会加载其他来源的jar包，这样传统的双亲委托模型就无法满足SPI的要求。通过给当前线程设置上下文类加载器，就可以由设置的上下文类加载器来实现对于接口实现类的加载。
*/
public class MyTest24{
    public static void main(String[] args){
        // java应用运行时的初始线程（就是启动该应用的线程）的上下文类加载器是系统类加载器，所以输出是。。
        System.out.println(Thread.currentThread().getContextClassLoader());
        // 获取该线程的类加载器
        System.out.println(Thread.class.getClassLoader());
    }
}
/**output:
 * sun.misc.Launcher$AppClassLoader@18b4aac2
 * null
 */
```

- 测试25：线程上下文类加载器测试

```java
package com.gjxaiou.class10;

public class MyTest25 implements Runnable{
    private Thread thread;
    public MyTest25(){
        // 使用new 就会执行构造方法，就会创建该线程对象
        thread =new Thread(this);
        // 通过start 就会执行下面的run 方法
        thread.start();
    }

    @Override
    public void run(){
        ClassLoader classLoader=this.thread.getContextClassLoader();
        this.thread.setContextClassLoader(classLoader);

        System.out.println("Class:"+classLoader.getClass());
        System.out.println("Parent:"+classLoader.getParent().getClass());
    }

    public static void main(String[] args){
        new MyTest25();
    }
}
/** output:
 * Class:class sun.misc.Launcher$AppClassLoader
 * Parent:class sun.misc.Launcher$ExtClassLoader
 */
```

为什么默认的线程上下文类加载器是应用类加载器，原因在 sun.misc.Launcher代码中：

```java
 public Launcher() {
        Launcher.ExtClassLoader var1;
        try {
            var1 = Launcher.ExtClassLoader.getExtClassLoader();
        } catch (IOException var10) {
            throw new InternalError("Could not create extension class loader", var10);
        }

        try {
            this.loader = Launcher.AppClassLoader.getAppClassLoader(var1);
        } catch (IOException var9) {
            throw new InternalError("Could not create application class loader", var9);
        }
		// 将 this.loader 设置成上下文类加载器，this.loader 看上面可以得到是 appClassLoader
        Thread.currentThread().setContextClassLoader(this.loader);
        String var2 = System.getProperty("java.security.manager");
```

- 线程上下文类加载器的一般使用模式：（步骤：获取-使用-还原） 伪代码：

  ```java
  // 获取当前线程的线程上下文类加载器 ClassLoader =Thread.currentThread().getContextLoader(); 
  try{ // targetTccl 是之前通过某种方式以及得到的类加载器 					 
      Thread.currentThread().setContextLoader(targetTccl);
      myMethod(); 
  }
  finally{ // 还原 
      Thread.currentThread().setContextLoader(classLoader); 
  } 
  ```

  

  - 在 myMethod 中调用 Thread.currentThread().getContextLoader() 做某些事情
  - 如果一个类由类加载器A加载，那么这个类的依赖类也是由相同的类加载器加载（该类之前未被加载过）
  - ContextClassLoader 的目的就是为了破坏类加载委托机制
  - 在SPI接口的代码中，使用线程上下文类加载器就可以成功的加载到SPI的实现类。
  - 当高层提供了统一的接口让底层去实现，同时又要在高层加载（或实例化）底层的类时，就必须通过上下文类加载器来帮助高层的 ClassLoader 找到并加载该类。

- 测试26：线程上下文类加载器的一般使用模式

```java
 package com.gjxaiou.class10;

import java.sql.Driver;
import java.util.Iterator;
import java.util.ServiceLoader;

public class MyTest26{
    public static void main(String[] args){

        // 一旦加入下面此行，将使用ExtClassLoader去加载Driver.class， ExtClassLoader不会去加载classpath，因此无法找到MySql的相关驱动。
        // Thread.getCurrentThread().setContextClassLoader(MyTest26.class.getClassLoader().parent());

        // ServiceLoader服务提供者，加载实现的服务
        ServiceLoader<Driver> loader= ServiceLoader.load(Driver.class);
        Iterator<Driver> iterator=loader.iterator();
        while(iterator.hasNext()){
            Driver driver = iterator.next();
            System.out.println("driver:"+ driver.getClass() + ",loader"+ driver.getClass().getClassLoader());
        }
        System.out.println("当前上下文加载器"
                +Thread.currentThread().getContextClassLoader());
        System.out.println("ServiceLoader的加载器"
                +ServiceLoader.class.getClassLoader());
    }
}
/**
 * driver:class com.mysql.cj.jdbc.Driver,loadersun.misc.Launcher$AppClassLoader@18b4aac2
 * // 因为上下文加载器没有设置，所以默认为 AppClassLoader
 * 当前上下文加载器sun.misc.Launcher$AppClassLoader@18b4aac2
 * // 因为 ServiceLoader 位于 java.util 包中，是 Java 核心库，位于 rt.jar 中，所有使用启动类加载器
 * ServiceLoader的加载器null
 */
```

**阅读 ClassLoader.java 源代码**

其中 JavaDoc 部分如下：

```java
<p> A <i>service</i> is a well-known set of interfaces and (usually
 * abstract) classes.  A <i>service provider</i> is a specific implementation
 * of a service.  The classes in a provider typically implement the interfaces
 * and subclass the classes defined in the service itself.  Service providers
 * can be installed in an implementation of the Java platform in the form of
 * extensions, that is, jar files placed into any of the usual extension
 * directories.  Providers can also be made available by adding them to the
 * application's class path or by some other platform-specific means.
   服务就是一系列接口和类（通常为抽象类）的集合，服务提供者就是服务的一个特定的实现，所以 ServiceLoader 是用于加载服务的具体实现； 服务提供者继承或者实现服务的抽象类或者接口
    <p><a name="format"> A service provider is identified by placing a
 * <i>provider-configuration file</i> in the resource directory
 * <tt>META-INF/services</tt>.</a>  The file's name is the fully-qualified <a
 * href="../lang/ClassLoader.html#name">binary name</a> of the service's type.
 * The file contains a list of fully-qualified binary names of concrete
 * provider classes, one per line.  Space and tab characters surrounding each
 * name, as well as blank lines, are ignored.  The comment character is
 * <tt>'#'</tt> (<tt>'&#92;u0023'</tt>,
 * <font style="font-size:smaller;">NUMBER SIGN</font>); on
 * each line all characters following the first comment character are ignored.
 * The file must be encoded in UTF-8.
    服务提供者放在 META-INF/services/服务的完全限定名字
```

[![image-20230220145855404](img/JVM：3-类加载器深入解析与阶段分解/image-20230220145855404.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/类加载器深入解析与阶段分解.resource/image-20191206210104209.png)

该文件内容为：

[![image-20230220145907665](img/JVM：3-类加载器深入解析与阶段分解/image-20230220145907665.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/类加载器深入解析与阶段分解.resource/image-20191206210126359.png)

- 测试27

```java
//跟踪代码
 public class MyTest27{
    public static void main(String[] args){
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection=DriverManager.getConnection(
  "jdbc:mysql://localhost:3306//mydb","user","password");
    }
 }
```

- jar hell问题以及解决办法 当一个类或者一个资源文件存在多个jar中，就会存在jar hell问题。 可通过以下代码解决问题：

  ```java
  ClassLoader calssLoader=Thread.currnetThread().getContextClassLoader();
  String resource-“java/lang/String.class”;
  Enumeration urls=calssLoader.getResources(resource);
  while(urls.hasMoreElements()){
  – URL url=urls.nextElement();
  – System.out.prinln(url);
  }
  ```

**视频 34- 36 是复习可以看看**



## 6.类装载器

### 6.2 什么是类装载器ClassLoader 

ClassLoader是一个抽象类，ClassLoader的实例将读入Java字节码，将类装载到JVM中。

ClassLoader可以定制，满足不同的字节码流获取方式，ClassLoader负责类装载过程中的加载阶段。



### 6.3 JDK中ClassLoader默认设计模式

ClassLoader的重要方法

```java
public Class<?> loadClass(String name) throws ClassNotFoundException
    //载入并返回一个Class
protected final Class<?> defineClass(bytel b, int off, int len)
    //定义一个类,不公开调用
protected Class<?> findClass(String name) throws ClassNotFoundException
    //loadClass回调该方法,自定义ClassLoader的推荐做法
protected final Class<?> findLoadedClass(String name)
    //寻找已经加载的类
```

**ClassLoader分类**

- BootStrap ClassLoader (启动ClassLoader ) 	

  默认加载 rt.jar，使用 -Xbootclasspath 将这个路径下的jar用该类加载器加载，可以使用Unsafe

- Extension ClassLoader (扩展ClassLoader)

  %JAVA_HOME%/lib/ext/*.jar

- App ClassLoader (应用ClassLoader/系统ClassLoader) 

  Classpath下的加载

- Custom ClassLoader(自定义ClassLoader)

  每个ClassLoader都有一个Parent作为父亲

自底向上去查找，从顶向下的去加载。

**双亲模式问题**

顶层ClassLoader无法加载底层ClassLoader的类

通过Thread.setContextClassLoader()来解决。

上下文加载器，是一种角色，在顶层ClassLoader中，传入底层ClassLoader的实例。

**打破常规模式**

如Tomcat就是从底层先去查，查到了就加载，找不到才会委托父类去加载。

### 6.4 热替换

当一个类class被替换后，系统无需重启，替换的类立即生效，和热加载有异曲同工之处。



### 为什么双亲委托模型无法满足SPI的要求

SPI是 （Service Provider Interface），它和API不同，它只负责提供规范或者接口，具体的操作由其实现类来完成。比如JDBC，Java只负责提供数据库连接的接口，也就是Connection，具体连接内容由数据库厂商提供的驱动来完成，我们使用Java语言连接某个数据库时，必须先加载该数据库驱动，之后才能获取数据库连接。

而加载数据库驱动的时候，就涉及到类加载器的知识，Java的双亲委托机制在此处可能就不太适用了，因为像Connection这些接口，都是定义在rt.jar中，在JVM运行时会通过启动类加载器（BootstrapClassloader）来加载，比如加载Driver的ServiceLoader，位于java.util包下，通过BootstrapClassloader来加载，但是驱动的具体实现，如数据库驱动的jar包或者classs文件，，ServiceLoader在扫描加载驱动的时候，需要依赖具体的驱动类，那么会调用自己的类加载器（BootClassLoader）去尝试加载数据库驱动的jar，但由于其保存在其他位置，不在BootClassLoader的扫描范围内，所以无法加载该类。

我们手动引入的数据库驱动的class文件是在classpath路径下，这部分class文件可以通过系统/应用类加载器（AppClassloader）来加载。所以Java设计了另外的一种方式，在线程中设置ContextClassLoader，上下文类加载器，在加载驱动的过程中，获取到系统类加载器，由系统类加载器来加载。

不同的类加载器加载class，虽然看起来都是加载class文件到内存中，但是不同的类加载器拥有不同的命名空间，子类加载器可以看到父类加载器命名空间中的Class对象，但是父类看不到子类的。看不到则意味着无法引用。（A类依赖B类，那么会用A类的加载器来加载B类），在不同的命名空间的相同类会出现，ClassCastException，com.lwj.A not cast to com.lwj.A，相同类却无法转化。

```java
private static boolean isDriverAllowed(Driver driver, ClassLoader classLoader) {
    boolean result = false;
    if(driver != null) {
        Class<?> aClass = null;
        try {
            aClass =  Class.forName(driver.getClass().getName(), true, classLoader);
        } catch (Exception ex) {
            result = false;
        }
		//判断调用线程的类加载器和加载该驱动的类加载器是否为同一个，不同后边会出现ClassNotFound问题
        result = ( aClass == driver.getClass() ) ? true : false;
    }

    return result;
}
```

### 加载

类加载并不一定在要被使用时才加载，JVM可能会预判某些类可能要被使用，会进行预先加载。在加载过程中如果遇到了class文件缺失或者存在错误，那么在首次使用该类的时候（也就是初始化）才会报错。如果一直不使用该类，类加载器就不会报错。



Class对象是存放在堆区的，不是方法区，这点很多人容易犯错。类的元数据（元数据并不是类的Class对象！Class对象是加载的最终产品，类的方法代码，变量名，方法名，访问权限，返回值等等都是在方法区的）才是存在方法区的。



### 连接

类被加载后，就进入连接阶段，连接就是将已经读入到内存的二进制数据合并到虚拟机的运行环境中。

- 验证
- 准备
- 解析

### 初始化

假如这个类还没有被加载和连接，那就先进行加载和连接。

假如这个类存在直接父类，并且这个父类还没有被初始化，那就先初始化直接父类。这一条仅限于类，不适用于接口。初始化一个类时，并不会先初始化它所实现的接口，在初始化一个接口时，并不会先初始化其父接口。只有当程序首次使用接口的静态变量时，才会导致接口初始化。

假如类中存在初始化语句，那就依次执行这些初始化语句。

#### 初始化时机

必须是调用特定类中定义的静态变量，调用子类中 继承父类的静态变量，则会初始化父类，不会初始化子类。

ClassLoader类的loadClass方法加载一个类时，并不是对类的主动使用，不会初始化。

### 类加载器

类加载器是逻辑上的父子关系，通过组合的方式来实现，而不是类的继承。一种类加载器的不同实例，通过组合的方式都可以构成父子关系。

- 根/引导/启动 类加载器  BootstarpClassLoader

  从系统属性sun.boot.class.path指定的目录中加载

  jre/lib/rt.jar或者-Xbootclasspath指定jar包

- 扩展 类加载器  ExtensionClassLoader

  从系统属性java.ext.dirs指定的目录中加载

  jre/ext/*.jar或者-Djava.ext.dirs指定目录下的jar包

- 系统/应用 类加载器 AppClassLoader

  从系统属性java.class.path指定的目录中加载

  环境变量classpath，它是用户自定义类加载器的默认父加载器。

使用系统属性 java.system.class.loader查看和设置 自定义的系统类加载器，之前的AppClassLoader作为新系统类加载器的父加载器。

#### 获取类加载器的途径

- clazz.getClassLoader（）获取当前类的类加载器
- Thread.currentThread.getContextClassLoader（）获取当前线程上下文类加载器  
- ClassLoader.getSystemClassLoader（）获取系统类加载器
- DriverManager.getCallerClassLoader（） 获取到调用者的类加载器

启动类加载器是JVM启动时通过C++代码创建的，而扩展类加载器和系统类加载器都是由启动类加载器来加载的。在双亲委托机制中，各个类加载器按照父子关系形成树形结构，除了根类加载器没有父加载器外，其余类加载器有且只有一个父加载器。



每个类都会使用自己的类加载器（即加载自身的类加载器）来加载依赖的类。

如果ClassX引用了ClassY，那么ClassX的类加载器就会去加载ClassY，前提是ClassY尚未被加载。

#### 双亲委托机制的优点

父亲委托机制的优点是能够提高软件系统的安全性,因为在此机制下,用户自定义的类加载器不可能加载应该由父加载器加载的可靠类,从而防止不可靠甚至恶意的代码代替由父加载器加载的可靠代码。例如, javalang.Object类总是由根类加载器加载,其他任何用户自定义的类加载器都不可能加载含有恶意代码的java lang.Object



**SPI（Service Provider Interface）**

父ClassLoader可以使用当前线程Thread.currentThread().getContextClassLoader()所指定的ClassLoader加载的类。这就改变了父ClassLoader不能使用子ClassLoader或者是其他没有直接父子关系的ClassLoader加载的类，即改变了双亲委托模型。

在双亲委托模型下，类加载是由下至上的，即下层的类加载器会委托上层的类加载器进行加载，但是对于SPI来说，有些接口是Java核心库所提供的，而Java核心库是由启动类加载器来加载的，而这些接口的实现却来自于不同的jar（厂商提供），Java的启动类加载器默认是不会加载器他来源的jar包，这样传统的双亲委托模型就无法满足SPI的要求。而通过给当前线程设置上下文类加载器，就可以由设置的上下文类加载器来实现对于接口实现类的加载。

*高层提供了统一的接口让低层去实现，同时又要在高层加载（或实例化）低层的类时，就必须要通过线程上下文类加载器来帮助高层的ClassLoader找到并加载该类。*

上下文类加载器其实是破坏了双亲委托机制的一种设计。如果没有通过setContextClassLoader进行设置的话，线程将继承其父线程的上下文类加载器，Java应用运行时的初始线程的上下文类加载器是系统类加载器，在线程中运行的代码可以通过该类加载器来加载类与资源。

#### 命名空间

每个类加载器都有自己的命名空间,命名空间由该加载器及所有父加载器所加载的类组成在同一个命名空间中,不会出现类的完整名字(包括类的包名)相同的两个类在不同的命名空间中,有可能会出现类的完整名字(包括类的包名)相同的两个类

同一个命名空间内的类是相互可见的。子加载器的命名空间包含所有父加载器的命餐空间,因此由子加载器加载的类能看见父加载器加载的类。例如系统类加载器加载的类能看见根类加载器加载的类。由父加载器加载的类不能看见子加载器加载的类如果两个加载器之间没有直接或间接的父子关系,那么它们各自加载的类相互补可见

#### 创建用户自定义的类加载器

要创建用户自己的类加载器,只需要扩展 java.lang.ClassLoader类,然后覆盖它的 findClass(String name)方法即可,该方法根据参数指定的类的名字,返回对应的 Class对象的引用。

由用户自定义的类加载器可以被卸载，JVM自带的加载器所加载的类不会被卸载。