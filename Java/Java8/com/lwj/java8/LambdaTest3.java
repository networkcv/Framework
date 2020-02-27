package com.lwj.java8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * create by lwj on 2020/2/26
 * 方法引用
 */
public class LambdaTest3 {

    //消费型接口 Consumer<T>	void accept(T t)
    public void getInteger(Integer i) {
        System.out.println("i: " + i);
    }
    public static void printInteger(Integer i) {
        System.out.println("i: " + i);
    }

    @Test
    public void test() {
        Consumer<String> consumer = (s) -> System.out.println(s);
        consumer.accept("a");
        Consumer<String> consumer1 = System.out::println;
        consumer1.accept("b");
        Consumer<Integer> consumer2 = new LambdaTest3()::getInteger;
        consumer2.accept(1);
        Consumer<Integer> consumer3 = LambdaTest3::printInteger;
        consumer2.accept(2);
    }

    //供给型接口 Supplier<T> T get()
    public Integer getTime(Supplier<Integer> suppliers) {
        return suppliers.get();
    }

    @Test
    public void test2() {
        System.out.println(getTime(() -> 2));

        Supplier<String> sp = () -> "1";
        System.out.println(sp.get());
    }

    //函数型接口 Function<T,R> R apply(T t)
    public List<String> filterString(List<String> list, Predicate<String> predicate) {
        List<String> res = new ArrayList<>();
        for (String s : list) {
            if (predicate.test(s)) {
                res.add(s);
            }
        }
        return res;
    }

    @Test
    public void test4() {
        List<String> list = Arrays.asList("1", "2", "3", "11");
        List<String> res = filterString(list, s -> s.startsWith("1"));
        System.out.println(res);
    }

}
