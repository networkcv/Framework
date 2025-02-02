/**
 * 某班级学号记录系统发生错乱，原整数学号序列 [1,2,3,4,...] 分隔符丢失后变为 1234... 的字符序列。请实现一个函数返回该字符序列中的第 k 位
 * 数字。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：k = 5
 * 输出：5
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：k = 12
 * 输出：1
 * 解释：第 12 位数字在序列 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, ... 里是 1 ，它是 11 的一部分。
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 0 <= k < 2³¹
 * <p>
 * <p>
 * 注意：本题与主站 400 题相同：https://leetcode-cn.com/problems/nth-digit/
 * <p>
 * <p>
 * <p>
 * Related Topics数学 | 二分查找
 * <p>
 * 👍 369, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */

package com.lwj.algo.leetcode.editor.cn;

class ShuZiXuLieZhongMouYiWeiDeShuZiLcof {
    public static void main(String[] args) {
        Solution solution = new ShuZiXuLieZhongMouYiWeiDeShuZiLcof().new Solution();
        System.out.println(solution.findKthNumber(2147483647));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int findKthNumber(int k) {
            return 0;
        }

        //查找第k位数字，时间复杂度过高
        public int findKthNumber2(int k) {
            //当前总位数
            int n = 0;
            //当前数字
            int num = 1;
            //数字的分位数 十分位，百分位
            int lastNumCount = 0;
            //最后一个加的数字
            int lastNum = 0;
            while (n < k) {
                lastNum = num;
                lastNumCount = (int) Math.log10(lastNum) + 1;
//                lastNumCount = String.valueOf(lastNum).length();
                n = n + lastNumCount;
                num++;
            }
            //加超出之前的位数
            int preN = n - lastNumCount;
            return String.valueOf(lastNum).charAt(k - preN - 1) - '0';
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}