

JVM 垃圾回收

[TOC]

## 一、垃圾收集区域

### （一）概述

- 考虑哪些内存需要回收、什么时候回收、如何回收；

- 首先**程序计数器、虚拟机栈、本地方法栈都是随线程而生随线程而灭**，大体上都是在编译期可知的（运行期会由  JIT 编译期进行优化），因此它们在方法或者线程结束之后对应的内存空间就回收了

- 下面只考虑 Java 堆和方法区，因为一个接口中的各个实现类需要的内存可能各不相同，一个方法的各个分支需要的内存也不一样，只能在程序运行期才能知道会创建哪些对象和回收都是动态的，需要关注这部分内存变化。

- 一般使用 new 语句创建对象的时候消耗 12 个字节，其中引用在栈上占 4 个字节，空对象在堆中占 8 个字节。如果该语句所在方法执行结束之后，对应 Stack 中的变量会马上进行回收，但是 Heap 中的对象要等到 GC 来回收。

### （二）方法区

- **Java 虛拟机规范表示可以不要求虚拟机在这区实现 GC，该区 GC 的“性价比”一般比较低**。在堆中，尤其是在新生代，常规应用进行 I 次 GC 一般可以回收 70%~95% 的空间，而方法区的 GC 效率远小于此；
- 当前的商业 JVM 都有实现方法区的 GC ,主要回收两部分内容：**废弃常量与无用类**；
- ==方法区的类回收需要同时满足如下三个条件==：（可以回收但是不一定回收）
  - 该类所有的实例都已经被 GC，也就是JVM中不存在该 Class 的任何实例；
  - 加载该类的类加载器已经被GC（因为类加载器和该类加载器加载的 Class 对象之间是双向引用的）；
  - 该类对应的 `java.lang.Class` 对象没有在任何地方被引用，且不能在任何地方通过反射访问该类的方法；
- 在大量使用反射、动态代理、CGLib 等字节码框架、动态生成 JSP 以及 OSGi 这类频繁自定义类加载器的场景都需要 JVM 具备类卸载的支持以保证方法区不会溢出。

## 二、垃圾判断

### （一）垃圾判断的算法

- 引用计数算法(Reference Counting)

  - 给对象添加一个引用计数器，当有一个地方引用它则计数器 +1，当引用失效的时候计数器 -1，任何时刻计数器为 0 的对象就是不可能再被使用的；

  - **引用计数算法无法解决对象循环引用的问题**。==问题：循环引用能不能解决==

    ​	如下面代码中两个对象处理互相引用对方，再无任何引用

    ```java
    package chapter3;
    
    import org.junit.jupiter.api.Test;
    
    public class ReferenceCountingGC {
        public Object instance = null;
        private static final int memory = 1024 * 1024;
        /**
         * 该成员属性作用为：占用内存，以便能在 GC 日志中看清楚是否被回收过
         */
        private byte[] bigSize = new byte[2 * memory];
    
        @Test
        public static void testGC() {
            ReferenceCountingGC objA = new ReferenceCountingGC();
            ReferenceCountingGC objB = new ReferenceCountingGC();
            objA.instance = objB;
            objB.instance = objA;
            objA = null;
            objB = null;
    
            // 直接进行 GC
            System.gc();
        }
    }
    
    ```

    分析：testGC() 方法的前四行执行之后，objA 对象被 objA 和 objB.instance 引用着，objB 也类似；执行objA=null 和 objB=null 之后，objA 对象的 objA 引用失效，但是 objB.instance 引用仍然存在，因此如果采用单纯的引用计数法，objA 并不会被回收，除非在执行 objB=null 时，遍历 objB 对象的属性，将里面的引用全部置为无效。

    

