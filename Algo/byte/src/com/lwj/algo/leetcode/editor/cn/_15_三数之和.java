package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//给你一个整数数组 nums ，判断是否存在三元组 [nums[i], nums[j], nums[k]] 满足 i != j、i != k 且 j != 
//k ，同时还满足 nums[i] + nums[j] + nums[k] == 0 。请你返回所有和为 0 且不重复的三元组。 
//
// 注意：答案中不可以包含重复的三元组。 
//
// 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [-1,0,1,2,-1,-4]
//输出：[[-1,-1,2],[-1,0,1]]
//解释：
//nums[0] + nums[1] + nums[2] = (-1) + 0 + 1 = 0 。
//nums[1] + nums[2] + nums[4] = 0 + 1 + (-1) = 0 。
//nums[0] + nums[3] + nums[4] = (-1) + 2 + (-1) = 0 。
//不同的三元组是 [-1,0,1] 和 [-1,-1,2] 。
//注意，输出的顺序和三元组的顺序并不重要。
// 
//
// 示例 2： 
//
// 
//输入：nums = [0,1,1]
//输出：[]
//解释：唯一可能的三元组和不为 0 。
// 
//
// 示例 3： 
//
// 
//输入：nums = [0,0,0]
//输出：[[0,0,0]]
//解释：唯一可能的三元组和为 0 。
// 
//
// 
//
// 提示： 
//
// 
// 3 <= nums.length <= 3000 
// -10⁵ <= nums[i] <= 10⁵ 
// 
//
// Related Topics数组 | 双指针 | 排序 
//
// 👍 7267, 👎 0bug 反馈 | 使用指南 | 更多配套插件 
//
//
//
//

class ThreeSum {
    public static void main(String[] args) {
        Solution solution = new ThreeSum().new Solution();
        System.out.println(solution.threeSum(new int[]{-1, 0, 1, 2, -1, -4}));
//        System.out.println(solution.threeSum(new int[]{-1, 0, 0, 1, 1}));
//        System.out.println(solution.threeSum(new int[]{-1, 0, 1}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<List<Integer>> threeSum(int[] nums) {
            int len = nums.length;
            Arrays.sort(nums);
            List<List<Integer>> res = new ArrayList<>();
            //只遍历到倒数第三个数字
            for (int i = 0; i < len - 2; i++) {
                int l = i + 1;
                int r = len - 1;
                //确保最外层数字不会重复遍历
                if (i > 0 && nums[i] == nums[i - 1]) {
                    continue;
                }
                //可进行的优化点1 最小的三个数都大于0，那后边的数肯定加起来都大于0，这里可以直接退出循环
                //可进行的优化点2 当前数加上最大的两个数都小于0，那说明当前nums[i]不存在满足条件三元组
                while (l < r) {
                    if (nums[i] + nums[l] + nums[r] == 0) {
                        res.add(Arrays.asList(nums[i], nums[l], nums[r]));
                        //考虑到这种情况 [-1, 0, 0, 1, 1] 会将 [-1，0，1],这个结果添加两次，所以这里需要将下标移动到不等于当前值的位置
                        int curL = nums[l];
                        int curR = nums[r];
                        while (l < r && curL == nums[l]) {
                            l++;
                        }
                        while (l < r && curR == nums[r]) {
                            r--;
                        }
                    } else if (nums[i] + nums[l] + nums[r] > 0) {
                        r--;
                    } else {
                        l++;
                    }
                }
            }
            return res;
        }

        public List<List<Integer>> threeSum1(int[] nums) {
            //a+b+c=target
            Arrays.sort(nums);
            List<List<Integer>> res = new ArrayList<>();
            int len = nums.length;
            for (int i = 0; i < len - 2; i++) {
                //确保最外层数字不会重复遍历
                if (i > 0 && nums[i] == nums[i - 1]) {
                    continue;
                }
                //优化点1 最小的三个数都大于0，那后边的数肯定加起来都大于0，这里可以直接退出循环
                if (nums[i] + nums[i + 1] + nums[i + 2] > 0) {
                    break;
                }
                //优化点2 当前数加上最大的两个数都小于0，那说明当前nums[i]不存在满足条件三元组
                if (nums[i] + nums[len - 1] + nums[len - 2] < 0) {
                    continue;
                }
                int l = i + 1;
                int r = len - 1;
                while (l < r) {
                    int sum = nums[l] + nums[r] + nums[i];
                    if (sum == 0) {
                        res.add(Arrays.asList(nums[i], nums[l], nums[r]));
                        int v = nums[l];
                        //确保中间的数字和之前不一致，这样最终结果肯定和之前的不一致
                        for (l++; l < r && nums[l] == nums[l - 1]; l++) ;
                        for (r--; l < r && nums[r] == nums[r + 1]; r--) ;
                    } else if (sum < 0) {
                        l++;
                    } else {
                        r--;
                    }
                }
            }
            return res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}