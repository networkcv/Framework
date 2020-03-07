package com.lwj._99_strategy_pattern._02_factory;

/**
 * create by lwj on 2019/9/27
 */
public class Calculator {
    private Operation operation;

    public Calculator() {}

    public Calculator(Operation operation) {
        this.operation = operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public double getRes(double a, double b) {
        return operation.getResult(a, b);
    }
}
