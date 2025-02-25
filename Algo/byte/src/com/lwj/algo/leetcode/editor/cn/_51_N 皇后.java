package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

//<p>按照国际象棋的规则，皇后可以攻击与之处在同一行或同一列或同一斜线上的棋子。</p>
//
//<p><strong>n&nbsp;皇后问题</strong> 研究的是如何将 <code>n</code>&nbsp;个皇后放置在 <code>n×n</code> 的棋盘上，并且使皇后彼此之间不能相互攻击。</p>
//
//<p>给你一个整数 <code>n</code> ，返回所有不同的&nbsp;<strong>n<em>&nbsp;</em>皇后问题</strong> 的解决方案。</p>
//
//<div class="original__bRMd"> 
// <div> 
//  <p>每一种解法包含一个不同的&nbsp;<strong>n 皇后问题</strong> 的棋子放置方案，该方案中 <code>'Q'</code> 和 <code>'.'</code> 分别代表了皇后和空位。</p> 
// </div>
//</div>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p> 
//<img alt="" src="https://assets.leetcode.com/uploads/2020/11/13/queens.jpg" style="width: 600px; height: 268px;" /> 
//<pre>
//<strong>输入：</strong>n = 4
//<strong>输出：</strong>[[".Q..","...Q","Q...","..Q."],["..Q.","Q...","...Q",".Q.."]]
//<strong>解释：</strong>如上图所示，4 皇后问题存在两个不同的解法。
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>n = 1
//<strong>输出：</strong>[["Q"]]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= n &lt;= 9</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数组</li><li>回溯</li></div></div><br><div><li>👍 2241</li><li>👎 0</li></div>
class NQueens {
    public static void main(String[] args) {
        Solution solution = new NQueens().new Solution();
        System.out.println(solution);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        List<List<String>> res = new LinkedList<>();
        int[] queen;

        public List<List<String>> solveNQueens(int n) {
            queen = new int[n];
            dfs(0, n);
            return res;
        }

        public void dfs(int row, int n) {
            if (row == queen.length) {
                List<String> subRes = new ArrayList<>();
                for (int j : queen) {
                    char[] chars = new char[n];
                    Arrays.fill(chars, '.');
                    chars[j] = 'Q';
                    subRes.add(new String(chars));
                }
                res.add(subRes);
                return;
            }
            for (int col = 0; col < n; col++) {
                if (isOk(row, col)) {
                    queen[row] = col;
                    dfs(row + 1, n);
                }
            }
        }

        //判断row行column列放置是否合适
        public boolean isOk(int row, int column) {
            for (int r = 0; r < row; r++) {
                int c = queen[r];
                if (c == column) return false;
                if (c - r == column - row || c + r == column + row) return false;
            }
            return true;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}