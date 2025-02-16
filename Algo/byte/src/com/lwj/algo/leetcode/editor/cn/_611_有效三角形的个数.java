package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;

/**
 * 给定一个包含非负整数的数组 nums ，返回其中可以组成三角形三条边的三元组个数。
 * <p>
 * <p>
 * <p>
 * 示例 1:
 * <p>
 * <p>
 * 输入: nums = [2,2,3,4]
 * 输出: 3
 * 解释:有效的组合是:
 * 2,3,4 (使用第一个 2)
 * 2,3,4 (使用第二个 2)
 * 2,2,3
 * <p>
 * <p>
 * 示例 2:
 * <p>
 * <p>
 * 输入: nums = [4,2,3,4]
 * 输出: 4
 * <p>
 * <p>
 * <p>
 * 提示:
 * <p>
 * <p>
 * 1 <= nums.length <= 1000
 * 0 <= nums[i] <= 1000
 * <p>
 * <p>
 * Related Topics贪心 | 数组 | 双指针 | 二分查找 | 排序
 * <p>
 * 👍 612, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class ValidTriangleNumber {
    public static void main(String[] args) {
        Solution solution = new ValidTriangleNumber().new Solution();
        System.out.println(solution.triangleNumber(new int[]{2, 2, 3, 4}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        //三角形两边之和大于第三边，需要满足：
        // a+b>c
        // a+c>b
        // b+c>a
        // 假设 1<a<b<c，只需要证明 a+b>c即可
        public int triangleNumber(int[] nums) {
            Arrays.sort(nums);
            int n = nums.length;
            int res = 0;
            for (int i = n - 1; i >= 2; --i) {
                int l = 0, r = i - 1;
                while (l < r) {
                    if (nums[l] + nums[r] > nums[i]) {
                        res += r - l;
                        --r;
                    } else {
                        ++l;
                    }
                }
            }
            return res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}