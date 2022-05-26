package com.lwj._03_初始化与清理._01_用构造器确保初始化;

/**
 * create by lwj on 2020/3/11
 */
public class Test3 {
    int i = 1;

    {
        i = 0;
    }

    public static void main(String[] args) {
        System.out.println(new Test3().i);
    }
}
