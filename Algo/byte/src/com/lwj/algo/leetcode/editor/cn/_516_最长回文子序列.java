package com.lwj.algo.leetcode.editor.cn;

//<p>给你一个字符串 <code>s</code> ，找出其中最长的回文子序列，并返回该序列的长度。</p>
//
//<p>子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>s = "bbbab"
//<strong>输出：</strong>4
//<strong>解释：</strong>一个可能的最长回文子序列为 "bbbb" 。
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>s = "cbbd"
//<strong>输出：</strong>2
//<strong>解释：</strong>一个可能的最长回文子序列为 "bb" 。
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= s.length &lt;= 1000</code></li> 
// <li><code>s</code> 仅由小写英文字母组成</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>字符串</li><li>动态规划</li></div></div><br><div><li>👍 1301</li><li>👎 0</li></div>
class LongestPalindromicSubsequence {
    public static void main(String[] args) {
        Solution solution = new LongestPalindromicSubsequence().new Solution();
        System.out.println(solution);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        String s;
        int[][] cache;

        public int longestPalindromeSubseq(String s) {
//            this.cache = new int[s.length()][s.length()];
//            for (int[] ints : cache) {
//                Arrays.fill(ints, -1);
//            }
//            this.s = s;
//            return dfs(0, s.length() - 1);
            return dp0(s);
        }

        //递推版
        public int dp0(String s) {
            int[][] dp = new int[s.length()][s.length()];
            //因为用到了i+1
            for (int i = s.length() - 1; i >= 0; i--) {
                dp[i][i] = 1;
                //if (i > j) return 0; 这个条件要求i要大于j
                for (int j = i + 1; j < s.length(); j++) {
                    if (s.charAt(i) == s.charAt(j)) {
                        dp[i][j] = dp[i + 1][j - 1] + 2;
                    } else {
                        dp[i][j] = Math.max(dp[i + 1][j], dp[i][j - 1]);
                    }
                }
            }
            return dp[0][s.length() - 1];
        }

        //回溯版-记忆化搜索
        public int dfs(int i, int j) {
            if (cache[i][j] != -1) {
                return cache[i][j];
            }
            if (i > j) return 0;
            if (i == j) return 1;
            if (s.charAt(i) == s.charAt(j)) {
                return cache[i][j] = dfs(i + 1, j - 1) + 2;
            }
            return cache[i][j] = Math.max(dfs(i + 1, j), dfs(i, j - 1));
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}