并发用户、RPS、TPS

并发用户：在性能测试工具中，一般称为虚拟用户（Virtual User，简称VU），指的是现实系统中操作业务的用户。

TPS：Transaction Per Second，每秒事务数，是衡量系统性能吞吐的一个非常重要的指标。

RPS：Request Per Second，每秒请求数。RPS模式适合用于容量规划和作为限流管控的参考依据。

RT：Response Time，响应时间，指的是业务从客户端发起到客户端接收的时间。



TPS计算

公式描述：TPS=VU/RT，（RT单位：秒）。

举例说明：假如1个虚拟用户在1秒内完成1笔事务，那么TPS明显就是1。如果某笔业务响应时间是1 ms，那么1个虚拟用户在1s内能完成1000笔事务，TPS就是1000了；如果某笔业务响应时间是1s，那么1个虚拟用户在1s内只能完成1笔事务，要想达到1000 TPS，就需要1000个虚拟用户。因此可以说1个虚拟用户可以产生1000 TPS，1000个虚拟用户也可以产生1000 TPS，无非是看响应时间快慢。