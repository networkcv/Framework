package com.lwj.algo.leetcode.editor.cn.custom._07_排序._04_归并排序;

import java.util.Arrays;

/**
 * create by lwj on 2019/3/31
 */
public class Test2 {
    public static void main(String[] args) {
        int[] arr = {7, 3, 1, 5, 3, 1, 0};
        mergeSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    private static void mergeSort(int[] arr, int l, int r) {
        if (l >= r) return;
        int i = (l+r)/ 2;
        mergeSort(arr, l, i );
        mergeSort(arr, i+1, r);
        meger(arr, l, i, i+1, r);
    }

    /**
     * 进行两个有序区间合并 并覆盖原数组
     */
    private static void meger(int[] arr, int s1, int e1, int s2, int e2) {
        int start=s1;
        int index=0;
        int[] new_arr = new int[arr.length];
        while (s1 <= e1 && s2 <= e2){
            if(arr[s1]<=arr[s2]){
                new_arr[index++]=arr[s1++];
            }else{
                new_arr[index++]=arr[s2++];
            }
        }
        //将含有剩余元素的有序区间拷贝到临时数组
        if(s1<=e1){
            while(s1<=e1)
                new_arr[index++]=arr[s1++];
        }else {
            while(s2<=e2)
                new_arr[index++]=arr[s2++];
        }
        System.arraycopy(new_arr, 0, arr, start, e2 - start + 1);
    }


}
