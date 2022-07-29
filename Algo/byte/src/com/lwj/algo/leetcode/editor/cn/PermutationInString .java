//<p>给你两个字符串&nbsp;<code>s1</code>&nbsp;和&nbsp;<code>s2</code> ，写一个函数来判断 <code>s2</code> 是否包含 <code>s1</code><strong>&nbsp;</strong>的排列。如果是，返回 <code>true</code> ；否则，返回 <code>false</code> 。</p>
//
//<p>换句话说，<code>s1</code> 的排列之一是 <code>s2</code> 的 <strong>子串</strong> 。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>s1 = "ab" s2 = "eidbaooo"
//<strong>输出：</strong>true
//<strong>解释：</strong>s2 包含 s1 的排列之一 ("ba").
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>s1= "ab" s2 = "eidboaoo"
//<strong>输出：</strong>false
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= s1.length, s2.length &lt;= 10<sup>4</sup></code></li> 
// <li><code>s1</code> 和 <code>s2</code> 仅包含小写字母</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>哈希表</li><li>双指针</li><li>字符串</li><li>滑动窗口</li></div></div><br><div><li>👍 729</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import java.util.HashMap;

class PermutationInString {
    public static void main(String[] args) {
        Solution solution = new PermutationInString().new Solution();

//        System.out.println(solution.checkInclusion("ab", "eidbaooo"));
        System.out.println(solution.checkInclusion("ab", "eidboaooo"));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean checkInclusion(String t, String s) {
            HashMap<Character, Integer> need = new HashMap<>();
            HashMap<Character, Integer> windows = new HashMap<>();
            for (Character cs : t.toCharArray()) {
                need.merge(cs, 1, Integer::sum);
            }
            int left = 0, right = 0, valid = 0;
            while (right < s.length()) {
                char c = s.charAt(right);
                right++;
                if (need.containsKey(c)) {
                    windows.merge(c, 1, Integer::sum);
                    if (windows.get(c).equals(need.get(c))) {
                        valid++;
                    }
                }
                while (right - left >= t.length()) {
                    if (valid == need.size()) {
                        return true;
                    }
                    char d = s.charAt(left);
                    left++;
                    if (need.containsKey(d)) {
                        if (windows.get(d).equals(need.get(d))) {
                            valid--;
                        }
                        windows.computeIfPresent(d, (k, v) -> v - 1);
                    }
                }
            }
            return false;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}