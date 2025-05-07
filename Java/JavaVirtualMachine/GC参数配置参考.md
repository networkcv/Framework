# JDK8统一jvm模版

## 基础命令

### 查看默认的参数

- java -XX:+PrintCommandLineFlags -version

### 查看GC详细信息

- java -XX:+PrintGCDetails -version

### 查看初始值

- java -XX:+PrintFlagsInitial -version

### 查看最终的值

- java -XX:+PrintFlagsFinal -version
- 一般都有一个默认值，可以通过命令行等配置方式覆盖掉这个默认值，这里查看的则是这个最终的值

## 通用配置

### -XX:+UnlockExperimentalVMOptions

- 解锁试验性jvm参数

### -XX:+ExplicitGCInvokesConcurrent

XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses

- 当调用System.gc时，执行YGC

### -XX:ParallelGCThreads

- GC时线程的并发度，在容器下默认为limit cpu

### -Xloggc:/opt/zcy/modules/gc.log

-XX:+PrintGCDetails -XX:+PrintGCDateStamps

- GC日志配置

### -XX:+HeapDumpOnOutOfMemoryError

-XX:HeapDumpPath=/opt/zcy/modules/java.hprof

- 堆内存溢出处理

### Xms和Xmx

- 设置为相同

### -XX:MaxNewSize 和 -XX:NewSize

### -XX:MetaSpaceSize 和 -XX:MaxMetaSpaceSize

### -XX:SurvivorRatio

- 默认为 8，也就是说 Eden 占新生代的 8/10，From 幸存区和 To 幸存区各占新生代的 1/10
- Eden = (R*Y)/(R+1+1) From = Y/(R+1+1) To = Y/(R+1+1)
  - R：SurvivorRatio 比例 Y：新生代空间大小

## PSPO

### -XX:+AdaptiveSizePolicy

- 默认启动。根据 GC 的情况自动计算计算 Eden、From 和 To 区的大小
- 不要和 SurvivorRatio 参数显示设置搭配使用，一起使用会导致参数失效
- 由于 AdaptiveSizePolicy 会动态调整 Eden、Survivor 的大小，有些情况存在 Survivor 被自动调为很小，比如十几 MB 甚至几 MB 的可能，这个时候 YGC 回收掉 Eden 区后，还存活的对象进入 Survivor 装不下，就会直接晋升到老年代，导致老年代占用空间逐渐增加，从而触发 FULL GC，如果一次 FULL GC 的耗时很长（比如到达几百毫秒），那么在要求高响应的系统就是不可取的

### * -XX:PreTenureSizeThreshold

- 大对象到底多大

## CMS

### -XX:+UseConcMarkSweepGC

### -XX:+UseCMSCompactAtFullCollection

- FGC后执行内存碎片整理
  - 配合-XX:CMSFullGCsBeforeCompaction=0几次FGC后整理
- 默认开启，且before=0

### -XX:CMSMaxAbortablePrecleanTime=5000

- Remark前增加了可中断的并发预清理，用于等待YGC执行，超时则直接进入Remark，设置超时时间；默认5000ms

### -XX:+CMSScavengeBeforeRemark

- 在Remark执行前强制执行一次YGC，降低remark的耗时

### -XX:+CMSClassUnloadingEnabled

-XX:+CMSPermGenSweepingEnabled

- CMS默认情况下不会回收Perm区

### -XX:CMSInitiatingOccupancyFraction=80

- 老年代使用了指定阈值的内存时，出发FullGC

### -XX:+UseCMSInitiatingOccupancyOnly

- 启用CMSInitiatingOccupancyFraction，如果不指定, JVM 仅在第一次使用设定值, 后续则会根据运行时采集的数据做自动调整

### -XX:+ParallelRefProcEnabled

- 并行的处理Reference对象，如WeakReference

## G1

### * -XX:+UseG1GC

### * -XX:MaxGCPauseMillis

- 停顿时间，建议值，G1会尝试调整Young区的块数来达到这个值

### * -XX:GCPauseIntervalMillis

- ？GC的间隔时间

### * -XX:+G1HeapRegionSize

- 分区大小，建议逐渐增大该值，1 2 4 8 16 32。
- 随着size增加，垃圾的存活时间更长，GC间隔更长，但每次GC的时间也会更长
- ZGC做了改进（动态区块大小）

### * G1NewSizePercent

- 新生代最小比例，默认为5%

### * G1MaxNewSizePercent

- 新生代最大比例，默认为60%

### * GCTimeRatio

- GC时间建议比例，G1会根据这个值调整堆空间

### * ConcGCThreads

- 线程数量

