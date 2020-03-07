package com.lwj._99_strategy_pattern._02_factory;

/**
 * create by lwj on 2019/9/27
 */
public class AddOperation implements Operation {
    @Override
    public double getResult(double a, double b) {
        return a+b;
    }
}
