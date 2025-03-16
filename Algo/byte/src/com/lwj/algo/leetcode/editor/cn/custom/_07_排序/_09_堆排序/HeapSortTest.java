package com.lwj.algo.leetcode.editor.cn.custom._07_排序._09_堆排序;

import java.util.Arrays;

/**
 * create by lwj on 2019/3/31
 */
public class HeapSortTest {
    public static void main(String[] args) {
        int[] arr = {1, 3, 2, 6, 5, 4};
        heapSort(arr, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    private static void heapSort(int[] arr, int n) {
//        buildHeap(arr, n);
        buildHeap1(arr, n);
        while(n>=0){
            swap(arr, n,0);
            heapify(arr, --n,0);
        }
    }

    //自下而上建堆
    private static void buildHeap1(int[] arr, int len) {
        while(len>=0){
            int n=len;
            while(((n-1)/2)>=0&&arr[n]>arr[(n-1)/2]){
                swap(arr,n,(n-1)/2);
                n=(n-1)/2;
            }
            len--;
        }
    }

    //自上而下建堆
    private static void buildHeap(int[] a, int n) {
        for (int i = n / 2; i >= 0; i--)
            heapify(a, n, i);
    }

    private static void heapify(int[] a, int n, int i) {
        while (true) {
            int maxPos = i;
            if (i * 2 + 1 <= n && a[i] < a[i * 2 + 1]) maxPos = i * 2 + 1;
            if (i * 2 + +2 <= n && a[maxPos] < a[i * 2 + 2]) maxPos = i * 2 + 2;
            if (maxPos == i) break;
            swap(a, i, maxPos);
            i = maxPos;
        }
    }

    private static void swap(int[] a, int i, int maxPos) {
        int tmp = a[i];
        a[i] = a[maxPos];
        a[maxPos] = tmp;
    }
}
