#  JVM

- [ ] JVM运行时区域划分
- [ ] 字节码结构
- [ ] 类加载器 / 类加载机制
- [ ] 字节码执行引擎
- [ ] 程序编译与代码优化
- [ ] GC 收集算法 / GC 收集器 /GC调优
- [ ] JVM性能监控
- [ ] Java内存模型
- [ ] 线程安全与锁优化

# 未来的技术方向

- 模块化

  例如Java中的微服务	

- 混合语言

  可以在虚拟机上执行更多支持JVM规范的语言

- 多核并行

  高频率转为多核心

  - JDK5中提供了更加灵活的并发框架AQS

  - JDK7又更新了ForkJoin

  - JDK8中提供了lambda表达式支持

- 丰富语法

  - JDK5中提供自动装箱、泛型、动态注解、枚举、遍历循环
  - JDk7中对二进制的支持、switch支持字符串、try with resource

- 64位取代32位

- 更强的垃圾回收

  - JDK11中的ZGC











# 编写高效优雅的Java程序准则

1. 构造器参数太多怎么办？

   参数超过5个，或者未来会增加参数，建议采用建造者模式

2. 不需要实例化的类构造器私有

3. 不要创建不必要的对象

   尽量使用基本类型，注意自动装箱和拆箱

   每次创建对象的时候都考虑一下，这个对象的生命周期

4. 避免使用终结方法

5. 使类和成员的可访问性最小化

6. 使可变性最小化

7. 优先使用复合，而不是继承

8. 接口优于抽象类

9. 可变参数谨慎使用

10. 返回0长度的数组或集合，不要返回null

11. 优先使用标准的异常

12. 枚举代替int常量



# test

1. 有关补码，简要阐述补码的好处。并计算给出 -99, -105, 205 整数的补码
    答：简述补码的好处：
    在人们的计算概念中零是没有正负之分的，统一0的处理
    统一处理加减法，无需增加减法器操作
    正数二进制的补码等于它本身，负数的二进制补码等于取反+1
    -99：
    原码：11100011
    反码  ：10011100
    补码  ：10011101
    其它的我直接给出补码了：
    -105：10010111
    205：00000000 11001101
2. 有关浮点数，根据IEEE745，计算11000001000100000000000000000000的单精度浮点的值，并给出计算过程。
    1 符号位      10000010 值是3       00100000000000000000000  值是1.001 
    = -1 * (2^3)*(2^0 + 2^-3)
    = -8*(1+8/1)
    = -8 -1
    = -9

3.写一个Java程序，将100.2转成IEEE745 二进制表示 ，给出程序和结果。

结果：01000010110010000110011001100110
程序（ 偷懒了，嘿嘿）：
public static void main(String[] args) {
		String value=convert(100.2f);
		System.out.println(value);
	}

	public static String convert(float num) {
		int intVal = Float.floatToIntBits(num);
		return intVal > 0 ? "0" + Integer.toBinaryString(intVal) : Integer
				.toBinaryString(intVal);
	}
## 2

1. 要求perm区溢出。可以设置一个较小的MaxPermSize，但是必须要让jvm起来。
然后 载入大量类 即可。不一定要动态生成类。找一个大点的jar包，把类加载一下就可以了。
2. 需要让函数调用层次更深，可以从2个方面回答，第一增大栈空间，也就是设置xss。
第二，可以减小局部变量表，比如 少用double，long，减少参数个数，局部变量在使用的时候，注意作用域。
在作用域开外的，局部变量，是可以被重用的，以此减少局部变量表的大小。

 布置人:geym
1. 写一个程序，让程序在运行之后，最终抛出由于Perm区溢出引起的OOM，给出运行的jdk版本，程序源码，运行参数，以及系统溢出后的截图、程序所依赖的jar包说明，并说明你的基本思路

答：
/**
 * jdk基于版本6 
 * 想要perm抛出Oom，首先要知道oom存放什么数据： 类型的常量池, 字段、方法信息 ,方法字节码
 * 由于Java想要动态创建字段、class信息需要引用到第三方Jar包。所以这个地方我利用无限创建常量池来使得抛出perm gen oom jvm
 * 运行参数：-XX:MaxPerSize=8M 程序只依懒jvm基本的jar包
 * 
 * @author zhanghua
 * 
 */
	public class PermOOM {

	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		while (true) {
			list.add(UUID.randomUUID().toString().intern());
		}
	}
	}
	系统溢出后打印的异常栈：
	Exception in thread "main" java.lang.OutOfMemoryError: PermGen space
	at java.lang.String.intern(Native Method)
	at test.classloader.PermOOM.main(PermOOM.java:20)


基本思路：
首先要知道oom存放什么数据： 类型的常量池, 字段、方法信息 ,方法字节码，
所以这个地方我利用无限创建常量池来使得抛出perm 空间填满，从而抛出perm区的Oom

2.你能想到有什么办法，可以让一个程序的函数调用层次变的更深。比如，你在一个递归调用中，发生了stack的溢出，你可以做哪些方面的尝试，使系统尽量不溢出？阐述你的观点和原因。

