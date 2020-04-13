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



### 4.Dubbo接口访问超时

### 5.Oauth2.0授权机制



### 冗余字段设计

在说命名之前先来看看数据库设计。

之前的项目中都是用pojo来作为数据传输的实体，从web层 serveice 层 dao层 一个pojo用到底，能这么用的前提是，在数据库设计阶段已经将数据表进行了反范式设计，表在设计的时候已经考虑到大致的业务场景。所以表会增加一些冗余的字段来减少多表关联查询；同样pojo也多了相关字段，我们可以很便捷的将查询结果进行封装然后传输，对于一些需要组合展示的数据，我们可以将结果封装到Map或者DTO中

针对一些小的项目这样做无疑是非常便利的。但是大的项目这样并不适用。

1. 数据量比较大，一个表动辄百万条数据，不提冗余字段占据的存储空间，多一个冗余字段就需要而额外对冗余字段的维护，降低了数据库性能，在维护冗余的过程中可能会出现短暂的数据不一致请况，这在需要强一致性的场景中是不允许的，如支付交易中。
2. 业务场景复杂且多变，在设计表结构时，对于冗余字段的设计无法覆盖到全部场景。因此必然会出现关联多表的数据查询，可以在**数据库中直接关联查询到结果返回** 或者 **在业务层中多次单表查询后将结果组装** 。

如果不设计冗余字段，也慢不了多少，建立完外键或者索引之后查询速度会提升很多。如果觉得这么整写sql语句会变长不如冗余字段好看，那也简单，建立视图呀，看上去跟冗余字段差不多，但它的内心却是十分纯洁不像冗余字段繁琐难维护。
如果冗余字段可控容易维护，那么维护它；如果冗余字段不可控，那么冗余的毫无价值，删除掉追求简洁。

分层领域模型规约：

- **DO**（ Data Object）：与数据库表结构一一对应，通过DAO层向上传输数据源对象。
- **DTO**（ Data Transfer Object）：数据传输对象，Service或Manager向外传输的对象。
- BO（ Business Object）：业务对象。 由Service层输出的封装业务逻辑的对象。
- AO（ Application Object）：应用对象。 在Web层与Service层之间抽象的复用对象模型，极为贴近展示层，复用度不高。
- **VO**（ View Object）：显示层对象，通常是Web向模板渲染引擎层传输的对象。
- POJO（ Plain Ordinary Java Object）：在本手册中， POJO专指只有setter/getter/toString的简单类，包括DO/DTO/BO/VO等。
- Query：数据查询对象，各层接收上层的查询请求。 注意超过2个参数的查询封装，禁止使用Map类来传输。

领域模型命名规约：

- 数据对象：xxxDO，xxx即为数据表名。
- 数据传输对象：xxxDTO，xxx为业务领域相关的名称。
- 展示对象：xxxVO，xxx一般为网页名称。
- POJO是DO/DTO/BO/VO的统称，禁止命名成xxxPOJO。

https://www.lagou.com/lgeduarticle/17564.html

### .Java实体命名思考

### .数据库设计范式与反范式设计

班级表里

### .Java8 Lambda



Mabtis报找不到方法，有几个重要的检查点：

1. 如果你的xml文件没有放在resource中，而是放在包下，maven默认不会将其编译到classpath下，需要在pom.xml中手动指定

   ```xml
           <resources>
               <resource>
                   <directory>src/main/java</directory>
                   <includes>
                       <include>**/*.properties</include>
                       <include>**/*.xml</include>
                   </includes>
                   <filtering>false</filtering>
               </resource>
           </resources>
   ```

2. 如果是springboot的话，你的启动类的扫描包配置是否正确，还有启动类的位置是否和controller、service这些包在同级目录或者比它们大

