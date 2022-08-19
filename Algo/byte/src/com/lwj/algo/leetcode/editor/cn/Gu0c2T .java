//<p>一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响小偷偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，<strong>如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警</strong>。</p>
//
//<p>给定一个代表每个房屋存放金额的非负整数数组 <code>nums</code>&nbsp;，请计算<strong>&nbsp;不触动警报装置的情况下 </strong>，一夜之内能够偷窃到的最高金额。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums =<strong> </strong>[1,2,3,1]
//<strong>输出：</strong>4
//<strong>解释：</strong>偷窃 1 号房屋 (金额 = 1) ，然后偷窃 3 号房屋 (金额 = 3)。
//&nbsp;    偷窃到的最高金额 = 1 + 3 = 4 。</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums =<strong> </strong>[2,7,9,3,1]
//<strong>输出：</strong>12
//<strong>解释：</strong>偷窃 1 号房屋 (金额 = 2), 偷窃 3 号房屋 (金额 = 9)，接着偷窃 5 号房屋 (金额 = 1)。
//&nbsp;    偷窃到的最高金额 = 2 + 9 + 1 = 12 。
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= nums.length &lt;= 100</code></li> 
// <li><code>0 &lt;= nums[i] &lt;= 400</code></li> 
//</ul>
//
//<p>&nbsp;</p>
//
//<p>
// <meta charset="UTF-8" />注意：本题与主站 198&nbsp;题相同：&nbsp;<a href="https://leetcode-cn.com/problems/house-robber/">https://leetcode-cn.com/problems/house-robber/</a></p>
//
//<div><div>Related Topics</div><div><li>数组</li><li>动态规划</li></div></div><br><div><li>👍 22</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;

class Gu0c2T {
    public static void main(String[] args) {
        Solution solution = new Gu0c2T().new Solution();
        System.out.println(solution.rob(new int[]{1, 2, 3, 1}));
        System.out.println(solution.rob(new int[]{2, 7, 9, 3, 1}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        int[] memo;

        public int rob(int[] nums) {
            memo = new int[nums.length];
            Arrays.fill(memo, -1);
            return dp(nums, nums.length - 1);
        }

        //索引为i时获取的最大金额
        private int dp(int[] nums, int i) {
            if (i < 0) {
                return 0;
            }
            if (memo[i] != -1) {
                return memo[i];
            }
            int max = Math.max(nums[i] + dp(nums, i - 2), dp(nums, i - 1));
            memo[i] = max;
            return max;
        }


        public int rob2(int[] nums) {
            return dp2(nums, 0);
        }


        private int dp2(int[] nums, int i) {
            if (i >= nums.length) {
                return 0;
            }
            return Math.max(
                    //抢当前
                    nums[i] + dp2(nums, i + 2),
                    //不抢当前
                    dp2(nums, i + 1));
        }

        public int rob3(int[] nums) {
            int a1 = 0, a2 = 0, tmp;
            for (int num : nums) {
                tmp = Math.max(a1 + num, a2);
                a1 = a2;
                a2 = tmp;
            }
            return a2;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)

}