答：首先了解到线程在调用每个方法的时候，都会创建相应的栈，在退出方法的时候移出栈桢，并且栈是私用的，也需要占用空间，所以让一个程序的函数调用层次变的更深
    减少栈贞的空间很必要。或者增大线程的线的大小。
    通过volatile增加调用层次深度。线程会对一个没有volatile的变量进行临时存储，这就导致线程栈的空间增大，如果对一个变量增加volatile修饰，可以适当增加深度，详情看实验：

    代码：
    /**
 * jdk6
 * 启动参数是默认参数
 * @author zhanghua
 *
	 */
	public class OverflowTest {
	private volatile int i=0;
	private volatile int b=0;
	private volatile int c=0;
	

//	private  int i=0;
//	private  int b=0;
//	private  int c=0;
	
	public static void main(String[] args) {
		OverflowTest o=new OverflowTest();
		try {
			o.deepTest();
		} catch (Throwable e) {
			System.out.println("over flow deep:"+o.i);
			e.printStackTrace();
		}
	}
	private void deepTest() {
		++i;
		++b;
		++c;
		deepTest();
	}
}

在上面代码运行两次：9800（函数调用层次）上下一百范围内浮动，如果将i,b,c用volatile修饰，函数调用层次在11344左右浮动。
所以我想到的方法是：减少方法栈占用空间，或者增加线程栈的空间。

不知老师还有其它办法？



## 3

第一题
1. 使用TraceClassLoading或者等价参数得到系统使用的类。
2. 解压rt.jar
3. 使用批处理脚本、python、或者自写代码将需要的类复制到新的目录下
4. 将新目录打包为rt.jar 替换原来的

第2题
1. 根据提供的边界值 ，计算新生代 Xmn  7m
2. 根据新生代和老年代的边界值 计算Xmx 40m
3. 根据perm区边界，计算MaxPermSize    16m
4. 无法给出正确的Xms和Permsize，只能计算出它们的范围（此项不作要求）



http://xpws2006.blog.163.com/blog/static/95438577200891651655351/
http://blog.csdn.net/cping1982/article/details/2865198
其实这样来看，from和to其实只是一个逻辑概念，对于物理上来说，新生代其实就是分配对象的内存+待复制对象的内存空间

-XX:+PrintGCDetails
-XX:+PrintGCDetails这个是每次gc都会打印的，只是程序结束后才打印详细的堆信息
 -Xmx不包含，持久代空间
堆空间是连续的

 -------------------作业第一题--------------------
首先了解到：Java运行主要引赖于bin和Lib目录，bin目录主要存储了java命令和需要的dll
lib目录是java虚拟机要用到的class和配置文件。
bin目录精简的方式：
1、bin目录最主要的工具是java.exe,它用来执行class文件.
如果只是为了单纯运行Java程序的话,其他可执行文件一般都是用不到的(可剔除).

2、 bin目录里面的动态链接库文件dll是java.exe执行class文件过程中调用的.
执行class文件,java.exe需要哪个库文件就加载哪个dll,不需用的可以剔除.
查看java用到那个dll的，可以通过windows的任务管理器，查看进程号，再用其它工具（如360）
查看引用到的dll

lib精简方式：
这里面我给出一个精简rt.jar的程序，自己写的.（这里主要是给出了精简rt.jar的程序）
主要思想就是：
1、把程序运行所需要的class文件通过-XX:TraceClassLoading打印到文本文件
2、用自己写的程序把需要的class和rt路径，精简rt存放的路径设置好
3、然后将rt1里面的目录和文件打包成rt.zip,改名为rt.jar，然后替换原来的rt.jar
4、可以达到精简的作用，再将Java.exe和对应的dll copy到相应的目录，
5、写一个批处理命令，用于自带的Java去执行jar包。

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.IOUtils;
//自己写的
public class CutJre {
	private String needClazz = "d:\\needClazz.txt";//需要的class
	private String rtPath = "D:\\Program Files\\Java\\jre6\\lib";//rt存放路径
	private String dstRtPath = "D:/cutJar/";//精简后的路径
	private JarFile rtJar;

	public static void main(String[] args) throws Exception {
		CutJre cutJre = new CutJre();
		cutJre.rtJar = new JarFile(cutJre.rtPath + "\\rt.jar");
		cutJre.copyClass("[Loaded sun.reflect.FieldAccessor from sda]");
//		 cutJre.execute();
	}

	private void execute() throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(needClazz)));
		String string = br.readLine();
		while (string != null) {
			string = br.readLine();
		}
	}
	
	private boolean copyClass(String traceStr) throws IOException {
		if (traceStr.startsWith("[Loaded")) {
			String className = traceStr.split(" ")[1];
			//不是rt里面的Jar包，是自己有的
			if(className.contains("zh")){
				return true;
			}
			copyFile(className);
		}
		return false;
	}
	
	private void copyFile(String className) throws IOException {
		String classFile = className.replace(".", "/") + ".class";
		String classDir = classFile.substring(0,classFile.lastIndexOf("/"));
	
		File dir=new File(dstRtPath+classDir);
		System.out.println(dir);
		if(!dir.exists()){
			dir.mkdirs();
		}
		JarEntry jarEntry = rtJar.getJarEntry(classFile);
		InputStream ins = rtJar.getInputStream(jarEntry);
		File file = new File(dstRtPath+ classFile);
		System.out.println(file);
		if(!file.exists()){
			file.createNewFile();
		}
		FileOutputStream fos = new FileOutputStream(file);
		IOUtils.copy(ins, fos);
		ins.close();
		fos.close();
	
	}
}

