package com.lwj.algo.leetcode.editor.cn;

//<p>给你一个整数数组 <code>nums</code> ，找到其中最长严格递增子序列的长度。</p>
//
//<p><strong>子序列&nbsp;</strong>是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。例如，<code>[3,6,2,7]</code> 是数组 <code>[0,3,1,6,2,2,7]</code> 的<span data-keyword="subsequence-array">子序列</span>。</p> &nbsp;
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [10,9,2,5,3,7,101,18]
//<strong>输出：</strong>4
//<strong>解释：</strong>最长递增子序列是 [2,3,7,101]，因此长度为 4 。
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [0,1,0,3,2,3]
//<strong>输出：</strong>4
//</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [7,7,7,7,7,7,7]
//<strong>输出：</strong>1
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= nums.length &lt;= 2500</code></li> 
// <li><code>-10<sup>4</sup> &lt;= nums[i] &lt;= 10<sup>4</sup></code></li> 
//</ul>
//
//<p>&nbsp;</p>
//
//<p><b>进阶：</b></p>
//
//<ul> 
// <li>你能将算法的时间复杂度降低到&nbsp;<code>O(n log(n))</code> 吗?</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数组</li><li>二分查找</li><li>动态规划</li></div></div><br><div><li>👍 3883</li><li>👎 0</li></div>
class LongestIncreasingSubsequence {
    public static void main(String[] args) {
        Solution solution = new LongestIncreasingSubsequence().new Solution();
//        System.out.println(solution.lengthOfLIS(new int[]{4, 10, 4, 3, 8, 9}));
        System.out.println(solution.lengthOfLIS(new int[]{10, 9, 2, 5, 3, 7, 101, 18}));
//        System.out.println(solution.lengthOfLIS(new int[]{7, 7, 7, 7, 7, 7, 7}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        int[] nums;

        public int lengthOfLIS(int[] nums) {
            this.nums = nums;
            int res = 0;
            for (int i = nums.length - 1; i >= 0; i--) {
                res = Math.max(res, dfs(i));
            }
            return res;
        }

        //回溯版-答案视角-暴力回溯
        public int dfs(int i) {
            int res = 0;
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    res = Math.max(res, dfs(j));
                }
            }
            return res + 1;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}