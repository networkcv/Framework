# JVM源码

## 编译JDK

https://juejin.cn/post/6969389200416178213

https://www.jianshu.com/p/ee7e9176632c



https://developer.apple.com/download/all/?q=command

https://jdk.java.net/java-se-ri/22

```sh
bash configure --with-debug-level=slowdebug --with-jvm-variants=server --enable-dtrace --disable-warnings-as-errors
```



```sh
#第一次编译的话需要编译全量  
make all

#也可以使用 compiledb
compiledb make all

#如修改了jdk后，增量的编译镜像
make images
```

## JVM 启动源码

```sh
java -classpath /Users/networkcavalry/Documents/GitHub/Framework/Java/JavaVirtualMachine/jvm/arthas/src/main/java  com.lwj.arthas.ArthasQuickStart2

```

https://www.cnblogs.com/springmorning/p/17478964.html





# 类加载器相关



## 源码阅读工具

source insight

## HSDB

HSDB 查看代理类class

HSDB：HotSpot Debugger Jvm自带工具，用于查看JVM运行时的状态

使用jhsdb时，保证作为attach目标的进程JDK版本和jhsdb所在的JDK版本一致

```java
jhsdb hsdb 
```



## Oop-Klass模型

https://juejin.cn/post/6844904054561193992

JVM参数：

-XX:+TraceClassLoading	打印类的加载日志

## 加载阶段

对.class文件加载到内存进行解析后生成klass信息，这些元信息存储在方法区。然后在堆区生成运行时class对象，也就是InstanceMirrorKlass的实例，也就是通过反射获取的class对象。

InstanceKlass 实例klass

- InstanceMirrorKlass 描述java.lang.class的实例，镜像Klass
- InstanceRefKlass 描述java.lang.ref.Reference的子类

ArrayKlass 数组klass

- TypeArrayKlass 基本类型数组对应的数据结构
- ObjArrayKlass 引用类型数组对应的数据结构



Klass中定义的静态属性也会保留在堆区



## 连接阶段（验证-准备-解析）

**验证**

1、文件格式验证
2、元数据验证
3、 宇节码验证
4、 符号引用验证

**准备**

为class对象中的静态变量分配内存，赋初值，就是数据类型的默认值

![image-20240621144244869](img/JVM/image-20240621144244869.png)

个人理解：因为我们的代码中可以只声明静态变量而不赋值，所以在class对象中需要提前把变量定义好，如果准备阶段不对静态属性赋值，后边jvm运行阶段中的class对象中则没有这个字段了。 

如果被fina/修饰，在编译的时候会给属性添加ConstantValue属性，准备阶段直接完成赋值，即没有初始化这一步

**解析**

类被加载后不一定会立马进行解析和初始化。初始化的时候会去检查是否完成解析。 

符号引用转换为直接引用，因为在加载阶段

符号引用：静态常量池的索引，此时还是类的全限定描述

<img src="img/JVM/image-20240621231104703.png" alt="image-20240621231104703" style="zoom: 33%;" /><img src="img/JVM/image-20240621231132730.png" alt="image-20240621231132730" style="zoom: 33%;" />



直接引用：内存地址，可以看到下图中索引为3的常量就指向了一个内存地址值，这个就是指向类的class对象

![image-20240621231014542](img/JVM/image-20240621231014542.png)

常量池：

- 静态常量池
- 运行时常量池（存在运行时常量池缓存）
- 字符串常量池





类或接口的解析

字段解析

类方法解析

接口方法解析

方法类型解析

方法句柄解析

调用点限定符解析



## 初始化阶段

通过执行\<clinit>方法，对静态变量、静态代码块进行初始化。

如果只定义了静态变量但未赋值，也表示没有需要初始化的内容，不会生成\<clinit>方法。

如果静态变量被final修饰，表示该变量的指向不会变化，那么直接可以在准备阶段完成赋值，在初始化阶段同样不会生成\<clinit>方法。

一个字节码文件只有一个 \<clinit>方法，且clinit方法中代码顺序和java代码中的顺序一致。



类加载时不一定会触发该类的clinit调用。只有下边的情况会触发clinit方法：

1. new、getstatic、putstatic、invokestatic
2. 反射
3. 初始化一个子类的clinit会去加载并初始化其父类
4. 启动类（main函数所在类） 



### clinit初始化死锁

这种情况的死锁不是java级别的，而是jvm级别的死锁。因此像jstack或者jconsole监测不到死锁。

```java
package com.lwj.classLoader;

import java.util.concurrent.TimeUnit;

/**
 * Date: 2024/6/21
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class ClassLoaderDeadLock {

    public static void main(String[] args) {
        new Thread(() -> A.test()).start();
        new Thread(() -> B.test()).start();
    }

}

class A {
    static {
        System.out.println("classA init");

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new B();
    }

    public static void test() {
        System.out.println("AAA");
    }
}


class B {
    static {
        System.out.println("classB init");

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        new A();
    }

    public static void test() {
        System.out.println("BBB");
    }
}
```