第二题：
认为最可能的启动JVM参数：
-Xms25m -Xmx40m -Xmn7m -XX:+PrintGCDetails -XX:PermSize=16m

首先 def new generation   total 6464K, used 115K [0x34e80000, 0x35580000, 0x35580000)
 eden space 5760K,   2% used [0x34e80000, 0x34e9cd38, 0x35420000)
  from space 704K,   0% used [0x354d0000, 0x354d0000, 0x35580000)
  to   space 704K,   0% used [0x35420000, 0x35420000, 0x354d0000)
通过这一行可以知道年轻代大小是7m.

通过 tenured generation   total 18124K, used 8277K [0x35580000, 0x36733000, 0x37680000)

（0x37680000-0x35580000）/1024/1024得到的结果是33m

通过以上可以得到最大堆是40m。但通过eden大小和 tenured generation   total 18124K计算出最小堆应该是25m

通过compacting perm gen  total 16384K, 可以计算出持久堆-XX:PermSize=16m

## 4

/**
 * 写一个程序，尽量产生STW现象。给出代码和启动JVM参数。并附上GC的log日志，标出停顿的时间。 <code>
 * 答：
 * 	  尽量产生stw现象，最好就是年老代一直被填满，而且尽量保证捕获error，保证程序可以长时间运行，又能满足课题要求。
 *   产生stw其它几个因素：
 *   	dump线程
 *      死锁检查
 *      堆dupm
 *   垃圾回收算法：为让stw时间较长，增大年老代空间和选用serial old垃圾算法进行回收老年代
 *   			 
 *  
 *  jvm垃圾回收参数：-Xms512m -Xmx512m -Xmn4m -XX:+PrintGCDetails -XX:+UseSerialGC 
 *   </code>
 * @author zhanghua
 * 
 */
	public class GenerateSTW {
	/**
	 * 通过集合引用对象，保证对象不被gc回收
	 */
	 private List<byte[]> content=new ArrayList<byte[]>();
	 public static void main(String[] args) {
		GenerateSTW stw=new GenerateSTW();
		stw.start();
	 }

	private void start() {
		while(true){
			try {
				content.add(new byte[1024]);
			} catch (OutOfMemoryError e) {
				//在不可以分配的时候，进行清理部分空间,继续运行，这样会很快产生下一次垃圾回收
				for(int i=0;i<1024;i++){
					content.remove(i);
				}
				
			}
			
		}
	}

}

gc:log
[GC [DefNew: 3711K->383K(3712K), 0.0065474 secs] 511956K->511923K(523904K), 0.0065829 secs] [Times: user=0.00 sys=0.02, real=0.01 secs] 
[GC [DefNew: 3711K->383K(3712K), 0.0070612 secs] 515251K->515217K(523904K), 0.0070912 secs] [Times: user=0.02 sys=0.00, real=0.01 secs] 
[GC [DefNew: 3711K->383K(3712K), 0.0071249 secs] 518545K->518512K(523904K), 0.0071581 secs] [Times: user=0.00 sys=0.00, real=0.01 secs] 
[GC [DefNew: 3711K->3711K(3712K), 0.0000197 secs][Tenured: 518128K->520191K(520192K), 0.2000829 secs] 521840K->521807K(523904K), [Perm : 2106K->2106K(12288K)], 0.2001707 secs] [Times: user=0.19 sys=0.00, real=0.20 secs] 
[Full GC [Tenured: 520191K->520191K(520192K), 0.1989891 secs] 523903K->523886K(523904K), [Perm : 2106K->2106K(12288K)], 0.1990396 secs] [Times: user=0.19 sys=0.00, real=0.20 secs] 
[Full GC [Tenured: 520191K->520191K(520192K), 0.1843441 secs] 523902K->523902K(523904K), [Perm : 2106K->2106K(12288K)], 0.1843975 secs] [Times: user=0.19 sys=0.00, real=0.18 secs] 
[Full GC [Tenured: 520191K->518799K(520192K), 0.5034074 secs] 523902K->518799K(523904K), [Perm : 2106K->2104K(12288K)], 0.5034560 secs] [Times: user=0.50 sys=0.00, real=0.50 secs] 
[GC [DefNew: 3328K->3328K(3712K), 0.0000172 secs][Tenured: 518799K->520191K(520192K), 0.1844447 secs] 522127K->522119K(523904K), [Perm : 2104K->2104K(12288K)], 0.1845251 secs] [Times: user=0.17 sys=0.00, real=0.19 secs] 
[Full GC [Tenured: 520191K->520191K(520192K), 0.1819704 secs] 523903K->523901K(523904K), [Perm : 2104K->2104K(12288K)], 0.1820234 secs] [Times: user=0.17 sys=0.00, real=0.18 secs] 
[Full GC [Tenured: 520191K->520191K(520192K), 0.1820428 secs] 523902K->523902K(523904K), [Perm : 2104K->2104K(12288K)], 0.1820878 secs] [Times: user=0.19 sys=0.00, real=0.18 secs] 
[Full GC [Tenured: 520191K->520191K(520192K), 0.1829175 secs] 523902K->523902K(523904K), [Perm : 2104K->2104K(12288K)], 0.1829629 secs] [Times: user=0.17 sys=0.00, real=0.18 secs] 

