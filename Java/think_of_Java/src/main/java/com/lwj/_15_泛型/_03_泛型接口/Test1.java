package com.lwj._15_泛型._03_泛型接口;

import org.junit.Test;

/**
 * create by lwj on 2020/3/23
 */
public class Test1 {
    abstract class A<T> {
    }

    @Test
    public void test() {
        A<String> a = new A<String>() {
        };
    }

}

class Test2<T> {
    T get(T i) {
        return i;
    }

    public static void main(String[] args) {
        Test2<String> test2 = new Test2<>();
        String a = test2.get("A");
        System.out.println(a);
    }
}
