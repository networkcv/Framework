package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ListNode;
import com.lwj.algo.leetcode.editor.cn.utils.ListNodeUtils;

import java.util.Stack;

/**
 * 给你两个 非空 链表来代表两个非负整数。数字最高位位于链表开始位置。它们的每个节点只存储一位数字。将这两数相加会返回一个新的链表。
 * <p>
 * 你可以假设除了数字 0 之外，这两个数字都不会以零开头。
 * <p>
 * <p>
 * <p>
 * 示例1：
 * <p>
 * <p>
 * <p>
 * <p>
 * 输入：l1 = [7,2,4,3], l2 = [5,6,4]
 * 输出：[7,8,0,7]
 * <p>
 * <p>
 * 示例2：
 * <p>
 * <p>
 * 输入：l1 = [2,4,3], l2 = [5,6,4]
 * 输出：[8,0,7]
 * <p>
 * <p>
 * 示例3：
 * <p>
 * <p>
 * 输入：l1 = [0], l2 = [0]
 * 输出：[0]
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 链表的长度范围为 [1, 100]
 * 0 <= node.val <= 9
 * 输入数据保证链表代表的数字无前导 0
 * <p>
 * <p>
 * <p>
 * <p>
 * 进阶：如果输入链表不能翻转该如何解决？
 * <p>
 * Related Topics栈 | 链表 | 数学
 * <p>
 * 👍 752, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class AddTwoNumbersIi {
    public static void main(String[] args) {
        Solution solution = new AddTwoNumbersIi().new Solution();
        ListNode l1 = ListNodeUtils.buildByArray(7, 2, 4, 3);
        ListNode l2 = ListNodeUtils.buildByArray(5, 6, 4);
//        System.out.println(solution.reverse(l1));
//        System.out.println(solution.reverse(l2));
//        System.out.println(solution.addTwoNumbers(solution.reverse(l1),solution.reverse(l2)));
//        System.out.println(solution.twoSum(l1, l2));
        System.out.println(solution.addTwoNumbers(l1, l2));
//        System.out.println(solution.reverse(ListNodeUtils.build(1234)));
//        System.out.println(solution.twoSum(ListNodeUtils.build(1234), ListNodeUtils.build(1234)));
//        System.out.println(solution.addTwoNumbers(ListNodeUtils.buildByArray(7, 2, 4, 3), l2));
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
        // 2025/2/13  两个链表反转，然后两个链表相加，再把结果反转返回
        public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
            ListNode r1 = reverse(l1);
            ListNode r2 = reverse(l2);
            ListNode sum = twoSum(r1, r2);
            return reverse(sum);
        }

        //反转链表 1 2
        public ListNode reverse(ListNode head) {
            ListNode pre = null;
            ListNode cur = head;
            while (cur != null) {
                ListNode next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
            return pre;
        }

        //两个链表相加 // 3427 465 = 7087
        public ListNode twoSum(ListNode l1, ListNode l2) {
            int carry = 0;
            ListNode dummpy = new ListNode(0);
            ListNode head = dummpy;
            while (l1 != null || l2 != null || carry != 0) {
                int sum = carry;
                if (l1 != null) {
                    sum += l1.val;
                    l1 = l1.next;
                }
                if (l2 != null) {
                    sum += l2.val;
                    l2 = l2.next;
                }
                //head 1
                ListNode cur = new ListNode(sum % 10);
                carry = sum / 10;
                head.next = cur;
                head = cur;
            }
            return dummpy.next;
        }


        // 2025/2/13  使用栈结构，先对两个链表进行压栈，然后同时出栈，相加组成新链表
        public ListNode addTwoNumbers1(ListNode l1, ListNode l2) {
            Stack<Integer> s1 = new Stack<>();
            Stack<Integer> s2 = new Stack<>();
            while (l1 != null) {
                s1.push(l1.val);
                l1 = l1.next;
            }
            while (l2 != null) {
                s2.push(l2.val);
                l2 = l2.next;
            }
            int carry = 0;
            ListNode head = null;
            //          h
            //  null <- 1
            while (!s1.isEmpty() || !s2.isEmpty() || carry != 0) {
                int i1 = s1.isEmpty() ? 0 : s1.pop();
                int i2 = s2.isEmpty() ? 0 : s2.pop();
                int sum = i1 + i2 + carry;
                ListNode cur = new ListNode(sum % 10);
                carry = sum / 10;
                cur.next = head;
                head = cur;
            }
            return head;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}