2、是否有方法尽可能减少一次STW停顿时间？由此带来的弊端是什么？
答：减少一次STW停顿时间，我这里从三个方面回答，一个是垃圾算法选择，一个是程序使用堆设置，无用对象尽早释放
垃圾算法选择：现在都是多核cpu，可以采用并行和并发收集器，如果是响应时间优化的系统应用 ，则jdk6版本一般

选择的垃圾回收算法是：XX:+UseConcMarkSweepGC,即cms收集器，这个收集器垃圾回收时间短，但是垃圾回收总时间变长，使的降低吞
吐量，算法使用的是标记-清除，并发收集器不对内存空间进行压缩,整理,所以运行一段时间以后会产生"碎片",使得运行效率降低.
CMSFullGCsBeforeCompaction此值设置运行多少次GC以后对内存空间进行压缩,整理

2、程序使用堆设置：应该根据程序运行情况，通过Jvm垃圾回收分析，设置一个比较合适的堆大小，不能一意味的将堆设置过大，导致
程序回收很大一块空间，所以会导致stw时间较长，

3、无用对象尽早释放：使用的对象，如果没有用，尽早设置null,尽量在年轻代将对象进行回收掉，可以减少full gc停顿时长

## 5

\1.   学习使用JMeter，并测试Tomcat的吞吐率，给出有关配置和测试结果的至少5个截图

答：我的jmeter和tomcat都在同一台机器

​     1、首先要安装jmeter,安装过程非常简单，将下载好的zip包解压，并配置环境变量就可以了，此时点击是jmeter就可以运行

**![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image002.jpg)**

​     **2****、运行Jmeter.bat****：**

**![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image004.jpg)**

​     **3****、增加Jmeter****线程组**

**![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image006.jpg)**

​     **4****、设置http request**

**![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image008.jpg)**

​     **5****、增加聚合报表和聚合的result****：**

​     **![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image010.jpg)**

**![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image012.jpg)**

 

 

 

**6****、开发echo servlet****，代码如下：**

**![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image014.jpg)**

 

**7****、配置web.xm****并发包到tomcat****下面：**

​     **![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image016.jpg)**

**8****、在catalina****里面设置jvm****启动参数：我初始设置比老师大，gcLog****存放d****盘**

**![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image018.jpg)**

**9****、启动tomcat,****并运行测试用例，查看聚合结果：可以看到Min****最在0****，最大在764ms,****我想因为是本机所以min****可以达到0ms,****大概每秒处理550****，没有发生错误**

**![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image020.jpg)**

**10****、看gc.log****日志：因为我这个是servlet****所以在内存需要方面非常少，以至于没有产生full gc**

**![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image022.jpg)****通过jstat****也能对上关系：**

**![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image024.jpg)**

 

2、  测试Tomcat在使用CMS下的吞吐率情况，和串行回收器和并行回收器在相同环境下做比较，给出几种回收器的报表截图，以及你对CMS的评价（从吞吐量等方面）。

由于使用的是servlet导致内存消耗比较小，所以servlet代码改造：

![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image026.jpg)

通过以上方式可以产生对象

 

**2.1****串行回收器参数：**

set CATALINA_OPTS=-Xloggc:d:/gc.log -XX:+PrintGCDetails -Xmx40M -Xms4M -XX:+UseSerialGC -XX:PermSize=64M

jstat汇总结果：

![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image028.jpg)

聚合报表结果：

![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image030.jpg)

 

 

**2.2****并行回收器：**

set CATALINA_OPTS=-Xloggc:d:/gc.log -XX:+PrintGCDetails -Xmx40M -Xms4M -XX:+UseParallelGC -XX:+UseParallelOldGC -XX:ParallelGCThreads=2 -XX:PermSize=64M

jstat汇总结果：

![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image032.jpg)

 

 

 

 

聚合报表结果

![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image034.jpg)

 

 

 

**2.3cms****垃圾收集器：**

jstat汇总结果：

![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image036.jpg)

聚合报表结果

 

![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image038.jpg)

 

 

关于对cms评价：因为我是做nio方面的，对系统的响应时间和垃圾停顿时间比较在意，所以cms还是比较熟的，cms相对来说有以下特点：

1、 能保证系统的响应时间,减少垃圾收集时的停顿时间，对一些做监控的应用优其重要。

2、 Cms采用Mark-Sweep会对堆产生空间碎片，但是可以指定在经过几次垃圾回收后再做总的空间整理

3、 从上面图中也可以看到，虽然fgc回收时间相对比并行收集器少，但总的回收次数确是多的，所以会导致系统停顿次数变多，导致吞吐量下降。

## 6

运行一个程序HelloMain，他会循环调用另外一个类Worker.doit()方法。此时，对Worker.doit()方法做更新。要求 更新后，HelloMain可以发现新的版本。
  可以选择替换class文件 ，也可以选择替换jar包。

答：我在这里选择的是替换class.
   这题的关键是发现class文件改变，用新的classloader去加载class文件，然后程序调用的也是通过新加载的class去调用。
   由于Java有双亲委派模式加哉，故需要热部署的class必须通过自定义的classloader去加载。
   强调同一个类的概念：
