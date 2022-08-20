//<p>统计一个数字在排序数组中出现的次数。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1:</strong></p>
//
//<pre>
//<strong>输入:</strong> nums = [<span><code>5,7,7,8,8,10]</code></span>, target = 8
//<strong>输出:</strong> 2</pre>
//
//<p><strong>示例&nbsp;2:</strong></p>
//
//<pre>
//<strong>输入:</strong> nums = [<span><code>5,7,7,8,8,10]</code></span>, target = 6
//<strong>输出:</strong> 0</pre>
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
//<p>&nbsp;</p>
//
//<p><strong>注意：</strong>本题与主站 34 题相同（仅返回值不同）：<a href="https://leetcode-cn.com/problems/find-first-and-last-position-of-element-in-sorted-array/">https://leetcode-cn.com/problems/find-first-and-last-position-of-element-in-sorted-array/</a></p>
//
//<div><div>Related Topics</div><div><li>数组</li><li>二分查找</li></div></div><br><div><li>👍 351</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

class ZaiPaiXuShuZuZhongChaZhaoShuZiLcof {
    public static void main(String[] args) {
        Solution solution = new ZaiPaiXuShuZuZhongChaZhaoShuZiLcof().new Solution();
        System.out.println(solution.search(new int[]{5, 7, 7, 8, 8, 10}, 8));
        System.out.println(solution.search(new int[]{5, 7, 7, 8, 8, 10}, 6));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int search(int[] nums, int target) {
            int rightest = searchRightest(nums, target);
            int leftest = searchLeftest(nums, target);
            if (rightest == -1) {
                return 0;
            }
            return rightest - leftest + 1;
        }

        //查找最右边
        private int searchRightest(int[] nums, int target) {
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
            if (l - 1 < 0) {
                return -1;
            }
            return nums[l - 1] == target ? l - 1 : -1;
        }

        //查找最左边
        private int searchLeftest(int[] nums, int target) {
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
    }
//leetcode submit region end(Prohibit modification and deletion)

}