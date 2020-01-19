package com.lwj.classLoader;


/**
 * create by lwj on 2020/1/17
 */
public class Test5 extends ClassLoader {
    public static void main(String[] args) {
        System.out.println(System.getProperty("sun.boot.class.path"));
        System.out.println(System.getProperty("java.ext.dirs"));
        System.out.println(System.getProperty("java.class.path"));
        System.out.println(System.getProperty("java.system.class.loader"));

    }
}
