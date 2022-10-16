package com.lwj._97_设计模式._02_适配器模式;

/**
 * create by lwj on 2020/3/16
 */
public class MultiplyAdapter2 implements Operator {
    Multiply multiply;

    public MultiplyAdapter2(Multiply multiply) {
        this.multiply = multiply;
    }

    @Override
    public int process(int i1, int i2) {
        return multiply.multiply(i1, i2);
    }
}
