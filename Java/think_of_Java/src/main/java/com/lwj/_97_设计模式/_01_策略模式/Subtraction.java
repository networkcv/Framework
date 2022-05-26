package com.lwj._97_设计模式._01_策略模式;

/**
 * create by lwj on 2020/3/16
 */
public class Subtraction implements Operator {
    @Override
    public int process(int i1, int i2) {
        return i1-i2;
    }
}
