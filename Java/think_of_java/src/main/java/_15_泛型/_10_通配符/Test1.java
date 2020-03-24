package _15_泛型._10_通配符;

import org.junit.Test;

/**
 * create by lwj on 2020/3/24
 */
public class Test1 {
    class A {
    }

    class B extends A {
    }

    @Test
    public void test() {
        A[] as = new B[10];
        System.out.println(as.getClass());
//        as[0] = new A();
    }
}
