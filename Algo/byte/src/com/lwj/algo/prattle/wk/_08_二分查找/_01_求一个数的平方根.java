package com.lwj.algo.prattle.wk._08_二分查找;

/**
 * create by lwj on 2019/3/12.
 */
public class _01_求一个数的平方根 {
    public static void main(String[] args) {
        int i=100;
        fun(i);
    }

    private static void fun(int i) {
        int low=0;
        int high=100;
        while(low<=high){
            int mid=low+((high-low)>>1);
            int tmp=mid*mid;
            if(tmp==i){
                System.out.println(mid);
                break;
            }else if(tmp>i){
                --high;
            }else {
                ++low;
            }
        }
    }
}
