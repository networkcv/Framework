//给你一个链表，两两交换其中相邻的节点，并返回交换后链表的头节点。你必须在不修改节点内部的值的情况下完成本题（即，只能进行节点交换）。 
//
// 
//
// 示例 1： 
//
// 
//输入：head = [1,2,3,4]
//输出：[2,1,4,3]
// 
//
// 示例 2： 
//
// 
//输入：head = []
//输出：[]
// 
//
// 示例 3： 
//
// 
//输入：head = [1]
//输出：[1]
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点的数目在范围 [0, 100] 内 
// 0 <= Node.val <= 100 
// 
// Related Topics 递归 链表 👍 1337 👎 0


package com.lwj.algo.leetcode.editor.cn;

class SwapNodesInPairs {
    public static void main(String[] args) {
        Solution solution = new SwapNodesInPairs().new Solution();
        solution.swapPairs(ListNodeUtils.get(2143));
        solution.swapPairs(ListNodeUtils.get(213));
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
        public ListNode swapPairs(ListNode head) {
            //1.迭代法 时间复杂度O(n) 空间复杂度O(1)
//            return iterate(head);
            // 递归法
            return recursive(head);
        }

        private ListNode recursive(ListNode head) {
            if (head == null || head.next == null) {
                return head;
            }
            ListNode tmp;
            ListNode nextRecursive = head.next.next;
            head.next.next = head;
            tmp = head.next;
            head.next = recursive(nextRecursive);
            return tmp;
        }

        private ListNode iterate(ListNode head) {
            ListNode dummyHead = new ListNode();
            ListNode pre = dummyHead;
            dummyHead.next = head;
            ListNode cur = dummyHead.next;
            while (cur != null && cur.next != null) {
                ListNode next = cur.next.next;
                pre.next = cur.next;
                cur.next.next = cur;
                cur.next = next;
                pre = cur;
                cur = next;
            }
            return dummyHead.next;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}