# 深入理解Java中的引用

本系列文章首先会介绍Reference类，为之后介绍的强引用、软引用、弱引用和虚引用打下基础。
 最后会介绍虚引用在DirectBuffer回收中的应用。

## 引用（Reference）

在介绍四种不同类型的引用之前先看一下他们的父类：`java.lang.ref.Reference`。看以看到Reference 有五个成员变量：**referent，queue，pending，next，discovered。**下面会一一介绍各个成员变量的作用。

### Reference状态

在介绍五个成员变量之前，首页要明确一点，Reference是有状态的，Reference对象的一共有四种状态。如下图所示

![img](img/5611237-39f910daeec0ef14.webp)

image.png

Reference对象所处状态不同，成员变量的值也会变化。

- **Active**: Reference对象新建时处于该状态。
- **Pending**: 当Reference包装的referent 对象要被垃圾回收时，JVM会把Reference设置成pending状态。如果Reference创建时指定了ReferenceQueue，那么会被ReferenceHandler线程处理进入到ReferenceQueue队列中，如果没有就进入Inactive状态。
- **Enqueue**: 进入ReferenceQueue中的对象，等待被回收
- **Inactive**: Reference对象从ReferenceQueue取出来并被处理掉。处于Inactive的Reference对象状态不能再改变

### 核心成员变量

1）**referent**: 表示被包装的对象
 下面代码中 obj 就是被包装的对象。

```java
Object obj = new Object()
WeakReference<Object> wo = new WeakReference<Object>(obj);
```

2） **queue**: 表示被包装的对象被回收时，需要被通知的队列，该队列在Reference构造函数中指定。当referent被回收的时候，Reference对象就处在了Pending状态，Reference会被放入到该队列中，如果构造函数没有指定队列，那么就进入Inactive状态。

```java
public abstract class Reference<T> {
    private T referent;
	volatile ReferenceQueue<? super T> queue;
    
    Reference(T referent) {
        this(referent, null);
    }

    Reference(T referent, ReferenceQueue<? super T> queue) {
        this.referent = referent;
        this.queue = (queue == null) ? ReferenceQueue.NULL : queue;
    }
}
```

3）**pending**: 表示等待被加入到queue的Reference 列表。

```java
    /* List of References waiting to be enqueued.  The collector adds
     * References to this list, while the Reference-handler thread removes
     * them.  This list is protected by the above lock object. The
     * list uses the discovered field to link its elements.
     */
    private static Reference<Object> pending = null;
```

这里有两个需要注意的点：

- 一是要区分Pending是引用状态，而pending是Reference的静态字段。
- 二是理解pending的意义，该字段用static 修饰，表示内存中的引用实例会共用这个静态变量，pending理解链表有点费解，因为代码层面上看这明明就是Reference对象。其实当Reference处在Pending状态时，他的pending字段被赋值成了下一个要处理的对象（即下面讲的discovered），通过discovered可以拿到下一个对象并且赋值给pending，直到最后一个，所以这里就可以把它当成一个链表。而discovered是JVM的垃圾回收器添加进去的，大家可以不用关心底层细节。

4）**discovered**: 当处于Reference处在Pending状态：discovered为pending集合中的下一个元素；其他状态：discovered为null

```java
    /* When active:   next element in a discovered reference list maintained by GC (or this if last)
     *     pending:   next element in the pending list (or null if last)
     *   otherwise:   NULL
     */
    transient private Reference<T> discovered;  /* used by VM */
```

上述discovered与pending的关系可以用下图表示

![img](img/5611237-e256908d0c80a2e9.webp)

5） **next**: 当Reference对象在queue中时（即Reference处于Enqueued状态），next描述当前引用节点所存储的下一个即将被处理的节点。

```java
   /* When active:   NULL
     *     pending:   this
     *    Enqueued:   next reference in queue (or this if last)
     *    Inactive:   this
     */
    @SuppressWarnings("rawtypes")
    Reference next;
```

ReferenceHandler线程会把Pending状态的Reference放入ReferenceQueue中，上面说的**next，discovered** 字段在入队之后也会发生变化，下一小节会介绍。

### ReferenceQueue入队过程

上面说到ReferenceHandler线程会把Pending状态的Reference对象放入到ReferenceQueue队列中。
 查看ReferenceQueue中入队源代码。

