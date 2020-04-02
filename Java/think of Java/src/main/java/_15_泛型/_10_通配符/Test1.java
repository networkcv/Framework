package _15_泛型._10_通配符;

import org.junit.Test;

import java.util.ArrayList;

/**
 * create by lwj on 2020/4/1
 */
public class Test1 {
    private class A {
    }

    private class B extends A {
        void b(ArrayList<? extends B> list) {
        }

        void b2(ArrayList<? super B> list) {
        }
    }

    private class C extends B {
    }

    @Test
    public void test() {
        ArrayList<C> list1 = new ArrayList<>();
        ArrayList<A> list2 = new ArrayList<>();
        B b = new B();
        b.b(list1);
//        b.b(list2);   //error
//        b.b2(list1);   //error
        b.b2(list2);
    }
}
