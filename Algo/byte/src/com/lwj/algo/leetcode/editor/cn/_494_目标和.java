package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;

//<p>给你一个非负整数数组 <code>nums</code> 和一个整数 <code>target</code> 。</p>
//
//<p>向数组中的每个整数前添加&nbsp;<code>'+'</code> 或 <code>'-'</code> ，然后串联起所有整数，可以构造一个 <strong>表达式</strong> ：</p>
//
//<ul> 
// <li>例如，<code>nums = [2, 1]</code> ，可以在 <code>2</code> 之前添加 <code>'+'</code> ，在 <code>1</code> 之前添加 <code>'-'</code> ，然后串联起来得到表达式 <code>"+2-1"</code> 。</li> 
//</ul>
//
//<p>返回可以通过上述方法构造的、运算结果等于 <code>target</code> 的不同 <strong>表达式</strong> 的数目。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [1,1,1,1,1], target = 3
//<strong>输出：</strong>5
//<strong>解释：</strong>一共有 5 种方法让最终目标和为 3 。
//-1 + 1 + 1 + 1 + 1 = 3
//+1 - 1 + 1 + 1 + 1 = 3
//+1 + 1 - 1 + 1 + 1 = 3
//+1 + 1 + 1 - 1 + 1 = 3
//+1 + 1 + 1 + 1 - 1 = 3
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [1], target = 1
//<strong>输出：</strong>1
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= nums.length &lt;= 20</code></li> 
// <li><code>0 &lt;= nums[i] &lt;= 1000</code></li> 
// <li><code>0 &lt;= sum(nums[i]) &lt;= 1000</code></li> 
// <li><code>-1000 &lt;= target &lt;= 1000</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数组</li><li>动态规划</li><li>回溯</li></div></div><br><div><li>👍 2086</li><li>👎 0</li></div>
class TargetSum {
    public static void main(String[] args) {
        Solution solution = new TargetSum().new Solution();
        System.out.println(solution.findTargetSumWays(new int[]{1, 1, 1, 1, 1}, 3));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int findTargetSumWays(int[] nums, int target) {

        }

        public int findTargetSumWays0(int[] nums, int target) {
            Integer sum = Arrays.stream(nums).boxed().reduce(Integer::sum).orElse(0);
            target += sum;
            if (target < 0 || target % 2 == 1) {
                return 0;
            }
            return dfs0(nums, nums.length - 1, target / 2);
        }

        /**
         * 回溯版-暴力回溯
         * <p>
         * 从nums中cur前的数字中选一部分数等于target，返回多少种选法
         *
         * @param cur    当前遍历值
         * @param target target
         * @return 选择种类数
         */
        public int dfs0(int[] nums, int cur, int target) {
            if (cur < 0) {
                return target == 0 ? 1 : 0;
            }
            return dfs0(nums, cur - 1, target) + dfs0(nums, cur - 1, target - nums[cur]);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}