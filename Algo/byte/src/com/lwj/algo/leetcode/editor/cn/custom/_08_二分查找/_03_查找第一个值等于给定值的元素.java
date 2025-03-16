package com.lwj.algo.leetcode.editor.cn.custom._08_二分查找;

/**
 * create by lwj on 2019/3/12.
 */
public class _03_查找第一个值等于给定值的元素 {
    public static void main(String[] args) {
        int arr[]={1,3,4,5,6,8,8,8,9,10};
        System.out.println(fun1(arr,arr.length,8));
    }

    private static int fun1(int[] arr, int n,int target) {
        int low=0;
        int high=n-1;
        int mid;
        while(low<=high){
            mid=low+((high-low)>>1);
            if(arr[mid]>target){
                high=--mid;
            }else if (arr[mid]<target){
                low=++mid;
            }else {
                //与普通的二分查找不同的是 在值相等时会判断前边的值是否与当前值相等，如果相等则high--继续查找，
                if(mid==0 || arr[mid-1]!=target ) return mid;
                else    high=--mid;
            }
        }
        return -1;
    }

}
