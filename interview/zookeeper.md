## Zookeeper
ZooKeeper 是一个典型的分布式数据一致性解决方案，分布式应用程序可以基于 ZooKeeper 实现诸如数据发布/订阅、负载均衡、命名服务、分布式协调/通知、集群管理、Master 选举、分布式锁和分布式队列等功能。

Zookeeper（业界简称zk）是一种提供配置管理、分布式协同以及命名的中心化服务，这些提供的功能都是分布式系统中非常底层且必不可少的基本功能

Dubbo就是资源调度和治理中心的管理工具。
Zookeeper 一个最常用的使用场景就是用于担任服务生产者和服务消费者的注册中心(提供发布订阅服务)。 
1.jpg
节点角色说明：
- Provider: 暴露服务的服务提供方。
- Consumer: 调用远程服务的服务消费方。
- Registry: 服务注册与发现的注册中心。
- Monitor: 统计服务的调用次调和调用时间的监控中心。
- Container: 服务运行容器。
调用关系说明：
- 0. 服务容器负责启动，加载，运行服务提供者。
- 1. 服务提供者在启动时，向注册中心注册自己提供的服务。
- 2. 服务消费者在启动时，向注册中心订阅自己所需的服务。
- 3. 注册中心返回服务提供者地址列表给消费者，如果有变更，注册中心将基于长连接推送变更数据给消费者。
- 4. 服务消费者，从提供者地址列表中，基于软负载均衡算法，选一台提供者进行调用，如果调用失败，再选另一台调用。
- 5. 服务消费者和提供者，在内存中累计调用次数和调用时间，定时每分钟发送一次统计数据到监控中心。

zooKeeper 主要提供下面几个功能：1、集群管理：容错、负载均衡。2、配置文件的集中管理3、集群的入口。

zookeeper奇数个节点的原因是其容错机制，只要半数以上节点存活，ZooKeeper 就能正常服务

而为了保证高可用，zookeeper需要以集群形态来部署，这样只要集群中大部分机器是可用的（能够容忍一定的机器故障），那么zookeeper本身仍然是可用的。客户端在使用zookeeper时，需要知道集群机器列表，通过与集群中的某一台机器建立TCP连接来使用服务，客户端使用这个TCP链接来发送请求、获取结果、获取监听事件以及发送心跳包。如果这个连接异常断开了，客户端可以连接到另外的机器上。

Zookeeper是一个工具，可以实现集群中的分布式协调服务。

所谓的分布式协调服务，就是在集群的节点中进行可靠的消息传递，来协调集群的工作。

Zookeeper之所以能够实现分布式协调服务，靠的就是它能够保证分布式数据一致性。

所谓的分布式数据一致性，指的就是可以在集群中保证数据传递的一致性。

 Zookeeper能够提供的分布式协调服务包括：数据发布订阅、负载均衡、命名服务、分布式协调/通知、集群管理、配置管理、分布式锁、分布式队列等功能


### 会话（Session）
Session 指的是 ZooKeeper 服务器与客户端会话。在 ZooKeeper 中，一个客户端连接是指客户端和服务器之间的一个 TCP 长连接。客户端启动的时候，首先会与服务器建立一个 TCP 连接，从第一次连接建立开始，客户端会话的生命周期也开始了。通过这个连接，客户端能够通过心跳检测与服务器保持有效的会话，也能够向Zookeeper服务器发送请求并接受响应，同时还能够通过该连接接收来自服务器的Watch事件通知。 Session的sessionTimeout值用来设置一个客户端会话的超时时间。当由于服务器压力太大、网络故障或是客户端主动断开连接等各种原因导致客户端连接断开时，只要在sessionTimeout规定的时间内能够重新连接上集群中任意一台服务器，那么之前创建的会话仍然有效。

在为客户端创建会话之前，服务端首先会为每个客户端都分配一个sessionID。由于 sessionID 是 Zookeeper 会话的一个重要标识，许多与会话相关的运行机制都是基于这个 sessionID 的，因此，无论是哪台服务器为客户端分配的 sessionID，都务必保证全局唯一。

### Znode
在谈到分布式的时候，我们通常说的“节点"是指组成集群的每一台机器。然而，在Zookeeper中，“节点"分为两类，第一类同样是指构成集群的机器，我们称之为机器节点；第二类则是指数据模型中的数据单元，我们称之为数据节点一一ZNode。

