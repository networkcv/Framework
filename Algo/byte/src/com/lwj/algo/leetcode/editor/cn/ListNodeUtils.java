package com.lwj.algo.leetcode.editor.cn;

import java.util.HashMap;
import java.util.Map;

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
    public static ListNode build(Integer integer) {
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

    public static ListNode buildByArray(Integer... nodes) {
        ListNode res = new ListNode();
        ListNode cur = res;
        for (Integer node : nodes) {
            cur.next = new ListNode(node);
            cur = cur.next;
        }
        return res.next;
    }

    /**
     * 根据数字构建链表，可以构建环形链表，相同的数字只会存在一个节点
     */
    public static ListNode buildCycle(Integer integer) {
        String[] nums = integer.toString().split("");
        Map<String, ListNode> numsMap = new HashMap<>();
        String preNum = null;
        for (String curNum : nums) {
            ListNode cur;
            if ((cur = numsMap.get(curNum)) == null) {
                cur = new ListNode(Integer.parseInt(curNum));
                numsMap.put(curNum, cur);
            }
            if (preNum != null) {
                ListNode preNode = numsMap.get(preNum);
                preNode.next = cur;
            }
            preNum = curNum;
        }
        return numsMap.get(nums[0]);
    }

}
