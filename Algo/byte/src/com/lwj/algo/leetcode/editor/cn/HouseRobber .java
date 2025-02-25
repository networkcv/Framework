////<p>你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，<strong>如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警</strong>。</p>
////
////<p>给定一个代表每个房屋存放金额的非负整数数组，计算你<strong> 不触动警报装置的情况下 </strong>，一夜之内能够偷窃到的最高金额。</p>
////
////<p>&nbsp;</p>
////
////<p><strong>示例 1：</strong></p>
////
////<pre>
////<strong>输入：</strong>[1,2,3,1]
////<strong>输出：</strong>4
////<strong>解释：</strong>偷窃 1 号房屋 (金额 = 1) ，然后偷窃 3 号房屋 (金额 = 3)。
////&nbsp;    偷窃到的最高金额 = 1 + 3 = 4 。</pre>
////
////<p><strong>示例 2：</strong></p>
////
////<pre>
////<strong>输入：</strong>[2,7,9,3,1]
////<strong>输出：</strong>12
////<strong>解释：</strong>偷窃 1 号房屋 (金额 = 2), 偷窃 3 号房屋 (金额 = 9)，接着偷窃 5 号房屋 (金额 = 1)。
////&nbsp;    偷窃到的最高金额 = 2 + 9 + 1 = 12 。
////</pre>
////
////<p>&nbsp;</p>
////
////<p><strong>提示：</strong></p>
////
////<ul>
//// <li><code>1 &lt;= nums.length &lt;= 100</code></li>
//// <li><code>0 &lt;= nums[i] &lt;= 400</code></li>
////</ul>
////
////<div><div>Related Topics</div><div><li>数组</li><li>动态规划</li></div></div><br><div><li>👍 2258</li><li>👎 0</li></div>
//
//package com.lwj.algo.leetcode.editor.cn;
//
//import java.util.Arrays;
//
//class HouseRobber {
//    public static void main(String[] args) {
//        Solution solution = new HouseRobber().new Solution();
//        System.out.println(solution.rob(new int[]{1, 2, 3, 1}));
//        System.out.println(solution.rob(new int[]{2, 7, 9, 3, 1}));
////        System.out.println(solution.rob1(new int[]{114, 117, 207, 117, 235, 82, 90, 67, 143, 146, 53, 108, 200, 91, 80, 223, 58, 170, 110, 236, 81, 90, 222, 160, 165, 195, 187, 199, 114, 235, 197, 187, 69, 129, 64, 214, 228, 78, 188, 67, 205, 94, 205, 169, 241, 202, 144, 240}));
//    }
//
//    //leetcode submit region begin(Prohibit modification and deletion)
//    class Solution {
//        //暴力递归
//        public int rob1(int[] nums) {
//            return dp1(nums, 0);
//        }
//
//
//        private int dp1(int[] nums, int i) {
//            if (i >= nums.length) {
//                return 0;
//            }
//            return Math.max(
//                    //抢当前
//                    nums[i] + dp1(nums, i + 2),
//                    //不抢当前
//                    dp1(nums, i + 1)
//            );
//        }
//
//        int[] memo;
//
//        //dp 备忘录 自顶向下
//        public int rob2(int[] nums) {
//            memo = new int[nums.length];
//            Arrays.fill(memo, -1);
//            return dp(nums, 0);
//        }
//
//        private int dp(int[] nums, int i) {
//            if (i >= nums.length) {
//                return 0;
//            }
//            if (memo[i] != -1) {
//                return memo[i];
//            }
//            int res = Math.max(
//                    //抢当前
//                    nums[i] + dp(nums, i + 2),
//                    //不抢当前
//                    dp(nums, i + 1)
//            );
//            memo[i] = res;
//            return res;
//
//        }
//
//        //dp 自底向上
//        public int rob(int[] nums) {
//            int a1 = 0, a2 = 0;
//            for (int num : nums) {
//                int tmpMax = Math.max(a1 + num, a2);
//                a1 = a2;
//                a2 = tmpMax;
//            }
//            return Math.max(a1, a2);
//        }
//    }
////leetcode submit region end(Prohibit modification and deletion)
//
//}