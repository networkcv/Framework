package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;

//<p>给定一个数组，它的第<em> </em><code>i</code> 个元素是一支给定的股票在第 <code>i</code><em> </em>天的价格。</p>
//
//<p>设计一个算法来计算你所能获取的最大利润。你最多可以完成&nbsp;<strong>两笔&nbsp;</strong>交易。</p>
//
//<p><strong>注意：</strong>你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例&nbsp;1:</strong></p>
//
//<pre>
//<strong>输入：</strong>prices = [3,3,5,0,0,3,1,4]
//<strong>输出：</strong>6
//<strong>解释：</strong>在第 4 天（股票价格 = 0）的时候买入，在第 6 天（股票价格 = 3）的时候卖出，这笔交易所能获得利润 = 3-0 = 3 。
//&nbsp;    随后，在第 7 天（股票价格 = 1）的时候买入，在第 8 天 （股票价格 = 4）的时候卖出，这笔交易所能获得利润 = 4-1 = 3 。</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>prices = [1,2,3,4,5]
//<strong>输出：</strong>4
//<strong>解释：</strong>在第 1 天（股票价格 = 1）的时候买入，在第 5 天 （股票价格 = 5）的时候卖出, 这笔交易所能获得利润 = 5-1 = 4 。 &nbsp; 
//&nbsp;    注意你不能在第 1 天和第 2 天接连购买股票，之后再将它们卖出。 &nbsp; 
//&nbsp;    因为这样属于同时参与了多笔交易，你必须在再次购买前出售掉之前的股票。
//</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>prices = [7,6,4,3,1] 
//<strong>输出：</strong>0 
//<strong>解释：</strong>在这个情况下, 没有交易完成, 所以最大利润为 0。</pre>
//
//<p><strong>示例 4：</strong></p>
//
//<pre>
//<strong>输入：</strong>prices = [1]
//<strong>输出：</strong>0
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;=&nbsp;prices.length &lt;= 10<sup>5</sup></code></li> 
// <li><code>0 &lt;=&nbsp;prices[i] &lt;=&nbsp;10<sup>5</sup></code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数组</li><li>动态规划</li></div></div><br><div><li>👍 1802</li><li>👎 0</li></div>
class BestTimeToBuyAndSellStockIii {
    public static void main(String[] args) {
        Solution solution = new BestTimeToBuyAndSellStockIii().new Solution();
        System.out.println(solution);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        int[] prices;
        int[][][] cache;

        public int maxProfit(int[] prices) {
            return maxProfit0(2, prices);
        }

        public int maxProfit0(int k, int[] prices) {
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