## 多线程中的设计模式

### 单例模式

### 不变模式-28

类似于String,Integer,Long等包装类

### Future模式

核心思想是异步调用，例如 Callable  

```java
public class Client {
  public static void main(String[] args) throws InterruptedException {
          Client client = new Client();
          FutureData data = (FutureData) client.request("test");
          while (true) {
              if (data.isReady) {
                  System.out.println(data.getResult());
                  return;
              }
          }
      }

  //主线程在异步调用的时候该方法时，会开启另一个线程，直接返回给主线程一个空的结果类
  public Data request(final String queryStr) {
          final FutureData futureData = new FutureData();
          new Thread(() -> {
              ReadlData readlData = new ReadlData(queryStr);
              futureData.setReadlData(readlData);
          }).start();
          return futureData;
      }
}

public class FutureData implements Data {
    protected volatile boolean isReady = false;
    protected ReadlData readlData = null;

    @Override
    public synchronized String getResult() {
        while (!isReady) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return readlData.getResult();
    }

    public synchronized void setReadlData(ReadlData readlData) {
        if (isReady) {
            return;
        }
        this.readlData = readlData;
        isReady = true;
        notifyAll();
    }
}

public class ReadlData implements Data {
    protected final String result;

    public ReadlData(String str){
        try {
            //假设获取真实数据很慢，模拟用户等待
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        result=str;
    }
    @Override
    public String getResult() {
        return result;
    }

}

```

### 线程封闭

#### 3.1 三种变量的区别

C和Java中的叫法区别

|    C     |   Java   |
| :------: | :------: |
| 本地变量 | 局部变量 |
| 全局变量 | 成员变量 |
| 静态变量 | 静态变量 |

三种变量的区别

|          |     成员变量     |        局部变量        |      静态变量      |
| :------: | :--------------: | :--------------------: | :----------------: |
| 定义位置 |   类中，方法外   | 方法中，或者方法的形参 |    类中，方法外    |
| 初始化值 | 有默认的初始化值 |  无，初始化后才能使用  |   有默认初始化值   |
| 调用方式 |     对象调用     |          - -           | 对象调用，类名调用 |
| 存储位置 |       堆中       |          栈中          |   方法区（堆中）   |
| 生命周期 |   与对象共存亡   |      与方法共存亡      |     与类共存亡     |
|   别名   |     实例变量     |          - -           |       类变量       |

静态变量：线程非安全。static变量被所有实例共享，当声明类变量时，不会生成static变量的副本，而是类的所有实例共享同一个static变量。一旦值被修改，其它对象均对修改可见，因此线程非安全。
实例变量：单例时线程非安全，非单例时线程安全。实例变量是实例对象私有的，若系统中只存在一个实例对象，则在多线程下，值改变则对其它对象都可见，所以线程非安全的；每个线程都在不同实例对象中执行，则对象之间的修改互不影响，线程安全。
局部变量：线程安全。定义在方法内部的变量，线程间不共享。
静态方法：方法中如果没有使用静态变量，就没有线程安全问题；静态方法内部的变量是局部变量。

#### 3.2  方法是如何执行的

![](D:\study\Framework\Java\img\23-方法调用过程.png)

到这里,方法调用的过程想必你已经清楚了,但是还有一个很重要的问题, "CPU去哪里找到调用方法的参数和返回地址?"如果你熟悉CPU的工作原理,你应该会立刻想到:通过CPU的堆栈寄存器。CPU支持一种栈结构,栈你一定很熟悉了,就像手枪的弹夹,先入后出。因为这个栈是和方法调用相关的,因此经常被称为调用栈。

例如,有三个方法A, B, C,他们的调用关系是A->B->C (A调用B, B调用C) ,在运行时,会构建出下面这样的调用栈。每个方法在调用栈里都有自己的独立空间,称为栈帧,每个栈帧里都有对应方法需要的参数和返回地址。当调用方法时,会创建新的栈帧,并压入调用栈;当方法返回时,对应的栈帧就会被自动弹出。也就是说,栈帧和方法是同生共死的。

![](D:\study\Framework\Java\img\24-调用栈结构.jpg)

利用栈结构来支持方法调用这个方案非常普遍,以至于CPU里内置了栈寄存器。虽然各家编程,语言定义的方法干奇百怪,但是方法的内部执行原理却是出奇的一致:都是靠栈结构解决的。Java语言虽然是靠虚拟机解释执行的,但是方法的调用也是利用栈结构解决的。

#### 3.3 局部变量存在哪

我们已经知道了方法间的调用在CPU眼里是怎么执行的,但还有一个关键问题:方法内的局部变量存哪里?

