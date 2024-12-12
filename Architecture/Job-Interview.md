**XXL-JOB** 所有功能都依赖数据库，且调度中心不支持分布式架构，在任务量和调度量比较大的情况下，会有性能瓶颈。

**engine** 分布式的任务中心

**ElasticJob**

[ElasticJob 的产品定位与新版本设计理念](https://www.infoq.cn/article/zcesh20kucb9qp1o1pnt) 

[ElasticJob](https://github.com/apache/shardingsphere-elasticjob) 是一款基于Quartz开发，依赖Zookeeper作为注册中心、轻量级、无中心化的分布式任务调度框架，目前已经通过Apache开源。

ElasticJob 相对于Quartz来说，从功能上最大的区别就是支持分片，可以将一个任务分片参数分发给不同的机器执行。架构上最大的区别就是使用Zookeeper作为注册中心，不同的任务分配给不同的节点调度，不需要抢锁触发，性能上比Quartz上强大很多，

缺点：暂不支持动态创建任务。

**PowerJob** 分布式调度与计算框架，无锁化设计，支持mapReduce动态分片



