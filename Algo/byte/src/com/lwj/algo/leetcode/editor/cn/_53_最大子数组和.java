package com.lwj.algo.leetcode.editor.cn;

/**
 * 给你一个整数数组 nums ，请你找出一个具有最大和的连续子数组（子数组最少包含一个元素），返回其最大和。
 * <p>
 * 子数组 是数组中的一个连续部分。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：nums = [-2,1,-3,4,-1,2,1,-5,4]
 * 输出：6
 * 解释：连续子数组 [4,-1,2,1] 的和最大，为 6 。
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：nums = [1]
 * 输出：1
 * <p>
 * <p>
 * 示例 3：
 * <p>
 * <p>
 * 输入：nums = [5,4,-1,7,8]
 * 输出：23
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 1 <= nums.length <= 10⁵
 * -10⁴ <= nums[i] <= 10⁴
 * <p>
 * <p>
 * <p>
 * <p>
 * 进阶：如果你已经实现复杂度为 O(n) 的解法，尝试使用更为精妙的 分治法 求解。
 * <p>
 * Related Topics数组 | 分治 | 动态规划
 * <p>
 * 👍 6907, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class MaximumSubarray {
    public static void main(String[] args) {
        Solution solution = new MaximumSubarray().new Solution();
        System.out.println(solution.maxSubArray(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4}));
//        System.out.println(solution.maxSubArray(new int[]{1}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public int maxSubArray(int[] nums) {
            //dp[i]表示以nums[i]结尾的连续子数组的最大和
            //整个数组的最大和结果不一定是在最后一个元素，也可能是在中间的区间
            int res = nums[0];
            int[] dp = new int[nums.length];
            dp[0] = nums[0];
            for (int i = 1; i < nums.length; i++) {
                //这里实际每次只到了前一个元素，可以通过复用同一个变量来降低空间复杂度，见maxSubArray2
                dp[i] = Math.max(dp[i - 1] + nums[i], nums[i]);
//                if (dp[i - 1] > 0) {
//                    dp[i] = dp[i - 1] + nums[i];
//                } else {
//                    dp[i] = nums[i];
//                }
                res = Math.max(res, dp[i]);
            }
            return res;
        }

        public int maxSubArray1(int[] nums) {
            int len = nums.length;
            // dp[i] 表示：以 nums[i] 结尾的连续子数组的最大和
            int[] dp = new int[len];
            dp[0] = nums[0];

            for (int i = 1; i < len; i++) {
                if (dp[i - 1] > 0) {
                    dp[i] = dp[i - 1] + nums[i];
                } else {
                    dp[i] = nums[i];
                }
            }

            // 也可以在上面遍历的同时求出 res 的最大值，这里我们为了语义清晰分开写，大家可以自行选择
            int res = dp[0];
            for (int i = 1; i < len; i++) {
                res = Math.max(res, dp[i]);
            }
            return res;
        }


        public int maxSubArray2(int[] nums) {
            int dpPre = 0;
            int res = nums[0];
            for (int num : nums) {
                dpPre = Math.max(dpPre, dpPre + num);
                res = Math.max(res, dpPre);
            }
            return res;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)

}