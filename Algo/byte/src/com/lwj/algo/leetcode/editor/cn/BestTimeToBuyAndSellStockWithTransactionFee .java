//<p>给定一个整数数组&nbsp;<code>prices</code>，其中 <code>prices[i]</code>表示第&nbsp;<code>i</code>&nbsp;天的股票价格 ；整数&nbsp;<code>fee</code> 代表了交易股票的手续费用。</p>
//
//<p>你可以无限次地完成交易，但是你每笔交易都需要付手续费。如果你已经购买了一个股票，在卖出它之前你就不能再继续购买股票了。</p>
//
//<p>返回获得利润的最大值。</p>
//
//<p><strong>注意：</strong>这里的一笔交易指买入持有并卖出股票的整个过程，每笔交易你只需要为支付一次手续费。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>prices = [1, 3, 2, 8, 4, 9], fee = 2
//<strong>输出：</strong>8
//<strong>解释：</strong>能够达到的最大利润:  
//在此处买入&nbsp;prices[0] = 1
//在此处卖出 prices[3] = 8
//在此处买入 prices[4] = 4
//在此处卖出 prices[5] = 9
//总利润:&nbsp;((8 - 1) - 2) + ((9 - 4) - 2) = 8</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>prices = [1,3,7,5,10,3], fee = 3
//<strong>输出：</strong>6
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= prices.length &lt;= 5 * 10<sup>4</sup></code></li> 
// <li><code>1 &lt;= prices[i] &lt; 5 * 10<sup>4</sup></code></li> 
// <li><code>0 &lt;= fee &lt; 5 * 10<sup>4</sup></code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>贪心</li><li>数组</li><li>动态规划</li></div></div><br><div><li>👍 777</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

class BestTimeToBuyAndSellStockWithTransactionFee {
    public static void main(String[] args) {
        Solution solution = new BestTimeToBuyAndSellStockWithTransactionFee().new Solution();
        System.out.println(solution.maxProfit(new int[]{1, 3, 2, 8, 4, 9}, 2));
        System.out.println(solution.maxProfit(new int[]{1, 3, 7, 5, 10, 3}, 3));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int maxProfit(int[] prices, int fee) {
            return dp(prices, fee);
        }

        //优化空间复杂度
        //第i天获取的最大利润
        private int dp(int[] prices, int fee) {
            int len = prices.length;
            int preNonHold = 0;
            int preHold = Integer.MIN_VALUE;
            for (int i = 1; i <= len; i++) {
                int curNonHold = Math.max(preHold + prices[i - 1], preNonHold);
                int curHold = Math.max(preHold, preNonHold - prices[i - 1] - fee);
                preHold = curHold;
                preNonHold = curNonHold;
            }
            return preNonHold;
        }

        //第i天获取的最大利润
        private int dp2(int[] prices, int fee) {
            int len = prices.length;
            int[][] dp = new int[len + 1][2];
            //1 代表持有
            dp[0][1] = Integer.MIN_VALUE;
            for (int i = 1; i <= len; i++) {
                dp[i][0] = Math.max(dp[i - 1][1] + prices[i - 1], dp[i - 1][0]);
                dp[i][1] = Math.max(dp[i - 1][1], dp[i - 1][0] - prices[i - 1] - fee);
            }
            return dp[len][0];
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}