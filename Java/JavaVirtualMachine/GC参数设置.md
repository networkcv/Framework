-  -XX:+TraceClassLoading	用于追踪运行类的加载信息并打印出来（在类的VM option 中进行配置）
-  -XX:+ UseSerialGC	在新生代和老年代使用串行收集器
-  -XX:SurvivorRatio	设置eden区大小和survivior区大小的比例
-  -XX:NewRatio	新生代和老年代的比
-  -XX:+UseParNewGC	:在新生代使用并行收集器
-  -XX: +UseParallelGC 	新生代使用并行回收收集器
-  -xx:+UseParalleloldGC 	老年代使用并行回收收集器
-  -XX:ParallelGCThreads 	设置用于垃圾回收的线程数
-  -xx:+UseConcMarkSweepGC	:新生代使用并行收集器,老年代使用CMS+串行收集器
-  -XX:ParallelCMSThreads 	设定CMS的线程数量
-  -XX:CMSInitiatingOccupancyFraction 	设置CMS收集器在老年代空间被使用多少后触发
-  -XX:+UseCMSCompactAtFulICollection 	设置CMS收集器在完成垃圾收集后是否要进行一次内存碎片的整理
-  -XX:CMSFullGCsBeforeCompaction 	设定进行多少次CMS垃圾回收后,进行一次内存压缩
-  -xx:+CMSClassUnloadingEnabled 	允许对类元数据进行回收
-  -XX:CMSInitiatingPermOccupancyFraction 	当永久区占用率达到这一百分比时,启动CMS回收
-  -XX:UseCMSInitiatingOccupancyOnly 	表示只在到达阀值的时候,才进行CMS回收 
-  -verbose:gc	打印GC信息	
-  -Xms20m	堆的初始大小
-  -Xmx20m	堆的最大空间
-  -Xmn10m	堆的新生代的大小
-  -XX:+PrintGCDetails	打印GC的详细信息
-  -XX:SurvivorRatio=8	eden：survivior1：survivor=8：1：1
-  -XX:+PrintCommandLineFlags	打印程序启动的JVM参数
-  -XX:+UseParallelGC 使用并行垃圾收集器
-  -XX:+UseConcMarkSweepGC	使用并发标记清除的方式进行老年代的GC
-  -XX:+UseParNewGC	使用ParNew收集器管理新生代的GC

- -XX:+UseSerialGC	使用串行垃圾收集器

- -XX:PretenureSizeThreshold=4194304	当对象大小超过4M时，会直接分配在老年代，需要搭配串行垃圾收集器使用

- -XX:MaxTenuringThreshold=5	设置对象晋升（Promote）到老年代的阈值最大值，会根据实际运行情况进行动态降低。G1中默认默认为15，CMS中默认为6

- -XX:+PrintTenuringDistribution	打印对象年龄变化及晋升阈值变化 Desired survivor size 1048576 bytes, new threshold 5 (max 5)

- -XX:TargetSurvivorRatio=60	Survivor的已使用率超过60%会重新计算对象晋升阈值

- -XX:+PrintGCDateStamps	打印每次GC的时间

- -XX:UseG1GC	使用G1垃圾收集器

- -XX:MaxGCPauseMillis=200m	G1垃圾收集器的最大停顿时间为200毫秒

- ​    -Xms  初始堆大小
- ​    -Xmx  最大堆大小
- ​    -Xss  线程的内存空间
- ​    -XX:NewSize=n 设置年轻代大小
- ​    -XX:NewRatio=n 设置年轻代和老年代的比值，如n为3，表示年轻代：年老代=1：3，年轻代占整个年轻代年老代和的1/4
- ​    -XX:SurvivorRatio=n  年轻代中Eden区与两个Survivor区的比值，注意Survivor区有两个，n=3表示Eden:Survivor=3：2，一个Survivor占年轻代的1/5
- ​    -XX:MaxPermSize=n 设置持久代大小 收集器设置
- ​    -XX:+UseSerialGC    设置串行收集器 
- ​    -XX:+UseParallelCC  设置并行收集器  高吞吐量
- ​    -XX:+UseParalledOldGC   设置并行年老代收集器
- ​    -XX:UseConcMarkSweepGC  设置并发收集器 垃圾回收统计信息
- ​    -XX:+PrintGC
- ​    -XX:+Printetails
- ​    -XX:+PrintGCTimeStamps
- ​    -Xloggc:filename    并行收集器设置