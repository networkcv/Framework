package com.lwj._01_singleton_pattern.test;

/**
 * create by lwj on 2019/7/31
 */
public enum _5_枚举式 {
    INSTANCE;
}
/*
反编译后
public final class _5_枚举式 extends Enum
{
    public static final _5_枚举式 INSTANCE;
    private static final _5_枚举式 ENUM$VALUES[];
    static
    {
        INSTANCE = new _5_枚举式("INSTANCE", 0);
        ENUM$VALUES = (new _5_枚举式[] {
             INSTANCE
        });
    }
}

类型的属性会在类被加载之后被初始化，当一个Java类第一次被真正使用到的时候静态资源被初始化、
Java类的加载和初始化过程都是线程安全的（因为虚拟机在加载枚举的类的时候，会使用ClassLoader的loadClass方法，
而这个方法使用同步代码块保证了线程安全）。所以，创建一个enum类型是线程安全的。

也就是说，我们定义的一个枚举，在第一次被真正用到的时候，会被虚拟机加载并初始化，而这个初始化过程是线程安全的。
而我们知道，解决单例的并发问题，主要解决的就是初始化过程中的线程安全问题。

所以，由于枚举的以上特性，枚举实现的单例是天生线程安全的

在序列化的时候Java仅仅是将枚举对象的name属性输出到结果中，反序列化的时候则是通过Enum的ValueOf方法
在序列化方面，Java中有明确规定，枚举的序列化和反序列化是有特殊定制的。这就可以避免反序列化过程中由于反射而导致的单例被破坏问题。
 */