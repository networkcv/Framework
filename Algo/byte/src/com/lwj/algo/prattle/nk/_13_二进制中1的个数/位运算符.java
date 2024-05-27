package com.lwj.algo.prattle.nk._13_二进制中1的个数;

/**
 * create by lwj on 2018/10/3
 */
public class 位运算符 {
    public static void main(String[] args) {
        int a = 60; /* 60 = 0011 1100 */
        int b = 13; /* 13 = 0000 1101 */
        int c = 0;
        c = a & b;       /* 12 = 0000 1100 */
        System.out.println("快速排序 & b = " + c);

        c = a | b;       /* 61 = 0011 1101 */
        System.out.println("快速排序 | b = " + c);

        c = a ^ b;       /* 49 = 0011 0001 */
        System.out.println("快速排序 ^ b = " + c);

        c = ~a;          /*-61 = 1100 0011 */
        System.out.println("~快速排序 = " + c);

        c = a << 2;     /* 240 = 1111 0000 */
        System.out.println("快速排序 << 2 = " + c);

        c = a >> 2;     /* 15 = 1111 */
        System.out.println("快速排序 >> 2  = " + c);

        c = a >>> 2;     /* 15 = 0000 1111 */
        System.out.println("快速排序 >>> 2 = " + c);
        //java中有三种移位运算符
        //
        //  <<      :     左移运算符，num << 1,相当于num乘以2
        //
        //  >>      :     右移运算符，num >> 1,相当于num除以2
        //
        //  >>>    :     无符号右移，忽略符号位，空位都以0补齐
    }
}
