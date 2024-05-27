//给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。
// 
// 
// 
//
// 示例 1： 
//
// 
//输入：head = [1,2,3,4,5]
//输出：[5,4,3,2,1]
// 
//
// 示例 2： 
//
// 
//输入：head = [1,2]
//输出：[2,1]
// 
//
// 示例 3： 
//
// 
//输入：head = []
//输出：[]
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点的数目范围是 [0, 5000] 
// -5000 <= Node.val <= 5000 
// 
//
// 
//
// 进阶：链表可以选用迭代或递归方式完成反转。你能否用两种方法解决这道题？ 
// 
// 
// Related Topics 递归 链表 👍 2649 👎 0


package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ListNode;
import com.lwj.algo.leetcode.editor.cn.utils.ListNodeUtils;

class ReverseLinkedList {
    public static void main(String[] args) {
        Solution solution = new ReverseLinkedList().new Solution();
        System.out.println(solution.reverseList(ListNodeUtils.build(12345), 3));
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
        //后驱节点
        ListNode successor = null;

        //递归反转链表前k个节点
        public ListNode reverseList(ListNode head, Integer k) {
            if (k == 1) {
                successor = head.next;
                return head;
            }
            ListNode last = reverseList(head.next, k - 1);
            head.next.next = head;
            head.next = successor;
            return last;
        }

        //迭代反转链表前k个节点
        public ListNode reverseList2(ListNode head, Integer k) {
            ListNode pre = null;
            ListNode cur = head;
            ListNode next;
            while (k-- > 0) {
                next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
            head.next = cur;
            return pre;
        }

        //递归法
        public ListNode reverseList(ListNode head) {
            if (head == null || head.next == null) {
                return head;
            }
            ListNode last = reverseList(head.next);
            head.next.next = head;
            head.next = null;
            return last;
        }

        //迭代法
        public ListNode reverseList3(ListNode head) {
            ListNode pre = null;
            ListNode cur = head;
            ListNode next;
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