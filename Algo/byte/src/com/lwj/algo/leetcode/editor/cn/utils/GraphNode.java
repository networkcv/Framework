package com.lwj.algo.leetcode.editor.cn.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Date: 2022/4/15
 * <p>
 * Description:  图节点
 *
 * @author liuWangjie
 */
public class GraphNode {
    private Integer val;
    private List<GraphNode> pointList = new ArrayList<>();

    public GraphNode(Integer val) {
        this.val = val;
    }

    public void addEdge(GraphNode pointNode) {
        pointList.add(pointNode);
    }


    //判断无向图可以，有向图存在问题
    public static boolean isCyc(List<GraphNode> graphNodeList) {
        for (GraphNode graphNode : graphNodeList) {
            if (isCyc(graphNode, graphNodeList.size())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isCyc(GraphNode graphNode, int size) {
        Queue<GraphNode> queue = new LinkedList<>();
        boolean[] visited = new boolean[size+1];
        queue.add(graphNode);
        while (queue.size() != 0) {
            GraphNode curNode = queue.poll();
            if (!visited[curNode.val]) {
                visited[curNode.val] = true;
                List<GraphNode> pointList = curNode.pointList;
                queue.addAll(pointList);
            } else {
                return true;
            }
        }
        return false;
    }
}