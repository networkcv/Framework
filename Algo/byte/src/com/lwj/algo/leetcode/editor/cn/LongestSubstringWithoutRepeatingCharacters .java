//<p>给定一个字符串 <code>s</code> ，请你找出其中不含有重复字符的&nbsp;<strong>最长子串&nbsp;</strong>的长度。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例&nbsp;1:</strong></p>
//
//<pre>
//<strong>输入: </strong>s = "abcabcbb"
//<strong>输出: </strong>3 
//<strong>解释:</strong> 因为无重复字符的最长子串是 <span><code>"abc"，所以其</code></span>长度为 3。
//</pre>
//
//<p><strong>示例 2:</strong></p>
//
//<pre>
//<strong>输入: </strong>s = "bbbbb"
//<strong>输出: </strong>1
//<strong>解释: </strong>因为无重复字符的最长子串是 <span><code>"b"</code></span>，所以其长度为 1。
//</pre>
//
//<p><strong>示例 3:</strong></p>
//
//<pre>
//<strong>输入: </strong>s = "pwwkew"
//<strong>输出: </strong>3
//<strong>解释: </strong>因为无重复字符的最长子串是&nbsp;<span><code>"wke"</code></span>，所以其长度为 3。
//&nbsp;    请注意，你的答案必须是 <strong>子串 </strong>的长度，<span><code>"pwke"</code></span>&nbsp;是一个<em>子序列，</em>不是子串。
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>0 &lt;= s.length &lt;= 5 * 10<sup>4</sup></code></li> 
// <li><code>s</code>&nbsp;由英文字母、数字、符号和空格组成</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>哈希表</li><li>字符串</li><li>滑动窗口</li></div></div><br><div><li>👍 7912</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import java.util.HashMap;

class LongestSubstringWithoutRepeatingCharacters {
    public static void main(String[] args) {
        Solution solution = new LongestSubstringWithoutRepeatingCharacters().new Solution();
        System.out.println(solution.lengthOfLongestSubstring("pwwkew"));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int lengthOfLongestSubstring(String s) {
            HashMap<Character, Integer> windows = new HashMap<>();
            int left = 0, right = 0;
            int res = 0;
            while (right < s.length()) {
                char c = s.charAt(right);
                right++;
                windows.merge(c, 1, Integer::sum);
                while (windows.get(c) > 1) {
                    char d = s.charAt(left);
                    left++;
                    windows.computeIfPresent(d, (k, v) -> v - 1);
                }
                res = Math.max(res, right - left);
            }
            return res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}