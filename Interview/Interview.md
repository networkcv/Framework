### 开场

- 面试必不可少的问题：自我介绍
- 你为什么要离开上一家公司
- 你做的这些项目中，哪个项目你最熟悉（后边的面试将针对这个项目仔细询问，从用的框架到自己负责的部分）
- 一点建议：介绍项目时简单说一下项目背景
- 说说你的项目中的亮点有哪些。
- 画一下你的项目的架构图。

### Java基础

IO和NIO的区别。

接口和抽象类的区别。

Object 类中的方法

ArrayList默认长度，增长因子，怎么增长的

集合中add（）和put（）的区别

- int和Integer的自动拆箱/装箱相关问题。
- 常量池相关问题。
- ==和equals的区别。
- 重载和重写的区别。
- String和StringBuilder、StringBuffer的区别。
- 静态变量、实例变量、局部变量线程安全吗，为什么。
- try、catch、finally都有return语句时执行哪个。

1、使用length属性获取数组长度，public、private、protected、friendly区别
2、Collection和Collections区别
3、String s=new String(‘xyz’);创建了几个object对象
4、short s1;
s1=s1+1;是否有错？
5、Overriding和Overloading区别
6、Set里面的元素不能重复，用什么方法区分重复与否。
7、给出一个常见的runtime exception。
8、error和exception区别。
9、List和Set是否继承自Collection接口。
10、abstract class和interface 的区别。
11、是否可以继承String类。
12、try{}里有一个return语句，紧跟在try后的finally里的code会不会被执行，什么时候执行，return前执行还是return后执行。
13、最有效率的方法算2*8等于几
14、两个对象值相同，x.equal(y)==true，但是却可有不同的hashcode，这句话对不对。
15、值传递和引用传递
16、switch是否作用在byte、long、string上。
17、ArrayList和Vector区别，HashMap和Hashtable区别（了解这几个类的底层jdk中的编码方式）。
18、GC是什么，为什么要有GC，简单介绍GC。
19、float f=3.4是否正确。
20、介绍Java中的Collection framework。
21、Collection框架中实现比较方法
22、String和Stringbuffer的区别
23、final、finally、finalize区别
24、面向对象的特征
25、String是最基本的数据类型吗。
26、运行时异常和一般异常的区别
27、说出ArrayList、Vector、Linkedlist的存储性能和特性
28、heap和stack区别
29、Java中的异常处理机制的简单原理和应用
30、垃圾回收的原理和特点，并考虑2种回收机制
31、说出一些常用的 集合类和方法
32、描述一下JVM加载Class文件的原理和机制
33、排序的几种方法，了解。（算法和数据结构在面试的时候还没有被问到）
34、Java语言如何进行异常处理，throws，throw，try catch finally代表什么意义，try块中可以抛出异常吗
35、一个’.java’源文件是否可以包括多个类，有什么限制。
36、Java中有几种类型流，jdk为每种类型的流提供了一些抽象类以供继承，请分别说出它们是哪些类。
37、Java中会存在内存泄漏吗，请简单描述。
38、静态变量和实例变量的区别。
39、什么是Java序列化，如何实现java序列化。
40、是否可以从一个static方法内部发生对非static方法调用。
41、写clone方法，通常都有一行代码。
42、Java中如何跳出多重嵌套循环
43、说出常用类、包、接口，各举5个。
44、Java中实现线程的方法，用关键字修饰同步方法。
45、同步和异步区别。
46、线程同步的方法。
47、字符串常用方法，字符串转化为整型数方法，整型数转化为字符串方法。

为什么要使用接口进行开发？

介绍一下Java中的static关键字（介绍一下静态变量、静态代码块、静态方法等等）

js是异步还是同步的，了解一下异步和同步的差别

反射的原理了解过吗？

简单介绍一下集合的分类（一般介绍完，面试官就会针对性的再问几个问题，下边会列举出几个）

list里边ArrayList和LinkedList的区别（有一家公司还问了我和vector的区别）

map的分类（回答上来有的面试官还问HashMap和Hashtable的区别）

遍历map有几种方式（虽然不要求全说出来，最起码要了解一种或几种遍历Map的方式）

Java里边关键字final、finally和finalize的区别

了解装箱和拆箱吗（又问了基本数据类型和包装类型的差别以及使用包装类型时要注意判断是否为空）

Ajax的原理（可以了解一下js里实现ajax的基本步骤）

能简单列举几个熟悉的设计模式吗？（回答上来后，面试官追问：简单介绍介绍单例模式，简单介绍一下工厂模式）

struts2的执行流程

struts2中怎么向后台传值（前台页面标签name和action变量名字相同，并对变量设置get set方法等等）

struts2怎么返回一个json数据（以及怎么解析json）

简单介绍一下Spring（IOC和AOP，以及这两个核心技术分别用来做什么）

AOP的实现用到了什么技术（我答了jdk代理和cglib代理，还吹了一些细节）

