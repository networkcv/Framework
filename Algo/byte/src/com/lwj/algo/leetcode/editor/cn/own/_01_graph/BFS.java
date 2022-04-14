package com.lwj.algo.leetcode.editor.cn.own._01_graph;

import com.lwj.algo.leetcode.editor.cn.Graph;

/**
 * Date: 2022/4/14
 * <p>
 * Description: 广度优先遍历  (breadth first search)
 *
 * @author liuWangjie
 */
public class BFS {
    private static final Graph graph;

    static {
        /*
         * 0 —— 1 —— 2
         * |    |    |
         * 3 —— 4 —— 5
         *      |    |
         *      6 —— 7
         * */
        graph = new Graph(8);
        graph.addEdge(0, 1);
        graph.addEdge(0, 3);
        graph.addEdge(1, 2);
        graph.addEdge(1, 4);
        graph.addEdge(3, 4);
        graph.addEdge(2, 5);
        graph.addEdge(4, 5);
        graph.addEdge(4, 6);
        graph.addEdge(5, 7);
        graph.addEdge(6, 7);
    }

    public static void main(String[] args) {
//        graph.bfs(0, 7);
//        graph.bfs(4, 0);
        System.out.println(graph.findLevelNode(4, 1));
        System.out.println(graph.findLevelNode(0, 0));
        System.out.println(graph.findLevelNode(0, 1));
        System.out.println(graph.findLevelNode(0, 2));
        System.out.println(graph.findLevelNode(0, 3));
        System.out.println(graph.findLevelNode(0, 4));
        System.out.println(graph.findLevelNode(0, 5));
    }


}
