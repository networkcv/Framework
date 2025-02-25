package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//<p>给定一个不含重复数字的数组 <code>nums</code> ，返回其 <em>所有可能的全排列</em> 。你可以 <strong>按任意顺序</strong> 返回答案。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [1,2,3]
//<strong>输出：</strong>[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [0,1]
//<strong>输出：</strong>[[0,1],[1,0]]
//</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [1]
//<strong>输出：</strong>[[1]]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= nums.length &lt;= 6</code></li> 
// <li><code>-10 &lt;= nums[i] &lt;= 10</code></li> 
// <li><code>nums</code> 中的所有整数 <strong>互不相同</strong></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数组</li><li>回溯</li></div></div><br><div><li>👍 3037</li><li>👎 0</li></div>
class Permutations {
    public static void main(String[] args) {
        Solution solution = new Permutations().new Solution();
        System.out.println(solution);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        List<List<Integer>> res = new ArrayList<>();
        LinkedList<Integer> path = new LinkedList<>();
        int[] nums;

        public List<List<Integer>> permute(int[] nums) {
            this.nums = nums;
            dfs(0);
            return res;
        }

        /**
         * 答案视角
         *
         * @param i 当前遍历元素
         */
        public void dfs(int i) {
            if (i == nums.length) {
                res.add(new ArrayList<>(path));
                return;
            }
            for (int num : nums) {
                if (!path.contains(num)) {
                    path.add(num);
                    dfs(i + 1);
                    path.removeLast();
                }
            }
        }
    }
//leetcode submit region end(Prohibit modification and deletion)
}
