package com.lwj._21_并发._99_test;

/**
 * create by lwj on 2020/6/5
 */
public class ThreadLocalTest {
    static ThreadLocal<String> str1 = new ThreadLocal<>();
    static ThreadLocal<String> str2 = new ThreadLocal<>();

    public static void main(String[] args) {
        str1.set("str1");
        str2.set("str2");
        Thread thread = Thread.currentThread();
        System.out.println();
    }


}
