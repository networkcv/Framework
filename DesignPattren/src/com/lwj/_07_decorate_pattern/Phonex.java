package com.lwj._07_decorate_pattern;

/**
 * create by lwj on 2019/8/3
 */
public class Phonex implements Phone {
    @Override
    public void call() {
        System.out.println("打电话");
    }

}
