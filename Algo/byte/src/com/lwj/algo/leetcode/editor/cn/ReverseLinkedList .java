////给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。
//// 
//// 
//// 
////
//// 示例 1： 
////
//// 
////输入：head = [1,2,3,4,5]
////输出：[5,4,3,2,1]
//// 
////
//// 示例 2： 
////
//// 
////输入：head = [1,2]
////输出：[2,1]
//// 
////
//// 示例 3： 
////
//// 
////输入：head = []
////输出：[]
//// 
////
//// 
////
//// 提示： 
////
//// 
//// 链表中节点的数目范围是 [0, 5000] 
//// -5000 <= Node.val <= 5000 
//// 
////
//// 
////
//// 进阶：链表可以选用迭代或递归方式完成反转。你能否用两种方法解决这道题？ 
//// 
//// 
//// Related Topics 递归 链表 👍 2405 👎 0
//


package com.lwj.algo.leetcode.editor.cn;

class ReverseLinkedList {
    public static void main(String[] args) {
        Solution solution = new ReverseLinkedList().new Solution();
        ListNode res = solution.reverseList(ListNodeUtils.get(123));
        System.out.println(res);
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
        public ListNode reverseList(ListNode head) {
            //1.迭代
//            ListNode preNode = null;
//            ListNode nextNode = null;
//
//            while (head != null) {
//                nextNode = head.next;
//                head.next = preNode;
//                preNode = head;
//                head = nextNode;
//            }
//            return preNode;
            //2.递归
//            ListNode preNode = null;
//            return reverseNode(head, preNode);
            //3.递归2
            return reverse(head);
        }

        public ListNode reverse(ListNode head) {
            if (head == null || head.next == null) {
                return head;
            }
            ListNode newHead = reverse(head.next);
            head.next.next = head;
            head.next = null;
            return newHead;
        }


        private ListNode reverseNode(ListNode head, ListNode preNode) {
            if (head == null) {
                return preNode;
            }
            ListNode nextNode = head.next;
            head.next = preNode;
            preNode = head;
            head = nextNode;
            return reverseNode(head, preNode);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}