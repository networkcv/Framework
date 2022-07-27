package com.lwj.algo.leetcode.editor.cn.utils;

import java.util.Arrays;

/**
 * Date: 2022/5/15
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class ArraysUtil {

    public static int[] build(Integer integer) {
        String[] split = integer.toString().split("");
        int[] res = new int[split.length];
        for (int i = 0; i < split.length; i++) {
            res[i] = Integer.parseInt(split[i]);
        }
        return res;
    }

    public static void print(int[] ints) {
        Arrays.stream(ints).forEach(s -> System.out.print(s + " "));
    }
    public static void print(char[] chars) {
        for (char c : chars) {
            System.out.print(c+" ");
        }
    }
}
