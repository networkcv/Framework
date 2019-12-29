## 1.整数的表达

二进制 0b “零b”  0b101=5

八进制 0   011=9

十六进制 0x “零x” 0x11=17

![](D:\Study\Framework\JVM\img\1577549076(1).jpg)

![](D:\Study\Framework\JVM\img\1577549332(1).jpg)

为什么要用补码？

- 因为使用原码表示0有二义性
- 便于两个数的相加，可以直接补码相加，减法可以理解为加负数

## 2.JVM运行机制

### JVM启动流程

![](D:\Study\Framework\JVM\img\1577594046(1).jpg)



### JVM基本结构

![](D:\Study\Framework\JVM\img\1577594124(1).jpg)

#### **PC寄存器**

- 每个线程拥有一个PC寄存器
- 在线程创建时创建
- 指向下一条指令的位置
- 执行本地方法时，PC的值为undefined

#### **方法区**

- 保存装载的类信息
  - 类型的常量池
  - 字段，方法信息
  - 方法字节码
- 通常和永久区（Perm）关联在一起

#### **Java堆**

- 应用系统对象都保存在Java堆中
- 所有线程共享Java堆
- 对于分代GC来说，堆也是分代的
- GC主要的工作区间

#### **Java栈**

- 线程私有
- 栈有一系列帧组成，也叫栈帧
- 帧保存一个方法的局部变量、操作数栈、返回地址、常量池指针
- 每次方法调用创建一个帧，并压栈

局部变量表：

不止是方法中的局部变量，还有传入的参数。

![](D:\Study\Framework\JVM\img\1577595082(1).jpg)

操作数栈：

java没有寄存器，所有参数传递使用操作数栈

![](D:\Study\Framework\JVM\img\1577595291(1).jpg)

栈上分配

- -server -Xms10m -Xmx10m  -XX:+DoEscapeAnalysis  -XX:+PrintGC
- 小对象（一般几十个bytes），在没有逃逸的情况下，可以选择直接分配在栈上
- 直接分配在栈上，可以自动回收，减轻GC压力
- 大对象或者逃逸对象无法在栈上分配

![](D:\Study\Framework\JVM\img\1577595783(1).jpg)



#### **栈、堆、方法区的交互**

![](D:\Study\Framework\JVM\img\1577596891(1).jpg)

 

### 内存模型

![](D:\Study\Framework\JVM\img\1577603643(1).jpg)



### 编译运行和解释运行的概念

解释执行：

- 解释执行以解释方式运行字节码
- 读一句执行一句

编译运行（JIT）：

- 将字节码编译成机器码
- 直接执行机器码
- 运行时编译
- 编译后性能有数量级的提升

## 3.JVM配置参数

### 3.1 Trace追踪参数

`-XX:+PrintGC`

![](D:\Study\Framework\JVM\img\1577605259(1).jpg)

`-Xloggc:D:/gc.log` 

`-XX:+PrintGCDetails` 这个是每次gc都会打印的，只是程序结束后才打印详细的堆信息

`-XX:+PrintHeapAtGC`  打印GC前后的堆详细信息

`-XX:+TraceClassLoading`  监控类的加载

![](D:\Study\Framework\JVM\img\1577610632(1).jpg)

`-XX:+PrintClassHistogram`  在程序运行中，按下Ctrl+Break，打印各个类的信息

![](D:\Study\Framework\JVM\img\1577610846(1).jpg)

### 3.2 堆的分配参数

`-Xms  -Xmx`  最小堆和最大堆

-Xms和-Xmx应该保持多少比例，使系统性能最佳?

![](D:\Study\Framework\JVM\img\1577610976(1).jpg)

`-Xmn` 	设置新生代大具体大小

`-XX:NewRatio` 	新生代比老年代的比例

- 年轻代（eden+survivor×2）：老年代（tenured，不含永久区）的比值

- -XX:NewRatio=4 年轻代：老年代 = 1：4 ，年轻代占堆的1/5

`-XX:SurvivorRatio`    表示一个eden区和Survivor区的比值

- -XX:SurvivorRatio=8，eden：一个Survivor：=8：1，所以两个Survivor占新生代的20%

`-XX:+HeapDumpOnOutOfMemoryError`			OOM时导出堆到文件

