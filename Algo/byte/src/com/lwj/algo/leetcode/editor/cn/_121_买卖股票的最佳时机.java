package com.lwj.algo.leetcode.editor.cn;

//<p>给定一个数组 <code>prices</code> ，它的第&nbsp;<code>i</code> 个元素&nbsp;<code>prices[i]</code> 表示一支给定股票第 <code>i</code> 天的价格。</p>
//
//<p>你只能选择 <strong>某一天</strong> 买入这只股票，并选择在 <strong>未来的某一个不同的日子</strong> 卖出该股票。设计一个算法来计算你所能获取的最大利润。</p>
//
//<p>返回你可以从这笔交易中获取的最大利润。如果你不能获取任何利润，返回 <code>0</code> 。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>[7,1,5,3,6,4]
//<strong>输出：</strong>5
//<strong>解释：</strong>在第 2 天（股票价格 = 1）的时候买入，在第 5 天（股票价格 = 6）的时候卖出，最大利润 = 6-1 = 5 。
//     注意利润不能是 7-1 = 6, 因为卖出价格需要大于买入价格；同时，你不能在买入前卖出股票。
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>prices = [7,6,4,3,1]
//<strong>输出：</strong>0
//<strong>解释：</strong>在这种情况下, 没有交易完成, 所以最大利润为 0。
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= prices.length &lt;= 10<sup>5</sup></code></li> 
// <li><code>0 &lt;= prices[i] &lt;= 10<sup>4</sup></code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数组</li><li>动态规划</li></div></div><br><div><li>👍 3722</li><li>👎 0</li></div>
class BestTimeToBuyAndSellStock {
    public static void main(String[] args) {
        Solution solution = new BestTimeToBuyAndSellStock().new Solution();
        System.out.println(solution);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        //枚举保存最值
        public int maxProfit(int[] prices) {
            int res = 0;
            int min = prices[0];
            for (int price : prices) {
                min = Math.min(min, price);
                res = Math.max(res, price - min);
            }
            return res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}