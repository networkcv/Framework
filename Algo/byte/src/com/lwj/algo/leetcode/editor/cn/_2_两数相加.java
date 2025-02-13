package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ListNode;
import com.lwj.algo.leetcode.editor.cn.utils.ListNodeUtils;

class AddTwoNumbers {
    public static void main(String[] args) {
        Solution solution = new AddTwoNumbers().new Solution();
        System.out.println(solution.addTwoNumbers(ListNodeUtils.build(999), ListNodeUtils.build(9)));
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
            ListNode pre = new ListNode(0);
            ListNode curNode = pre;
            int remainder = 0;
            while (l1 != null && l2 != null) {
                int sum = l1.val + l2.val + remainder;
                int number = sum % 10;
                remainder = sum / 10;
                curNode.next = new ListNode(number);
                curNode = curNode.next;
                l1 = l1.next;
                l2 = l2.next;
            }
            if (l1 == null) {
                while (l2 != null) {
                    int sum = l2.val + remainder;
                    int number = sum % 10;
                    remainder = sum / 10;
                    curNode.next = new ListNode(number);
                    curNode = curNode.next;
                    l2 = l2.next;
                }
            }
            while (l1 != null) {
                int sum = l1.val + remainder;
                int number = sum % 10;
                remainder = sum / 10;
                curNode.next = new ListNode(number);
                curNode = curNode.next;
                l1 = l1.next;
            }
            if (remainder == 1) {
                curNode.next = new ListNode(1);
            }

            return pre.next;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}