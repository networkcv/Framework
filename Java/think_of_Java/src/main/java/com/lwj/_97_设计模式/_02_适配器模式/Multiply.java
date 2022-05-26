package com.lwj._97_设计模式._02_适配器模式;

/**
 * create by lwj on 2020/3/16
 * 已有的类，也是对两个数进行操作，但是没有实现Operator接口，因此无法直接作为参数传入。
 */
public class Multiply {
    public int multiply(int i1, int i2) {
        return i1 * i2;
    }
}
