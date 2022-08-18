//<p>给你一个 <strong>无重复元素</strong> 的整数数组&nbsp;<code>candidates</code> 和一个目标整数&nbsp;<code>target</code>&nbsp;，找出&nbsp;<code>candidates</code>&nbsp;中可以使数字和为目标数&nbsp;<code>target</code> 的 <em>所有&nbsp;</em><strong>不同组合</strong> ，并以列表形式返回。你可以按 <strong>任意顺序</strong> 返回这些组合。</p>
//
//<p><code>candidates</code> 中的 <strong>同一个</strong> 数字可以 <strong>无限制重复被选取</strong> 。如果至少一个数字的被选数量不同，则两种组合是不同的。&nbsp;</p>
//
//<p>对于给定的输入，保证和为&nbsp;<code>target</code> 的不同组合数少于 <code>150</code> 个。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例&nbsp;1：</strong></p>
//
//<pre>
//<strong>输入：</strong>candidates = <span><code>[2,3,6,7], </code></span>target = <span><code>7</code></span>
//<strong>输出：</strong>[[2,2,3],[7]]
//<strong>解释：</strong>
//2 和 3 可以形成一组候选，2 + 2 + 3 = 7 。注意 2 可以使用多次。
//7 也是一个候选， 7 = 7 。
//仅有这两种组合。</pre>
//
//<p><strong>示例&nbsp;2：</strong></p>
//
//<pre>
//<strong>输入: </strong>candidates = [2,3,5]<span><code>, </code></span>target = 8
//<strong>输出: </strong>[[2,2,2,2],[2,3,3],[3,5]]</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入: </strong>candidates = <span><code>[2], </code></span>target = 1
//<strong>输出: </strong>[]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= candidates.length &lt;= 30</code></li> 
// <li><code>1 &lt;= candidates[i] &lt;= 200</code></li> 
// <li><code>candidate</code> 中的每个元素都 <strong>互不相同</strong></li> 
// <li><code>1 &lt;= target &lt;= 500</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数组</li><li>回溯</li></div></div><br><div><li>👍 2117</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import java.util.LinkedList;
import java.util.List;

class CombinationSum {
    public static void main(String[] args) {
        Solution solution = new CombinationSum().new Solution();
        System.out.println(solution.combinationSum(new int[]{2, 3, 6, 7}, 7));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        List<List<Integer>> res = new LinkedList<>();
        LinkedList<Integer> track = new LinkedList<>();
        int sum;

        public List<List<Integer>> combinationSum(int[] candidates, int target) {
            backtrack(candidates, target, 0);
            return res;
        }

        private void backtrack(int[] candidates, int target, int start) {
            if (sum == target) {
                res.add(new LinkedList<>(track));
                return;
            }
            if (sum > target) {
                return;
            }
            for (int i = start; i < candidates.length; i++) {
                sum += candidates[i];
                track.add(candidates[i]);
                backtrack(candidates, target, i);
                sum -= candidates[i];
                track.removeLast();
            }

        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}