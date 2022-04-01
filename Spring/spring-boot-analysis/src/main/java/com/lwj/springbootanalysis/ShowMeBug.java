package com.lwj.springbootanalysis;

/**
 * Date: 2022/3/31
 * <p>
 * Description:
 *
 * @author liuWangjie
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

// 必须定义 `ShowMeBug` 入口类和 `public static void main(String[] args)` 入口方法
public class ShowMeBug {

    static class Node {
        int id;
        int parentId;
        String name;

        public Node(int id, int parentId, String name) {
            this.id = id;
            this.parentId = parentId;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public int getParentId() {
            return parentId;
        }

        public String getName() {
            return name;
        }
    }

    public static void main(String[] args) {
        List<Node> nodeList = Arrays.asList(
                new Node(1, 0, "AA"),
                new Node(2, 1, "BB"),
                new Node(3, 1, "CC"),
                new Node(4, 3, "DD"),
                new Node(5, 3, "EE"),
                new Node(6, 2, "FF"),
                new Node(7, 2, "GG"),
                new Node(8, 4, "HH"),
                new Node(9, 5, "II"),
                new Node(10, 0, "JJ"),
                new Node(11, 10, "KK"),
                new Node(12, 10, "LL"),
                new Node(13, 12, "MM"),
                new Node(14, 13, "NN"),
                new Node(15, 14, "OO")
        );
        print(nodeList);
    }

    public static void print(List<Node> nodeList) {
        nodeList.sort(Comparator.comparing(Node::getParentId));
        Node root = new Node(0, -1, "root");
        print(nodeList, root, -1);
    }


    public static void print(List<Node> nodeList, Node node, Integer level) {
        printSpace(level);
        level++;
        if (node.getId() != 0) {
            System.out.println(node.getName());
        }
        for (Node childNode : getChildList(nodeList, node)) {
            print(nodeList, childNode, level);
        }
    }

    private static List<Node> getChildList(List<Node> nodeList, Node node) {
        ArrayList<Node> resList = new ArrayList<>();
        for (Node treeNode : nodeList) {
            if (treeNode.getParentId() == node.getId()) {
                resList.add(treeNode);
            }
        }
        return resList;
    }

    public static void printSpace(Integer level) {
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
    }
}