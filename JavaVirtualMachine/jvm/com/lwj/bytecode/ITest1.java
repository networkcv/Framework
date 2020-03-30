package com.lwj.bytecode;

/**
 * create by lwj on 2020/2/26
 */
public interface ITest1 {
    public static int a=1;
}

class Test11 implements  ITest1{
    public static int b=a;
}