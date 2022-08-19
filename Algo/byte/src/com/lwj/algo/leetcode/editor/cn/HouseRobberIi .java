//<p>你是一个专业的小偷，计划偷窃沿街的房屋，每间房内都藏有一定的现金。这个地方所有的房屋都 <strong>围成一圈</strong> ，这意味着第一个房屋和最后一个房屋是紧挨着的。同时，相邻的房屋装有相互连通的防盗系统，<strong>如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警</strong> 。</p>
//
//<p>给定一个代表每个房屋存放金额的非负整数数组，计算你 <strong>在不触动警报装置的情况下</strong> ，今晚能够偷窃到的最高金额。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例&nbsp;1：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [2,3,2]
//<strong>输出：</strong>3
//<strong>解释：</strong>你不能先偷窃 1 号房屋（金额 = 2），然后偷窃 3 号房屋（金额 = 2）, 因为他们是相邻的。
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [1,2,3,1]
//<strong>输出：</strong>4
//<strong>解释：</strong>你可以先偷窃 1 号房屋（金额 = 1），然后偷窃 3 号房屋（金额 = 3）。
//&nbsp;    偷窃到的最高金额 = 1 + 3 = 4 。</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [1,2,3]
//<strong>输出：</strong>3
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= nums.length &lt;= 100</code></li> 
// <li><code>0 &lt;= nums[i] &lt;= 1000</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数组</li><li>动态规划</li></div></div><br><div><li>👍 1135</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

class HouseRobberIi {
    public static void main(String[] args) {
        Solution solution = new HouseRobberIi().new Solution();
        System.out.println(solution.rob(new int[]{2, 3, 2}));
        System.out.println(solution.rob(new int[]{1, 2, 3, 1}));

    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int rob(int[] nums) {
            int n = nums.length;
            if (n == 1) {
                return nums[0];
            }

            //环形数组问题：
            //首先，首尾房间不能同时被抢，那么只可能有三种不同情况：
            //要么都不抢(index): : 1, n-2 这种情况可以pass掉，因为数组是正数，所以两个总得抢一个才能最大
            //要么第一间房子被抢最后一间不抢: 0, n - 2
            //要么最后一间房子被抢第一间不抢: 1, n - 1
            return Math.max(robRange(nums, 0, n - 2), robRange(nums, 1, n - 1));

        }

        private int robRange(int[] nums, int start, int end) {
            int a1 = 0, a2 = 0, tmp;
            for (int i = start; i <= end; i++) {
                tmp = Math.max(a1 + nums[i], a2);
                a1 = a2;
                a2 = tmp;
            }
            return Math.max(a1, a2);
        }

    }
//leetcode submit region end(Prohibit modification and deletion)

}