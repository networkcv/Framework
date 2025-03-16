package com.lwj.algo.leetcode.editor.cn.custom._07_排序._05_快速排序;

import java.util.Arrays;

/**
 * create by lwj on 2019/3/31
 */
public class Test2 {
    public static void main(String[] args) {
        int[] arr = {4, 5, 1, 2 ,3};
//      int[] arr={6,5,4,3,2,1};
        quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    private static void quickSort(int[] arr, int l, int r) {
        if (l >= r) return;
        int p = partition(arr, l, r);
        quickSort(arr, 1, p-1);
        quickSort(arr, p + 1, r);
    }

    private static int  partition(int[] arr, int l, int r) {
        int i = l;
        int j = l;
        int p = arr[r];
        for (int k = l; k < r; k++) {
            if (arr[k] < p) {   //注意此处一定比分区点元素小，保证i左边是小分区，
                swap(arr, i++, j);
            }
            j++;
        }
        swap(arr,i,r);
        return i;

    }

    private static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;

    }

}
