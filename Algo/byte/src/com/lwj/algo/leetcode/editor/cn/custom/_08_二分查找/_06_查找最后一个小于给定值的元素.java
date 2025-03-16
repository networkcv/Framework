package com.lwj.algo.leetcode.editor.cn.custom._08_二分查找;

/**
 * create by lwj on 2019/3/12.
 */
public class _06_查找最后一个小于给定值的元素 {
    public static void main(String[] args) {
        int arr[]={1,3,4,5,6,7,8,8,9,10};
        System.out.println(fun1(arr,9,7));
    }

    private static int fun1(int[] arr, int n,int target) {
        int low=0;
        int high=n-1;
        int mid;
        while(low<=high){
            mid=low+((high-low)>>1);
            if(arr[mid]<target){
                if(mid==n-1||arr[mid+1]>=target) return mid;
                else low=++mid;
            }else{
                high=--mid;
            }
        }
        return -1;
    }
}
