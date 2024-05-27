package com.lwj.algo.prattle.bk._03_数组中的重复数字;

import org.junit.Test;

/**
 * create by lwj on 2019/2/3
 */
public class Test2 {
    /*
    找出数组重复数字，不能修改数组
    一个长度为8的数组，元素在1～7中
    */
    @Test
    public void fun1() {
        //使用辅助数组
        int[] arrs = new int[]{1, 2, 3, 6, 4, 5, 7, 7};
        int[] cparrs = new int[arrs.length];
        for (int i = 0; i < arrs.length; i++) {
            if (cparrs[arrs[i]] == 0) {
                cparrs[arrs[i]] = arrs[i];
            } else {
                System.out.println(arrs[i]);
                break;
            }
        }
    }

    @Test
    public static int fun2() {
        //此种情况特殊可以使用二分法，不停缩小重复数字所在位置
        //长度n为7 元素范围为1～6
        //二分法为等比数列，8：4：2：1，需要执行的次数为O(logn) 而countRange统计方法每次需要遍历数组所有元素，所以时间复
        //杂度为O(n)，结合起来该方法的时间复杂度为O(nlogn)
        int[] arrs = new int[]{1, 2, 3, 6, 4, 5, 4,};
        int len = arrs.length;
        int start = 1, end = len - 1, mid;
        while (end >= start) {
            mid = (end + start) / 2;
            int count = countRange(arrs, len, start, mid);
            if (start == end) {
                if (count > 1) {
                    return start;
                } else {
                    break;
                }
            }
            if (count > (mid - start + 1)) {
                end = mid;
            } else {
                start = mid + 1;
            }
        }
        return -1;
    }

    public static int countRange(int[] arrs, int len, int start, int end) {
        int count = 0;
        for (int i = 0; i < len; i++) {
            if (arrs[i] >= start && arrs[i] <= end) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        int i = fun2();
        System.out.println(i);
    }

}
