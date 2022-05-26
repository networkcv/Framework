package com.lwj._21_并发._99_test;

import java.util.ArrayList;

/**
 * create by lwj on 2019/11/14
 */
public class 条件断点测试 extends Thread {
    static ArrayList list = new ArrayList();


    @Override
    public void run() {
        for (int i = 0; i < 500; i++) {
            list.add(new Object());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        条件断点测试 t1 = new 条件断点测试();
        条件断点测试 t2 = new 条件断点测试();
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(list.size());

    }
}
