[toc]

# 1. 运行时数据区域

​		Java虚拟机在执行Java程序的过程中，会把它所管理的内存区域划分为若干个不同的数据区域。这些区域的用途由Java虚拟机规范来定义，不同厂商的虚拟机甚至同一厂商但不同版本的虚拟机在实现上都会存在区别。因此，本文及后续文章在没有特殊说明的情况下，都是针对Hotspot虚拟机来学习。

![](.\img\运行时数据区域.png)

​		淡蓝色的框内就是JDK6的运行时数据区域，包括程序计数器、Java虚拟机栈、本地方法栈、堆、方法区。接下来本小节会详细介绍每个区域的用途。同时也会对JDK8中调整的区域进行说明。



## 1.1 程序计数器

 		程序计数器，在上图中看着挺大的一块，但在实际的内存划分中，只是一块很小的内存空间。它可以看作当前线程的所执行的字节码的行号指示器，字节码解释器会根据这个计数器的值来选取下一条要被执行的指令，程序代码默认是从上往下顺序执行的，但可以通过改变计数器的值来实现 分支、循环、跳转、异常处理、线程恢复等基础功能。

​		它是每个线程私有的空间，**每个线程都有一个独立的程序计数器**，在线程创建时创建，在线程销毁时销毁，各个线程之间计数器互不影响，独立存储。当线程重新获取到CPU的执行时间时，线程会从挂起时保存的行号继续向下执行。

​		如果线程执行的是一个 Java 方法，则计数器记录的是正在执行的虚拟机字节码指令的地址，如果正在执行的是本地方法，则计数器值为空（Undefined）。

程序计数器也是唯一一个不会抛出OutOfMemoryError（OOM）的内存区域。

> 内存溢出（Out Of Memory）：程序运行无法申请到足够的内存空间进行分配。
>
> 内存泄漏（Memory Leak）：程序将内存分给一些临时使用的对象，使用结束后，仍有引用指向该对象，导致不会被GC所回收。



## 1.2 Java虚拟机栈

​		与程序计数器一样，Java 虚拟机栈也是线程私有的，它的生命周期和线程相同，描述的是 Java 方法执行的内存模型，它和数据结构中的栈一样，先进后出。其中的存储的元素是栈帧（Stack Frame），栈帧由局部变量表、操作数栈、动态链接、方法出口等构成，每个方法从开始调用到执行完成的过程，其实就是栈帧在入栈到出栈的过程。

​		**局部变量表**

​		通常所说的栈，一般是指虚拟机栈的这部分，局部变量表中主要存放了编译器可知的各种**基本数据类型**（boolean、byte、char、short、int、float、long、double）、**引用类型**（referenece类型，它不等同于对象本身，只是一个指向对象的引用）和 **returnAddress类型**（指向了一个字节码指令的地址）。

​		在局部变量表中，32位以内的类型只占用1个局部变量空间（slot）（包括returnAddress类型，并且byte、short、char、boolean在存储前都会被转为int类型，0表示false，非0表示true），64位类型（long和double类型）的数据会占用2个slot，为了更节省栈帧空间，Slot可以进行复用。可以简单理解为，当某个变量在后续的方法体中没有被使用，那么这个变量对应的Slot就可以分配给其他变量。

​		局部变量表不止存放了方法中的局部变量，还有传入的参数。如果执行的是实例方法（非静态方法），那么局部变量表中index为0的Slot默认存放被调方法的实例对象，因此在方法中可以通过this关键字来获取到这个隐藏的参数。

​		示例如下：

```java
	int test1(String a) {
        int a = 0;
        {
            int b = 0;
            b=b+1;
        }
        int c =1;
    }
```

​		上边代码对应的局部变量表（LocalVariableTable）：

![](.\img\局部变量表1.jpg)

​		

​		编译程序代码的时候，栈帧中需要多大的局部变量表，多深的操作数栈都已经完全确定了。因此一个栈帧需要分配多少内存，不会受到程序运行期变量数据的影响。



​		**动态连接**
​		每个栈帧都包含一个指向运行时常量池中该栈帧所属方法的引用，持有这个引用是为了支付方法调用过程中的动态连接（Dynamic Linking）。

​		在类加载阶段中的解析阶段会将符号引用转为直接引用，这种转化也称为静态解析。另外的一部分将在每一次运行时期转化为直接引用。这部分称为动态连接。



​		**操作数栈**

java没有寄存器，所有参数传递使用操作数栈

![](.\img\1577595291(1).jpg)

栈上分配

- -server -Xms10m -Xmx10m  -XX:+DoEscapeAnalysis  -XX:+PrintGC
- 小对象（一般几十个bytes），在没有逃逸的情况下，可以选择直接分配在栈上
- 直接分配在栈上，可以自动回收，减轻GC压力
- 大对象或者逃逸对象无法在栈上分配

![](.\img\1577595783(1).jpg)

**方法出口：**
当一个方法开始执行后，只有2种方式可以退出这个方法 ：

方法返回指令 ： 执行引擎遇到一个方法返回的字节码指令，这时候有可能会有返回值传递给上层的方法调用者，这种退出方式称为正常完成出口。

异常退出 ： 在方法执行过程中遇到了异常，并且没有处理这个异常，就会导致方法退出。

无论采用任何退出方式，在方法退出之后，都需要返回到方法被调用的位置，程序才能继续执行，方法返回时可能需要在栈帧中保存一些信息。

一般来说，方法正常退出时，调用者的PC计数器的值可以作为返回地址，栈帧中会保存这个计数器值。

而方法异常退出时，返回地址是要通过异常处理器表来确定的，栈帧中一般不会保存这部分信息。

Java 方法有两种返回方式：

- return 语句。
- 抛出异常。

不管哪种返回方式都会导致栈帧被弹出。



在Java虚拟机规范中，对该区域规定了两种异常状况：

- **StackOverFlowError：** 如果线程请求栈的深度超过当前 Java 虚拟机栈的最大深度的时候，就抛出 StackOverFlowError 错误。
- **OutOfMemoryError：** 若 Java 虚拟机栈的内存大小允许动态扩展，且当线程请求栈时内存用完了，无法再动态扩展了，此时抛出 OutOfMemoryError 错误。



