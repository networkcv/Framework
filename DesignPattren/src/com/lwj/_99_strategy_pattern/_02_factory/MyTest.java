package com.lwj._99_strategy_pattern._02_factory;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * create by lwj on 2019/9/27
 */
public class MyTest {
    @Test
    public void test(){
        Calculator calculator = new Calculator(OperationFactory.getOperationInstance("-"));
        System.out.println(calculator.getRes(1,1));
        Calculator calculator1 = new Calculator(OperationFactory.getOperationInstance("+"));
        System.out.println(calculator1.getRes(1,1));
        Calculator calculator2 = new Calculator(OperationFactory.getOperationInstance("*"));
        System.out.println(calculator2.getRes(1,1));
        HashMap map =new HashMap();
        map.put("1","ok");
    }
}
