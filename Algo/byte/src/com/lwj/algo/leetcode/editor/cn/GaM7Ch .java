//<p>给定不同面额的硬币 <code>coins</code> 和一个总金额 <code>amount</code>。编写一个函数来计算可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回&nbsp;<code>-1</code>。</p>
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
//<p><strong>示例 4：</strong></p>
//
//<pre>
//<strong>输入：</strong>coins = [1], amount = 1
//<strong>输出：</strong>1
//</pre>
//
//<p><strong>示例 5：</strong></p>
//
//<pre>
//<strong>输入：</strong>coins = [1], amount = 2
//<strong>输出：</strong>2
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
//<p>&nbsp;</p>
//
//<p>
// <meta charset="UTF-8" />注意：本题与主站 322&nbsp;题相同：&nbsp;<a href="https://leetcode-cn.com/problems/coin-change/">https://leetcode-cn.com/problems/coin-change/</a></p>
//
//<div><div>Related Topics</div><div><li>广度优先搜索</li><li>数组</li><li>动态规划</li></div></div><br><div><li>👍 46</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;

class GaM7Ch {
    public static void main(String[] args) {
        Solution solution = new GaM7Ch().new Solution();
        System.out.println(solution.coinChange(new int[]{1, 2, 5}, 100));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        //状态： amount
        //选择： coins
        //函数定义：凑齐amount金额需要coinChange(amount)枚硬币
        //暴力递归
        public int coinChange1(int[] coins, int amount) {
            if (amount == 0) {
                return 0;
            }
            if (amount < 0) {
                return -1;
            }
            int res = Integer.MAX_VALUE;
            for (int coin : coins) {
                int num = coinChange1(coins, amount - coin);
                if (num < 0)
                    continue;
                res = Math.min(res, num + 1);
            }
            return res == Integer.MAX_VALUE ? -1 : res;
        }

        int[] dp;
        int flag;

        //dp 自顶向下
        public int coinChange2(int[] coins, int amount) {
            dp = new int[amount + 1];
            flag = amount + 1;
            Arrays.fill(dp, flag);

            return helper(coins, amount);
        }

        private int helper(int[] coins, int amount) {
            if (amount == 0) {
                return 0;
            }
            if (amount < 0) {
                return -1;
            }
            if (dp[amount] != flag) {
                return dp[amount];
            }
            for (int coin : coins) {
                int num = helper(coins, amount - coin);
                if (num < 0)
                    continue;
                dp[amount] = Math.min(dp[amount], num + 1);
            }
            return dp[amount] == flag ? -1 : dp[amount];
        }


        //dp 自底向上
        public int coinChange(int[] coins, int amount) {
            if (amount == 0) {
                return 0;
            }
            if (amount < 0) {
                return -1;
            }
            int[] dp = new int[amount + 1];
            Arrays.fill(dp, amount + 1);
            dp[0] = 0;
            for (int i = 0; i < dp.length; i++) {
                for (int coin : coins) {
                    if (i < coin)
                        continue;
                    dp[i] = Math.min(dp[i], dp[i - coin] + 1);
                }
            }
            return dp[amount] == amount + 1 ? -1 : dp[amount];
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}