## 1.3 本地方法栈

本地方法栈（Native Method Stack）：方法上加上 Native 关键字，表示该方法是有 C/C++ 实现，不是 Java 实现的，即主要用于执行本地方法；同样会抛出 StackOverflowError 和 OutOfMemoryError 异常。

- 虚拟机栈和本地方法栈区别：前者是为虚拟机执行 Java 方法（即字节码）服务，后者是为虚拟机使用到的 Native 方法服务。
- 虚拟机规范中对该部分没有强制规范， **Hotspot 虚拟机直接将本地方法栈和虚拟机栈合二为一**。
- 本地方法栈：本地方法栈保存的是native方法的调用信息，当一个JVM创建的线程调用native方法后，JVM不在Java虚拟机栈中创建栈帧，JVM只是简单的动态链接并直接调用native方法。

6) 本地方法栈:本地方法栈和java栈非常类似,最大的不同在于java栈用于方法的调用,而本地方法栈则用于本地方法的调用,作为对java虚拟机的重要扩展, 
       java虚拟机允许java直接调用本地方法(通常使用C编写)

Hotspot虚拟机将本地方法栈和虚拟机栈合二为一，可以使用 **-Xss** 来设置栈的大小，默认为1m。

## 1.4 Java堆 

堆heap 
        1.堆用于存储创建好的对象和数组（数组也是对象）
        2.JVM只有一个堆，被所有线程共享
        3.堆是一个不连续的内存空间，分配灵活，速度慢

- 应用系统对象都保存在Java堆中
- 所有线程共享Java堆
- 对于分代GC来说，堆也是分代的
- GC主要的工作区间



堆（Heap）：在虚拟机启动时候创建，对所有线程共享，存放绝大部分的对象实例（部分会使用栈上分配，标量替换 技术存放在其他位置），Java 中不能直接使用对象，只能通过引用方式获取该对象然后使用它，引用作为一个变量是在栈中。

- 线程共享的 Java 堆中可以划分出多个线程私有的分配缓冲区（Thread Local Allocation Buffer,TLAB）

- Java 堆可以处于物理上不连续的内存空间中，只要逻辑上连续即可，一般都是可以扩展的；

- 堆：Java堆是存放new关键字产生对象的地方，这个区域也是JVM垃圾回收的主要工作区域。

  |    JVM参数     |      含义      |
  | :------------: | :------------: |
  |      -Xms      |   堆的最小值   |
  |      -Xmx      |   堆的最大值   |
  |      -Xmn      |  新生代的大小  |
  |  -XX:NewSize   | 新生代的最小值 |
  | -XX:MaxNewSize | 新生代的最大值 |

  2) java堆: java堆在虚拟机启动的时候建立,它是java程序最主要的内存工作区域。几乎所有的java对象实例都存放在java堆中。
       堆空间是所有线程共享的,这是一块与java应用密切相关的内存空间。

## 1.5 方法区

方法区：对所有线程共享，存储元信息，包括已被虚拟机加载的类信息、常量、静态变量、即使编译器编译后的代码、类中（Class ）固有的信息；在 Hotspot 中 **永久代（Permanent Generation）从 JDK 1.8 中已经废弃 **，并且永久代不等于方法区，Hotspot 虚拟机（其他虚拟机不存在永久代概念）使用永久代来实现方法区，即将 GC 分代收集器拓展到方法区，使得垃圾收集器可以像管理 Java 堆一样管理该部分内存，省去专门为方法区编写内存管理代码的工作。

- 运行时常量池（Runtime Constant Pool）：方法区的一部分，Class 文件中的常量池会存储编译期生成的字面值和符号引用，该部分内容在类加载后进入方法区的运行时常量池中存放，**运行时常量池相比 Class 文件常量池而言具有动态性**，因为 Java 并不要求常量一定只有编译期才能产生即并非只有预置在 Class 文件中常量池部分的内容才可以进入方法区运行时常量池，**运行期间也可以将新的常量池放入池中**，例如：String 类的 intern（） 方法；

- 方法区：用于存储被虚拟机加载的class文件所生成的字节码对象，其中包含了和类相关的重要信息，如常量池，静态变量等数据。

  JDK7及以前

  |     JVM参数     |      含义      |
  | :-------------: | :------------: |
  |  -XX:PermSize   | 永久代的最小值 |
  | -XX:MaxPermSize | 永久代的最大值 |

  JDK8以后

  |       JVM参数       |      含义      |
  | :-----------------: | :------------: |
  |  XX:MetaspaceSize   | 元空间的最小值 |
  | XX:MaxMetaspaceSize | 元空间的最大值 |

  JDK6，7，8，中永久代、方法区与元空间？

  方法区，是JVM规范中用来存放已被虚拟机加载的类信息、常量、静态变量、即时编译后的代码等数据，它和堆一样可以被多个线程所共享，但堆中主要存放的是对象，对象可以通过GC频繁回收，而方法区中，主要是针对常量池的回收以及类信息的卸载，该区的回收效果没有堆的回收效果明显，现在很多框架会在运行期动态生成class文件并加载到方法区，例如Spring的AOP会使用CGLib动态生成代理类，或者JSP第一次运行的时候需要编译为Java类，这些动态生成的类会被类加载器加载到方法区，如果这些类只加载不卸载就会发生OOM，因此考虑到这一点，在JDK6中，Hotspot虚拟机使用永久代来实现方法区，将GC的分代算法扩展到方法区，这样GC 就可以像管理Java堆一样管理这部分内存，但实际的效果并不理想（通过该JVM参数 -XX:MaxPermSize 可以设置永久代的上限）因为判定一个类需要被卸载的条件比较苛刻。因此在JDK7的Hotspot中，已经把原本放在永久代的字符串常量池移出，JDK8中，使用Metaspace（元空间）来取代方法区，而元空间已经不属于虚拟机运行时数据区，也就意味着元空间的大小不再受限于JVM，而是由进程的可用内存上限来决定，32位系统的进程可用上限为4G。

- 运行时常量池：是方法区的一部分，class文件中的常量池是用来存放编译期生成的字面量和符号引用，这部分的内容将在类加载进入方法区的运行时常量池存放。运行时常量池除了存放class文件中的常量池，在运行期间，也可以将新的常量放入池中，例如，String类的intern（）方法。

