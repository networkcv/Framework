
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
