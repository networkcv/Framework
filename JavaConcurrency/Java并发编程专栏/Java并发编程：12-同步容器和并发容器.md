**前言：**

本篇将简单介绍同步容器和并发容器，不会过多的深入，关于HashMap和ConcurrentHashMap这两个类会另外的篇章中分析。

[toc]

**面试问题**

Q ：

Q ：ConcurrentHashMap 和 Hashtable 的区别？

## 1.同步容器

### 1.1 对容器的理解

在内存中，数据要么以数组的方式连续存储，要么以链表的方式非连续存储，很多时候这两种最基本的数据结构无法满足我们的需求，因此衍生出更多的抽象数据结构，如树、堆、栈、队列、图、跳跃表和散列表等。

高级语言中再进一步的封装，按照集合特点分为List、Map、Set和Queue这几大类。再使用不同的数据结构来实现这些接口，如ArrayList、LinkedList、HashSet、HashMap等等，以便应对不同场景，例如读多写少使用数组类型，写多读少使用链表类型。同时封装还可以降低我们的使用成本，通过最简单的API调用就可以使用这些复杂的数据结构。

### 1.2 同步容器

上面提到的那些容器都是不是线程安全的，也就意味着在多线程并发访问这些容器的时候，会出现线程安全问题。

举个最简单的例子，多个线程同时对只有一个元素的集合做移除 `remove()`，开始执行时，多个线程都通过了集合元素不为0的判断，但在第一个线程移除元素后，之前通过该判断的线程没有重新判断就执行`remove()`，这样就会抛出 `IndexOutOfBoundsException` 异常。这种由于不恰当的执行时序而出现不正确结果的情况被称为**竟态条件**（Race Condition）。

在容器使用中还有一个地方容易发生这种 “先检查后执行” 的竟态条件——迭代器遍历元素。

```java
        while(iterator.hasNext()){
            foo(iterator.next());
        }
```

为了保证这些容器的线程安全，JDK的开发人员提供了另外一套线程安全的容器，Vector、Stack和Hashtable，这些容器内部的方法都是用synchronized加锁，来保证组合操作的原子性，每次调用方法都要加锁和释放锁，而且由于锁的是整个集合对象，一个线程读取的时候，另外一个线程只能等待，这样极大的影响了性能，所以在JDK5中推出了并发容器。

如果需要对已有的容器提供线程安全性保证，可以使用Collections工具类的  `synchronizedXXX()` 方法来包装我们的容器。但这是通过全局的锁来同步不同线程间的并发访问，因此也会带来不可忽视的性能问题。



## 2.并发容器

### 2.1 CopyOnWriteArrayList

CopyOnWriteArrayList用于代替同步List，在读远远多于写的场景下可以提供更好的并发性能。

在容器内部维护着一个Object[]数组引用 array，所有读操作不需要加锁，直接去读array，如果读的时候有写操作发生，写操作将array指向的数组复制一份，在新数组上增加元素，完成后将array指向新数组，在修改的过程中，读操作可以正常进行，只是在修改后但还没有指向新数组前，读到不是最新的数据。

**读操作**

```java
    private transient volatile Object[] array;

    public E get(int index) {
        return get(getArray(), index);
    }
    @SuppressWarnings("unchecked")
    private E get(Object[] a, int index) {
        return (E) a[index];
    }
    final Object[] getArray() {
        return array;
    }
```

**写操作**

```java
    public boolean add(E e) {
        final ReentrantLock lock = this.lock;
        lock.lock();//加锁 虽然可以读写并行，但是写写还是会阻塞的
        try {
            Object[] elements = getArray();
            int len = elements.length;
            Object[] newElements = Arrays.copyOf(elements, len + 1);//拷贝新数组
            newElements[len] = e;
            setArray(newElements);
            return true;
        } finally {
            lock.unlock();//释放锁
        }
    }
```



选择应用场景时需要注意，适合对读性能要求很高，读多写少，而且能够容忍读写的短暂不一致，还需要注意的是其写时复制机制，会在内存中同时存在两个对象，如果对象过大，可能会引发Yong GC和Full GC。

