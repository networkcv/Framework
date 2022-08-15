//<p>一个机器人位于一个 <code>m x n</code><em>&nbsp;</em>网格的左上角 （起始点在下图中标记为 “Start” ）。</p>
//
//<p>机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为 “Finish” ）。</p>
//
//<p>问总共有多少条不同的路径？</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<p><img src="https://assets.leetcode.com/uploads/2018/10/22/robot_maze.png" /></p>
//
//<pre>
//<strong>输入：</strong>m = 3, n = 7
//<strong>输出：</strong>28</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>m = 3, n = 2
//<strong>输出：</strong>3
//<strong>解释：</strong>
//从左上角开始，总共有 3 条路径可以到达右下角。
//1. 向右 -&gt; 向下 -&gt; 向下
//2. 向下 -&gt; 向下 -&gt; 向右
//3. 向下 -&gt; 向右 -&gt; 向下
//</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>m = 7, n = 3
//<strong>输出：</strong>28
//</pre>
//
//<p><strong>示例 4：</strong></p>
//
//<pre>
//<strong>输入：</strong>m = 3, n = 3
//<strong>输出：</strong>6</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= m, n &lt;= 100</code></li> 
// <li>题目数据保证答案小于等于 <code>2 * 10<sup>9</sup></code></li> 
//</ul>
//
//<p>&nbsp;</p>
//
//<p>
// <meta charset="UTF-8" />注意：本题与主站 62&nbsp;题相同：&nbsp;<a href="https://leetcode-cn.com/problems/unique-paths/">https://leetcode-cn.com/problems/unique-paths/</a></p>
//
//<div><div>Related Topics</div><div><li>数学</li><li>动态规划</li><li>组合数学</li></div></div><br><div><li>👍 24</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;

class TwoAoeFn {
    public static void main(String[] args) {
        Solution solution = new TwoAoeFn().new Solution();
        System.out.println(solution.uniquePaths(19, 13));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        //num(m*n)大小的矩阵，从左上到右下的最大路径条数
        public int uniquePaths1(int m, int n) {
            return helper(m - 1, n - 1);
        }

        //暴力递归 O(2^n)时间复杂度
        //从(0,0)到(x,y)的最大路径条数
        private int helper(int x, int y) {
            if (x == 0 && y == 0) {
                return 1;
            }
            if (x < 0 || y < 0) {
                return 0;
            }
            return helper(x - 1, y) + helper(x, y - 1);
        }

        //dp 自顶向下
        int[][] dp;

        public int uniquePaths2(int m, int n) {
            dp = new int[m][n];
            return dp(m - 1, n - 1);
        }

        private int dp(int x, int y) {
            if (x == 0 && y == 0) {
                return 1;
            }
            if (x < 0 || y < 0) {
                return 0;
            }
            if (dp[x][y] > 0) {
                return dp[x][y];
            }
            dp[x][y] = helper(x - 1, y) + helper(x, y - 1);
            return dp[x][y];
        }

        //dp 自底向上
        public int uniquePaths(int m, int n) {
            int[][] dp = new int[m][n];
            Arrays.fill(dp[0], 1);
            for (int i = 0; i < m; i++) {
                dp[i][0] = 1;
            }
            dp[0][0] = 1;
            for (int i = 1; i < dp.length; i++) {
                for (int j = 1; j < dp[i].length; j++) {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
            return dp[m - 1][n - 1];
        }


    }
//leetcode submit region end(Prohibit modification and deletion)

}