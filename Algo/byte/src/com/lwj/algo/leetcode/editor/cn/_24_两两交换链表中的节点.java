package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ListNode;

/**
 * 给你一个链表，两两交换其中相邻的节点，并返回交换后链表的头节点。你必须在不修改节点内部的值的情况下完成本题（即，只能进行节点交换）。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：head = [1,2,3,4]
 * 输出：[2,1,4,3]
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：head = []
 * 输出：[]
 * <p>
 * <p>
 * 示例 3：
 * <p>
 * <p>
 * 输入：head = [1]
 * 输出：[1]
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 链表中节点的数目在范围 [0, 100] 内
 * 0 <= Node.val <= 100
 * <p>
 * <p>
 * Related Topics递归 | 链表
 * <p>
 * 👍 2352, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class SwapNodesInPairs {
    public static void main(String[] args) {
        Solution solution = new SwapNodesInPairs().new Solution();
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
        // 2025/2/13  递归版
        //1 2 3 4
        public ListNode swapPairs(ListNode head) {
            if (head == null || head.next == null) return head;
            ListNode nextRecursiveNode = head.next.next;
            head.next.next = head;
            ListNode newHead = head.next;
            head.next = swapPairs(nextRecursiveNode);
            return newHead;
        }

        // 2025/2/13  //k个一组反转元素的简化版，k=2
        public ListNode swapPairs1(ListNode head) {
            int k = 2;
            ListNode tmp = head;
            //链表长度
            int len = 0;
            while (tmp != null) {
                len++;
                tmp = tmp.next;
            }
            ListNode dummy = new ListNode(-1);
            dummy.next = head;
            //正在翻转的这组链表的前一个节点
            ListNode p0 = dummy;
            ListNode pre = dummy;
            ListNode cur = head;
            while (len - k >= 0) {
                len = len - k;
                int n = 0;
                //翻转一组链表节点  -1 [1,2,3,4,5], k = 2
                while (n++ < k) {
                    ListNode next = cur.next;
                    cur.next = pre;
                    pre = cur;
                    cur = next;
                }
                p0.next.next = cur;
                ListNode nextP0 = p0.next;
                p0.next = pre;
                p0 = nextP0;
            }
            return dummy.next;
        }

        //2022/4/9
        public ListNode swapPairs0(ListNode head) {
            //1.迭代法 时间复杂度O(n) 空间复杂度O(1)
//                    return iterate(head);
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