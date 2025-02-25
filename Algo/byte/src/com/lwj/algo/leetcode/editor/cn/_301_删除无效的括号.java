package com.lwj.algo.leetcode.editor.cn;

import java.util.*;

//<p>给你一个由若干括号和字母组成的字符串 <code>s</code> ，删除最小数量的无效括号，使得输入的字符串有效。</p>
//
//<p>返回所有可能的结果。答案可以按 <strong>任意顺序</strong> 返回。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>s = "()())()"
//<strong>输出：</strong>["(())()","()()()"]
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>s = "(a)())()"
//<strong>输出：</strong>["(a())()","(a)()()"]
//</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>s = ")("
//<strong>输出：</strong>[""]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= s.length &lt;= 25</code></li> 
// <li><code>s</code> 由小写英文字母以及括号 <code>'('</code> 和 <code>')'</code> 组成</li> 
// <li><code>s</code> 中至多含 <code>20</code> 个括号</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>广度优先搜索</li><li>字符串</li><li>回溯</li></div></div><br><div><li>👍 960</li><li>👎 0</li></div>
class RemoveInvalidParentheses {
    public static void main(String[] args) {
        Solution solution = new RemoveInvalidParentheses().new Solution();
//        System.out.println(solution.removeInvalidParentheses("(a)())()"));
        System.out.println(solution.removeInvalidParentheses("()())()"));
//        System.out.println(solution.removeInvalidParentheses("((((((((((((((((((aaaaa))"));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        //分别统计出至少要删除的左右括号数量
        public List<String> removeInvalidParentheses(String s) {
            int[] nums = atLestDelPairNum(s);
            int left = nums[0];
            int right = nums[1];
            dfs(0, left, right, s);
            return new ArrayList<>(res);
        }

        /**
         * 输入视角
         *
         * @param i     当前遍历元素索引
         * @param left  还需删除的左括号数量
         * @param right 还需删除的右括号数量
         */
        public void dfs(int i, int left, int right, String s) {
            if (left == 0 && right == 0) {
                String str = removeStr(s);
                if (checkValid(str)) {
                    res.add(str);
                }
                return;
            }
            if (left + right > s.length() - i) return;//剩余字符数量无法凑够删除数量，直接return
            char c = s.charAt(i);
            //不删
            dfs(i + 1, left, right, s);
            //删左括号
            if (c == '(' && left > 0) {
                pathSet.add(i);
                dfs(i + 1, left - 1, right, s);
                pathSet.remove(i);
            }
            //删右括号
            if (c == ')' && right > 0) {
                pathSet.add(i);
                dfs(i + 1, left, right - 1, s);
                pathSet.remove(i);
            }
        }

        public int[] atLestDelPairNum(String s) {
            int left = 0, right = 0;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '(') {
                    left++;
                } else if (c == ')') {
                    if (left == 0) {
                        right++;
                    } else {
                        left--;
                    }
                }
            }
            return new int[]{left, right};
        }

        //该方法剪枝效果不好，时间复杂度高，可以考虑在考虑删/不删的时候增加对括号的判断，如果当前是左括号，且大于至少还要删除的左括号数量
        public List<String> removeInvalidParentheses0(String s) {
            int d = atLestDelNum(s);
            int targetLen = s.length() - d;
            dfs(0, s, targetLen);
            return new ArrayList<>(res);
        }

        Set<String> res = new HashSet<>();

        //记录每个节点是否删除
        LinkedList<Integer> path = new LinkedList<>();
        HashSet<Integer> pathSet = new HashSet<>();

        /**
         * 输入视角
         * 判断当前索引位置元素是否删除
         *
         * @param i         字符串索引位置
         * @param s         目标字符串
         * @param targetLen 满足最少删除的字符串长度
         */
        public void dfs(int i, String s, int targetLen) {
            int remain = s.length() - pathSet.size();
            if (remain < targetLen) {
                return;
            }
            if (i == s.length()) {
                String str = removeStr(s);
                if (checkValid(str) && targetLen == str.length()) {
                    res.add(str);
                }
                return;
            }
            //不删
            dfs(i + 1, s, targetLen);
            //删
            pathSet.add(i);
            dfs(i + 1, s, targetLen);
            pathSet.remove(i);
        }


        //主要需考虑")("这种情况
        public int atLestDelNum(String s) {
            Stack<Character> stack = new Stack<>();
            int count = 0;
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (c == '(') {
                    stack.push(c);
                } else if (c == ')') {
                    if (!stack.isEmpty() && stack.peek() == '(') {
                        stack.pop();
                    } else {
                        count++;
                    }
                }
            }
            return count + stack.size();
        }


        public String removeStr(String s) {
            StringBuilder res = new StringBuilder();
            for (int i = 0; i < s.length(); i++) {
                if (!pathSet.contains(i)) {
                    res.append(s.charAt(i));
                }
            }
            return res.toString();
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