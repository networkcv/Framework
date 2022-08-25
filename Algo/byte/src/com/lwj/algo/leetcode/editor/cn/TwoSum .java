////////给定一个整数数组 nums 和一个整数目标值 target，请你在该数组中找出 和为目标值 target 的那 两个 整数，并返回它们的数组下标
//。 
////
////////
//////// 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。 
////////
//////// 你可以按任意顺序返回答案。 
////////
//////// 
////////
//////// 示例 1： 
////////
//////// 
////////输入：nums = [2,7,11,15], target = 9
////////输出：[0,1]
////////解释：因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。
//////// 
////////
//////// 示例 2： 
////////
//////// 
////////输入：nums = [3,2,4], target = 6
////////输出：[1,2]
//////// 
////////
//////// 示例 3： 
////////
//////// 
////////输入：nums = [3,3], target = 6
////////输出：[0,1]
//////// 
////////
//////// 
////////
//////// 提示： 
////////
//////// 
//////// 2 <= nums.length <= 10⁴ 
//////// -10⁹ <= nums[i] <= 10⁹ 
//////// -10⁹ <= target <= 10⁹ 
//////// 只会存在一个有效答案 
//////// 
////////
//////// 进阶：你可以想出一个时间复杂度小于 O(n²) 的算法吗？ 
//////// Related Topics 数组 哈希表 👍 13975 👎 0
//////
////
//


package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;
import java.util.HashMap;

class TwoSum {
    public static void main(String[] args) {
        Solution solution = new TwoSum().new Solution();
        System.out.println(Arrays.toString(solution.doublePoint(new int[]{2, 7, 11, 15}, 9)));
        System.out.println(Arrays.toString(solution.doublePoint(new int[]{3, 2, 4}, 6)));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[] twoSum(int[] nums, int target) {
            //1.迭代
//            return iteration(nums, target);
            //2.hash
            return hash(nums, target);
            //3.双指针 需要有序数组
//            return doublePoint(nums, target);
        }

        private int[] doublePoint(int[] nums, int target) {
            int l = 0, r = nums.length - 1;
            while (l < r) {
                int cur = nums[l] + nums[r];
                if (cur == target) {
                    return new int[]{l, r};
                } else if (cur > target) {
                    r--;
                } else {
                    l++;
                }
            }
            return new int[]{-1, -1};
        }

        private int[] hash(int[] nums, int target) {
            HashMap<Integer, Integer> hashMap = new HashMap<>();
            for (int i = 0; i < nums.length; i++) {
                int num = target - nums[i];
                if (hashMap.containsKey(num)) {
                    return new int[]{hashMap.get(num), i};
                }
                hashMap.put(nums[i], i);
            }
            return null;
        }

        private int[] iteration(int[] nums, int target) {
            for (int i = 0; i < nums.length; i++) {
                for (int j = 0; j < nums.length; j++) {
                    if (nums[i] + nums[j] == target) {
                        return new int[]{i, j};
                    }
                }
            }
            return null;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}