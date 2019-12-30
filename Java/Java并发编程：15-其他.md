## 并发调试

### 多线程调试的方法
使用条件断点或异常断点
### 线程dump及分析
jstack 进程号
jstack -l 进程号 查看详细情况
## Lambda 表达式    

http://blog.oneapm.com/apm-tech/226.html
https://blog.csdn.net/qq_36951116/article/details/80296967

将方法作为参数传递，类似于一种匿名函数
@FunctionalInterface 用这个注解标记
每个 Lambda 表达式都能隐式地赋值给函数式接口，例如，我们可以通过 Lambda 表达式创建 Runnable 接口的引用。
Runnable r = () -> System.out.println("hello world");
当不指明函数式接口时，编译器会自动解释这种转化：
new Thread(
() -> System.out.println("hello world")
).start();

new Thread(()->System.out.print(1)).start();

t(new WorkerInterface() {
public String doSomework(String s) {
    return s;
}
});

t((String s) -> {
    return s;
});
    
简写： t(s -> s);

以下是一些 Lambda 表达式及其函数式接口：
Consumer<Integer>  c = (int x) -> { System.out.println(x) };
BiConsumer<Integer, String> b = (Integer x, String y) -> System.out.println(x + " : " + y);
Predicate<String> p = (String s) -> { s == null };

## JDK8对并发的新支持

### StampedLock 

读写锁的改进，之前的ReadWriteLock是读写互斥的，StampedLock在读写互斥上做了改进  
读写互斥时，在读线程比较多而写线程比较少的情况下，写线程容易发生饥饿现象，导致一直写不进去  
StampedLock的读可以不阻塞写，读线程在 读取后返回前的时候，写线程完成了数据修改，则读线程需要重新读取  
锁内部维护了一个等待线程队列，所有申请锁，但是没成功的线程都记录在这个队列中，每个节点都有一个标记位，判断当前节点是否已经释放锁  
当一个线程试图获取锁时，会判断当前等待队列尾部节点的标记位是否已经成功释放锁



### CompletableFuture   

工具类，实现了CompletionStage，Java8中对Future的增强，可以流式调用



## 乐观锁和悲观锁

### 悲观锁

总是假设最坏的情况，每次去拿数据的时候都认为别人会修改，所以每次在拿数据的时候都会上锁，这样别人想拿这个数据就会阻塞直到它拿到锁（**共享资源每次只给一个线程使用，其它线程阻塞，用完后再把资源转让给其它线程**）。传统的关系型数据库里边就用到了很多这种锁机制，比如行锁，表锁等，读锁，写锁等，都是在做操作之前先上锁。Java中`synchronized`和`ReentrantLock`等独占锁就是悲观锁思想的实现。

### 乐观锁

总是假设最好的情况，每次去拿数据的时候都认为别人不会修改，所以不会上锁，但是在更新的时候会判断一下在此期间别人有没有去更新这个数据，可以使用版本号机制和CAS算法实现。**乐观锁适用于多读的应用类型，这样可以提高吞吐量**，像数据库提供的类似于**write_condition机制**，其实都是提供的乐观锁。在Java中`java.util.concurrent.atomic`包下面的原子变量类就是使用了乐观锁的一种实现方式**CAS**实现的。



**乐观锁与悲观锁的比较**


悲观锁，就是思想很悲观，每次去拿数据的时候都认为别人会去修改，所以每次在拿数据的时候都会上锁，这样别人想拿这个数据就会block直到它拿到锁。传统的关系型数据库中就用到了很多悲观锁的机制，比如行锁，表锁，读锁，写锁等，都是在做操作前先上锁，synchronized也是悲观锁。

乐观锁，就是思想很乐观，每次去拿数据的时候都认为别人不会去修改，所以不会上锁，但是在更新的时候会去判断一下在此期间别人有没有去更新这个数据，可以使用版本号或时间戳等机制（提交版本必须大于记录当前版本才能执行更新）。像数据库如果提供类似于write_condition机制的其实都是提供的乐观锁，CAS思想也是乐观锁。

两种锁各有优缺点，乐观锁适用于写比较少读比较多的情况，即冲突真的很少发生的时候，这样可以省去了锁的开销，加大了整个系统的吞吐量。但如果经常产生冲突，上层应用不断进行retry，这样反而降低了性能，所以这种情况下悲观锁比较合适。
乐观锁事实上并没有使用锁机制。



#### [两种锁的使用场景](https://snailclimb.gitee.io/javaguide/#/docs/essential-content-for-interview/面试必备之乐观锁与悲观锁?id=两种锁的使用场景)

从上面对两种锁的介绍，我们知道两种锁各有优缺点，不可认为一种好于另一种，像**乐观锁适用于写比较少的情况下（多读场景）**，即冲突真的很少发生的时候，这样可以省去了锁的开销，加大了系统的整个吞吐量。但如果是多写的情况，一般会经常产生冲突，这就会导致上层应用会不断的进行retry，这样反倒是降低了性能，所以**一般多写的场景下用悲观锁就比较合适。**

