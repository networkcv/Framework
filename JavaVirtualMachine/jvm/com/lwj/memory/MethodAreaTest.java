package com.lwj.memory;

import java.io.Serializable;

public class MethodAreaTest extends Object implements Serializable {

    private static String name = "hello java";

    public MethodAreaTest(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        int x = 100;
        int y = 100;
        int result = testSum(x, y);
        System.out.println(result);
    }

    public static int testSum(int x , int y){
        return  x + y;
    }
}