IOC，为什么要让Spring来管理对象

了解过hibernate和mybatis吗（还问了为什么要使用orm框架）

- HashMap、LinkedHashMap、ConcurrentHashMap、ArrayList、LinkedList的底层实现。
- HashMap和Hashtable的区别。
- ArrayList、LinkedList、Vector的区别。
- HashMap和ConcurrentHashMap的区别。
- HashMap和LinkedHashMap的区别。
- HashMap是线程安全的吗。
- ConcurrentHashMap是怎么实现线程安全的。
- Java 反射？反射有什么缺点？你是怎么理解反射的（为什么框架需要反射）？
- 谈谈对 Java 注解的理解，解决了什么问题？
- 内部类了解吗？匿名内部类了解吗？
- BIO和NIO区别,4核cpu，100个http连接，用BIO和NIO分别需要多少个线程
- 假如我们需要存500个数需要多大的HashMap？
- HashMap的负载因子。

### Concurrency

- synchronized 和 volatile 
- synchronized 的实现方式
- synchronized 的 锁升级过程
- Lock CAS
- 线程池
- 创建线程的3种方式。
- 什么是线程安全。
- Runnable接口和Callable接口的区别。
- wait方法和sleep方法的区别。
- synchronized、Lock、ReentrantLock、ReadWriteLock。
- 介绍下CAS(无锁技术)。
- volatile关键字的作用和原理。
- 什么是ThreadLocal。
- 创建线程池的4种方式。
- ThreadPoolExecutor的内部工作原理。
- 分布式环境下，怎么保证线程安全。
- Java线程池有哪些参数？阻塞队列有几种？拒绝策略有几种？
- 死锁

### Java8新特性

- Lambda
- Stream，带来的性能提升

### JVM

- JVM原理
- 调优的基本步骤
- CPU100%，如何解决
- GC机制
- 介绍下垃圾收集机制（在什么时候，对什么，做了什么）。
- 垃圾收集有哪些算法，各自的特点。
- 类加载的过程。
- 双亲委派模型。
- 有哪些类加载器。
- 能不能自己写一个类叫java.lang.String。
- jvm 内存结构
- jvm 调优参数
- 什么是类加载？
- 何时类加载？
- java的类加载流程？
- 知道哪些类加载器。类加载器之间的关系？
- 类加载器之间的关系？
- **类加载器的双亲委派** （结合tomcat说一下双亲委派）
- **为什么需要双亲委派**
- Java内存模型
- 栈中存放什么数据，堆中呢？
- 大对象放在哪个内存区域
- 堆区如何分类
- 垃圾回收有哪些算法
- GC的全流程
- GC中老年代用什么回收方法？

### JavaWeb

- 你登录一个网站 后续的一个请求是怎么知道是刚才那个人请求的
- session
- 单点登录
- 在a网站登录，跳转到b网站 你是怎么把a网站的信息带过去的
- 过滤器、拦截器、监听器；

### Spring相关

- IOC
- AOP
- Spring的好处
- SpringMVC的工作流程
- SpringBoot启动方式
- SpringBoot配置多数据源
- SpringBoot配置方式注入Bean
- SpringBoot自定义配置文件
- Hibernate和Mybatis的区别。
- Spring MVC和Struts2的区别。
- Spring用了哪些设计模式。
- Spring中AOP主要用来做什么。
- Spring注入bean的方式。
- 什么是IOC，什么是依赖注入。
- Spring是单例还是多例，怎么修改。
- Spring事务隔离级别和传播性。
- 介绍下Mybatis/Hibernate的缓存机制。
- Mybatis的mapper文件中#和$的区别。
- Mybatis的mapper文件中resultType和resultMap的区别。
- Mybatis中DAO层接口没有写实现类，Mapper中的方法和DAO接口方法是怎么绑定到一起的，其内部是怎么实现的。
- spring boot和spring的区别
- ioc 和 aop(ioc流程、aop实现原理)、spring aop异常处理、当一段代码被try catch后再发生异常时，aop的异常通知是否执行，为什么？
- spring bean的生命周期说一下
- spring data jpa底层是什么？
- hibernate和mybatis区别
- spring boot 过滤器
- spring boot 拦截器
- Spring动态代理默认用哪一种
- 写出spring jdk动态代理的实现。
- 画出spring boot处理一个http请求的全过程

### MyBatis

- Mybatis中<where>节点和where子句的区别

- mybatis中id回显

- Mybatis中动态sql

- Mybatis缓存

  

### MySQL

- mysql优化

- 如何优化查询，explain

- 索引相关

  - 一张表建多少个索引
  - 组合索引

- 为什么要用B+树及其数据结构的优势

- 介绍下B树、二叉树。

- 数据库引擎区别

- Mysql分页，参数是什么

