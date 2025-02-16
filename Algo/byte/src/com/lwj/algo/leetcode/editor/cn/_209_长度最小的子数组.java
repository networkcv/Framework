package com.lwj.algo.leetcode.editor.cn;

/**
 * 给定一个含有 n 个正整数的数组和一个正整数 target 。
 * <p>
 * 找出该数组中满足其总和大于等于 target 的长度最小的 子数组 [numsl, numsl+1, ..., numsr-1, numsr] ，并返回其长度
 * 。如果不存在符合条件的子数组，返回 0 。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：target = 7, nums = [2,3,1,2,4,3]
 * 输出：2
 * 解释：子数组 [4,3] 是该条件下的长度最小的子数组。
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：target = 4, nums = [1,4,4]
 * 输出：1
 * <p>
 * <p>
 * 示例 3：
 * <p>
 * <p>
 * 输入：target = 11, nums = [1,1,1,1,1,1,1,1]
 * 输出：0
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 1 <= target <= 10⁹
 * 1 <= nums.length <= 10⁵
 * 1 <= nums[i] <= 10⁴
 * <p>
 * <p>
 * <p>
 * <p>
 * 进阶：
 * <p>
 * <p>
 * 如果你已经实现 O(n) 时间复杂度的解法, 请尝试设计一个 O(n log(n)) 时间复杂度的解法。
 * <p>
 * <p>
 * Related Topics数组 | 二分查找 | 前缀和 | 滑动窗口
 * <p>
 * 👍 2361, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class MinimumSizeSubarraySum {
    public static void main(String[] args) {
        Solution solution = new MinimumSizeSubarraySum().new Solution();
        System.out.println(solution.minSubArrayLen(15, new int[]{5, 1, 3, 5, 10, 7, 4, 9, 2, 8}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        //滑动窗口，快慢指针，先用快指针向右遍历到达i位置满足target条件的位置，然后慢指针向右遍历到达j位置不满足target时，
        // i-j+1就是以i为右端点下的最长子数组长度
        public int minSubArrayLen(int target, int[] nums) {
            int q = 0, s = 0;
            int len = nums.length;
            int res = Integer.MAX_VALUE;
            int sum = 0;
            while (q < len) {
                sum += nums[q];
                //[2,3,1] 6
                while (sum >= target) {
                    res = Math.min(res, q - s + 1);
                    sum -= nums[s++];
                }
                q++;
            }
            return res == Integer.MAX_VALUE ? 0 : res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}