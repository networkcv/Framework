package com.lwj._01_singleton_pattern;

import org.junit.Test;

import java.lang.reflect.Constructor;

/**
 * create by LiuWangJie on 2018/7/25
 * 单例模式
 */
public class MyTest {
    @Test
    public void fun1() throws Exception {
        Class<静态内部类实现单例> clazz = (Class<静态内部类实现单例>) Class.forName("com.lwj._01_singleton_pattern.静态内部类实现单例");
        Constructor<静态内部类实现单例> c = clazz.getDeclaredConstructor(null);
        c.setAccessible(true);
        静态内部类实现单例 instance1 = c.newInstance();
        静态内部类实现单例 instance2 = c.newInstance();
        System.out.println(instance1==instance2);
//        MyThread thread = new MyThread();
//        thread.start();
//
//        System.out.println(God.getGod());
//        System.out.println(God.getGod());

        枚举式实现单例 instance = 枚举式实现单例.INSTANCE;
        System.out.print("枚举式单例测试:");
        System.out.println(枚举式实现单例.INSTANCE==枚举式实现单例.INSTANCE);

        System.out.println("主线程ID:"+Thread.currentThread().getId());
        MyThread thread1 = new MyThread("thread1");
        MyThread thread2 = new MyThread("thread2");
        MyThread thread3 = new MyThread("thread3");
        MyThread thread4 = new MyThread("thread4");
        MyThread thread5 = new MyThread("thread5");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
    }
}
