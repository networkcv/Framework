//<p>给定一个包含 <code>n</code> 个整数的数组&nbsp;<code>nums</code>，判断&nbsp;<code>nums</code>&nbsp;中是否存在三个元素&nbsp;<code>a</code> ，<code>b</code> ，<code>c</code> <em>，</em>使得&nbsp;<code>a + b + c = 0</code> ？请找出所有和为 <code>0</code> 且&nbsp;<strong>不重复&nbsp;</strong>的三元组。</p>
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
//<strong>输入：</strong>nums = []
//<strong>输出：</strong>[]
//</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [0]
//<strong>输出：</strong>[]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>0 &lt;= nums.length &lt;= 3000</code></li> 
// <li><code>-10<sup>5</sup> &lt;= nums[i] &lt;= 10<sup>5</sup></code></li> 
//</ul>
//
//<p>&nbsp;</p>
//
//<p>
// <meta charset="UTF-8" />注意：本题与主站 15&nbsp;题相同：<a href="https://leetcode-cn.com/problems/3sum/">https://leetcode-cn.com/problems/3sum/</a></p>
//
//<div><div>Related Topics</div><div><li>数组</li><li>双指针</li><li>排序</li></div></div><br><div><li>👍 78</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class OneFGaJU {
    public static void main(String[] args) {
        Solution solution = new OneFGaJU().new Solution();
        System.out.println(solution.threeSum(new int[]{-1, 0, 1, 2, -1, -4}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<List<Integer>> threeSum(int[] nums) {
            Arrays.sort(nums);
            return nSum(nums, 3, 0, 0);
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
