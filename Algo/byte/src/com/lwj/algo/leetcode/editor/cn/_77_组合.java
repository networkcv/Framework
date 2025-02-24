package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//<p>给定两个整数 <code>n</code> 和 <code>k</code>，返回范围 <code>[1, n]</code> 中所有可能的 <code>k</code> 个数的组合。</p>
//
//<p>你可以按 <strong>任何顺序</strong> 返回答案。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>n = 4, k = 2
//<strong>输出：</strong>
//[
//  [2,4],
//  [3,4],
//  [2,3],
//  [1,2],
//  [1,3],
//  [1,4],
//]</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>n = 1, k = 1
//<strong>输出：</strong>[[1]]</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= n &lt;= 20</code></li> 
// <li><code>1 &lt;= k &lt;= n</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>回溯</li></div></div><br><div><li>👍 1728</li><li>👎 0</li></div>
class Combinations {
    public static void main(String[] args) {
        Solution solution = new Combinations().new Solution();
        System.out.println(solution.combine(4, 2));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        List<List<Integer>> res = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        int n;
        int k;

        public List<List<Integer>> combine(int n, int k) {
            this.n = n;
            this.k = k;
//            dfs(1, n, k);
//            dfs0(n);
            dfs1(n);
            return res;
        }

        /**
         * 答案视角 倒序
         *
         * @param i 当前遍历元素
         */
        public void dfs1(int i) {
            int need = k - path.size();
            if (need == 0) {
                res.add(new ArrayList<>(path));
                return;
            }
            if (need > i) {
                return;
            }
            for (int j = i; j > 0; j--) {
                path.add(j);
                dfs1(j - 1);
                path.removeLast();
            }
        }

        /**
         * 输入视角 倒序
         *
         * @param i 当前遍历元素
         */
        public void dfs0(int i) {
            int need = k - path.size();
            if (need == 0) {
                res.add(new ArrayList<>(path));
                return;
            }
            if (need > i) {
                return;
            }
            dfs0(i - 1);
            path.addLast(i);
            dfs0(i - 1);
            path.removeLast();
        }


        /**
         * 输入视角 正序
         *
         * @param i 当前遍历元素
         */
        public void dfs(int i, int n, int k) {
            if (path.size() == k) {
                res.add(new ArrayList<>(path));
                return;
            }
            //(n-i+1)表示还可以选几个数  k-path.size() 表示还剩差几个数
            if (i > n || n - i + 1 < k - path.size()) {
                return;
            }
            //不选
            dfs(i + 1, n, k);
            //选
            path.add(i);
            dfs(i + 1, n, k);
            path.removeLast();
        }
    }

//leetcode submit region end(Prohibit modification and deletion)

}