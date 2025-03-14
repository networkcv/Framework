package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.List;

//<p>给你一个 <code>m</code> 行 <code>n</code> 列的矩阵&nbsp;<code>matrix</code> ，请按照 <strong>顺时针螺旋顺序</strong> ，返回矩阵中的所有元素。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p> 
//<img alt="" src="https://assets.leetcode.com/uploads/2020/11/13/spiral1.jpg" style="width: 242px; height: 242px;" /> 
//<pre>
//<strong>输入：</strong>matrix = [[1,2,3],[4,5,6],[7,8,9]]
//<strong>输出：</strong>[1,2,3,6,9,8,7,4,5]
//</pre>
//
//<p><strong>示例 2：</strong></p> 
//<img alt="" src="https://assets.leetcode.com/uploads/2020/11/13/spiral.jpg" style="width: 322px; height: 242px;" /> 
//<pre>
//<strong>输入：</strong>matrix = [[1,2,3,4],[5,6,7,8],[9,10,11,12]]
//<strong>输出：</strong>[1,2,3,4,8,12,11,10,9,5,6,7]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>m == matrix.length</code></li> 
// <li><code>n == matrix[i].length</code></li> 
// <li><code>1 &lt;= m, n &lt;= 10</code></li> 
// <li><code>-100 &lt;= matrix[i][j] &lt;= 100</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数组</li><li>矩阵</li><li>模拟</li></div></div><br><div><li>👍 1885</li><li>👎 0</li></div>
class SpiralMatrix {
    public static void main(String[] args) {
        Solution solution = new SpiralMatrix().new Solution();
        System.out.println(solution.spiralOrder(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<Integer> spiralOrder(int[][] matrix) {
            List<Integer> res = new ArrayList<>();
            //四个方向 对应 右 下 左  上
            int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
            int m = matrix.length;
            int n = matrix[0].length;
            int i = 0;
            int j = 0;
            int di = 0;
            boolean[][] visited = new boolean[m][n];
            for (int k = 0; k < m * n; k++) {
                res.add(matrix[i][j]);
                visited[i][j] = true;
                int x = i + dirs[di][0];
                int y = j + dirs[di][1];
                if (x < 0 || y < 0 || x >= m || y >= n || visited[x][y]) {
                    //转向
                    di = (di + 1) % 4;
                }
                i += dirs[di][0];
                j += dirs[di][1];
            }
            return res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}