//给你一个单链表的头节点 head ，请你判断该链表是否为回文链表。如果是，返回 true ；否则，返回 false 。 
//
// 
//
// 示例 1： 
//
// 
//输入：head = [1,2,2,1]
//输出：true
// 
//
// 示例 2： 
//
// 
//输入：head = [1,2]
//输出：false
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点数目在范围[1, 10⁵] 内 
// 0 <= Node.val <= 9 
// 
//
// 
//
// 进阶：你能否用 O(n) 时间复杂度和 O(1) 空间复杂度解决此题？ 
// Related Topics 栈 递归 链表 双指针 👍 1454 👎 0


package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ListNodeUtils;

class PalindromeLinkedList {
    public static void main(String[] args) {
        Solution solution = new PalindromeLinkedList().new Solution();
//        System.out.println(solution.isPalindrome(ListNodeUtils.build(1221)));
        System.out.println(solution.isPalindrome(ListNodeUtils.build(121)));
        System.out.println(solution.isPalindrome(ListNodeUtils.build(1222)));
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

        ListNode left;

        //模仿双指针实现回文判断,巧妙使用递归压栈，从后往前遍历
        public boolean isPalindrome2(ListNode head) {
            left = head;
            return traverse(head);
        }

        private boolean traverse(ListNode right) {
            if (right == null)
                return true;
            boolean res = traverse(right.next);
            res = (left.val == right.val) && res;
            left = left.next;
            return res;
        }

        //寻找中点，反转后半段，再进行比较
        public boolean isPalindrome(ListNode head) {
            ListNode f = head, s = head;
            while (f != null && f.next != null) {
                f = f.next.next;
                s = s.next;
            }
            //奇数链表中点的下一个节点
            if (f != null) {
                s = s.next;
            }
            //反转链表
            ListNode newHead = reverseListNode(s);
            //进行比较
            while (newHead != null) {
                if (newHead.val != head.val) {
                    return false;
                }
                newHead = newHead.next;
                head = head.next;
            }
            return true;
        }

        //先复制链表，再反转整个链表，再进行比较
        public boolean isPalindrome3(ListNode head) {
            ListNode copyHead = copy(head);
            ListNode newHead = reverseListNode(copyHead);
            while (newHead != null) {
                if (newHead.val != head.val) {
                    return false;
                }
                head = head.next;
                newHead = newHead.next;
            }
            return true;
        }

        private ListNode copy(ListNode head) {
            if (head == null) {
                return null;
            }
            ListNode dummy = new ListNode();
            ListNode p = dummy;
            while (head != null) {
                p.next = new ListNode(head.val);
                head = head.next;
                p = p.next;

            }
            return dummy.next;
        }

        private ListNode reverseListNode(ListNode head) {
            if (head == null || head.next == null) {
                return head;
            }
            ListNode last = reverseListNode(head.next);
            head.next.next = head;
            head.next = null;
            return last;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}