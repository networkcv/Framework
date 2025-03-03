package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//<p>给定一个非负整数&nbsp;<em><code>numRows</code>，</em>生成「杨辉三角」的前&nbsp;<em><code>numRows</code>&nbsp;</em>行。</p>
//
//<p><small>在「杨辉三角」中，每个数是它左上方和右上方的数的和。</small></p>
//
//<p><img alt="" src="https://pic.leetcode-cn.com/1626927345-DZmfxB-PascalTriangleAnimated2.gif" /></p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1:</strong></p>
//
//<pre>
//<strong>输入:</strong> numRows = 5
//<strong>输出:</strong> [[1],[1,1],[1,2,1],[1,3,3,1],[1,4,6,4,1]]
//</pre>
//
//<p><strong>示例&nbsp;2:</strong></p>
//
//<pre>
//<strong>输入:</strong> numRows = 1
//<strong>输出:</strong> [[1]]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示:</strong></p>
//
//<ul> 
// <li><code>1 &lt;= numRows &lt;= 30</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数组</li><li>动态规划</li></div></div><br><div><li>👍 1226</li><li>👎 0</li></div>
class PascalsTriangle {
    public static void main(String[] args) {
        Solution solution = new PascalsTriangle().new Solution();
        System.out.println(solution.generate(3));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        Map<Integer, List<Integer>> cache;


        public List<List<Integer>> generate(int numRows) {
            cache = new HashMap<>(numRows);
            List<List<Integer>> res = new ArrayList<>();
            for (int i = 1; i <= numRows; i++) {
                res.add(dfs(i));
            }
            return res;
        }

        public List<Integer> dfs(int numRows) {
            List<Integer> cacheHit = cache.get(numRows);
            if (cacheHit != null) {
                return cacheHit;
            }
            ArrayList<Integer> newList = new ArrayList<>();
            newList.add(1);
            if (numRows == 1) {
                cache.put(numRows, newList);
                return newList;
            }
            List<Integer> list = dfs(numRows - 1);
            for (int i = 1; i < list.size(); i++) {
                newList.add(list.get(i) + list.get(i - 1));
            }
            newList.add(1);
            cache.put(numRows, newList);
            return newList;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}