package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;

//<p>给定一个整数数组
// <meta charset="UTF-8" /><code>prices</code>，其中第&nbsp;<em>&nbsp;</em><code>prices[i]</code>&nbsp;表示第&nbsp;<code><em>i</em></code>&nbsp;天的股票价格 。​</p>
//
//<p>设计一个算法计算出最大利润。在满足以下约束条件下，你可以尽可能地完成更多的交易（多次买卖一支股票）:</p>
//
//<ul> 
// <li>卖出股票后，你无法在第二天买入股票 (即冷冻期为 1 天)。</li> 
//</ul>
//
//<p><strong>注意：</strong>你不能同时参与多笔交易（你必须在再次购买前出售掉之前的股票）。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1:</strong></p>
//
//<pre>
//<strong>输入:</strong> prices = [1,2,3,0,2]
//<strong>输出: </strong>3 
//<strong>解释:</strong> 对应的交易状态为: [买入, 卖出, 冷冻期, 买入, 卖出]</pre>
//
//<p><strong>示例 2:</strong></p>
//
//<pre>
//<strong>输入:</strong> prices = [1]
//<strong>输出:</strong> 0
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= prices.length &lt;= 5000</code></li> 
// <li><code>0 &lt;= prices[i] &lt;= 1000</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数组</li><li>动态规划</li></div></div><br><div><li>👍 1821</li><li>👎 0</li></div>
class BestTimeToBuyAndSellStockWithCooldown {
    public static void main(String[] args) {
        Solution solution = new BestTimeToBuyAndSellStockWithCooldown().new Solution();
        System.out.println(solution);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        int[] prices;
        int[][] cache;

        public int maxProfit(int[] prices) {
            this.prices = prices;
            cache = new int[prices.length][2];
            for (int[] ints : cache) {
                Arrays.fill(ints, Integer.MIN_VALUE);
            }
//            return dfs0(prices.length - 1, false);
//            return dp0();
            return dp1();
        }

        //递推版-空间优化
        public int dp1() {
            int p0 = 0, f0 = 0, f1 = Integer.MIN_VALUE;
            for (int price : prices) {
                int tmp_f1 = Math.max(f1, p0 - price);
                p0 = f0;
                f0 = Math.max(f0, f1 + price);
                f1 = tmp_f1;
            }
            return f0;
        }

        //递推版
        public int dp0() {
            //d[i][1],表示第i-2天结束时，持有股票的最大收益
            int[][] dp = new int[prices.length + 2][2];
            //将i=0带入计算一下，用dp[2][1]则表示第0天结束时持有股票的最大收益，收益是负的prices[0]，所以dp[1][1]应该为MIN_VALUE
            dp[1][1] = Integer.MIN_VALUE;
            for (int i = 0; i < prices.length; i++) {
                dp[i + 2][1] = Math.max(dp[i + 1][1], dp[i][0] - prices[i]);
                dp[i + 2][0] = Math.max(dp[i + 1][0], dp[i + 1][1] + prices[i]);
            }
            return dp[prices.length + 1][0];
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
            //持有股票，需要考虑冻结期，冷冻期就是卖出后无法在第二天买入，所以当天如果要买股票的话，前提必须是前两天不持有股票
            if (hold) {
                return cache[i][1] = Math.max(dfs0(i - 1, true), dfs0(i - 2, false) - prices[i]);
            }
            //没有股票
            return cache[i][0] = Math.max(dfs0(i - 1, false), dfs0(i - 1, true) + prices[i]);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}