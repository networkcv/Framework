package com.lwj._09_接口.接口;

/**
 * create by lwj on 2020/3/13
 */
public class Test1 {
    public static void main(String[] args) {
        T t = new T() {
            @Override
            public void t2() {
                System.out.println("t2");
            }
        };
        t.t0();
        T.t1();
        t.t2();
    }

}

interface T {
    public static final int i = 1;


    default void t0() {
        System.out.println("t0");
    }

    static void t1() {
        System.out.println("t1");
    }

    public abstract void t2();
}
