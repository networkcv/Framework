package com.lwj.bytecode;

import java.lang.reflect.Method;

/**
 * Date: 2024/7/11 
 *
 * Description: 
 *
 * @author 乌柏
 */
public class ReflectionTest {

    private static int count = 0;

    public static void foo() {
        new Exception("test#" + (count++)).printStackTrace();
    }

    public static void main(String[] args) throws Exception {
        Class<?> clz = Class.forName("com.lwj.bytecode.ReflectionTest");
        Method method = clz.getMethod("foo");
        for (int i = 0; i < 20; i++) {
            method.invoke(null);
        }
        System.in.read();
    }
}

