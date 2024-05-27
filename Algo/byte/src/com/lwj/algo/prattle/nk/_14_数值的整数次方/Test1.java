package com.lwj.algo.prattle.nk._14_数值的整数次方;

/**
 * create by lwj on 2018/10/3
 */
public class Test1 {
    //给定一个double类型的浮点数base和int类型的整数exponent。求base的exponent次方。
    public static void main(String[] args) {
        System.out.println(Power(2, -3));

    }

    public static double Power(double base, int exponent) {
        int ex=exponent;
        if (exponent == 0) {
            return 1;
        }
        if (exponent == 1 || exponent == -1) {
            return ex > 0 ? base : 1 / base;
        } else {
            if (exponent < 0) {
                exponent = (-1) * exponent;
            }
            double b = base;
            for (int i = 2; i <= exponent; i++) {
                base = base * b;
            }
            return ex > 0 ? base : 1 / base;
        }

    }
}
