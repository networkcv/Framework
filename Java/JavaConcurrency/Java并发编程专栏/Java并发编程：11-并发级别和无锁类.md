**前言：**

前面几篇都是关于J.U.C的同步工具类，包括使用时需要注意的地方，以及它们是如何通过AQS来实现的，在解读源码的时候，发现经常出现CAS操作，下面我们来了解一下CAS。

[toc]

**面试问题**

Q ：介绍一下Atomic 原子类及其原理？

Q ：谈谈你对CAS的理解？

## 1.并发级别

我们都知道CAS是无锁操作，那么什么是无锁？这就要引出并发级别这个概念了，由于临界区的原因，多线程之间的并发必须受到控制，根据控制并发的策略，可以把并发的级别分类，可以分为阻塞、无饥饿、无障碍、无锁、无等待。

### 1.1 阻塞

难进易出，当临界区被占用时，其他线程无法继续执行，必须在临界区外等待，直至临界区资源被释放，才可以去申请，如果申请到了才能继续执行，不然还要继续等待。Java中我们使用内置锁synchronized或者显式锁ReentrantLock，都可能会使线程阻塞。

阻塞的控制方式是悲观策略，认为两个进入临界区的线程很可能都会对数据做修改，为了保护共享数据，所以使用加锁的方式，无论线程是进去读还是写，都让他们排队进入临界区，但实际可能是大量的读操作，极少的写操作，导致读操作的效率也被极大的拉低了。

### 1.2 无饥饿

线程是有优先级之分的，线程调度的时候会更倾向于满足优先级高的线程。这样就会导致资源的不公平分配，优先级高的线程一直在执行，优先级低的线程一直拿不到时间片，就会产生饥饿。举个例子，ReentrantLock支持公平锁和非公平锁，非公平锁会在加入等待队列前直接尝试获取锁，并没有考虑等待队列中是否已经有节点在它之前排队，公平锁的公平之处在于它会去检查前面是否有节点，如果有则不尝试获取锁。

### 1.3 无障碍  

易进难出，无障碍是一种最弱的非阻塞调度，多个线程可以同时进入临界区，但是在释放资源时，会判断是否发生数据竞争，比如A线程读取数据x，要释放资源时，系统会判断当前的临界区内x值是否发生变化，如果发生变化，则会回滚A线程的操作。

相对于阻塞级别的悲观策略，无障碍级别的调度是一种乐观策略，它认为多个进入临界区的线程很有可能不会发生冲突，可能都是读操作。如果检测到冲突，就进行回滚。如果在冲突密集的情况下，所有线程可能都不断回滚自己的操作，使得没有一个线程可以走出临界区，影响系统的正常执行。

通过一个实例可以很好的理解，线程A修改了x的值，要释放资源出临界区时，线程B修改了x的值，系统会回滚线程A的操作，线程B要出临界区时，线程C又修改了x的值，这下该回滚B的操作了，线程C要出临界区的时候，之前被回滚的A完成了修改操作，所以C也要被回滚了，此处A打算出临界区，B又来了，这样就形成了一个闭环，谁都别想走。

### 1.4 无锁 

无锁的并行都是无障碍的，在无锁的情况下，所有线程都可以尝试对临界区的访问，但是与无障碍不同的是，**无锁的并发保证必然有 一个线程 能在有限步内完成操作，离开临界区**

还是A、B、C三个线程修改x值的问题，要想打破之前形成的闭环，就必须要有一个线程先出去，通过竞争的方式每次选出一个线程胜出，胜出的可以释放临界区资源。

### 1.5 无等待  

无状态的前提是无锁的，要求 **所有线程** 都必须在有限步内完成，这样就不会发生饥饿现象。



## 2.无锁类

### 2.1 无锁类的介绍

为了方便使用CAS，Java在J.U.C中提供了一个atomic包，里边包含一些直接使用CAS操作的线程安全的类。

根据操作的数据类型，可以分为以下4类：

**基本类型**

使用原子的方式更新基本类型

