package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.TreeNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//给你一个二叉树的根节点 <code>root</code> ，树中每个节点都存放有一个 <code>0</code> 到 <code>9</code> 之间的数字。
//
//<div class="original__bRMd"> 
// <div> 
//  <p>每条从根节点到叶节点的路径都代表一个数字：</p> 
// </div>
//</div>
//
//<ul> 
// <li>例如，从根节点到叶节点的路径 <code>1 -&gt; 2 -&gt; 3</code> 表示数字 <code>123</code> 。</li> 
//</ul>
//
//<p>计算从根节点到叶节点生成的 <strong>所有数字之和</strong> 。</p>
//
//<p><strong>叶节点</strong> 是指没有子节点的节点。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p> 
//<img alt="" src="https://assets.leetcode.com/uploads/2021/02/19/num1tree.jpg" style="width: 212px; height: 182px;" /> 
//<pre>
//<strong>输入：</strong>root = [1,2,3]
//<strong>输出：</strong>25
//<strong>解释：</strong>
//从根到叶子节点路径 <span><code>1-&gt;2</code></span> 代表数字 <span><code>12</code></span>
//从根到叶子节点路径 <span><code>1-&gt;3</code></span> 代表数字 <span><code>13</code></span>
//因此，数字总和 = 12 + 13 = <span><code>25</code></span></pre>
//
//<p><strong>示例 2：</strong></p> 
//<img alt="" src="https://assets.leetcode.com/uploads/2021/02/19/num2tree.jpg" style="width: 292px; height: 302px;" /> 
//<pre>
//<strong>输入：</strong>root = [4,9,0,5,1]
//<strong>输出：</strong>1026
//<strong>解释：</strong>
//从根到叶子节点路径 <span><code>4-&gt;9-&gt;5</code></span> 代表数字 495
//从根到叶子节点路径 <span><code>4-&gt;9-&gt;1</code></span> 代表数字 491
//从根到叶子节点路径 <span><code>4-&gt;0</code></span> 代表数字 40
//因此，数字总和 = 495 + 491 + 40 = <span><code>1026</code></span>
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li>树中节点的数目在范围 <code>[1, 1000]</code> 内</li> 
// <li><code>0 &lt;= Node.val &lt;= 9</code></li> 
// <li>树的深度不超过 <code>10</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>树</li><li>深度优先搜索</li><li>二叉树</li></div></div><br><div><li>👍 789</li><li>👎 0</li></div>
class SumRootToLeafNumbers {
    public static void main(String[] args) {
        Solution solution = new SumRootToLeafNumbers().new Solution();
        System.out.println(solution);
    }

    //leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     * int val;
     * TreeNode left;
     * TreeNode right;
     * TreeNode() {}
     * TreeNode(int val) { this.val = val; }
     * TreeNode(int val, TreeNode left, TreeNode right) {
     * this.val = val;
     * this.left = left;
     * this.right = right;
     * }
     * }
     */
    class Solution {
        List<Integer> res = new ArrayList<>();

        public int sumNumbers(TreeNode root) {
            sumNumbers0(root, "");
            Optional<Integer> reduce = res.stream().reduce(Integer::sum);
            return reduce.orElse(0);
        }


        public void sumNumbers0(TreeNode root, String numStr) {
            if (root == null) {
                return;
            }
            numStr += root.val;
            if (root.left == null && root.right == null) {
                res.add(Integer.parseInt(numStr));
                return;
            }
            sumNumbers0(root.left, numStr);
            sumNumbers0(root.right, numStr);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}