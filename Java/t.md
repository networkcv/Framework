### Java基础

IO和NIO的区别。

接口和抽象类的区别。

Object 类中的方法

ArrayList默认长度，增长因子，怎么增长的

集合中add（）和put（）的区别

- int和Integer的自动拆箱/装箱相关问题。
- 常量池相关问题。
- ==和equals的区别。
- 重载和重写的区别。
- String和StringBuilder、StringBuffer的区别。
- 静态变量、实例变量、局部变量线程安全吗，为什么。
- try、catch、finally都有return语句时执行哪个。

1、使用length属性获取数组长度，public、private、protected、friendly区别
2、Collection和Collections区别
3、String s=new String(‘xyz’);创建了几个object对象
4、short s1;
s1=s1+1;是否有错？
5、Overriding和Overloading区别
6、Set里面的元素不能重复，用什么方法区分重复与否。
7、给出一个常见的runtime exception。
8、error和exception区别。
9、List和Set是否继承自Collection接口。
10、abstract class和interface 的区别。
11、是否可以继承String类。
12、try{}里有一个return语句，紧跟在try后的finally里的code会不会被执行，什么时候执行，return前执行还是return后执行。
13、最有效率的方法算2*8等于几
14、两个对象值相同，x.equal(y)==true，但是却可有不同的hashcode，这句话对不对。
15、值传递和引用传递
16、switch是否作用在byte、long、string上。
17、ArrayList和Vector区别，HashMap和Hashtable区别（了解这几个类的底层jdk中的编码方式）。
18、GC是什么，为什么要有GC，简单介绍GC。
19、float f=3.4是否正确。
20、介绍Java中的Collection framework。
21、Collection框架中实现比较方法
22、String和Stringbuffer的区别
23、final、finally、finalize区别
24、面向对象的特征
25、String是最基本的数据类型吗。
26、运行时异常和一般异常的区别
27、说出ArrayList、Vector、Linkedlist的存储性能和特性
28、heap和stack区别
29、Java中的异常处理机制的简单原理和应用
30、垃圾回收的原理和特点，并考虑2种回收机制
31、说出一些常用的 集合类和方法
32、描述一下JVM加载Class文件的原理和机制
33、排序的几种方法，了解。（算法和数据结构在面试的时候还没有被问到）
34、Java语言如何进行异常处理，throws，throw，try catch finally代表什么意义，try块中可以抛出异常吗
35、一个’.java’源文件是否可以包括多个类，有什么限制。
36、Java中有几种类型流，jdk为每种类型的流提供了一些抽象类以供继承，请分别说出它们是哪些类。
37、Java中会存在内存泄漏吗，请简单描述。
38、静态变量和实例变量的区别。
39、什么是Java序列化，如何实现java序列化。
40、是否可以从一个static方法内部发生对非static方法调用。
41、写clone方法，通常都有一行代码。
42、Java中如何跳出多重嵌套循环
43、说出常用类、包、接口，各举5个。
44、Java中实现线程的方法，用关键字修饰同步方法。
45、同步和异步区别。
46、线程同步的方法。
47、字符串常用方法，字符串转化为整型数方法，整型数转化为字符串方法。

为什么要使用接口进行开发？

介绍一下Java中的static关键字（介绍一下静态变量、静态代码块、静态方法等等）

js是异步还是同步的，了解一下异步和同步的差别

反射的原理了解过吗？

简单介绍一下集合的分类（一般介绍完，面试官就会针对性的再问几个问题，下边会列举出几个）

list里边ArrayList和LinkedList的区别（有一家公司还问了我和vector的区别）

map的分类（回答上来有的面试官还问HashMap和Hashtable的区别）

遍历map有几种方式（虽然不要求全说出来，最起码要了解一种或几种遍历Map的方式）

Java里边关键字final、finally和finalize的区别

了解装箱和拆箱吗（又问了基本数据类型和包装类型的差别以及使用包装类型时要注意判断是否为空）

Ajax的原理（可以了解一下js里实现ajax的基本步骤）

能简单列举几个熟悉的设计模式吗？（回答上来后，面试官追问：简单介绍介绍单例模式，简单介绍一下工厂模式）

struts2的执行流程

struts2中怎么向后台传值（前台页面标签name和action变量名字相同，并对变量设置get set方法等等）

struts2怎么返回一个json数据（以及怎么解析json）

简单介绍一下Spring（IOC和AOP，以及这两个核心技术分别用来做什么）

AOP的实现用到了什么技术（我答了jdk代理和cglib代理，还吹了一些细节）

IOC，为什么要让Spring来管理对象

了解过hibernate和mybatis吗（还问了为什么要使用orm框架）

- HashMap、LinkedHashMap、ConcurrentHashMap、ArrayList、LinkedList的底层实现。
- HashMap和Hashtable的区别。
- ArrayList、LinkedList、Vector的区别。
- HashMap和ConcurrentHashMap的区别。
- HashMap和LinkedHashMap的区别。
- HashMap是线程安全的吗。
- ConcurrentHashMap是怎么实现线程安全的。
- Java 反射？反射有什么缺点？你是怎么理解反射的（为什么框架需要反射）？
- 谈谈对 Java 注解的理解，解决了什么问题？
- 内部类了解吗？匿名内部类了解吗？
- BIO和NIO区别,4核cpu，100个http连接，用BIO和NIO分别需要多少个线程
- 假如我们需要存500个数需要多大的HashMap？
- HashMap的负载因子。