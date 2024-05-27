package com.lwj.algo.prattle.bk._99_others.打印整数二进制表示;

import org.junit.Test;

public class Test1 {
    @Test
    public void fun1() {
//        byte b = -6;
//        String s = Integer.toBinaryString(b);
//        System.out.println(s);
        int a = -6;
        for (int i = 0; i < 32; i++) {
            //0x80000000为32位的十六进制，二进制是1后边31个0   >>>为无符号右移   负数计算存储都是补码
            int ttt =0x80000000>>> i;
            int tt=a &ttt;
            int t = tt >>> (31 - i);
            System.out.print(t);
        }
        //无符号右移后(可以理解为带着符号位右移)，空缺的位置补0，所以生成的都是非负数
        int q=0;
        System.out.println();
        System.out.println(q>>>1);
    }
}
