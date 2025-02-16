package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;

/**
 * 给你一个长度为 n 的整数数组 nums 和 一个目标值 target。请你从 nums 中选出三个整数，使它们的和与 target 最接近。
 * <p>
 * 返回这三个数的和。
 * <p>
 * 假定每组输入只存在恰好一个解。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：nums = [-1,2,1,-4], target = 1
 * 输出：2
 * 解释：与 target 最接近的和是 2 (-1 + 2 + 1 = 2)。
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：nums = [0,0,0], target = 1
 * 输出：0
 * 解释：与 target 最接近的和是 0（0 + 0 + 0 = 0）。
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 3 <= nums.length <= 1000
 * -1000 <= nums[i] <= 1000
 * -10⁴ <= target <= 10⁴
 * <p>
 * <p>
 * Related Topics数组 | 双指针 | 排序
 * <p>
 * 👍 1699, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class ThreeSumClosest {
    public static void main(String[] args) {
        Solution solution = new ThreeSumClosest().new Solution();
        System.out.println(solution);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int threeSumClosest(int[] nums, int target) {
            Arrays.sort(nums);
            int len = nums.length;
            int sum = nums[0] + nums[1] + nums[2];
            int res = sum;
            for (int i = 0; i < len; i++) {
                int l = i + 1, r = len - 1;
                while (l < r) {
                    sum = nums[i] + nums[l] + nums[r];
                    if (Math.abs(sum - target) < Math.abs(res - target)) {
                        res = sum;
                    }
                    if (sum == target) {
                        return sum;
                    } else if (sum > target) {
                        r--;
                    } else {
                        l++;
                    }
                }
            }
            return res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}