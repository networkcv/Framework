//<p>给你一个包含 <code>n</code> 个整数的数组&nbsp;<code>nums</code>，判断&nbsp;<code>nums</code>&nbsp;中是否存在三个元素 <em>a，b，c ，</em>使得&nbsp;<em>a + b + c = </em>0 ？请你找出所有和为 <code>0</code> 且不重复的三元组。</p>
//
//<p><strong>注意：</strong>答案中不可以包含重复的三元组。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [-1,0,1,2,-1,-4]
//<strong>输出：</strong>[[-1,-1,2],[-1,0,1]]
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [0,1,1]
//<strong>输出：</strong>[]
//</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [0,0,0]
//<strong>输出：</strong>[[0,0,0]]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>3 &lt;= nums.length &lt;= 3000</code></li> 
// <li><code>-10<sup>5</sup> &lt;= nums[i] &lt;= 10<sup>5</sup></code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数组</li><li>双指针</li><li>排序</li></div></div><br><div><li>👍 5155</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ThreeSum {
    public static void main(String[] args) {
        Solution solution = new ThreeSum().new Solution();
        System.out.println(solution.threeSum(new int[]{-1, 0, 1, 2, -1, -4}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<List<Integer>> threeSum(int[] nums) {
            Arrays.sort(nums);
            return nSum(nums, 3, 0, 0);
        }

        public List<List<Integer>> nSum(int[] nums, int n, int start, int target) {
            int len = nums.length;
            List<List<Integer>> res = new ArrayList<List<Integer>>();
            if (n < 2 || len < n) {
                return res;
            }
            if (n == 2) {
                int l = start, r = len - 1;
                while (l < r) {
                    int left = nums[l];
                    int right = nums[r];
                    if (nums[l] + nums[r] == target) {
                        res.add(new ArrayList<>(Arrays.asList(nums[l], nums[r])));
                        while (l < r && nums[l] == left)
                            l++;
                        while (l < r && nums[r] == right)
                            r--;
                    } else if (nums[l] + nums[r] > target) {
                        while (l < r && nums[r] == right)
                            r--;
                    } else if (nums[l] + nums[r] < target) {
                        while (l < r && nums[l] == left)
                            l++;
                    }
                }
            } else {
                int pre = Integer.MAX_VALUE;
                for (int i = start; i < len; i++) {
                    if (pre == nums[i]) {
                        continue;
                    }
                    List<List<Integer>> lists = nSum(nums, n - 1, i + 1, target - nums[i]);
                    for (List<Integer> list : lists) {
                        list.add(nums[i]);
                        res.add(list);
                    }
                    pre = nums[i];
                }
            }
            return res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}