package com.lwj.algo.prattle.nk._8_旋转数组的最小数字;

/**
 * create by lwj on 2018/10/1
 */
public class Test1 {
    public static void main(String[] args) {
        int[] arrays = {3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,0, 1,2,3};
        long l1 = System.nanoTime();
        System.out.println(minNumber(arrays));
        long l2 = System.nanoTime();
        System.out.println(minNumberInRotateArray(arrays));
        long l3 = System.nanoTime();
        System.out.println(minNumberInArray(arrays));
        long l4 = System.nanoTime();
        System.out.println("二分法\t" + (l2 - l1) + "ns");
        System.out.println("暴力破解\t" + (l3 - l2) + "ns");
        System.out.println("找规律\t" + (l4 - l3) + "ns");
    }

    public static int minNumber(int[] array) {   //二分法
        if (array.length == 0) {
            return 0;
        }
        if (array.length == 1) {
            return array[0];
        }
        int result = array[0];
        int low = 0;
        int high = array.length - 1;
        int mid;
        while (array[low] >= array[high]) { //如果最左边比最右边小，说明是一个递增数组 arr[0]是最小值     务必是>= 如果不加=号  3，4，5，1，2，3会出错
            if (high - low == 1) {   //在左边>=右边的前提下，如果只有两个数，那右边的 arr[high]是最小值
                return array[high];
            }
            mid = (low + high) / 2;   //中间位置
            if (array[mid] >= array[low]) {     //若中间值大于array[low]，则中间值左边一定时有序递增， 若中间值小于array[high]，则中间值右边一定是有序递增的
                low = mid;
            } else {
                high = mid;
            }
        }
        return result;
    }

    public static int minNumberInRotateArray(int[] array) {//暴力破解   O(n)
        if (array.length == 0) {
            return 0;
        }
        int result = array[0];
        for (int i = 1; i < array.length - 1; i++) {
            if (array[i] < result) {
                result = array[i];
            }
        }
        return result;
    }

    public static int minNumberInArray(int[] array) {//前边比后边大，返回后一个 O(n)  找规律
        if (array.length == 0) {
            return 0;
        }
        if (array.length == 1) {
            return array[0];
        }
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return array[i + 1];
            } else {

                if (i == array.length - 2) {
                    return array[0];
                }
            }
        }
        return 0;
    }
}
