package com.lwj._10_内部类._02_链接到外部类.demo3;

/**
 * create by lwj on 2020/3/17
 */
public class Test3 {
    public static void main(String[] args) {
        Outer outer = new Outer("hello");
//        Inner inner1 = outer.new Inner();
        Outer.Inner inner = outer.inner();
    }

}