### * InitiatingHeapOccupancyPercent

- 启动MixedGC时，堆空间占用比例
  - 设置触发标记周期的 Java 堆占用率阈值。默认值是 45%。这里的 java 堆占比指的是 non_young_capacity_bytes，包括 old+humongous

### G1HeapWastePercent

- 在 global concurrent marking 结束之后，我们可以知道 old gen regions 中有多少空间要被回收，在每次 YGC 之后和再次发生 Mixed GC 之前，会检查垃圾占比是否达到此参数，只有达到了，下次才会发生 Mixed GC

### G1MixedGCLiveThresholdPercent

- old generation region 中的存活对象的占比，只有在此参数之下，才会被选入 CSet

### G1MixedGCCountTarget

- 一次 global concurrent marking 之后，最多执行 Mixed GC 的次数

### G1OldCSetRegionThresholdPercent

- 一次 Mixed GC 中能被选入 CSet 的最多 old generation region 数量

### -XX:+G1TraceEagerReclaimHumongousObjects

- 跟踪并输出超大对象回收相关信息



## 其他配置

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

-  -XX:+UseSerialGC	使用串行垃圾收集器

-  -XX:PretenureSizeThreshold=4194304	当对象大小超过4M时，会直接分配在老年代，需要搭配串行垃圾收集器使用

-  -XX:MaxTenuringThreshold=5	设置对象晋升（Promote）到老年代的阈值最大值，会根据实际运行情况进行动态降低。G1中默认默认为15，CMS中默认为6

-  -XX:+PrintTenuringDistribution	打印对象年龄变化及晋升阈值变化 Desired survivor size 1048576 bytes, new threshold 5 (max 5)

-  -XX:TargetSurvivorRatio=60	Survivor的已使用率超过60%会重新计算对象晋升阈值

-  -XX:+PrintGCDateStamps	打印每次GC的时间

-  -XX:UseG1GC	使用G1垃圾收集器

-  -XX:MaxGCPauseMillis=200m	G1垃圾收集器的最大停顿时间为200毫秒

-  ​    -Xms  初始堆大小
-  ​    -Xmx  最大堆大小
-  ​    -Xss  线程的内存空间
-  ​    -XX:NewSize=n 设置年轻代大小
-  ​    -XX:NewRatio=n 设置年轻代和老年代的比值，如n为3，表示年轻代：年老代=1：3，年轻代占整个年轻代年老代和的1/4
-  ​    -XX:SurvivorRatio=n  年轻代中Eden区与两个Survivor区的比值，注意Survivor区有两个，n=3表示Eden:Survivor=3：2，一个Survivor占年轻代的1/5
-  ​    -XX:MaxPermSize=n 设置持久代大小 收集器设置
-  ​    -XX:+UseSerialGC    设置串行收集器 
-  ​    -XX:+UseParallelCC  设置并行收集器  高吞吐量
-  ​    -XX:+UseParalledOldGC   设置并行年老代收集器
-  ​    -XX:UseConcMarkSweepGC  设置并发收集器 垃圾回收统计信息
-  ​    -XX:+PrintGC
-  ​    -XX:+Printetails
-  ​    -XX:+PrintGCTimeStamps
-  ​    -Xloggc:filename    并行收集器设置

## 模板

**根据应用的性，如应用不存在长期对象的，可以适当放大-Xmn的大小，将以下Xmn * 1.5 ～ Xmn * 2**



注：低延迟系统小堆，如果有stw耗时长问题，也可以考虑使用g1，具体请咨询无涯

### 1G

-Xmx640M -Xms640M -Xmn192M -XX:MaxMetaspaceSize=256M -XX:MetaspaceSize=256M -XX:+UnlockExperimentalVMOptions -XX:+UseParallelGC -XX:-UseAdaptiveSizePolicy -XX:MaxGCPauseMillis=100 -XX:ErrorFile=/opt/hs_err_pid%p.log -XX:HeapDumpPath=/opt/zcy/modules/java.hprof -XX:+HeapDumpOnOutOfMemoryError -Xloggc:/opt/zcy/modules/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCCause -XX:+PrintPromotionFailure -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintAdaptiveSizePolicy -XX:+PrintCommandLineFlags 

### 2G

-Xmx1344M -Xms1344M -Xmn448M -XX:MaxMetaspaceSize=256M -XX:MetaspaceSize=256M -XX:+UnlockExperimentalVMOptions -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:+CMSClassUnloadingEnabled -XX:+ParallelRefProcEnabled -XX:+CMSScavengeBeforeRemark -XX:ErrorFile=/opt/hs_err_pid%p.log -XX:HeapDumpPath=/opt/zcy/modules/java.hprof -XX:+HeapDumpOnOutOfMemoryError -Xloggc:/opt/zcy/modules/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCCause -XX:+PrintPromotionFailure -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintAdaptiveSizePolicy -XX:+PrintCommandLineFlags 

