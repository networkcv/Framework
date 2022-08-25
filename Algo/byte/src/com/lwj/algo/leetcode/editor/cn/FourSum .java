//<p>给你一个由 <code>n</code> 个整数组成的数组&nbsp;<code>nums</code> ，和一个目标值 <code>target</code> 。请你找出并返回满足下述全部条件且<strong>不重复</strong>的四元组&nbsp;<code>[nums[a], nums[b], nums[c], nums[d]]</code>&nbsp;（若两个四元组元素一一对应，则认为两个四元组重复）：</p>
//
//<ul> 
// <li><code>0 &lt;= a, b, c, d&nbsp;&lt; n</code></li> 
// <li><code>a</code>、<code>b</code>、<code>c</code> 和 <code>d</code> <strong>互不相同</strong></li> 
// <li><code>nums[a] + nums[b] + nums[c] + nums[d] == target</code></li> 
//</ul>
//
//<p>你可以按 <strong>任意顺序</strong> 返回答案 。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [1,0,-1,0,-2,2], target = 0
//<strong>输出：</strong>[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [2,2,2,2,2], target = 8
//<strong>输出：</strong>[[2,2,2,2]]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= nums.length &lt;= 200</code></li> 
// <li><code>-10<sup>9</sup> &lt;= nums[i] &lt;= 10<sup>9</sup></code></li> 
// <li><code>-10<sup>9</sup> &lt;= target &lt;= 10<sup>9</sup></code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数组</li><li>双指针</li><li>排序</li></div></div><br><div><li>👍 1343</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class FourSum {
    public static void main(String[] args) {
        Solution solution = new FourSum().new Solution();
        System.out.println(solution.fourSum(new int[]{1000000000, 1000000000, 1000000000, 1000000000}, -294967296));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<List<Integer>> fourSum(int[] nums, int target) {
            Arrays.sort(nums);
            return nSum(nums, 4, 0, target);
        }

        public List<List<Integer>> nSum(int[] nums, int n, int start, long target) {
            int len = nums.length;
            List<List<Integer>> res = new ArrayList<>();
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
                    long l1 = target;
                    long l2 = nums[i];
                    List<List<Integer>> lists = nSum(nums, n - 1, i + 1, l1 - l2);
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