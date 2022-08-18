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
//<div><div>Related Topics</div><div><li>回溯</li></div></div><br><div><li>👍 1089</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import java.util.LinkedList;
import java.util.List;

class Combinations {
    public static void main(String[] args) {
        Solution solution = new Combinations().new Solution();
        System.out.println(solution.combine(4, 2));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        List<List<Integer>> res = new LinkedList<>();
        LinkedList<Integer> track = new LinkedList<>();

        //无重组合问题其实就是将无重子集中的某一层取了出来
        public List<List<Integer>> combine1(int n, int k) {
            int[] nums = new int[n];
            for (int i = 0; i < n; i++) {
                nums[i] = i + 1;
            }
            backtrack1(nums, track, k, 0);
            return res;

        }

        /* 对比一下 无重子集问题的回溯
        private void backtrack(int[] nums, int start) {
            res.add(new LinkedList<>(track));
            for (int i = start; i < nums.length; i++) {
                track.add(nums[i]);
                backtrack(nums, i + 1);
                track.removeLast();
            }
        }
        */

        private void backtrack1(int[] nums, LinkedList<Integer> track, int k, int start) {
            if (track.size() == k) {
                res.add(new LinkedList<>(track));
                return;
            }
            for (int i = start; i < nums.length; i++) {
                track.add(nums[i]);
                backtrack1(nums, track, k, i + 1);
                track.removeLast();
            }
        }

        public List<List<Integer>> combine(int n, int k) {
            backtrack(1, n, k);
            return res;
        }

        private void backtrack(int start, int n, int k) {
            if (track.size() == k) {
                res.add(new LinkedList<>(track));
                return;
            }
            for (int i = start; i <= n; i++) {
                track.add(i);
                backtrack(i + 1, n, k);
                track.removeLast();
            }
        }

    }
//leetcode submit region end(Prohibit modification and deletion)

}