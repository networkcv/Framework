package com.lwj._97_设计模式._01_策略模式;

/**
 * create by lwj on 2020/3/16
 * 所谓的策略模式，其实就是通过不同的参数对象来使方法表现出不同的行为。
 */
public class Test1 {
    public static int process(Operator operator, int i1, int i2) {
        return operator.process(i1, i2);
    }
    public static void main(String[] args) {
        System.out.println(process(new Addition(), 2, 1));
        System.out.println(process(new Subtraction(), 2, 1));
    }
}
