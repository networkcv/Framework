



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


# 并发调试和JDK8新特性
### 多线程调试的方法
使用条件断点或异常断点
### 线程dump及分析
jstack 进程号
jstack -l 进程号 查看详细情况
### JDK8对并发的新支持
- LongAdder  
和ConcurroncurrentHashMap的思想类似，将一个整数划分为多个单元，将并发线程的读写操作分发到多个单元上  
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
- CompletableFuture   
工具类，实现了CompletionStage，Java8中对Future的增强，可以流式调用
- StampedLock 
读写锁的改进，之前的ReadWriteLock是读写互斥的，StampedLock在读写互斥上做了改进  
读写互斥时，在读线程比较多而写线程比较少的情况下，写线程容易发生饥饿现象，导致一直写不进去  
StampedLock的读可以不阻塞写，读线程在 读取后返回前的时候，写线程完成了数据修改，则读线程需要重新读取  
锁内部维护了一个等待线程队列，所有申请锁，但是没成功的线程都记录在这个队列中，每个节点都有一个标记位，判断当前节点是否已经释放锁  
当一个线程试图获取锁时，会判断当前等待队列尾部节点的标记位是否已经成功释放锁


Lambda 表达式    
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