## 双亲委派机制

当发现一个类需要加载的时候，先委托其父类去加载该类，比如应用类加载器委托扩展类加载器，扩展类加载器委托启动类加载器，优先让顶级的类加载去加载类，如果父级加载不到，再由当前类加载器去加载。这样能保证核心路径的类优先被加载，比如rt.jar包所在的路径。



# 字节码

**Java 虚拟机规定义了 u1、u2、u4 三种数据结构来表示 1、2、4 字节无符号整数，**相同类型的若干条数据集合用表（table）的形式来存储。表是一个变长的结构，由代表长度的表头（n）和 紧随着的 n 个数据项组成。class 文件采用类似 C 语言的结构体来存储数据。

## 常量池

常量池结构如下所示：

```c
{
    u2             constant_pool_count;
    cp_info        constant_pool[constant_pool_count-1];
}
```

- 常量池大小（cp_info_count），常量池是 class 文件中第一个出现的变长结构，既然是池，就有大小，常量池大小的由两个字节表示。假设为值为 n，常量池真正有效的索引是 1 ~ n-1。0 属于保留索引，用来表示不指向任何常量池项。

- 常量池项（cp_info）集合，最多包含 n-1 个。为什么是最多呢？Long 和 Double 类型的常量会占用两个索引位置，如果常量池包含了这两种类型，实际的常量池项的元素个数比 n-1 要小。

  

常量池由常量项组成，Java 虚拟机目前一共定义了 14 种常量类型，这些常量名都以 "CONSTANT" 开头，以 "info" 结尾

每个常量项都由两部分构成：表示类型的 tag 和表示内容的字节数组，但u1可表示的范围是2个字节的有符号整数的范围。1个字节是8个二进制位，表示的范围是256个数字，字节码中是以16进制来表示的，只需要2位就能表示一个字节。

```c
cp_info {
    u1 tag;
    u1 info[];
}
```

字节码中是以16进制来表示的，1位可以表示16个数字，1个字节是8个二进制位，



**CONSTANT_Integer_info、CONSTANT_Float_info**

这两种结构分别用来表示 int 和 float 类型的常量，这两种类型的结构很类似，都用四个字节来表示具体的数值常量，它们的结构定义如下：

```c
CONSTANT_Integer_info {
    u1 tag;	 	 // 3 表示是为一个integer的常量项
    u4 bytes;  // 表示这个常量项需要4个字节，对应在字节码中就是8位
}

CONSTANT_Float_info {
    u1 tag; 	 // 4 表示是为一个float的常量项
    u4 bytes;	
}
```

<img src="img/JVM/16d5ec4797cc171a~tplv-t2oaga2asx-jj-mark:1512:0:0:0:q75.awebp" alt="图1-9 整型常量项结构" style="zoom:50%;" />

Java 语言规范还定义了 boolean、byte、short 和 char 类型的变量，但在常量池中都会被当做 CONSTANT_Integer_info 来处理。



**CONSTANT_Long_info 和 CONSTANT_Double_info**

这两种结构分别用来表示 long 和 double 类型的常量，这两个结构类似，都用 8 个字节表示具体的常量数值。它们的结构如下：

```c
CONSTANT_Long_info {
    u1 tag;  // tag为5
    u4 high_bytes;
    u4 low_bytes;
}

CONSTANT_Double_info {
    u1 tag; // tag为6
    u4 high_bytes;
    u4 low_bytes;
}
```

**CONSTANT_Utf8_info**

CONSTANT_Utf8_info 存储的是经过 MUTF-8(modified UTF-8) 编码的字符串，结构如下:

```c
CONSTANT_Utf8_info {
    u1 tag; //tag为1
    u2 length; //表示长度
    u1 bytes[length]; //字节数组
}
```

由三部分构成：第一个字节是 tag，值为固定为 1，tag 之后的两个字节 length 表示字符串的长度，第三部分是采用 MUTF-8 编码的长度为 length 的字节数组。

如果要存储的字符串是"hello"，存储结构如下图所示

![图1-13 UTF8 类型常量项结构](img/JVM/16d5ec47c7f49b65~tplv-t2oaga2asx-jj-mark:1512:0:0:0:q75.awebp)

![image-20240624174324197](img/JVM/image-20240624174324197.png)



# 虚拟机栈

## 栈帧

主要是由局部变量表和操作数栈组成。

## 虚拟机指令	

if_icmpge     21    // 将操作数栈顶的两个元素进行比较, 如果 次顶部元素 >= 顶部元素，则重定向到偏移量为 21 的指令

像这种需要多个参数的指令，靠近栈顶的是最靠后的参数。





# 其他

## 对象实例话

每一个构造方法都会对应一个\<init>方法，每个\<init>方法中都会包含类中定义的代码块，每次调用时都会触发代码块的调用。



准备阶段为静态变量赋初值，初始化阶段完成静态变量初始化。









JDK7 支持动态类型语言，新增了 invokedynamic 指令。

https://www.cnblogs.com/wade-luffy/p/6058087.html



5 1 10 6