Zookeeper将所有数据存储在内存中，数据模型是一棵树（Znode Tree)，由斜杠（/）的进行分割的路径，就是一个Znode，例如/foo/path1。每个上都会保存自己的数据内容，同时还会保存一系列属性信息。

在Zookeeper中，node可以分为持久节点和临时节点两类。所谓持久节点是指一旦这个ZNode被创建了，除非主动进行ZNode的移除操作，否则这个ZNode将一直保存在Zookeeper上。而临时节点就不一样了，它的生命周期和客户端会话绑定，一旦客户端会话失效，那么这个客户端创建的所有临时节点都会被移除。

###  ZooKeeper 集群角色介绍
最典型集群模式： Master/Slave 模式（主备模式）。在这种模式中，通常 Master服务器作为主服务器提供写服务，其他的 Slave 服务器从服务器通过异步复制的方式获取 Master 服务器最新的数据提供读服务。

但是，在 ZooKeeper 中没有选择传统的 Master/Slave 概念，而是引入了Leader、Follower 和 Observer 三种角色。如下图所示

ZooKeeper 集群中的所有机器通过一个 Leader 选举过程来选定一台称为 “Leader” 的机器，Leader 既可以为客户端提供写服务又能提供读服务。除了 Leader 外，Follower 和 Observer 都只能提供读服务。Follower 和 Observer 唯一的区别在于 Observer 机器不参与 Leader 的选举过程，也不参与写操作的“过半写成功”策略，因此 Observer 机器可以在不影响写性能的情况下提升集群的读性能。


当 Leader 服务器出现网络中断、崩溃退出与重启等异常情况时，ZAB 协议就会进人恢复模式并选举产生新的Leader服务器。这个过程大致是这样的：

Leader election（选举阶段）：节点在一开始都处于选举阶段，只要有一个节点得到超半数节点的票数，它就可以当选准 leader。
Discovery（发现阶段）：在这个阶段，followers 跟准 leader 进行通信，同步 followers 最近接收的事务提议。
Synchronization（同步阶段）:同步阶段主要是利用 leader 前一阶段获得的最新提议历史，同步集群中所有的副本。同步完成之后 准 leader 才会成为真正的 leader。
Broadcast（广播阶段） 到了这个阶段，Zookeeper 集群才能正式对外提供事务服务，并且 leader 可以进行消息广播。同时如果有新的节点加入，还需要对新节点进行同步。

6.1 ZAB 协议&Paxos算法
Paxos 算法应该可以说是 ZooKeeper 的灵魂了。但是，ZooKeeper 并没有完全采用 Paxos算法 ，而是使用 ZAB 协议作为其保证数据一致性的核心算法。另外，在ZooKeeper的官方文档中也指出，ZAB协议并不像 Paxos 算法那样，是一种通用的分布式一致性算法，它是一种特别为Zookeeper设计的崩溃可恢复的原子消息广播算法。

6.3 ZAB 协议两种基本的模式：崩溃恢复和消息广播
ZAB协议包括两种基本的模式，分别是 崩溃恢复和消息广播。当整个服务框架在启动过程中，或是当 Leader 服务器出现网络中断、崩溃退出与重启等异常情况时，ZAB 协议就会进人恢复模式并选举产生新的Leader服务器。当选举产生了新的 Leader 服务器，同时集群中已经有过半的机器与该Leader服务器完成了状态同步之后，ZAB协议就会退出恢复模式。其中，所谓的状态同步是指数据同步，用来保证集群中存在过半的机器能够和Leader服务器的数据状态保持一致。

当集群中已经有过半的Follower服务器完成了和Leader服务器的状态同步，那么整个服务框架就可以进人消息广播模式了。 当一台同样遵守ZAB协议的服务器启动后加人到集群中时，如果此时集群中已经存在一个Leader服务器在负责进行消息广播，那么新加人的服务器就会自觉地进人数据恢复模式：找到Leader所在的服务器，并与其进行数据同步，然后一起参与到消息广播流程中去。正如上文介绍中所说的，ZooKeeper设计成只允许唯一的一个Leader服务器来进行事务请求的处理。Leader服务器在接收到客户端的事务请求后，会生成对应的事务提案并发起一轮广播协议；而如果集群中的其他机器接收到客户端的事务请求，那么这些非Leader服务器会首先将这个事务请求转发给Leader服务器。

