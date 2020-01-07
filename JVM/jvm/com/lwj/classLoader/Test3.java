package com.lwj.classLoader;

/**
 * create by lwj on 2020/1/7
 * 普通/静态代码块的加载顺序
 */
public class Test3 {
    public static void main(String[] args) {
        new MyTest3();
    }
}

class MyTest3 extends MyPTest3 {
    {
        System.out.println("子类普通代码块1");
    }

    MyTest3() {
        System.out.println("子类构造方法");
    }

    {
        System.out.println("子类普通代码块2");
    }

    static {
        System.out.println("子类静态代码块");
    }

}

class MyPTest3 {
    {
        System.out.println("父类普通代码块1");
    }

    MyPTest3() {
        System.out.println("父类构造方法");
    }

    {
        System.out.println("父类普通代码块2");
    }

    static {
        System.out.println("父类静态代码块");
    }

}