//<p>给定两个字符串&nbsp;<code>s</code>&nbsp;和 <code>p</code>，找到&nbsp;<code>s</code><strong>&nbsp;</strong>中所有&nbsp;<code>p</code><strong>&nbsp;</strong>的&nbsp;<strong>异位词&nbsp;</strong>的子串，返回这些子串的起始索引。不考虑答案输出的顺序。</p>
//
//<p><strong>异位词 </strong>指由相同字母重排列形成的字符串（包括相同的字符串）。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例&nbsp;1:</strong></p>
//
//<pre>
//<strong>输入: </strong>s = "cbaebabacd", p = "abc"
//<strong>输出: </strong>[0,6]
//<strong>解释:</strong>
//起始索引等于 0 的子串是 "cba", 它是 "abc" 的异位词。
//起始索引等于 6 的子串是 "bac", 它是 "abc" 的异位词。
//</pre>
//
//<p><strong>&nbsp;示例 2:</strong></p>
//
//<pre>
//<strong>输入: </strong>s = "abab", p = "ab"
//<strong>输出: </strong>[0,1,2]
//<strong>解释:</strong>
//起始索引等于 0 的子串是 "ab", 它是 "ab" 的异位词。
//起始索引等于 1 的子串是 "ba", 它是 "ab" 的异位词。
//起始索引等于 2 的子串是 "ab", 它是 "ab" 的异位词。
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示:</strong></p>
//
//<ul> 
// <li><code>1 &lt;= s.length, p.length &lt;= 3 * 10<sup>4</sup></code></li> 
// <li><code>s</code>&nbsp;和&nbsp;<code>p</code>&nbsp;仅包含小写字母</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>哈希表</li><li>字符串</li><li>滑动窗口</li></div></div><br><div><li>👍 958</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class FindAllAnagramsInAString {
    public static void main(String[] args) {
        Solution solution = new FindAllAnagramsInAString().new Solution();
        System.out.println(solution.findAnagrams("cbaebabacd", "abc"));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<Integer> findAnagrams(String s, String p) {
            ArrayList<Integer> res = new ArrayList<>();
            HashMap<Character, Integer> need = new HashMap<>();
            HashMap<Character, Integer> windows = new HashMap<>();
            for (char c : p.toCharArray()) {
                need.merge(c, 1, Integer::sum);
            }
            int left = 0, right = 0, valid = 0;
            while (right < s.length()) {
                char c = s.charAt(right);
                right++;
                if (need.containsKey(c)) {
                    windows.merge(c, 1, Integer::sum);
                    if (need.get(c).equals(windows.get(c))) {
                        valid++;
                    }
                }
                while (right - left >= p.length()) {
                    if (valid == need.size()) {
                        res.add(left);
                    }
                    char d = s.charAt(left);
                    left++;
                    if (need.containsKey(d)) {
                        if (need.get(d).equals(windows.get(d))) {
                            valid--;
                        }
                        windows.computeIfPresent(d, (k, v) -> v - 1);
                    }
                }
            }
            return res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}