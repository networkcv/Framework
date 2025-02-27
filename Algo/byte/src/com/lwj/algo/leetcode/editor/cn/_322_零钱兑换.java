package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;

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
//<div><div>Related Topics</div><div><li>广度优先搜索</li><li>数组</li><li>动态规划</li></div></div><br><div><li>👍 2981</li><li>👎 0</li></div>
class CoinChange {
    public static void main(String[] args) {
        Solution solution = new CoinChange().new Solution();
//        System.out.println(solution.coinChange(new int[]{2}, 3));
        System.out.println(solution.coinChange(new int[]{411, 412, 413, 414, 415, 416, 417, 418, 419, 420, 421, 422}, 9864));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {


        int[] coins;
        int[][] cache;

        public int coinChange(int[] coins, int amount) {
            this.coins = coins;
//            int minNum = dfs0(coins.length - 1, amount);
            this.cache = new int[coins.length][amount + 1];
            for (int[] ints : cache) {
                Arrays.fill(ints, -1);
            }
            int minNum = dfs1(coins.length - 1, amount);

            return minNum == Integer.MAX_VALUE / 2 ? -1 : minNum;
        }


        //回溯版-记忆化搜索
        public int dfs1(int cur, int amount) {
            //边界条件
            if (cur < 0) {
                return amount == 0 ? 0 : Integer.MAX_VALUE / 2;
            }
            if (cache[cur][amount] != -1) {
                return cache[cur][amount];
            }
            //如果当前硬币金额大于amount，只能不选
            if (coins[cur] > amount) {
                return dfs1(cur - 1, amount);
            }
            //不选 或者 选
            return cache[cur][amount] = Math.min(dfs1(cur - 1, amount), dfs1(cur, amount - coins[cur]) + 1);
        }

        // 回溯版-暴力解法
        public int dfs0(int cur, int amount) {
            //边界条件
            if (cur < 0) {
                return amount == 0 ? 0 : Integer.MAX_VALUE / 2;
            }
            //如果当前硬币金额大于amount，只能不选
            if (coins[cur] > amount) {
                return dfs0(cur - 1, amount);
            }
            //不选 或者 选
            return Math.min(dfs0(cur - 1, amount), dfs0(cur, amount - coins[cur]) + 1);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}