通过阅读本文，想必大家已从 ①ZooKeeper的由来。 -> ②ZooKeeper 到底是什么 。-> ③ ZooKeeper 的一些重要概念（会话（Session）、 Znode、版本、Watcher、ACL）-> ④ZooKeeper 的特点。 -> ⑤ZooKeeper 的设计目标。-> ⑥ ZooKeeper 集群角色介绍 （Leader、Follower 和 Observer 三种角色）-> ⑦ZooKeeper &ZAB 协议&Paxos算法。 这七点了解了 ZooKeeper 。



10分钟看懂！基于Zookeeper的分布式锁: https://blog.csdn.net/qiangcuo6087/article/details/79067136
ZooKeeper集群与Leader选举: https://zhuanlan.zhihu.com/p/60083015
图解zookeeper FastLeader选举算法: http://codemacro.com/2014/10/19/zk-fastleaderelection/


zookeeper  一致 有头 数据树
一致：数据会在所有的服务器中保持一致
有头：通过ZAB协议使得集群正常运行(超过半数节点正常)))中有且只有一个leader
数据树：它的内存模型是一颗数据树
解决分布式系统数据一致性问题
1.安装zookeeper
2.统一修改配置文件
    cp zoo_sample.cfg zoo.config
    server.1=192.168.1.1:2888:3888
    server.2=192.168.1.2:2888:3888
    server.3=192.168.1.3:2888:3888
    2181 为zk服务端对外提供服务的端口
    2888 为leader和flower之间通讯使用的端口
    3888 选举leader使用的端口号
3.分别在tmp/zookeeper中配置id
    vim myid 
    1/2/3   三台机器分别的编号
    !wq
4.启动zookeeper服务端
    ./zkServer.sh start/stop
    more *.out 查看启动日志 
    jps 显示当前所有java进程pid的命令
    ./zkServer.sh status 观察运行状态 如查看谁是leader/follower 
    flower会处理读服务，将写服务交由leader来处理
5.使用zookeeper客户端测试
    ./zkClient.sh -server  连接至zk集群的任意节点
    zookeeper和redis相似，将数据树保存在内存中便于操作
    

内存模型
zookeeper在内存中的模型是一颗数据树，所有服务端节点看到的数据树都是一致的
 /
 |--app1--------data    
 |--app2--------data
    |-- data1---data
    |-- data2---data

集群角色
ZooKeeper没有使用Master/Slave的概念，而是将集群中的节点分为了3类角色：
- Leader
在一个ZooKeeper集群中，只能存在一个Leader，这个Leader是集群中事务请求唯一的调度者和处理者，所谓事务请求是指会改变集群状态的请求；Leader根据事务ID可以保证事务处理的顺序性。
如果一个集群中存在多个Leader，这种现象称为「脑裂」。相当于原本一个大集群，裂出多个小集群，他们之间的数据是不会相互同步的。「脑裂」后集群中的数据会变得非常混乱。
Leader根据事务ID可以保证事务处理的顺序性,
- Follower
Follower角色的ZooKeeper服务只能处理非事务请求；如果接收到客户端事务请求会将请求转发给Leader服务器；参与Leader选举；参与Leader事务处理投票处理。
Follower发现集群中Leader不可用时会变更自身状态，并发起Leader选举投票，最终集群中的某个Follower会被选为Leader。
- Observer
Observer与Follower很像，可以处理非事务请求；将事务请求转发给Leader服务器。
与Follower不同的是，Observer不会参与Leader选举；不会参与Leader事务处理投票。
Observer用于不影响集群事务处理能力的前提下提升集群的非事务处理能力。

Leader选举
Leader在集群中是非常重要的一个角色，负责了整个事务的处理和调度，保证分布式数据一致性的关键所在。既然Leader在ZooKeeper集群中这么重要所以一定要保证集群在任何时候都有且仅有一个Leader存在。
如果集群中Leader不可用了，需要有一个机制来保证能从集群中找出一个最优的服务晋升为Leader继续处理事务和调度等一系列职责。这个过程称为Leader选举。

选举机制
ZooKeeper选举Leader依赖下列原则并遵循优先顺序：
1、选举投票必须在同一轮次中进行
如果Follower服务选举轮次不同，不会采纳投票。
2、数据最新的节点优先成为Leader
数据的新旧使用事务ID判定，事务ID越大认为节点数据约接近Leader的数据，自然应该成为Leader。
3、比较server.id，id值大的优先成为Leader
如果每个参与竞选节点事务ID一样，再使用server.id做比较。server.id是节点在集群中唯一的id，myid文件中配置。
不管是在集群启动时选举Leader还是集群运行中重新选举Leader。集群中每个Follower角色服务都是以上面的条件作为基础推选出合适的Leader，一旦出现某个节点被过半推选，那么该节点晋升为Leader。