### [乐观锁常见的两种实现方式](https://snailclimb.gitee.io/javaguide/#/docs/essential-content-for-interview/面试必备之乐观锁与悲观锁?id=乐观锁常见的两种实现方式)

> **乐观锁一般会使用版本号机制或CAS算法实现。**

#### [1. 版本号机制](https://snailclimb.gitee.io/javaguide/#/docs/essential-content-for-interview/面试必备之乐观锁与悲观锁?id=_1-版本号机制)

一般是在数据表中加上一个数据版本号version字段，表示数据被修改的次数，当数据被修改时，version值会加一。当线程A要更新数据值时，在读取数据的同时也会读取version值，在提交更新时，若刚才读取到的version值为当前数据库中的version值相等时才更新，否则重试更新操作，直到更新成功。

**举一个简单的例子：** 假设数据库中帐户信息表中有一个 version 字段，当前值为 1 ；而当前帐户余额字段（ balance ）为 $100 。

1. 操作员 A 此时将其读出（ version=1 ），并从其帐户余额中扣除 $50（ $100-$50 ）。
2. 在操作员 A 操作的过程中，操作员B 也读入此用户信息（ version=1 ），并从其帐户余额中扣除 $20 （ $100-$20 ）。
3. 操作员 A 完成了修改工作，将数据版本号加一（ version=2 ），连同帐户扣除后余额（ balance=$50 ），提交至数据库更新，此时由于提交数据版本大于数据库记录当前版本，数据被更新，数据库记录 version 更新为 2 。
4. 操作员 B 完成了操作，也将版本号加一（ version=2 ）试图向数据库提交数据（ balance=$80 ），但此时比对数据库记录版本时发现，操作员 B 提交的数据版本号为 2 ，数据库记录当前版本也为 2 ，不满足 “ 提交版本必须大于记录当前版本才能执行更新 “ 的乐观锁策略，因此，操作员 B 的提交被驳回。

这样，就避免了操作员 B 用基于 version=1 的旧数据修改的结果覆盖操作员A 的操作结果的可能。

#### [2. CAS算法](https://snailclimb.gitee.io/javaguide/#/docs/essential-content-for-interview/面试必备之乐观锁与悲观锁?id=_2-cas算法)

即**compare and swap（比较与交换）**，是一种有名的**无锁算法**。无锁编程，即不使用锁的情况下实现多线程之间的变量同步，也就是在没有线程被阻塞的情况下实现变量的同步，所以也叫非阻塞同步（Non-blocking Synchronization）。**CAS算法**涉及到三个操作数

- 需要读写的内存值 V
- 进行比较的值 A
- 拟写入的新值 B

当且仅当 V 的值等于 A时，CAS通过原子方式用新值B来更新V的值，否则不会执行任何操作（比较和替换是一个原子操作）。一般情况下是一个**自旋操作**，即**不断的重试**。

### [乐观锁的缺点](https://snailclimb.gitee.io/javaguide/#/docs/essential-content-for-interview/面试必备之乐观锁与悲观锁?id=乐观锁的缺点)

> ABA 问题是乐观锁一个常见的问题

#### [1 ABA 问题](https://snailclimb.gitee.io/javaguide/#/docs/essential-content-for-interview/面试必备之乐观锁与悲观锁?id=_1-aba-问题)



#### [2 循环时间长开销大](https://snailclimb.gitee.io/javaguide/#/docs/essential-content-for-interview/面试必备之乐观锁与悲观锁?id=_2-循环时间长开销大)

**自旋CAS（也就是不成功就一直循环执行直到成功）如果长时间不成功，会给CPU带来非常大的执行开销。** 如果JVM能支持处理器提供的pause指令那么效率会有一定的提升，pause指令有两个作用，第一它可以延迟流水线执行指令（de-pipeline）,使CPU不会消耗过多的执行资源，延迟的时间取决于具体实现的版本，在一些处理器上延迟时间是零。第二它可以避免在退出循环的时候因内存顺序冲突（memory order violation）而引起CPU流水线被清空（CPU pipeline flush），从而提高CPU的执行效率。

#### [3 只能保证一个共享变量的原子操作](https://snailclimb.gitee.io/javaguide/#/docs/essential-content-for-interview/面试必备之乐观锁与悲观锁?id=_3-只能保证一个共享变量的原子操作)

CAS 只对单个共享变量有效，当操作涉及跨多个共享变量时 CAS 无效。但是从 JDK 1.5开始，提供了`AtomicReference类`来保证引用对象之间的原子性，你可以把多个变量放在一个对象里来进行 CAS 操作.所以我们可以使用锁或者利用`AtomicReference类`把多个共享变量合并成一个共享变量来操作。