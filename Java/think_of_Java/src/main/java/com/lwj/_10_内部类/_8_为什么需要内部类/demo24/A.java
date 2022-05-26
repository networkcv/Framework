package com.lwj._10_内部类._8_为什么需要内部类.demo24;

/**
 * create by lwj on 2020/3/17
 */
public class A {
    public static int i=0;
    U creatU() {
        U u = new U() {
            @Override
            public void method1() {
                System.out.println("A-method1 "+i++);
            }

            @Override
            public void method2() {
                System.out.println("A-method2 "+i++);
            }
            @Override
            public void method3() {
                System.out.println("A-method3 "+i++);

            }
        };
        return u;
    }
}
