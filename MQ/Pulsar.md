# [B站Pulsar视频](https://www.bilibili.com/video/BV1CF411v7Dh?p=13&spm_id_from=pageDriver)

# 云原生

DevOps + 持续交付 + 微服务 + 容器

采用开源堆栈（K8s + docker）进行容器化，基于微服务架构提高灵活性和可维护性，借助敏捷方法、DevOps支持持续迭代和运维自动化，利用云平台设施实现弹性伸缩、动态调度、优化资源利用率。

**DevOps**：区别于以前开发code运维部署上线，DevOps 是将部分运维工作交与开发，开发可以自己部署机器，从写代码到上线，而运维人员是负责整个平台的稳定和性能。

**微服务**：应用需要低耦合+高内聚，提供核心的原子能力，比如电商中，商品、交易、订单都是分开的。而商品内部也可以再进行更细粒度的微服务拆分。

**持续交付**：在不影响用户使用服务前提下，频繁将新功能发布给用户使用，在不停用老pod的情况下，发布新pod，然后将流量访问转移到新的pod上，再回收老pod。

**容器化**：运维的时候，不需要关心每个服务所使用的技术栈，每个服务都被无差别的封装在容器中。如k8s和docker。 

# Pulsar优势

## 多租户模式

- 租户和命名空间（namespace）是 Pulsar 支持多租户的两个核心概念。


- 在租户级别，Pulsar 为特定的租户预留合适的存储空间、应用授权与认证机制。


- 在命名空间级别，Pulsar 有一系列的配置策略（policy），包括存储配额、流控、消息过期策略和命名空间之间的隔离策略。

![image-20220326235733033](img/Pulsar/image-20220326235733033.png)

## 灵活的消息模型

- **Pulsar 做了队列模型和流模型的统一**，在 Topic 级别只需保存一份数据，同一份数据可多次消费。以流式、队列等方式计算不同的订阅模型大大提升了灵活度。
  - 队列模型就是我们平时使用的消息队列，消息先进先出。
  - 流模型则是像stream流一样，支持对消息加工

- 同时 pulsar 通过事务采用 Exactly-Once（精准一次）在**进行消息传输过程中，可以确保数据不丢不重**

![image-20220327000117844](img/Pulsar/image-20220327000117844.png)

## 存储与计算分离！

Pulsar 使用计算与**存储分离的云原生架构**，数据不在 Broker 里，而是存在共享存储内部。上层是无状态 Broker，复制消息分发和服务；下层是持久化的存储层 Bookie 集群。Pulsar 存储是分片的，这种构架可以避免扩容时受限制，实现数据的独立扩展和快速恢复

![image-20220327000503972](img/Pulsar/image-20220327000503972.png)

## 分片流

Pulsar 将无界的数据看作是分片的流，分片分散存储在分层存储（tiered storage）。、BookKeeper 集群和

Broker 节点上，而对外提供一个统一的、无界数据的视图。棋次，不需要用户显式迁移数据，减少存储成本并保持近似无限的存储。

![image-20220327000822045](img/Pulsar/image-20220327000822045.png)

## 支持跨地域复制

Pulsar 中的跨地域复制是将 Pulsar 中持久化的消息在多个集群间备份。在 Pulsar2.4.0 中新增了复制订阅模式（Replicated-subscriptions.），在某个集群失效情况下，该功能可以在其他集群恢复消费者的消费状态，从而达到热备模式下消息服务的高可用。

![image-20220327001021926](img/Pulsar/image-20220327001021926.png)



# Pulsar 相关组件

## 层级存储

数据分级存储。可以将一些老的数据存到冷盘中。

- Infinite Stream：以流的方式永久保存原始数据

- 分区的容量不再受限制

- 充分利用云存储或现有的廉价存储（例如 HDFS）

- 数据统一表征：客户端无需关心数据究竟存储在哪里

一条消息过来后，pulsar会先预写日志BookKeeper，收到ack消息后，在Broker中做一份缓存。BookKeeper中的日志会刷到BookKeeper的Ledger进行磁盘存储，所以BookKeeper是真正保存数据的地方。

如果数据需要永久保存，也可以将BookKeeper的数据进行卸载分片，存储到云上或者HDFS上。

![image-20220327001443128](img/Pulsar/image-20220327001443128.png)