1.类加载器负责加载所有的类，系统为所有被载入内存中的类生成一个java.lang.Class实例.一旦一个类被载入JVM中，同一个类就不会被再次载入了。
2.在Java中，一个类用其全限定类名，包名和类名，作为标识；但在JVM中一个类用其全限定类名和其类加载器作为其唯一标识。 即，如果Worker被appclassloader加载过后，又被自定义classloader加载，它们两个不是同一个class.
所以在我的程序中，Worker不能被appclassloader，即在我的程序中不能明确使用“Worker.doit()”。这里我是通过反射调用的，

## 7

package com.zh.jvm007;

/**
 * 写一段代码，让4个线程，相互死锁(A等待B，B等待C，C等待D，D等待A)。导出线程dump，并分析。给出死锁代码，线程dump和分析过程
 * 
 * @author zhanghua
 * 
 */
	public class TestReDeadLock {
	public static void main(String[] args) throws Exception {
		A a = new A();
		B b = new B();
		C c = new C();
		D d = new D();
		
		a.next = b;
		b.next = c;
		c.next = d;
		d.next = a;
		//以上将调用流程定义好
		Thread at = new Thread(a, "AThread");
		Thread bt = new Thread(b, "BThread");
		Thread ct = new Thread(c, "CThread");
		Thread dt = new Thread(d, "DThread");
		//启动线程
		at.start();
		Thread.sleep(10);
		bt.start();
		Thread.sleep(10);
		ct.start();
		Thread.sleep(10);
		dt.start();

	}

}

class A extends P implements Runnable {

	P next;
	
	public A() {
	};
	
	public synchronized void getRes() {
		System.out.println("当前线程：" + Thread.currentThread().getName()
				+ " | 进入了" + this.getClass().getSimpleName() + " 获取到资源 | 准备调用："
				+ next.getClass().getSimpleName());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		next.getRes();
	}
	
	@Override
	public void run() {
		getRes();
	}

}

class B extends P implements Runnable {

	P next;
	
	public synchronized void getRes() {
		System.out.println("当前线程：" + Thread.currentThread().getName()
				+ " | 进入了" + this.getClass().getSimpleName() + " 获取到资源 | 准备调用："
				+ next.getClass().getSimpleName());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		next.getRes();
	}
	
	@Override
	public void run() {
		getRes();
	}

}

class C extends P implements Runnable {

	P next;
	
	public synchronized void getRes() {
		System.out.println("当前线程：" + Thread.currentThread().getName()
				+ " | 进入了" + this.getClass().getSimpleName() + " 获取到资源 | 准备调用："
				+ next.getClass().getSimpleName());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		next.getRes();
	}
	
	@Override
	public void run() {
		getRes();
	}

}

class D extends P implements Runnable {

	P next;
	
	public synchronized void getRes() {
		System.out.println("当前线程：" + Thread.currentThread().getName()
				+ " | 进入了" + this.getClass().getSimpleName() + " 获取到资源 | 准备调用："
				+ next.getClass().getSimpleName());
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		next.getRes();
	}
	
	@Override
	public void run() {
		getRes();
	}

}

class P {
	public synchronized void getRes() {
	}
}
------------------------------------------------
2014-09-21 18:03:01
Full thread dump Java HotSpot(TM) Client VM (11.3-b02 mixed mode):
"DestroyJavaVM" prio=6 tid=0x018acc00 nid=0x31ac waiting on condition [0x00000000..0x003afd20]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"DThread" prio=6 tid=0x0bd5d400 nid=0xad4 waiting for monitor entry [0x0c06f000..0x0c06f9e8]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at com.zh.jvm007.A.getRes(TestReDeadLock.java:46)
	- waiting to lock <0x03f43a50> (a com.zh.jvm007.A)
	at com.zh.jvm007.D.getRes(TestReDeadLock.java:123)
	- locked <0x03f43a60> (a com.zh.jvm007.D)
	at com.zh.jvm007.D.run(TestReDeadLock.java:128)
	at java.lang.Thread.run(Thread.java:619)

   Locked ownable synchronizers:
	- None

"CThread" prio=6 tid=0x0bd5d000 nid=0x3538 waiting for monitor entry [0x0c01f000..0x0c01fa68]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at com.zh.jvm007.D.getRes(TestReDeadLock.java:115)
	- waiting to lock <0x03f43a60> (a com.zh.jvm007.D)
	at com.zh.jvm007.C.getRes(TestReDeadLock.java:100)
	- locked <0x03f43ad8> (a com.zh.jvm007.C)
	at com.zh.jvm007.C.run(TestReDeadLock.java:105)
	at java.lang.Thread.run(Thread.java:619)

   Locked ownable synchronizers:
	- None

"BThread" prio=6 tid=0x019a9400 nid=0x1ea8 waiting for monitor entry [0x0bfcf000..0x0bfcfae8]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at com.zh.jvm007.C.getRes(TestReDeadLock.java:92)
	- waiting to lock <0x03f43ad8> (a com.zh.jvm007.C)
	at com.zh.jvm007.B.getRes(TestReDeadLock.java:77)
	- locked <0x03f43b50> (a com.zh.jvm007.B)
	at com.zh.jvm007.B.run(TestReDeadLock.java:82)
	at java.lang.Thread.run(Thread.java:619)

   Locked ownable synchronizers:
	- None

