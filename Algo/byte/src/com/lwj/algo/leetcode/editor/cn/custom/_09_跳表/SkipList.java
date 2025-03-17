package com.lwj.algo.leetcode.editor.cn.custom._09_跳表;

import java.util.Arrays;
import java.util.Random;

/**
 * 跳表的一种实现方法。
 * 跳表中存储的是正整数，并且存储的是不重复的。
 * <p>
 * create by lwj on 2019/3/12
 */
public class SkipList {

    public static final int MAX_LEVEL = 3;

    private int size = 0;

    private int levelCount = 1;

    private Node head = new Node();  // 带头链表

    private Random r = new Random();

    public void insert2(int value) {
        Node[] update;
        if (size % 2 == 0) {
            update = new Node[2];
        } else {
            update = new Node[1];
        }
        Arrays.fill(update, head);

        for (int i = 0; i < update.length; i++) {
            //将update[i]遍历至value节点前一个
            while (update[i].forwards[i] != null && update[i].forwards[i].data < value) {
                update[i] = update[i].forwards[i];
            }
        }
        Node newNode = new Node();
        newNode.data = value;
        for (int i = 0; i < update.length; i++) {
            //先记录下一个节点
            newNode.forwards[i] = update[i].forwards[i];
            //再将当前节点的下一个节点指向newNode
            update[i].forwards[i] = newNode;
        }
        size++;
    }

    public Node find2(int value) {
        Node pre = head;
        //先从快速线查找
        for (int i = head.forwards.length - 1; i >= 0; i--) {
            while (pre.forwards[i] != null && pre.forwards[i].data < value) {
                pre = pre.forwards[i];
            }
        }
        if (pre.forwards[0] == null || pre.forwards[0].data != value) return null;
        return pre.forwards[0];
    }

    public void delete2(int value) {
        Node[] update = new Node[2];
        Arrays.fill(update, head);
        for (int i = 0; i < update.length; i++) {
            while (update[i].forwards[i] != null && update[i].forwards[i].data < value) {
                update[i] = update[i].forwards[i];
            }
        }
        for (int i = 0; i < update.length; i++) {
            //当前层不存在value值
            if (update[i].forwards[i] == null || update[i].forwards[i].data != value) {
                return;
            }
            Node nextNextNode = update[i].forwards[i].forwards[i];
            update[i].forwards[i] = nextNextNode;
        }
        size--;
    }

    public Node find(int value) {
        Node p = head;
        for (int i = levelCount - 1; i >= 0; --i) {
            while (p.forwards[i] != null && p.forwards[i].data < value) {
                p = p.forwards[i];
            }
        }

        if (p.forwards[0] != null && p.forwards[0].data == value) {
            return p.forwards[0];
        } else {
            return null;
        }
    }

    public void insert(int value, int level) {
//        int level = randomLevel();
        Node newNode = new Node();
        newNode.data = value;
        newNode.maxLevel = level;
        Node[] update = new Node[level];
        Arrays.fill(update, head);

        // record every level largest value which smaller than insert value in update[]
        for (int i = level - 1; i >= 0; --i) {
            Node p = head;
            while (p.forwards[i] != null && p.forwards[i].data < value) {
                p = p.forwards[i];
                update[i] = p;// use update save node in search path
            }
        }

        // in search path node next node become new node forwords(next)
        for (int i = 0; i < level; ++i) {
            newNode.forwards[i] = update[i].forwards[i];
            update[i].forwards[i] = newNode;
        }

        // update node hight
        if (levelCount < level) levelCount = level;
    }

    public void delete(int value) {
        Node[] update = new Node[levelCount];
        Node p = head;
        for (int i = levelCount - 1; i >= 0; --i) {
            while (p.forwards[i] != null && p.forwards[i].data < value) {
                p = p.forwards[i];
            }
            update[i] = p;
        }

        if (p.forwards[0] != null && p.forwards[0].data == value) {
            for (int i = levelCount - 1; i >= 0; --i) {
                if (update[i].forwards[i] != null && update[i].forwards[i].data == value) {
                    update[i].forwards[i] = update[i].forwards[i].forwards[i];
                }
            }
        }
    }

    // 随机 level 次，如果是奇数层数 +1，防止伪随机
    private int randomLevel() {
        int level = 1;
        for (int i = 1; i < MAX_LEVEL; ++i) {
            if (r.nextInt() % 2 == 1) {
                level++;
            }
        }

        return level;
    }

    public void printAll() {
        Node p = head;
        while (p.forwards[0] != null) {
            System.out.print(p.forwards[0] + " ");
            p = p.forwards[0];
        }
        System.out.println();
    }

    public static class Node {
        private int data = -1;
        private final Node[] forwards = new Node[MAX_LEVEL];
        private int maxLevel = 0;

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("{ data: ");
            builder.append(data);
            builder.append("; levels: ");
            builder.append(maxLevel);
            builder.append(" }");

            return builder.toString();
        }
    }


}