- #### **方法区**

  - 保存装载的类信息
    - 类型的常量池
    - 字段，方法信息
    - 方法字节码
  - 通常和永久区（Perm）关联在一起

  方法区
        1.JVM只有一个方法区，被所有线程共享
        2.方法区实际也是堆，只是用于存储类，常量相关的信息
        3.用于存放程序中永远不变或唯一的内容，（类信息[Class对象]，静态变量，字符串常量等）



  1) 类加载子系统与方法区:类加载子系统负责从文件系统或者网络中加载Class信息,加载的类信息存放于一块称为方法区的内存空间。
       除了类的信息外,方法区中可能还会存放运行时常量池信息,包括字符串字面量和数字常量(这部分常量信息是class文件中常量池部分的内存映射)

## 1.6 直接内存



## 1.7 栈、堆、方法区的关系

 ![](.\img\1577596891(1).jpg)

 






直接内存：不是虚拟机运行时数据区的一部分，也不是 Java 虚拟机规范中定义的内存区域，即不是 JVM 管理的内存，与 Java NIO（New Input/Output） 密切相关，通过使用 Native 函数库直接分配堆外内存，由操作系统进行管理， JVM 通过存储在堆上的 DirectByteBuffer 对象作为该内存的引用来操作直接内存；

3) 直接内存: java的NIO库允许java程序使用直接内存。直接内存是在java堆外的、直接向系统申请的内存空间。通常访问直接内存的速度会优于java堆。
因此出于性能的考虑,读写频繁的场合可能会考虑使用直接内存。由于直接内存在java堆外,因此它的大小不会直接受限于Xmx指定的最大堆大小,
但是系统内存是有限的, java堆和直接内存的总和依受限于操作系统能给出的最大内存

直接内存：不是虚拟机运行时数据区的一部分，也不是java虚拟机规范中定义的内存区域：如果使用了NIO，这块区域会被频繁使用，通过使用Java堆中的DirectByteBuffer对象作为这块内存的引用进行操作，这块内存默认与Java堆的最大值一样，防止出现OOM可以设置 **-XX:MaxDirectMemorySize** 来设置。


# 二、Java 对象创建过程

注：这里对象指普通 Java 对象，不包括数组和 Class 对象

- 创建对象的方式

  - 使用 new 关键字
  - 使用 clone
  - 通过反射
  - 通过反序列化

- new关键字创建对象的3个步骤:

  - 在堆内存中创建出对象的实例

    当虚拟机遇到一条 new 指令时候，首先虚拟机会检查该指令的参数能否在常量池中定位到一个类的符号引用，然后检查这个符号引用所代表的类是不是被正确的加载、连接、初始化，如果没有首先进行类加载过程。

    当上述过程完成之后，虚拟机开始为新生对象分配内存（实际分配的内存空间在对象加载完成之后就确定了），为对象分配内存的任务相当于将一块确定大小的内存从 Java 堆中划分出来；在堆中为对象分配内存分为两种情况（堆内存空间分为已经被占用和未被占用两部分），【情况一（指针碰撞）：如果占用和未占用分别是两块连续空间，中间存放一个指针作为分界点的指示器】如果在未被占用的空间中为对象分配了一段的内存空间，则原来指向未被占用空间位置的指针发生偏移，指向下一个未被占用的空间位置（指针挪动的距离等价于分配的内存），这样对象就创建完成了；【情况二（空闲列表）：两块空间不连续】虚拟机会记录已被使用和未被使用的地址列表，以及未被使用的内存地址大小，如果需要为对象分配内存空间，则需要在未被使用的地址列表中选择一块可以容纳该对象的内存空间放置对象，然后在列表中将记录修改；

    - 指针碰撞（Bump the Pointer）(前提是堆中的空间通过一个指针进行分割，一侧是已经被占用的空间，另一侧是未被占用的空间)
    - 空闲列表(Free List)(前提是堆内存空间中已被使用与未被使用的空间是交织在一起的，这时，虚拟机就需要通过一个列表来记录哪些空间是可以使用的，哪些空间是已被使用的，接下来找出可以容纳下新创建对象的且未被使用的空间，在此空间存放该对象，同时还要修改列表上的记录)
    - 为什么堆不确定是否平整：取决于堆所采用的垃圾收集器是否带有压缩整理功能；
    - 针对并发情况下频繁创建对象可能带来的线程不安全问题（分配了内存但是指针没来得及修改，其他对象同时使用了原来的指针进行分配内存）：方法一：对分配内存空间的动作进行同步处理（虚拟机中采用 CAS 加上失败重试保证更新操作的原子性）；方法二：将内存分配的动作按照线程划分在不同的空间中进行，即每个线程在 Java 堆中预先分配一小块内存称为本地线程分配缓存（TLAB），哪个线程要分配内存时就在哪个线程的 TLAB 上进行分配，只有 TLAB 用完并分配新的 TLAB 时候才需要进行同步锁定，可以使用 `-XX:+/-UseTLAB`参数设定。

  - 为对象的实例成员变量赋初值（对于静态变量在加载阶段就进行了赋初值），因为虚拟机在内存分配完成之后就会将分配到的内存空间都初始化为零值（不包括对象头）（若使用 TLAB，在分配 TLAB 时就执行该步骤），保证对象的实例字段可以在不赋初值情况下就可以使用。

  - 虚拟机对对象进行必要的设置，如该对象为哪个类的实例、怎么找到类的元数据，对象的 Hash 码等，这些信息都存放在**对象的对象头**中，可以进行不同设置。至此对于虚拟机来说一个对象已经产生了，但是对于 Java 程序而言对象创建才刚刚开始，还需要执行 ``方法，同时对字段进行赋值。

  - 将对象的引用返回

- 对象在内存中的布局（即对象包含的信息）

  - 对象头（Header）：例如对象的 Hash 码以及分代信息
    - 一部分称为（Mark Word）用于存储自身的运行时的数据，如哈希码、GC 分代年龄、锁状态标志、线程持有的锁、偏向线程 ID，根据虚拟机位数不同占 32 / 64 bit，该部分数据结构不固定，会根据对象的状态复用自己的存储空间。
    - 另一部分为：类型指针，即对象指向它的类元数据的指针（虚拟机通过该指针来确定这个对象是哪个类的实例），但是不是所有的虚拟机实现都必须在对象数据上保留类型指针；