过半原则
ZooKeeper集群会有很多类型投票。Leader选举投票；事务提议投票；这些投票依赖过半原则。就是说ZooKeeper认为投票结果超过了集群总数的一半，便可以安全的处理后续事务。
事务提议投票
假设有3个节点组成ZooKeeper集群，客户端请求添加一个节点。Leader接到该事务请求后给所有Follower发起「创建节点」的提议投票。如果Leader收到了超过集群一半数量的反馈，继续给所有Follower发起commit。此时Leader认为集群过半了，就算自己挂了集群也是安全可靠的。
Leader选举投票
假设有3个节点组成ZooKeeper集群，这时Leader挂了，需要投票选举Leader。当相同投票结果过半后Leader选出。
集群可用节点
ZooKeeper集群中每个节点有自己的角色，对于集群可用性来说必须满足过半原则。这个过半是指Leader角色 + Follower角色可用数大于集群中Leader角色 + Follower角色总数。
假设有5个节点组成ZooKeeper集群，一个Leader、两个Follower、两个Observer。当挂掉两个Follower或挂掉一个Leader和一个Follower时集群将不可用。因为Observer角色不参与任何形式的投票。
所谓过半原则算法是说票数 > 集群总节点数/2。其中集群总节点数/2的计算结果会向下取整。

## 应用场景
集群配置一致
高可用HA(High Availability)
    通常使用冗余方式 建立备用的服务器，在主服务器挂了的时候顶上  active/standby
发布/订阅(pub/sub)
命名服务(naming service)
负载均衡(load balance)
分布式锁

## zookeeper 都有哪些使用场景
### 分布式协调
这个其实是 zookeeper 很经典的一个用法，简单来说，就好比，你 A 系统发送个请求到 mq，然后 B 系统消息消费之后处理了。那 A 系统如何知道 B 系统的处理结果？用 zookeeper 就可以实现分布式系统之间的协调工作。A 系统发送请求之后可以在 zookeeper 上对某个节点的值注册个监听器，一旦 B 系统处理完了就修改 zookeeper 那个节点的值，A 系统立马就可以收到通知，完美解决
![03_分布式协调](./03_分布式协调.png)

### 分布式锁
举个栗子。对某一个数据连续发出两个修改操作，两台机器同时收到了请求，但是只能一台机器先执行完另外一个机器再执行。那么此时就可以使用 zookeeper 分布式锁，一个机器接收到了请求之后先获取 zookeeper 上的一把分布式锁，就是可以去创建一个 znode，接着执行操作；然后另外一个机器也尝试去创建那个 znode，结果发现自己创建不了，因为被别人创建了，那只能等着，等第一个机器执行完了自己再执行。
![04_分布式锁](./04_分布式锁.png)

### 配置信息管理
zookeeper 可以用作很多系统的配置信息的管理，比如 dubbo，kafka、storm 等等很多分布式系统都会选用 zookeeper 来做一些元数据、配置信息的管理
![配置信息管理](./05_配置信息管理.png)

### HA高可用性
这个应该是很常见的，比如 hadoop、hdfs、yarn 等很多大数据系统，都选择基于 zookeeper 来开发 HA 高可用机制，就是一个重要进程一般会做主备两个，主进程挂了立马通过 zookeeper 感知到切换到备用进程。
![HA高可用性](./06_HA高可用性.png)


## 一般实现分布式锁都有哪些方式
### redis 分布式锁
官方叫做 RedLock 算法，是 redis 官方支持的分布式锁算法。

这个分布式锁有 3 个重要的考量点：

互斥（只能有一个客户端获取锁）
不能死锁
容错（只要大部分 redis 节点创建了这把锁就可以）
redis 最普通的分布式锁
第一个最普通的实现方式，就是在 redis 里使用 setnx 命令创建一个 key，这样就算加锁。

SET resource_name my_random_value NX PX 30000
执行这个命令就 ok。

NX：表示只有 key 不存在的时候才会设置成功。（如果此时 redis 中存在这个 key，那么设置失败，返回 nil）
PX 30000：意思是 30s 后锁自动释放。别人创建的时候如果发现已经有了就不能加锁了。
释放锁就是删除 key ，但是一般可以用 lua 脚本删除，判断 value 一样才删除：

-- 删除锁的时候，找到 key 对应的 value，跟自己传过去的 value 做比较，如果是一样的才删除。
if redis.call("get",KEYS[1]) == ARGV[1] then
    return redis.call("del",KEYS[1])
