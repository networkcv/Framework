package com.lwj.algo.prattle.wk._07_排序._05_快速排序;

import java.util.Arrays;

/**
 * create by lwj on 2019/3/4.
 */
public class QuickSort {
    //采用分治思想   自上而下的处理(先排序分区然后获取分区点，再进行子数组的排序分区)
    //最好时间复杂度   平均时间复杂度  最坏时间复杂度      空间复杂度      是否稳定
    //     O(nlogn)      O(nlogn)          O(n^2)            O(logn)        不稳定
    public static void main(String[] args) {
        int[] arr = {4, 5, 1, 2 ,3};
//        int[] arr = {4, 5, 6, 1, 3, 2};
//      int[] arr={6,5,4,3,2,1};
        quickSort(arr, 0, arr.length - 1);
        System.out.println(Arrays.toString(arr));
    }

    private static void quickSort(int[] arr, int left, int right) {
        if (left >= right) return;
        int point = partition(arr, left, right);    //进行分区并获取分区点
        quickSort(arr, left, point - 1);
        quickSort(arr, point + 1, right);
    }

    private static int partition(int[] arr, int left, int right) {
        int point = arr[right];
        int i = left;
        int tmp;
        for (int j = left; j < right; j++) {
            if (arr[j] < point) {
                if (i == j) {  //i和j指向同一个元素时，就不用交换了
                    i++;
                } else {
                    tmp = arr[i];
                    arr[i++] = arr[j];
                    arr[j] = tmp;
                }
            }
        }                                                                                     //   1 2            2 1
        tmp = arr[i];   //point点和比point大的区域的第一个元素交换位置，因此快排不是稳定排序，例如 6 6 4 交换后 4 6 6
        arr[i] = arr[right];
        arr[right] = tmp;
        return i;
    }

}
