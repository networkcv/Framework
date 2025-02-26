package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;
import java.util.LinkedList;

//<p>你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，<strong>如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警</strong>。</p>
//
//<p>给定一个代表每个房屋存放金额的非负整数数组，计算你<strong> 不触动警报装置的情况下 </strong>，一夜之内能够偷窃到的最高金额。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>[1,2,3,1]
//<strong>输出：</strong>4
//<strong>解释：</strong>偷窃 1 号房屋 (金额 = 1) ，然后偷窃 3 号房屋 (金额 = 3)。
//&nbsp;    偷窃到的最高金额 = 1 + 3 = 4 。</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>[2,7,9,3,1]
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
//<div><div>Related Topics</div><div><li>数组</li><li>动态规划</li></div></div><br><div><li>👍 3182</li><li>👎 0</li></div>
class HouseRobber {
    public static void main(String[] args) {
        Solution solution = new HouseRobber().new Solution();
//        int[] param = new int[]{183, 219, 57, 193, 94, 233, 202, 154, 65, 240, 97, 234, 100, 249, 186, 66, 90, 238, 168, 128, 177, 235, 50, 81, 185, 165, 217, 207, 88, 80, 112, 78, 135, 62, 228, 247, 211};
        int[] param = new int[]{1, 2, 3, 1};
        System.out.println(solution.rob(param));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int rob(int[] nums) {
            return rob2(nums);
        }

        //回溯版-暴力回溯
        public int rob0(int[] nums) {
            dfs0(nums.length - 1, nums);
            return res;
        }

        int res = 0;
        LinkedList<Integer> path = new LinkedList<>();

        public void dfs0(int i, int[] nums) {
            if (i < 0) {
                Integer sum = path.stream().reduce(Integer::sum).orElse(0);
                res = Math.max(res, sum);
                return;
            }
            dfs0(i - 1, nums);
            path.add(nums[i]);
            dfs0(i - 2, nums);
            path.removeLast();
        }

    }
//leetcode submit region end(Prohibit modification and deletion)

}