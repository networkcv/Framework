package com.lwj.algo.leetcode.editor.cn;

//<p>给定两个字符串&nbsp;<code>text1</code> 和&nbsp;<code>text2</code>，返回这两个字符串的最长 <strong>公共子序列</strong> 的长度。如果不存在 <strong>公共子序列</strong> ，返回 <code>0</code> 。</p>
//
//<p>一个字符串的&nbsp;<strong>子序列</strong><em>&nbsp;</em>是指这样一个新的字符串：它是由原字符串在不改变字符的相对顺序的情况下删除某些字符（也可以不删除任何字符）后组成的新字符串。</p>
//
//<ul> 
// <li>例如，<code>"ace"</code> 是 <code>"abcde"</code> 的子序列，但 <code>"aec"</code> 不是 <code>"abcde"</code> 的子序列。</li> 
//</ul>
//
//<p>两个字符串的 <strong>公共子序列</strong> 是这两个字符串所共同拥有的子序列。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>text1 = "abcde", text2 = "ace" 
//<strong>输出：</strong>3  
//<strong>解释：</strong>最长公共子序列是 "ace" ，它的长度为 3 。
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>text1 = "abc", text2 = "abc"
//<strong>输出：</strong>3
//<strong>解释：</strong>最长公共子序列是 "abc" ，它的长度为 3 。
//</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>text1 = "abc", text2 = "def"
//<strong>输出：</strong>0
//<strong>解释：</strong>两个字符串没有公共子序列，返回 0 。
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= text1.length, text2.length &lt;= 1000</code></li> 
// <li><code>text1</code> 和&nbsp;<code>text2</code> 仅由小写英文字符组成。</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>字符串</li><li>动态规划</li></div></div><br><div><li>👍 1693</li><li>👎 0</li></div>
class LongestCommonSubsequence {
    public static void main(String[] args) {
        Solution solution = new LongestCommonSubsequence().new Solution();
//        System.out.println(solution.longestCommonSubsequence("abcde", "ace"));
        System.out.println(solution.longestCommonSubsequence("abdd", "ad"));
//        System.out.println(solution.longestCommonSubsequence("ylqpejqbalahwr", "yrkzavgdmdgtqpg")); //3
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        String text1, text2;
        int[][] cache;

        public int longestCommonSubsequence(String text1, String text2) {
            this.text1 = text1;
            this.text2 = text2;
//            return dfs0(text1.length() - 1, text2.length() - 1);

//            cache = new int[text1.length() + 1][text2.length() + 1];
//            for (int[] ints : cache) {
//                Arrays.fill(ints, -1);
//
//            }
//            return dfs1(text1.length() - 1, text2.length() - 1);

//            return dp1(text1, text2);
            return dp2(text1, text2);
        }


        //递推版-一纬度数组
        public int dp2(String text1, String text2) {
            char[] t = text2.toCharArray();
            int m = t.length;
            int[] f = new int[m + 1];
            for (char x : text1.toCharArray()) {
                int pre = 0; // f[0]
                for (int j = 0; j < m; j++) {
                    int tmp = f[j + 1];
                    f[j + 1] = x == t[j] ? pre + 1 : Math.max(f[j + 1], f[j]);
                    pre = tmp;
                }
            }
            return f[m];
        }

        //递推版-两行二维数组
        public int dp1(String text1, String text2) {
            int len1 = text1.length();
            int len2 = text2.length();
            int[][] dp = new int[2][len2 + 1];
            dp[0][0] = 0;
            for (int i = 0; i < len1; i++) {
                for (int j = 0; j < len2; j++) {
                    char c1 = text1.charAt(i);
                    char c2 = text2.charAt(j);
                    if (c1 == c2) {
                        dp[(i + 1) % 2][j + 1] = dp[i % 2][j] + 1;
                    } else {
                        dp[(i + 1) % 2][j + 1] = Math.max(dp[i % 2][j + 1], dp[(i + 1) % 2][j]);
                    }
                }
            }
            return dp[len1 % 2][len2];
        }

        //递推版-二维数组
        public int dp0(String text1, String text2) {
            int len1 = text1.length();
            int len2 = text2.length();
            int[][] dp = new int[len1 + 1][len2 + 1];
            dp[0][0] = 0;
            for (int i = 0; i < len1; i++) {
                for (int j = 0; j < len2; j++) {
                    char c1 = text1.charAt(i);
                    char c2 = text2.charAt(j);
                    if (c1 == c2) {
                        dp[i + 1][j + 1] = dp[i][j] + 1;
                    } else {
                        dp[i + 1][j + 1] = Math.max(dp[i][j + 1], dp[i + 1][j]);
                    }
                }
            }
            return dp[len1][len2];
        }

        //回溯版-记忆化搜索
        public int dfs1(int cur1, int cur2) {
            if (cur1 < 0 || cur2 < 0) return 0;

            if (cache[cur1][cur2] != -1) {
                return cache[cur1][cur2];
            }
            char c1 = text1.charAt(cur1);
            char c2 = text2.charAt(cur2);
            if (c1 == c2) {
                return dfs1(cur1 - 1, cur2 - 1) + 1;
            }
            return cache[cur1][cur2] = Math.max(dfs1(cur1 - 1, cur2), dfs1(cur1, cur2 - 1));
        }

        //回溯版-暴力解法
        public int dfs0(int cur1, int cur2) {
            if (cur1 < 0 || cur2 < 0) return 0;
            char c1 = text1.charAt(cur1);
            char c2 = text2.charAt(cur2);
            if (c1 == c2) {
                return dfs0(cur1 - 1, cur2 - 1) + 1;
            }
            return Math.max(dfs0(cur1 - 1, cur2), dfs0(cur1, cur2 - 1));
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}