"AThread" prio=6 tid=0x019aac00 nid=0x1150 waiting for monitor entry [0x0bf7f000..0x0bf7fb68]
   java.lang.Thread.State: BLOCKED (on object monitor)
	at com.zh.jvm007.B.getRes(TestReDeadLock.java:69)
	- waiting to lock <0x03f43b50> (a com.zh.jvm007.B)
	at com.zh.jvm007.A.getRes(TestReDeadLock.java:54)
	- locked <0x03f43a50> (a com.zh.jvm007.A)
	at com.zh.jvm007.A.run(TestReDeadLock.java:59)
	at java.lang.Thread.run(Thread.java:619)

   Locked ownable synchronizers:
	- None

"Low Memory Detector" daemon prio=6 tid=0x01992800 nid=0xa94 runnable [0x00000000..0x00000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"CompilerThread0" daemon prio=10 tid=0x0198d400 nid=0x3ca8 waiting on condition [0x00000000..0x0bc8f914]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"Attach Listener" daemon prio=10 tid=0x0198c800 nid=0x1520 runnable [0x00000000..0x0bc3fd14]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"Signal Dispatcher" daemon prio=10 tid=0x0195bc00 nid=0x24c runnable [0x00000000..0x00000000]
   java.lang.Thread.State: RUNNABLE

   Locked ownable synchronizers:
	- None

"Finalizer" daemon prio=8 tid=0x01943400 nid=0x3340 in Object.wait() [0x0bb9f000..0x0bb9fa68]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x03f43e50> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:116)
	- locked <0x03f43e50> (a java.lang.ref.ReferenceQueue$Lock)
	at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:132)
	at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:159)

   Locked ownable synchronizers:
	- None

"Reference Handler" daemon prio=10 tid=0x01942000 nid=0x209c in Object.wait() [0x0bb4f000..0x0bb4fae8]
   java.lang.Thread.State: WAITING (on object monitor)
	at java.lang.Object.wait(Native Method)
	- waiting on <0x03f43c98> (a java.lang.ref.Reference$Lock)
	at java.lang.Object.wait(Object.java:485)
	at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:116)
	- locked <0x03f43c98> (a java.lang.ref.Reference$Lock)

   Locked ownable synchronizers:
	- None

"VM Thread" prio=10 tid=0x0193ec00 nid=0x3ae8 runnable 

"VM Periodic Task Thread" prio=10 tid=0x019a4c00 nid=0x3b18 waiting on condition 

JNI global references: 847


Found one Java-level deadlock:
=============================
"DThread":
  waiting to lock monitor 0x0bd5f1ac (object 0x03f43a50, a com.zh.jvm007.A),
  which is held by "AThread"
"AThread":
  waiting to lock monitor 0x0194aabc (object 0x03f43b50, a com.zh.jvm007.B),
  which is held by "BThread"
"BThread":
  waiting to lock monitor 0x0194b6ec (object 0x03f43ad8, a com.zh.jvm007.C),
  which is held by "CThread"
"CThread":
  waiting to lock monitor 0x0bd5e4ac (object 0x03f43a60, a com.zh.jvm007.D),
  which is held by "DThread"

Java stack information for the threads listed above:
===================================================
"DThread":
	at com.zh.jvm007.A.getRes(TestReDeadLock.java:46)
	- waiting to lock <0x03f43a50> (a com.zh.jvm007.A)
	at com.zh.jvm007.D.getRes(TestReDeadLock.java:123)
	- locked <0x03f43a60> (a com.zh.jvm007.D)
	at com.zh.jvm007.D.run(TestReDeadLock.java:128)
	at java.lang.Thread.run(Thread.java:619)
"AThread":
	at com.zh.jvm007.B.getRes(TestReDeadLock.java:69)
	- waiting to lock <0x03f43b50> (a com.zh.jvm007.B)
	at com.zh.jvm007.A.getRes(TestReDeadLock.java:54)
	- locked <0x03f43a50> (a com.zh.jvm007.A)
	at com.zh.jvm007.A.run(TestReDeadLock.java:59)
	at java.lang.Thread.run(Thread.java:619)
"BThread":
	at com.zh.jvm007.C.getRes(TestReDeadLock.java:92)
	- waiting to lock <0x03f43ad8> (a com.zh.jvm007.C)
	at com.zh.jvm007.B.getRes(TestReDeadLock.java:77)
	- locked <0x03f43b50> (a com.zh.jvm007.B)
	at com.zh.jvm007.B.run(TestReDeadLock.java:82)
	at java.lang.Thread.run(Thread.java:619)
"CThread":
	at com.zh.jvm007.D.getRes(TestReDeadLock.java:115)
	- waiting to lock <0x03f43a60> (a com.zh.jvm007.D)
	at com.zh.jvm007.C.getRes(TestReDeadLock.java:100)
	- locked <0x03f43ad8> (a com.zh.jvm007.C)
	at com.zh.jvm007.C.run(TestReDeadLock.java:105)
	at java.lang.Thread.run(Thread.java:619)

Found 1 deadlock.

## 8

1  根据“图一.jpg”知道大对象有三个，且StandManager最大，第三个最小，且最大内存在33.3M， 三者应该是类与子类关系， 

2  根据“图一.jpg”中StandManager#2，和“图二.jpg”、“图三.jpg”得到， 占对象空间的主要东西是segment,

3  根据“图四.jpg”可以得到是大量StandSession占了主要内存空间，导致OOM