- AtomicInteger：整型原子类
- AtomicLong：长整型原子类
- AtomicBoolean ：布尔型原子类

**数组类型**

使用原子的方式更新数组里的某个元素

- AtomicIntegerArray：整型数组原子类
- AtomicLongArray：长整型数组原子类
- AtomicReferenceArray ：引用类型数组原子类

**引用类型**

- AtomicReference：引用类型原子类
- AtomicStampedReference：支持时间戳的引用类型原子类
- AtomicMarkableReference ：原子更新带有标记位的引用类型

**对象的属性修改类型**

- AtomicIntegerFieldUpdater:原子更新整型字段的更新器
- AtomicLongFieldUpdater：原子更新长整型字段的更新器
- AtomicReferenceFieldUpdater：原子更新引用类型里的字段
- AtomicStampedReference ：原子更新带有版本号的引用类型。该类将整数值与引用关联起来，可用于解决原子的更新数据和数据的版本号，可以解决使用 CAS 进行原子更新时可能出现的 ABA 问题。
- AtomicMarkableReference：原子更新带有标记的引用类型。该类将 boolean 标记与引用关联起来，也可以解决使用 CAS 进行原子更新时可能出现的 ABA 问题。

**原子累加器**

- LongAdder	原子类型累加器
- LongAccumulator
- DoubleAdder
- DoubleAccumulator

### 2.2 AtomicInteger

这个类是atomic包中最常用的类，可以将其看作一个线程安全的Integer，但是对其的修改方式和Integer有所不同，必须通过方法来修改Integer的值，方法内部都使用CAS。

CAS（Compare And Swap），它包含三个参数CAS（V，E，N）。V表示要更新的变量，E表示预期值，N表示新值。E值是之前读取的V值，仅当前内存中V值等于E值时，才会将V值设置为新值N。如果V值和E值不同，则说明有其他线程做了更新，当前线程什么都不做。

多个线程同时使用CAS时操作一个变量时，只有一个会成功并返回true。其他失败的线程不会被挂起，只是返回false，被告知失败，并且允许再次尝试，或者放弃尝试。

虽然CAS会先读取值，然后比较，最后再赋值，但是这整个操作是一个原子操作，由一条CPU指令（cmpxchg指令）完成，通过比较交换指令实现，省去了线程频繁调度和线程锁竞争的开销，所以比基于锁的方式性能更好，而且还不会发生死锁。

**AtomicInteger 类常用方法**

```java
public final int get() 									//获取当前的值
public final void set(int newValue)						//设置当前值
public final int getAndSet(int newValue)				//设置新值，返回旧值
public final boolean compareAndSet(int expect,int u)	//如果当前值为expect，则设置为u
public final int getAndIncrement()						//当前值加1，返回旧值
public final int getAndDecrement()						//当前值减1，返回旧值
public final int getAndAdd(int delta) 					//当前值加delat，返回旧值
public final int addAndGEt(int delta) 					//当前值加delat，返回新值
public final int incrementAndGet()						//当前值加1，返回新值
public final int decrementAndGet()   					//当前值减1，返回新值
```

**AtomicInteger内部实现**

```java
	//用于保存Integer的值
	private volatile int value;  
	//CAS修改时value，快速定位到value所在内存位置的偏移量
	private static final long valueOffset;
	//定义了真正执行CAS指令的本地方法
	private static final Unsafe unsafe = Unsafe.getUnsafe();
```

和AtomicInteger类似的还有其他基本类型的Atomic类，如AtomicLong、AtomicBoolean。



### 2.3 AtomicIntegerArray

除了基本类型外，还可以对数组进行原子操作。

- AtomicIntegerArray：整形数组原子类
- AtomicLongArray：长整形数组原子类
- AtomicReferenceArray ：引用类型数组原子类

上面三个类提供的方法几乎相同，所以我们这里以 AtomicIntegerArray 为例子来介绍。

**AtomicIntegerArray 类常用方法**

