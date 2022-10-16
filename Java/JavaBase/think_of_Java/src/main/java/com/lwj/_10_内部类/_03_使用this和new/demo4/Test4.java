package com.lwj._10_内部类._03_使用this和new.demo4;


/**
 * create by lwj on 2020/3/17
 * 在内部类中可以生成对外部类的引用
 */
public class Test4 {
    public static void main(String[] args) {
        Sequence sequence = new Sequence(10);
        Sequence.SequenceSelector iterator = (Sequence.SequenceSelector) sequence.iterator();
        Sequence sequence1 = iterator.sequence();
        System.out.println(sequence==sequence1);
    }
}
