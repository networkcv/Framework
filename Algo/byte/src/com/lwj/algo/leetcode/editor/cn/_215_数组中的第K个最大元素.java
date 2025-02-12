package com.lwj.algo.leetcode.editor.cn;

import java.util.PriorityQueue;

/**
 * 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
 * <p>
 * 请注意，你需要找的是数组排序后的第 k 个最大的元素，而不是第 k 个不同的元素。
 * <p>
 * 你必须设计并实现时间复杂度为 O(n) 的算法解决此问题。
 * <p>
 * <p>
 * <p>
 * 示例 1:
 * <p>
 * <p>
 * 输入: [3,2,1,5,6,4], k = 2
 * 输出: 5
 * <p>
 * <p>
 * 示例 2:
 * <p>
 * <p>
 * 输入: [3,2,3,1,2,4,5,5,6], k = 4
 * 输出: 4
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 1 <= k <= nums.length <= 10⁵
 * -10⁴ <= nums[i] <= 10⁴
 * <p>
 * <p>
 * Related Topics数组 | 分治 | 快速选择 | 排序 | 堆（优先队列）
 * <p>
 * 👍 2631, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class KthLargestElementInAnArray {
    public static void main(String[] args) {
        Solution solution = new KthLargestElementInAnArray().new Solution();
        System.out.println(solution);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int findKthLargest(int[] nums, int k) {
            //默认小顶堆
            PriorityQueue<Integer> heap = new PriorityQueue<>();
            for (int num : nums) {
                heap.add(num);
                if (heap.size() > k) {
                    heap.poll();
                }
            }
            return heap.poll();
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}