局部变量的作用域是方法内部,也就是说当方法执行完,局部变量就没用了,局部变量应该和方法同生共死。此时你应该会想到调用栈的栈帧,调用栈的栈帧就是和方法同生共死的,所以局部变量放到调用栈里那儿是相当的合理。事实上,的确是这样的,局部变量就是放到了调用栈里。于是调用栈的结构就变成了下图这样。

![](D:\study\Framework\Java\img\25-局部变量的栈调用结果.jpg)

这个结论相信很多人都知道,因为学Java语言的时候,基本所有的教材都会告诉你new出来的对象是在堆里,局部变量是在栈里,只不过很多人并不清楚堆和栈的区别,以及为什么要区分堆和栈。现在你应该很清楚了,局部变量是和方法同生共死的,一个变量如果想跨越方法的边界,就必须创建在堆里。

#### 3.4 调用栈与线程

两个线程可以同时用不同的参数调用相同的方法,那调用栈和线程之间是什么关系呢?答案是:每个线程都有自己独立的调用栈。因为如果不是这样,那两个线程就互相干扰了。如下面这幅图·所示,线程A, B, C每个线程都有自己独立的调用栈。

![](D:\study\Framework\Java\img\26-线程与调用栈的关系.jpg)

现在,让我们回过头来再看篇首的问题: Java方法里面的局部变量是否存在并发问题?现在你应该很清楚了,一点问题都没有。因为每个线程都有自己的调用栈,局部变量保存在线程各自的调用栈里面,不会共享,所以自然也就没有并发问题。再次重申一遍:没有共享,就没有伤害。

#### 3.5 线程封闭

方法里的局部变量,因为不会和其他线程共享,所以没有并发问题,这个思路很好,已经成为解决并发问题的一个重要技术,同时还有个响当当的名字叫做线程封闭,比较官方的解释是:仅在单线程内访问数据。由于不存在共享,所以即便不同步也不会有并发问题,性能杠杠的。

采用线程封闭技术的案例非常多,例如从数据库连接池里获取的连接Connection,在JDBC规范里并没有要求这个Connection必须是线程安全的。数据库连接池通过线程封闭技术,保证一个Connection一旦被一个线程获取之后,在这个线程关闭Connection之前的这段时间里,不会再分配给其他线程,从而保证了Connection不会有并发问题。

### ThreadLocal

用空间换时间，给每个线程内部都设置一个局部变量

#### [3.1. ThreadLocal简介](https://snailclimb.gitee.io/javaguide/#/docs/java/Multithread/JavaConcurrencyAdvancedCommonInterviewQuestions?id=_31-threadlocal简介)

通常情况下，我们创建的变量是可以被任何一个线程访问并修改的。**如果想实现每一个线程都有自己的专属本地变量该如何解决呢？** JDK中提供的`ThreadLocal`类正是为了解决这样的问题。 **`ThreadLocal`类主要解决的就是让每个线程绑定自己的值，可以将`ThreadLocal`类形象的比喻成存放数据的盒子，盒子中可以存储每个线程的私有数据。**

**如果你创建了一个`ThreadLocal`变量，那么访问这个变量的每个线程都会有这个变量的本地副本，这也是`ThreadLocal`变量名的由来。他们可以使用 `get（）` 和 `set（）` 方法来获取默认值或将其值更改为当前线程所存的副本的值，从而避免了线程安全问题。**

再举个简单的例子：

比如有两个人去宝屋收集宝物，这两个共用一个袋子的话肯定会产生争执，但是给他们两个人每个人分配一个袋子的话就不会出现这样的问题。如果把这两个人比作线程的话，那么ThreadLocal就是用来避免这两个线程竞争的。

#### [3.2. ThreadLocal示例](https://snailclimb.gitee.io/javaguide/#/docs/java/Multithread/JavaConcurrencyAdvancedCommonInterviewQuestions?id=_32-threadlocal示例)

相信看了上面的解释，大家已经搞懂 ThreadLocal 类是个什么东西了。

```java
import java.text.SimpleDateFormat;
import java.util.Random;

public class ThreadLocalExample implements Runnable{

     // SimpleDateFormat 不是线程安全的，所以每个线程都要有自己独立的副本
    private static final ThreadLocal<SimpleDateFormat> formatter = ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyyMMdd HHmm"));

    public static void main(String[] args) throws InterruptedException {
        ThreadLocalExample obj = new ThreadLocalExample();
        for(int i=0 ; i<10; i++){
            Thread t = new Thread(obj, ""+i);
            Thread.sleep(new Random().nextInt(1000));
            t.start();
        }
    }

    @Override
    public void run() {
        System.out.println("Thread Name= "+Thread.currentThread().getName()+" default Formatter = "+formatter.get().toPattern());
        try {
            Thread.sleep(new Random().nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //formatter pattern is changed here by thread, but it won't reflect to other threads
        formatter.set(new SimpleDateFormat());

        System.out.println("Thread Name= "+Thread.currentThread().getName()+" formatter = "+formatter.get().toPattern());
    }

}
```

