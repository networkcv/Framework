/**
 * 给定两个排序后的数组 A 和 B，其中 A 的末端有足够的缓冲空间容纳 B。 编写一个方法，将 B 合并入 A 并排序。
 * <p>
 * 初始化 A 和 B 的元素数量分别为 m 和 n。
 * <p>
 * 示例：
 * <p>
 * <p>
 * 输入：
 * A = [1,2,3,0,0,0], m = 3
 * B = [2,5,6],       n = 3
 * <p>
 * 输出： [1,2,2,3,5,6]
 * <p>
 * 说明：
 * <p>
 * <p>
 * A.length == n + m
 * <p>
 * <p>
 * Related Topics数组 | 双指针 | 排序
 * <p>
 * 👍 182, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */

package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ArraysUtil;

class SortedMergeLcci {
    public static void main(String[] args) {
        Solution solution = new SortedMergeLcci().new Solution();
        int[] a = {0};
        int[] b = {1};
        solution.merge(a, a.length, b, b.length);
        ArraysUtil.print(a);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public void merge(int[] A, int m, int[] B, int n) {
            int[] a = A;
            int[] b = B;
            int ai = m - 1;
            int bi = n - 1;
            for (int i = A.length - 1; i >= 0; i--) {
                int tmp;
                if (ai >= 0 && bi >= 0) {
                    if (a[ai] > b[bi]) {
                        tmp = a[ai];
                        ai--;
                    } else {
                        tmp = b[bi];
                        bi--;
                    }
                } else if (ai < 0) {
                    tmp = b[bi];
                    bi--;
                } else {
                    tmp = a[ai];
                    ai--;
                }
                a[i] = tmp;
            }
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}