`-XX:+HeapDumpPath`		导出OOM的路径

![](D:\Study\Framework\JVM\img\1577613097(1).jpg)



`-XX:OnOutOfMemoryError` 		在OOM时，执行一个脚本

![](D:\Study\Framework\JVM\img\1577613232(1).jpg)

**堆参数设置小结**

![](D:\Study\Framework\JVM\img\1577613314(1).jpg)

### 3.3 永久区分配参数

`-XX:PermSize` 	`-XX:MaxPermSize`

设置永久区的初始空间和最大空间



### 3.4 栈大小分配

- 通常只有几百k
- 决定了函数调用的深度
- 每个线程都有独立的栈空间
- 局部变量、参数分配在栈上



## 4.GC算法与种类

### GC概念

Garbage Collection 垃圾回收，防止内存泄漏

GC对象是堆空间和永久区



### GC算法

**引用可达计数法**

- 引用和去引用伴随加法和减法，影响性能
- 很难处理循环引用

**标记-清除**

从根节点开始对可达对象做一次标记，清除阶段同一清除

**复制算法**

- 将内存分为两块，每次只能使用其中一块，算法执行时，会将存活对象复制到另外一端空间
- 浪费了一半空间

**标记-压缩**

从根节点开始对可达对象做一次标记，将所有存活对象复制到内存的一段，清除这个区域外的垃圾对象。



### 分代思想

根据对象的存活周期长短分为新生代和老年代。

少量对象存活，适合复制算法。

大量对象存活，适合标记清理或标记压缩



### 可触及性

- 可触及的

  从根节点可以触及到这个对象

- 可复活的

  一旦所有引用被释放，就是可复活状态

  因为在finalize()方法中可能复活该对象，在finalize方法中对其进行引用的指向。

  ![](D:\Study\Framework\JVM\img\1577620326(1).jpg)

- 不可触及的

  早finalize()后，可能会进入不可触及的状态

  不可触及的对象不可能复活，可以被回收

**对于可触及性经验**

避免使用finalize()，操作不慎可能导致错误，可以使用try-catch-finally来替代

**根**

- 栈中引用的对象
- 方法区中静态成员或者常量引用的对象（全局对象）
- JNI方法栈中的对象

### Stop-The-World

- 简称STW，是Java中一种全局暂停的现象

- 所有Java代码停止，native代码还可以继续运行，可以理解为JVM中除GC外都处于挂起状态，不能再执行应用层面的事情，只进行GC。

- 多半由GC引起，也可能有其他原因：

  - Dump线程
  - 死锁检查
  - 堆Dump

  危害

  - 老年代的GC比较耗时，可能导致服务停止，没有响应，
  - 遇到HA系统，可能引起主备切换，严重危害生产环境。



## 5.GC参数

### 5.1 垃圾收集器选择

1. **Serial Collector（串行收集器）**

   -XX ：+UserSerialGC

   最古老，最稳点，单线程，可能会产生较长的停顿

   新生代、老年代使用串行回收

   新生代使用复制算法，老年代使用标记-压缩算法

   ![](D:\Study\Framework\JVM\img\1577628866(1).jpg)

   上边的GC是新生代的垃圾回收，FullGC是老年代的垃圾回收。

   

2. **ParNew Collector（并行收集器）**

   -XX:+UseParNewGC

   - 新生代并行
   - 老年代串行

   在串行收集器的基础上，将新生代的回收通过多线程来实现，多个线程执行复制算法，需要多核的支持老年代不变还是单线程串行。

   -XX:ParallelGCThreads 限制线程数量

   ![](D:\Study\Framework\JVM\img\1577629337(1).jpg)

3. **Paralle Collector（并行收集器）**

  类似于ParNew收集器

  新生代复制算法，老年代标记-压缩算法

  更加关注吞吐量。不过无论是串行还是并行都会有STW发生，JVM都会停下来。

  -XX:+UseParallelGC	使用ParallelGC收集器，新生代并行，老年代串行

  -XX:UseParallelOldGC	新生代和老年代都并行

  ![](D:\Study\Framework\JVM\img\1577629681(1).jpg)

  -XX:MaxGCPauseMills

  - 最大停顿时间，单位毫秒
  - GC尽量保证不超过

  停顿时间和吞吐量，就像时间和空间一样，尽量根据程序选择一个平衡点。

