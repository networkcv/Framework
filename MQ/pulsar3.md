## Metrics & Monitoring

![image-20210730001019209](img/pulsar3/image-20210730001019209.png)

## Deployment & Performance Tuning

### zookeeper

![image-20210730085635508](img/pulsar3/image-20210730085635508.png) 

### Bookies

- 每次写几个节点
- 每次要写保留几个备份
- 每次等几份数据落盘后返回

![image-20210730085820756](img/pulsar3/image-20210730085820756.png)

![image-20210730090413562](img/pulsar3/image-20210730090413562.png)

![image-20210730090430549](img/pulsar3/image-20210730090430549.png)

![image-20210730090508932](img/pulsar3/image-20210730090508932.png)

Journal缓解写压力

Ledger缓解读压力

![image-20210730090714112](img/pulsar3/image-20210730090714112.png)

分配大比例的堆外内存，来减少文件IO对JVM的影响。

![image-20210730090922963](img/pulsar3/image-20210730090922963.png)

![image-20210730091039417](img/pulsar3/image-20210730091039417.png)

### Broker

![image-20210730091609406](img/pulsar3/image-20210730091609406.png)

### Other Tuning

![image-20210730091657402](img/pulsar3/image-20210730091657402.png)

## Schema

Broker 会根据 Schema 来对消息进行序列化和反序列化，省区了Client的步骤。

同时也对 Producer 和 Consumer 进行了约束。

![image-20210730092022672](img/pulsar3/image-20210730092022672.png)

![image-20210730092251423](img/pulsar3/image-20210730092251423.png)

### Typed Client

![image-20210730092435491](img/pulsar3/image-20210730092435491.png)

Schema 有确定的pojo

![image-20210730092715336](img/pulsar3/image-20210730092715336.png)

没有确定pojo，手动生成Schema

![image-20210730092754445](img/pulsar3/image-20210730092754445.png)

pulsar 内部是如何做 schema 校验的

![image-20210730092856707](img/pulsar3/image-20210730092856707.png)

![image-20210730093238707](img/pulsar3/image-20210730093238707.png)

![image-20210730094226177](img/pulsar3/image-20210730094226177.png)

