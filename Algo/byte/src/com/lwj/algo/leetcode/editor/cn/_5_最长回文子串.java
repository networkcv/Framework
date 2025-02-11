package com.lwj.algo.leetcode.editor.cn;

/**
 * 给你一个字符串 s，找到 s 中最长的 回文 子串。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：s = "babad"
 * 输出："bab"
 * 解释："aba" 同样是符合题意的答案。
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：s = "cbbd"
 * 输出："bb"
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 1 <= s.length <= 1000
 * s 仅由数字和英文字母组成
 * <p>
 * <p>
 * Related Topics双指针 | 字符串 | 动态规划
 * <p>
 * 👍 7525, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class LongestPalindromicSubstring {
    public static void main(String[] args) {
        Solution solution = new LongestPalindromicSubstring().new Solution();
        System.out.println(solution);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
    }

    public String longestPalindrome(String s) {
        String res = "";
        for (int i = 0; i < s.length(); i++) {
            //奇回文
            int l = i, r = i;
            while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
                l--;
                r++;
            }
            //结束while循环时,l可能是-1，r也可能超出最大长度，所以求有效长度的时候 右索引-左索引+1=(r - 1) - (l - 1) + 1
            if ((r - 1) - (l + 1) + 1 > res.length()) {
                res = s.substring(l + 1, r);
            }
            //偶回文
            l = i;
            r = i + 1;
            while (l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r)) {
                l--;
                r++;
            }
            //结束while循环时,l可能是-1，r也可能超出最大长度，所以求有效长度的时候 右索引-左索引+1=(r - 1) - (l - 1) + 1
            if ((r - 1) - (l + 1) + 1 > res.length()) {
                res = s.substring(l + 1, r);
            }
        }
        return res;
    }
//leetcode submit region end(Prohibit modification and deletion)

}