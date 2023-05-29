package com.lwj.classLoader;

/**
 * create by lwj on 2020/1/7
 * 静态常量的值确定的时候，在编译阶段就会把确定的值存放到常量池中，
 * 以后，该类对该常量的引用都会转换为对自身常量池的引用。
 */
public class Test0 {
    public static void main(String[] args){
        System.out.println(MyParents0.s);
    }
}

class MyParents0{
    public static final String s= "1";
    static{
        System.out.println("this is MyParents");
    }

}
