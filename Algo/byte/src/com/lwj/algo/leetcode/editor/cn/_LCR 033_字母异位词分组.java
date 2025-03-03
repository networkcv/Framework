package com.lwj.algo.leetcode.editor.cn;

import java.util.*;

//<p>给定一个字符串数组 <code>strs</code> ，将&nbsp;<strong>变位词&nbsp;</strong>组合在一起。 可以按任意顺序返回结果列表。</p>
//
//<p><strong>注意：</strong>若两个字符串中每个字符出现的次数都相同，则称它们互为变位词。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入:</strong> strs = <span><code>["eat", "tea", "tan", "ate", "nat", "bat"]</code></span>
//<strong>输出: </strong>[["bat"],["nat","tan"],["ate","eat","tea"]]</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入:</strong> strs = <span><code>[""]</code></span>
//<strong>输出: </strong>[[""]]
//</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入:</strong> strs = <span><code>["a"]</code></span>
//<strong>输出: </strong>[["a"]]</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= strs.length &lt;= 10<sup>4</sup></code></li> 
// <li><code>0 &lt;= strs[i].length &lt;= 100</code></li> 
// <li><code>strs[i]</code>&nbsp;仅包含小写字母</li> 
//</ul>
//
//<p>&nbsp;</p>
//
//<p>
// <meta charset="UTF-8" />注意：本题与主站 49&nbsp;题相同：&nbsp;<a href="https://leetcode-cn.com/problems/group-anagrams/">https://leetcode-cn.com/problems/group-anagrams/</a></p>
//
//<div><div>Related Topics</div><div><li>数组</li><li>哈希表</li><li>字符串</li><li>排序</li></div></div><br><div><li>👍 68</li><li>👎 0</li></div>
class Sfvd7V {
    public static void main(String[] args) {
        Solution solution = new Sfvd7V().new Solution();
        System.out.println(solution.groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public List<List<String>> groupAnagrams(String[] strs) {
            Map<String, List<String>> res = new HashMap<>();
            for (String str : strs) {
                String sortStr = sortString(str);
                List<String> subList = res.getOrDefault(sortStr, new ArrayList<>());
                subList.add(str);
                res.put(sortStr, subList);
            }
            return new ArrayList<>(res.values());
        }

        public String sortString(String s) {
            char[] charArray = s.toCharArray();
            Arrays.sort(charArray);
            return new String(charArray);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}