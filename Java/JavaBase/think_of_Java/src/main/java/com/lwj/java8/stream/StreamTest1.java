package com.lwj.java8.stream;

import org.junit.Test;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * create by lwj on 2020/2/27
 * 获取Stream的方式：
 * 1.通过集合创建
 * 2.通过数组创建
 * 3.通过Stream的 of
 * 4. 创建无限流
 */
public class StreamTest1 {
    @Test
    public void test() {
        IntStream stream = Arrays.stream(new int[]{1, 2, 3});
        Stream<String> stream1 = Arrays.stream(new String[]{"1", "2"});
    }
}
