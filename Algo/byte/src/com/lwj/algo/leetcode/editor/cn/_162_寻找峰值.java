package com.lwj.algo.leetcode.editor.cn;

/**
 * 峰值元素是指其值严格大于左右相邻值的元素。
 * <p>
 * 给你一个整数数组 nums，找到峰值元素并返回其索引。数组可能包含多个峰值，在这种情况下，返回 任何一个峰值 所在位置即可。
 * <p>
 * 你可以假设 nums[-1] = nums[n] = -∞ 。
 * <p>
 * 你必须实现时间复杂度为 O(log n) 的算法来解决此问题。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：nums = [1,2,3,1]
 * 输出：2
 * 解释：3 是峰值元素，你的函数应该返回其索引 2。
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：nums = [1,2,1,3,5,6,4]
 * 输出：1 或 5
 * 解释：你的函数可以返回索引 1，其峰值元素为 2；
 *      或者返回索引 5， 其峰值元素为 6。
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 1 <= nums.length <= 1000
 * -2³¹ <= nums[i] <= 2³¹ - 1
 * 对于所有有效的 i 都有 nums[i] != nums[i + 1]
 * <p>
 * <p>
 * Related Topics数组 | 二分查找
 * <p>
 * 👍 1358, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class FindPeakElement {
    public static void main(String[] args) {
        Solution solution = new FindPeakElement().new Solution();
//        System.out.println(solution.findPeakElement(new int[]{1, 2, 3, 1}));
        System.out.println(solution.findPeakElement(new int[]{1}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int findPeakElement(int[] nums) {
            int l = 0, r = nums.length - 2;
            while (l <= r) {
                int mid = (r + l) / 2;
                if (nums[mid] >= nums[mid + 1]) {
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }
            return l == nums.length ? 0 : l;
        }

        public int findPeakElement0(int[] nums) {
            for (int i = 1; i < nums.length - 1; i++) {
                if (nums[i] > nums[i - 1] && nums[i] > nums[i + 1]) {
                    return i;
                }
            }
            return nums[0] > nums[nums.length - 1] ? 0 : nums.length - 1;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}