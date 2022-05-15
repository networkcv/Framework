//输入一个整型数组，数组中的一个或连续多个整数组成一个子数组。求所有子数组的和的最大值。 
//
// 要求时间复杂度为O(n)。 
//
// 
//
// 示例1: 
//
// 输入: nums = [-2,1,-3,4,-1,2,1,-5,4]
//输出: 6
//解释: 连续子数组 [4,-1,2,1] 的和最大，为 6。 
//
// 
//
// 提示： 
//
// 
// 1 <= arr.length <= 10^5 
// -100 <= arr[i] <= 100 
// 
//
// 注意：本题与主站 53 题相同：https://leetcode-cn.com/problems/maximum-subarray/ 
//
// 
// Related Topics 数组 分治 动态规划 👍 531 👎 0


package com.lwj.algo.leetcode.editor.cn;

class LianXuZiShuZuDeZuiDaHeLcof {
    public static void main(String[] args) {
        Solution solution = new LianXuZiShuZuDeZuiDaHeLcof().new Solution();

//        System.out.println(solution.maxSubArray(new int[]{5, 4, -1, 7, 8}));
//        System.out.println(solution.maxSubArray(new int[]{5}));
        System.out.println(solution.maxSubArray(new int[]{-2,1,-3,4,-1,2,1,-5,4}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int maxSubArray(int[] nums) {
//            return fun1(nums);
            return fun2(nums);
        }

        private int fun2(int[] nums) {
            int max = nums[0];
            int sum = 0;
            for (int num : nums) {
                if (sum > 0) {
                    sum += num;
                } else {
                    sum = num;
                }
                max=Math.max(max,sum);
            }
            return max;
        }

        private int fun1(int[] nums) {
            int[] dp = new int[nums.length];
            dp[0] = nums[0];
            int max = dp[0];

            for (int n = 1; n < dp.length; n++) {
                dp[n] = Math.max(dp[n - 1] + nums[n], nums[n]);
                if (dp[n] > max) {
                    max = dp[n];
                }
            }
            return max;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}