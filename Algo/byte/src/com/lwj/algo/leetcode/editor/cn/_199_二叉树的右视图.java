package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.TreeNode;

import java.util.ArrayList;
import java.util.List;

//<p>给定一个二叉树的 <strong>根节点</strong> <code>root</code>，想象自己站在它的右侧，按照从顶部到底部的顺序，返回从右侧所能看到的节点值。</p>
//
//<p>&nbsp;</p>
//
//<p><strong class="example">示例 1：</strong></p>
//
//<div class="example-block"> 
// <p><span class="example-io"><b>输入：</b>root = [1,2,3,null,5,null,4]</span></p> 
//</div>
//
//<p><strong>输出：</strong><span class="example-io">[1,3,4]</span></p>
//
//<p><strong>解释：</strong></p>
//
//<p><img alt="" src="https://assets.leetcode.com/uploads/2024/11/24/tmpd5jn43fs-1.png" style="width: 400px; height: 207px;" /></p>
//
//<p><strong class="example">示例 2：</strong></p>
//
//<div class="example-block"> 
// <p><span class="example-io"><b>输入：</b>root = [1,2,3,4,null,null,null,5]</span></p> 
//</div>
//
//<p><span class="example-io"><b>输出：</b>[1,3,4,5]</span></p>
//
//<p><strong>解释：</strong></p>
//
//<p><img alt="" src="https://assets.leetcode.com/uploads/2024/11/24/tmpkpe40xeh-1.png" style="width: 400px; height: 214px;" /></p>
//
//<p><strong class="example">示例 3：</strong></p>
//
//<div class="example-block"> 
// <p><strong>输入：</strong><span class="example-io">root = [1,null,3]</span></p> 
//</div>
//
//<p><strong>输出：</strong><span class="example-io">[1,3]</span></p>
//
//<p><strong class="example">示例 4：</strong></p>
//
//<div class="example-block"> 
// <p><span class="example-io"><b>输入：</b>root = []</span></p> 
//</div>
//
//<p><strong>输出：</strong><span class="example-io">[]</span></p>
//
//<p>&nbsp;</p>
//
//<p><strong>提示:</strong></p>
//
//<ul> 
// <li>二叉树的节点个数的范围是 <code>[0,100]</code></li> 
// <li>
//  <meta charset="UTF-8" /><code>-100&nbsp;&lt;= Node.val &lt;= 100</code>&nbsp;</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>树</li><li>深度优先搜索</li><li>广度优先搜索</li><li>二叉树</li></div></div><br><div><li>👍 1171</li><li>👎 0</li></div>
class BinaryTreeRightSideView {
    public static void main(String[] args) {
        Solution solution = new BinaryTreeRightSideView().new Solution();
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

        public List<Integer> rightSideView(TreeNode root) {
            rightSideView(root, 1);
            return res;
        }


        public void rightSideView(TreeNode root, int level) {
            if (root == null) return;
            if (res.size() < level) {
                res.add(root.val);
            }
            rightSideView(root.right, level + 1);
            rightSideView(root.left, level + 1);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}