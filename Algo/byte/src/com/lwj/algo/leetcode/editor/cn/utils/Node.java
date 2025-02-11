package com.lwj.algo.leetcode.editor.cn.utils;

/**
 * Date: 2022/4/1
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class Node {
    public int val;
    public Node pre;
    public Node next;

    public Node() {
    }

    public Node(int val) {
        this.val = val;
    }

    @Override
    public String toString() {
        String cur = "ListNode{" +
                "val=" + val +
                '}';
        String nextStr = next != null ? next.toString() : "";
        return cur + " " + nextStr;
    }
}
