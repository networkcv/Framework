//给你两个单链表的头节点 headA 和 headB ，请你找出并返回两个单链表相交的起始节点。如果两个链表没有交点，返回 null 。 
//
// 图示两个链表在节点 c1 开始相交： 
//
// 
//
// 题目数据 保证 整个链式结构中不存在环。 
//
// 注意，函数返回结果后，链表必须 保持其原始结构 。 
//
// 
//
// 示例 1： 
//
// 
//
// 
//输入：intersectVal = 8, listA = [4,1,8,4,5], listB = [5,0,1,8,4,5], skipA = 2, 
//skipB = 3
//输出：Intersected at '8'
//解释：相交节点的值为 8 （注意，如果两个链表相交则不能为 0）。
//从各自的表头开始算起，链表 A 为 [4,1,8,4,5]，链表 B 为 [5,0,1,8,4,5]。
//在 A 中，相交节点前有 2 个节点；在 B 中，相交节点前有 3 个节点。
// 
//
// 示例 2： 
//
// 
//
// 
//输入：intersectVal = 2, listA = [0,9,1,2,4], listB = [3,2,4], skipA = 3, skipB = 
//1
//输出：Intersected at '2'
//解释：相交节点的值为 2 （注意，如果两个链表相交则不能为 0）。
//从各自的表头开始算起，链表 A 为 [0,9,1,2,4]，链表 B 为 [3,2,4]。
//在 A 中，相交节点前有 3 个节点；在 B 中，相交节点前有 1 个节点。
// 
//
// 示例 3： 
//
// 
//
// 
//输入：intersectVal = 0, listA = [2,6,4], listB = [1,5], skipA = 3, skipB = 2
//输出：null
//解释：从各自的表头开始算起，链表 A 为 [2,6,4]，链表 B 为 [1,5]。
//由于这两个链表不相交，所以 intersectVal 必须为 0，而 skipA 和 skipB 可以是任意值。
//这两个链表不相交，因此返回 null 。
// 
//
// 
//
// 提示： 
//
// 
// listA 中节点数目为 m 
// listB 中节点数目为 n 
// 0 <= m, n <= 3 * 10⁴ 
// 1 <= Node.val <= 10⁵ 
// 0 <= skipA <= m 
// 0 <= skipB <= n 
// 如果 listA 和 listB 没有交点，intersectVal 为 0 
// 如果 listA 和 listB 有交点，intersectVal == listA[skipA + 1] == listB[skipB + 1] 
// 
//
// 
//
// 进阶：你能否设计一个时间复杂度 O(n) 、仅用 O(1) 内存的解决方案？ 
// Related Topics 哈希表 链表 双指针 👍 239 👎 0


package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ListNodeUtils;
import com.lwj.algo.leetcode.editor.cn.utils.Pair;

class IntersectionOfTwoLinkedListsLcci {
    public static void main(String[] args) {
        Solution solution = new IntersectionOfTwoLinkedListsLcci().new Solution();
        System.out.println(solution.getIntersectionNode(null, null));
        Pair<ListNode, ListNode> pair0 = ListNodeUtils.buildIntersection(3, 23);
        System.out.println(solution.getIntersectionNode(pair0.getLeft(), pair0.getRight()).val);
        Pair<ListNode, ListNode> pair2 = ListNodeUtils.buildIntersection(135, 246);
        System.out.println(solution.getIntersectionNode(pair2.getLeft(), pair2.getRight()));
        Pair<ListNode, ListNode> pair3 = ListNodeUtils.buildIntersection(91345, 2345);
        System.out.println(solution.getIntersectionNode(pair3.getLeft(), pair3.getRight()).val);
        Pair<ListNode, ListNode> pair4 = ListNodeUtils.buildIntersection(1, 1);
        System.out.println(solution.getIntersectionNode(pair4.getLeft(), pair4.getRight()).val);
        Pair<ListNode, ListNode> pair5 = ListNodeUtils.buildIntersection(21845, 501845);
        System.out.println(solution.getIntersectionNode(pair5.getLeft(), pair5.getRight()).val);
    }
    //leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for singly-linked list.
     * public class ListNode {
     * int val;
     * ListNode next;
     * ListNode(int x) {
     * val = x;
     * next = null;
     * }
     * }
     */
    public class Solution {
        //无环链表相交 将两个链表首位相连，就变成了判断是否有环 有环则相交，无环则不相交
        public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
            if (headA == null || headB == null) {
                return null;
            }
            ListNode last = headA;
            while (last.next != null) {
                last = last.next;
            }
            ListNode p1 = last;
            ListNode p2 = headB;
            p1.next = p2;

            //判断是否有环
            ListNode res = hasCycle(headA);
            last.next = null;
            return res;
        }

        private ListNode hasCycle(ListNode head) {
            ListNode fast = head;
            ListNode slow = head;
            while (fast != null && fast.next != null) {
                fast = fast.next.next;
                slow = slow.next;
                if (fast == slow) {
                    break;
                }
            }
            if (fast == null || fast.next == null) {
                return null;
            }
            slow = head;
            while (fast != slow) {
                slow = slow.next;
                fast = fast.next;
            }
            return slow;
        }

        //无环链表相交 两个链表互相拼接的方式来对齐链表
        public ListNode getIntersectionNode3(ListNode headA, ListNode headB) {
            ListNode p1 = headA;
            ListNode p2 = headB;
            while (p1 != p2) {
                if (p1 == null) {
                    p1 = headB;
                } else {
                    p1 = p1.next;
                }
                if (p2 == null) {
                    p2 = headA;
                } else {
                    p2 = p2.next;
                }
            }
            return p1;
        }

        //无环链表相交 笨办法，先让两个链表对齐
        public ListNode getIntersectionNode2(ListNode headA, ListNode headB) {
            ListNode p1 = headA;
            ListNode p2 = headB;
            while (p1 != null && p2 != null) {
                if (p1 == p2) {
                    return p1;
                }
                p1 = p1.next;
                p2 = p2.next;
            }
            if (p1 == null && p2 == null) {
                return null;
            }
            if (p1 == null) {
                return find(headB, headA, p2);
            }
            return find(headA, headB, p1);
        }

        private ListNode find(ListNode longList, ListNode shortList, ListNode nonNullListNode) {
            while (nonNullListNode != null) {
                longList = longList.next;
                nonNullListNode = nonNullListNode.next;
            }
            while (shortList != null && longList != null) {
                if (shortList == longList) {
                    return shortList;
                }
                shortList = shortList.next;
                longList = longList.next;
            }
            return null;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}