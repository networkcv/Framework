package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.List;

//<p>给你一个整数数组&nbsp;<code>nums</code> ，数组中的元素 <strong>互不相同</strong> 。返回该数组所有可能的<span data-keyword="subset">子集</span>（幂集）。</p>
//
//<p>解集 <strong>不能</strong> 包含重复的子集。你可以按 <strong>任意顺序</strong> 返回解集。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [1,2,3]
//<strong>输出：</strong>[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [0]
//<strong>输出：</strong>[[],[0]]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= nums.length &lt;= 10</code></li> 
// <li><code>-10 &lt;= nums[i] &lt;= 10</code></li> 
// <li><code>nums</code> 中的所有元素 <strong>互不相同</strong></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>位运算</li><li>数组</li><li>回溯</li></div></div><br><div><li>👍 2438</li><li>👎 0</li></div>
class Subsets {
    public static void main(String[] args) {
        Solution solution = new Subsets().new Solution();
        System.out.println(solution);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        List<List<Integer>> res = new ArrayList<>();
        List<Integer> path = new ArrayList<>();


        public List<List<Integer>> subsets(int[] nums) {
//            dfs(0, nums);
            dfs2(0, nums);
            return res;
        }

        /**
         * 以答案视角
         */
        public void dfs2(int i, int[] nums) {
            res.add(new ArrayList<>(path));
            if (i == nums.length) return;

            for (int j = i; j < nums.length; j++) {
                path.add(nums[j]);
                dfs2(j + 1, nums);
                path.remove(path.size() - 1);
            }
        }

        /**
         * 以输入视角
         *
         * @param i    表示当前对第i个下标的元素进行选择
         * @param nums nums
         */
        public void dfs(int i, int[] nums) {
            if (i == nums.length) {
                res.add(new ArrayList<>(path));
                return;
            }
            //不选当前数
            dfs(i + 1, nums);
            //选
            path.add(nums[i]);
            dfs(i + 1, nums);
            //这一步很重要，因为公用了一个path，所以在递归结束的时候移除当前添加的节点
            path.remove(path.size() - 1);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}