package com.lwj.algo.leetcode.editor.cn.utils;

/**
 * Date: 2022/4/1
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class ListNode {
    public int val;
    public ListNode next;

    public ListNode() {
    }

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
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
