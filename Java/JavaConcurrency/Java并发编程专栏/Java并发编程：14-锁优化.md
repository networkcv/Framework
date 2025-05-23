# 锁优化

## 偏向锁

**偏向锁**是一种优化的锁机制，它的核心思想是：**如果一个对象被一个线程访问，那么这个对象的锁就偏向于这个线程。**

在内存层面就是将当前线程的线程id写入到这个锁对象的对象头当中。

后续如果还是同一个线程访问锁对象，就不需要复杂的锁操作，直接访问即可。

### 加锁流程

1. **第一次访问**：
   - `Thread-1` 尝试获取 `obj` 的锁。
   - 如果 `obj` 的锁是未锁定状态（即没有被任何线程持有），`Thread-1` 会将自己的线程ID记录在 `obj` 的锁标记中，这个过程称为**偏向**。
   - 从这一刻起，`obj` 的锁就偏向于 `Thread-1`。
2. **后续访问**：
   - 如果后续还是 `Thread-1` 访问 `obj`，它会检查锁标记中的线程ID是否是自己的ID。
   - 如果是，说明 `obj` 的锁仍然偏向于 `Thread-1`，`Thread-1` 可以直接访问 `obj`，无需再进行复杂的锁操作。
3. **其他线程访问**：
   - 如果另一个线程 `Thread-2` 也想访问 `obj`，它会发现锁标记中的线程ID不是自己的ID。
   - 这时，`Thread-2` 会尝试撤销偏向锁，将锁升级为**轻量级锁**或**重量级锁**，具体取决于当前的锁状态和线程竞争情况。

### **偏向锁的优势**

偏向锁的主要优势在于**减少锁操作的开销**。在单线程场景下，偏向锁可以避免复杂的锁操作，直接访问对象，从而提高性能。

- **减少锁的开销**：如果一个对象只被一个线程访问，偏向锁可以避免复杂的锁操作，直接访问对象。
- **提高性能**：在单线程场景下，偏向锁可以显著提高性能，因为锁操作的开销被大大减少。

### **偏向锁的局限性**

偏向锁也有它的局限性：

- **多线程场景**：如果一个对象被多个线程频繁访问，偏向锁可能会频繁地被撤销和升级，这反而会增加锁操作的开销。
- **锁升级**：当偏向锁被撤销时，锁会被升级为轻量级锁或重量级锁，这个过程可能会带来一定的性能开销。



### 偏向锁的撤销

当一个线程（`Thread-2`）尝试访问一个已经被另一个线程（`Thread-1`）偏向的对象时，它会发现锁标记中的线程ID不是自己的ID。这时，`Thread-2`会尝试撤销偏向锁，将锁升级为轻量级锁或重量级锁。

## 轻量级锁

**轻量级锁**是一种比偏向锁更复杂的锁机制，但比重量级锁更轻量。它的核心思想是：**通过原子操作来尝试获取锁，避免直接进入重量级锁的开销。**具体方式是CAS自旋。

### 工作原理

1. **尝试获取锁**：
   - 当一个线程尝试获取一个对象的锁时，如果发现对象的锁已经被其他线程持有（即锁标记中的线程ID不是自己的ID），它会尝试通过原子操作（如 `CAS`）将锁标记中的线程ID更新为自己的ID。
   - 如果更新成功，说明锁已经被当前线程获取，可以直接进入临界区。
   - 如果更新失败，说明锁已经被其他线程持有，需要进一步处理。
2. **锁膨胀**：
   - 如果多个线程同时尝试获取同一个对象的锁，轻量级锁可能会升级为重量级锁。这个过程称为**锁膨胀**。

## 重量级锁

**重量级锁**是一种传统的锁机制，它的核心思想是：**通过操作系统提供的互斥锁（如 `mutex`）来实现线程同步。**

### 工作原理

1. **获取锁**：
   - 当一个线程尝试获取一个对象的锁时，如果发现锁已经被其他线程持有，它会进入阻塞状态，等待锁的释放。
   - 操作系统会管理这些阻塞的线程，当锁被释放时，操作系统会唤醒等待的线程，让它们重新尝试获取锁。
2. **性能开销**：
   - 重量级锁的性能开销较大，因为它涉及到操作系统的上下文切换和线程阻塞/唤醒操作。
   - 重量级锁适用于线程竞争激烈的情况，因为它可以有效地管理多个线程对共享资源的访问。

## 锁的升级过程

当一个线程尝试访问一个已经被其他线程偏向的对象时，锁会经历以下升级过程：

1. **偏向锁**：
   - 如果对象的锁是偏向锁，且锁标记中的线程ID不是当前线程的ID，当前线程会尝试撤销偏向锁。
   - 撤销偏向锁后，锁会升级为轻量级锁。
2. **轻量级锁**：
   - 当多个线程同时尝试获取同一个对象的锁时，轻量级锁可能会升级为重量级锁。
   - 如果线程竞争不激烈，轻量级锁可以有效减少锁的开销。
3. **重量级锁**：
   - 如果线程竞争激烈，轻量级锁会升级为重量级锁。
   - 重量级锁通过操作系统提供的互斥锁来管理线程同步，适用于多线程竞争的情况。

## 通俗理解

想象一下，你有一把锁，这把锁有三种状态：

1. **偏向锁**：
   - 这把锁默认是偏向于某个特定的人（线程）的。如果这个人再次使用这把锁，可以直接开门，无需复杂的操作。
   - 如果另一个人（线程）想使用这把锁，他会发现锁是偏向于别人的，于是会尝试撤销偏向锁。
2. **轻量级锁**：
   - 撤销偏向锁后，锁会变成轻量级锁。轻量级锁通过一种简单的机制（如原子操作）来尝试获取锁。
   - 如果多个线程同时尝试获取锁，轻量级锁可能会升级为重量级锁。
3. **重量级锁**：
   - 如果线程竞争激烈，轻量级锁会升级为重量级锁。重量级锁通过操作系统提供的互斥锁来管理线程同步。
   - 重量级锁的开销较大，但适用于多线程竞争的情况。

## 总结

- **偏向锁**：适用于单线程场景，减少锁操作的开销。
- **轻量级锁**：适用于线程竞争不激烈的情况，通过原子操作减少锁的开销。
- **重量级锁**：适用于线程竞争激烈的情况，通过操作系统提供的互斥锁管理线程同步。



## 

## 

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