## Pulsar IO（Connector）连接器

- Pulsar I0 分为输入（Input）和输出（Output）两个模块，输入代表数据从哪里来，通过 Source 实现数据输入。输出代表数据要往哪里去，通过 Sink 实现数据输出。


- Pulsar 提出了 Connector（也称为 Pulsar I0），用于解决 Pulsar 与周边系统的集成问题，帮助用户高效完成工作。


- 目前 pulsar I0 支持非常多的连接集成操作：例如 HDFS、Spark、。Flink、Flume、ES、HBase 等

![image-20220327002435351](img/Pulsar/image-20220327002435351.png)

## Pulsar Funcations（轻量级计算框架）

说白了就是数据流处理。

Pulsar Functions 是一个轻量级的计算框架，可以给用户提供一个部署简单、运维简单、API 简单的 FASS (Function as a service）平台。Pulsar Functions 提供基于事件的服务，支持有状态与无状态的多语言计算，是对复杂的大数据处理框架的有力补充。

Pulsar Functions 的设计灵感来自于 Heron 这样的流处理引擎，Pulsar Functions 将会拓展 Pulsar 和整个消息领域的未来。使用 Pulsar Functions，用户可以轻松地部署和管理 function，通过 function 从 Pulsar topic 读取数据或者生产新数据到 Pulsar topic。。

# Pulsar 与 Kafka 对比

## 模型概念

- Kafka: producer - topic -  consumer group - consumer（可以将多个消费者放在一个消费组里）
- Pulsar: producer - topic - subsciption - consumer（一个订阅里也可以有多个消费者）

## 消息消费模式！

- Kafka：主要集中在流（Stream）模式，对单个 partitionz 是独占消费，没有共享（Queue）的消费模式。简单来说，对于一个分区，消费组间是多播，组内是单播，只有一个消费者能拿到消息消费，具体的话可以让这个消费者开多线程去消费。

- Pulsar：提供了统一的消息模型和 API。流（Stream）模式-独占和故障切换订阅方式；队列（Queue）模式-共享订阅的方式。共享订阅模式可以让一个订阅内的多个消费者共同去消费一个分区。

  补充：[Kafka下的生产消费者模式与订阅发布模式](https://cloud.tencent.com/developer/article/1639449)

## 消息确认（ack) 

-  Kafka：使用偏移量 offset，当消费失败后重新消费时可能出现消息重复消费问题。
-  Pulsar：使用专门的 cursor 管理。累积确认和 kafka 效果一样；提供单条或选择性确认 

## 消息保留

- Kafka：根据设置的保留期来删除消息，有可能消息没被消费，过期后被删除，不支持 TTL 
- Pulsar：消息只有被所有订阅消费后才会删除，不会丢失数据，也运行设置保留期，保留被消费的数据，支持 TTL

##  数据存储！

​	两者都有类似的消息概念，客户端通过topic与消息系统交互，每个topic都可以分多个分区partition。partition又可以细分为segment，不同的是：

- Kafka：按 partition 来存储到 broker 中。集群扩容时会将 partition 复制到新的broker里 。

-  [Kafka文件存储机制](https://tech.meituan.com/2015/01/13/kafka-fs-design-theory.html)

- [Kafka集群partitions/replicas默认分配解析](https://blog.csdn.net/lizhitao/article/details/41778193)

  当集群节点中包含4个分区partition，2个副本replication时：

  <img src="img/Pulsar/image-20220328104636803.png" alt="image-20220328104636803" style="zoom: 67%;" />

  当集群新增节点时，Partition增加到6个broker时：

  <img src="img/Pulsar/image-20220328104731788.png" alt="image-20220328104731788" style="zoom:67%;" />

  

- Pulsar：将 partition 细分为一个个的 segment 片段来存储，没有存在 broker 中，而是单独放到了 bookie 中。

  ![image-20220327004812504](img/Pulsar/image-20220327004812504.png)

## Kafka 主要痛点

1. Kafka 很难进行扩展，因为 Kafka 把消息持久化在 broker 中，迁移主题分区时，需要把分区的数据完全复制到其他 broker 中，这个操作非常耗时。
2. 当需要通过更改分区大小以获得更多的存储空间时，会与消息索引产生冲突，打乱消息顺序。因此，如果用户需要保证消息的顺序，Kafk 就变得非常棘手了。
3. Kafka 集群的分区再均衡会影响相关生产者和消费者的性能。
4. 发生故障时，Kafka 主题无法保证消总的完整性
5. 需使用 Kafka 需要和 offset 打交道，这点让人很头痛，因为 broker 并不维护 consumer 到消费状态
6.  如果使用率很高，则必须尽快删除旧消息，否则就会出现磁盘空间不够用的问题。
7. 跨地域复制问题

# Pulsar 集群架构

## 基础架构介绍

![image-20220327000503972](img/Pulsar/image-20220327000503972.png)

 

Broker集群负责计算和处理，如处理和负载均衡 producer 发出的消息，并将这些消息分派给 consumer，和将消息存储到 BookKeeper 中。

Bookie集群负责消息的持久化存储。

zookeeper集群用来处理多个 Pulsar集群（broker集群）之间的协调任务。

- Producer：数据生成者，即发送消息的一方。生产者负责创建消息，将其投递到 Pulsar 中。
- Consumer：数据消费者，即接收消息的一方。消费者连接到 Pulsar 并接收消息，进行相应的业务处理。
- Broker：无状态的服务层，负责接收消息、传递消息、集群负载均衡等操作，Broker 不会持久化保存元数据。
- BookKeeper：有状态的持久层，负责持久化地存储消息。
- ZooKeeper：存储 Pulsar 、 BookKeeper 的元数据，集群配置等信息，负责集群间的协调(例如：Topic 与 Broker 的关系)、服务发现等。

## Brokers

Pulsar 的 broker 是一个无状态组件，主要负责运行另外的两个组件：

- 一个 HTTP 服务器，它暴露了 REST 系统管理接口以及在生产者和消费者之间进行 Topic 查找的 API。图中的 Service di scovery。
- 一个调度分发器，它是异步的 TCP 服务器，通过自定义二进制协议应用于所有相关的数据传输。图中的 Dispatcher。

出于性能考虑，消息通常从 Managed Ledger 缓存中分派出去，除非积压超过缓存大小。如果积压的消息对于缓存来说太大了，则 Broker 将开始从 BookKeeper 那里读取 Entries (Entry 同样是 BookKeeper 中的概念，相当于一条记录）。

最后，为了支持全局 Topic 异地复制，Broker 会控制 Replicators 追踪本地发布的条目，并把这些条目用 Java 客户端重新发布到其他区域。

## Zookeeper的元数据存储

Pulsar 使用 Apache Zookeeper 进行元数据存储、集群配置和协调。

- 配置存储：存储租户，命名域和其他需要全局一致的配置项
- 每个集群有自己独立的 ZooKeeper 保存集群内部配置和协调信息，例如归属信息，broker 负载报告，BookKeeper

ledger 信息（这个是 BookKeeper 本身所依赖的）等等。

## bookKeeper 持久化存储

Apache Pulsar 为应用程序提供有保证的信息传递，如果消息成功到达 oroker，就认为其预期到达了目的地。

为了提供这种保证，未确认送达的消息需要持久化存储直到它们被确认送达。这种消息传递模式通常称为持久消息传递。在 Pulsar 内部，所有消息都被保存并同步 N 份，例如，2 个服务器保存四份，每个服务器上面都有镜像的 RAID 存储。

Pulsar 用 Apache bookKeeper 作为持久化存储。bookKeeper。是一个分布式的预写日志（WAL）系统，有如下几个特性：

1. 使 pulsar 能够利用独立的日志，称为 ledgers。可以随着时间的推移为 topic 创建多个 Ledgers
2. 它为处理顺序消息提供了非常有效的存储

3) 保证了多系统挂掉时 Ledgers 的读取一致性
4) 提供不同的 Bookies 之间均匀的 I0 分布的特性
5) 它在容量和吞吐量方面都具有水平伸缩性。能够通过增加 bookies立即增加容量到集群中，并提升吞吐量
6) Bookies 被设计成可以承载数千的并发读写的 ledgers。使用多个磁盘设备（一个用于日志，另一个用于一般存储），这样Bookies可以将读操作的影响和写操作的延迟分隔开。



![image-20220327011757179](img/Pulsar/image-20220327011757179.png)

Ledger 是一个只追加的数据结构，并且只有一个写入器，这个写入器负责多个 bookKeeper 存储节点（就是 Bookies）的写入。Ledger 的条目会被复制到多个 bookies。Ledgers 本身有着非常简单的语义：

- Pulsar Broker 可以创建 ledeger，添加内容到 ledger 和关闭 ledger。


- 当一个 ledger 被关闭后，除非明确的要写数据或者是因为写入器挂掉导致 ledger 关闭，ledger 只会以只读模式打开。


- 最后，当 ledger 中的条目不再有用的时候，整个 ledger 可以被删除（ledger 分布是跨 Bookies 的）。

## 单机Pulsar启动

```
./pulsar standalone
```

##  [Pulsar集群搭建视频-09](https://www.bilibili.com/video/BV1CF411v7Dh?p=9&spm_id_from=pageDriver)





pulsar
组成部分：
消息发送者：生产消息
消息消费者：消费消息
broker：是一个无状态层，负责接收消息、传递消息、集群负载均衡等操作，broker不会持久化保存数据
bookkeeper：有状态的持久层，主要负责持久化地存储消息
zookeeper：存储pulsar、bookkeeper的元数据，集群配置信息，负责集群间的协调、服务发现

1.broker的扩展
当需要支持更多消费者和生产者的时候，可以添加broker节点，在broker节点的负载达到阀值的时候，会将负载迁移到负载较低的节点，这个时候分区也会在多个broker节点中做平衡迁移。

2.bookie扩展
当存储层需要扩展的时候是通过增加bookie节点来实现的

3.分区topic
普通的topic只会被放在一个broker中，这样限制了吞吐量。分区topic支持被多个broker进行处理，增大了吞吐量。
每个topic分区会被分配到某个broker中，生产者和消费者会和这些分区所在的broker进行连接，发送并消费消息。

4.生产者的访问模式
shared：共享模式，多个生产者可以发送到同一个topic
exclusive：在这个情况下只有一个生产者可以发送消息到topic，其他的生产者发送消息到这个topic时会发生错误，只有当独占这个topic的生产者宕机时候，原生产者被驱逐，此时别的生产者才能发送消息到这个topic

5.消息发送的模式
roundrobinpartition 轮询模式，消息如果指定了key会根据key值进行hash计算，然后放到对应的分区中，若没有key则进行轮询发送，是以批次的形式作为边界发送
single partition：如果没有指定key则进行随机发送，指定了则进行hash计算，再发送到对应的分区

6.消费者的确认模式
消费者会发送一个请求来获取消息，在消费者中有一个队列是用来存储broker推送过来的消息
模式：
累计确认：消费者只需要确认最后一条消息，在此之前的所有消息都是确认，不会在此到消费者
单条确认：消费者需要确认每条消息并发送ack给broker
取消确认：若消费者无法消费某条消息，消费者可以发送一个取消确认给broker，broker会重新发送消息给消费者
确认超时：当acktimeout的时候，会发送未确认消息的请求

7.redelivery backoff机制
可以指定消息重试的次数，和消息重发的延迟来重新消费处理消息

8.消息预拉取
consumer客户端会默认预先拉取消息到consumer本地，broker会把这些已经推送到consumer本地的消息记录为pendingack，这些消息既不会被投递到其他的消费者也不会ack超时，除非当前consumer被关闭，消息才会被重新投递

9.未确认的消息处理
未确认的消息会被维护再pendingack，在unackedmessagetracker中维护了一个时间轮，新的追踪消息会被放在最后一个刻度，每次调度都是处理头一个刻度做消息重投递，并且清除掉这个刻度里面的消息，并且增加一个新的刻度在队尾。

10.消息去重的原理
消息去重的一种方式就是确保消息只生成一次，这种方式消息去重的工作交由应用去做
在pulsar中broker支持配置开启消息去重，开启后一条消息被多次发送到topic上，这条消息也只会被持久化一次在硬盘上。

producer对每一个发送的消息，都会采用递增的方式生成一个唯一的sequenceID,这个消息会放在message的元数据中传递给broker，broker会记录接收到的最大的sequenceID和处理好的最大的sequenceID，若收到的消息的sequenceID大于这两个记录的sequenceID，则认为是新消息，便开始持久化操作，不是则不做后续操作。

11.消息的延迟传递
原理：
实现延迟投递有两种方式：
deliverafter
deliverat
