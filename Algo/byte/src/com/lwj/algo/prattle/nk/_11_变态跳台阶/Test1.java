package com.lwj.algo.prattle.nk._11_变态跳台阶;

/**
 * create by lwj on 2018/10/2
 */
public class Test1 {
    //一只青蛙一次可以跳上1级台阶，也可以跳上2级……它也可以跳上n级。求该青蛙跳上一个n级的台阶总共有多少种跳法。
    //  1   2   3   4
    //  1   2   4   8
    public static void main(String[] args){
        long l1 = System.nanoTime();
        System.out.println(JumpFloorII(10));
        long l2 = System.nanoTime();
        System.out.println(JumpFloorII1(10));
        long l3 = System.nanoTime();
        System.out.println("递归法耗时"+(l2-l1)+"ns");
        System.out.println("迭代法耗时"+(l3-l2)+"ns");
    }

    private static int JumpFloorII1(int target) {        //递归法
//        f(n)=f(n-1)+f(n-2)+...+f(n-n)
//        f(n)  =f(0)+f(1)+...+f(n-3)+f(n-2)+f(n-1)
//        f(n-1)=f(0)+f(1)+...+f(n-3)+f(n-2)
//        f(n)=f(n-1)+f(n-1)=2f(n-1)
        if(target==0){
            return 0;
        }
        if(target==1){
            return 1;
        }
        return 2*JumpFloorII1(target-1);
    }

    public static int JumpFloorII(int target) {     //迭代法
        if(target==0){
            return 0;
        }
        if(target==1){
            return 1;
        }
        int n=2;
        for (int i=2;i<target;i++){
            n=n*2;
        }
        return n;
    }
}
