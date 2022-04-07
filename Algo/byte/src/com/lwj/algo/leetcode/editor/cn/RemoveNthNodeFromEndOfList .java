////给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。 
////
//// 
////
//// 示例 1： 
////
//// 
////输入：head = [1,2,3,4,5], n = 2
////输出：[1,2,3,5]
//// 
////
//// 示例 2： 
////
//// 
////输入：head = [1], n = 1
////输出：[]
//// 
////
//// 示例 3： 
////
//// 
////输入：head = [1,2], n = 1
////输出：[1]
//// 
////
//// 
////
//// 提示： 
////
//// 
//// 链表中结点的数目为 sz 
//// 1 <= sz <= 30 
//// 0 <= Node.val <= 100 
//// 1 <= n <= sz 
//// 
////
//// 
////
//// 进阶：你能尝试使用一趟扫描实现吗？ 
//// Related Topics 链表 双指针 👍 1949 👎 0
//


package com.lwj.algo.leetcode.editor.cn;

class RemoveNthNodeFromEndOfList {
    public static void main(String[] args) {
        Solution solution = new RemoveNthNodeFromEndOfList().new Solution();
        ListNode listNode = ListNodeUtils.get(123);
        solution.removeNthFromEnd(listNode,3);
        solution.removeNthFromEnd(listNode,1);
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
        public ListNode removeNthFromEnd(ListNode head, int n) {
            //构建一个虚拟头节点，从这个节点遍历，就可以将删除头节点这一情况规避掉
            ListNode res = new ListNode();
            ListNode first = res;
            ListNode slow = res;
            res.next = head;
            while (n-- > 0) {
                first = first.next;
            }
            while (first.next != null) {
                first = first.next;
                slow = slow.next;
            }
            slow.next = slow.next.next;
            return res.next;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}