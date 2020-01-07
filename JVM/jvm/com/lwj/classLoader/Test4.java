package com.lwj.classLoader;

/**
 * create by lwj on 2020/1/7
 * 通过子类来调用父类的静态变量，但直接使用的其实是父类，
 * 所以可以看到只加载了父类，并没有加载子类
 */
public class Test4 {
    public static void main(String[] args){
        System.out.println(MyTest4.i);
    }
}
class MyTest4 extends MyPTest4{
    public static int j=1;
}
class MyPTest4{
    public static  int i=1;
}