Output:

```
Thread Name= 0 default Formatter = yyyyMMdd HHmm
Thread Name= 0 formatter = yy-M-d ah:mm
Thread Name= 1 default Formatter = yyyyMMdd HHmm
Thread Name= 2 default Formatter = yyyyMMdd HHmm
Thread Name= 1 formatter = yy-M-d ah:mm
Thread Name= 3 default Formatter = yyyyMMdd HHmm
Thread Name= 2 formatter = yy-M-d ah:mm
Thread Name= 4 default Formatter = yyyyMMdd HHmm
Thread Name= 3 formatter = yy-M-d ah:mm
Thread Name= 4 formatter = yy-M-d ah:mm
Thread Name= 5 default Formatter = yyyyMMdd HHmm
Thread Name= 5 formatter = yy-M-d ah:mm
Thread Name= 6 default Formatter = yyyyMMdd HHmm
Thread Name= 6 formatter = yy-M-d ah:mm
Thread Name= 7 default Formatter = yyyyMMdd HHmm
Thread Name= 7 formatter = yy-M-d ah:mm
Thread Name= 8 default Formatter = yyyyMMdd HHmm
Thread Name= 9 default Formatter = yyyyMMdd HHmm
Thread Name= 8 formatter = yy-M-d ah:mm
Thread Name= 9 formatter = yy-M-d ah:mm
```

从输出中可以看出，Thread-0已经改变了formatter的值，但仍然是thread-2默认格式化程序与初始化值相同，其他线程也一样。

上面有一段代码用到了创建 `ThreadLocal` 变量的那段代码用到了 Java8 的知识，它等于下面这段代码，如果你写了下面这段代码的话，IDEA会提示你转换为Java8的格式(IDEA真的不错！)。因为ThreadLocal类在Java 8中扩展，使用一个新的方法`withInitial()`，将Supplier功能接口作为参数。

```java
 private static final ThreadLocal<SimpleDateFormat> formatter = new ThreadLocal<SimpleDateFormat>(){
        @Override
        protected SimpleDateFormat initialValue()
        {
            return new SimpleDateFormat("yyyyMMdd HHmm");
        }
    };
```

#### [3.3. ThreadLocal原理](https://snailclimb.gitee.io/javaguide/#/docs/java/Multithread/JavaConcurrencyAdvancedCommonInterviewQuestions?id=_33-threadlocal原理)

从 `Thread`类源代码入手。

```java
public class Thread implements Runnable {
 ......
//与此线程有关的ThreadLocal值。由ThreadLocal类维护
ThreadLocal.ThreadLocalMap threadLocals = null;

//与此线程有关的InheritableThreadLocal值。由InheritableThreadLocal类维护
ThreadLocal.ThreadLocalMap inheritableThreadLocals = null;
 ......
}
```

从上面`Thread`类 源代码可以看出`Thread` 类中有一个 `threadLocals` 和 一个 `inheritableThreadLocals` 变量，它们都是 `ThreadLocalMap` 类型的变量,我们可以把 `ThreadLocalMap` 理解为`ThreadLocal` 类实现的定制化的 `HashMap`。默认情况下这两个变量都是null，只有当前线程调用 `ThreadLocal` 类的 `set`或`get`方法时才创建它们，实际上调用这两个方法的时候，我们调用的是`ThreadLocalMap`类对应的 `get()`、`set()`方法。

`ThreadLocal`类的`set()`方法

```java
    public void set(T value) {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
    }
    ThreadLocalMap getMap(Thread t) {
        return t.threadLocals;
    }
```

通过上面这些内容，我们足以通过猜测得出结论：**最终的变量是放在了当前线程的 `ThreadLocalMap` 中，并不是存在 `ThreadLocal` 上，`ThreadLocal` 可以理解为只是`ThreadLocalMap`的封装，传递了变量值。** `ThrealLocal` 类中可以通过`Thread.currentThread()`获取到当前线程对象后，直接通过`getMap(Thread t)`可以访问到该线程的`ThreadLocalMap`对象。

**每个`Thread`中都具备一个`ThreadLocalMap`，而`ThreadLocalMap`可以存储以`ThreadLocal`为key的键值对。** 比如我们在同一个线程中声明了两个 `ThreadLocal` 对象的话，会使用 `Thread`内部都是使用仅有那个`ThreadLocalMap` 存放数据的，`ThreadLocalMap`的 key 就是 `ThreadLocal`对象，value 就是 `ThreadLocal` 对象调用`set`方法设置的值。`ThreadLocal` 是 map结构是为了让每个线程可以关联多个 `ThreadLocal`变量。这也就解释了 ThreadLocal 声明的变量为什么在每一个线程都有自己的专属本地变量。