- 说一下数据库三范式（也可补充说明一下反三范式）

  数据库中为什么分多个用户

  索引是什么，有什么作用（当时我以一本书的目录进行举例说明的，作用就是提高检索效率）

  使用索引有什么需要注意的地方吗（在插入数据时会更新索引，影响插入效率这方面）

  索引的分类（回答上来后又问了唯一索引和主键的区别）

  组合索引使用时的注意事项（组合索引一块使用会走索引；只用到组合索引中第一个字段时也会走索引；使用like时通配符写到开头不走索引等等）

  多表查询的方式

  表分区了解吗（垂直分表和水平分表，按时间字段就行分表是属于什么。  注：按时间字段进行分表属于水平分表，垂直分是按列分表，一般有不常用的大字段时考虑垂直分表）

  说一下纯SQL优化以及你是怎么优化的

  写过存储过程和触发器吗

  关于事务的处理，为什么要回滚（简单说一下要符合事务的基本特点ACID）

  union和union all的区别

  最后就是要记得一些基本的SQL语法

  group by、内连接和外连接。

  - 给题目让你手写SQL。
  - 有没有SQL优化经验。
  - Mysql索引的数据结构。
  - SQL怎么进行优化。
  - SQL关键字的执行顺序。
  - 有哪几种索引。
  - 什么时候该（不该）建索引。
  - Explain包含哪些列。
  - Explain的Type列有哪几种值。
  - 非关系型数据库和关系型数据库的区别？
  - 事务的四大特性
  - MySQL 事务隔离级别？默认是什么级别？
  - 乐观锁与悲观锁的区别
  - 数据库两种存储引擎的区别
  - 最左前缀匹配原则及它的原因
  - 大表优化的思路
  - where和having的区别
  - 分库分表
  - explain 命令
  - 如何加快数据库查询速度
  - 聚集索引和非聚集索引的区别
  - 什么时候不该使用索引？
  - 索引底层的数据结构？
  - B+树做索引比红黑树好在哪里？

### Redis

- 技术选型，与Memcache
- 其底层数据结构及其实现的数据线结构
- Redis的IO模型
- Redis为什么效率高
- Redis 的持久化，RDB和AOP的优缺点，RDB恢复到一个点，然后通过AOF把剩下的数据补全
- Redis 集群相关
- 缓存穿透，缓存雪崩、缓存击穿
- 项目中 redis 是怎么用的？解决了什么问题？
- 说一下有缓存情况下查询的流程以及有缓存情况下修改的流程。
- redis有哪些数据结构
- redis内存满了怎么办
- redis内存淘汰算法除了lru还有哪些
- 分布式缓存可能出现的问题
- 缓存穿透问题

### Dubbo

- 调用链路，比如中间的服务暴露过程

- 调用过程中的监视器

- 在启动的时候的Container的容器与Spring融合

- 保证幂等
  - 数据库的唯一性ID

### 微服务/分布式

- 分布式锁的实现。redis  zk
- 分布式session存储解决方案。
- 为什么要网关？
- 限流的算法有哪些？
- 为什么要分布式 id ？分布式 id 生成策略有哪些？
- 了解RPC吗？有哪些常见的 RPC 框架？
- 如果让你自己设计 RPC 框架你会如何设计？
- Dubbo 了解吗？Spring Cloud 了解吗？

### 数据结构和算法

- 介绍下栈和队列。
- LRU 算法了解吗？你能实现一个吗？
- 写排序算法（快排、堆排）
- 布隆过滤器了解吗？

### 设计题

- 假如有10亿个数，只有一个重复，内存只能放下5亿个数，怎么找到这个重复的数字？
- 如何设计一个秒杀系统（服务端、数据库、分布式）？分布式系统的设计？
- 有一个服务器专门接收大量请求，怎么设计？
- 如果让你自己设计 RPC 框架你会如何设计？
- 怎么快速出现一个stackoverflow错误？

### 设计模式

- 策略模式
- 状态模式
- 先问你熟悉哪些设计模式
- 然后再具体问你某个设计模式具体实现和相关扩展问题。
- 阅读Spring源码的时候什么设计模式最让你影响深刻？如何使用？
- 单例模式，单例模式的使用场景
- 观察者模式，观察者模式的使用场景

### Linux

- 常用的linux命令。

### 计算机网络

- 计算机网络的一些常见状态码
- ping 所使用的协议
- TCP的三次握手与四次挥手的内容
- TCP为什么连接是三次握手而断开是四次握手
- TCP与UDP的区别及使用场景
- 一次完整的HTTP请求所经的步骤
- http 如何保存登录信息(没太搞懂意思)
- Cookie 和 Session的关系

### 其他

- Restful 了解吗？简单说一下自己对它的认识，如果我要返回一个 boolean 类型的数据怎么办？
- 之前的离职原因
- 平常怎么做技术学习的
- 你感觉自己的优缺点有哪些
- 你克服过什么困难
- 你有什么想问我的