### 3G

-Xmx2048M -Xms2048M -Xmn768M -XX:MaxMetaspaceSize=256M -XX:MetaspaceSize=256M -XX:+UnlockExperimentalVMOptions -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:+CMSClassUnloadingEnabled -XX:+ParallelRefProcEnabled -XX:+CMSScavengeBeforeRemark -XX:ErrorFile=/opt/hs_err_pid%p.log -XX:HeapDumpPath=/opt/zcy/modules/java.hprof -XX:+HeapDumpOnOutOfMemoryError -Xloggc:/opt/zcy/modules/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCCause -XX:+PrintPromotionFailure -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintAdaptiveSizePolicy -XX:+PrintCommandLineFlags 

### 4G

-Xmx2688M -Xms2688M -Xmn960M -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=512M -XX:+UnlockExperimentalVMOptions -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:+CMSClassUnloadingEnabled -XX:+ParallelRefProcEnabled -XX:+CMSScavengeBeforeRemark -XX:ErrorFile=/opt/hs_err_pid%p.log -XX:HeapDumpPath=/opt/zcy/modules/java.hprof -XX:+HeapDumpOnOutOfMemoryError -Xloggc:/opt/zcy/modules/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCCause -XX:+PrintPromotionFailure -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintAdaptiveSizePolicy -XX:+PrintCommandLineFlags 

### 5G

-Xmx3392M -Xms3392M -Xmn1216M -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=512M -XX:+UnlockExperimentalVMOptions -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:+CMSClassUnloadingEnabled -XX:+ParallelRefProcEnabled -XX:+CMSScavengeBeforeRemark -XX:ErrorFile=/opt/hs_err_pid%p.log -XX:HeapDumpPath=/opt/zcy/modules/java.hprof -XX:+HeapDumpOnOutOfMemoryError -Xloggc:/opt/zcy/modules/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCCause -XX:+PrintPromotionFailure -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintAdaptiveSizePolicy -XX:+PrintCommandLineFlags 

### 6G

-Xmx4096M -Xms4096M -Xmn1536M -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=512M -XX:+UnlockExperimentalVMOptions -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:+CMSClassUnloadingEnabled -XX:+ParallelRefProcEnabled -XX:+CMSScavengeBeforeRemark -XX:ErrorFile=/opt/hs_err_pid%p.log -XX:HeapDumpPath=/opt/zcy/modules/java.hprof -XX:+HeapDumpOnOutOfMemoryError -Xloggc:/opt/zcy/modules/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCCause -XX:+PrintPromotionFailure -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintAdaptiveSizePolicy -XX:+PrintCommandLineFlags 

### 7G

-Xmx4736M -Xms4736M -Xmn1728M -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=512M -XX:+UnlockExperimentalVMOptions -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -XX:+ExplicitGCInvokesConcurrentAndUnloadsClasses -XX:+CMSClassUnloadingEnabled -XX:+ParallelRefProcEnabled -XX:+CMSScavengeBeforeRemark -XX:ErrorFile=/opt/hs_err_pid%p.log -XX:HeapDumpPath=/opt/zcy/modules/java.hprof -XX:+HeapDumpOnOutOfMemoryError -Xloggc:/opt/zcy/modules/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCCause -XX:+PrintPromotionFailure -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintAdaptiveSizePolicy -XX:+PrintCommandLineFlags 

### 8G

-Xmx5440M -Xms5440M -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=512M -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+ParallelRefProcEnabled -XX:ErrorFile=/opt/hs_err_pid%p.log -XX:HeapDumpPath=/opt/zcy/modules/java.hprof -XX:+HeapDumpOnOutOfMemoryError -Xloggc:/opt/zcy/modules/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCCause -XX:+PrintPromotionFailure -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintAdaptiveSizePolicy -XX:+PrintCommandLineFlags 

### 9G

-Xmx6144M -Xms6144M -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=512M -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+ParallelRefProcEnabled -XX:ErrorFile=/opt/hs_err_pid%p.log -XX:HeapDumpPath=/opt/zcy/modules/java.hprof -XX:+HeapDumpOnOutOfMemoryError -Xloggc:/opt/zcy/modules/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCCause -XX:+PrintPromotionFailure -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintAdaptiveSizePolicy -XX:+PrintCommandLineFlags 

### 10G