`ThreadLocalMap`是`ThreadLocal`的静态内部类。

![ThreadLocal内部类](https://my-blog-to-use.oss-cn-beijing.aliyuncs.com/2019-6/ThreadLocal%E5%86%85%E9%83%A8%E7%B1%BB.png)

#### [3.4. ThreadLocal 内存泄露问题](https://snailclimb.gitee.io/javaguide/#/docs/java/Multithread/JavaConcurrencyAdvancedCommonInterviewQuestions?id=_34-threadlocal-内存泄露问题)

`ThreadLocalMap` 中使用的 key 为 `ThreadLocal` 的弱引用,而 value 是强引用。所以，如果 `ThreadLocal` 没有被外部强引用的情况下，在垃圾回收的时候，key 会被清理掉，而 value 不会被清理掉。这样一来，`ThreadLocalMap` 中就会出现key为null的Entry。假如我们不做任何措施的话，value 永远无法被GC 回收，这个时候就可能会产生内存泄露。ThreadLocalMap实现中已经考虑了这种情况，在调用 `set()`、`get()`、`remove()` 方法的时候，会清理掉 key 为 null 的记录。使用完 `ThreadLocal`方法后 最好手动调用`remove()`方法

```java
      static class Entry extends WeakReference<ThreadLocal<?>> {
            /** The value associated with this ThreadLocal. */
            Object value;

            Entry(ThreadLocal<?> k, Object v) {
                super(k);
                value = v;
            }
        }
```

**弱引用介绍：**

> 如果一个对象只具有弱引用，那就类似于**可有可无的生活用品**。弱引用与软引用的区别在于：只具有弱引用的对象拥有更短暂的生命周期。在垃圾回收器线程扫描它 所管辖的内存区域的过程中，一旦发现了只具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存。不过，由于垃圾回收器是一个优先级很低的线程， 因此不一定会很快发现那些只具有弱引用的对象。
>
> 弱引用可以和一个引用队列（ReferenceQueue）联合使用，如果弱引用所引用的对象被垃圾回收，Java虚拟机就会把这个弱引用加入到与之关联的引用队列中



#### 3.5. ThreadLocal与synchronized的比较

（1）ThreadLocal使用场合主要解决多线程中数据因并发产生的不一致问题。ThreadLocal为每个线程中并发访问的数据提供一个副本，通过访问副本来运行业务，这样的结果是耗费了内存，但大大减少了线程同步所带来的性能消耗，也减少了线程并发控制的复杂度；

（2）ThreadLocal不能使用原子类型，只能使用Object类型。ThreadLocal的使用要比synchronized简单得多；

（3）ThreadLocal和synchronized都用于解决多线程并发的访问，但是二者有本质区别：synchronized是利用锁的机制，使变量或代码块在某一时刻只能被一个线程访问，而ThreadLocal为每一个线程都提供了变量的副本，使每个线程在某一时间访问到的并不是同一个对象，这样就隔离了多个线程对数据的共享。而synchronized却正好相反，它用于多个线程间通信时能够获取数据共享；

（4）同步会带来巨大的性能开销，所以同步操作应该是细粒度的（对象中的不同元素使用不同的锁，而不是整个对象一个锁），如果同步使用得当，带来的性能开销是微不足道的，使用同步真正的风险是复杂性和可能破坏资源安全，而不是性能；

（5）synchronized用于线程间的数据共享，ThreadLocal用于线程间的数据隔离。
ThreadLocal：数据隔离，适合多个线程需要多次使用同一个对象，并且需要该对象具有相同的初始化值时；
synchronized：数据同步，当多个线程想访问或修改同一个对象，需要阻塞其它线程从而只允许其中一个线程对其进行访问与修改。

#### 3.6. 如何实现线程同步

(1) synchronized 关键字，修饰代码块、方法或静态方法
    同步是一种高开销的操作，因此应该尽量减少同步的内容。通常没有必要同步整个方法，使用synchronized代码块同步关键代码即可。
(2) ReentrantLock 
    在Java5中新增了java.util.concurrent包来支持同步。ReentrantLock类是可重入、互斥、实现了Lock接口的锁，它与使用synchronized方法和块具有相同的基本行为和语义，并且扩展了其能力。
    ReentrantLock类的常用方法有：
    ReentrantLock()：创建一个ReentrantLock实例；
    lock()：获得锁；
    unlock()：释放锁；2
    注：ReentrantLock还有一个可以创建公平锁的构造方法，但由于会大幅降低程序运行效率，不推荐使用。

(3) ThreadLocal 实现线程同步
    使用ThreadLocal管理变量，则每个使用该变量的线程都获得该变量的一个副本，副本之间相互独立，这样每个线程都可以随意更改自己的变量副本，而不会对其它线程产生影响。
