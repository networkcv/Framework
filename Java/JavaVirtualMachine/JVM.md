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