4  根据“select s.creationTime from org.apache.catalina.session.StandardSession s”得到StandSession的creationTime，如”图五.jpg”所示；

5  

代码：

var maxTime = 0;
var startTime = 0;
var endTime = 0;
var objArr = toArray(heap.objects("org.apache.catalina.session.StandardSession"));
var size = objArr.length;
for(var i = 0;i<size;i++){
	var num = 0;
	for(var j = i-1;j >= 0;j--){
		if(objArr[i].creationTime - objArr[j].creationTime  < 1000){
			num++;
			
		}
		else{
			if (num >= maxTime){
				maxTime = num;
				startTime = objArr[j].creationTime;
				endTime = objArr[i].creationTime;
			}
			break;
			
		}
	
	}
}
"the max time is :" + maxTime + ", and  the start time is  :" + startTime + ", the endTime is  :" + endTime;


-------------------自己写-----------------------------------------------
var objArr = toArray(heap.objects("org.apache.catalina.session.StandardSession"));
var maxNum = 0;
var stime = 0;
var etime = 0;
var size = objArr.length;
for(var i = 0;i<size;i++){
	var num = 0;
	var tempEndTime=0;
	for(var j = 0;j<size;j++){
		var diff=objArr[i].creationTime - objArr[j].creationTime;
		if(diff>=0 && diff  <=1000){
		    num++;
           if( objArr[j].creationTime>tempEndTime){
		        tempEndTime=objArr[j].creationTime;
		    }
		}	
	}
	if (num > maxNum){
	   maxNum=num;	
	   stime = objArr[i].creationTime;
	   etime=tempEndTime;
	}
}
"maxNum:"+maxNum+",stime:"+stime+",etime:"+etime;


-------------------自己写2-----------------------------------------------

 var objArr =sort(heap.objects("org.apache.catalina.session.StandardSession"),'lhs.creationTime - rhs.creationTime')
 var size = objArr.length;
 var maxNum=0;
 var stime = 0;
 var etime = 0;
 for(var i = 0;i<size;i++){
     var tempStime=objArr[i].creationTime;
     var num=0;
     for(var j = i;j<size;j++){
	if(objArr[j].creationTime-tempStime<=1000){
	   num++;
	}else{
	   if(num>maxNum){
	       stime=tempStime;
               etime=objArr[j-1].creationTime;
	       maxNum=num;
	   }
	   break;
	}
     }
 }
"maxNum:"+maxNum+",stime:"+stime+",really etime:"+etime+",virtual time:"+(parseInt(stime)+1000);

maxNum:865,stime:1403324651641,really etime:1403324652639,virtual time:1403324652641


-----------------------结束----------------------------------------------
结论：
the max time is :978, and the start time is :1403324651055, the endTime is :1403324652260


var counts={};
max(
    map(
        sort(
            heap.objects("org.apache.catalina.session.StandardSession"),
           'rhs.creationTime - lhs.creationTime'
        ),
       function(session){
         var key = session.creationTime * 1 ;
         if( ! counts[key]){
             counts[key] = 1;
         } else {
             counts[key] = counts[key] + 1;
         }
         var sum = 0;
         for (var i = key ; i < key+1000 ; i++){
             if( counts[i]){ 
                 sum += counts[i]; 
             } 
         }
      return { startTime:session.creationTime,
               endTime:(session.creationTime*1+1000).toString(),
               count:sum};
     }
     ),
'lhs.count > rhs.count')

-----------------------
为什么只输出最后一个
var objArr = toArray(heap.objects("org.apache.catalina.session.StandardSession"));
var size = objArr.length;
for(var i = 0;i<size;i++){
    "creatTime:"+objArr[i].creationTime;
 }
 这个没有严格的定义 他支持js 辅助 但是也只是辅助



