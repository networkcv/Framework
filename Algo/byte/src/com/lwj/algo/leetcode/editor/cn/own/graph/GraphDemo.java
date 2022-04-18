package com.lwj.algo.leetcode.editor.cn.own.graph;

import com.lwj.algo.leetcode.editor.cn.Graph;
import org.junit.Test;

import java.util.Arrays;

/**
 * Date: 2022/4/14
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class GraphDemo {
    private final Graph graph;

    {
        /*
         * 0 —— 1 —— 2
         * |    |    |
         * 3 —— 4 —— 5
         *      |    |
         *      6 —— 7
         */
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

    @Test
    public void graph_dfs() {
        graph.dfs(0, 3);
        graph.dfs(4, 0);
    }

    @Test
    public void graph_bfs() {
        graph.bfs(0, 5);
        graph.bfs(4, 0);
    }

    @Test
    public void find() {
        System.out.println(graph.findLevelNode(4, 1));
        System.out.println(graph.findLevelNode(0, 0));
        System.out.println(graph.findLevelNode(0, 1));
        System.out.println(graph.findLevelNode(0, 2));
        System.out.println(graph.findLevelNode(0, 3));
        System.out.println(graph.findLevelNode(0, 4));
        System.out.println(graph.findLevelNode(0, 5));
    }



    private final Graph digraph;

    {
        /*
         * 0 ➡️ 1 ➡️ 2
         * ⬇️   ⬇️
         * 3 ⬅️ 4 ➡️ 5
         *          ⬇️
         *      6 ⬅️ 7
         */
        digraph = new Graph(8, true);
        digraph.addEdge(0, 1);
        digraph.addEdge(0, 3);
        digraph.addEdge(1, 2);
        digraph.addEdge(1, 4);
        digraph.addEdge(4, 3);
        digraph.addEdge(4, 5);
        digraph.addEdge(5, 7);
        digraph.addEdge(7, 6);
    }

    @Test
    public void digraph_bfs() {
        digraph.bfs(0, 6);
        digraph.bfs(0, 3);
        digraph.bfs(0, 5);
    }

    @Test
    public void graphNodeTest() {
        GraphNode graphNode1 = new GraphNode(1);
        GraphNode graphNode2 = new GraphNode(2);
        GraphNode graphNode3 = new GraphNode(3);
        GraphNode graphNode4 = new GraphNode(4);
        GraphNode graphNode5 = new GraphNode(5);
        graphNode1.addEdge(graphNode2);
        graphNode1.addEdge(graphNode5);
        graphNode2.addEdge(graphNode3);
        graphNode3.addEdge(graphNode4);
        graphNode4.addEdge(graphNode5);
        System.out.println(GraphNode.isCyc(Arrays.asList(graphNode1, graphNode2, graphNode3, graphNode4, graphNode5)));
    }
}
