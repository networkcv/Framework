//<p>给定一个&nbsp;<code>m x n</code> 二维字符网格&nbsp;<code>board</code> 和一个字符串单词&nbsp;<code>word</code> 。如果&nbsp;<code>word</code> 存在于网格中，返回 <code>true</code> ；否则，返回 <code>false</code> 。</p>
//
//<p>单词必须按照字母顺序，通过相邻的单元格内的字母构成，其中“相邻”单元格是那些水平相邻或垂直相邻的单元格。同一个单元格内的字母不允许被重复使用。</p>
//
//<p>&nbsp;</p>
//
//<p>例如，在下面的 3×4 的矩阵中包含单词 "ABCCED"（单词中的字母已标出）。</p>
//
//<p><img alt="" src="https://assets.leetcode.com/uploads/2020/11/04/word2.jpg" style="width: 322px; height: 242px;" /></p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>board = [["A","B","C","E"],["S","F","C","S"],["A","D","E","E"]], word = "ABCCED"
//<strong>输出：</strong>true
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>board = [["a","b"],["c","d"]], word = "abcd"
//<strong>输出：</strong>false
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>m == board.length</code></li> 
// <li><code>n = board[i].length</code></li> 
// <li><code>1 &lt;= m, n &lt;= 6</code></li> 
// <li><code>1 &lt;= word.length &lt;= 15</code></li> 
// <li><code>board </code>和<code> word </code>仅由大小写英文字母组成</li> 
//</ul>
//
//<p><strong>注意：</strong>本题与主站 79 题相同：<a href="https://leetcode-cn.com/problems/word-search/">https://leetcode-cn.com/problems/word-search/</a></p>
//
//<div><div>Related Topics</div><div><li>数组</li><li>回溯</li><li>矩阵</li></div></div><br><div><li>👍 673</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

class JuZhenZhongDeLuJingLcof {
    public static void main(String[] args) {
        Solution solution = new JuZhenZhongDeLuJingLcof().new Solution();
//        char[][] board = new char[][]{{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}};
//        solution.exist(board, "ABCCED");
        char[][] board = {{'A', 'B', 'C', 'E'}, {'S', 'F', 'C', 'S'}, {'A', 'D', 'E', 'E'}};
        solution.exist(board, "ABCB");
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean exist(char[][] board, String word) {
            boolean[][] visited = new boolean[board.length][board[0].length];
            char[] chars = word.toCharArray();
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[0].length; j++) {
                    if (dfs(board, chars, visited, i, j, 0)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean dfs(char[][] board, char[] chars, boolean[][] visited, int i, int j, int start) {
            if (i < 0 || i >= board.length || j < 0 || j >= board[0].length ||
                    board[i][j] != chars[start] || visited[i][j]) {
                return false;
            }
            if (start == chars.length - 1) {
                return true;
            }
            visited[i][j] = true;
            boolean res = dfs(board, chars, visited, i + 1, j, start + 1) || dfs(board, chars, visited, i - 1, j, start + 1) || dfs(board, chars, visited, i, j + 1, start + 1) || dfs(board, chars, visited, i, j - 1, start + 1);
            visited[i][j] = false;
            return res;
        }


    }
//leetcode submit region end(Prohibit modification and deletion)

}