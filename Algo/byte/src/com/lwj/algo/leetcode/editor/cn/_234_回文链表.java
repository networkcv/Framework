package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ListNode;
import com.lwj.algo.leetcode.editor.cn.utils.ListNodeUtils;

import java.util.Stack;

/**
 * 给你一个单链表的头节点 head ，请你判断该链表是否为回文链表。如果是，返回 true ；否则，返回 false 。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：head = [1,2,2,1]
 * 输出：true
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：head = [1,2]
 * 输出：false
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 链表中节点数目在范围[1, 10⁵] 内
 * 0 <= Node.val <= 9
 * <p>
 * <p>
 * <p>
 * <p>
 * 进阶：你能否用 O(n) 时间复杂度和 O(1) 空间复杂度解决此题？
 * <p>
 * Related Topics栈 | 递归 | 链表 | 双指针
 * <p>
 * 👍 2007, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class PalindromeLinkedList {
    public static void main(String[] args) {
        Solution solution = new PalindromeLinkedList().new Solution();
//        System.out.println(solution.isPalindrome(ListNodeUtils.build(101)));
        System.out.println(solution.isPalindrome(ListNodeUtils.build(1221)));
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

        //使用栈压入前半段元素，然后出栈比对后半段元素
        public boolean isPalindrome(ListNode head) {
            Stack<Integer> stack = new Stack<>();
            ListNode q = head;
            ListNode s = head;
            if (head != null && head.next == null) {
                return true;
            }
            while (q != null && q.next != null) {
                stack.push(s.val);
                q = q.next.next;
                s = s.next;
            }
            //处理奇数链表,奇数链表遍历后q指向最后一个元素，偶数链表遍历后q指向null
            if (q != null) {
                stack.push(s.val);
            }
            while (s != null) {
                if (s.val != stack.pop()) {
                    return false;
                }
                s = s.next;
            }
            return true;
        }

        ListNode left;

        //2022/7/27
        //模仿双指针实现回文判断,巧妙使用递归压栈，从后往前遍历
        public boolean isPalindrome1(ListNode head) {
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

        //2022/7/27
        //寻找中点，反转后半段，再进行比较
        public boolean isPalindrome2(ListNode head) {
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
            return travelListNodeEquals(head, newHead);
        }

        /**
         * 遍历比较链表节点是否一致
         *
         * @param head1 head1
         * @param head2 head2
         * @return 是否一致
         */
        private boolean travelListNodeEquals(ListNode head1, ListNode head2) {
            //进行比较
            while (head2 != null) {
                if (head2.val != head1.val) {
                    return false;
                }
                head2 = head2.next;
                head1 = head1.next;
            }
            return true;
        }

        //2022/7/27
        //先复制链表，再反转整个链表，再进行比较
        public boolean isPalindrome3(ListNode head) {
            ListNode copyHead = copy(head);
            ListNode newHead = reverseListNode(copyHead);
            return travelListNodeEquals(head, newHead);
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