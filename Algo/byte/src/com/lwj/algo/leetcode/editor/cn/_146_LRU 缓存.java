package com.lwj.algo.leetcode.editor.cn;

import java.util.HashMap;
import java.util.Map;

/**
 * 请你设计并实现一个满足
 * LRU (最近最少使用) 缓存 约束的数据结构。
 * <p>
 * <p>
 * <p>
 * 实现
 * LRUCache 类：
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * LRUCache(int capacity) 以 正整数 作为容量 capacity 初始化 LRU 缓存
 * int get(int key) 如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1 。
 * void put(int key, int value) 如果关键字 key 已经存在，则变更其数据值 value ；如果不存在，则向缓存中插入该组 key-
 * value 。如果插入操作导致关键字数量超过 capacity ，则应该 逐出 最久未使用的关键字。
 * <p>
 * <p>
 * <p>
 * <p>
 * 函数 get 和 put 必须以 O(1) 的平均时间复杂度运行。
 * <p>
 * <p>
 * <p>
 * 示例：
 * <p>
 * <p>
 * 输入
 * ["LRUCache", "put", "put", "get", "put", "get", "put", "get", "get", "get"]
 * [[2], [1, 1], [2, 2], [1], [3, 3], [2], [4, 4], [1], [3], [4]]
 * 输出
 * [null, null, null, 1, null, -1, null, -1, 3, 4]
 * <p>
 * 解释
 * LRUCache lRUCache = new LRUCache(2);
 * lRUCache.put(1, 1); // 缓存是 {1=1}
 * lRUCache.put(2, 2); // 缓存是 {1=1, 2=2}
 * lRUCache.get(1);    // 返回 1
 * lRUCache.put(3, 3); // 该操作会使得关键字 2 作废，缓存是 {1=1, 3=3}
 * lRUCache.get(2);    // 返回 -1 (未找到)
 * lRUCache.put(4, 4); // 该操作会使得关键字 1 作废，缓存是 {4=4, 3=3}
 * lRUCache.get(1);    // 返回 -1 (未找到)
 * lRUCache.get(3);    // 返回 3
 * lRUCache.get(4);    // 返回 4
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 1 <= capacity <= 3000
 * 0 <= key <= 10000
 * 0 <= value <= 10⁵
 * 最多调用 2 * 10⁵ 次 get 和 put
 * <p>
 * <p>
 * Related Topics设计 | 哈希表 | 链表 | 双向链表
 * <p>
 * 👍 3372, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class LruCache {
    public static void main(String[] args) {
        LRUCache lruCache = new LruCache().new LRUCache(2);
        lruCache.put(2, 1);
        lruCache.put(1, 1);
        lruCache.put(2, 3);
        lruCache.put(4, 1);
        System.out.println(lruCache.get(1));
        System.out.println(lruCache.get(2));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class LRUCache {

        private final Map<Integer, Node> nodeMap;
        private final Node head = new Node(0, 0);
        private final Node tail = new Node(0, 0);
        private final Integer capacity;


        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.nodeMap = new HashMap<>(capacity);
            head.next = tail;
            tail.pre = head;
        }

        public int get(int key) {
            Node curNode = nodeMap.get(key);
            if (curNode == null) {
                return -1;
            } else {
                remove(key);
                insertHead(curNode);
                return curNode.val;
            }
        }

        public int remove(int key) {
            Node node = nodeMap.get(key);
            if (node == null) {
                return -1;
            } else {
                Node pre = node.pre;
                Node next = node.next;
                pre.next = next;
                next.pre = pre;
                return node.val;
            }
        }

        private void insertHead(Node curNode) {
            Node next = head.next;
            next.pre = curNode;
            curNode.next = next;
            head.next = curNode;
            curNode.pre = head;
        }

        public void put(int key, int value) {
            Node node = nodeMap.get(key);
            //key存在
            if (node != null) {
                remove(key);
                node.val = value;
                insertHead(node);
            } else {
                Node newNode = new Node(key, value);
                nodeMap.put(key, newNode);
                insertHead(newNode);
                //超出最大容量，丢弃最后一个节点
                if (nodeMap.size() > capacity) {
                    Node lastNode = tail.pre;
                    remove(lastNode.key);
                    nodeMap.remove(lastNode.key);
                }
            }
        }
    }

    class Node {
        public Integer key;
        public Integer val;
        public Node pre;
        public Node next;

        public Node(Integer key, Integer val) {
            this.key = key;
            this.val = val;
        }
    }

/**
 * Your LRUCache object will be instantiated and called as such:
 * LRUCache obj = new LRUCache(capacity);
 * int param_1 = obj.get(key);
 * obj.put(key,value);
 */
//leetcode submit region end(Prohibit modification and deletion)

}