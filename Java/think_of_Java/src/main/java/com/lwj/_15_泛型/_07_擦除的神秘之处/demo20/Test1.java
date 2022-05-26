package com.lwj._15_泛型._07_擦除的神秘之处.demo20;

import org.junit.Test;

/**
 * create by lwj on 2020/3/23
 * 创建一个具有两个方法的接口，以及一个实现类这个接口并添加了另一个方法的类型。在另一个类中，
 * 创建一个泛型方法，它的参数类型由这个接口定义边界，并展示该接口中的方法在这个泛型方法中都是
 * 可调用的，在main()方法中传递一个实现类的实例给这个泛型方法。
 */
public class Test1 {
    interface I1 {
        void m1();

        void m2();
    }

    class C1 implements I1 {

        @Override
        public void m1() {
            System.out.println("C1 - m1");
        }

        @Override
        public void m2() {
            System.out.println("C1 - m2");
        }

        public void m3() {
        }
    }

    class C2 {
        <T extends I1> void m4(T t) {
            t.m1();
            t.m2();
        }
    }

    @Test
    public void test() {
        C2 c2 = new C2();
        c2.m4(new C1());
    }
}