else
    return 0
end
为啥要用 random_value 随机值呢？因为如果某个客户端获取到了锁，但是阻塞了很长时间才执行完，比如说超过了 30s，此时可能已经自动释放锁了，此时可能别的客户端已经获取到了这个锁，要是你这个时候直接删除 key 的话会有问题，所以得用随机值加上面的 lua 脚本来释放锁。

但是这样是肯定不行的。因为如果是普通的 redis 单实例，那就是单点故障。或者是 redis 普通主从，那 redis 主从异步复制，如果主节点挂了（key 就没有了），key 还没同步到从节点，此时从节点切换为主节点，别人就可以 set key，从而拿到锁。

RedLock 算法
这个场景是假设有一个 redis cluster，有 5 个 redis master 实例。然后执行如下步骤获取一把锁：

获取当前时间戳，单位是毫秒；
跟上面类似，轮流尝试在每个 master 节点上创建锁，过期时间较短，一般就几十毫秒；
尝试在大多数节点上建立一个锁，比如 5 个节点就要求是 3 个节点 n / 2 + 1；
客户端计算建立好锁的时间，如果建立锁的时间小于超时时间，就算建立成功了；
要是锁建立失败了，那么就依次之前建立过的锁删除；
只要别人建立了一把分布式锁，你就得不断轮询去尝试获取锁。
![redis-redlock](./07_redis-redlock.png)

Redis 官方给出了以上两种基于 Redis 实现分布式锁的方法，详细说明可以查看：https://redis.io/topics/distlock 。

### zk 分布式锁
zk 分布式锁，其实可以做的比较简单，就是某个节点尝试创建临时 znode，此时创建成功了就获取了这个锁；这个时候别的客户端来创建锁会失败，只能注册个监听器监听这个锁。释放锁就是删除这个 znode，一旦释放掉就会通知客户端，然后有一个等待着的客户端就可以再次重新加锁。
```java
/**
 * ZooKeeperSession
 */
public class ZooKeeperSession {

    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);

    private ZooKeeper zookeeper;
    private CountDownLatch latch;

    public ZooKeeperSession() {
        try {
            this.zookeeper = new ZooKeeper("192.168.31.187:2181,192.168.31.19:2181,192.168.31.227:2181", 50000, new ZooKeeperWatcher());
            try {
                connectedSemaphore.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("ZooKeeper session established......");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取分布式锁
     * 
     * @param productId
     */
    public Boolean acquireDistributedLock(Long productId) {
        String path = "/product-lock-" + productId;

        try {
            zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
            return true;
        } catch (Exception e) {
            while (true) {
                try {
                    // 相当于是给node注册一个监听器，去看看这个监听器是否存在
                    Stat stat = zk.exists(path, true);

                    if (stat != null) {
                        this.latch = new CountDownLatch(1);
                        this.latch.await(waitTime, TimeUnit.MILLISECONDS);
                        this.latch = null;
                    }
                    zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                    return true;
                } catch (Exception ee) {
                    continue;
                }
            }

        }
        return true;
    }

    /**
     * 释放掉一个分布式锁
     * 
     * @param productId
     */
    public void releaseDistributedLock(Long productId) {
        String path = "/product-lock-" + productId;
        try {
            zookeeper.delete(path, -1);
            System.out.println("release the lock for product[id=" + productId + "]......");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 建立 zk session 的 watcher
     */
    private class ZooKeeperWatcher implements Watcher {

        public void process(WatchedEvent event) {
            System.out.println("Receive watched event: " + event.getState());

            if (KeeperState.SyncConnected == event.getState()) {
                connectedSemaphore.countDown();
            }

            if (this.latch != null) {
                this.latch.countDown();
            }
        }

    }

    /**
     * 封装单例的静态内部类
     */
    private static class Singleton {

        private static ZooKeeperSession instance;

        static {
            instance = new ZooKeeperSession();
        }

        public static ZooKeeperSession getInstance() {
            return instance;
        }

    }

    /**
     * 获取单例
     * 
     * @return
     */
    public static ZooKeeperSession getInstance() {
        return Singleton.getInstance();
    }

    /**
     * 初始化单例的便捷方法
     */
    public static void init() {
        getInstance();
    }

}
```
也可以采用另一种方式，创建临时顺序节点：

