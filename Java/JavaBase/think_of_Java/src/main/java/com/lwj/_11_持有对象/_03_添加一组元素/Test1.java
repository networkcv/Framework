package com.lwj._11_持有对象._03_添加一组元素;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * create by lwj on 2020/3/18
 */
public class Test1 {
    public static void main(String[] args) {
        List<Integer> integers = Arrays.asList(1, 2, 3, 4);
        System.out.println(integers.getClass());
        List<Integer> integers2 = new ArrayList<>();
        System.out.println(integers);
        System.out.println(integers2.getClass());
    }

    @Test
    public void test() {
        int[] arr = new int[]{1, 2, 3, 4};
        System.out.println(arr);
        System.out.println(arr.toString());
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void test2() {
        String s="a";
        String s1="a";
        System.out.println(s.endsWith(s1));
    }
}
