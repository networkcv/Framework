package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.TreeNode;
import com.lwj.algo.leetcode.editor.cn.utils.TreeNodeUtil;

//<p>给定一个二叉树, 找到该树中两个指定节点的最近公共祖先。</p>
//
//<p><a href="https://baike.baidu.com/item/%E6%9C%80%E8%BF%91%E5%85%AC%E5%85%B1%E7%A5%96%E5%85%88/8918834?fr=aladdin" target="_blank">百度百科</a>中最近公共祖先的定义为：“对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，满足 x 是 p、q 的祖先且 x 的深度尽可能大（<strong>一个节点也可以是它自己的祖先</strong>）。”</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p> 
//<img alt="" src="https://assets.leetcode.com/uploads/2018/12/14/binarytree.png" style="width: 200px; height: 190px;" /> 
//<pre>
//<strong>输入：</strong>root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
//<strong>输出：</strong>3
//<strong>解释：</strong>节点 <span><code>5 </code></span>和节点 <span><code>1 </code></span>的最近公共祖先是节点 <span><code>3 。</code></span>
//</pre>
//
//<p><strong>示例 2：</strong></p> 
//<img alt="" src="https://assets.leetcode.com/uploads/2018/12/14/binarytree.png" style="width: 200px; height: 190px;" /> 
//<pre>
//<strong>输入：</strong>root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 4
//<strong>输出：</strong>5
//<strong>解释：</strong>节点 <span><code>5 </code></span>和节点 <span><code>4 </code></span>的最近公共祖先是节点 <span><code>5 。</code></span>因为根据定义最近公共祖先节点可以为节点本身。
//</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>root = [1,2], p = 1, q = 2
//<strong>输出：</strong>1
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li>树中节点数目在范围 <code>[2, 10<sup>5</sup>]</code> 内。</li> 
// <li><code>-10<sup>9</sup> &lt;= Node.val &lt;= 10<sup>9</sup></code></li> 
// <li>所有 <code>Node.val</code> <code>互不相同</code> 。</li> 
// <li><code>p != q</code></li> 
// <li><code>p</code> 和 <code>q</code> 均存在于给定的二叉树中。</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>树</li><li>深度优先搜索</li><li>二叉树</li></div></div><br><div><li>👍 2904</li><li>👎 0</li></div>
class LowestCommonAncestorOfABinaryTree {
    public static void main(String[] args) {
        Solution solution = new LowestCommonAncestorOfABinaryTree().new Solution();
        TreeNode root = TreeNodeUtil.constructTree(3, 5, 1, 6, 2, 0, 8, null, null, 7, 4);
        System.out.println(solution.lowestCommonAncestor(root, new TreeNode(9), new TreeNode(9)));
    }

    //leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     * int val;
     * TreeNode left;
     * TreeNode right;
     * TreeNode(int x) { val = x; }
     * }
     */
    class Solution {
        public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
            //如果当前节点是目标节点中的一个，那么另外一个节点无论在不在当前节点的子节点中，公共祖先都是当前节点
            if (root == null || root.val == p.val || root.val == q.val) return root;
            //当前节点不是目标节点中的任意一个
            TreeNode leftNodeCommonNode = lowestCommonAncestor(root.left, p, q);
            TreeNode rightNodeCommonNode = lowestCommonAncestor(root.right, p, q);
            if (leftNodeCommonNode != null && rightNodeCommonNode != null) {
                return root;
            } else if (leftNodeCommonNode == null && rightNodeCommonNode != null) {
                //两个节点都在右子树
                return rightNodeCommonNode;
            } else {
                //两个节点都在左子树，或者两个子树都没找到节点时返回null
                return leftNodeCommonNode;
            }
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}