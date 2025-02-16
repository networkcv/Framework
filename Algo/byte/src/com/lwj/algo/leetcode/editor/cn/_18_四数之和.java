package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 给你一个由 n 个整数组成的数组 nums ，和一个目标值 target 。请你找出并返回满足下述全部条件且不重复的四元组 [nums[a], nums[b],
 * nums[c], nums[d]] （若两个四元组元素一一对应，则认为两个四元组重复）：
 * <p>
 * <p>
 * 0 <= a, b, c, d < n
 * a、b、c 和 d 互不相同
 * nums[a] + nums[b] + nums[c] + nums[d] == target
 * <p>
 * <p>
 * 你可以按 任意顺序 返回答案 。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：nums = [1,0,-1,0,-2,2], target = 0
 * 输出：[[-2,-1,1,2],[-2,0,0,2],[-1,0,0,1]]
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：nums = [2,2,2,2,2], target = 8
 * 输出：[[2,2,2,2]]
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 1 <= nums.length <= 200
 * -10⁹ <= nums[i] <= 10⁹
 * -10⁹ <= target <= 10⁹
 * <p>
 * <p>
 * Related Topics数组 | 双指针 | 排序
 * <p>
 * 👍 2024, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class FourSum {
    public static void main(String[] args) {
        Solution solution = new FourSum().new Solution();
//        System.out.println(solution.fourSum(new int[]{1, 0, -1, 0, -2, 2}, 0));
//        System.out.println(solution.fourSum(new int[]{2, 2, 2, 2}, 8));
        System.out.println(solution.fourSum(new int[]{1000000000, 1000000000, 1000000000, 1000000000}, -294967296));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<List<Integer>> fourSum(int[] nums, int target) {
            Arrays.sort(nums);
            List<List<Integer>> res = new ArrayList<>();
            int len = nums.length;
            for (int i = 0; i < len - 3; i++) {
                //防止溢出
                long a = nums[i];
                //跳过重复
                if (i > 0 && a == nums[i - 1]) continue;
                //优化一 最小的四个数相加大于target直接退出整体循环
                if (a + nums[i + 1] + nums[i + 2] + nums[i + 3] > target) break;
                //优化二 当前数+剩下三个最大数小于target 当前数中没有满足条件的集合 退出当前数循环
                if (a + nums[len - 3] + nums[len - 2] + nums[len - 1] < target) continue; //
                for (int j = i + 1; j < len - 2; j++) {
                    long b = nums[j];
                    //跳过重复
                    if (j > i + 1 && b == nums[j - 1]) continue;
                    if (a + b + nums[j + 1] + nums[j + 2] > target) break; // 优化一
                    if (a + b + nums[len - 2] + nums[len - 1] < target) continue; // 优化二
                    int l = j + 1, r = len - 1;
                    while (l < r) {
                        long sum = a + b + nums[l] + nums[r];
                        if (sum == target) {
                            res.add(Arrays.asList((int) a, (int) b, nums[l], nums[r]));
                            for (l++; l < r && nums[l] == nums[l - 1]; l++) ;
                            for (r--; l < r && nums[r] == nums[r + 1]; r--) ;
                        } else if (sum < target) {
                            l++;
                        } else {
                            r--;
                        }
                    }
                }
            }
            return res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}