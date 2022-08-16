## 从intern看不同版本常量池的变化

[深入解析intern](https://tech.meituan.com/2014/03/06/in-depth-understanding-string-intern.html)

```java
String str1 = new StringBuilder("计算机").append("软件").toString(); 
System.out.println(str1.intern() == str1);
//JDK6 false
//JDK7 true
```

JDK7中，将字符串常量池的实现从 永久代（方法区的具体实现）  移至 堆 中。

在执行第一行代码后，由于 “计算机”，‘’软件“ 是显示声明的，所以会直接去字符串常量池中创建，但 str1 指向的却是堆中的对象 ”计算机软件“，这个对象目前并没有在字符串常量池中出现。

在执行 str1.inter() 后，要将 str1 指向的 “计算机软件” 放入常量池，如果是在JDK6中，永久代和堆是分开的，所以需要将这个字符串复制到永久代中的字符串常量池。而在JDK7中，常量池不需要再存储一份数据了，直接存储的是该对象堆中的引用。这份引用指向 str1 引用的对象。



**从上述的例子代码可以看出 jdk7 版本对 intern 操作和常量池都做了一定的修改。主要包括2点：**

- 将String常量池 从 Perm 区移动到了 Java Heap区
- `String#intern` 方法时，如果存在堆中的对象，会直接保存对象的引用，而不会重新创建对象。