```java
boolean enqueue(Reference<? extends T> r) { /* Called only by Reference class */
        synchronized (lock) {
            // Check that since getting the lock this reference hasn't already been
            // enqueued (and even then removed)
            ReferenceQueue<?> queue = r.queue;
            if ((queue == NULL) || (queue == ENQUEUED)) {
                return false;
            }
            assert queue == this;
            //设置queue状态
            r.queue = ENQUEUED;
            //改变next指针
            r.next = (head == null) ? r : head;
            head = r;
            queueLength++;
            if (r instanceof FinalReference) {
                sun.misc.VM.addFinalRefCount(1);
            }
            lock.notifyAll();
            return true;
        }
    }
```

可以看到入队的Reference节点r进入队列，Reference节点被放在队列头，所以这是一个**先进后出队列**。 入队的示意图如下：

![img](img/5611237-0a1dc360d23886cb.webp)



### ReferenceHandler线程

Reference类中另一个比较重要的成员是ReferenceHandler，ReferenceHandler是一个守护线程。当JVM加载Reference的时候，就会启动这个线程，并且线性优先级被设置为**高优先级**。

```java
public abstract class Reference<T> {
	static {
        ThreadGroup tg = Thread.currentThread().getThreadGroup();
		...
        Thread handler = new ReferenceHandler(tg, "Reference Handler");

        handler.setPriority(Thread.MAX_PRIORITY);
        handler.setDaemon(true);
        handler.start();
	    ...
    }
    private static class ReferenceHandler extends Thread {
        ...
    }
}
```

![image-20200401135404024](img/image-20200401135404024.png)

```java
private static class ReferenceHandler extends Thread {
    ReferenceHandler(ThreadGroup g, String name) {
        super(g, name);
    }

    public void run() {
        while (true) {
            tryHandlePending(true);
        }
    }
    
    static boolean tryHandlePending(boolean waitForNotify) {
        Reference<Object> r;
        Cleaner c;
        try {
            synchronized (lock) {
                if (pending != null) {
                    r = pending;
                    // 'instanceof' might throw OutOfMemoryError sometimes
                    // so do this before un-linking 'r' from the 'pending' chain...
                    c = r instanceof Cleaner ? (Cleaner) r : null;
                    // unlink 'r' from 'pending' chain
                    pending = r.discovered;
                    r.discovered = null;
                } else {
                    // The waiting on the lock may cause an OutOfMemoryError
                    // because it may try to allocate exception objects.
                    if (waitForNotify) {
                        lock.wait();
                    }
                    // retry if waited
                    return waitForNotify;
                }
            }
        } catch (OutOfMemoryError x) {
            Thread.yield();
            return true;
        } catch (InterruptedException x) {
            return true;
        }

        // Cleaner是 虚引用的子类，在入队前进调用 clean()
        if (c != null) {
            c.clean();
            return true;
        }

        // 进行入队操作
        ReferenceQueue<? super Object> q = r.queue;
        if (q != ReferenceQueue.NULL) q.enqueue(r);
        return true;
    }
}
```

通过上面代码可以看到ReferenceHandler线程做的是不断的检查pending是否为null, 如果不为null,将pending对象进行入队操作，而pending的赋值由JVM操作。**所以ReferenceQueue在这里作为JVM与上层Reference对象管理之间的消息传递方式**。

## 总结

Reference具有的**Active（活跃状态）、Pending（未决定状态）、Enqueued（入队状态）、Inactive（不活跃状态）**四种状态。

- 在Reference对象创建时，引用处于**Active**的状态，除了要传入指向的对象 referent，还支持传入一个ReferenceQueue，如果不传入ReferenceQueue，则指向对象被垃圾回收时，Reference对象会直接进入 Inactive状态。
- 在传入queue的情况下，如果指向对象被垃圾回收，则Reference对象会进入**Pending** 状态，在Reference类中有一个静态成员 pending，该成员由JVM来帮我们赋值，具体会指向等待进入ReferenceQueue的Reference对象，而 ReferenceHandler 的任务是不停的检查 pending 是否为 null，不为空的话，将当前Reference对象入队（先进后出的队列），并将pending指向下一个需要入队的Reference（也就是discovered 指向的对象）。
- 如果是Cleaner（ Cleaner是 虚引用的子类）则会在入队前调用 clean() 方法，完成入队后，Reference对象的状态变为 **Enqueued**。
- Reference对象从ReferenceQueue取出来并被处理会变成**Inactive**。

**Reference**

https://www.jianshu.com/p/d275812816e5

https://www.jianshu.com/p/e279b8b492aa