如果有一把锁，被多个人给竞争，此时多个人会排队，第一个拿到锁的人会执行，然后释放锁；后面的每个人都会去监听排在自己前面的那个人创建的 node 上，一旦某个人释放了锁，排在自己后面的人就会被 zookeeper 给通知，一旦被通知了之后，就 ok 了，自己就获取到了锁，就可以执行代码了。
```java
public class ZooKeeperDistributedLock implements Watcher {

    private ZooKeeper zk;
    private String locksRoot = "/locks";
    private String productId;
    private String waitNode;
    private String lockNode;
    private CountDownLatch latch;
    private CountDownLatch connectedLatch = new CountDownLatch(1);
    private int sessionTimeout = 30000;

    public ZooKeeperDistributedLock(String productId) {
        this.productId = productId;
        try {
            String address = "192.168.31.187:2181,192.168.31.19:2181,192.168.31.227:2181";
            zk = new ZooKeeper(address, sessionTimeout, this);
            connectedLatch.await();
        } catch (IOException e) {
            throw new LockException(e);
        } catch (KeeperException e) {
            throw new LockException(e);
        } catch (InterruptedException e) {
            throw new LockException(e);
        }
    }

    public void process(WatchedEvent event) {
        if (event.getState() == KeeperState.SyncConnected) {
            connectedLatch.countDown();
            return;
        }

        if (this.latch != null) {
            this.latch.countDown();
        }
    }

    public void acquireDistributedLock() {
        try {
            if (this.tryLock()) {
                return;
            } else {
                waitForLock(waitNode, sessionTimeout);
            }
        } catch (KeeperException e) {
            throw new LockException(e);
        } catch (InterruptedException e) {
            throw new LockException(e);
        }
    }

    public boolean tryLock() {
        try {
 		    // 传入进去的locksRoot + “/” + productId
		    // 假设productId代表了一个商品id，比如说1
		    // locksRoot = locks
		    // /locks/10000000000，/locks/10000000001，/locks/10000000002
            lockNode = zk.create(locksRoot + "/" + productId, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
   
            // 看看刚创建的节点是不是最小的节点
	 	    // locks：10000000000，10000000001，10000000002
            List<String> locks = zk.getChildren(locksRoot, false);
            Collections.sort(locks);
	
            if(lockNode.equals(locksRoot+"/"+ locks.get(0))){
                //如果是最小的节点,则表示取得锁
                return true;
            }
	
            //如果不是最小的节点，找到比自己小1的节点
	  int previousLockIndex = -1;
            for(int i = 0; i < locks.size(); i++) {
		if(lockNode.equals(locksRoot + “/” + locks.get(i))) {
	         	    previousLockIndex = i - 1;
		    break;
		}
	   }
	   
	   this.waitNode = locks.get(previousLockIndex);
        } catch (KeeperException e) {
            throw new LockException(e);
        } catch (InterruptedException e) {
            throw new LockException(e);
        }
        return false;
    }

    private boolean waitForLock(String waitNode, long waitTime) throws InterruptedException, KeeperException {
        Stat stat = zk.exists(locksRoot + "/" + waitNode, true);
        if (stat != null) {
            this.latch = new CountDownLatch(1);
            this.latch.await(waitTime, TimeUnit.MILLISECONDS);
            this.latch = null;
        }
        return true;
    }

    public void unlock() {
        try {
            // 删除/locks/10000000000节点
            // 删除/locks/10000000001节点
            System.out.println("unlock " + lockNode);
            zk.delete(lockNode, -1);
            lockNode = null;
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public class LockException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public LockException(String e) {
            super(e);
        }

        public LockException(Exception e) {
            super(e);
        }
    }
}
```
## 使用 zk 来设计分布式锁可以吗？这两种分布式锁的实现方式哪种效率比较高？
redis 分布式锁和 zk 分布式锁的对比
redis 分布式锁，其实需要自己不断去尝试获取锁，比较消耗性能。
zk 分布式锁，获取不到锁，注册个监听器即可，不需要不断主动尝试获取锁，性能开销较小。
另外一点就是，如果是 redis 获取锁的那个客户端 出现 bug 挂了，那么只能等待超时时间之后才能释放锁；而 zk 的话，因为创建的是临时 znode，只要客户端挂了，znode 就没了，此时就自动释放锁。

redis 分布式锁大家没发现好麻烦吗？遍历上锁，计算时间等等;zk 的分布式锁语义清晰实现简单。

所以先不分析太多的东西，就说这两点，我个人实践认为 zk 的分布式锁比 redis 的分布式锁牢靠、而且模型简单易用。
