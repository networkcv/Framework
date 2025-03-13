package com.lwj.algo.leetcode.editor.cn;

//<p>给你一个由&nbsp;<code>'1'</code>（陆地）和 <code>'0'</code>（水）组成的的二维网格，请你计算网格中岛屿的数量。</p>
//
//<p>岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。</p>
//
//<p>此外，你可以假设该网格的四条边均被水包围。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>grid = [
//  ["1","1","1","1","0"],
//  ["1","1","0","1","0"],
//  ["1","1","0","0","0"],
//  ["0","0","0","0","0"]
//]
//<strong>输出：</strong>1
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>grid = [
//  ["1","1","0","0","0"],
//  ["1","1","0","0","0"],
//  ["0","0","1","0","0"],
//  ["0","0","0","1","1"]
//]
//<strong>输出：</strong>3
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>m == grid.length</code></li> 
// <li><code>n == grid[i].length</code></li> 
// <li><code>1 &lt;= m, n &lt;= 300</code></li> 
// <li><code>grid[i][j]</code> 的值为 <code>'0'</code> 或 <code>'1'</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>深度优先搜索</li><li>广度优先搜索</li><li>并查集</li><li>数组</li><li>矩阵</li></div></div><br><div><li>👍 2716</li><li>👎 0</li></div>
class NumberOfIslands {
    public static void main(String[] args) {
        Solution solution = new NumberOfIslands().new Solution();
        char[][] numbers = {{'1', '1', '0', '0', '0'}, {'1', '1', '0', '0', '0'}, {'0', '0', '1', '0', '0'}, {'0', '0', '0', '1', '1'}};
        System.out.println(solution.numIslands(numbers));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        boolean[][] visited;
        char[][] grid;

        public int numIslands(char[][] grid) {
            this.grid = grid;
            int res = 0;
            visited = new boolean[grid.length][grid[0].length];
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (grid[i][j] == '1' && !visited[i][j]) {
                        travese(i, j);
                        res++;
                    }
                }
            }
            return res;
        }

        public void travese(int i, int j) {
            if (i < 0 || j < 0 || i == visited.length || j == visited[0].length || grid[i][j] == '0' || visited[i][j]) {
                return;
            }
            visited[i][j] = true;
            travese(i - 1, j);
            travese(i, j - 1);
            travese(i, j + 1);
            travese(i + 1, j);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}