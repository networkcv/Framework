package com.lwj.jvisualvm;

import java.io.IOException;
import java.util.ArrayList;

public class Demo8_JVisualVM {
    public static void main(String[] args) throws InterruptedException, IOException {
        System.in.read();
        fun();
        System.in.read();
    }

    private static void fun() throws InterruptedException {
        ArrayList<Capacity> list = new ArrayList<Capacity>();
        for (int i = 0; i < 10000; i++) {
            Thread.sleep(400);
            list.add(new Capacity());
        }
    }
}
class Capacity {
    private  byte[] big = new byte[8 * 1024 * 1024]; //8m
}