CopyOnWriteArraySet的作用是代替同步的Set，原理和CopyOnWriteArrayList相同。



### 2.2 ConcurrentHashMap

ConcurrentHashMap用于替代低性能的HashTable，在学习ConcurrentHashMap之前，建议先了解[HashMap底层的实现原理](https://segmentfault.com/a/1190000012926722)。

在JDK8之后HashMap底层实现做了改进，将之前的 数组+链表 改进为 数组+链表+红黑树，当链表长度大于阈值（默认为8）时，将链表转化为红黑树，使搜索时间控制在O(logn)上。 

在JDK7的时候，ConcurrentHashMap底层是采用 分段的数组+链表 实现的，通过多个分段锁对桶数组进行分段管理，每个锁只负责桶数组的一部分，多线程并发访问的时候，只要访问的不是同一个锁负责的区域，就不会发生数据竞争，通过这种 “细化锁粒度”的方式来提高并发率。

到了JDK8，ConcurrentHashMap在底层实现上也加入了红黑树，并且，在保证线程安全的方式上也做了改进，

不再使用Segment分段锁机制，利用CAS+synchronized来保证线程安全，synchronized只锁定对应链表或红黑二叉树的首节点，这样只要hash值不冲突就不会发生数据竞争。

如果想深入了解ConcurrentHashMap，可以参考[这篇文章](https://crossoverjie.top/2018/07/23/java-senior/ConcurrentHashMap/)及[http://www.cnblogs.com/chengxiao/p/6842045.html](http://www.cnblogs.com/chengxiao/p/6842045.html)

这里简单介绍一下它的逻辑 

1. 将当前 Segment 中的 table 通过 key 的 hashcode 定位到 HashEntry。
2. 遍历该 HashEntry，如果不为空则判断传入的 key 和当前遍历的 key 是否相等，相等则覆盖旧的 value。
3. 不为空则需要新建一个 HashEntry 并加入到 Segment 中，同时会先判断是否需要扩容。
4. 最后会解除在 1 中所获取当前 Segment 的锁。



### 2.3 ConcurrentSkipListMap

ConcurrentSkipListMap是一种基于SkipList（跳表）实现的，有序存储键值对的Map。

**跳表**

举个简单的例子，想查找书中某个知识点，肯定会先去查看目录中相关的章节，直接找到对应的页数，如果章节数过多，则可以将所有章节再划分为多个部分。

如下图所示：

1. 第一部分

   - 1.1 第一章节
   - 1.2 第二章节

2. 第二部分

   - 2.1 第三章节

     ...

章节可以理解为是目录的索引，而划分部分又是对章节建立索引，通过这种多级索引的方式可以加快查询速度。

**跳表的原理其实就是多级索引。**

下面借用一下小争哥[《数据结构与算法之美》](https://time.geekbang.org/column/article/42896)中的图片：

![2级索引跳表](http://my-blog-to-use.oss-cn-beijing.aliyuncs.com/18-12-9/93666217.jpg)

对于一个没有建立索引的单链表，也就是原始链表，如果想要找到18这个节点，则需要从头开始遍历18次才能找到。如果建立了索引，从第二级的索引上去找，只需要遍历7次即可。针对链表长度比较大的时候，构建索引查找效率的提升就会非常明显。那么是不是索引越多级越好？显然不是，以上图为例，建立三级索引后，依然需要遍历6次。每多建立一级索引，都需要额外的空间来存储索引，**跳表是一种利用空间换时间的算法。**因此需要在执行效率和内存消耗之间找到一个均衡点。

跳表不止可以快速查找，还支持动态的插入、删除，且时间复杂度都是O（logN）。

在链表中，定位好要插入的位置，具体的插入操作的时间复杂度是O（1），但是要找到这个插入的位置，则需要遍历链表，这个查找操作是比较耗时的，而在跳表中查找需要O（logN），相比而言插入的耗时基本可以忽略不计，所以跳表的查找、插入、删除的时间复杂度都为O（logN）。



### 2.3.1 BlockingQueue

ConcurrentQueue
        offer()    代替add() 如果队满，则返回false，不会抛出异常    
        offer("aaa",1,TimeUnit.SECONDS) //按时间段阻塞，插入时最多等待1秒
        poll()     取队列中的元素，如果队列为空，则返回null而不是抛异常
        peek()     看一下，获得队首元素，但不取出 

​		put()   //加入，满了则会等待，线程阻塞
​	    take()  //取出，空了则会等待，线程阻塞

ConcurrentLinkedQueue
BlockingQueue   阻塞队列   阻塞式的生产者消费者模式
    LinkedBlockingQueue 
        无界队列
    ArrayBlockingQueue  
        有界队列，有固定大小的
    LinkedTransferQueue  
        需要实时处理的队列， transfer() 将生产的东西直接交给消费者，不经过队列，如果没有消费者则阻塞 netty用的比较多，可以用add/offer/put
    SynchromusQueue   
        同步队列 容量为0  不能用add，只能用put ，put调用的还是transfer，本质是一种特殊的TransferQueue，不保留数据
DelayQueue  
    也是一个无界队列 在队列中待够指定时间才可以被消费   执行定时任务

阻塞带Blocking，队列，入队操作阻塞，队空，出队操作阻塞。

单端Queue，双端Deque。

|      | 阻塞                                                         | 非阻塞                |
| ---- | ------------------------------------------------------------ | --------------------- |
| 单端 | ArrayBlockingQueue、LinkedBlockingQueue、SynchronousQueue、LinkedTransferQueue、PriorityBlockingQueue、DelayQueue | ConcurrentLinkedQueue |
| 双端 | LinkedBlockingDeque                                          | ConcurrentLinkedDeque |

在任何时刻都只允许一个任务插入或移除元素  
可以挂起和恢复消费者   

ArrayBlockingQueue	底层实现为数组，支持有界队列

LinkedBlockingQueue	底层实现为链表，支持有界队列

SynchronousQueue	内部并不持有队列，此时生产者线程的入队操作必须等待消息者线程的出队操作

LinkedTransferQueue 融合了LinkedBlockingQueue和SynchronousQueue的功能，性能更好。

DelayQueue  延时的无界队列，用于放置实现类Delayed接口的对象，其中的对象只能在其到期后才能从队列取走
PriorityBlockingQueue 基础的优先级队列，可以阻塞读取操作

插入   

- add() 队满则抛异常
- offer() 队满返回false
- offer(e,time,unit) 队满则会在给定时间内阻塞
- put() 如果队列已满则会进入阻塞，直到可插入

移除  

- remove()  队空则抛异常
- poll() 队空则返回null
- poll(e,time,unit) 队空则会在给定时间内阻塞
- take()  队空则进入阻塞，直到可返回

检查  

- element()   队空则抛异常
- peek() 队空则返回null

#### [5.1 BlockingQueue 简单介绍](https://snailclimb.gitee.io/javaguide/#/docs/java/Multithread/并发容器总结?id=_51-blockingqueue-简单介绍)

上面我们己经提到了 ConcurrentLinkedQueue 作为高性能的非阻塞队列。下面我们要讲到的是阻塞队列——BlockingQueue。阻塞队列（BlockingQueue）被广泛使用在“生产者-消费者”问题中，其原因是 BlockingQueue 提供了可阻塞的插入和移除的方法。当队列容器已满，生产者线程会被阻塞，直到队列未满；当队列容器为空时，消费者线程会被阻塞，直至队列非空时为止。

BlockingQueue 是一个接口，继承自 Queue，所以其实现类也可以作为 Queue 的实现来使用，而 Queue 又继承自 Collection 接口。下面是 BlockingQueue 的相关实现类：

![BlockingQueue 的实现类](http://my-blog-to-use.oss-cn-beijing.aliyuncs.com/18-12-9/51622268.jpg)

**下面主要介绍一下:ArrayBlockingQueue、LinkedBlockingQueue、PriorityBlockingQueue，这三个 BlockingQueue 的实现类。**

#### [5.2 ArrayBlockingQueue](https://snailclimb.gitee.io/javaguide/#/docs/java/Multithread/并发容器总结?id=_52-arrayblockingqueue)

**ArrayBlockingQueue** 是 BlockingQueue 接口的有界队列实现类，底层采用**数组**来实现。ArrayBlockingQueue 一旦创建，容量不能改变。其并发控制采用可重入锁来控制，不管是插入操作还是读取操作，都需要获取到锁才能进行操作。当队列容量满时，尝试将元素放入队列将导致操作阻塞;尝试从一个空队列中取一个元素也会同样阻塞。

ArrayBlockingQueue 默认情况下不能保证线程访问队列的公平性，所谓公平性是指严格按照线程等待的绝对时间顺序，即最先等待的线程能够最先访问到 ArrayBlockingQueue。而非公平性则是指访问 ArrayBlockingQueue 的顺序不是遵守严格的时间顺序，有可能存在，当 ArrayBlockingQueue 可以被访问时，长时间阻塞的线程依然无法访问到 ArrayBlockingQueue。如果保证公平性，通常会降低吞吐量。如果需要获得公平性的 ArrayBlockingQueue，可采用如下代码：

```java
private static ArrayBlockingQueue<Integer> blockingQueue = new ArrayBlockingQueue<Integer>(10,true);
```

#### [5.3 LinkedBlockingQueue](https://snailclimb.gitee.io/javaguide/#/docs/java/Multithread/并发容器总结?id=_53-linkedblockingqueue)

**LinkedBlockingQueue** 底层基于**单向链表**实现的阻塞队列，可以当做无界队列也可以当做有界队列来使用，同样满足 FIFO 的特性，与 ArrayBlockingQueue 相比起来具有更高的吞吐量，为了防止 LinkedBlockingQueue 容量迅速增，损耗大量内存。通常在创建 LinkedBlockingQueue 对象时，会指定其大小，如果未指定，容量等于 Integer.MAX_VALUE。

**相关构造方法:**

```java
    /**
     *某种意义上的无界队列
     * Creates a {@code LinkedBlockingQueue} with a capacity of
     * {@link Integer#MAX_VALUE}.
     */
    public LinkedBlockingQueue() {
        this(Integer.MAX_VALUE);
    }

    /**
     *有界队列
     * Creates a {@code LinkedBlockingQueue} with the given (fixed) capacity.
     *
     * @param capacity the capacity of this queue
     * @throws IllegalArgumentException if {@code capacity} is not greater
     *         than zero
     */
    public LinkedBlockingQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException();
        this.capacity = capacity;
        last = head = new Node<E>(null);
    }
```

#### [5.4 PriorityBlockingQueue](https://snailclimb.gitee.io/javaguide/#/docs/java/Multithread/并发容器总结?id=_54-priorityblockingqueue)

**PriorityBlockingQueue** 是一个支持优先级的无界阻塞队列。默认情况下元素采用自然顺序进行排序，也可以通过自定义类实现 `compareTo()` 方法来指定元素排序规则，或者初始化时通过构造器参数 `Comparator` 来指定排序规则。

PriorityBlockingQueue 并发控制采用的是 **ReentrantLock**，队列为无界队列（ArrayBlockingQueue 是有界队列，LinkedBlockingQueue 也可以通过在构造函数中传入 capacity 指定队列最大的容量，但是 PriorityBlockingQueue 只能指定初始的队列大小，后面插入元素的时候，**如果空间不够的话会自动扩容**）。

简单地说，它就是 PriorityQueue 的线程安全版本。不可以插入 null 值，同时，插入队列的对象必须是可比较大小的（comparable），否则报 ClassCastException 异常。它的插入操作 put 方法不会 block，因为它是无界队列（take 方法在队列为空的时候会阻塞）。

**推荐文章：**

《解读 Java 并发队列 BlockingQueue》

https://javadoop.com/post/java-concurrent-queue



### 2.3.2 ConcurrentLinkedQueue

Java 提供的线程安全的 Queue 可以分为**阻塞队列**和**非阻塞队列**，其中阻塞队列的典型例子是 BlockingQueue，非阻塞队列的典型例子是 ConcurrentLinkedQueue，在实际应用中要根据实际需要选用阻塞队列或者非阻塞队列。 **阻塞队列可以通过加锁来实现，非阻塞队列可以通过 CAS 操作实现。**

从名字可以看出，`ConcurrentLinkedQueue`这个队列使用链表作为其数据结构．ConcurrentLinkedQueue 应该算是在高并发环境中性能最好的队列了。它之所有能有很好的性能，是因为其内部复杂的实现。

ConcurrentLinkedQueue 内部代码我们就不分析了，大家知道 ConcurrentLinkedQueue 主要使用 CAS 非阻塞算法来实现线程安全就好了。

ConcurrentLinkedQueue 适合在对性能要求相对较高，同时对队列的读写存在多个线程同时进行的场景，即如果对队列加锁的成本较高则适合使用无锁的 ConcurrentLinkedQueue 来替代。

## 3.总结

### 3.1同步集合与并发集合的比较

​    同步集合：可以简单地理解为通过synchronized实现同步的集合。如果有多个线程调用同步集合的方法，它们将会串行执行；
​    并发集合：jdk5的重要特征，增加了并发包java.util.concurrent.*，以CAS为基础。
​    常见的并发集合：
​    ConcurrentHashMap：线程安全的HashMap实现（ConcurrentHashMap不允许空值或空键，HashMap可以）；
​    CopyOnWriteArrayList：线程安全且在读操作时无锁的ArrayList；
​    CopyOnWriteArraySet：基于CopyOnWriteArrayList，不添加重复元素；
​    ArrayBlockingQueue：基于数组，先进先出，线程安全，可实现指定时间的阻塞读写，并且容量可以限制；
​    LinkedBlockingQueue：基于链表实现，读写各用一把锁，在高并发读写操作的情况下，性能优于ArrayBlockingQueue；
​    同步集合比并发集合慢得多，主要原因是锁，同步集合会对整个Map或List加锁。

### 3.2 JDK 提供的并发容器总结

JDK 提供的这些容器大部分在 `java.util.concurrent` 包中。

- **ConcurrentHashMap:** 线程安全的 HashMap
- **CopyOnWriteArrayList:** 线程安全的 List，在读多写少的场合性能非常好，远远好于 Vector.
- **ConcurrentLinkedQueue:** 高效的并发队列，使用链表实现。可以看做一个线程安全的 LinkedList，这是一个非阻塞队列。
- **BlockingQueue:** 这是一个接口，JDK 内部通过链表、数组等方式实现了这个接口。表示阻塞队列，非常适合用于作为数据共享的通道。
- **ConcurrentSkipListMap:** 跳表的实现。这是一个 Map，使用跳表的数据结构进行快速查找。

### 3.3 面试问题

1. **HashTable的size方法中明明只有一条语句"return count"，为什么还要做同步？**

   同一时间只能有一个线程执行被锁定对象的同步方法，但是对于该对象的非同步方法，可以多条线程同时访问)
   所以，这样就出现了问题，而给size添加了同步之后，意味着线程B调用size方法只有在线程A调用put方法之后，这样就保证了线程安全。

2. 

### 3.4 Map小结



|                       |     Key      |    Value     | 线程安全 | key有序 |
| :-------------------: | :----------: | :----------: | :------: | :-----: |
|        HashMap        |  允许为null  |  允许为null  |    否    |   否    |
|        TreeMap        | 不允许为null |  允许为null  |    否    |   是    |
|       HashTable       | 不允许为null | 不允许为null |    是    |   否    |
|   ConcurrentHashMap   | 不允许为null | 不允许为null |    是    |   否    |
| ConcurrentSkipListMap | 不允许为null | 不允许为null |    是    |   是    |

### 3.5 BlockingQueue小结

注意建议不要使用无界队列，容易OOM



## Reference

&emsp;&emsp;《Java 并发编程实战》  
&emsp;&emsp;《实战Java高并发程序设计》  
&emsp;&emsp;https://snailclimb.gitee.io/javaguide/#/

**感谢阅读**！