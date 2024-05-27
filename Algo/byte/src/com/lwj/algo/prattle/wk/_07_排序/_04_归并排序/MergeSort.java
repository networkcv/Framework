package com.lwj.algo.prattle.wk._07_排序._04_归并排序;

import java.util.Arrays;

/**
 * create by lwj on 2019/3/4.
 */
public class MergeSort {
    /*
    采用分治思想   分治算法一般使用递归来实现
    通过将一个无序数组分成两个子数组，然后将排序后的子数组再进行合并成一个数组，
    子数组的排序用同样的方式，分成更小的两个子数组，排序后合并。
    分为子数组的过程叫做 归   两个子数组的合并叫做 并  这样就实现了归并排序。
    最好时间复杂度 平均时间复杂度  最坏时间复杂度   空间复杂度    是否稳定
        O(nlogn)     O(nlogn)       O(nlogn)           O(n)         稳定
    递推公式：
    merge_sort(p…r) = merge(merge_sort(p…q), merge_sort(q+1…r))

    终止条件：
    p >= r 不用再继续分解
    */
    public static void main(String[] args) {
        int[] arr = {7, 3, 1, 5, 3, 1, 0};
        mergeSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    /**
     * @param arr 传入要排序的数组元素
     * @param le   要排序数组的最左边
     * @param r   要排序数组的最右边
     */
    public static void mergeSort(int[] arr, int le, int r) {
        if (le >= r) return;
        int mid = le + ((r - le) >> 1);
        //归：先分成两个无序子数组
        mergeSort(arr, le, mid);
        mergeSort(arr, mid + 1, r);
        //并：对有序的子数组进行合并
        merge(arr, le, mid, mid + 1, r);
    }

    private static void merge(int[] arr, int s1, int e1, int s2, int e2) {
        int start = s1;
        int[] new_arr = new int[arr.length];
        int index = 0;
        while (s1 <= e1 && s2 <= e2) {
            if (arr[s1] <= arr[s2]) {
                new_arr[index++] = arr[s1++];
            } else {
                new_arr[index++] = arr[s2++];
            }
        }
        //将含有剩余数据的数组元素拷贝到临时数组new_arr
        if (s1 > e1) {
            while (s2 <= e2) {
                new_arr[index++] = arr[s2++];
            }
        } else {
            while (s1 <= e1) {
                new_arr[index++] = arr[s1++];
            }
        }
        //arr = {7, 3, 1, 5, 3, 1, 0};
        //将临时数组的数据拷贝覆盖回原数组比如new_arr[3,7,0,0,0,0,0]覆盖原数组后为arr[3,7,1,5,3,1,0]
        System.arraycopy(new_arr, 0, arr, start, e2 - start + 1);
    }
}
