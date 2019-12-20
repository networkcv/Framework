
# 锁优化

## 锁优化思路
### 减少锁持有时间
只对需要同步的代码块进行加锁，不建议把整个方法锁住，这样会增大线程因同时访问临界资源的而产生锁竞争的几率

### 减小锁粒度
- 将大对象拆成小对象，大大增加并行度，降低锁竞争
- 偏向锁，轻量级锁成功率提高
- HashMap的同步实现
Collections.synchronizdMap(Map<K,V> m)  返回SynchronizedMap对象   
- ConcurrentHashMap
将之前的哈希表逻辑上拆分为若干个Segment片段，Segment<K,V>[] segments  
Segment中维护HashMap<K,V>,进行操作时，先定位到Segment，然后在进行锁竞争，执行对应操作  
在减小锁粒度后，ConcurrentHashMap允许若干个线程同时进入  

### 锁分离
根据功能进行锁的读写分离，使用ReadWriteLock，在读多写少的情况下，可以提高性能  
读写分离思想的延伸，只要操作互不影响，锁就可以分离  
LinkedBlockingQueue

### 锁消除
在即时编译器时，如果发现不可能被共享的对象或者不会发生竞争的锁，则可以消除这些对象的锁操作  

```java
//JVM通常会去掉下面的加锁操作
sysnchronized(new Object){
	...
}
```

比如使用StringBuffer、 Vector，会引入锁，JVM会对一些进行锁消除，前提是开启 -server  
开启锁消除 -server -XX:+DOEscapeAnalysis -XX:+ELiminateLocks   
进行逃逸分析后，如果变量没有逃出了当前作用域，JVM对进行锁优化   
逸出分析(Escape Analysis)：判断程序的其他地方会不会用到这个被加锁的变量  

```java
public String getStoogeNames(){
    List<String> stooges = new Vector<String> ();
    stooges.add("1");
    stooges.add("2");
    stooges.add("3");
    return stooges.toString();
}
```

上面例子中对List的唯一引用就是局部变量stooges，并且所有封闭在栈中的变量都会自动成为线程本地变量。在方法中，至少会将Vector上的锁获取/释放4次，JVM会分析这些调用 ，从而使stooges及其内部状态不会逸出，因此可以去掉这4此对锁获取操作。

### 锁粗化

通常情况下,为了保证多线程间的有效并发,会要求每个线程持有锁的时间尽量短,即在使用完公共资源后,应该立即释放锁。只有这样,等待在这个锁上的其他线程才能尽早的获得资源执行任务。但是，凡事都有一个度,如果对同一个锁不停的进行请求、同步和释放，其本身也会消耗，系统宝贵的资源，反而不利于性能的优化。

编译器也可以执行锁粒度粗化操作，将临近的同步代码块用同一个锁合并起来，对于上面的那个例子，就是将3个add操作和toString操作合并在一次锁操作中。



## 虚拟机内的锁优化
**对象头**

Mark Word，对象头的标记，32位  
描述对象的hash、锁信息、垃圾回收标记，年龄  
- 指向锁记录的指针  
- 指向monitor的指针
- GC标记
- 偏向锁线程ID  
### 偏向锁
- 大部分情况是没有竞争的，所以可以通过偏向来提高性能
- 所谓偏向，就是偏心，即锁会偏向于当前已经占有锁的线程
- 将对象头Mark的标记设置为偏向，并将线程ID写入对象头Mark
- 只要没有竞争，获得偏向锁的线程，在将来进入同步块，不需要做同步
- 当其他线程请求相同锁时，偏向模式结束
- -XX：+UseBiasedLocking 启用偏向锁 -XX:BiasedLockingStartupDelay=0 JVM启动后延时0s启动偏向锁
- 在竞争激烈的场合，偏向锁会增加系统负担
### 轻量级锁
普通的锁处理性能不够理想，轻量级锁是一种快速锁定方法   
如果对象没有被锁定，将对象头的Mark指针保存到锁对象中，将对象头设置为指向锁的指针（在线程栈空间中）  
如果轻量级锁失败，表示存在竞争，升级为重量级锁（常规锁，操作系统层面的）  
在没有锁竞争的前提下，减少传统锁使用OS互斥量产生的性能损耗  
在竞争激烈时，轻量级锁会做很多额外操作

### 自旋锁
一次挂起操作会消耗80000个时钟周期，自旋锁在尝试获取锁失败时，不会直接被挂起，而是循环的再去尝试拿锁    
当竞争存在时，如果线程可以很快获得锁，那么可以不在OS层挂起线程，让线程做几个空操作（自旋）    
如果同步块很长，自旋失败，会降低系统性能  
如果同步块很短，自旋成功，节省线程挂起切换时间，提升系统性能

### 偏向锁，轻量级锁，自旋锁总结
不是Java语言层面的锁优化方法  
内置于JVM中的获取锁的优化方法和获取锁的步骤
- 偏向锁可用会先尝试偏向锁
- 轻量级锁可用会先尝试轻量级锁
- 以上都失败，尝试自旋锁
- 再失败，尝试普通锁，使用OS互斥量在操作系统层挂起

### 一个错误使用锁的案例
```java
public class ThreadNotSafeDemo {
    public static class T extends Thread {
        static Integer i = 0;

        @Override
        public void run() {
            for (int j = 0; j < 1000; j++) {
                synchronized (i) {
                    i++;
                }
            }
        }
        public static void main(String[] args) throws InterruptedException {
            T t1 = new T();
            T t2 = new T();
            t1.start();
            t2.start();
            t1.join();
            t2.join();
            System.out.println(i);
        }
    }
}
```
上边这段代码，看起来没问题，但实际上是线程不安全的，原因在于Integer和String一样是不可变的  
一旦被赋值之后，这个Integer的value就不会再发生改变了，而i++这个操作建立在自动拆箱和装箱的基础上  
每次进行++操作后，都会把结果赋给一个新的Integer对象，这就导致每次获取的不是同一把锁，最终导致结果偏小  

### ThreadLocal及其源码分析
用空间换时间，给每个线程内部都设置一个局部变量



### 3.ThreadLocal

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

### 



### 