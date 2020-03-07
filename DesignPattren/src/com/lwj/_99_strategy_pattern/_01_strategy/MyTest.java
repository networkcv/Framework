package com.lwj._99_strategy_pattern._01_strategy;

import org.junit.Test;

/**
 * create by Administrator on 2018/7/29
 * 策略模式
 */
public class MyTest {

    @Test
    public void fun1() {
        Calculator calculator = new Calculator();
        calculator.setStrategy(new Addition());
        int addition = calculator.getResult(2, 1);
        calculator.setStrategy(new Subtraction());
        int subtraction = calculator.getResult(2, 1);
        System.out.println(addition);
        System.out.println(subtraction);


    }
}
