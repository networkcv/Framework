### 1.多线程获取数据

**需求**

要从一个硬件厂商提供的一个接口获取数据
使用webSocket的方式
我给厂商发送设备编号，厂商把设备每秒的使用数据给我返回来
然后解析，存到数据库
之前我是用单线程搞得，现在马上要加设备而且用fastjson解析起来比较费时，所以打算用多线程来搞
但是设备有时候还会接入和移出，对应的websocket也得给着变化

**思路点**

中断一个webSocket 可以通过关闭底层socket连接的方式，让程序执行完成

考虑让线程执行结束后复用，发现线程结束后无法再启动

考虑使用线程池，哪怕用自己实现的都可以

？防止更新监听设备的定时任务与设备获取的任务并发竞争启动，导致的多线程线程安全问题。

```java
public class RateAcquireRunnale implements Runnable {
	volatile boolean isRun=false;
}
```

定时任务去轮询检测数据库设备更新情况，有更新则hash到对应线程，通过线程WebSocket增加新设备的监听

使用消息队列，新增一件设备后，hash到对应线程  good！



### 2.实例类使用建造者模式





### 3.接口幂等性问题

断点调试Service时，dubbo认为可能发生了网络延迟或者丢包，所以进行了快速失败重试机制，连续多次请求该接口，发送了多条请求，最终可能都会到达，这样就需要对接口进行幂等性考虑

- redis+uuid
- 数据库主键唯一

https://blog.csdn.net/xdkprosperous/article/details/90406882



### 4.Oauth2.0授权机制