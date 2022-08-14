//<p>给你一个整数数组 <code>coins</code> ，表示不同面额的硬币；以及一个整数 <code>amount</code> ，表示总金额。</p>
//
//<p>计算并返回可以凑成总金额所需的 <strong>最少的硬币个数</strong> 。如果没有任何一种硬币组合能组成总金额，返回&nbsp;<code>-1</code> 。</p>
//
//<p>你可以认为每种硬币的数量是无限的。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例&nbsp;1：</strong></p>
//
//<pre>
//<strong>输入：</strong>coins = <span><code>[1, 2, 5]</code></span>, amount = <span><code>11</code></span>
//<strong>输出：</strong><span><code>3</code></span> 
//<strong>解释：</strong>11 = 5 + 5 + 1</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>coins = <span><code>[2]</code></span>, amount = <span><code>3</code></span>
//<strong>输出：</strong>-1</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>coins = [1], amount = 0
//<strong>输出：</strong>0
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= coins.length &lt;= 12</code></li> 
// <li><code>1 &lt;= coins[i] &lt;= 2<sup>31</sup> - 1</code></li> 
// <li><code>0 &lt;= amount &lt;= 10<sup>4</sup></code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>广度优先搜索</li><li>数组</li><li>动态规划</li></div></div><br><div><li>👍 2091</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;

class CoinChange {
    public static void main(String[] args) {
/*
        明确 base case -> 明确「状态」-> 明确「选择」 -> 定义 dp 数组/函数的含义。

        判断是否存在最优子结构，存在的话先通过状态转移方式写暴力递归，再检查是否存在重叠子问题，存在的话尝试通过备忘录（dp table）来记录重叠子问题的结果，
        可以先通过自顶向下的方式分解问题，再看能不能通过自底向上的方式推导出dp table，最后再看有没有可能优化掉dp table，降低空间复杂度。



        1、确定 base case，这个很简单，显然目标金额 amount 为 0 时算法返回 0，因为不需要任何硬币就已经凑出目标金额了。
        2、确定「状态」，也就是原问题和子问题中会变化的变量。由于硬币数量无限，硬币的面额也是题目给定的，
        只有目标金额会不断地向 base case 靠近，所以唯一的「状态」就是目标金额 amount。
        3、确定「选择」，也就是导致「状态」产生变化的行为。目标金额为什么变化呢，因为你在选择硬币，
        你每选择一枚硬币，就相当于减少了目标金额。所以说所有硬币的面值，就是你的「选择」。
        4、明确 dp 函数/数组的定义。我们这里讲的是自顶向下的解法，所以会有一个递归的 dp 函数，一般来说函数的参数就是状态转移中会变化的量，
        也就是上面说到的「状态」；函数的返回值就是题目要求我们计算的量。就本题来说，状态只有一个，即「目标金额」，题目要求我们计算凑出目标金额所需的最少硬币数量。
*/
        Solution solution = new CoinChange().new Solution();
//        System.out.println(solution.coinChange2(new int[]{1, 2, 5}, 100));
        System.out.println(solution.coinChange(new int[]{2}, 3));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        //暴力递归 要凑出金额amount至少要多少枚硬币  大概率会超时
        public int coinChange1(int[] coins, int amount) {
            //base case
            if (amount == 0) {
                return 0;
            }
            if (amount < 0) {
                return -1;
            }
            int res = Integer.MAX_VALUE;
            for (int coin : coins) {
                int num = coinChange1(coins, amount - coin);
                if (num == -1) {
                    continue;
                }
                res = Math.min(num + 1, res);

            }
            return res == Integer.MAX_VALUE ? -1 : res;
        }

        int[] dp;

        //dp 自顶向下
        public int coinChange2(int[] coins, int amount) {
            dp = new int[amount + 1];
            Arrays.fill(dp, -2);
            return helper(coins, amount);
        }

        public int helper(int[] coins, int amount) {
            //base case
            if (amount == 0) {
                return 0;
            }
            if (amount < 0) {
                return -1;
            }
            if (dp[amount] != -2) {
                return dp[amount];
            }
            int res = Integer.MAX_VALUE;
            for (int coin : coins) {
                int num = helper(coins, amount - coin);
                if (num == -1) {
                    continue;
                }
                res = Math.min(num + 1, res);

            }
            dp[amount] = res == Integer.MAX_VALUE ? -1 : res;
            return dp[amount];
        }

        //函数定义：凑出 amount，至少需要 coinChange(amount) 枚硬币
        //dp 自底向上 通过累加不断从前向后推出 需要的最少硬币数量
        public int coinChange(int[] coins, int amount) {
            if (amount == 0) {
                return 0;
            }
            if (amount < 0) {
                return -1;
            }
            int[] dp = new int[amount + 1];
            //填充特殊值来区别是否处理过,因为比较的是min 所以这里取了和Integer的最大值相同作用的值
            Arrays.fill(dp, amount + 1);
//            Arrays.fill(dp, Integer.MAX_VALUE); wrong 最大值+1 会溢出 变为Integer.MIN_VALUE
//            Arrays.fill(dp, Integer.MAX_VALUE-1); right
            //dp 数组定义：凑出金额i，至少需要 dp[i]枚硬币
            dp[0] = 0;
            //外层遍历所有状态
            for (int i = 0; i < dp.length; i++) {
                //内层遍历所有选择
                for (int coin : coins) {
                    //当状态小于选择时，也就是金额小于当前面值的硬币时，直接continue
                    if (i < coin) {
                        continue;
                    }
                    //当金额 大于等于 当前硬币时 如 1，5，10 中 coin=5  amount=10  肯定是dp[10]小于dp[5]+1
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
            return dp[amount] == amount + 1 ? -1 : dp[amount];
//            return dp[amount] == Integer.MAX_VALUE ? -1 : dp[amount];
//            return dp[amount] == Integer.MAX_VALUE-1 ? -1 : dp[amount];
        }
    }
    //leetcode submit region end(Prohibit modification and deletion)

}