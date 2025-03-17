package com.lwj.algo.leetcode.editor.cn.custom._09_跳表;


/**
 * create by lwj on 2019/8/8
 */
public class Test2 {
    public static void main(String[] args) {
        SkipList skipList = new SkipList();
        skipList.insert2(1);
        skipList.insert2(2);
        skipList.insert2(3);
        skipList.insert2(4);
        skipList.insert2(5);
        System.out.println(skipList.find2(6));
        System.out.println(skipList.find2(3));
        skipList.delete2(3);
        System.out.println(skipList.find2(3));
    }

}
