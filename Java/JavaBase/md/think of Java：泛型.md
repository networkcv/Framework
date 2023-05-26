参考 https://mp.weixin.qq.com/s/Yn9CIfgLozZNs_xfSo1ZmA

ParameterizedType

ParameterizedType可能是一个比较陌生的概念。但是经常用反射（Core Reflection ）API的开发者可能比较熟悉，Type类型的一个派生类就是ParameterizedType。这个概念的非形式化（大白话）解释，可以简单的类比为泛型类型Foo<T>的一个实现。比如Foo<String>，Foo<Integer>就分别是Foo<T>的ParameterizedType



类型擦除（Type Erasure）

虽然Java引入了泛型这个概念，但为了保持向前兼容（JVM层面的兼容，不需要改动Bytecode和JVM设计），和编译性能（相比C++的Template会以模板参数生成新的类型），Java引入了类型擦除[9]作为泛型的实现方案。通过类型擦除，Java不需要在虚拟机实现上做任何修改，同时也不会为ParameterizedType创建新的类。


类型擦除的缺陷



正如所有设计都存在权衡。Java泛型通过类型擦除获得了好处同时也导致了两个主要问题。

1. Java泛型是Compile-time（编译期的）的，也就是说在运行时，所有泛型信息都被抹除了。所以JVM无法感知类型的存在。
2. 所以我们无法通过反射API，在运行期获得Type Variable所代表的类型。

但是这个特性导致我们在写工具类时会遇到一些困难。比如无法单独通过T来创建实例。如果T是一个非泛型类还好，我们可以通过直接传入类型信息进行一些操作

```
public static final <T> void foo(List<T> list, Class<T> tClass)
```

但当T是一个ParameterizedType时，上述接口里的tClass类型信息，也只能获得ParameterizedType里非泛型的类型信息。比如T位List时，Class就是List.class。在一些场景，比如反序列化时，会遇到一些麻烦。



```
/** 
* 定义一个泛型类，其中 
* Type Parameter是T extends Number 
* Type Variable是T 
* Type Argument是Foo<Integer>里的Integer 
*/
```



## 泛型介绍

一般的类和方法，只能使用具体的类型，要么是基本类型，要么是自定义的类。如果编写可以应用于多种类型的代码，这种刻板的限制代码的束缚就会很大。

在面向对象编程语言中，多态算是一种泛化机制。参数类型为父类的方法，可以接受任意继承该父类的子类作为参数。这样的方法更加通用一些，但由于类的单继承，所以如果方法的参数是一个接口，而不是一个类，会使这种限制放松了许多。

使用接口也存在问题，因为一旦指明了接口，它就要求你的代码必须使用某个特定的接口。而我们更希望的是代码能够应用于 “某种不具体的类型”，而不是一个具体的接口或类。

于是在Java SE5中推出了 `泛型` ，泛型的本质是 **参数化类型**，使用时才确定具体的类型，就像填入方法的参数一样，设置 `<>` 中的类名。这里的参数化类型与上文的泛化并不冲突，可以说是通过参数化类型来实现了 能够应用于"未知类型"的泛化。

而促成泛型出现的很大原因是为了创造容器类。容器也就是存放对象的地方，数组也是，不过与简单的数组相比，容器更加灵活，具备更多不同的功能。容器的底层是Object数组，因此可以存储任意类型的对象，但通常我们会用来存储一种类型的对象。泛型的主要目的之一就是来指定容器要持有对象的类型，而且由编译器来保证类型的正确性。

## 实际上引入泛型的主要目标：

- 类型安全

  泛型的主要目标是提高Java程序的类型安全，可以在编译期就检查出因Java类型不正确所导致的ClassCastException异常，符合 越早出错代价越小 原则。

- 消除强制类型转换

  使用时可以直接得到目标类型，消除许多强制类型转换，使代码更可读，使用泛型在字节码层面上是没有区别的，都需要通过 `checkcast` 指令进行类型转换，只是编译器来帮我们进行转换罢了。

- 潜在的性能收益

  由于泛型的实现方式，支持泛型几乎不需要JVM或类文件更改，所以的工作都是在编译器中完成。

## 泛型接口

这里的 “接口” 泛指接口、抽象类还有具体的类，按照我们之前所理解的参数化类型，那么我们如何将具体的类型作为方法的参数传入呢，传入后又该如何使用呢。

```java
public class MyTest1<T>{	//在类名后边用 <> 表示传入一个类型参数，T相当于代替传入类型的一个别名
	T get(T i) {	//在定义类时已经指定类型的别名，因此这里就使用该类型，当作传入的参数或者返回类型
        return i;
    }
    
    public static void main(String[] args) {
        MyTest1<String> test1 = new MyTest1<>();	//传入要使用的类名
        String a = test1.get("A");	//不需要进行强制类型转换就可以获得我们想要的类型
    }
}
```

## 泛型方法

