//给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字。 
//
// 请你将两个数相加，并以相同形式返回一个表示和的链表。 
//
// 你可以假设除了数字 0 之外，这两个数都不会以 0 开头。 
//
// 
//
// 示例 1： 
//
// 
//输入：l1 = [2,4,3], l2 = [5,6,4]
//输出：[7,0,8]
//解释：342 + 465 = 807.
// 
//
// 示例 2： 
//
// 
//输入：l1 = [0], l2 = [0]
//输出：[0]
// 
//
// 示例 3： 
//
// 
//输入：l1 = [9,9,9,9,9,9,9], l2 = [9,9,9,9]
//输出：[8,9,9,9,0,0,0,1]
// 
//
// 
//
// 提示： 
//
// 
// 每个链表中的节点数在范围 [1, 100] 内 
// 0 <= Node.val <= 9 
// 题目数据保证列表表示的数字不含前导零 
// 
// Related Topics 递归 链表 数学 👍 7850 👎 0


package com.lwj.algo.leetcode.editor.cn;

import java.math.BigDecimal;

class AddTwoNumbers {
    public ListNode num2node(BigDecimal i) {
        String s = String.valueOf(i.toString());
        StringBuffer stringBuffer = new StringBuffer(s).reverse();
        String[] split = stringBuffer.toString().split("");
        ListNode res = new ListNode();
        ListNode cur = res;
        for (String str : split) {
            cur.next = new ListNode(Integer.parseInt(str));
            cur = cur.next;
        }
        return res.next;
    }

    public BigDecimal node2num(ListNode node) {
        StringBuilder sb = new StringBuilder();
        while (node != null) {
            sb.append(node.val);
            node = node.next;
        }
        return new BigDecimal(sb.reverse().toString());
    }


    public static void main(String[] args) {
        Solution solution = new AddTwoNumbers().new Solution();
        System.out.println(solution.addTwoNumbers(ListNodeUtils.get(99999999), ListNodeUtils.get(9999)));//89990001
    }
    //leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for singly-linked list.
     * public class ListNode {
     * int val;
     * ListNode next;
     * ListNode() {}
     * ListNode(int val) { this.val = val; }
     * ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     * }
     */
    class Solution {
        public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
            //1.链表转化为数字再相加
//            return num2node(node2num(l1).add(node2num(l2)));
            //2.直接链表每个节点相加
            return fun2(l1, l2);
            //3.直接将结点加起来，不考虑进位，最后统一遍历节点做进位
        }

        public ListNode fun2(ListNode l1, ListNode l2) {
            ListNode res = new ListNode();
            ListNode cur = res;
            boolean flag = false;
            while (l1 != null && l2 != null) {
                int nodeCountVal = flag ? l1.val + l2.val + 1 : l1.val + l2.val;
                if (nodeCountVal >= 10) {
                    nodeCountVal = nodeCountVal - 10;
                    flag = true;
                } else {
                    flag = false;
                }
                cur.next = new ListNode(nodeCountVal);
                cur = cur.next;
                l1 = l1.next;
                l2 = l2.next;
            }
            if (l1 == null) {
                if (flag) {
                    while (l2 != null) {
                        int nodeCountVal = flag ? l2.val + 1 : l2.val;
                        if (nodeCountVal >= 10) {
                            flag = true;
                            nodeCountVal = 0;
                        } else {
                            flag = false;
                        }
                        cur.next = new ListNode(nodeCountVal);
                        cur = cur.next;
                        l2 = l2.next;
                    }
                } else {
                    cur.next = l2;
                }
            } else {
                if (flag) {
                    while (l1 != null) {
                        int nodeCountVal = flag ? l1.val + 1 : l1.val;
                        if (nodeCountVal >= 10) {
                            flag = true;
                            nodeCountVal = 0;
                        } else {
                            flag = false;
                        }
                        cur.next = new ListNode(nodeCountVal);
                        cur = cur.next;
                        l1 = l1.next;
                    }
                } else {
                    cur.next = l1;
                }
            }
            if (flag) {
                cur.next = new ListNode(1);
            }
            return res.next;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}