//假设你正在爬楼梯。需要 n 阶你才能到达楼顶。 
//
// 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？ 
//
// 
//
// 示例 1： 
//
// 
//输入：n = 2
//输出：2
//解释：有两种方法可以爬到楼顶。
//1. 1 阶 + 1 阶
//2. 2 阶 
//
// 示例 2： 
//
// 
//输入：n = 3
//输出：3
//解释：有三种方法可以爬到楼顶。
//1. 1 阶 + 1 阶 + 1 阶
//2. 1 阶 + 2 阶
//3. 2 阶 + 1 阶
// 
//
// 
//
// 提示： 
//
// 
// 1 <= n <= 45 
// 
// Related Topics 记忆化搜索 数学 动态规划 👍 2425 👎 0


package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;

class ClimbingStairs {
    public static void main(String[] args) {
        Solution solution = new ClimbingStairs().new Solution();
        System.out.println(solution.climbStairs(45));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        //暴力递归
        public int climbStairs1(int n) {
            if (n == 1) {
                return 1;
            }
            if (n == 2) {
                return 2;
            }
            return climbStairs1(n - 1) + climbStairs1(n - 2);
        }


        //dp 自顶向下
        int[] dp;
        int flag = -1;

        public int climbStairs2(int n) {
            dp = new int[n + 1];
            Arrays.fill(dp, flag);
            return helper(n);
        }

        private int helper(int n) {
            if (n == 1) {
                return 1;
            }
            if (n == 2) {
                return 2;
            }
            if (dp[n] != flag) {
                return dp[n];
            }
            dp[n] = helper(n - 1) + helper(n - 2);
            return dp[n];
        }

        //dp 自底向上
        public int climbStairs(int n) {
            if (n < 3) {
                return n;
            }
            int[] dp = new int[n + 1];
            Arrays.fill(dp, 0);
            dp[1] = 1;
            dp[2] = 2;
            for (int i = 3; i < dp.length; i++) {
                dp[i] = dp[i - 1] + dp[i - 2];
            }
            return dp[n];
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}