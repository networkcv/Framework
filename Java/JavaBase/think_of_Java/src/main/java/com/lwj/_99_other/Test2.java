package com.lwj._99_other;

import org.junit.Test;

/**
 * create by lwj on 2020/3/16
 */
public class Test2 {
    public static void main(String[] args) {
        Integer n = null;
        int i = n;
    }
    @Test
    public void test(){
        Integer n0 = 127;
        Integer n1 = Integer.valueOf(127);
        System.out.println(n0==n1);
        Integer n2 = Integer.valueOf(127);
        Integer n3 = Integer.valueOf(128);
        Integer n4 = Integer.valueOf(128);
        System.out.println(n1==n2);
        System.out.println(n3==n4);
        Integer n5 = new Integer(127);
        System.out.println(n1==n5);
    }

    @Test
    public void test3() throws ClassNotFoundException {
        Class clazz = String.class;
        Class clazz2 = "1".getClass();
        Class clazz3 =Class.forName("java.lang.String");
    }



}

