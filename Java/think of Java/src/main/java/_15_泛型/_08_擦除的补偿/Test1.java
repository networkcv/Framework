package _15_泛型._08_擦除的补偿;

import org.junit.Test;

/**
 * create by lwj on 2020/3/23
 */
public class Test1 {
    class A<T> {
    }

    A<Integer>[] a;
    @Test
    public void test(){
        a= (A<Integer>[]) new Object[1];
    }
}
