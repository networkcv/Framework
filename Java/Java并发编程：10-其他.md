
## 多线程中的设计模式
### 单例模式
### 不变模式
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

### LongAdder  

和ConcurroncurrentHashMap的思想类似，将一个long划分为多个单元，将并发线程的读写操作分发到多个单元上  
以保证CAS更新能够成功，取值前需要对各个单元进行求和，返回sum  
考虑到如果并发不高的话，这种做法会损耗系统资源，所以默认会维持一个long，如果发生冲突，则会拆分为多个单元  
和AtomicInteger类似的使用方式，在AtomicInteger上进行了热点分离  

```java
public void add(long x)
public void increment()
public void decrement()
public long sum()
public long longValue()
public int intValue()
```



### StampedLock 

读写锁的改进，之前的ReadWriteLock是读写互斥的，StampedLock在读写互斥上做了改进  
读写互斥时，在读线程比较多而写线程比较少的情况下，写线程容易发生饥饿现象，导致一直写不进去  
StampedLock的读可以不阻塞写，读线程在 读取后返回前的时候，写线程完成了数据修改，则读线程需要重新读取  
锁内部维护了一个等待线程队列，所有申请锁，但是没成功的线程都记录在这个队列中，每个节点都有一个标记位，判断当前节点是否已经释放锁  
当一个线程试图获取锁时，会判断当前等待队列尾部节点的标记位是否已经成功释放锁



### CompletableFuture   

工具类，实现了CompletionStage，Java8中对Future的增强，可以流式调用

## 3.线程封闭

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

### 