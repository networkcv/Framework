package com.lwj.classLoader;

import java.util.UUID;

/**
 * create by lwj on 2020/1/7
 * 静态常量的值不确定的时候
 */
public class Test1 {
    public static void main(String[] args){
        System.out.println(MyParents1.s);
    }
}

class MyParents1{
    public static final String s= UUID.randomUUID().toString();
    static{
        System.out.println("this is MyParents");
    }

}
