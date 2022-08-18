//<p>给定一个可包含重复数字的序列 <code>nums</code> ，<em><strong>按任意顺序</strong></em> 返回所有不重复的全排列。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [1,1,2]
//<strong>输出：</strong>
//[[1,1,2],
// [1,2,1],
// [2,1,1]]
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [1,2,3]
//<strong>输出：</strong>[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= nums.length &lt;= 8</code></li> 
// <li><code>-10 &lt;= nums[i] &lt;= 10</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数组</li><li>回溯</li></div></div><br><div><li>👍 1161</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class PermutationsIi {
    public static void main(String[] args) {
        Solution solution = new PermutationsIi().new Solution();
        System.out.println(solution.permuteUnique(new int[]{1, 1, 2}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        List<List<Integer>> res = new LinkedList<>();
        LinkedList<Integer> track = new LinkedList<>();

        public List<List<Integer>> permuteUnique(int[] nums) {
            Arrays.sort(nums);
            boolean[] used = new boolean[nums.length];
            backtrack(nums, used, track);
            return res;
        }

        private void backtrack(int[] nums, boolean[] used, LinkedList<Integer> track) {
            if (track.size() == nums.length) {
                res.add(new LinkedList<>(track));
                return;
            }
            for (int i = 0; i < nums.length; i++) {
                if (used[i]) {
                    continue;
                }
                //这里的需要特别关注 !used[i-1] 这个判断逻辑。
                //假设重复的1表示为1` 那么[1,1`,2]和[1`,1,2]重复的原因是两个1调换了位置，所以这里的实现是先排序，
                //然后!used[i-1]是确保使用 1`前 1 必须被使用，相当于固定了两个1出现的顺序
                if (i != 0 && nums[i] == nums[i - 1] && !used[i-1]) {
                    continue;
                }
                track.add(nums[i]);
                used[i] = true;
                backtrack(nums, used, track);
                track.removeLast();
                used[i] = false;

            }
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}