package com.lwj._99_other;

import java.util.Arrays;
import java.util.HashSet;

/**
 * create by lwj on 2020/4/15
 */
public class Summation2 {
    public static void main(String[] args) {
        int[] nums = {0, 3, 3, 2, 1, 5, 3, 9};
        System.out.println(find(nums, 9));
    }

    static boolean find(int[] arr, int target) {
        HashSet<Integer> set = new HashSet<>();
        Arrays.stream(arr).forEach(i -> set.add(target - i));
        for (int a : arr) {
            if (set.contains(a))
                return true;
        }
        return false;
    }
}
