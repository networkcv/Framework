package com.lwj.algo.leetcode.editor.cn.custom._02_数组._01_回文串的判断;

/**
 * create by lwj on 2019/8/12
 */
public class Test1 {
    public static void main(String[] args) {
        String s = "123321";
        StringBuffer sb = new StringBuffer(s);
        StringBuffer rsb = sb.reverse();
        if (s.equals(rsb.toString())) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
    }
}
