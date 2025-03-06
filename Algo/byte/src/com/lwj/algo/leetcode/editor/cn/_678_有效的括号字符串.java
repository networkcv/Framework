package com.lwj.algo.leetcode.editor.cn;

import java.util.Stack;

//<p>给你一个只包含三种字符的字符串，支持的字符类型分别是 <code>'('</code>、<code>')'</code> 和 <code>'*'</code>。请你检验这个字符串是否为有效字符串，如果是 <strong>有效</strong> 字符串返回 <code>true</code> 。</p>
//
//<p><strong>有效</strong> 字符串符合如下规则：</p>
//
//<ul> 
// <li>任何左括号 <code>'('</code>&nbsp;必须有相应的右括号 <code>')'</code>。</li> 
// <li>任何右括号 <code>')'</code>&nbsp;必须有相应的左括号 <code>'('</code>&nbsp;。</li> 
// <li>左括号 <code>'('</code> 必须在对应的右括号之前 <code>')'</code>。</li> 
// <li><code>'*'</code>&nbsp;可以被视为单个右括号 <code>')'</code>&nbsp;，或单个左括号 <code>'('</code>&nbsp;，或一个空字符串 <code>""</code>。</li> 
//</ul>
//
//<p>&nbsp;</p>
//
//<p><strong class="example">示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>s = "()"
//<strong>输出：</strong>true
//</pre>
//
//<p><strong class="example">示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>s = "(*)"
//<strong>输出：</strong>true
//</pre>
//
//<p><strong class="example">示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>s = "(*))"
//<strong>输出：</strong>true
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= s.length &lt;= 100</code></li> 
// <li><code>s[i]</code> 为 <code>'('</code>、<code>')'</code> 或 <code>'*'</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>栈</li><li>贪心</li><li>字符串</li><li>动态规划</li></div></div><br><div><li>👍 656</li><li>👎 0</li></div>
class ValidParenthesisString {
    public static void main(String[] args) {
        Solution solution = new ValidParenthesisString().new Solution();
//        System.out.println(solution.checkValid("( )"));
        System.out.println(solution.checkValidString("(((((*(()((((*((**(((()()*)()()()*((((**)())*)*)))))))(())(()))())((*()()(((()((()*(())*(()**)()(())"));
//        System.out.println(solution.checkValidString("((((()(()()()*()(((((*)"));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean checkValidString(String s) {
            char[] charArray = s.toCharArray();
            System.out.println(s);
            return dfs(charArray);
        }

        //暴力回溯 会超时，需要减枝优化
        public boolean dfs(char[] chars) {
            for (int j = 0; j < chars.length; j++) {
                if (chars[j] == '*') {
                    chars[j] = '(';
                    boolean l = dfs(chars);
                    if (l) return true;
                    chars[j] = ')';
                    boolean r = dfs(chars);
                    if (r) return true;
                    chars[j] = ' ';
                    boolean m = dfs(chars);
                    chars[j] = '*';
                    return m;
                }
            }
            return checkValid(new String(chars));
        }

        public boolean checkValid(String str) {
            Stack<Character> stack = new Stack<>();
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (c == '(') {
                    stack.push(c);
                } else if (c == ')') {
                    if (stack.isEmpty()) return false;
                    stack.pop();
                }
            }
            return stack.isEmpty();
        }

    }
//leetcode submit region end(Prohibit modification and deletion)

}