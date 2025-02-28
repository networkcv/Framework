package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;

//<p>给你一个非负整数数组 <code>nums</code> 和一个整数 <code>target</code> 。</p>
//
//<p>向数组中的每个整数前添加&nbsp;<code>'+'</code> 或 <code>'-'</code> ，然后串联起所有整数，可以构造一个 <strong>表达式</strong> ：</p>
//
//<ul> 
// <li>例如，<code>nums = [2, 1]</code> ，可以在 <code>2</code> 之前添加 <code>'+'</code> ，在 <code>1</code> 之前添加 <code>'-'</code> ，然后串联起来得到表达式 <code>"+2-1"</code> 。</li> 
//</ul>
//
//<p>返回可以通过上述方法构造的、运算结果等于 <code>target</code> 的不同 <strong>表达式</strong> 的数目。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [1,1,1,1,1], target = 3
//<strong>输出：</strong>5430143
//<strong>解释：</strong>一共有 5 种方法让最终目标和为 3 。
//-1 + 1 + 1 + 1 + 1 = 3
//+1 - 1 + 1 + 1 + 1 = 3
//+1 + 1 - 1 + 1 + 1 = 3
//+1 + 1 + 1 - 1 + 1 = 3
//+1 + 1 + 1 + 1 - 1 = 3
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [1], target = 1
//<strong>输出：</strong>1
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= nums.length &lt;= 20</code></li> 
// <li><code>0 &lt;= nums[i] &lt;= 1000</code></li> 
// <li><code>0 &lt;= sum(nums[i]) &lt;= 1000</code></li> 
// <li><code>-1000 &lt;= target &lt;= 1000</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数组</li><li>动态规划</li><li>回溯</li></div></div><br><div><li>👍 2086</li><li>👎 0</li></div>
class TargetSum {
    public static void main(String[] args) {
        Solution solution = new TargetSum().new Solution();
//        System.out.println(solution.findTargetSumWays(new int[]{1, 1, 1, 1, 1}, 3));
        System.out.println(solution.findTargetSumWays(new int[]{1, 2, 3, 4, 5}, 7));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {


        public int findTargetSumWays(int[] nums, int target) {
            Integer sum = Arrays.stream(nums).boxed().reduce(Integer::sum).orElse(0);
            target += sum;
            if (target < 0 || target % 2 == 1) {
                return 0;
            }
//            return dfs0(nums, nums.length - 1, target / 2);
//            return dfs1(nums, nums.length - 1, target / 2);
            return dp(nums, target / 2);
//            return dp1(nums, target / 2);
//            return dp2(nums, target / 2);
        }

        //递推版-空间优化 1个一维数组
        public int dp2(int[] nums, int target) {
            int s = 0;
            for (int x : nums) {
                s += x;
            }
            s -= Math.abs(target);
            if (s < 0 || s % 2 == 1) {
                return 0;
            }
            int m = s / 2; // 背包容量

            int[] f = new int[m + 1];
            f[0] = 1;
            for (int x : nums) {
                for (int c = m; c >= x; c--) {
                    f[c] = f[c] + f[c - x];
                }
            }
            return f[m];
        }

        //递推版-空间优化 只有2行的二维数组
        public int dp1(int[] nums, int target) {
            int[][] dp = new int[2][target + 1];
            dp[0][0] = 1;
            for (int i = 0; i < nums.length; i++) {
                for (int j = 0; j <= target; j++) {
                    if (nums[i] > j) {
                        //不选
                        dp[(i + 1) % 2][j] = dp[i % 2][j];
                    } else {
                        //不选+选
                        dp[(i + 1) % 2][j] = dp[i % 2][j] + dp[i % 2][j - nums[i]];
                    }
                }
            }
            return dp[nums.length % 2][target];
        }

        // 递推版
        public int dp(int[] nums, int target) {
            //dp[i][j]表示从右往左遍历至nums[i-1]，当target等于j时的满足方案个数
            //dp[0][0]是边界条件，
            //target+1是因为下标0为边界条件，最终要返回满足target,所以二维数组的长度是[target+1]
            int[][] dp = new int[nums.length + 1][target + 1];
            dp[0][0] = 1;
            for (int i = 0; i < nums.length; i++) {
                for (int j = 0; j <= target; j++) {
                    if (nums[i] > j) {
                        //不选
                        dp[i + 1][j] = dp[i][j];
                    } else {
                        //不选+选
                        dp[i + 1][j] = dp[i][j] + dp[i][j - nums[i]];
                    }
                }
            }
            return dp[nums.length][target];
        }

        /**
         * 回溯版-暴力回溯-剪枝优化
         * <p>
         * 从nums中cur前的数字中选一部分数等于target，返回多少种选法
         *
         * @param cur    当前遍历值
         * @param target target
         * @return 选择种类数
         */
        public int dfs1(int[] nums, int cur, int target) {
            if (cur < 0) {
                return target == 0 ? 1 : 0;
            }
            //如果当前值大于target，选了target该分支下的target一定小于0，避免无效运算所以这里不选nums[cur]的值
            if (nums[cur] > target) {
                return dfs1(nums, cur - 1, target);
            }
            return dfs1(nums, cur - 1, target) + dfs1(nums, cur - 1, target - nums[cur]);
        }

        /**
         * 回溯版-暴力回溯
         * <p>
         * 从nums中cur前的数字中选一部分数等于target，返回多少种选法
         *
         * @param cur    当前遍历值
         * @param target target
         * @return 选择种类数
         */
        public int dfs0(int[] nums, int cur, int target) {
            if (cur < 0) {
                return target == 0 ? 1 : 0;
            }
            return dfs0(nums, cur - 1, target) + dfs0(nums, cur - 1, target - nums[cur]);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}