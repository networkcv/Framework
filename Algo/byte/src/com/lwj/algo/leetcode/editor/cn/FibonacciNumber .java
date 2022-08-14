//<p><strong>斐波那契数</strong>&nbsp;（通常用&nbsp;<code>F(n)</code> 表示）形成的序列称为 <strong>斐波那契数列</strong> 。该数列由&nbsp;<code>0</code> 和 <code>1</code> 开始，后面的每一项数字都是前面两项数字的和。也就是：</p>
//
//<pre>
//F(0) = 0，F(1)&nbsp;= 1
//F(n) = F(n - 1) + F(n - 2)，其中 n &gt; 1
//</pre>
//
//<p>给定&nbsp;<code>n</code> ，请计算 <code>F(n)</code> 。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>n = 2
//<strong>输出：</strong>1
//<strong>解释：</strong>F(2) = F(1) + F(0) = 1 + 0 = 1
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>n = 3
//<strong>输出：</strong>2
//<strong>解释：</strong>F(3) = F(2) + F(1) = 1 + 1 = 2
//</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>n = 4
//<strong>输出：</strong>3
//<strong>解释：</strong>F(4) = F(3) + F(2) = 2 + 1 = 3
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>0 &lt;= n &lt;= 30</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>递归</li><li>记忆化搜索</li><li>数学</li><li>动态规划</li></div></div><br><div><li>👍 515</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

class FibonacciNumber {
    public static void main(String[] args) {
        Solution solution = new FibonacciNumber().new Solution();
        System.out.println(solution.fib(0));
//        System.out.println(solution.fib(4));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {


        //dp 自底向上底基础上优化空间复杂度
        public int fib(int n) {
            if (n == 0 || n == 1) {
                return n;
            }
            int pre = 0, cur = 1;
            for (int i = 2; i <= n; i++) {
                int sum = pre + cur;
                pre = cur;
                cur = sum;
            }
            return cur;
        }

        //dp 自底向上
        public int fib3(int n) {
            if (n == 0) {
                return 0;
            }
            int[] dp = new int[n + 1];

            dp[1] = 1;
            for (int i = 2; i <= n; i++) {
                dp[i] = dp[i - 1] + dp[i - 2];
            }
            return dp[n];

        }

        int[] dp;

        //dp方式 自顶向下 减少重复计算量
        public int fib2(int n) {
            dp = new int[n + 1];
            return helper(n);

        }

        private int helper(int n) {
            if (n < 2) {
                return n;
            }
            if (dp[n] != 0) {
                return dp[n];
            }
            dp[n] = helper(n - 1) + helper(n - 2);
            return dp[n];
        }

        //递归法
        public int fib1(int n) {
            if (n < 2) {
                return n;
            }
            return fib1(n - 1) + fib1(n - 2);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}