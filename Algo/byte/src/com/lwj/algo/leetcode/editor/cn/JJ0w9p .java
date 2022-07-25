//给定一个非负整数 x ，计算并返回 x 的平方根，即实现 int sqrt(int x) 函数。 
//
// 正数的平方根有两个，只输出其中的正数平方根。 
//
// 如果平方根不是整数，输出只保留整数的部分，小数部分将被舍去。 
//
// 
//
// 示例 1: 
//
// 
//输入: x = 4
//输出: 2
// 
//
// 示例 2: 
//
// 
//输入: x = 8
//输出: 2
//解释: 8 的平方根是 2.82842...，由于小数部分将被舍去，所以返回 2
// 
//
// 
//
// 提示: 
//
// 
// 0 <= x <= 2³¹ - 1 
// 
//
// 
//
// 注意：本题与主站 69 题相同： https://leetcode-cn.com/problems/sqrtx/ 
// Related Topics 数学 二分查找 👍 29 👎 0


package com.lwj.algo.leetcode.editor.cn;

class JJ0w9p {
    public static void main(String[] args) {
        Solution solution = new JJ0w9p().new Solution();
        System.out.println(solution.mySqrt(8));
        System.out.println(solution.mySqrt(0));
//        System.out.println(solution.mySqrt2(10));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int mySqrt2(int x) {
            int left = 0, right = x;
            while (left < right) {
                // 为了防止 mid 为 0 , 在后面加1
                int mid = left + (right - left) / 2 + 1;
                // 通过除法 x/mid 来判断比用乘法好, 乘法容易溢出
                if (x / mid >= mid) {
                    // 由于 x 的平方根是向下取整的, 所以当 x/mid >= mid, 也就是 mid^2 <= x,
                    // 有可能 mid 就是平方根, 所以 left = mid, 而不是 mid + 1
                    left = mid;
                } else {
                    right = mid - 1;
                }
            }
            return left;
        }

        public int mySqrt(int x) {
            int left = 1, right = x/2+1;
            while (left <= right) {
                int mid = left + (right - left) / 2;
                if (mid < x / mid) {
                    left = mid + 1;
                } else if (mid > x / mid) {
                    right = mid - 1;
                } else {
                    return mid;
                }
            }
            return right;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}