```java
public final int get(int i) 						//获取数组第i个下标元素
public final int getAndSet(int i, int newValue)		//将下标为i的元素设置为newValue，返回旧值
public final int getAndIncrement(int i)				//将下标为i的元素递增，返回旧值
public final int getAndDecrement(int i) 			//将下标为i的元素递减，返回旧值
public final int getAndAdd(int delta)				//将下标为i的元素加上预期的值，返回旧值
boolean compareAndSet(int expect, int update) 		//进行CAS操作，第i个下标元素等于expect，则设置为update，成功返回true
public final void lazySet(int i, int newValue)		//最终 将index=i 位置的元素设置为newValue,使用 lazySet 设置之后可能导致其他线程在之后的一小段时间内还是可以读到旧的值。
```



### 2.4 AtomicReference

与AtomicInteger非常相似，不同之处在于AtomicReference对应普通的对象引用。在AtomicReference还需要注意“ABA问题“。

”ABA问题“是CAS在两次乐观读之间，变量被修改为B又被修改为A，看起来好像没有被修改一样，如果是数字，其保存的信息就是其数值本身，只要最终改回为期望值，那么加法计算就不会出错，但是对引用而言，中间修改对象的内容，可能会影响CAS判断当前数据的状态。

这类问题的根本原因是对象在修改过程中丢失了状态信息，因此，只要记录对象在修改过程中的状态值，就可以解决这类问题，JDK 1.5 以后的 **AtomicStampedReference** 就是这么做的，它内部不仅维护对象值，还维护了一个更新时间的时间戳，修改的时候不仅要期望值，还要而外传入时间戳，当其中的value被修改时，同时还会更新时间戳。

```java
public boolean compareAndSet(V expectedReference,V newReference,
                              int expectedStamp,int newStamp) 
```

- expectedReference：期望值
- newReference：新值
- expectedStamp：期望时间戳
- newStamp：新时间戳



### 2.5 AtomicIntegerFieldUpdater

如果需要原子更新某个类里的某个字段时，需要用到对象的属性修改类型原子类。

- AtomicIntegerFieldUpdater:原子更新整形字段的更新器
- AtomicLongFieldUpdater：原子更新长整形字段的更新器
- AtomicStampedReference ：原子更新带有版本号的引用类型。该类将整数值与引用关联起来，可用于解决原子的更新数据和数据的版本号，可以解决使用 CAS 进行原子更新时可能出现的 ABA 问题。

要想原子地更新对象的属性需要两步。第一步，因为对象的属性修改类型原子类都是抽象类，所以每次使用都必须使用静态方法 newUpdater()创建一个更新器，并且需要设置想要更新的类和属性。第二步，更新的对象属性必须使用 public volatile 修饰符。

几个注意事项：

- 第一，Updater只能修改它可见范围内的变量。因为Updater使用反射得到这个变量。如果变量不可见,就会出错。比如如果score申明为private,就是不可行的。
- 第二，为了确保变量被正确的读取,它必须是volatile类型的。如果我们原有代码中未申明这个类型,那么简单地申明一下就行,这不会引起什么问题。
- 第三，由于CAS操作会通过对象实例中的偏移量直接进行赋值,因此,它不支持static字段(Unsafe. objetrieldofset0不支持静态变量)

### 2.6 LongAdder

这个类仅仅用来执行累加操作，相比于原子的基本数据类型，速度更快。

实现原理和ConcurronHashMap类似，采用了热点分离的思想，将一个long划分为多个单元，将并发线程的读写操作分发到多个单元上，以保证CAS更新能够成功，取值前需要对各个单元进行求和，返回sum。

考虑到如果并发不高的话，这种做法会损耗系统资源，所以默认会维持一个long，如果发生冲突，则会拆分为多个单元，并且会动态的扩容。在高并发环境下，LongAdder性能更高，但同时也会消耗更多的空间。

和AtomicInteger类似的使用方式，但是不支持`compareAndSet()`。

```java
public void add(long x)
public void increment()
public void decrement()
public long sum()
public long longValue()
public int intValue()
```



### 2.7 Unsafe类

