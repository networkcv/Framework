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
            return rob4(nums);
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

        //回溯版，这里没有像之前的dfs只进行深度遍历，该dfs在递归返回的时候返回了当前问题的解,优化了空间复杂度O(1)
        public int rob1(int[] nums) {
            return dfs1(nums.length - 1, nums);
        }

        public int dfs1(int i, int[] nums) {
            if (i < 0) {
                return 0;
            }
            return Math.max(dfs1(i - 1, nums), dfs1(i - 2, nums) + nums[i]);
        }

        //回溯版-增加数组缓存子问题的答案，优化时间负责度为O(N),空间复杂度为O(N)
        int[] cache;

        public int rob2(int[] nums) {
            cache = new int[nums.length];
            Arrays.fill(cache, -1);
            return dfs2(nums.length - 1, nums);
        }

        public int dfs2(int i, int[] nums) {
            if (i < 0) {
                return 0;
            }
            if (cache[i] != -1) {
                return cache[i];
            }
            int max = Math.max(dfs2(i - 1, nums), dfs2(i - 2, nums) + nums[i]);
            cache[i] = max;
            return max;
        }

        //递推版-手动设置初始值
        public int rob3(int[] nums) {
            if (nums == null || nums.length == 0) return 0;
            if (nums.length == 1) return nums[0];
            if (nums.length == 2) return Math.max(nums[0], nums[1]);
            int[] dp = new int[nums.length];
            dp[0] = nums[0];
            dp[1] = Math.max(nums[1], nums[0]);
            for (int i = 2; i < nums.length; i++) {
                dp[i] = Math.max(dp[i - 2] + nums[i], dp[i - 1]);
            }
            return dp[nums.length - 1];
        }

        //递推版-扩大dp数组长度
        public int rob4(int[] nums) {
            int len = nums.length;
            int[] dp = new int[len + 2];
            for (int i = 0; i < len; i++) {
                dp[i + 2] = Math.max(dp[i] + nums[i], dp[i + 1]);
            }
            return dp[dp.length - 1];
        }

        //递推版-空间优化
        public int rob5(int[] nums) {
            int f0 = 0;
            int f1 = 0;
            for (int x : nums) {
                int newF = Math.max(f1, f0 + x);
                f0 = f1;
                f1 = newF;
            }
            return f1;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)

}