泛型方法和泛型接口的区别在于，传入参数化类型的位置不同，泛型接口是在类名或者接口名的后边，而泛型方法则不依赖于泛型接口，可以单独存在于普通类中。

```java
public class MyTest2{
	<T> T get(T i){	//将定义从类名后移至方法返回类型前
		return i;
	}
    
    public static void main(String[] args){
        MyTest2 myTest2 = new MyTest2();
        String b = myTest2.<String>get("B");	//在方法调用前指定参数化类型
    }
}
```

## 类型擦除

Java中的泛型和C++中的模板有一个很大的不同：

- C++ 中模板的实例化会为每一种类型都产生一套不同的代码，这就是所谓的代码膨胀。
- Java 中并不会产生这个问题。虚拟机中并没有泛型类型对象，所有的对象都是普通类。

当编译器对带有泛型的Java代码进行编译时，它会去执行 **类型检查** 和 **类型推断** ，然后生成普通的不带泛型的字节码，这种普通的字节码可以被Java虚拟机接收执行，这个就是 **类型擦除** 。

既然在编译期擦除了泛型的信息，那运行中如何保证取出的类型就是擦除之前的声明呢？

泛型就是一个语法糖，它运行时没有存储任何类型信息，它只是在编译期根据类型信息，对使用泛型的代码做了类型转换，将原始类型转换成想要的类型，并擦除擦除泛型代码，这一切操作都是编译器在后台进行，可以保证类型安全。

泛型中没有逻辑上的父子关系，如下例，在编译擦除泛型后，两个方法的全限定名完全一致，因此会报错。

```java
//error 
void dispose(List<Integer> a){}	
void dispose(List<Object> a){}
```

## 边界

普通的类型变量在为指定边界的情况下，默认是擦除为Object，也就是会转换为Object类型。

```java
public class Test1<T> {
    void disposeNumber(T t){
        t.	//由于t被转换为了Object类型，因此此时只能调用Object中的方法
    }
}
```

而诸如 `List<T>` 这样的类型将会被擦除为List 

```java
<T> void test1(List<T> list) {
    list.size();
}
```

disposeNumber 方法是用来处理Number的，因此在使用的时候，具体的参数类型也一定要为Number的实现类，那我们如何让编译器知道这一点。

```java
class Test2<T extends Number> {	//注意这里使用了 extends 关键字
    void disposeNumber(T t) {
	    t.intValue();
    }
}
```

我们显式的指定 类型参数 T 是 "继承" 自Number，因此编译器将对应代码转换为Number类型，这样就像指定了擦除的边界。

上边的继承用引号标了起来，原因是这里不能只理解为继承，这里重用了 extends 关键字，表示的是，Number及其子类都可以作为具体从泛型参数传入。

```java
class Test2<T extends Integer> {
    void disposeNumber(T t) {
        t.intValue();
    }
}

@Test
public void test(){
Test2<Integer> integerTest2 = new Test2<>();
}

final class F{}
class FF extends F{}	//error 无法继承 final修饰的 F类
```

## PECS原则

何时使用`extends`，何时使用`super`？为了便于记忆，我们可以用PECS原则：Producer Extends Consumer Super。

即：如果需要返回`T`，它是生产者（Producer），要使用`extends`通配符；如果需要写入`T`，它是消费者（Consumer），要使用`super`通配符。

还是以`Collections`的`copy()`方法为例：

```
public class Collections {
    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
        for (int i=0; i<src.size(); i++) {
            T t = src.get(i); // src是producer
            dest.add(t); // dest是consumer
        }
    }
}
```

需要返回`T`的`src`是生产者，因此声明为`List`，需要写入`T`的`dest`是消费者，因此声明为`List`。

**上面说到使用 Object 来达到复用，会失去泛型在安全性和直观表达性上的优势，那为什么 ArrayList 等源码中的还能看到使用 Object 作为类型？**

根据《Effective Java》中所述，这里涉及到一个 “移植兼容性”：

> 泛型出现时，Java 平台即将进入它的第二个十年，在此之前已经存在了大量没有使用泛型的 Java 代码。人们认为让这些代码全部保持合法，并且能够与使用泛型的新代码互用，非常重要。

这样都是为了兼容，新代码里要使用泛型而不是原始类型。

泛型是通过擦除来实现的。因此泛型只在编译时强化它的类型信息，而在运行时丢弃(或者擦除)它的元素类型信息。擦除使得使用泛型的代码可以和没有使用泛型的代码随意互用。



**Java 中 `List<Object>` 和原始类型 `List` 之间的区别？**

- 在编译时编译器不会对原始类型进行类型安全检查，却会对带参数的类型进行检查
- 通过使用 Object 作为类型，可以告知编译器该方法可以接受任何类型的对象，比如String 或 Integer
- 你可以把任何带参数的类型传递给原始类型 List，但却不能把 List< String> 传递给接受 List< Object> 的方法，因为泛型的不可变性，会产生编译错误。