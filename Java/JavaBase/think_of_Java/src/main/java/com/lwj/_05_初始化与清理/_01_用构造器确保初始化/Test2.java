package com.lwj._05_初始化与清理._01_用构造器确保初始化;

/**
 * create by lwj on 2020/3/11
 */
public class Test2 {
    String s1;
    String s2 = "hello";
    String s3;

    Test2() {
        s3 = "good-bye";
    }

    public static void main(String[] args) {
        Test2 t = new Test2();
        System.out.println("t.s1: " + t.s1);
        System.out.println("t.s2: " + t.s2);
        System.out.println("t.s3: " + t.s3);
    }
}