- 另一部分：**只有数组对象有**，用于记录数组长度的数据，因为虚拟机可以通过普通 Java 对象的元数据信息确定 Java 对象的大小，但是从数据的元数据中却无法确定数组的大小。

  - 实例数据（Instance Data）：即对象真正存储的有效信息，也是在程序代码中所定义的各种类型的字段内容（无论是从父类继承或者子类中定义的）。这里信息存储的顺序受虚拟机分配策略参数和字段在 Java 源码中定义顺序的影响；Hotspot 虚拟机中默认的分配策略为：longs/doubles、ints、shorts/chars、bytes/booleans，oops(Ordinary Object Pointers)，其次父类中定义的变量在子类之前；
  - 对齐填充（Padding）（非必须）：起到占位符作用，因为 Hotspot 中自动内存管理系统要求对象起始地址必须是 8 字节的整倍数（即对象的大小必须是 8 字节的整数倍）。

- 对象的访问定位（引用访问对象的方式）

Java 程序需要通过栈上的 reference 数据来操作堆上的具体对象；

- 使用句柄的方式

  - 首先在堆中划分出一块内存来作为句柄池，reference 中存储的是对象的句柄地址，句柄分为两部分，一部分为该对象实例真正的指针，执行真正的对象实例数据信息，第二部分为类型数据各自的具体地址信息，元数据信息放置在方法区。
  - 优势：reference 中存储的是稳定的句柄地址，当对象移动（如垃圾回收时候）时只会改变句柄中的实例数据指针，而 reference 本身不需要修改。

  [![通过句柄方式访问对象](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/Java%E5%86%85%E5%AD%98%E7%BB%93%E6%9E%84.resource/955ac97ce62d2deb57356f1aee43f33a.jpeg)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/Java内存结构.resource/955ac97ce62d2deb57356f1aee43f33a.jpeg)

  - 使用直接指针的方式（Hotspot 使用方式）

    - Java 堆对象中放置访问类型数据的相关信息，reference 中存储的是对象地址。
    - 优势：速度更快，节省一次指针定位的时间开销（并且对象的访问在 Java 中非常频繁）。

    [![通过直接指针方式访问对象](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/Java%E5%86%85%E5%AD%98%E7%BB%93%E6%9E%84.resource/ee322420543cd38485ba6e1ae665ac82.jpeg)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/Java内存结构.resource/ee322420543cd38485ba6e1ae665ac82.jpeg)

## 一、虚拟机堆内存溢出测试

因为堆用于存储对象实例，所以通过不断的创建对象实例，并且保证 GC Roots 到对象之间有科大路径来避免垃圾回收机制清除这些对象。

```
  //-Xms5m -Xmx5m -XX:+HeapDumpOnOutOfMemoryError 设置jvm对空间最小和最大值（如果两值相同则堆不会自动扩展）以及遇到内存溢出异常时 Dump 出当前的内存堆转储快照，便于以后分析。
package com.gjxaiou.memory;

import java.util.ArrayList;
import java.util.List;

public class MyTest1 {
    public static void main(String[] args) {
      
        //打开jvisualvm 装在磁盘上的转存文件
        List<MyTest1> list = new ArrayList<>();
        while (true) {
            list.add(new MyTest1());
        }
    }
}
```

报错结果：

```
java.lang.OutOfMemoryError: Java heap space
Dumping heap to java_pid20108.hprof ...
Heap dump file created [2076951 bytes in 0.060 secs]
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
	at com.gjxaiou.memory.MyTest1.main(MyTest1.java:12)
```

对 Dump 出来的堆转存储快照进行分析，判断内存中对象是否是必要的，即首先确定是内存泄漏（Memory Leak）还是内存溢出（Memory Overflow）

- 如果是内存泄漏，查看泄漏对象到 GC Roots 的引用链，就可以找到泄漏对象是通过怎样的路径与 GC Roots 相关联并且导致垃圾回收器无法回收他们，从而定位泄漏代码的位置；
- 反之则表示内存中的对象确实必须保持存活，则应当检查虚拟机堆参数（-Xmx 和 -Xms），是否可以增大；另一方面检查代码上是否存在某些对象生命周期过长，保持状态时间过长的情况，尝试减少程序运行期的内存消耗

### （一）JVisualVM 使用

直接在 cmd 控制台中输入 jvisualvm 即可开启

