package com.lwj._21_并发._01_并行设计模式._00_单例模式;

/**
 * create by lwj on 2019/11/18
 */
public class SinglePattern {
    private SinglePattern() {
    }

    public static SinglePattern getSinglePatern() {
        return InnerClass.singlePattern;
    }

    private static class InnerClass {
        static SinglePattern singlePattern = new SinglePattern();
    }

    public static void main(String[] args) {
        SinglePattern singlePatern = SinglePattern.getSinglePatern();
        SinglePattern singlePatern2 = SinglePattern.getSinglePatern();
        System.out.println(singlePatern);
        System.out.println(singlePatern2);
    }
}