-Xmx6784M -Xms6784M -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=512M -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+ParallelRefProcEnabled -XX:ErrorFile=/opt/hs_err_pid%p.log -XX:HeapDumpPath=/opt/zcy/modules/java.hprof -XX:+HeapDumpOnOutOfMemoryError -Xloggc:/opt/zcy/modules/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCCause -XX:+PrintPromotionFailure -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintAdaptiveSizePolicy -XX:+PrintCommandLineFlags 

### 11G

-Xmx7488M -Xms7488M -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=512M -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+ParallelRefProcEnabled -XX:ErrorFile=/opt/hs_err_pid%p.log -XX:HeapDumpPath=/opt/zcy/modules/java.hprof -XX:+HeapDumpOnOutOfMemoryError -Xloggc:/opt/zcy/modules/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCCause -XX:+PrintPromotionFailure -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintAdaptiveSizePolicy -XX:+PrintCommandLineFlags 

### 12G

-Xmx8192M -Xms8192M -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=512M -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+ParallelRefProcEnabled -XX:ErrorFile=/opt/hs_err_pid%p.log -XX:HeapDumpPath=/opt/zcy/modules/java.hprof -XX:+HeapDumpOnOutOfMemoryError -Xloggc:/opt/zcy/modules/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCCause -XX:+PrintPromotionFailure -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintAdaptiveSizePolicy -XX:+PrintCommandLineFlags 

### 13G

-Xmx8832M -Xms8832M -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=512M -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+ParallelRefProcEnabled -XX:ErrorFile=/opt/hs_err_pid%p.log -XX:HeapDumpPath=/opt/zcy/modules/java.hprof -XX:+HeapDumpOnOutOfMemoryError -Xloggc:/opt/zcy/modules/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCCause -XX:+PrintPromotionFailure -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintAdaptiveSizePolicy -XX:+PrintCommandLineFlags 

### 14G

-Xmx9536M -Xms9536M -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=512M -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+ParallelRefProcEnabled -XX:ErrorFile=/opt/hs_err_pid%p.log -XX:HeapDumpPath=/opt/zcy/modules/java.hprof -XX:+HeapDumpOnOutOfMemoryError -Xloggc:/opt/zcy/modules/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCCause -XX:+PrintPromotionFailure -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintAdaptiveSizePolicy -XX:+PrintCommandLineFlags 

### 15G

-Xmx10240M -Xms10240M -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=512M -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+ParallelRefProcEnabled -XX:ErrorFile=/opt/hs_err_pid%p.log -XX:HeapDumpPath=/opt/zcy/modules/java.hprof -XX:+HeapDumpOnOutOfMemoryError -Xloggc:/opt/zcy/modules/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCCause -XX:+PrintPromotionFailure -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintAdaptiveSizePolicy -XX:+PrintCommandLineFlags 

### 16G

-Xmx10880M -Xms10880M -XX:MaxMetaspaceSize=512M -XX:MetaspaceSize=512M -XX:+UnlockExperimentalVMOptions -XX:+UseG1GC -XX:MaxGCPauseMillis=100 -XX:+ParallelRefProcEnabled -XX:ErrorFile=/opt/hs_err_pid%p.log -XX:HeapDumpPath=/opt/zcy/modules/java.hprof -XX:+HeapDumpOnOutOfMemoryError -Xloggc:/opt/zcy/modules/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCCause -XX:+PrintPromotionFailure -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintAdaptiveSizePolicy -XX:+PrintCommandLineFlags 

## 公司模板

-XX:+UnlockExperimentalVMOptions -XX:SurvivorRatio=10 -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:CMSMaxAbortablePrecleanTime=5000 -XX:+CMSClassUnloadingEnabled -XX:CMSInitiatingOccupancyFraction=80 -XX:+UseCMSInitiatingOccupancyOnly -XX:+ExplicitGCInvokesConcurrent -XX:ParallelGCThreads=2 -Xloggc:/opt/zcy/modules/gc.log -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCApplicationStoppedTime -XX:+PrintGCApplicationConcurrentTime -XX:+PrintGCCause -XX:+PrintPromotionFailure -XX:+PrintTenuringDistribution -XX:+PrintHeapAtGC -XX:+PrintAdaptiveSizePolicy -XX:+PrintCommandLineFlags -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/zcy/modules/java.hprof -XX:ErrorFile=/opt/hs_err_pid%p.log

最后就是在编码的时候也要避免需要连续地址空间的大对象的产生，如过长的字符串，用于存放附件、序列化或反序列化的 byte 数组等，还有就是过早晋升问题尽量在爆发问题前就避免掉。