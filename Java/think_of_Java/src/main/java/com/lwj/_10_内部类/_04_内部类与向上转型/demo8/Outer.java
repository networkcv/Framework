package com.lwj._10_内部类._04_内部类与向上转型.demo8;

/**
 * create by lwj on 2020/3/17
 */
public class Outer {
    int num=new Inner().i;
//    int num1=i;
    private int num2=1;
    private int num3=StaticInner.i;
    private class Inner{
        private int i=1;
        private int i2=num2;
    }
    public class Inner2{}
    public static class StaticInner{
        private static int i=1;
    }
}
