### !并发级别
- 阻塞
难进易出，当一个线程进入临界区后，其他线程必须在临界区外等待，进入后完成操作就可以释放资源
- 非阻塞
  - 无障碍  
易进难出，无障碍是一种最弱的非阻塞调度，自由进入临界区，但是在释放资源时，会判断是否发生数据竞争，比如A线程读取数据x，要释放资源时，系统会判断当前的临界区内x值是否发生变化，如果发生变化，则会回滚A线程的操作
  - 无锁  
无锁的前提是无障碍的，而且要保证有一个线程可以胜出  
通过一个实例可以很好的理解，线程A修改了x的值，要释放资源出临界区时，线程B修改了x的值，系统会回滚线程A的操作，线程B要出临界区时，线程C又修改了x的值，这下该回滚B的操作了，线程C要出临界区的时候，之前被回滚的A完成了修改操作，所以C也要被回滚了，此处A打算出临界区，B又来了，这样就形成了一个闭环  
还是A、B、C三个线程修改x值的问题，要想打破之前形成的闭环，就必须要有一个线程先出去，通过竞争的方式每次选出一个线程胜出，胜出的可以释放临界区资源
  - 无等待  
无状态的前提是无锁的，要求所有线程都必须在有限步内完成，而且也是无饥饿的


## 无锁
### 无锁类的原理详解
CAS  compare and swap
CAS有三个操作数：内存值(也就是要更新的变量)V、旧的预期值A、要修改的值(新值)B，当且仅当预期值A和内存值V相同时，将内存值修改为B并返回true，否则什么都不做并返回false。  
虽然CAS会先读取值，然后比较，最后再赋值，但是这整个操作是一个原子操作，由一条CPU指令完成，通过比较交换指令实现，省去了线程频繁调度的开销，所以比基于锁的方式性能更好

### 无锁类的使用
- AtomicXXX类  
底层维护一个被volatile修饰的基本类型值  
AtomicXXX只是对其的一个封装  
```java
//无锁的方式大都是通过这样的方式实现，
public final int getAndIncrement(){
  for(;;){
    int current =get(); //获取内存中当前的值，旧的预期值A
    int next=current+1; //对已获取到的值递增，要修改为的新值B
    //判断内存中当前的值是否和current相等
    //如果相等，则将当前值修改为目标值next，并返回true
    //如果不等，则会返回false，说明内存中已经有别的线程修改了值且进行提交，那么当前线程会进入下一次循环，重新获取新值，再重复上面的动作，直至内存的值和get()的值一样，也就是没人修改内存的值
    if(compareAndSet(current,next)){ 
      return current;  //返回修改前的值
    }
  }
}
```
- AtomicReference<V>   
针对线程引用来保证线程安全
- AtomicStampedReference<V>  
解决过程状态上敏感的问题  
A->B->A  
线程1先get(),线程2将A改为B，线程3又将B改为A，线程1compareAndSet时发现还是A,返回true，但其实已经不是之前的那个A了，如果是数值的话，只关注结果，不会出现问题，但如果是过程状态，则会出现问题  
使用AtomicStampedReference，会给每个状态打上时间戳，compareAndSet的时候不光会比较值还会比较时间戳

- Unsafe  
提供了非安全的操作，提供内JDK内部使用，非公开的API，在不同JDK中可能有较大差异
可以根据偏移量去设置值
提供park()  把线程停下来
负责底层的CAS操作，如AtomicInter的底层实现

- AtomicIntergerArray  
作用和AtomicInteger类似，通过CAS原理保证数组中每个元素的线程安全

- AtomicIntegerFieldUpdater
将已有的基本类型的变量，封装到AtomicIntegerFieldUpdater
底层使用反射技术

- LockFreeVector   
无锁vector  使用二维数组模拟一维数组  
```java
//第一数组放8个元素，第二个数组放16个元素，第三个数组放32个元素。。，减少数组扩容的性能消耗
private final AtomicReferenceArray<AtomicReferenceArray<E>> buckets; 
```
- ConcurrentLinkedQueue 非阻塞队列
- ConcurrentSkipList 跳表


25. 乐观锁与悲观锁的比较
    悲观锁，就是思想很悲观，每次去拿数据的时候都认为别人会去修改，所以每次在拿数据的时候都会上锁，这样别人想拿这个数据就会block直到它拿到锁。传统的关系型数据库中就用到了很多悲观锁的机制，比如行锁，表锁，读锁，写锁等，都是在做操作前先上锁，synchronized也是悲观锁。

    乐观锁，就是思想很乐观，每次去拿数据的时候都认为别人不会去修改，所以不会上锁，但是在更新的时候会去判断一下在此期间别人有没有去更新这个数据，可以使用版本号或时间戳等机制（提交版本必须大于记录当前版本才能执行更新）。像数据库如果提供类似于write_condition机制的其实都是提供的乐观锁，CAS思想也是乐观锁。

    两种锁各有优缺点，乐观锁适用于写比较少读比较多的情况，即冲突真的很少发生的时候，这样可以省去了锁的开销，加大了整个系统的吞吐量。但如果经常产生冲突，上层应用不断进行retry，这样反而降低了性能，所以这种情况下悲观锁比较合适。
    乐观锁事实上并没有使用锁机制。

  5:写一个程序,证明AtomXXx类比synchronized更高效
比较简单的自增自减的原子性问题可以使用AtomicXXX类来解决
AtomicInteger count = new AtomicInteger(0);
用count.incrementAndGet() 替代count++  

公平锁 ：谁等的时间长，谁获得锁
public static Lock lock =new ReentrantLock(true); //公平锁
public void run(){
    for(int i=0;i<100;i++){
        lock.lock();
        System.out.println(Thread.currentThread().getName()+" 获得锁");
        lock.unlock();
    }
}

6: AtomXXx类可以保证可见性吗?请写一个程序来证明
AtomicXXX类的一个方法既可以保证可见性，也可以保证原子性

7: 一个程序证明AtomXX类的多个方法并不保证原子性
AtomicInteger count=new AtomicInteger(0);
if(count.get()<1000)
    count.incrementAndGet();

8:写一个程序模拟死锁
synchronized (a) {
            Thread.sleep(1000);
            synchronized (b) {
                System.out.println("A ok");
            }
        }
synchronized (b) {
            Thread.sleep(1000);
            synchronized (a) {
                System.out.println("B ok");
            }
        }




