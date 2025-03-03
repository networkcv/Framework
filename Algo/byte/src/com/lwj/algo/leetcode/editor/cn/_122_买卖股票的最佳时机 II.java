package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;

//<p>给你一个整数数组 <code>prices</code> ，其中&nbsp;<code>prices[i]</code> 表示某支股票第 <code>i</code> 天的价格。</p>
//
//<p>在每一天，你可以决定是否购买和/或出售股票。你在任何时候&nbsp;<strong>最多</strong>&nbsp;只能持有 <strong>一股</strong> 股票。你也可以先购买，然后在 <strong>同一天</strong> 出售。</p>
//
//<p>返回 <em>你能获得的 <strong>最大</strong> 利润</em>&nbsp;。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>prices = [7,1,5,3,6,4]
//<strong>输出：</strong>7
//<strong>解释：</strong>在第 2 天（股票价格 = 1）的时候买入，在第 3 天（股票价格 = 5）的时候卖出, 这笔交易所能获得利润 = 5 - 1 = 4。
//随后，在第 4 天（股票价格 = 3）的时候买入，在第 5 天（股票价格 = 6）的时候卖出, 这笔交易所能获得利润 = 6 - 3 = 3。
//最大总利润为 4 + 3 = 7 。</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>prices = [1,2,3,4,5]
//<strong>输出：</strong>4
//<strong>解释：</strong>在第 1 天（股票价格 = 1）的时候买入，在第 5 天 （股票价格 = 5）的时候卖出, 这笔交易所能获得利润 = 5 - 1 = 4。
//最大总利润为 4 。</pre>
//
//<p><strong>示例&nbsp;3：</strong></p>
//
//<pre>
//<strong>输入：</strong>prices = [7,6,4,3,1]
//<strong>输出：</strong>0
//<strong>解释：</strong>在这种情况下, 交易无法获得正利润，所以不参与交易可以获得最大利润，最大利润为 0。</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= prices.length &lt;= 3 * 10<sup>4</sup></code></li> 
// <li><code>0 &lt;= prices[i] &lt;= 10<sup>4</sup></code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>贪心</li><li>数组</li><li>动态规划</li></div></div><br><div><li>👍 2663</li><li>👎 0</li></div>
class BestTimeToBuyAndSellStockIi {
    public static void main(String[] args) {
        Solution solution = new BestTimeToBuyAndSellStockIi().new Solution();
//        System.out.println(solution.maxProfit(new int[]{7, 1, 5, 3, 6, 4}));
        System.out.println(solution.maxProfit(new int[]{1, 2, 3, 4, 5}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {


        int[] prices;
        int[][] cache;

        int[][] dp;

        //递推版-空间优化
        public int maxProfit(int[] prices) {
            //边界值，f0表示当天结束时不持有股票的最收益，
            //f1表示当天结束时持有股票的最大收益，持有的时候可能是买入，所以收益可能是负数，所以初始化的默认值为MIN_VALUE
            int f0 = 0;
            int f1 = Integer.MIN_VALUE;
            for (int price : prices) {
                int tmpF1 = Math.max(f1, f0 - price);
                f0 = Math.max(f0, f1 + price);
                f1 = tmpF1;
            }
            //最后一天如果买入股票的话，是没有机会卖出的，所以f1一是小于f0
            return f0;
        }

        //递推版
        public int maxProfit1(int[] prices) {
            dp = new int[prices.length + 1][2];
            dp[0][1] = Integer.MIN_VALUE;
            for (int i = 0; i < prices.length; i++) {
                dp[i + 1][1] = Math.max(dp[i][1], dp[i][0] - prices[i]);
                dp[i + 1][0] = Math.max(dp[i][0], dp[i][1] + prices[i]);
            }
            return dp[prices.length][0];
        }

        public int maxProfit0(int[] prices) {
            this.prices = prices;
            cache = new int[prices.length][2];
            for (int[] ints : cache) {
                Arrays.fill(ints, Integer.MIN_VALUE);
            }
            return dfs0(prices.length - 1, false);
        }

        //回溯版-记忆化搜索
        //dfs定义第i天结束时,hold股票时的最大利润，i是索引下标
        public int dfs0(int i, boolean hold) {
            if (i < 0) {
                if (hold) {
                    //这里是最小值的原因是，假设第一天买入了股票，[7,1,5,3,6,4]那么当天的利润应该是-7,如果这里也返回0，则当天的利润就是0了
                    return Integer.MIN_VALUE;
                } else {
                    return 0;
                }
            }
            if (cache[i][hold ? 1 : 0] != Integer.MIN_VALUE) {
                return cache[i][hold ? 1 : 0];
            }
            //持有股票
            if (hold) {
                return cache[i][1] = Math.max(dfs0(i - 1, true), dfs0(i - 1, false) - prices[i]);
            }
            //没有股票
            return cache[i][0] = Math.max(dfs0(i - 1, false), dfs0(i - 1, true) + prices[i]);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}