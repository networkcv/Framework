//<p>给你一个按照非递减顺序排列的整数数组 <code>nums</code>，和一个目标值 <code>target</code>。请你找出给定目标值在数组中的开始位置和结束位置。</p>
//
//<p>如果数组中不存在目标值 <code>target</code>，返回&nbsp;<code>[-1, -1]</code>。</p>
//
//<p>你必须设计并实现时间复杂度为&nbsp;<code>O(log n)</code>&nbsp;的算法解决此问题。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [<span><code>5,7,7,8,8,10]</code></span>, target = 8
//<strong>输出：</strong>[3,4]</pre>
//
//<p><strong>示例&nbsp;2：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [<span><code>5,7,7,8,8,10]</code></span>, target = 6
//<strong>输出：</strong>[-1,-1]</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>nums = [], target = 0
//<strong>输出：</strong>[-1,-1]</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>0 &lt;= nums.length &lt;= 10<sup>5</sup></code></li> 
// <li><code>-10<sup>9</sup>&nbsp;&lt;= nums[i]&nbsp;&lt;= 10<sup>9</sup></code></li> 
// <li><code>nums</code>&nbsp;是一个非递减数组</li> 
// <li><code>-10<sup>9</sup>&nbsp;&lt;= target&nbsp;&lt;= 10<sup>9</sup></code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数组</li><li>二分查找</li></div></div><br><div><li>👍 1862</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;

class FindFirstAndLastPositionOfElementInSortedArray {
    public static void main(String[] args) {
        Solution solution = new FindFirstAndLastPositionOfElementInSortedArray().new Solution();

//        System.out.println(solution.searchRightest(new int[]{2, 2}, 3));
//        System.out.println(solution.searchRightest(new int[]{2, 2}, 1));
//        System.out.println(solution.searchRightest(new int[]{1, 1, 2, 2, 2, 3, 4}, 2));

        System.out.println(Arrays.toString(solution.searchRange(new int[]{1}, 1))); //[0,0]
        System.out.println(Arrays.toString(solution.searchRange(new int[]{2, 2}, 3))); //[-1,-1]
        System.out.println(Arrays.toString(solution.searchRange(new int[]{1, 1, 2, 2, 2, 3, 4}, 2))); //[2,4]
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[] searchRange1(int[] nums, int target) {
            if (nums.length == 0) {
                return new int[]{-1, -1};
            }
            int l = 0, r = nums.length - 1, mid;
            while (l <= r) {
                mid = (l + r) / 2;
                if (nums[mid] == target) {
                    r = mid - 1;
                } else if (nums[mid] > target) {
                    r = mid - 1;
                } else if (nums[mid] < target) {
                    l = mid + 1;
                }
            }
            if (l == nums.length || nums[l] != target) {
                return new int[]{-1, -1};
            }
            r = l;
            while (r + 1 < nums.length && nums[r + 1] == target) {
                r++;
            }
            return new int[]{l, r};
        }

        public int[] searchRange(int[] nums, int target) {
            int leftest = searchLeftest(nums, target);
            int rightest = searchRightest(nums, target);
            return new int[]{leftest, rightest};
        }

        public int searchLeftest(int[] nums, int target) {
            int l = 0, r = nums.length - 1, mid;
            while (l <= r) {
                mid = (l + r) / 2;
                if (nums[mid] == target) {
                    r = mid - 1;
                } else if (nums[mid] > target) {
                    r = mid - 1;
                } else if (nums[mid] < target) {
                    l = mid + 1;
                }
            }
            if (l == nums.length) {
                return -1;
            }
            return nums[l] == target ? l : -1;
        }

        public int searchRightest(int[] nums, int target) {
            int l = 0, r = nums.length - 1, mid;
            while (l <= r) {
                mid = (l + r) / 2;
                if (nums[mid] == target) {
                    l = mid + 1;
                } else if (nums[mid] > target) {
                    r = mid - 1;
                } else if (nums[mid] < target) {
                    l = mid + 1;
                }
            }
            // 此时 left - 1 索引越界
            if (l - 1 < 0) {
                return -1;
            }
            // 判断一下 nums[left] 是不是 target
            return nums[l - 1] == target ? (l - 1) : -1;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}