package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ListNode;


class ReverseLinkedList {
    public static void main(String[] args) {
        Solution solution = new ReverseLinkedList().new Solution();
        System.out.println(solution);
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


        // 2025/2/13  递归版 1 2 3
        //反转当前链表，并返回反转后的头节点
        public ListNode reverseList1(ListNode head) {
            if (head == null || head.next == null) {
                return head;
            }
            ListNode newHead = reverseList1(head.next);
            head.next.next = head; // 把下一个节点指向自己
            head.next = null; // 断开指向下一个节点的连接，保证最终链表的末尾节点的 next 是空节点
            return newHead;
        }

        // 2025/2/11  遍历版
        public ListNode reverseList(ListNode head) {
            ListNode pre = null;
            ListNode next;
            ListNode cur = head;
            while (cur != null) {
                next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
            return pre;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}