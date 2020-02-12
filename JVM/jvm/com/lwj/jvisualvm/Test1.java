package com.lwj.jvisualvm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2020/2/3
 */
public class Test1 {
    public static void main(String[] args) {
        ArrayList<Object> list = new ArrayList<>();
        while(true){
            list.add(new Object());
            System.gc();
        }
    }

    @Test
    public void test1(){

        try {
            TimeUnit.MILLISECONDS.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        test1();
    }
}
