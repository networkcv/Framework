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
//<div><div>Related Topics</div><div><li>数组</li><li>回溯</li></div></div><br><div><li>👍 1464</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class NQueens {
    public static void main(String[] args) {
        Solution solution = new NQueens().new Solution();
//        System.out.println(solution.solveNQueens(4));
        System.out.println(solution.solveNQueens(8));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        List<List<String>> res = new LinkedList<>();

        public List<List<String>> solveNQueens(int n) {
            //初始化棋盘
            char[][] board = initBoard(n);
            backtrack(board, 0);
            return res;
        }

        // 路径：board 中小于 row 的那些行都已经成功放置了皇后
        // 选择列表：第 row 行的所有列都是放置皇后的选择
        // 结束条件：row 超过 board 的最后一行
        private void backtrack(char[][] board, int row) {
            int n = board.length;
            if (n == row) {
                res.add(char2List(board));
                return;
            }
            for (int col = 0; col < n; col++) {
                if (!isValid(board, row, col)) {
                    continue;
                }
                board[row][col] = 'Q';
                backtrack(board, row + 1);
                board[row][col] = '.';
            }
        }

        private List<String> char2List(char[][] board) {
            ArrayList<String> res = new ArrayList<>(board.length);
            for (char[] chars : board) {
                res.add(String.valueOf(chars));
            }
            return res;
        }

        //判断 在已有board棋盘下 row行col列放置皇后是否会互相攻击
        private boolean isValid(char[][] board, int row, int col) {
            //由于backtrack会对一行内的Q进行回溯，所以不会出现Q在同一行，只需检测是否在同一列，或者同一斜线
            //又因为外部当前处理的是row行，所以row行以下的都没有放置皇后，所以不需要向下去检测，所以检测范围再次缩小到。
            //最终只需要检测 同一列是否有皇后，或者左上，右上 这三个角度
            //同一列是否有皇后
            for (int i = 0; i < row; i++) {
                if (board[i][col] == 'Q') {
                    return false;
                }
            }
            //左上是否有皇后
            for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--) {
                if (board[i][j] == 'Q') {
                    return false;
                }
            }
            //右上是否有皇后
            for (int i = row - 1, j = col + 1; i >= 0 && j < board.length; i--, j++) {
                if (board[i][j] == 'Q') {
                    return false;
                }
            }
            return true;
        }

        private char[][] initBoard(int n) {
            char[][] boards = new char[n][n];
            for (char[] board : boards) {
                Arrays.fill(board, '.');
            }
            return boards;
        }


    }
//leetcode submit region end(Prohibit modification and deletion)

}