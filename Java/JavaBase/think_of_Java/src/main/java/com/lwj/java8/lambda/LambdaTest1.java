package com.lwj.java8.lambda;


import org.junit.Test;

import java.util.Comparator;
import java.util.function.Consumer;

/**
 * create by lwj on 2020/2/26
 * 基础语法
 */
public class LambdaTest1 {
    @Test
    public void test() {
        Runnable runnable0 = new Runnable() {
            @Override
            public void run() {
                System.out.println(0);
            }
        };
        runnable0.run();
        Runnable runnable1 = () -> System.out.println(1);
        runnable1.run();
        Runnable runnable2 = () -> {
            System.out.println(2);
        };
        runnable2.run();
    }

    @Test
    public void test1() {
        Comparator<Integer> comparator0 = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        };
        System.out.println(comparator0.compare(0, 1));
        Comparator<Integer> comparator1 = (o1, o2) -> Integer.compare(o1, o2);
        System.out.println(comparator1.compare(0, 1));
        Comparator<Integer> comparator2 = Integer::compare;
        System.out.println(comparator2.compare(0, 1));

    }

    @Test
    public void test2() {
        Consumer<String> consumer1 = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        consumer1.accept("consumer1");
        Consumer<String> consumer2 = (s) -> System.out.println(s);
        consumer2.accept("consumer2");
    }
}

