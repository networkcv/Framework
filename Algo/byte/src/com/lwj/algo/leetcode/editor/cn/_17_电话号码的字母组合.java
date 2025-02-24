package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//<p>给定一个仅包含数字&nbsp;<code>2-9</code>&nbsp;的字符串，返回所有它能表示的字母组合。答案可以按 <strong>任意顺序</strong> 返回。</p>
//
//<p>给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。</p>
//
//<p><img src="https://assets.leetcode-cn.com/aliyun-lc-upload/uploads/2021/11/09/200px-telephone-keypad2svg.png" style="width: 200px;" /></p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>digits = "23"
//<strong>输出：</strong>["ad","ae","af","bd","be","bf","cd","ce","cf"]
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>digits = ""
//<strong>输出：</strong>[]
//</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>digits = "2"
//<strong>输出：</strong>["a","b","c"]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>0 &lt;= digits.length &lt;= 4</code></li> 
// <li><code>digits[i]</code> 是范围 <code>['2', '9']</code> 的一个数字。</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>哈希表</li><li>字符串</li><li>回溯</li></div></div><br><div><li>👍 3000</li><li>👎 0</li></div>
class LetterCombinationsOfAPhoneNumber {
    public static void main(String[] args) {
        Solution solution = new LetterCombinationsOfAPhoneNumber().new Solution();
        System.out.println(solution.letterCombinations("23"));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        private String[] letterMap = {
                " ",    //0
                "",     //1
                "abc",  //2
                "def",  //3
                "ghi",  //4
                "jkl",  //5
                "mno",  //6
                "pqrs", //7
                "tuv",  //8
                "wxyz"  //9
        };
        List<String> res = new ArrayList<>();

        public List<String> letterCombinations(String digits) {
            if (digits.isEmpty()) return res;
            List<Integer> digitsList = Arrays.stream(digits.split("")).map(Integer::parseInt).collect(Collectors.toList());
            dfs(0, "", digitsList);
            return res;
        }

        /**
         * @param i          遍历输入按键的索引
         * @param path       当前记录的路径
         * @param digitsList 输入按键列表
         */
        public void dfs(int i, String path, List<Integer> digitsList) {
            if (i == digitsList.size()) {
                res.add(path);
                return;
            }
            String letters = letterMap[digitsList.get(i)];
            for (int j = 0; j < letters.length(); j++) {
                dfs(i + 1, path + letters.charAt(j), digitsList);
            }
        }
    }

//leetcode submit region end(Prohibit modification and deletion)

}