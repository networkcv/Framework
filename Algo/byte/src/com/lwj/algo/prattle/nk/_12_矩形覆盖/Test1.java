package com.lwj.algo.prattle.nk._12_矩形覆盖;

/**
 * create by lwj on 2018/10/2
 */
public class Test1 {
    //我们可以用2*1的小矩形横着或者竖着去覆盖更大的矩形。请问用n个2*1的小矩形无重叠地覆盖一个2*n的大矩形，总共有多少种方法？
    //依旧是斐波那契数列
    public static void main(String[] args){
        System.out.println(RectCover(5));
    }
    public static int RectCover(int target) {
        if (target < 1) {
            return 0;
        }
        if (target == 1) {
            return 1;
        }
        if (target == 2) {
            return 2;
        }
        return RectCover(target - 1) + RectCover(target - 2);
    }
}
