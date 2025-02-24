package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//<p>找出所有相加之和为&nbsp;<code>n</code><em> </em>的&nbsp;<code>k</code><strong>&nbsp;</strong>个数的组合，且满足下列条件：</p>
//
//<ul> 
// <li>只使用数字1到9</li> 
// <li>每个数字&nbsp;<strong>最多使用一次</strong>&nbsp;</li> 
//</ul>
//
//<p>返回 <em>所有可能的有效组合的列表</em> 。该列表不能包含相同的组合两次，组合可以以任何顺序返回。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1:</strong></p>
//
//<pre>
//<strong>输入:</strong> <em><strong>k</strong></em> = 3, <em><strong>n</strong></em> = 7
//<strong>输出:</strong> [[1,2,4]]
//<strong>解释:</strong>
//1 + 2 + 4 = 7
//没有其他符合的组合了。</pre>
//
//<p><strong>示例 2:</strong></p>
//
//<pre>
//<strong>输入:</strong> <em><strong>k</strong></em> = 3, <em><strong>n</strong></em> = 9
//<strong>输出:</strong> [[1,2,6], [1,3,5], [2,3,4]]
//<strong>解释:
//</strong>1 + 2 + 6 = 9
//1 + 3 + 5 = 9
//2 + 3 + 4 = 9
//没有其他符合的组合了。</pre>
//
//<p><strong>示例 3:</strong></p>
//
//<pre>
//<strong>输入:</strong> k = 4, n = 1
//<strong>输出:</strong> []
//<strong>解释:</strong> 不存在有效的组合。
//在[1,9]范围内使用4个不同的数字，我们可以得到的最小和是1+2+3+4 = 10，因为10 &gt; 1，没有有效的组合。
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示:</strong></p>
//
//<ul> 
// <li><code>2 &lt;= k &lt;= 9</code></li> 
// <li><code>1 &lt;= n &lt;= 60</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数组</li><li>回溯</li></div></div><br><div><li>👍 919</li><li>👎 0</li></div>
class CombinationSumIii {
    public static void main(String[] args) {
        Solution solution = new CombinationSumIii().new Solution();
        System.out.println(solution);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        List<List<Integer>> res = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();

        /**
         * @param k k个数字
         * @param n k个数之和为n
         */
        public List<List<Integer>> combinationSum3(int k, int n) {
//            dfs(9, k, n);
            dfs1(9, k, n);
            return res;
        }

        /**
         * 选择视角
         */
        private void dfs1(int i, int k, int t) {
            int need = k - path.size();
            if (t < 0 || need > i) return;
            if (t == 0 && need == 0) {
                res.add(new ArrayList<>(path));
                return;
            }
            dfs(i - 1, k, t);
            path.add(i);
            dfs(i - 1, k, t - i);
            path.removeLast();
        }

        /**
         * @param i 当前遍历元素
         * @param k k个数字
         * @param t targe（还差的数字）t=n-已累加的数字
         */
        private void dfs(int i, int k, int t) {
            int need = k - path.size();
            //剪枝
            if (need > i || t < 0) {
                return;
            }
            if (t == 0 && need == 0) {
                res.add(new ArrayList<>(path));
                return;
            }
            for (int j = i; j > 0; j--) {
                path.add(j);
                dfs(j - 1, k, t - j);
                path.removeLast();
            }
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}