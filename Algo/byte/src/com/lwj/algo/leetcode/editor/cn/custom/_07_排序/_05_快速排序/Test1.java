package com.lwj.algo.leetcode.editor.cn.custom._07_排序._05_快速排序;

import org.junit.Test;

/**
 * create by lwj on 2019/2/11
 */
public class Test1 {
    //参考：https://time.geekbang.org/column/article/41913
    //采用分治思想
    //平均时间复杂度  最坏时间复杂度      空间复杂度    是否稳定
    //    O(nlogn)       O(n^2)            O(1)        不稳定
    @Test
    public void fun1(){
        int [] arr={8,10,2,3,6,1,5};
        quick_sort(arr,arr.length);
        for (int i : arr) {
            System.out.print(i+" ");
        }
    }
    public void quick_sort(int [] arr,int len){
        quick_sortJava(arr,0,len-1);
    }
    public void quick_sortJava(int [] arr,int start,int end ){
        if(start>=end) return ;
        //进行分区，并进行排序
        int pivot= partition(arr,start,end);
        quick_sortJava(arr,start,pivot-1);
        quick_sortJava(arr,pivot+1,end);
    }
    public int partition(int [] arr,int start,int end){
        int pivot =end;
        int i=start;
        int tmp;
        for(int j=start;j<end;j++){
            if(arr[j]<=arr[pivot]){
                 tmp= arr[i];
                 arr[i]=arr[j];
                 arr[j]=tmp;
                 i++;
            }
        }
        tmp=arr[i];
        arr[i]=arr[end];
        arr[end]=tmp;
        return i;
    }

}
