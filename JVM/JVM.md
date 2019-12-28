#  JVM

## 1.类加载

```java
Class T {
	public static T t=new T();
	public statix int count=2;
	private T(){
		count++;
	}
	public static void main(String []args){
		System.out.println(T.count);
	}
}
// Output 2
```

![](D:\study\Framework\JVM\img\1577523754(1).jpg)

loading：加载，使用ClassLoader将class字节码文件加载到内存

linking：链接

- verification：校验
- preparation：准备，将静态变量赋值为默认值
- resolution：解析，构造方法可以解析，private可以解析，但是多态不知道具体的调用者是谁，无法解析

initializing：初始化，指的是类的初始化，而不是对象初始化，将静态变量赋值为初始值

## 2.G1

garbage first

STW：垃圾回收需要的停顿，G1可以使垃圾回收可控。可以使内存不连续

![](D:\study\Framework\JVM\img\1577526717.jpg)

追求响应时间：

-  XX：MaxGCPauseMillis 200  将GC的时间控制在200毫秒
- 可以对STW进行控制

灵活：

- 分Region回收
- 优先回收花费时间少、垃圾比例高的Region

每个Region多大

- 取值 

  ​	1，2，4，8，16，32 单位m

- 手工指定 `-XX：G1HeapRegionSize` 

对象何时进入老年代

1. 超过 `XX:MaxTenuringThreshold` 指定次数（YGC）

   `- Parallel Sacvenge 15`

   `- CMS 6`

   `- G115`

2. 动态年龄

   s1 -> s2 超过50%

   把年龄最大的放入

​	