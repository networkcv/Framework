package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;

//<p>给你一个 <strong>非空</strong> 整数数组 <code>nums</code> ，除了某个元素只出现一次以外，其余每个元素均出现两次。找出那个只出现了一次的元素。</p>
//
//<p>你必须设计并实现线性时间复杂度的算法来解决此问题，且该算法只使用常量额外空间。</p>
//
//<div class="original__bRMd"> 
// <div> 
//  <p>&nbsp;</p> 
// </div>
//</div>
//
//<p><strong class="example">示例 1 ：</strong></p>
//
//<div class="example-block"> 
// <p><strong>输入：</strong>nums = [2,2,1]</p> 
//</div>
//
//<p><strong>输出：</strong>1</p>
//
//<p><strong class="example">示例 2 ：</strong></p>
//
//<div class="example-block"> 
// <p><strong>输入：</strong>nums = [4,1,2,1,2]</p> 
//</div>
//
//<p><strong>输出：</strong>4</p>
//
//<p><strong class="example">示例 3 ：</strong></p>
//
//<div class="example-block"> 
// <p><strong>输入：</strong>nums = [1]</p> 
//</div>
//
//<p><strong>输出：</strong>1</p>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= nums.length &lt;= 3 * 10<sup>4</sup></code></li> 
// <li><code>-3 * 10<sup>4</sup> &lt;= nums[i] &lt;= 3 * 10<sup>4</sup></code></li> 
// <li>除了某个元素只出现一次以外，其余每个元素均出现两次。</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>位运算</li><li>数组</li></div></div><br><div><li>👍 3275</li><li>👎 0</li></div>
class SingleNumber {
    public static void main(String[] args) {
        Solution solution = new SingleNumber().new Solution();
        System.out.println(solution.singleNumber(new int[]{1, 1, 4, 1, 2, 1, 2}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int singleNumber(int[] nums) {
            if (nums.length <= 2) return nums[0];
            Arrays.sort(nums);
            int pre1 = nums[0];
            int pre2 = nums[1];
            if (pre1 != pre2) {
                return pre1;
            }
            for (int i = 2; i < nums.length; i++) {
                int cur = nums[i];
                if (pre1 != pre2 && pre2 != cur) {
                    return pre2;
                }
                pre1 = pre2;
                pre2 = cur;
            }
            return pre2;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}