1.对于文件 oom_第8周第1题.rar(http://pan.baidu.com/s/1kTl32Eb) ，给出最合理的堆溢出的原因

答：通过mat工具,打开堆快照。通过默认分析可以大致看到是appclassloader内存占用很多，并且string实例很多：

![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image002.jpg)。

2通过对appclassloader进行with outcoming references![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image004.jpg)

3、可以依次找到在StringOOM的类中加载了一个String集合， 里面存放了大量String类型的字符串,导致了内存泄露

![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image006.jpg)

 

\2. tomcat_第8周第2题.rar（http://pan.baidu.com/s/1hqJz6hY）是tomcat被大量请求后oom的dump，使用jvisualvm的库，以秒为单位，计算得出系统承受负载最大的时刻，给出那个时刻的时间点（起始时间和终止时间合计一秒），以及那一秒钟内，系统的每秒接受的请求数量。比如，在系统 1234毫秒 到 2234毫秒这一秒内，请求量合计500次。 给出你的结论，并提供操作工作截图或相关代码

 

答：

首先一个请求会创建一个org.apache.catalina.session.StandardSession ,并且Session里面有一个创建时间，可以通过这个创建时间统计一秒内的承受负载，从而统计到最大负载时刻，

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

1、 通过jvisualvm打开tomcat.hprof的堆文件，

![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image008.jpg)

通过简单命令查询，可以得到s.creationTime,并且发现得到的session的creationTime并不是有序的。按照我统计高峰的思路，需要先对session的创建时间升序排序，然后用双重循环依次递增求出最大高峰。

命令如下：

var objArr =sort(heap.objects("org.apache.catalina.session.StandardSession"),'lhs.creationTime - rhs.creationTime')

 var size = objArr.length;

 var maxNum=0;

 var stime = 0;

 var etime = 0;

 for(var i = 0;i<size;i++){

   var tempStime=objArr[i].creationTime;

   var num=0;

   for(var j = i;j<size;j++){

  if(objArr[j].creationTime-tempStime<=1000){

​    num++;

  }else{

​    if(num>maxNum){

​      stime=tempStime;

​        etime=objArr[j-1].creationTime;

​      maxNum=num;

​    }

​    break;

  }

   }

 }

"maxNum:"+maxNum+",stime:"+stime+",really etime:"+etime+",virtual time:"+(parseInt(stime)+1000);

 

程序运行结果截图：

![img](file:///C:/Users/NETWOR~1/AppData/Local/Temp/msohtmlclip1/01/clip_image010.jpg)

## 9

1. 写2个程序，分别使用无锁的方式，和有锁的方式对某一个整数进行++操作直到某一个很大的值M，同样使用N个线程，给出2个程序的性能比较
当N=3 30 300 1000
M=1000000
给出以上4种情况的性能比较
答：
当n=3时
    LockRunnable threads 3：stime:1413100154405  etime:1413100155190 ctime:785 value:2000001
NoLockRunnable threads 3：stime:1413100155191  etime:1413100155243 ctime:52 value:2000002
当n=30时
    LockRunnable threads 30：stime:1413100197538  etime:1413100198266 ctime:728 value:2000028
NoLockRunnable threads 30：stime:1413100198268  etime:1413100198351 ctime:83 value:2000005
当n=300时
    LockRunnable threads 300：stime:1413100211515  etime:1413100212341 ctime:826 value:2000298
NoLockRunnable threads 300：stime:1413100212344  etime:1413100212607 ctime:263 value:2000001
当n=1000时
    LockRunnable threads 1000：stime:1413100226395  etime:1413100229474 ctime:3079 value:2000326
NoLockRunnable threads 1000：stime:1413100229476  etime:1413100231860 ctime:2384 value:2000001
总结如下：
	无锁的方式比有锁快，当M越大时，差别越大。
是用一个java程序,代码如下：

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class JvmLockCompare {
	//线程数
	private static int nThreads=1000;
	//二百万
	private static int endValue=2000*10*10*10;
	public static void main(String[] args) throws InterruptedException {
		System.out.println("当n="+nThreads+"时");
		testLockMethod();
		testNoLockMethod();

	}
	private static void testLockMethod() throws InterruptedException {
		//有锁方式
		LockRunnable lockMethod=new LockRunnable();
		LockRunnable.endValue=endValue;
		//初始化线程
		ExecutorService service=  Executors.newFixedThreadPool(nThreads);
		//开始计算 
		long stime=System.currentTimeMillis();
		for(int i=0;i<nThreads;i++){
			service.submit(lockMethod);
		}
		service.shutdown();
		service.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
		//结束计算 
		long etime=System.currentTimeMillis();
		//打印日志
		System.out.println("  LockRunnable threads "+nThreads+"：stime:"+stime+"  etime:"+etime+" ctime:"+(etime-stime)+" value:"+LockRunnable.startValue);
	}
	private static void testNoLockMethod() throws InterruptedException {
		//无锁方式
		NoLockRunnable lockMethod=new NoLockRunnable();
		NoLockRunnable.endValue=endValue;
		//初始化线程
		ExecutorService service=  Executors.newFixedThreadPool(nThreads);
		//开始计算 
		long stime=System.currentTimeMillis();
		for(int i=0;i<nThreads;i++){
			service.submit(lockMethod);
		}
		service.shutdown();
		service.awaitTermination(Integer.MAX_VALUE, TimeUnit.SECONDS);
		//结束计算 
		long etime=System.currentTimeMillis();
		//打印日志
		System.out.println("NoLockRunnable threads "+nThreads+"：stime:"+stime+"  etime:"+etime+" ctime:"+(etime-stime)+" value:"+NoLockRunnable.startValue);
	}

}
class NoLockRunnable implements Runnable{
	protected static AtomicInteger startValue=new AtomicInteger();
	protected static int endValue;

	@Override
	public void run() {
		int value=startValue.get();
		while(value<endValue){
			value=startValue.incrementAndGet();
		}
	}
}
class LockRunnable implements Runnable{
	protected  static int startValue;
	protected static int endValue;
	@Override
	public void run() {
		while(startValue<endValue){
			addValue();
		}
	}
	private synchronized void addValue() {
		startValue++;
	}
}

2. JVM获得锁的一般步骤是什么？（伤心。。不知道对不对呀）
1、偏向锁可用会先尝试偏向锁
2、轻量级锁可用会先尝试轻量级锁
3、以上都失败，尝试自旋锁
4、再失败，尝试普通锁，使用OS互斥量在操作系统层挂起

#  `1`.整数的表达

二进制 0b “零b”  0b101=5

八进制 0   011=9

十六进制 0x “零x” 0x11=17

![](D:\Study\Framework\JVM\img\1577549076(1).jpg)

![](D:\Study\Framework\JVM\img\1577549332(1).jpg)

为什么要用补码？

- 因为使用原码表示0有二义性
- 便于两个数的相加，可以直接补码相加，减法可以理解为加负数