[![image-20191211162736253](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/Java%E5%86%85%E5%AD%98%E7%BB%93%E6%9E%84.resource/image-20191211162736253.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/Java内存结构.resource/image-20191211162736253.png)

[![image-20191211163216509](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/Java%E5%86%85%E5%AD%98%E7%BB%93%E6%9E%84.resource/image-20191211163216509.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/Java内存结构.resource/image-20191211163216509.png)

如果在上面的代码中：` list.add(new MyTest1());` 调用 `System.gc()`;然后再次执行该程序，这时会在 JVisualVM 的左边本地进程中多一个该程序的进程，点击打开之后

首先可以看到概述以及 JVM 参数

[![image-20191211164809109](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/Java%E5%86%85%E5%AD%98%E7%BB%93%E6%9E%84.resource/image-20191211164809109.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/Java内存结构.resource/image-20191211164809109.png)

然后可以在监视中查看，其他线程和抽样器均可以可视化的查看程序运行信息；

[![image-20191211164902563](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/Java%E5%86%85%E5%AD%98%E7%BB%93%E6%9E%84.resource/image-20191211164902563.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/Java内存结构.resource/image-20191211164902563.png)

# 二、虚拟机栈内存溢出测试

Hotspot 虚拟机不区分虚拟机栈和本地方法栈，因此通过（-Xoss）设置本地方法栈大小是无效的，栈容量只能通过 `-Xss` 参数设置

- 如果线程请求的栈深度大于虚拟机所允许的最大深度，抛出 `StackOverflowError`异常；
- 如果虚拟机在拓展栈时无法申请到足够的内存空间，则抛出 `OutOfMemoryError` 异常；
- 以上两种异常会互相重叠，本质是对同一件事情的两种描述，因为栈空间无法继续分配的时候，可能是内存太小，也可能为已使用的栈空间过大。

在下面**单线程**的情况下，无论是使用 `-Xss` 参数减少栈内存容量或者是定义了大量的本地变量从而增加此方法帧中本地变量表的长度，**只能**抛出 `StackOverflowError`，出异常的时候输出堆栈深度相应减小；

```
package com.gjxaiou.memory;

/**
 * 虚拟机栈溢出测试(使用递归)
 * @Author GJXAIOU
 * @Date 2019/12/11 16:53
 */

public class MyTest2 {
    // 查看一共递归了多少层
    private int length;
    public int getLength() {
        return length;
    }

    public void test() throws InterruptedException {
        length++;
        Thread.sleep(300);
        test();
    }

    public static void main(String[] args) {
        //测试调整虚拟机栈内存大小为：  -Xss160k，此处除了可以使用JVisuale监控程序运行状况外还可以使用jconsole
        MyTest2 myTest2 = new MyTest2();
        try {
            myTest2.test();
            // 注意：catch 捕捉的是 Throwable，不是 Exception，因为 STackOverflow 和 OutOfMemoryError 都不是 Exception 的子类
        } catch (Throwable e) {
            //打印最终的最大栈深度为：2581
            System.out.println(myTest2.getLength());
            e.printStackTrace();
        }
    }
}
```

程序报错：

```
java.lang.StackOverflowError
	at com.gjxaiou.memory.MyTest2.test(MyTest2.java:18)
	at com.gjxaiou.memory.MyTest2.test(MyTest2.java:19)
	at com.gjxaiou.memory.MyTest2.test(MyTest2.java:19)
	at com.gjxaiou.memory.MyTest2.test(MyTest2.java:19)
	at com.gjxaiou.memory.MyTest2.test(MyTest2.java:19)
	at com.gjxaiou.memory.MyTest2.test(MyTest2.java:19)
    ......
    
```

程序运行时候同时打开 JvisualVM ，在 线程 选项右上角有一个 线程 Dump，可以查看所有线程的状态，这里主要看 Main 线程，可以由下图中看出该线程一直在调用 19行的 test() 方法，然后最后 返回了 26 行的调用方法，其他的监视、线程等等也可以查看；

[![image-20191211171420096](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/Java%E5%86%85%E5%AD%98%E7%BB%93%E6%9E%84.resource/image-20191211171420096.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/Java内存结构.resource/image-20191211171420096.png)

**测试多线程情况**

首先操作系统对于分配给每个进程的内存是有限制的，为 总内存 - 最大堆容量 - 最大方法区容量（程序计数器忽略），剩余的内存由虚拟机和本地方法栈进行瓜分，如果每个线程分配的栈容量越大则可以建立的线程数量越少。

测试：创建线程导致内存溢出异常 【因为 Windows 的虚拟机中，Java 线程是映射到操作系统的内核线程上的，所以以下代码运行很危险，慎用】

```
package chapter2;

public class Code2_1 {
    public void dontShop() {
        while (true) {

        }
    }

    public void stackLeakByThread() {
        while (true) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    dontShop();
                }
            });
            thread.start();
        }
    }

    public static void main(String[] args) {
        Code2_1 oom = new Code2_1();
        oom.stackLeakByThread();
    }
}
```

### （一）JConsole 使用

同样在控制台中使用 `jconsole`命名来启动（提前启动项目），然后本地连接到该项目即可监控程序，**特色**：可以在线程选项框最下面检查程序是否存在死锁；

```
package com.gjxaiou.memory;

/**
 * @Author GJXAIOU
 * @Date 2019/12/11 18:03
 */
public class MyTest3 {
    public static void main(String[] args) {
        // 构造两个线程
        // 步骤一：Thread-A 线程启动，执行 A.method（）方法，然后就会拿到类 A 对应的 Class 对象的锁，同时执行方法，睡眠，当执行到 B.method() 方法时候，发现该方法也是 synchronized 的，所以会尝试获取类 B 对应的 Class 对象对应的锁；
        new Thread(() -> A.method(), "Thread-A").start();
        //步骤二：同时 Thread-B 线程启动，同上步骤就会形成死锁
        new Thread(() -> B.method(), "Thread-B").start();
    }
}

class A{
    // 线程进入到 synchronized 修饰的方法后，并且该方法是由 static 修饰的，则持有的不是当前类（Class A）对应的锁，而是当前类所对应的 Class
    // 对象的锁，所以不管该类有多少个实例或者对象，持有的都是一把锁
    public static synchronized  void method(){
        System.out.println("method from A");
        try {
            Thread.sleep(5000);
            B.method();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class B{
    public static synchronized void method(){
        System.out.println("method from B");
        try {
            Thread.sleep(5000);
            A.method();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

程序对应的监测结果为：首先通过线程栏正下方的 “检测死锁” 之后结果如下：

状态可以看出：`java.lang.Class`上的 Blocked，拥有者是 Thread-B，说明线程 Thread-B 已经持有了 `java.lang.Class@77552c4c` 这个对象的锁，所以 Thread-A 在这个对象上处于阻塞状态；因为调用的是 `B.method()`所以等待的是 B 类对应的 Class 对象的锁。

[![image-20191211182158268](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/Java%E5%86%85%E5%AD%98%E7%BB%93%E6%9E%84.resource/image-20191211182158268.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/Java内存结构.resource/image-20191211182158268.png)

同样在 JVisualVM 中会自动提示检测到死锁，并且按照提示在线程选项中生成一个线程 Dump，然后查看上面的两个线程，发现他们分别已经锁定了自己的 Class 对象，想锁定对方的 Class 对象；

[![image-20191211183809571](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/Java%E5%86%85%E5%AD%98%E7%BB%93%E6%9E%84.resource/image-20191211183809571.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/Java内存结构.resource/image-20191211183809571.png)

## 三、方法区元空间溢出测试

因为方法区用于存放 Class 的相关信息，如类名、访问修饰符、常量池、字段描述、方法描述等，因此只有不断产生类来填满方法区来制造溢出异常。

因为从 1.8 开始废除永久代，使用元空间，因为元空间采用的是操作系统本地的内存，初始内存大小为 21 M，并且如果不断占用达到空间最大内存大小则元空间虚拟机会进行垃圾回收，如果回收还是不够就会进行内存扩展，最大可以扩展到物理内存最大值；

首先需要显式的设定初始元空间大小，同时因为元空间中存放一个类的 Class 的元信息（并不存放最占空间的对象实例）， 因此需要不断将 Class 信息不断的增加到元空间中，例如在 Spring （jsp 会动态转为 Servlet，CGlib 等等同理）中会在运行期动态的生成类（就是该类在编译时候是不存在的，在运行期动态创建），这些动态创建类的元信息就要放在元空间中，因此需要不断的动态创建类。

因为一个类如果要被垃圾收集器回收的判定条件是比较苛刻的，因此需要注意大量产生 Class 的应用中，例如： CGLib 字节码增强和动态语言之外、大量 JSP 或者动态产生 JSP 的应用（因为 JSP 第一次运行的时候需要编译Wie Java 类）、基于 OSGi 的应用（即使是同一个类文件，被不同的类加载器加载也视为不同的类）。

```
package com.gjxaiou.memory;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
/**
 * @Author GJXAIOU
 * @Date 2019/12/11 19:00
 */

/**
 * 元空间内存溢出测试(使用 cglib,需要导入对应 jar 包和 asm.jar)
 * 设置元空间最大大小（不让其扩容）：-XX:MaxMetaspaceSize=200m
 * 关于元空间参考：https://www.infoq.cn/article/java-permgen-Removed
 */
public class MyTest4 {
    public static void main(String[] args) {
        //使用动态代理动态生成类（不是实例）
        while (true) {
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(MyTest4.class);
            enhancer.setUseCache(false);
            enhancer.setCallback((MethodInterceptor) (obj, method, ags, proxy) -> proxy.invokeSuper(obj, ags));
            System.out.println("Hello World");
            // 在运行期不断创建 MyTest 类的子类
            enhancer.create();
        }
    }
}
/** output:
 * Caused by: java.lang.OutOfMemoryError: Metaspace
 */
```

从 Jconsole 中可以看出，只有类是不断增加的

[![image-20191211193600512](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/Java%E5%86%85%E5%AD%98%E7%BB%93%E6%9E%84.resource/image-20191211193600512.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/Java内存结构.resource/image-20191211193600512.png)

使用 JVisualVM 可以查看元空间增长情况

[![image-20191211193944968](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/Java%E5%86%85%E5%AD%98%E7%BB%93%E6%9E%84.resource/image-20191211193944968.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/Java内存结构.resource/image-20191211193944968.png)

### 四、JVM命令使用

查看当成程序进程号： ps -ef | grep java（获取所有包含 java 的进程及其 id）**建议使用**：`jsp -l`

```
package com.gjxaiou.memory;

/**
 * @Author GJXAIOU
 * @Date 2019/12/11 20:20
 */
public class MyTest5 {
    public static void main(String[] args) {
        while (true) {
            System.out.println("hello world");
        }
    }
}
```

- 使用 `jmap -clstats` + pid 结果如下：

[![image-20191211205050818](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/Java%E5%86%85%E5%AD%98%E7%BB%93%E6%9E%84.resource/image-20191211205050818.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/Java内存结构.resource/image-20191211205050818.png)

```
C:\Users\gjx16>jmap -clstats 17992
Attaching to process ID 17992, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.221-b11
finding class loader instances ..done.
computing per loader stat ..done.
please wait.. computing liveness.liveness analysis may be inaccurate ...
class_loader    classes bytes   parent_loader   alive?  type

<bootstrap>     606     1134861   null          live    <internal>
0x00000006c24ba258      0       0       0x00000006c2404b38      dead    java/util/ResourceBundle$RBClassLoader@0x00000007c00648a8
0x00000006c2404b38      4       5070    0x00000006c2404ba8      live    sun/misc/Launcher$AppClassLoader@0x00000007c000f958
0x00000006c2404ba8      0       0         null          live    sun/misc/Launcher$ExtClassLoader@0x00000007c000fd00

total = 4       610     1139931     N/A         alive=3, dead=1     N/A
```

- 使用 `jmap -heap` + pid 查看堆中状况

```
C:\Users\gjx16>jmap -heap 5816
Attaching to process ID 5816, please wait...
Debugger attached successfully.
Server compiler detected.
JVM version is 25.221-b11

using thread-local object allocation.
Parallel GC with 10 thread(s)

Heap Configuration:
   MinHeapFreeRatio         = 0
   MaxHeapFreeRatio         = 100
   MaxHeapSize              = 4257218560 (4060.0MB)
   NewSize                  = 88604672 (84.5MB)
   MaxNewSize               = 1418723328 (1353.0MB)
   OldSize                  = 177733632 (169.5MB)
   NewRatio                 = 2
   SurvivorRatio            = 8
   MetaspaceSize            = 21807104 (20.796875MB)
   CompressedClassSpaceSize = 1073741824 (1024.0MB)
   MaxMetaspaceSize         = 17592186044415 MB
   G1HeapRegionSize         = 0 (0.0MB)

Heap Usage:
PS Young Generation
Eden Space:
   capacity = 48758784 (46.5MB)
   used     = 11702160 (11.160049438476562MB)
   free     = 37056624 (35.33995056152344MB)
   24.000106319304436% used
From Space:
   capacity = 524288 (0.5MB)
   used     = 0 (0.0MB)
   free     = 524288 (0.5MB)
   0.0% used
To Space:
   capacity = 1572864 (1.5MB)
   used     = 0 (0.0MB)
   free     = 1572864 (1.5MB)
   0.0% used
PS Old Generation
   capacity = 177733632 (169.5MB)
   used     = 1155216 (1.1016998291015625MB)
   free     = 176578416 (168.39830017089844MB)
   0.6499704006498894% used

3158 interned Strings occupying 259480 bytes.
```

- 使用 `jstat -gc` + pid 查看元空间容量和被使用量

[![image-20191211205017818](https://github.com/GJXAIOU/Notes/raw/master/JavaVirtualMachine/JVMNotes/Java%E5%86%85%E5%AD%98%E7%BB%93%E6%9E%84.resource/image-20191211205017818.png)](https://github.com/GJXAIOU/Notes/blob/master/JavaVirtualMachine/JVMNotes/Java内存结构.resource/image-20191211205017818.png)

```
C:\Users\gjx16>jstat -gc 14320
 S0C    S1C    S0U    S1U      EC       EU        OC         OU       MC     MU    CCSC   CCSU   YGC     YGCT    FGC    FGCT     GCT
1536.0 1536.0  0.0    0.0   48640.0   8755.1   173568.0    1061.0   4864.0 3763.1 512.0  409.7      19    0.013   0      0.000    0.013
```

其中 MC表示元空间总大小，MU表示元空间已使用的大小；

- jcmd (从JDK 1. 7开始增加的命令)

| 命令                                             | 含义                                                 |
| ------------------------------------------------ | ---------------------------------------------------- |
| jcmd pid VM.flags                                | 查看该线程的JVM 的启动参数                           |
| jcmd pid help                                    | 列出当前运行的 Java 进程可以执行的操作               |
| jcmd pid help 具体命令                           | 查看具体命令的选项                                   |
| jcmd pid PerfCounter.print                       | 查看具体命令的选项                                   |
| jcmd pid VM.uptime                               | 查有JVM的启动时长                                    |
| jcmd pid GC.class_ histogram                     | 查看系统中类的统计信息                               |
| jcmd pid Thread.print                            | 查看线程堆栈信息                                     |
| jcmd pid GC.heap_dump filename.hprof(可以加路径) | 导出 Heap dump文件， 导出的文件可以通过jvisualvm查看 |
| jcmd pid VM.system_ properties                   | 查看 JVM 的属性信息                                  |
| jcmd pid VM.version                              | 查看目标 JVM 进程的版本信息                          |
| jcmd pid VM.command_line                         | 查看 JVM 启动的命令行参数信息                        |

- jstack ：可以查看或者导出 Java 应用程序中线程的堆栈信息 `jstack pid`

- **jmc**（Java Mission Control）:页面式的查看工具，可以安装插件

  - 使用命令行开启
  - 功能更加齐全，界面更加优秀

  注：jfr（Java Flight Recoder）Java 飞行记录器：可以实时获取 Java 进程的统计数据

- JVisualVM 中有 OQL 对象查询语言，类似于 SQL 语句，可以查询一些值；

### JVM内存举例说明

```
public void method() {
    Object object = new Object();

    /*生成了2部分的内存区域，1)object这个引用变量，因为
        是方法内的变量，放到JVM Stack里面,2)真正Object
        class的实例对象，放到Heap里面
        上述 的new语句一共消耗12个bytes, JVM规定引用占4
        个bytes (在JVM Stack)， 而空对象是8个bytes(在Heap)
        方法结束后，对应Stack中的变量马上回收，但是Heap
        中的对象要等到GC来回收、*/
}
```

### 本机直接内存溢出

直接内存（DirectMemory）的容量如果不通过 `-XX:MaxDirectMemorySize` 指定，默认等于 Java 堆最大值（可通过 -Xmx 指定）。

下面代码越过了 DirectByteBuffer 类，直接通过反射获取 Unsafe 实例进行内存分配（Unsafe 类的 getUnsafe() 方法限制了只有引导类加载器才会返回实例，也就是设计者希望只有 rt.jar 中的类才能使用 Unsafe 的功能）。因为，虽然使用 DirectByteBuffer 分配内存也会抛出内存溢出异常，但它抛出异常时并没有真正向操作系统申请分配内存，而是通过计算得知内存无法分配，于是手动抛出异常，真正申请分配内存的方法是unsafe.allocateMemory()。

```
import java.lang.reflect.Field;
import sun.misc.Unsafe;


public class DirectMemoryOOM {
  	private static final int _1MB = 1024 * 1024;

  	public static void main(String[] args) throws IllegalArgumentException,
     IllegalAccessException {
   	 	Field unsafeField = Unsafe.class.getDeclaredFields()[0];
  		unsafeField.setAccessible(true);
    	Unsafe unsafe = (Unsafe) unsafeField.get(null);
    	while (true) {
     		unsafe.allocateMemory(_1MB);
    	}
  	}
}
```

运行结果： Exception in thread "main" java.lang.OutOfMemoryError at sun.misc.Unsafe.allocateMemory(Native Method) at org.fenixsoft.oom.DMOOM.main(DMOOM.java:20） 由DirectMemory异致的内存溢出，一个明显的特征是在Heap Dump文件中不会看见明显的异常，如果读者发现OOM之后Dump文件很小，而程序中又直接或间接使用了NIO，那就可以考虑一下是不是这方面的原因。







**虚拟机栈**：栈帧（Stack Frame ）

**程序计数器**（Program Counter ）：记录下一条指令的位置，描述指令执行顺序

**本地方法栈**：主要处理本地方法，在Hotspot中本地方法栈和JVM方法栈是同一个，可以-Xss控制

**堆**（Heap ）：JVM管理的最大一块内存空间，与堆相关的一个重要概念是垃圾收集器，现在几乎所有的垃圾收集器都是采用分代收集算法。堆空间也基于这一点进行了相应的划分：新生代与老年代，Eden，Survivor1和Survivor2。

**方法区**（Method Area）：存储元信息，永久代（Permanent Generation），从JDK8开始，已经彻底废弃了永久代，使用元空间（meta space）,元空间是直接使用操作系统的物理内存，并且由专门的元空间虚拟机来管理，默认大小为21m，会进行垃圾回收和扩容，上限是物理内存

**运行时常量池**：方法区的一部分内容

**直接内存**：Driect Memory，由操作系统管理，不经过JVM的堆内存，速度更快，但是申请和回收比较麻烦，与Java NIO密切相关，JVM通过堆上的DirectByteBuffer来操作直接内存



new关键字创建对象的3个步骤：

1. 在堆内存中创建出对象的实例
2. 为对象的实例成员变量赋初值
3. 将对象的引用返回

**在执行第1步 创建对象实例时，通过以下两种方式来决定实例创建在内存的什么位置**

- 指针碰撞：前提是堆中的空间通过一个指针进行分割，一侧是已经被占用的空间，另一侧是未被占用的空间

- 空闲列表：前提是堆内空间中已被使用和未被使用的是交织在一起的，这时，虚拟机就需要一个列表来记录哪些空间是可以使用的，哪些空间是已经被使用的，接下来找出可以容纳下创建新对象且未被使用的空间，在此空间存放新对象，同时会要修改列表上的记录。

**对象在内存中的布局**

1. 对象头
2. 实例数据（在类中的声明的各项信息）
3. 对齐填充（可选）

**引用方法对象的方式**

- 句柄：引用指向句柄，句柄中包含两部分，一部分是指向实例的基本信息，另一部分是指向该类信息

- 直接指针：引用直接指向实例的基本数据，另一部分指向类信息，Hotspot采用的就是这种方式





- 



### 栈上分配

​	通过逃逸分析后，如果变量不会超出作用范围，便不在堆中创建对象，而是在栈上进行分配，随着函数调用结束会和栈帧一起销毁，减少垃圾回收的压力。

**逃逸分析**：必须在-server 下使用，通过-XX:+DoEscapeAnalysis 启用逃逸分析。

**标量替换**：-XX:+EliminateAllocations，在对象没有逸出的情况下，将一个对象的字段当作局部变量在栈上分配。

**TLAB**：（ThreadLocalAllocationBuffer）多线程并发申请内存需要加锁来保证内存的正确分配，不会出现两个线程的分配到同一块内存区域，TLAB事先在堆里面为每个线程分配一块内存，这块内存只有在线程创建对象时是私有的，其他线程可以访问该区域中的对象。通过 -XX:-UseTLAB来控制。



### 虚拟机中的对象

new 一个对象时：

1. 检查加载：检查对应的类是否已经被加载到方法区。

2. 分配内存

   划分内存：指针碰撞和空闲列表

   并发安全问题：用CAS失败重试TLAB

3. 内存空间初始化：比如整型默认为0，boolean类型默认为false

4. 设置：将已申请到内存区域与对应类信息相关联，还有hashcode、分代年龄等记录在对象头中？？

5. 对象初始化：执行对应的构造方法，为相应的成员变量赋初值



### 对象的内存结构

- 对象头

  - 对象自身的运行时数据：如hashcode、GC年龄、锁的状态、偏向锁的id
  - 类型指针：指向该对象的所属类型

- 实例数据

  对象的实例数据就是在java代码中能看到的属性和他们的值。

- 对齐填充

  因为JVM要求java的对象占的内存大小应该是8bit的倍数，所以后面有几个字节用于把对象的大小补齐至8bit的倍数。



### 访问对象的方式

![](.\img\1581938192(1).jpg)

**句柄：**句柄池中的句柄会包含该对象的实例数据和对象类型数据，垃圾收集可能会移动该对象在内存中的存放地址，所以句柄的指向地址也会随之变化，但句柄的所作位置不会变化，虚拟机栈中的指向该句柄的引用也不用改变。

**直接指针：**需要在对象发生移动时，更新虚拟机栈中该对象的引用。好处也显而易见，直接指针只要一次寻址就能找到对象的数据，而使用句柄的方式需要两次。Hotspot采用的就是直接指针。





## Java对象的分配

对象的存放，会根据对象情况，依次看满足下边从上往下的四个区域。

### 栈上分配

- 线程私有小对象，随着栈帧的消失而消失，都不用垃圾回收你
- 无逃逸，逃逸的简单理解为，有别的引用可以指向这个对象，这个对象不再完全被方法控制，方法结束，但是这个对象的外部引用还在。
- 支持标量替换
- 无需我们调整

###  线程本地分配TLAB（Thread Local Allcation Buffer）

- 占用eden，默认1%，是提高对象分配效率的机制
- 多线程的时候不用竞争eden就就可以申请空间，提高效率
- 小对象
- 无需我们调整

### 老年代

- 大对象，不够大也不放

###  eden

- 上面都不满足，才会放到eden





8.Java堆分析

### 8.1 内存溢出（OOM）的原因

JVM中那些内存区间？

堆，永久区，线程栈，直接内存

- 堆溢出

  占用大量堆空间，直接溢出，抛出OutOfmemoryError：Java heap space

  解决办法：增大堆空间，即使释放内存

- 永久区溢出

  抛出OutOfmemoryError：PermGen space

  解决办法：增大Perm区，允许Class回收

- 栈溢出

  创建线程的时候，需要为线程分配栈空间，如果无法给出足够空间，就会OOM

  解决办法：减小堆空间，减少栈大小

- 直接内存溢出

  直接内存在堆外，是操作系统直接分配给JVM内存的

  ByteBuffer.allocateDirect()无法从操作系统获得足够空间

  解决办法：减少堆内存，有意出发GC，GC可以回收直接内存的空间，但是直接内存不会触发GC

### 8.2 MAT

MAT为一个内存分析工具MAT(Memory Analyzer Tool)

**支配树**

如果只有一个对象D能到F，那个对象D支配对象F，D为支配者，F是被支配对象。

在支配者被回收时，被支配对象也会被回收，因为没有别的引用指向它，也就无法再使用了。

![](.\img\1577877011(1).jpg)

**显示线程对象信息**

with out  它引用了哪些对象

with in 系统中哪些对象引用到它了

**浅堆**

一个对象结构所占用的内存大小，浅堆的大小和对象的内容无关，只和对象结构有关

对象头占8个字节，一个引用占4个字节，那么只定义一个引用的对象的浅堆为12字节

**深堆**

一个对象被GC回收后，可以真实释放的内存大小

只能通过对象访问到的（直接或者间接）所有对象的浅堆之和（支配树）

上边那个4字节的引用，指向了一个16字节的对象，那么上边那个对象的深堆为28字节

### 8.3 Tomcat OOM分析案例

Tomcat在接收大量请求时发生OOM，获取Dump文件，进行分析

使用MAT打开堆

解决办法：

- OOM由于session过多引起，可以考虑增大堆大小
- 可以的话缩短session过期时间，，使其及时过期回收

