package com.lwj.algo.leetcode.editor.cn.custom._09_跳表;


/**
 * create by lwj on 2019/8/8
 */
public class Test2 {
    public static void main(String[] args) {
        SkipList skipList = new SkipList();
        skipList.insert(1, 2);
        skipList.insert(5, 3);
        skipList.insert(2, 1);
        skipList.insert(4, 2);
        System.out.println(skipList.find(6));
        skipList.insert(3, 2);
        System.out.println(skipList.find(3));
        skipList.delete(3);
    }

}