unsafe是sun.misc.Unsafe类型，该类是JDK内部使用的专属类，主要提供一些用于执行低级别、不安全操作的方法，涉及到指针，如直接访问系统内存资源、自主管理内存资源等，这些方法在提升Java运行效率、增强Java语言底层资源操作能力方面起到了很大的作用。

![](D:\study\Framework\Java\img\33-Unsafe.jpg)

**如何获取**

JDK的开发人员并不希望我们使用这个类，Unsafe的静态方法getUnsafe代码如下：

```java
    public static Unsafe getUnsafe() {
        Class var0 = Reflection.getCallerClass();
        if (!VM.isSystemDomainLoader(var0.getClassLoader())) {
            throw new SecurityException("Unsafe");
        } else {
            return theUnsafe;
        }
    }
```

根据Java类加载器的工作原理，应用程序的类由App Loader加载，而系统核心类，如rt.jar中的类由Bootstrap类加载器加载。Bootstrap加载器没有Java对象，因此获得这个类加载器会返回null，所以当一个类的类加载其为null时，说明它是由Bootstarp加载的，或者是rt.jar中的类。

但是必要的时候我们还是可以获取到的：

- 方法一：从`getUnsafe`方法的使用限制条件出发，通过Java命令行命令`-Xbootclasspath/a`把调用Unsafe相关方法的类A所在jar包路径追加到默认的bootstrap路径中，使得A被引导类加载器加载，从而通过`Unsafe.getUnsafe`方法安全的获取Unsafe实例。

- 方法二：通过反射的方式获取

  ```java
  private static Unsafe reflectGetUnsafe() {
      try {
        Field field = Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        return (Unsafe) field.get(null);
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        return null;
      }
  }
  ```

**CAS相关**

```java
public final native boolean compareAndSwapInt(Object o, long offset, int expected, int x)
```

- 参数 o：给定的对象。

- 参数 offset：对象内的偏移量，用来寻找要修改的字段（对象的引用会指向该对象的头部，偏移量可以快速定位该字段）。

- 参数 expected：期望值。

- 参数 x：要设置的值。

  ![](D:\study\Framework\Java\img\34-CAS.jpg)

```java
public native int getInt(long offset);
public native void putInt(long offset, int x);
public native long objectFieldOffset(Field f);
```

**线程调度**

还记得AQS中挂起`park()`和唤醒`unpark()`操作吗，具体调用的是LockSupport类的静态方法。相比于Thread类提供的 `suspend()`与`resume()`，推荐使用LockSupport的原因是，即使unpark在park之前调用，也不会导致线程永久被挂起 ，LockSupport的底层使用的是Unsafe类。

```java
	public static void park(Object blocker) {
        Thread t = Thread.currentThread();
        setBlocker(t, blocker);
        UNSAFE.park(false, 0L);
        setBlocker(t, null);
    }

	public static void unpark(Thread thread) {
        if (thread != null)
            UNSAFE.unpark(thread);
    }
```

## 3.总结

无锁相对于阻塞，性能好，不会出现死锁，但是因为自旋反复尝试，可能会出现活锁或饥饿问题。

**无锁适用于读多写少，冲突较少场景。**

使用synchronized同步锁进行线程阻塞和唤醒切换以及用户态内核态间的切换操作额外浪费消耗cpu资源；而CAS基于硬件实现，不需要进入内核，不需要切换线程，操作自旋几率较少，因此可以获得更高的性能。

**阻塞适用于写多，冲突较多的场景。**

CAS自旋的概率会比较大，从而浪费更多的CPU资源，效率远低于synchronized。

> 原子类只能针对一个共享变量，多个变量还是需要使用互斥锁来解决。



## Reference

&emsp;&emsp;《Java 并发编程实战》  
&emsp;&emsp;《实战Java高并发程序设计》  
&emsp;&emsp;https://snailclimb.gitee.io/javaguide/#/
&emsp;&emsp;https://tech.meituan.com/2019/02/14/talk-about-java-magic-class-unsafe.html

**感谢阅读**！