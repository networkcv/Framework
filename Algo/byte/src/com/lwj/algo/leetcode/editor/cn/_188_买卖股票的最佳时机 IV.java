package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;

//<p>给你一个整数数组&nbsp;<code>prices</code> 和一个整数 <code>k</code> ，其中 <code>prices[i]</code> 是某支给定的股票在第 <code>i</code><em> </em>天的价格。</p>
//
//<p>设计一个算法来计算你所能获取的最大利润。你最多可以完成 <code>k</code> 笔交易。也就是说，你最多可以买 <code>k</code> 次，卖 <code>k</code> 次。</p>
//
//<p><strong>注意：</strong>你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。</p>
//
//<p>&nbsp;</p>
//
//<p><strong class="example">示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>k = 2, prices = [2,4,1]
//<strong>输出：</strong>2
//<strong>解释：</strong>在第 1 天 (股票价格 = 2) 的时候买入，在第 2 天 (股票价格 = 4) 的时候卖出，这笔交易所能获得利润 = 4-2 = 2 。</pre>
//
//<p><strong class="example">示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>k = 2, prices = [3,2,6,5,0,3]
//<strong>输出：</strong>7
//<strong>解释：</strong>在第 2 天 (股票价格 = 2) 的时候买入，在第 3 天 (股票价格 = 6) 的时候卖出, 这笔交易所能获得利润 = 6-2 = 4 。
//     随后，在第 5 天 (股票价格 = 0) 的时候买入，在第 6 天 (股票价格 = 3) 的时候卖出, 这笔交易所能获得利润 = 3-0 = 3 。</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= k &lt;= 100</code></li> 
// <li><code>1 &lt;= prices.length &lt;= 1000</code></li> 
// <li><code>0 &lt;= prices[i] &lt;= 1000</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数组</li><li>动态规划</li></div></div><br><div><li>👍 1249</li><li>👎 0</li></div>
class BestTimeToBuyAndSellStockIv {
    public static void main(String[] args) {
        Solution solution = new BestTimeToBuyAndSellStockIv().new Solution();
//        System.out.println(solution.maxProfit(2, new int[]{3, 2, 6, 5, 0, 3}));
//        System.out.println(solution.maxProfit(2, new int[]{3, 3, 5, 0, 0, 3, 1, 4}));
        System.out.println(solution.maxProfit(2, new int[]{2, 4, 1}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        int[] prices;
        int[][][] cache;

        //最大利润一定是在卖出的时候，买入的话利润会减少，所以只需考虑最多卖出k次的情况，不用考虑最多买入k次
        public int maxProfit(int k, int[] prices) {
            this.prices = prices;
            cache = new int[prices.length + 1][2][k + 1];
            for (int[][] ints : cache) {
                for (int[] ints2 : ints) {
                    Arrays.fill(ints2, Integer.MIN_VALUE);
                }
            }
            return dfs(prices.length - 1, false, k);
        }

        /**
         * 回溯版-记忆化搜索
         * 第i天结束时，hold股票的最大收益情况
         *
         * @param i    当前天数
         * @param hold 持有股票情况
         * @param k    剩余卖出次数
         */
        public int dfs(int i, boolean hold, int k) {
            if (k < 0) return Integer.MIN_VALUE;
            if (i < 0) {
                return hold ? Integer.MIN_VALUE : 0;
            }
            if (cache[i][hold ? 1 : 0][k] != Integer.MIN_VALUE) return cache[i][hold ? 1 : 0][k];
            if (hold) {
                return cache[i][1][k] = Math.max(dfs(i - 1, true, k), dfs(i - 1, false, k) - prices[i]);
            }
            return cache[i][0][k] = Math.max(dfs(i - 1, false, k), dfs(i - 1, true, k - 1) + prices[i]);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}