- 根搜索算法( GC Roots Tracing )【可达性】

  - 在实际的生产语言中(Java、 C#等)都是使用根搜索算法判定对象是否存活；

  - 算法基本思路就是通过一系列的称为 GC Roots 的点作为起始点进行向下搜索，当一个对象到 GC Roots 没有任何引用链(Reference Chain)相连，则证明此对象是不可用的。下图中 object5/6/7 之间虽然互相有引用，但是它们到 GC Roots 是不可达的，因此会被判定为是可回收对象。

### （二）可作为GC Roots的对象

- 虚拟机栈（栈帧中的本地变量表）中引用的对象。
- 方法区中类静态属性引用的对象。
- 方法区中常量引用的对象。
- 本地方法栈中 JNI（即一般说的Native方法）引用的对象

<img src="./img/image-20191212212156641.png" alt="image-20191212212156641" style="zoom:50%;" />



-----------

## 3. 引用

详见：**[think of Java：99-深入Java引用.md](../Java/think of Java：99-深入Java引用.md)**

在JDK 1.2之后，Java 对引用的概念进行了扩充，将引用分为强引用（Strong Reference）、 软引用（Soft Reference）、 弱引用（Weak Reference）、 虚引用（Phantom Reference）4种，这4种引用强度依次逐渐减弱。

### 3.1 四种引用类型

**强引用**

​		就是指在程序代码之中普遍存在的，类似`Object obj = new Object（）`这类的引用，只要强引用还存在，垃圾收集器永远不会回收掉被引用的对象。

**软引用**

​		是用来描述一些还有用但并非必需的对象。 对于软引用关联着的对象，在系统将要发生内存溢出异常之前，将会把这些对象列进回收范围之中进行第二次回收。 **如果这次回收还没有足够的内存，才会抛出内存溢出异常**。 在 JDK 1.2 之后，提供了 SoftReference 类来实现软引用，软引用通常用来实现内存敏感的缓存，如果还有空闲内存，就可以暂时保留缓存，当内存不足时清理掉，这样就保证了使用缓存的同时，不会耗尽内存。 

示例：

```java
MyObject aRef = new MyObject();
SoftReference aSoftRef = new SoftReference(aRef);
```

​		一旦 SoftReference 保存了对一个 Java 对象的软引用后，在垃圾线程对这个 Java 对象回收前，SoftReference 类所提供的 get() 方法返回 Java 对象的强引用。另外，一旦垃圾线程回收该 Java 对象之后，get() 方法将返回 null。

**弱引用**

​		也是用来描述非必需对象的，但是它的强度比软引用更弱一些，**被弱引用关联的对象只能生存到下一次垃圾收集发生之前**。 当垃圾收集器工作时，无论当前内存是否足够，都会回收掉只被弱引用关联的对象。 在JDK 1.2 之后，提供了 WeakReference 类来实现弱引用。可以用来维护一种非强制性的映射关系，如果试图获取时对象还在，就使用它，否则重新实例化，它同样是很多缓存实现的选择。

​		在 Java 集合中有一种特殊的 Map 类型：WeakHashMap， 在这种 Map 中存放了键对象的弱引用，当一个键对象被垃圾回收，那么相应的值对象的引用会从 Map 中删除。WeakHashMap 能够节约存储空间，可用来缓存那些非必须存在的数据。

**虚引用**

​		也称为幽灵引用或者幻影引用，它是最弱的一种引用关系。一个对象是否有虚引用的存在，完全不会对其生存时间构成影响，在任何时候都可能被垃圾回收器回收，get（）总是返回null，**无法通过虚引用来取得一个对象实例。为一个对象设置虚引用关联的唯一目的就是希望能在这个对象被收集器回收前收到一个系统通知**。在 JDK 1.2 之后，提供了 PhantomReference 类来实现虚引用，这种引用通常用来做所谓的 Post - Mortem 清理机制，如 Java 平台自身的 Cleaner机制也是继承虚引用来实现。也可以利用虚引用监控对象创建和销毁。

### 3.2 引用队列的使用

​		谈到各种引用的编程，就必然要提到引用队列。我们在创建各种引用并关联到相应对象时，可以选择是否需要关联引用队列。

​		虚引用无法取得对象实例，因此必须和引用队列（ReferenceQueue）联合使用。当垃圾收集器准备回收一个对象时，如果发现它还有虚引用，JVM会把该Reference设置成pending状态，如果引用创建时指定了ReferenceQueue，在回收对象的内存之前，ReferenceHandler线程会把这个虚引用加入到与之关联的引用队列中。程序可以通过判断引用队列中是否已经加入了虚引用，来了解被引用的对象是否将要被垃圾回收。

```java
Object counter = new Object();
ReferenceQueue refQueue = new ReferenceQueue();
PhantomReference<Object> p = new PhantomReference<>(counter,refQueue);
counter=null;
System.gc();
try{
    Reference remove = refQueue.remove(10000L);
    if (remove!=null){
        System.out.println("cleaned");
    }
}catch (InterruptedException e){
    e.printStackTrace();
}
```



----

## 四、对象回收与finalize()

**即使在可达性分析算法中不可达的对象，也并非是“非死不可”的**，这时候它们暂时处于“缓刑”阶段，要真正宣告一个对象死亡，至少要经历两次标记过程：

- 如果对象在进行可达性分析后发现没有与 GC Roots 相连接的引用链，那它将会被第一次标记并且进行一次筛选，**筛选的条件是**此对象是否有必要执行 finalize() 方法。当对象没有覆盖 finalize() 方法，或者 finalize() 方法已经被虚拟机调用过，虚拟机将这两种情况都视为“没有必要执行”。**如果这个对象被判定为有必要执行finalize() 方法，那么这个对象将会放置在一个叫做 F-Queue 的队列之中，并在稍后由一个由虚拟机自动建立的、低优先级的 Finalizer 线程去执行它**。**这里所谓的“执行”是指虚拟机会触发这个方法，但并不承诺会等待它运行结束**，这样做的原因是，如果一个对象在 finalize() 方法中执行缓慢或者发生了死循环，将很可能会导致 F-Queue 队列中其他对象永久处于等待，甚至导致整个内存回收系统崩溃。

- finalize() 方法是对象逃脱死亡命运的最后一次机会，稍后 GC 将对 F-Queue 中的对象进行第二次小规模的标记，如果对象要在 finalize() 中成功拯救自己——**只要重新与引用链上的任何一个对象建立关联即可，譬如把自己（this关键字）赋值给某个类变量或者对象的成员变量**，那在第二次标记时它将被移除出“即将回收”的集合；如果对象这时候还没有逃脱,那基本上它就真的被回收了。

代码示例：

```java
package chapter3;

/**
 * 此代码演示两点：
 * 1.对象可以在GC时自我救赎。
 * 2.这种自我救赎的机会只有一次，因为finalize()方法最多只会被调用一次。
 */
public class FinalizeEscapeGC {
    public static FinalizeEscapeGC saveMe = null;

    public void isLive() {
        System.out.println("我还活着！");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("执行finalize()方法中……");
        // 完成自我救赎
        saveMe = this;
    }

    public static void main(String[] args) throws InterruptedException {
        saveMe = new FinalizeEscapeGC();

        // 对象第一次拯救自己
        saveMe = null;
        System.gc();

        // 因为finalize方法优先级比较低，所以暂停进行等待
        Thread.sleep(5000);

        if (saveMe == null) {
            System.out.println("我已经死亡！");
        } else {
            saveMe.isLive();
        }

        // 对象第二次自我救赎，失败
        saveMe = null;
        System.gc();
        Thread.sleep(5000);

        if (saveMe == null) {
            System.out.println("我已经死亡！");
        } else {
            saveMe.isLive();
        }
    }
}

```

执行结果：

```java
执行finalize()方法中……
我还活着！
我已经死亡！
```

从结果可以看出 saveMe 对象的 finalize() 方法确实被 GC 收集器触发过，但是在被收集前逃脱了；

同时程序中两段相同的代码执行结果一次逃脱一次失败，因为任何一个对象的 finalize() 方法都只会被系统自动调用一次，如果对象面临下一次回收，它的 finalize() 方法不会被再次执行，因此第二段代码中自救失败。

![finalize 执行过程](D:/study/Notes/JavaVirtualMachine/JVMNotes/JVM 垃圾回收.resource/finalize 执行过程.jpg)

- 需要特别说明的是，**建议大家尽量避免使用这种方法来拯救对象**，因为它不是 C/C++ 中的析构函数，而是Java刚诞生时为了使C/C++程序员更容易接受它所做出的一个妥协。它的运行代价高昂，不确定性大，无法保证各个对象的调用顺序。有些教材中描述它适合做“关闭外部资源”之类的工作“，这完全是对这个方法用途的一种自我安慰。finalize()能做的所有工作，使用try-finally或者其他方式都可以做得更好、更及时，所以建议大家完全可以忘掉Java语言中有这个方法的存在。



## 五、JVM 常见的 GC 算法

- 标记-清除算法(Mark Sweep)

- 标记-整理算法(Mark-Compact)

- 复制算法(Copying)

- 分代算法(Generational)

  **新生使用复制算法，老年代一般采用标记-清除算法或者标记-整理算法**；

### （一）标记一清除算法(Mark-Sweep)

​		Finalizer机制和Cleaner机制的缺陷，在《Effective Java》第三版中对避免使用 finalize 和 Cleaner进行了详细说明。	

- YGC(不同于CMS)
  - G1 YGC 在 Eden 充满时触发，在回收之后所有之前属于 Eden 的区块全部变成空白，即不属于任何一个分区( Eden、Survivor、Old )
  - YGC执行步骤：
    - 阶段1:根扫描 静态和本地对象被描
    - 阶段2:更新RS 处理dirty card队列更新RS
    - 阶段3:处理RS 检测从年轻代指向老年代的对象
    - 阶段4:对象拷贝 拷贝存活的对象到survivor/old区域
    - 阶段5:处理引用队列 软引用，弱引用，虚引用处理
- 并发阶段（global concurrent marking）
- 混合模式
- Full GC (一 般是G1出现问题时发生，本质上不属于G1，G1进行的回退策略（回退为：Serial Old GC）)

### 什么时候发生 Mixed GC?

- 由一些参数控制，另外也控制着哪些老年代 Region 会被选入 CSet  (收集集合)，下面是一部分的参数

  - **G1HeapWastePercent**：在 global concurrent marking 结束之后，我们可以知道 old gen regions 中有多少空间要被回收，在每次 YGC 之后和再次发生 Mixed GC 之前（YGC  和 Mixed GC 之间是交替进行（不是一次一次交替，可能是多次对一次）的），会检查垃圾占比是否达到此参数，只有达到了，下次才会发生 Mixed GC；
  - **G1MixedGCLiveThresholdPercent**： old generation region 中的存活对象的占比，只有在此参数之下，才会被选入CSet
  - **G1MixedGCCountTarget**：一 次 global concurrent marking 之后，最多执行 Mixed GC 的次数
  - **G1OldCSetRegionThresholdPercent**：一次 Mixed GC 中能被选入 CSet 的最多 old generation region 数量
  - 除了以上的参数，G1 GC 相关的其他主要的参数有：

  | 参数                               | 含义                                                         |
  | :--------------------------------- | :----------------------------------------------------------- |
  | -XX:G1HeapRegionSize=n             | 设置 Region 大小，并非最终值                                 |
  | -XX:MaxGCPauseMillis               | 设置 G1 收集过程目标时间，默认值200 ms，不是硬性条件         |
  | -XX:G1NewSizePercent               | 新生代最小值，默认值 5%                                      |
  | -XX:G1MaxNewSizePercent            | 新生代最大值，默认值 60%                                     |
  | -XX:ParallelGCThreads              | STW 期间，并行 GC 线程数                                     |
  | -XX:ConcGCThreads=n                | 并发标记阶段，并行执行的线程数                               |
  | -XX:InitiatingHeapOccupancyPercent | 设置触发标记周期的 Java 堆占用率阈值。默认值是 45%。这里的 Java 堆占比指的是 non_young_capacity_bytes，包括old+humongous |

### G1 收集概览

- G1算法将堆划分为若干个区域( Region),它仍然属于分代收集器。不过,这些区域的一部分包含新生代,**新生代的垃圾收集依然采用暂停所有应用线程的方式**，将存活对象拷贝到老年代或者 Survivor 空间。老年代也分成很多区域，**G1收集器通过将对象从一个区域复制到另外一个区域,完成了清理工作**。这就意味着,在正常的处理过程中，G1完成了堆的压缩(至少是部分堆的压缩，因为复制本质上就包括压缩),这样也就不会有CMS内存碎片问题的存在

#### Humongous区域

在G1中,还有一种特殊的区域,叫 Humongous区域。如果一个对象占用的空间达到或是超过了分区容量50%以上,G1收集器就认为这是一个巨型对象。**这些巨型对象,默认直接会被分配在老年代**,但是如果它是一个短期存在的巨型对象就会对垃圾收集器造成负面影响。为了解决这个问题， G1 划分了一个 Humongous 区,它用来专门存放巨型对象。如果一个H 区装不下一个巨型对象,那么 G1 会寻找连续的H分区来存储。为了能找到连续的H区,有时候不得不启动 Full GC



### G1 Yong GC

- Young GC 主要是对Eden区进行GC，**它在Eden空间耗尽时会被触发**。在这种情况下Eden空间的数据移动到 Survivor空间中如果 Survivor 空间不够,Eden空间的部分数据会直接晋升到老年代空间。 Survivor区的数据移动到新的 Survivor 区中,也有部分数据晋升到老年代空间中。最终Eden空间的数据为空,GC完成工作,应用线程继续执行;
- 如果仅仅 GC 新生代对象,我们如何找到所有的根对象呢?老年代的所有对象都是根么?那这样扫描下来会耗费大量的时间。于是，G1引进了 RSet 的概念。它的全称是 Remembered set，作用是跟踪指向某个堆内的对象引用

![image-20191218205128114](D:/study/Notes/JavaVirtualMachine/JVMNotes/JVM 垃圾回收.resource/image-20191218205128114.png)

- 在 CMS 中,也有RSet的概念,**在老年代中有一块区域用来记录指向新生代的引用这是一种 point-out**,在进行 Young Go时扫描根时,仅仅需要扫描这一块区域,而不需要扫描整个老年代

- 但在 G1 中,并没有使用 point-out，这是由于一个分区太小,分区数量太多,如果是用 point-out 的话,会造成大量的扫描浪费,有些根本不需要 GC 的分区引用也扫描了。

- 于是 G1中使用 point-in 来解决。 point-in 的意思是哪些分区引用了当前分区中的对象。这样,仅仅将这些对象当做根来扫描就避免了无效的扫描。

- 由于新生代有多个，那么我们需要在新生代之间记录引用吗?这是不必要的，原因在于每次 GC 时所有新生代都会被扫描，**所以只需要记录老年代到新生代之间的引用即可**

- 需要注意的是，如果引用的对象很多，赋值器需要对每个引用做处理，赋值器开销会很大，为了解决赋值器开销这个问题，在 G1 中又引入了另外一个概念：卡表( Card table)。一个 Card table 将一个分区在逻辑上划分为固定大小的连续区域，每个区域称之为卡。卡通常较小，介于 128 到 512 字节之间。 Card Table通常为字节数组，由 Card 的索引(即数组下标)来标识每个分区的空间地址；

- 默认情况下，每个卡都未被引用，当一个地址空间被引用时候，这个地址空间对应的数组索引的值被标记为 0，即标记为脏被引用，此外 Rset 也将这个数组下标记录下来。一般情况下，**这个 Rset 其实是一个 HashTable，Key 是别的 Region 的起始地址，Value 是一个集合，里面的元素是 Card Table 的 Index**。

- G1 Young GC 过程

  - 阶段一：根扫描；

    静态和本地对象被扫描

  - 阶段二：更新 RS

    处理 Dirty Card 队列，更新 RS

  - 阶段三：处理 RS

    检测从年轻代指向老年代的对象

  - 阶段四：对象拷贝

    拷贝存活的对象到 Survivor/old 区域

  - 阶段五：处理引用队列

    软引用、弱引用、虚引用处理



#### 再谈 Mixed GC

- **Mixed GC 不仅进行正常的新生代垃圾收集，同时也回收部分后台扫描线程（即全局并发标记的线程）标记的老年代分区**；
- Mixed GC 步骤：
  - 全局并发标记（Global concurrent marking）
  - 拷贝存活对象（evacuation）

- 在 G1 GC 中， Global concurrent Marking 主要是为 Mixed GC 提供标记服务的，并不是一次 GC 过程中的一个必须环节。



### 三色标记算法

提到并发标记，我们不得不了解并发标记的三色标记算法。它是描述追踪式回收器的一种有效的方法，利用它可以推演回收器的正确性，**标记表示该对象是可达的，即不应该被当做垃圾回收**

- 我们将对象分成三种类型:
  - **黑色**：根对象，或者该对象与它的子对象（一个对象里面包含或者容纳的成员变量，因为一个对象或者类里可以引用其他的对象）都被扫描过(对象被标记了，且它的所有 field 也被标记完了)
  - **灰色**：对象本身被扫描，但还没扫描完该对象中的子对象( 它的 field 还没有被标记或标记完)
  - **白色**：未被扫描对象，扫描完成所有对象之后，最终为白色的为不可达对象，即垃圾对象(对象没有被标记到)

#### 示例：

遍历了所有可达的对象后，所有可达的对象都变成了黑色。不可达的对象即为白色，需要被清理,如图：

[<img src="D:/study/Notes/JavaVirtualMachine/JVMNotes/JVM 垃圾回收.resource/sanmark.gif" alt="三色标记算法" style="zoom: 50%;" />](https://github.com/weolwo/jvm-learn/blob/master/src/resources/images/sanmark.gif)



- 但是如果在标记过程中，应用程序也在运行，那么对象的指针就有可能改变。这样的话，我们就会遇到一个问题:对象丢失问题

<img src="D:/study/Notes/JavaVirtualMachine/JVMNotes/JVM 垃圾回收.resource/image-20191218210809306.png" alt="image-20191218210809306" style="zoom:50%;" />

这时候应用程序执行了以下操作: A.c=C B.c=null 这样，对象的状态图变成如下情形:

[<img src="D:/study/Notes/JavaVirtualMachine/JVMNotes/JVM 垃圾回收.resource/sans2.png" alt="img" style="zoom:50%;" />](https://github.com/weolwo/jvm-learn/blob/master/src/resources/images/sans2.png)

这时候垃圾收集器再标记扫描的时候就会变成下图这样

[<img src="D:/study/Notes/JavaVirtualMachine/JVMNotes/JVM 垃圾回收.resource/sans1.png" alt="img" style="zoom:50%;" />

- **很显然，此时C是白色，被认为是垃圾需要清理掉，显然这是不合理的**

### SATB

- 在G1中，使用的是SATB ( Snapshot-At-The- Beginning)的方式，删除的时候记录所有的对象
- 它有3个步骤
  - 在开始标记的时候生成一个快照图，标记存活对象
  - 在并发标记的时候所有被改变的对象入队(在writebarrier里把所有旧的引用所指向的对象都变成非白的（例如上面的 C 颜色会变成灰色或者黑色，不会导致被回收）)
  - 可能存在浮动垃圾，将在下次被收集

### G1混合式回收

- G1到现在可以知道哪些老的分区可回收垃圾最多。当全局并发标记完成后，在某个时刻，就开始了Mixed GC。这些垃圾回收被称作“混合式”是因为他们不仅仅进行正常的新生代垃圾收集，同时也回收部分后台扫描线程标记的分区混合式GC也是采用的复制清理策略，当GC完成后，会重新释放空间



#### G1分代算法

为老年代设置分区的目的是老年代里有的分区垃圾多,有的分区垃圾少,这样在回收的时候可以专注于收集垃圾多的分区这也是G1名称的由来不过这个算法并不适合新生代垃圾收集，**因为新生代的垃圾收集算法是复制算法,但是新生代也使用了分区机制主要是因为便于代大小的调整**。



### SATB详解

- SATB 是维持并发 GC 的一种手段。G1 并发的基础就是 SATB。SATB 可以理解成在 GC 开始之前对堆内存里的对象做次快照，此时活的对象就认为是活的，从而形成了一个对象图。
- 在 GC 收集的时候，新生代的对象也认为是活的对象，除此之外其他不可达的对象都认为是垃圾对象

#### 如何找到在GC过程中分配的对象呢?

- 每个region记录着两个 top-at-mark-start ( TAMS 指针，分别为 prevTAMS 和 nextTAMS。在TAMS以上的对象就是新分配的，因而被视为隐式marked（即默认被标记了）。
- 通过这种方式我们就找到了在GC过程中新分配的对象，并把这些对象认为是活的对象。
- 解决了对象在GC过程中分配的问题，那么在GC过程中引用发生变化的问题怎么解决呢?
  - G1给出的解决办法是通过 WriteBarrier。Write Barrier 就是对引用字段进行赋值做了额外处理。通过Write Barrier就可以了解到哪些引用对象发生了什么样的变化

### 基础知识

- mark的过程就是遍历heap标记live object的过程，采用的是三色标记算法，这三种颜色为white(表示还未访问到)、gray(访问到但是它用到的引用还没有完全扫描、black( 访问到而且其用到的引用已经完全扫描完)

- 整个三色标记算法就是从GCroots出发遍历heap,针对可达对象先标记white为gray,然后再标记gray为black;遍历完成之后所有可达对象都是black的，所有white都是可以回收的
- SATB仅仅对于在marking开始阶段进行快照（"snapshot"(marked all reachable at markstart)），但是concurrent的时候并发修改可能造成对象漏标记
  - 对black新引用了一个white对象，然后又从gray对象中删除了对该white对象的引用，这样会造成了该white对象漏标记
  - 对black新引用了一个white对象，然后从gray对象删了一个引用该white对象的white对象，这样也会造成了该white对象漏标记，
  - 对black新引用了一个刚new出来的white对象，没有其他gray对象引用该white对象，这样也会造成了该white对象漏标记
- 对于三色算法在concurrent的时候可能产生的漏标记问题，SATB在marking阶段中，对于从gray对象移除的目标引用对象将其标记为 gray,对于black引用的新产生的对象将其标记为black;由于是在开始的时候进行snapshot,因而可能存在Floating Garbage

### 漏标与误标

- 误标没什么关系，顶多造成浮动垃圾，在下次gc还是可以回收的，但是漏标的后果是致命的，把本应该存活的对象给回收了，从而影响的程序的正确性
- 漏标的情况只会发生在白色对象中，且满足以下任意一个条件
  - 并发标记时，应用线程给一个黑色对象的引用类型字段赋值了该白色对象
  - 并发标记时，应用线程删除所有灰色对象到该白色对象的引用（示例：几个灰色和一个黑色同时执行该白色）
- 对于第一种情况，利用post-write barrier，记录所有新增的引用关系，然后根据这些引用关系为根重新扫描一遍
- 对于第二种情况，利用pre-write barrier，将所有即将被删除的引用关系的旧引用记录下来，最后以这些旧引用为根重新扫描一遍

### 停顿预测模型

- G1收集器突出表现出来的一点是通过一个停顿预测模型根据用户配置的停顿时间来选择CSet的大小，从而达到用户期待的应用程序暂停时间。
- 通过-XX:MaxGCPauseMillis参数来设置。这一点有点类似于ParallelScavenge收集器。 关于停顿时间的设置并不是越短越好。
- 设置的时间越短意味着每次收集的CSet越小，导致垃圾逐步积累变多，最终不得不退化成SerialGC;停顿时间设置的过长，那么会导致每次都会产生长时间的停顿，影响了程序对外的响应时间

### G1的收集模式

Young GC 和 Mixed GC 是分代 G1 模式下选择 Cset 的两种子模式；

- G1的运行过程是这样的：会在 Young GC 和 Mixed GC 之间不断地切换运行，同时定期地做全局并发标记，在实在赶不上对象创建速度的情况下 使用 Full GC(这时候会回退到 Serial GC)。
- 初始标记是在 Young GC 上执行的，在进行全局并发标记的时候不会做 MixedGC，在做 MixedGC 的时候也不会启动初始标记阶段。
- 当 MixedGC 赶不上对象产生的速度的时候就退化成 FullGC，这一点是需要重点调优的地方。

### G1最佳实践

- 不要设置新生代和老年代的大小
  - G1 收集器在运行的时候会调整新生代和老年代 的大小。通过改变代的大小来调整对象晋升的速度以及晋升年龄，从而达到我们为收集器设置的暂停时间目标。
  - 设置了新生代大小相当于放弃了 G1 为我们做的自动调优。我们需要做的只是设置整个堆内存的大小，剩下的交给 G1 自已去分配各个代的大小即可。
- 不断调优暂停时间指标
  - 通过 `-XX:MaxGCPauseMillis=x` 可以设置启动应用程序暂停的时间，G1 在运行的时候会根据这个参数选择 CSet 来满足响应时间的设置。一般情况下这个值设置到 100ms 或者 200ms 都是可以的(不同情况下会不一样)，但如果设置成 50ms 就不太合理。**暂停时间设置的太短，就会导致出现 G1 跟不上垃圾产生的速度，最终退化成 Full GC**。所以对这个参数的调优是一个持续的过程，逐步调整到最佳状态。
- 关注Evacuation Failure
  - Evacuation（表示 copy） Failure 类似于 CMS 里面的晋升失败，堆空间的垃圾太多导致无法完成 Region之间的拷贝，于是不得不退化成 Full GC 来做一次全局范围内的垃圾收集

### G1日志解析:

程序代码为：

VM 参数为：`-verbose:gc -Xms10m -Xmx10m -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:MaxGCPauseMillis=200m`

其中：` * -XX:+UseG1GC` 表示指定垃圾收集器使用G1；`-XX:MaxGCPauseMillis=200m ` 表示设置垃圾收集最大停顿时间

```java
package com.gjxaiou.gc.g1;

/**
 * @Author GJXAIOU
 * @Date 2019/12/20 20:59
 */
public class G1LogAnalysis {
    public static void main(String[] args) {
        int size = 1024 * 1024;
        byte[] myAlloc1 = new byte[size];
        byte[] myAlloc2 = new byte[size];
        byte[] myAlloc3 = new byte[size];
        byte[] myAlloc4 = new byte[size];
        System.out.println("hello world");
    }
}
```

日志结果为：

```java
2019-12-20T21:02:10.163+0800: [GC pause (G1 Humongous Allocation【说明分配的对象超过了region大小的50%】) (young) (initial-mark), 0.0015901 secs]
   [Parallel Time: 0.8 ms, GC Workers: 10【GC工作线程数】]
      [GC Worker Start (ms): Min: 90.3, Avg: 90.4, Max: 90.4, Diff: 0.1]【几个垃圾收集工作的相关信息统计】
      [Ext Root Scanning (ms): Min: 0.1, Avg: 0.2, Max: 0.3, Diff: 0.1, Sum: 2.1]
      [Update RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
         [Processed Buffers: Min: 0, Avg: 0.0, Max: 0, Diff: 0, Sum: 0]
      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Object Copy (ms): Min: 0.4, Avg: 0.4, Max: 0.5, Diff: 0.1, Sum: 4.4]
      [Termination (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
         [Termination Attempts: Min: 1, Avg: 5.4, Max: 8, Diff: 7, Sum: 54]
【上面的几个步骤为YOUNG GC的固定执行步骤】
 * 阶段1:根扫描
 * 静态和本地对象被描
 * 阶段2:更新RS
 * 处理dirty card队列更新RS
 * 阶段3:处理RS
 * 检测从年轻代指向老年代的对象
 * 阶段4:对象拷贝
 * 拷贝存活的对象到survivor/old区域
 * 阶段5:处理引用队列
 * 软引用，弱引用，虚引用处理
      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.3]
      [GC Worker Total (ms): Min: 0.7, Avg: 0.7, Max: 0.7, Diff: 0.1, Sum: 6.9]
      [GC Worker End (ms): Min: 91.1, Avg: 91.1, Max: 91.1, Diff: 0.0]
   [Code Root Fixup: 0.0 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.1 ms]【 清楚 cardTable所花费时间】
   [Other: 0.7 ms]
      [Choose CSet: 0.0 ms]
      [Ref Proc: 0.1 ms]
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.1 ms]
      [Humongous Register: 0.0 ms]
      [Humongous Reclaim: 0.0 ms]
      [Free CSet: 0.0 ms]
   [Eden: 2048.0K(6144.0K)->0.0B(2048.0K) Survivors: 0.0B->1024.0K Heap: 3725.2K(10.0M)->2836.0K(10.0M)]
 [Times: user=0.01 sys=0.00, real=0.00 secs] 
2019-12-20T21:02:10.165+0800: [GC concurrent-root-region-scan-start]
2019-12-20T21:02:10.165+0800: [GC pause (G1 Humongous Allocation) (young)2019-12-20T21:02:10.165+0800: [GC concurrent-root-region-scan-end, 0.0006999 secs]
2019-12-20T21:02:10.165+0800: [GC concurrent-mark-start]
, 0.0013416 secs]
   [Root Region Scan Waiting: 0.3 ms]
   [Parallel Time: 0.5 ms, GC Workers: 10]
      [GC Worker Start (ms): Min: 92.5, Avg: 92.6, Max: 92.6, Diff: 0.1]
      [Ext Root Scanning (ms): Min: 0.1, Avg: 0.1, Max: 0.2, Diff: 0.1, Sum: 1.0]
      [Update RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
         [Processed Buffers: Min: 0, Avg: 0.0, Max: 0, Diff: 0, Sum: 0]
      [Scan RS (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Code Root Scanning (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
      [Object Copy (ms): Min: 0.3, Avg: 0.3, Max: 0.3, Diff: 0.0, Sum: 3.0]
      [Termination (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.0]
         [Termination Attempts: Min: 1, Avg: 4.6, Max: 8, Diff: 7, Sum: 46]
      [GC Worker Other (ms): Min: 0.0, Avg: 0.0, Max: 0.0, Diff: 0.0, Sum: 0.1]
      [GC Worker Total (ms): Min: 0.4, Avg: 0.4, Max: 0.5, Diff: 0.1, Sum: 4.1]
      [GC Worker End (ms): Min: 93.0, Avg: 93.0, Max: 93.0, Diff: 0.0]
   [Code Root Fixup: 0.0 ms]
   [Code Root Purge: 0.0 ms]
   [Clear CT: 0.2 ms]
   [Other: 0.3 ms]
      [Choose CSet: 0.0 ms]
      [Ref Proc: 0.2 ms]
      [Ref Enq: 0.0 ms]
      [Redirty Cards: 0.1 ms]
      [Humongous Register: 0.0 ms]
      [Humongous Reclaim: 0.0 ms]
      [Free CSet: 0.0 ms]
   [Eden: 1024.0K(2048.0K)->0.0B【新生代清理后】(1024.0K) Survivors: 1024.0K->1024.0K Heap: 3901.0K(10.0M)->4120.5K(10.0M)]
 [Times: user=0.00 sys=0.00, real=0.00 secs] 
2019-12-20T21:02:10.166+0800: [GC concurrent-mark-end, 0.0012143 secs]
2019-12-20T21:02:10.167+0800: [Full GC (Allocation Failure)  4120K->3676K(10M), 0.0020786 secs]
   [Eden: 0.0B(1024.0K)->0.0B(1024.0K) Survivors: 1024.0K->0.0B Heap: 4120.5K(10.0M)->3676.9K(10.0M)], [Metaspace: 3091K->3091K(1056768K)]
 [Times: user=0.00 sys=0.00, real=0.00 secs] 
2019-12-20T21:02:10.169+0800: [GC remark, 0.0000082 secs]
 [Times: user=0.00 sys=0.00, real=0.00 secs] 
2019-12-20T21:02:10.169+0800: [GC concurrent-mark-abort]
hello world
Heap
 garbage-first heap   total 10240K, used 4700K [0x00000000ff600000, 0x00000000ff700050, 0x0000000100000000)
  region size 1024K【说明region默认大小】, 1 young (1024K), 0 survivors (0K)
 Metaspace       used 3229K, capacity 4496K, committed 4864K, reserved 1056768K
  class space    used 350K, capacity 388K, committed 512K, reserved 1048576K

Process finished with exit code 0

```



# **堆**

- 堆里存放的是对象的实例

- 是Java虚拟机管理内存的最大一块

- GC主要的工作区域，为了高效GC，会把堆细分为更多的子区域

- 线程共享

  

**方法区**

- 存放了每个Class的结构信息，包括常量池、字段描述、方法描述
- GC的非主要工作区域
- 线程共享



```java
public void method1(){
    Object obj = new Object();
}
```

- obj这个引用变量，因为是方法内的变量，会放在JVM Stack里面
- 真正Object class的实例对象，放到Heap里面
- 上述new语句一共消耗12个bytes，JVM规定引用占4个bytes，而空对象是8个bytes
- 方法结束后，对应的Stack中的变量马上回收，但是Heap中的对象要等下一次GC来回收

**方法区的GC**

- Java虚拟机规范表示可以不要求虚拟机在方法区实现GC，该空间的GC性价比一般比较低

- 在堆中，尤其是在新生代，常规应用进行一次GC一般可以回收70%-95%的空间，而方法区的GC效率远小于此

- 当前主流JVM都有实现方法区的GC，主要回收：废弃常量与无用类

  类回收需要满足的3个条件：

  - 该类所有实例都已经被GC，也就是JVM中不存在该Class的任何实例
  - 加载该类的ClassLoader已经被GC
  - 该类对应的java.lang.Class对象没有在任何地方被引用，如不能在任何地方通过反射访问该类的方法。

- 在大量使用反射、动态代理、CGLib等字节码框架、动态生成JSP以及OSGi这类频繁自定义ClassLoader的场景都需要JVM具备类卸载的支持以保证方法区不会溢出。

  



**垃圾判断的算法**

1. 引用计数算法

   给对象添加一个引用计数器，当有一个地方引用它，计数器加1，当引用失效，计数器减1，任何时刻计数器为0的对象就是不可能再被使用的。

   引用计数算法无法解决对象循环引用的问题。

   

2. 根搜索算法 GC Roots Tracing

   算法思路是通过一系列称为“GC Roots”的点作为起始进行向下搜索，当一个对象到GC Roots没有任何引用链，（Reference Chain）相连，则证明此对象是不可用的。

   在Java中，GC Roots包括：

   - VM栈（栈中的本地变量）中的引用

   - 方法区中的静态引用

   - JNI（一般说的Native方法）中的引用

     
   
   当执行系统停顿下来后,并不需要一个不漏地检查完所有执行上下文和全局的引用位置,虚拟机应当是有办法直接得知哪些地方存放着对象引用。在HotSpot的实现中,是使用一组称为OopMap的数据结构来达到这个目的的

### 安全点

在OopMap的协助下, HotSpot可以快速且准确地完成GC Roots枚举,但一个很现实的问题随之而来:可能导致引用关系变化,或者说OopMap内容变化的指令非常多,如果为每一条指令都生成对应的OopMap,那将会需要大量的额外空间,这样GC的空间成本将会变得更高。

实际上, HotSpot并没有为每条指令都生成OopMap,而只是在“特定的位置”记录了这些信息,这些位置称为安全点(Safepoint) ,**即程序执行时并非在所有地方都能停顿下来开始GC,只有在达到安全点时才能暂停。**

Safepoint的选定既不能太少以至于让GC等待时间太长,也不能过于频繁以至于过分增大运行时的负载。所以,安全点的选定基本上是以“**是否具有让程序长时间执行的特征**”为标准进行选定的-因为每条指令执行的时间非常短暂,程序不太可能因为指令流长度太长这个原因而过长时间运行, “长时间执行”的最明显特征就是指令序列复用,例如方法调用、循环跳转、异常跳转等,所以具有这些功能的指令才会产生Safepoint.

对于Safepoint, 另一个需要考虑的问题是如何在GC发生时让所有线程(这里不包括执行JNI调用的线程)都“跑”到最近的安全点上再停顿下来:抢占式中断( Preemptive Suspension)和主动式中断( Voluntary Suspension)

**抢占式中断:**它不需要线程的执行代码主动去配合,在GC发生时,首先把所有线程全部中断,如果有线程中断的地方不在安全点上,就恢复线程,让它“跑”到安全点上。

**主动式中断:**当GC需要中断线程的时候,不直接对线程操作,仅仅简单地设置一个标志,各个线程执行时主动去轮询这个标志,发现中断标志为真时就自己中断挂起。轮询标志的地方和安全点是重合的,另外再加上创建对象需要分配内存的地方

现在几乎没有虚拟机采用抢占式中断来暂停线程从而响应GC事件。



**安全区**

在使用Safepoint似乎已经完美地解决了如何进入GC的问题,但实际上情况却并不一定。Safepoint机制保证了程序执行时,在不太长的时间内就会遇到可进入GC的 Safepoint。但如果程序在“不执行”的时候呢?所谓程序不执行就是没有分配CPU时间,典型的例子就是处于 Sleep状态或者Blocked状态,这时候线程无法响应JVM的中断请求, JVM也显然不太可能等待线程重新分配 CPU时间。对于这种情况,就需要安全区域(Safe Region)来解决了



### 空间分配担保

在发生Minor GC之前,虚拟机会先检查老年代最大可用的连续空间是否大于新生代所有对象总空间,如果这个条件成立,那么Minor GC可以确保是安全的。当大量对象在Minor GC后仍然存活,就需要老年代进行空间分配担保,把Survivor无法容纳的对象直接进入老年代。如果老年代判断到剩余空间不足(根据以往每一次回收晋升到老年代对象容量的平均值作为经验值) ,则进行一次Full GC





**JVM 常见GC算法**

1. 标记-清除算法（Mark-Sweep）

   - 算法分为“标记”和“清除”两个阶段，首先标记出所有需要回收的对象，然后回收所有需要回收的对象

   缺点：

   - 效率问题，标记和清理两个过程效率都不高
   - **碎片问题**，标记清理之后会产生大量不连续的内存碎片，空间碎片太多可能会导致后续无法找到足够的 **连续空间** 从而提前触发下一次GC

   

2. 标记-整理算法（Mark-Compact）

   - 标记过程仍然是一样，但后续步骤不是进行直接清理，而是令所有存活的对象一端移动，然后直接清理掉这端边界以外的内存。
   - 没有内存碎片
   - 比Mark-Sweep**耗费更多的时间**进行Compact

   

3. 复制算法（Copying）

   - 将可用内存划分两块，每次只适用其中的一块，当半区内存用完了，仅将还存活的对象复制到另外一块上面，然后就把原来整块内存空间一次性清理掉
   - 这样使得每次内存回收都是对整个半区的回收，内存分配时也就不用考虑内存碎片等复杂情况，只要移动堆项指针，按顺序分配内存就可以了，实现简单，运行高效。
   - **浪费空间**，只是这种算法的代价是将内存缩小为原来一半，代价高昂。
   - 现在的商业虚拟机中都是用了这一种收集算法来回收新生代
   - 将内存分为一块较大的eden空间和2块较少的survivor空间，每次使用eden和其中一块 survivor，当回收时将eden和survivor还存活的对象一次性拷贝到另外一块survivor空间上，然后清理掉eden和用过的survivor
   - Oracle Hotspot虚拟机默认eden和survivor的大小比例是8:1,也就是每次只有10%的内存是“浪费”的
   - 复制收集算法在对象存活率高的时候，效率有所下降。
   - 如果不想浪费50%的空间，就需要有而外的空间进行分配担保用于应付半区内存中所有对象都100%存活的极端情况，所以在老年代一般不能直接选用这种算法。
   - 只需要扫描存活的对象,效率更高
   - 不会产生碎片
   - 需要浪费额外的内存作为复制区
   - 复制算法非常适合生命周期比较短的对象,因为每次GC总能回收大部分的对象,复制的开销比较小
   - 根据IBM的专门研究，98%的Java对象只会存活1个GC周期,对这些对象很适合用复制算法。而且不用1: 1的划分工作区和复制区的空间，所以Eden：Survivor1：Survivor2 的默认比例为 8：1：1

   

4. 分代算法（Generational）

   - 当前商业虚拟机的垃圾收集都是采用“分代收集” (Generational Collecting)算法，根据对象不同的存活周期将内存划分为几块。
   - 一般是把Java堆分作**新生代和老年代**，这样就可以根据各个年代的特点采用最适当的收集算法，譬如新生代每次GC都有大批对象死去，只有少量存活，那就选用复制算法只需要付出少量存活对象的复制成本就可以完成收集。

   **4.1 年轻代(Young Generation)**

   - 新生成的对象都放在新生代。年轻代用复制算法进行GC (理论上,年轻代对象的生命周期非常短,所以适合复制算法)
   - 年轻代分三个区。一个Eden区，两个Survivor区(可以通过参数设置Survivor个数)。对象在Eden区中生成。当Eden区满时,还存活的对象将被复制到一个Survivor区,当这个Survivor区满时，此区的存活对象将被复制到另外一个Survivor区，当第二个Survivor区也满了的时候，从第一个Survivor区复制过来的并且此时还存活的对象,将被复制到老年代。2个Survivor是完全对称,轮流替换。
   - Eden和2个Survivor的缺省比例是8:1:1，也就是10%的空间会被浪费。可以根据GC log的信息调整大小的比例

   **4.2 老年代 (OId Generation)**  

   - 存放了经过一次或多次GC还存活的对象
   - 一般采用Mark-Sweep或者Mark-Compact算法进行GC
   - 有多种垃圾收集器可以选择。每种垃圾收集器可以看作一个GC算法的具体实现。可以根据具体应用的需求选用合适的垃圾收集器(追求吞吐量?追求最短的响应时间?）

   **4.3 永久代**

   - JDK8，后变成了元空间。
   - 永久代不属于堆（Heap），但是GC也会涉及到这个区域
   - 存放了每个Class的结构信息，包括常量池、字段描述、方法描述。与垃圾收集的Java对象关系不大

   

### 内存分配

1. 堆上分配

   大多数情况在Eden上分配，偶尔会直接在old上分配

   细节取决于GC的实现

2. 栈上分配

   原子类型的局部变量

   



### GC的时机

- 在分代模型的基础上, GC从时机上分为两种: Scavenge GC和Full GC

- Scavenge GC (Minor GC)

  触发时机:新对象生成时, Eden空间满了

  理论上Eden区大多数对象会在Scavenge GC回收,复制算法的执行效率会很高, Scavenge GC时间比较短

- Full GC

  对整个JVM进行整理,包括Young、Old和Perm

  主要的触发时机: 1) Old满了2) Perm满了3) system.gc()

  效率很低,尽量减少Full GC，一旦出现Full GC则会 STW 

### 垃圾回收器（Garbage Collector）

- 分代模型: GC的宏观愿景
- 垃圾回收器: GC的具体实现
- Hotspot JVM提供多种垃圾回收器,我们需要根据具体应用的需要采用不同的回收器
- 没有万能的垃圾回收器,每种垃圾回收器都有自己的适用场景



### 垃圾收集器的“并行”和“并发”

并行(Parallel) :指多个收集器的线程同时工作,但是用户线程处于等待状态

并发(Concurrent):指收集器在工作的同时,可以允许用户线程工作。

并发不代表解决了GC停顿的问题,在关键的步骤还是要停顿。比如在收集器标记垃圾的时候。但在清除垃圾的时候,用户线程可以和GC线程并发执行。



### Serial收集器

- 最早的收集器,单线程进行GC
- New和Old Generation都可以使用
- 在新生代,采用复制算法;在老年代,采用MarkCompact算法
- 因为是单线程GC,没有多线程切换的额外开销,简单实用
- Hotspot Client模式缺省的收集器
- ![](.\img\1581075605(1).jpg)



### ParNew收集器

- ParNew收集器就是Serial的多线程版本,除了使用多个收集线程外,其余行为包括算法、STW,对象分配规则、回收策略等都与Serial收集器一模一样。
- 对应的这种收集器是虚拟机运行在Server模式的默认新生代收集器,在单CPU的环境中, ParNew收集器并不会比Serial收集器有更好的效果
- Serial收集器在新生代的多线程版本
- 使用复制算法(因为针对新生代)
- 只有在多CPU的环境下,效率才会比Serial收集器高
- 可以通过-XX:ParallelGCThreads来控制GC线程数的多少。需要结合具体CPU的个数
- Server模式下新生代的缺省收集器



### Parallel Scavenge收集器

- Parallel Scavenge收集器也是一个多线程收集器,也是使用复制算法,但它的对象分配规则与回收策略都与ParNew收集器有所不同,它是以吞吐量最大化(即GC时间占总运行时间最小)为目标的收集器实现,它允许较长时间的STW换取总吞吐量最大化



### Serial Old收集器

- Serial Old是单线程收集器,使用标记一整理算法,是老年代的收集器



### Parallel Old收集器

- 老年代版本吞吐量优先收集器,使用多线程和标记一整理算法, JVM 1.6提供,在此之前,新生代使用了PS收集器的话,老年代除Serial Old外别无选择,因为PS无法与CMS收集器配合工作。

- Parallel Scavenge在老年代的实现
- 在JVM 1.6才出现Parallel Old
- 采用多线程, Mark-Compact算法
- 更注重吞吐量Parallel Scavenge + Parallel Old=高吞吐量,但GC停顿可能不理想
- ![](.\img\1581075971(1).jpg)



### CMS收集器

Concurrent Mark Sweep Garbage Collection 并发标记清除老年代的垃圾收集器，以获取最短回收停顿时间（Stop-The-World）为目标，多数应用B/S系统的服务器上。

CMS是基于“标记一清除”算法实现的,整个过程分为4个步骤：

- 初始标记(CMS initial mark）	

  会发生STW，标记老年代中那些直接被GC Roots引用或者被年轻代存活对象所引用的所有的对象，速度很快

  ![](.\img\1581303814(1).jpg)

- 并发标记(CMS concurrent mark）

  在这个阶段会和用户线程并发进行，Garbage Collector会遍历老年代，然后标记所有存活的对象，它会根据上个阶段找到的GC Roots遍历查找。并发标记阶段，它会与用户的应用程序并发运行。并不是老年代所有的存活对象都会被标记，因为在标记期间用户的程序可能会改变一些引用

  ![](.\img\1581304052(1).jpg)

- Concurrent Preclean	并发的预先清理	

  这也是一个并发阶段,与应用的线程并发运行,并不会stop应用的线程。在并发运行的过程中,一些对象的引用可能会发生变化,但是这种情况发生时, JVM会将包含这个对象的区域(Card)标记为 Dirty,这也就是Card Marking

  在pre-clean阶段,那些能够从Dirty对象到达的对象也会被标记,这个标记做完之后，dirty card 标记就会被清除了

  ![](.\img\1581304518(1).jpg)

  ![](.\img\1581304596(1).jpg)

  

- Concurrent Abortable Preclean	并发的可能失败的预先清理

  这也是一个并发阶段,但是同样不会影响用户的应用线程,这个阶段是为了尽量承担STW (stop-the-world)中最终标记阶段的工作。这个阶段持续时间依赖于很多的因素, ,由于这个阶段是在重复做很多相同的工作,直接满足一些条件(比如:重复迭代的次数、完成的工作量或者时钟时间等)

  

- 重新标记(CMS remark）	会发生STW

  这是第二个STW阶段，这个阶段的目标是标记老年代所有的存活对象,由于之前的阶段是并发执行的, gc线程可能跟不上应用程序的变化,为了完成标记老年代所有存活对象的目标, STW就非常有必要了。

  通常CMS的Final Remark阶段会在年轻代尽可能干净的时候运行, 日的是为了减少连续STW发生的可能性 (年轻代存活对象过多的话,也会导致老年代涉及的存活对象会很多)。这个阶段会比前面的几个阶段更复杂一些

- 并发清除(CMS concurrent sweep）

  这里不需要STW，它与业务线程并发运行，这个阶段会清除那些不再使用的对象，回收它们的占用空间为将来使用

  ![](.\img\1581317909(1).jpg)

  

- Concurrent Reset	并发重置

  这个阶段也是并发执行的，它会重设CMS内部的数据结构，为下次的GC做准备



并发标记阶段就是进行GC Roots Tracing的过程，该阶段不会阻碍业务线程的运行。

重新标记阶段则是为了修正并发标记期间因用户程序继续运作而导致标记产生变动的那一部分对象的标记记录,这个阶段的停顿时间一般会比初始标记阶段稍长一些,但远比并发标记的时间短

CMS收集器的运作步骤如下图所示,在整个过程中耗时最长的并发标记和并发清除过程收集器线程都可以与用户线程一起工作,因此,从总体上看, CMS收集器的内存回收过程是与用户线程一起并发执行的。

![](.\img\1581296308(1).jpg)

**优点：**

CMS通过将大量工作分散到并发处理阶段来减少STW时间，并发收集、低停顿。

**缺点：**

- CMS收集器对CPU资源非常敏感

- CMS收集器无法处理**浮动垃圾**( Floating Garbage ) ,可能出现"ConcurnetMode Failure"失败而导致另一次Full GC的产生。可能引发串行Full GC ，如果在应用中老年代增长不是太快,可以适当调高参数-XX: CMSInitiatingOccupancyFraction的值来提高触发百分比,以便降低内存回收次数从而获取更好的性能。要是CMS运行期间预留的内存无法满足程序需要时,虚拟机将启动后备预案:临时启用Serial Old收集器来重新进行老年代的垃圾收集,这样停顿时间就很长了。所以说参数-XX:CMSInitiatingOccupancyFraction，设置得太高很容易导致大量"Concurrent ModeFailure"失败,性能反而降低。

- 收集结束时会有大量空间碎片产生,空间碎片过多时,将会给大对象分配带来很大麻烦,往往出现老年代还有很大空间剩余,但是无法找到足够大的连续空间来分配当前对象,不得不提前进行一次Full GC. CMS收集器提供了一个.XX:+UseCMSCompactAtFullCollection开关参数(默认就是开启的) ,用于在CMS收集器顶不住要进行Full GC时开启内存碎片的合并整理过程,内存整理的过程是无法并发的,空间碎片问题没有了,但停顿时间不得不变长。

- 对于堆比较大的应用，GC的时间难以估计。





### G1收集器

**G1收集器堆结构**

**分区(Region)** : G1采取了不同的策略来解决并行、串行和CMS收集器的碎片、暂停时间不可控等问题--G1将整个堆物理上分成相同大小的分区（Region）

### ![](.\img\1581392335(1).jpg)

每个分区都可能是年轻代也可能是老年代,但是在同一时刻只能属于某个代。年轻代、幸存区、老年代这些概念还存在,**成为逻辑上的概念**,这样方便复用之前分代框架的逻辑。

在物理上不需要连续,则带来了额外的好处有的分区内垃圾对象特别多,有的分区内垃圾对象很少, G1会优先回收垃圾对象特别多的分区这样可以花费较少的时间来回收这些分区的垃圾,这也就是**G1名字的由来,即首先收集垃圾最多的分区。**

依然是在新生代满了的时候,对整个新生代进行回收-整个新生代中的对象,要么被回收、要么晋升,至于新生代也采取分区机制的原因,**则是因为这样跟老年代的策略统一,方便调整代的太小**。

G1还是一种带压缩的收集器,在回收老年代的分区时,是将存活的对象从一个分区拷贝到另一个可用分区,这个拷贝的过程就实现了局部的压缩。

**收集集合(CSet)** :一组可被回收的分区的集合。在CSet中存活的数据会在GC过程中被移动到另一个可用分区, CSet中的分区可以来自eden空间、survivor空间、或者老年代。 

**已记忆集合(RSet) :** RSet记录了其他 Region中的对象引用本Region中对象的关系,属于points-into结构(谁引用了我的对象) 。 RSet的价值在于使得垃圾收集器不需要扫描整个堆找到谁引用了当前分区中的对象,只需要扫描RSet即可

Region1和Region3中的对象都引用了Region2中的对象，因此在Regison2的RSet中记录了这两个引用。

![](.\img\1581393065(1).jpg)

G1 GC是在points-out的card table之上再加了一层结构来构成points-into RSet:每个region会记录下到底哪些别的region有指向自己的指针,而这些指针分别在哪些card的范围内。

这个RSet其实是一个hash table, key是别的region的起始地址, value是一个集合,里面的元素是card table的index。举例来说,如果region A的RSet里有一项的key是region B,value里有index为1234的card,它的意思就是region B的card里有引用指向region A。所以对region A来说,该RSet记录的是points-into的关系;而card table仍然记录了points-out的关系

Snapshot-At-The-Beginning(SATB):SATB是G1 GC在并发标记阶段使用的增量式的标记算法。

并发标记是并发多线程的,但并发线程在同一时刻只扫描一个分区

G1使用了gc停顿可预测的模型,来满足用户设定的gc停顿时间,根据用户设定的目标时间, G1会自动地选择哪些region要清除,一次清除多少个region

G1从多个region中复制存活的对象,然后集中放入一个region中,同时整理、清除内存(copying收集算法)



### G1 GC模式

G1提供了两种GC模式, Young GC和Mixed GC，两种都是需要Stop The World的

 **Young GC**：选定所有年轻代里的Region。通过控制年轻代的Region个数,即年轻代内存大小,来控制Young GC的时间开销。

**Mixed GC**：选定所有年轻代里的Region,外加根据global concurrent marking统计得出收集收益高的若干老年代Region。在用户指定的开销目标范围内尽可能选择收益高的老年代Region ，Mixed GC不是Full GC，**它只能回收部分老年代的Region,**如果Mixed GC实在无法跟上程序分配内存的速度,导致老年代填满无法继续进行Mixed GC,就会使用serial old GC (Full GC)来收集整个GC heap。**所以本质上, G1是不提供Full GC的。** 

**Global Concurrent Marking**的执行过程类似于CMS，但是不同的是，在G1 GC中，它主要是为**Mixed GC提供标记服务的**，并不是一次GC过程的一个必须环节。它主要有如下四个步骤：

- 初始标记(initial mark, STW) :它标记了从GC Root开始直接可达的对象。
- 并发标记(Concurrent Marking) :这个阶段从 GC Root开始对heap中的对象进行标记,标记线程与应用程序线程并发执行,并且收集各个 Region的存活对象信息。
- 重新标记(Remark, STW) :标记那些在并发标记阶段发生变化的对象,将被回收。
- 清理(Cleanup) :清除空Region (没有存活对象的) ,加入到free list。

第一阶段initial mark是共用了Young GC的暂停,这是因为他们可以复用root scan操作,所以可以说global concurrent marking是伴随Young GC而发生的。

第四阶段Cleanup只是回收了没有存活对象的Region,所以它并不需要STW



### G1在运行过程中的主要模式

- Young GC（不同于CMS）

  G1 YGC在Eden充满时触发，在回收之后所有之前属于Eden的区块全部变成空白，即不属于任何一个分区（Eden、Survivor、Old），之前的对象会移动到Survivor或Old区中。

  Young GC主要是对Eden区进行GC,它在Eden空间耗尽时会被触发。在这种情况下,Eden空间的数据移动到Survivor空间中,如果Survivor空间不够, Eden空间的部分数据会直接晋升到老年代空间。Survivor区的数据移动到新的Survivor区中,也有部分数据晋升到老年代空间中。最终Eden空间的数据为空, GC完成工作,应用线程继续执行

  - 阶段1:根扫描静态和本地对象被扫描

  - 阶段2:更新RS处理dirty card队列更新RS

  - 阶段3:处理RS检测从年轻代指向老年代的对象

  - 阶段4:对象拷贝拷贝存活的对象到survivorlold区域

  - 阶段5:处理引用队列软引用,弱引用,虚引用处理

- Mixed GC

  **MIixed GC不仅进行正常的新生代垃圾收集，同时也回收部分后台扫描线程标记的收益高的老年代分区，其中后台扫描线程是指Global Concurrent Marking**

  它的GC步骤分为两步:

  - **全局并发标记**	global concurrent marking

    在G1 GC中, global concurrent marking主要是为Mixed GC提供标记服务的,并不是一次GC过程的一个必须环节。globalconcurrent marking的执行过程分为四个步骤

  - **拷贝存活对象**	evacuation

  由一些参数控制，另外也控制着哪些老年代Region会被选入CSet（收集集合）

  - **G1 HeapWastePercent：**在globalconcurrent marking结束之后,我们可以知道old gen regions中有多少空间要被回收,在每次YGC之后和再次发生Mixed GC之前,会检查垃圾占比是否达到此参数,只有达到了,下次才会发生Mixed GC，先5次YGC后发生一次MixGC，下次可能就自动调整到8次YGC才发生一次MixGC。

  - **G1MixedGCLiveThresholdPercent**: oldgeneration region中的存活对象的占比,只有在此参数之下,才会被选入CSet

  - **G1MixedGCCountTarget**:一次global concurrent marking之后,最多执行Mixed GC的次数

  - **G1OldCSetRegionThresholdPercent**:次Mixed GC中能被选入CSet的最多old generation region数量 

  - ![](.\img\1581515060(1).jpg)

  - |                参数                |                             含义                             |
    | :--------------------------------: | :----------------------------------------------------------: |
    |       -XX:G1HeapReoionSize=n       |                  设置Region大小，并非最终值                  |
    |        -XX:MaxGCPauseMillis        | 设置G1收集过程目标时间，默认值200ms，不是硬性条件，需要多次尝试 |
    |        -XX:G1NewSizePercent        |                    新生代最小值，默认值5%                    |
    |       -XX:G1MaxNewSizePercem       |                   新生代最大值，默认值60%                    |
    |       -XX:ParallelGCThreads        |                   STW期间，并行GC的线程数                    |
    |        -XX:ConcGCThreads=n         |                并发标记阶段，并行执行的线程数                |
    | -XX:InitiatingHeapOccupancyPercent | 设置触发标记周期的Java堆占用率阀值,默认值是45%,这里的java堆占比指的是non young capacity bytes,包括old+humongous |

    

  G1算法将堆划分为若干个区域(Region) ,它仍然属于分代收集器。不过,这些区域的一部分包含新生代,新生代的垃圾收集依然采用暂停所有应用线程的方式,将存活对象拷贝到老年代或者 Survivor空间。老年代也分成很多区域, G1收集器通过将对象从一个区域复制到另外一个区域,完成了清理工作。这就意味着,在正常的处理过程中, G1完成了堆的压缩(至少是部分堆的压缩) ,这样也就不会有CMS内存碎片问题的存在了

  **Humongous区域：**在G1中,还有一种特殊的区域, 叫Humongous区域。如果一个对象占用的空间达到或是超过了分区容量50%以上, G1收集器就认为这是一个巨型对象。这些巨型对象,默认直接会被分配在老年代,但是如果它是一个短期存在的巨型对象,就会对垃圾收集器造成负面影响。为了解决这个问题, G1划分了一个Humongous区,它用来专门 存放巨型对象。如果一个H区装不下一个巨型对象,那么G1会寻找连续的H分区来存储。为了能找到连续的H区,有时候不得不启动Full GC

  

  在CMS中,也有RSet的概念,在老年代中有一块区域用来记录指向新生代的引用。这是一种point-out,在进行Young GC时,扫描根时,仅仅需要扫描这一块区域,而不需要扫描整个老年代。

  但在G1中,并没有使用point-out,这是由于一个分区太小,分区数量太多,如果是用point-out的话,会造成大量的扫描浪费,有些根本不需要GC的分区引用也扫描了。于是G1中使用point-in来解决。point-in的意思是哪些分区引用了当前分区中的对象。这样,仅仅将这些对象当做根来扫描就避免了无效的扫描。由于新生代有多个,那么我们需要在新生代之间记录引用吗?这是不必要的,原因在于每次GC时,所有新生代都会被扫描**,所以只需要记录老年代到新生代之间的引用即可**

  需要注意的是,如果引用的对象很多,赋值器需要对每个引用做处理,赋值器开销会很大,为了解决赋值器开销这个问题,在G1中又引入了另外一个概念,卡表 (Card Table)。一个Card Table将一个分区在逻辑上划分为固定大小的连续区域,每个区域称之为卡。卡通常较小,介于128到512字节之间。Card Table通常为字节数组,由Card的素引(即数组下标)来标识每个分区的空间地址

  情况下,每个卡都未被引用。当一个地址空间被引用时,这个地址空间对应的数组索引的值被标记为'0',即标记为脏被引用,此外RSet也将这个数组下标记录下来。一般情况下,这个RSet其实是一个Hash Table, Key是别的Region的起始地址, Value是一个集合,里面的元素是CardTable的Index

  

G1在运行过程中的主要模式：

- YGC（不同于CMS）、

  G1 YGC在Eden充满时触发，在回收之后所有之前属于Eden的区块全部变成空白，即不属于任何一个分区（Eden、Survivor、Old）

- 并发阶段

  **全局并发标记**	global concurrent marking

  在G1 GC中, global concurrent marking主要是为Mixed GC提供标记服务的,并不是一次GC过程的一个必须环节。globalconcurrent marking的执行过程分为四个步骤

- 混合模式

  Mix GC，由一些参数控制，另外也控制着哪些老年代Region会被选入CSet（收集集合）

- Full GC

  （一般是G1出现问题时发生，使用serialize Old  GC 进行Full GC）

  

  



### 三色标记算法

提到并发标记,我们不得不了解并发标记的三色标记算法。它是描述追踪式回收器的一种有效的方法,利用它可以推演回收器的正确性

我们将对象分成三种类型

- 黑色:根对象,或者该对象与它的子对象都被扫描过(对象被标记了,且它的所有field也被标记完了)
- 灰色:对象本身被扫描,但还没扫描完该对象中的子对象(它的field还没有被标记或标记完)
- 白色:未被扫描对象,扫描完成所有对象之后,最终为白色的为不可达对象,即垃圾对象(对象没有被标记到)

三色标记算法的过程中会两种问题：

- 误标问题，这个是由于在并发标记时，有引用指向该对象，所以进行了标记，但与并发标记同时执行的业务线程中，取消了指向该对象，所以该对象在此次垃圾回收中应该要被回收掉，但由于之前进行了标记，只能等待下次垃圾回收该对象，这个对象就成为了浮动垃圾。

- 漏标问题，这个是很严重的，这个又细分为两个情况：

  - 在GC过程中分配的对象

    **如何找到在GC过程中分配的对象呢?**每个region记录着两个top-at-mark-start (TAMS指针,分别为prevTAMS和nextTAMS,在TAMS以上的对象就是新分配的,因而被视为隐式marked。

    通过这种方式我们就找到了在GC过程中新分配的对象,并把这些对象认为是活的对象。

  - 在GC过程中引用发生变化

    G1给出的解决办法是通过Write Barrier。Write Barrier就是对引用字段进行赋值做了额外处理。通过Write Barrier就可以了解到哪些引用对象发生了什么样的变化，在并发标记的时候所有被改变的对象入队(**在write barrier里把所有旧的引用所指向的对象都变成非白的)**

漏标的情况只会发生在白色对象中,且满足以下任意一个条件

- 并发标记时,应用线程给一个黑色对象的引用类型字段赋值了该白色对象

- 并发标记时,应用线程删除所有灰色对象到该白色对象的引用

解决办法：

- 对于第一种情况,利用post-write barrier,记录所有新增的引用关系,然后根据这些引用关系为根重新扫描一遍
- 对于第二种情况,利用pre-write barrier,将所有即将被删除的引用关系的旧引用记录下来,最后以这些旧引用为根重新扫描一遍

使用SATB解决三色标记算法的漏标问题，被标记的对象，说明这个对象是可达的，不会被清除。

SATB的三个步骤：

- 在开始标记的时候生成一个快照图,标记存活对象

- 在并发标记的时候所有被改变的对象入队(**在write barrier里把所有旧的引用所指向的对象都变成非白的)**

- 可能存在浮动垃圾,将在下次被收集

mark的过程就是遍历heap标记live object的过程,采用的是三色标记算法,这三种颜色为white(表示还未访问到)、gray(访问到但是它用到的引用还没有完全扫描)、 black(访问到而且其用到的引用已经完全扫描完)

整个三色标记算法就是从GC roots出发遍历heap,针对可达对象先标记white为gray,然后再标记 gray为black;遍历完成之后所有可达对象都是 black的,所有white都是可以回收的

出现漏标的情况：

- 对black新引用了一个white对象,然后又从 gray对象中删除了对该white对象的引用,这样会造成了该white对象漏标记

- 对black新引用了一个white对象,然后从 gray对象删了一个引用该white对象的white对象,这样也会造成了该white对象漏标记

- 对black新引用了一个刚new出来的white对象,没有其他gray对象引用该white对象,这样也会造成了该white对象漏标记



对于三色算法在concurrent的时候可能产生的漏标记问题, SATB在marking阶段中**对于从gray对象移除的目标引用对象标记为gray,对于black引用的新产生的对象标记为black**;由于是在开始的时候进行snapshot, 因而i可能存在Floating Garbage

SATB是维持并发GC的一种手段。G1并发的基础就是SATB, SATB可以理解成在GC开始之前对堆内存里的对象做一次快照,此时活的对象就认为是活的,从而形成一个对象图。

在GC收集的时候,新生代的对象也认为是活的对象,除此之外其他不可达的对象都认为是垃圾对象。



**为什么要采用分区机制？**

为老年代设置分区的目的是老年代里有的分区垃圾多,有的分区垃圾少,这样在回收的时候可以专注于 的分区,这也是G1（Garbage First）名称的由来。

不过这个机制并不适合新生代垃圾收集,因为新生代的垃圾收集算法是复制算法,但是**新生代也使用了分区机制主要是因为便于代大小的调整**



### 停顿预测模型

G1收集器突出表现出来的一点是通过一个停顿预测模型根据用户配置的停顿时间来选择CSet的大小,从而达到用户期待的应用程序暂停时间。

通过-XX:MaxGCPauseMillis参数来设置。这一点有点类似于ParallelScavenge收集器。关于停顿时间的设置并不是越短越好。

设置的时间越短意味着每次收集的CSet越小,导致垃圾逐步积累变多,最终不得不退化成Serial GC;停顿时间设置的过长,那么会导致每次都会产生长时间的停顿,影响了程序对外的响应时间

### G1的收集模式

Young GC:收集年轻代里的Region

Mixed GC:年轻代的所有有Region +全局并发标记阶段选出的收益高的Region

无论是Young GC还是Mixed GC都只是并发拷贝的阶段

分代G1模式下选择CSet有两种子模式,分别对应

Young GC和Mixed GC: Young GC: CSet就是所有年轻代里面的 Region

Mixed GC: CSet是所有年轻代里的Region加上在全局并发标记阶段标记出来的收益高的Region

G1的运行过程是这样的:会在Young GC和Mixed GC之间不断地切换运行,同时定期地做全局并发标记,**在实在赶不上对象创建速度的情况下使用Full GC(Serial GC)**

初始标记是在Young GC上执行的,在进行全局并发标记的时候不会做Mixed GC,在做MixedGC的时候也不会启动初始标记阶段。

当Mixed GC赶不上对象产生的速度的时候就退化成Full GC,这一点是需要重点调优的地方



### G1最佳实践

不断调优暂停时间指标

通过-XX:MaxGCPauseMillis-x可以设置启动应用程序暂停的时间, G1在运行的时候会根据这个参数选择CSet来满足响应时间的设置。一般情况下这个值设置到100ms或者200ms都是可以的(不同情况下会不一样),但如果设置成50ms就不太合理。暂停时间设置的太短,就会导致出现G1跟不上垃圾产生的速度。最终退化成Full GC。所以对这个参数的调优是一个持续的过程,逐步调整到最佳状态。



### G1 VS CMS

- 对比Parallel Scavenge(基于copying) Parallel Old收集器(基于mark-compact sweep), Parallel会对整个区域做整理导致 gc停顿会比较长,而G1只是特定地整理几个region。
- G1并非一个实时的收集器,与parallel Scavenge一样,对gc停顿时间的设置并不绝对生效,只是G1有较高的几率保证不超过设定的gc停顿时间。与之前的gc收集器对比, G1会根据用户设定的gc停顿时间,智能评估哪几个region需要被回收可以满足用户的设定



- G1在压缩空间方面有优势，并且不会造成内存碎片

  对比使用mark-sweep的CMS，G1采用复制算法，将对象从一个分区复制到另外一个分区，可以让空间的利用率更大，不会有碎片的产生。

- G1通过将内存空间分成区域(Region)的方式避免内存碎片问题

- Eden、Survivor、 Old区不再固定，在不同时间，同一块分区可以属于不同的角色，在内存使用效率上来说更灵活

- G1可以通过设置预期停顿时间(Pause Time)来控制垃圾收集时间,避免应用雪崩现象 

  通过对预测模型的分析，在指定的时间内，尽可能的回收最需要进行垃圾回收的分区。

- G1在回收内存后会马上同时做合并空闲内存的工作,而 CMS默认是在STW (stop the world)的时候做 

- G1会在Young GC中使用，而CMS只能在Old区使用



### G1的适合场景

服务端多核CPU、JVM内存占用较大的应用

应用在运行过程中会产生大量的内存碎片，需要经常压缩空间

想要更可控，可预期的GC停顿周期，防止高并发下应用的雪崩现象



### Java内存泄漏的经典原因

- 对象定义在错误的范围（Wrong Scope），本应定义为局部变量却定义为成员变量
- 异常（Exception）处理不当，没有关闭资源
- 集合数据使用管理不当，减少底层由数组实现集合的resize()



### GC参数

- -verbose:gc	打印GC信息	
- -Xms20m	堆的初始大小
- -Xmx20m	堆的最大空间
- -Xmn10m	堆的新生代的大小
- -XX:+PrintGCDetails	打印GC的详细信息
- -XX:SurvivorRatio=8	eden：survivior1：survivor=8：1：1
- -XX:+PrintCommandLineFlags	打印程序启动的JVM参数
- -XX:+UseParallelGC 使用并行垃圾收集器
- -XX:+UseConcMarkSweepGC	使用并发标记清除的方式进行老年代的GC
- -XX:+UseParNewGC	使用ParNew收集器管理新生代的GC



- -XX:+UseSerialGC	使用串行垃圾收集器

- -XX:PretenureSizeThreshold=4194304	当对象大小超过4M时，会直接分配在老年代，需要搭配串行垃圾收集器使用

  

- -XX:MaxTenuringThreshold=5	设置对象晋升（Promote）到老年代的阈值最大值，会根据实际运行情况进行动态降低。G1中默认默认为15，CMS中默认为6

- -XX:+PrintTenuringDistribution	打印对象年龄变化及晋升阈值变化

  Desired survivor size 1048576 bytes, new threshold 5 (max 5)

- -XX:TargetSurvivorRatio=60	Survivor的已使用率超过60%会重新计算对象晋升阈值

- -XX:+PrintGCDateStamps	打印每次GC的时间

  

- -XX:UseG1GC	使用G1垃圾收集器

- -XX:MaxGCPauseMillis=200m	G1垃圾收集器的最大停顿时间为200毫秒

  

  经历了多次GC后,存活的对象会在From Survivor与To survivor之间来回存放，而这里面的一个前提则是这两个空间有足够的大小来存放这些数据，在GC算法中会计算每个对象年龄的大小，如果达到某个年龄后发现总大小已经大于了survivor空间的50%，那么这时就需要调整阈值，不能再继续等到默认的15次GC后才完成晋升，因为这样会导致survivor空间不足，所以需要调整阈值，让这些存活对象尽快完成晋升。



**实例解析**

JDK1.8 中新生代默认采用Parallel Scavenge收集器，老年代默认采用Parallel Old收集器

[GC (System.gc()) [PSYoungGen: 7829K->760K(9216K)] 7829K->6912K(19456K), 0.0031114 secs] [Times: user=0.00 sys=0.00, real=0.00 secs] 

- 调用GC的原因：（Allocation Failure 分配失败/System.gc()调用GC方法）

- PSYoungGen：代表新生代采用Parallel Scavenge收集器
- 7829K	清理之前新生代的使用空间
- 760K	清理之后新生代的使用空间
- 9216K	新生代总大小，因为采用的是复制算法，所以有1m的survivor始终无法使用
- 7829K	清理之前堆的使用空间
- 6912K	清理之后堆的使用空间
- 19456K	堆的总大小，新生代（eden+survivor1）+老年代
-  0.0031114 secs	此次垃圾回收所用时间
- ParOldGen：代表老年代采用Parallel Old收集器



## 2.垃圾回收

### 2.1 GC如何确定垃圾

**什么是垃圾？**

不会被使用到的对象就是垃圾， 可能是一个没有引用指向的对象，也可能是两个互相引用的对象，还可能是环形引用的对象。

**如何确定垃圾？**

引用计数：会用循环引用的问题。

正向可达：从roots对象计算可以到达的对象，到不了的对象可以被认为是垃圾。main方法中创建的对象都是roots对象



### 2.2 垃圾收集算法

#### 2.2.1 Mark-Sweep 标记清除

![](.\img\1577455245(1).jpg)

标记的地方，代表下次创建的对象时可以使用，而不是把该位置保存的值擦除。

缺点：内存碎片化，如果再有大的对象需要被创建，则会进行fullGC，将未被清除的对象压缩复制在一个区域，将碎片化的内存空间整理到一起。

#### 2.2.2 Copying 复制

![](.\img\1577455659(1).jpg)

缺点：浪费内存，4G的话只能2G能用

#### 2.2.3 Mark-Compact 标记压缩

![](.\img\1577456026(1).jpg)

比Copy效率低些。

**JVM采用分代算法**

新生代中eden与survive之间使用Copy算法，存活对象少，占用内存空间也不大，效率也高

老年代采用的是 Mark-Compact 算法，垃圾少







JVM参数设置
    -Xms  初始堆大小
    -Xmx  最大堆大小
    -Xss  线程的内存空间
    -XX:NewSize=n 设置年轻代大小
    -XX:NewRatio=n 设置年轻代和老年代的比值，如n为3，表示年轻代：年老代=1：3，年轻代占整个年轻代年老代和的1/4
    -XX:SurvivorRatio=n /sə'vaɪvə/ 年轻代中Eden区与两个Survivor区的比值，注意Survivor区有两个，n=3表示Eden:Survivor=3：2，一个Survivor占年轻代的1/5
    -XX:MaxPermSize=n 设置持久代大小
    收集器设置
    -XX:+UseSerialGC    设置串行收集器 
    -XX:+UseParallelCC  设置并行收集器  高吞吐量
    -XX:+UseParalledOldGC   设置并行年老代收集器
    -XX:UseConcMarkSweepGC  设置并发收集器
    垃圾回收统计信息
    -XX:+PrintGC
    -XX:+Printetails
    -XX:+PrintGCTimeStamps
    -Xloggc:filename
    并行收集器设置

调优总结
    年轻代大小选择
        响应时间优先的应用：尽可能设大，年轻代收集发生的频率也是最小的，同时减少到达年老代的对象
JVM堆结构图及分代
    JVM内存分代策略
        Java虚拟机根据对象存活的周期不同，把堆内存划分为几块，一般分为新生代、老年代和永久代（对HotSpot而言）
    为什么要分代？
            堆内存是虚拟机管理的内存中最大的一块，也是垃圾回收最频繁的一块区域，我们程序所有的对象实例都存放在堆内存中。
        给堆内存分代是为了提高对象内存分配和垃圾回收的效率。试想一下，如果堆内存没有区域划分，
        所有的新创建的对象和生命周期很长的对象放在一起，随着程序的执行，堆内存需要频繁进行垃圾收集，
        而每次回收都要遍历所有的对象，会严重影响我们的GC效率。
            有了内存分代，新创建的对象会在新生代中分配内存，经过多次回收仍然存活下来的对象存放在老年代中，
        静态属性、类信息等存放在永久代中，新生代中的对象存活时间短，只需要在新生代区域中频繁进行GC，
        老年代中对象生命周期长，内存回收的频率相对较低，不需要频繁进行回收，永久代中回收效果太差，一般不进行垃圾回收，
        还可以根据不同年代的特点采用合适的垃圾收集算法。分代收集大大提升了收集效率，这些都是内存分代带来的好处。
    新生代
        新生对象优先存放新生代，常规应用进行一次垃圾回收一般会回收70%-95%，回收率很高。
        默认比例   Eden：Survivor0：Survivor1=8:1:1 
        HotSpot 采用复制算法来回收新生代，由于热点探测技术替代之前的虚拟机


​        

    Java HotSpot Client VM (-client),为在客户端环境中减少启动时间而优化;
    Java HotSpot Server VM (-server),为在服务器环境中最大程度提高执行速度而设计；

垃圾回收机制
    寻找需要回收的对象，垃圾回收算法
        1.引用计数器
            比较古老的回收算法，原理是此对象增加一个引用，即增加一个计数，减少一个引用，计数器减一，垃圾回收时只收集计数器为0的对象，此算法无法处理循环引用的对象
        2.复制算法
            类似于新生代的处理，将内存空间划为两个相等的区域，每次只使用其中一个区域，垃圾回收时，将正在使用的对象复制到另一区域，复制过去可以进行内存整理，不会出现碎片问题
        3.标记清除法(Mark-Sweep)
            此算法分两阶段，第一阶段从引用根节点开始标记所有被引用的对象，第二阶段，把未被标记的对象清除，此算法需要暂停应用，且会产生内存碎片
        4.标记整理算法

    通用的分代垃圾回收机制：
        根据不同的生命周期将对象划分为 年轻代，年老代，持久代。
        JVM将堆内存划分为 Eden Survivor Tenured/Old空间
        年轻代在 Eden Survivor1 Survivor2   年老代在 Tenured/Old   持久代在方法区
    垃圾回收过程：
        1.新创建的对象，绝大多数都会存储在Eden中
        2.当Eden满了(达到一定比例)不能创建新对象，则触发垃圾回收，通过算法将无用对象清理掉
          然后剩余对象复制到某个Survivor，如Survivor1，同时清空Eden区
        3.当Eden再次满了，会对Survivor1进行清理，然后将剩余的对象存到另一个Survivor中，如Survivor2
          同时将Eden区中不能清空的对象，也复制到Survivor2中，保证Eden和Survivor1,均被清空
        4.重复多次（默认15次）Survivor中没有被清理的对象，则会复制到老年代Tenured/Old
        5.当Tenured/Old满了，则会触发一个一次完整地垃圾回收（FullGC），清理年轻代，老年代，成本较高，会对系统性能产生影响


​    



## 2.G1

garbage first

STW：垃圾回收需要的停顿，G1可以使垃圾回收可控。可以使内存不连续

![](.\img\1577526717.jpg)

追求响应时间：

-  XX：MaxGCPauseMillis 200  将GC的时间控制在200毫秒
-  可以对STW进行控制

灵活：

- 分Region回收
- 优先回收花费时间少、垃圾比例高的Region

每个Region多大

- 取值 

  ​	1，2，4，8，16，32 单位m

- 手工指定 `-XX：G1HeapRegionSize` 

对象何时进入老年代

1. 超过 `XX:MaxTenuringThreshold` 指定次数（YGC）

   `- Parallel Sacvenge 15`

   `- CMS 6`

   `- G115`

2. 动态年龄

   s1 -> s2 超过50%

   把年龄最大的放入

​	



### 判断对象存活的算法

- 引用计数法

- 根搜索算法（GC Root Trace）

  从GC Root出发去寻找能被追踪到的对象，可作为GC Root的对象有：

  - 方法区：类静态属性引用的对象
  - 方法区：常量引用的对象
  - 虚拟机栈（本地变量表）中引用的对象
  - 本地方法栈JNI（Java Native Interface）中引用的对象

- 

  



### 分代收集

![](.\img\1581950850(1).jpg)

**垃圾收集中的并发和并行**：

- 并行：多个线程同时进行垃圾收集
- 并发：垃圾收集线程和应用线程同时进行

**新生代收集器**

- **Serial**
  复制算法，单线程，简单高效，适合内存不大的单核CPU

- **ParNew**

  复制算法，并行多线程收集器，ParNew是Serial的多线程版本，搭配CMS垃圾收集器的首选，更关注响应时间

- **Parallel Scavenge**

  复制算法，并行多线程收集器，类似于ParNew，更加关注吞吐量，达到一个可控制的吞吐量，本身是Server级别多核机器的默认GC方式，适合后台运算不需要太多交互任务

**老年代收集器**

- **Serial Old**

  标记整理算法，单线程，Client模式下虚拟机

- **Parallel Old**

  标记整理算法，并行的多线程收集器，配合Parallel Scavenge的面向吞吐量的特性而开发的对应组合，注重吞吐量以及CPU资源敏感的场景适用

- **CMS**

  标记清除算法，并行与并发收集器，尽可能的缩短垃圾收集时用户线程的停止时间，

  缺点：

  - 内存碎片
  - 需要更多的CPU资源
  - 浮动垃圾问题，需要更大的堆空间

  重视服务的响应速度、系统停顿时间和用户体验的互联网网站或者B/S系统

  -XX:+UseConcMarkSweepGC，表示新生代使用ParaNew，老年代使用CMS

  -XX:CMSlnitialOccupyFraction，当老年代空间使用超过这个值的时候启动收集默认为92%

  出现错误: "Concurrent Mode Failure",启动Serial old收集器。

  -XX:UseCMSCompactAtFullcollection，(默认开启)需要进行FullGC的时候开启内存碎片的整理,无法并发。

  -XX:CMSFullGCsBeforeCompaction，（默认为0）设置多少次不压缩的FullGC后来一次压缩的Full GC

  

  ![](.\img\1581953695(1).jpg)

![](.\img\1581953930(1).jpg)

**G1**

G1收集器，会对新生代和老年代都进行垃圾收集，标记整理+复制算法，没有空间碎片，并行与并发收集器，JDK7引入，JDK9采用为默认收集器，采用分区回收的思想，在不牺牲吞吐量的前提先完成低停顿的内存回收，可指定预计的停顿时间，面向服务端的垃圾收集器，目标为取代CMS

![](.\img\1581954828(1).jpg)

![](.\img\1.png)

**G1 并发标记周期**？？

- 初始标记：时间很短暂，仅仅标记一下GC Roots，能直接关联到的对象，速度很快，会产生STW（全局停顿），都会有一次新生代的GC
- 根区域扫描：扫描survivor区可以直接到达的老年代区域
- 并发标记阶段：
- 重新标记阶段：
- 独占清理：
- 并发清理：

-XX:MaxGCPauseMills	指定目标最大停顿时间，G1会尝试调整新生代和老年代region的比例

-XX:ParllerGCThreads	指定GC的工作线程数



**ZGC**

JDK11中提供的一种可扩展的低延迟垃圾收集器

处理TB级别的堆

GC时间不超过10ms

与G1相比，应用吞吐量的降低不超过15%

有色指针和加载屏障



### 内存分配与回收策略

- 对象优先在Eden区分配

  当Eden区没用足够空间进行分配时，虚拟机将发起一次Minor GC，

- 大对象直接进入老年代，-XX:PretenureSizeThreshold  超过该参数则直接在老年代分配，缺省为0 ，表示不会直接分配在老年代，这个参数值对Serial和ParNew两款收集器有效。

- 长期存活的对象将进入老年代	-XX:MaxTenuringThreshold 设定新生代进入老年代的年龄，默认15

- 动态对象年龄判断	

  虚拟机并不要求对象年龄必须达到最大值才能晋升老年代，JVM会动态检查Survivor区域中的对象年龄，如果Survivor区中相同年龄的所有对象的大小总占比超过Survivor空间的一半，那么该年龄及以上的对象就可以直接进入老年代了。

- 空间分配担保

  当Eden区满的时候，要发生Minor GC之前，虚拟机会先检查老年代最大可用的连续空间是否大于新生代所有对象的总空间，因为在最坏的情况下新生代的所有对象都不会被回收，需要全部复制到老年代。如果老年代的空间足够，那么执行Minor GC不会出任何问题；在不够的情况下，并不一定会触发Full GC，因为新生代可能会回收一部分对象，剩余的对象，刚好能被老年代所容纳（这样本次Minor GC就不会引发Full GC），当然也有容纳不下剩余对象的可能，这个就存在一定概率。设置空间分配担保（-XX:+HandlePromotionFailure）就允许这个概率的发生，不然在老年代连续空间不足的情况下直接进行Full GC。

  空间分配担保的方式是通过计算之前每次晋升到老年代对象平均大小，如果大于当前老年代的连续空间，那么会尝试着进行一次Minor GC；小于的话重新发起Full GC。在JDK6 Update24 之后的规则变为只要老年代的连续空间大于新生代对象总大小或者历次晋升的平均年龄大小就会进行Minor GC，否则进行Full GC。

  

## 3.JVM配置参数

### 3.1 Trace追踪参数

`-XX:+PrintGC`

![](.\img\1577605259(1).jpg)

`-Xloggc:D:/gc.log` 

`-XX:+PrintGCDetails` 这个是每次gc都会打印的，只是程序结束后才打印详细的堆信息

`-XX:+PrintHeapAtGC`  打印GC前后的堆详细信息

`-XX:+TraceClassLoading`  监控类的加载

![](.\img\1577610632(1).jpg)

`-XX:+PrintClassHistogram`  在程序运行中，按下Ctrl+Break，打印各个类的信息

![](.\img\1577610846(1).jpg)

### 3.2 堆的分配参数

`-Xms  -Xmx`  最小堆和最大堆

-Xms和-Xmx应该保持多少比例，使系统性能最佳?

![](.\img\1577610976(1).jpg)

`-Xmn` 	设置新生代大具体大小

`-XX:NewRatio` 	新生代比老年代的比例

- 年轻代（eden+survivor×2）：老年代（tenured，不含永久区）的比值

- -XX:NewRatio=4 年轻代：老年代 = 1：4 ，年轻代占堆的1/5

`-XX:SurvivorRatio`    表示一个eden区和Survivor区的比值

- -XX:SurvivorRatio=8，eden：一个Survivor：=8：1，所以两个Survivor占新生代的20%

`-XX:+HeapDumpOnOutOfMemoryError`	OOM时导出堆到文件

`-XX:HeapDumpPath`		导出OOM的路径  -XX:HeapDumpPath=d:/

![](.\img\1577613097(1).jpg)



`-XX:OnOutOfMemoryError` 		在OOM时，执行一个脚本

![](.\img\1577613232(1).jpg)

**堆参数设置小结**

![](.\img\1577613314(1).jpg)

### 3.3 永久区分配参数

`-XX:PermSize` 	`-XX:MaxPermSize`

设置永久区的初始空间和最大空间

JDK1.8后失效，采用-XX:MaxMetaspaceSize=10m来设定最大元空间大小



### 3.4 栈大小分配

- -Xss256k

- 通常只有几百k
- 决定了函数调用的深度
- 每个线程都有独立的栈空间
- 局部变量、参数分配在栈上



## 4.GC算法与种类

### GC概念

Garbage Collection 垃圾回收，防止内存泄漏

GC对象是堆空间和永久区



### GC算法

**引用可达计数法**

- 引用和去引用伴随加法和减法，影响性能
- 很难处理循环引用

**标记-清除**

从根节点开始对可达对象做一次标记，清除阶段同一清除

**复制算法**

- 将内存分为两块，每次只能使用其中一块，算法执行时，会将存活对象复制到另外一端空间
- 浪费了一半空间

**标记-压缩**

从根节点开始对可达对象做一次标记，将所有存活对象复制到内存的一段，清除这个区域外的垃圾对象。



### 分代思想

根据对象的存活周期长短分为新生代和老年代。

少量对象存活，适合复制算法。

大量对象存活，适合标记清理或标记压缩



### 可触及性

- 可触及的

  从根节点可以触及到这个对象

- 可复活的

  一旦所有引用被释放，就是可复活状态

  因为在finalize()方法中可能复活该对象，在finalize方法中对其进行引用的指向。

  ![](.\img\1577620326(1).jpg)

- 不可触及的

  早finalize()后，可能会进入不可触及的状态

  不可触及的对象不可能复活，可以被回收

**对于可触及性经验**

避免使用finalize()，操作不慎可能导致错误，可以使用try-catch-finally来替代

**根**

- 栈中引用的对象
- 方法区中静态成员或者常量引用的对象（全局对象）
- JNI方法栈中的对象

### Stop-The-World

- 简称STW，是Java中一种全局暂停的现象

- 所有Java代码停止，native代码还可以继续运行，可以理解为JVM中除GC外都处于挂起状态，不能再执行应用层面的事情，只进行GC。

- 多半由GC引起，也可能有其他原因：

  - Dump线程
  - 死锁检查
  - 堆Dump

  危害

  - 老年代的GC比较耗时，可能导致服务停止，没有响应，
  - 遇到HA系统，可能引起主备切换，严重危害生产环境。

## 5.GC参数

### 5.1 垃圾收集器选择

1. **Serial Collector（串行收集器）**

   -XX ：+UserSerialGC

   最古老，最稳点，单线程，可能会产生较长的停顿

   新生代、老年代使用串行回收

   新生代使用复制算法，老年代使用标记-压缩算法

   ![](.\img\1577628866(1).jpg)

   上边的GC是新生代的垃圾回收，FullGC是老年代的垃圾回收。

   

2. **ParNew Collector（并行收集器）**

   -XX:+UseParNewGC

   - 新生代并行
   - 老年代串行

   在串行收集器的基础上，将新生代的回收通过多线程来实现，多个线程执行复制算法，需要多核的支持老年代不变还是单线程串行。

   -XX:ParallelGCThreads 限制线程数量

   ![](.\img\1577629337(1).jpg)

3. **Paralle Collector（并行收集器）**

  类似于ParNew收集器

  新生代复制算法，老年代标记-压缩算法

  更加关注吞吐量。不过无论是串行还是并行都会有STW发生，JVM都会停下来。

  -XX:+UseParallelGC	使用ParallelGC收集器，新生代并行，老年代串行

  -XX:UseParallelOldGC	新生代和老年代都并行

  ![](.\img\1577629681(1).jpg)

  -XX:MaxGCPauseMills

  - 最大停顿时间，单位毫秒
  - GC尽量保证不超过

  停顿时间和吞吐量，就像时间和空间一样，尽量根据程序选择一个平衡点。

4. **CMS Collector（CMS收集器）**

  -XX:+UseConcMarkSweepGC

  Concurrent Mark Sweep 并发标记清除

  老年代使用标记-清除算法，与标记-压缩算法相比，少了将堆空间压缩的步骤，可以在JVM运行的时，与用户线程一起执行，并发阶段会占用系统资源，因此会降低吞吐量，且容易内存碎片化，好处是停顿时间短。  

  CMS运行过程比较复杂，着重实现了标记的过程，尽可能将全局停顿减小，但是无法消除。

  - 初始标记
    - 根可以直接关联到的对象
    - 速度快，但也会产生停顿
  - 并发标记（和用户线程一起）
    - 主要标记过程，会扫描全部对象
  - 重新标记
    - 由于并发标记时，用户线程仍然运行，因此在正式清理前，再做修正
    - 独占CPU，会产生全局停顿
  - 并发清除（和用户线程一起）
    - 基于标记结果，直接清理对象

  ![](.\img\1577631382(1).jpg)

CMS收集器特点：	

​		尽可能降低停顿

​		会影响系统整体吞吐量和性能，如分一半CPU时间去GC，在GC期间，系统反应速度就下降一半。

​		清理不彻底，因为在清理的时候，用户线程还在运行，会产生新的垃圾。

​		因为和用户线程一起运行，不能在内存空间快慢时再清理，-XX:CMSInitiatingOccupancyFraction设置触发GC的阀值，如果不幸内存预留空间不足，会引发concurrent mode failure。

​	![](.\img\1577631921(1).jpg)

使用：

-XX:+UseCMSCompactAtFullCollection 	Full GC后,进行一次整理整理过程是独占的,会引起停顿时间变长

-XX:+CMSFullGCsBeforeCompaction	 设置进行几次Full GC后,进行一次碎片整理

-XX:ParallelCMSThreads	设定CMS的线程数量，约等于cpu核数



5. **G1**

- 不仅停顿短，同时并发大，不过没CMS短，没Parallel并发量大，是一个均衡点

### 5.2 GC参数小结

- -XX:+ UseSerialGC	在新生代和老年代使用串行收集器
- -XX:SurvivorRatio	设置eden区大小和survivior区大小的比例
- -XX:NewRatio	新生代和老年代的比
- -XX:+UseParNewGC	:在新生代使用并行收集器
- -XX: +UseParallelGC 	新生代使用并行回收收集器
- -xx:+UseParalleloldGC 	老年代使用并行回收收集器
- -XX:ParallelGCThreads 	设置用于垃圾回收的线程数
- -xx:+UseConcMarkSweepGC	:新生代使用并行收集器,老年代使用CMS+串行收集器
- -XX:ParallelCMSThreads 	设定CMS的线程数量
- -XX:CMSInitiatingOccupancyFraction 	设置CMS收集器在老年代空间被使用多少后触发
- -XX:+UseCMSCompactAtFulICollection 	设置CMS收集器在完成垃圾收集后是否要进行一次内存碎片的整理
- -XX:CMSFullGCsBeforeCompaction 	设定进行多少次CMS垃圾回收后,进行一次内存压缩
- -xx:+CMSClassUnloadingEnabled 	允许对类元数据进行回收
- -XX:CMSInitiatingPermOccupancyFraction 	当永久区占用率达到这一百分比时,启动CMS回收
- -XX:UseCMSInitiatingOccupancyOnly 	表示只在到达阀值的时候,才进行CMS回收 

