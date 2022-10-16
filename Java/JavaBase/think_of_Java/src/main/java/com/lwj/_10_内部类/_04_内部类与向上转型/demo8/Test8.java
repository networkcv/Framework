package com.lwj._10_内部类._04_内部类与向上转型.demo8;

/**
 * create by lwj on 2020/3/17
 */
public class Test8 {
    public static void main(String[] args) {
        Outer.StaticInner staticInner = new Outer.StaticInner();
        Outer outer = new Outer();
    }

}