4. **CMS Collector（CMS收集器）**

  -XX:+UseConcMarkSweepGC

  Concurrent Mark Sweep 并发标记清除

  老年代使用标记-清除算法，与标记-压缩算法相比，少了将堆空间压缩的步骤，可以在JVM运行的时，与用户线程一起执行，并发阶段会占用系统资源，因此会降低吞吐量，且容易内存碎片化，好处是停顿时间短。  

  CMS运行过程比较复杂，着重实现了标记的过程，尽可能将全局停顿减小，但是无法消除。

  - 初始标记
    - 根可以直接关联到的对象
    - 速度快，但也会产生停顿
  - 并发标记（和用户线程一起）
    - 主要标记过程，会扫描全部对象
  - 重新标记
    - 由于并发标记时，用户线程仍然运行，因此在正式清理前，再做修正
    - 独占CPU，会产生全局停顿
  - 并发清除（和用户线程一起）
    - 基于标记结果，直接清理对象

  ![](D:\Study\Framework\JVM\img\1577631382(1).jpg)

CMS收集器特点：	

​		尽可能降低停顿

​		会影响系统整体吞吐量和性能，如分一半CPU时间去GC，在GC期间，系统反应速度就下降一半。

​		清理不彻底，因为在清理的时候，用户线程还在运行，会产生新的垃圾。

​		因为和用户线程一起运行，不能在内存空间快慢时再清理，-XX:CMSInitiatingOccupancyFraction设置触发GC的阀值，如果不幸内存预留空间不足，会引发concurrent mode failure。

​	![](D:\Study\Framework\JVM\img\1577631921(1).jpg)

使用：

-XX:+UseCMSCompactAtFullCollection 	Full GC后,进行一次整理整理过程是独占的,会引起停顿时间变长

-XX:+CMSFullGCsBeforeCompaction	 设置进行几次Full GC后,进行一次碎片整理

-XX:ParallelCMSThreads	设定CMS的线程数量，约等于cpu核数



5. **G1**

- 不仅停顿短，同时并发大，不过没CMS短，没Parallel并发量大，是一个均衡点

### 5.2 GC参数小结

- -XX:+ UseSerialGC	在新生代和老年代使用串行收集器
- -XX:SurvivorRatio	设置eden区大小和survivior区大小的比例
- -XX:NewRatio	新生代和老年代的比
- -XX:+UseParNewGC	:在新生代使用并行收集器
- -XX: +UseParallelGC 	新生代使用并行回收收集器
- -xx:+UseParalleloldGC 	老年代使用并行回收收集器
- -XX:ParallelGCThreads 	设置用于垃圾回收的线程数
- -xx:+UseConcMarkSweepGC	:新生代使用并行收集器,老年代使用CMS+串行收集器
- -XX:ParallelCMSThreads 	设定CMS的线程数量
- -XX:CMSInitiatingOccupancyFraction 	设置CMS收集器在老年代空间被使用多少后触发
- -XX:+UseCMSCompactAtFulICollection 	设置CMS收集器在完成垃圾收集后是否要进行一次内存碎片的整理
- -XX:CMSFullGCsBeforeCompaction 	设定进行多少次CMS垃圾回收后,进行一次内存压缩
- -xx:+CMSClassUnloadingEnabled 	允许对类元数据进行回收
- -XX:CMSInitiatingPermOccupancyFraction 	当永久区占用率达到这一百分比时,启动CMS回收
- -XX:UseCMSInitiatingOccupancyOnly 	表示只在到达阀值的时候,才进行CMS回收 

### 5.3 Tomcat优化实例

**Jmeter**：

​	性能测试工具

**第一次：**

set CATALINA_OPTS--server-Xloggc:gc.log -XX:+PrintGCDetails-Xmx32M-Xms32M XX:+HeapDumpOnOutOfMemoryError -XX:+UseSerialGC -XX:PermSize=32M

![](D:\Study\Framework\JVM\img\1577633036(1).jpg)

**第二次：**

![](D:\Study\Framework\JVM\img\1577633088(1).jpg)

**第三次：**

![](D:\Study\Framework\JVM\img\1577633250(1).jpg)

**第四次：**

JDK版本的升级，也会带来JVM性能的提高