package com.lwj.java8.stream;

import org.junit.Test;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * create by lwj on 2020/2/27
 * Stream的终止操作
 */
public class StreamTest3 {
    /**
     * 匹配与查找
     */
    @Test
    public void test() {
//        allMatch(Predicate p) 检查是否匹配所有元素
//        anyMatch(Predicate p) 检查是否至少匹配一个元素
//        noneMatch(Predicate p) 检查是否没有匹配的元素

//        findFirst() 返回第一个元素
//        findAny()   返回任意元素

//        count()     返回元素中的总个数
//        Max(Consumer c)   返回流中最大元素
//        Min(Consumer c)   返回流中最小元素
//        forEach(Consumer c)   使用集合的遍历操作
    }

    /**
     * 归约
     */
    @Test
    public void test1() {
        //执行终止操作后，流就关闭了，无法再进行其他中间操作，
        Stream<Integer> limit1 = Stream.iterate(1, o -> o + 1).limit(10);
        Stream<Integer> limit2 = Stream.iterate(1, o -> o + 1).limit(10);
//        reduce(BinaryOperator accumulator)
//        reduce(T identity, BinaryOperator accumulator) 将流中的元素反复结合起来，得到一个值
//        identity 初始的值

        BinaryOperator<Integer> binaryOperator = (i1, i2) -> i1 + i2;
        Integer sum = limit1.reduce(0, binaryOperator);
        Integer sum1 = limit2.reduce(0, Integer::sum);

        System.out.println(sum.equals(sum1));
    }

    /**
     * 收集
     */
    @Test
    public void test3() {
        //collect(Collector c) 将流转换为其他形式，接收一个Collector接口的实现，
        // 用于给Stream中元素做汇总的方法
        Stream<Integer> integerStream = Stream.of(1, 2, 3, 4, 5, 6);
        List<Integer> collect = integerStream.collect(Collectors.toList());
        collect.forEach(System.out::println);
    }


}
