package com.lwj.algo.prattle.wk._08_二分查找;

/**
 * create by lwj on 2019/3/12.
 */
public class _04_查找最后一个值等于给定值的元素 {
    public static void main(String[] args) {
        int arr[]={1,3,4,5,6,8,8,8,9,10};
        System.out.println(fun1(arr,9,8));
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
                if(mid==n-1 || arr[mid+1]!=target ) return mid;
                else    low=++mid;
            }
        }
        return -1;
    }
}
