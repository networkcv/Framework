package com.lwj.algo.leetcode.editor.cn;

/**
 * Date: 2022/4/7
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class ListNodeUtils {

    /**
     * 根据数字获取链表
     */
    public static ListNode get(Integer integer) {
        String s = integer.toString();
        StringBuffer stringBuffer = new StringBuffer(s);
        String[] split = stringBuffer.toString().split("");
        ListNode res = new ListNode();
        ListNode cur = res;
        for (String str : split) {
            cur.next = new ListNode(Integer.parseInt(str));
            cur = cur.next;
        }
        return res.next;
    }

    public static ListNode getByArray(Integer... nodes) {
        ListNode res = new ListNode();
        ListNode cur = res;
        for (Integer node : nodes) {
            cur.next = new ListNode(node);
            cur = cur.next;
        }
        return res.next;
    }

}
