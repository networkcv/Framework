15. HashTable的size方法中明明只有一条语句"return count"，为什么还要做同步
    (同一时间只能有一个线程执行被锁定对象的同步方法，但是对于该对象的非同步方法，可以多条线程同时访问)
    所以，这样就出现了问题，而给size添加了同步之后，意味着线程B调用size方法只有在线程A调用put方法之后，这样就保证了线程安全。


20. 同步集合与并发集合的比较
        同步集合：可以简单地理解为通过synchronized实现同步的集合。如果有多个线程调用同步集合的方法，它们将会串行执行；
        并发集合：jdk5的重要特征，增加了并发包java.util.concurrent.*，以CAS为基础。
        常见的并发集合：
        ConcurrentHashMap：线程安全的HashMap实现（ConcurrentHashMap不允许空值或空键，HashMap可以）；
        CopyOnWriteArrayList：线程安全且在读操作时无锁的ArrayList；
        CopyOnWriteArraySet：基于CopyOnWriteArrayList，不添加重复元素；
        ArrayBlockingQueue：基于数组，先进先出，线程安全，可实现指定时间的阻塞读写，并且容量可以限制；
        LinkedBlockingQueue：基于链表实现，读写各用一把锁，在高并发读写操作的情况下，性能优于ArrayBlockingQueue；
        同步集合比并发集合慢得多，主要原因是锁，同步集合会对整个Map或List加锁。



ConcurrentMap
    不考虑并发
    HashMap
        线程不安全
    TreeMap
        底层通过红黑树实现

    低并发
    HashTable
        对整个容器进行加锁，效率比较低
    Collections.synchronizedXXX(List/Map)
            对线程不安全的容器进行加锁

    高并发
    ConcurrentHashMap
        将对整个容器的锁拆解为多个对容器的分段锁    1.8后没有分段锁，采用CAS？
    ConcurrentSkipListMap
        底层用跳表实现的Map 对插入的数据进行排序

并发容器
Collections.synchronizedXXX(List/Map)  对线程不安全的容器进行加锁

ConcurrentQueue
        offer()    代替add() 如果队满，则返回false，不会抛出异常    
        offer("aaa",1,TimeUnit.SECONDS) //按时间段阻塞，插入时最多等待1秒
        poll()     取队列中的元素，如果队列为空，则返回null而不是抛异常
        peek()     看一下，获得队首元素，但不取出 

        put()   //加入，满了则会等待，线程阻塞
        take()  //取出，空了则会等待，线程阻塞

    高并发
    CopyOnWriteList
        读的量特别大，写的量特别小，例如 监听器的配置
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


