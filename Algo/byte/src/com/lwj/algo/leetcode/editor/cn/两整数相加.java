/**
 * 给你两个整数 num1 和 num2，返回这两个整数的和。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：num1 = 12, num2 = 5
 * 输出：17
 * 解释：num1 是 12，num2 是 5 ，它们的和是 12 + 5 = 17 ，因此返回 17 。
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：num1 = -10, num2 = 4
 * 输出：-6
 * 解释：num1 + num2 = -6 ，因此返回 -6 。
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * -100 <= num1, num2 <= 100
 * <p>
 * <p>
 * Related Topics数学
 * <p>
 * 👍 302, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */

package com.lwj.algo.leetcode.editor.cn;

class AddTwoIntegers {
    public static void main(String[] args) {
        Solution solution = new AddTwoIntegers().new Solution();
        System.out.println(solution);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int sum(int num1, int num2) {
            return num1 + num2;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}