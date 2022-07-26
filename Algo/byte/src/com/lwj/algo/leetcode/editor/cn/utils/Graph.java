package com.lwj.algo.leetcode.editor.cn.utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Date: 2022/4/14
 * <p>
 * Description:  无向图
 *
 * @author liuWangjie
 */
public class Graph {
    private boolean isDirect;//是否是有向图，默认无向图
    private int v; //顶点个数
    private LinkedList<Integer> adj[];//临接表

    public Graph(int v) {
        this(v, false);
    }

    public Graph(int v, boolean isDirect) {
        this.isDirect = isDirect;
        this.v = v;
        adj = new LinkedList[v];
        for (int i = 0; i < v; i++) {
            adj[i] = new LinkedList<>();
        }
    }

    /**
     * 无向图一条边存两次
     */
    public void addEdge(int s, int t) {
        if (isDirect) {
            adj[s].add(t);
        } else {
            adj[s].add(t);
            adj[t].add(s);
        }
    }

    /**
     * 查找最短路径（广度优先遍历）
     *
     * @param s 开始节点
     * @param t 目标节点
     */
    public void bfs(int s, int t) {
        if (s == t) return;
        //记录已访问节点数据
        boolean[] visited = new boolean[v];
        //广度优先按层访问 记录当前层还没被访问的节点
        Queue<Integer> queue = new LinkedList<>();
        //记录搜索路径 下标表示访问到的节点 下标对应到值表示是从哪个节点访问到的
        int[] prev = new int[v];
        for (int i = 0; i < v; i++) {
            prev[i] = -1;
        }
        visited[s] = true;
        queue.add(s);
        while (queue.size() != 0) {
            Integer cur = queue.poll();
            for (int i = 0; i < adj[cur].size(); i++) {
                Integer q = adj[cur].get(i);
                if (!visited[q]) {
                    prev[q] = cur;
                    if (q == t) {
                        print(prev, s, t);
                        System.out.println();
                        return;
                    }
                    queue.add(q);
                    visited[q] = true;
                }
            }
        }
    }

    private void print(int[] prev, int s, int t) {
        if (prev[t] != -1 && s != t) {
            print(prev, s, prev[t]);
        }
        System.out.print(t + " ");
    }

    private static boolean found = false;

    /**
     * 深度优先遍历
     *
     * @param s 开始节点
     * @param t 目标节点
     */
    public void dfs(int s, int t) {
        found = false;
        if (s == t) {
            return;
        }
        //记录已访问节点数据
        boolean[] visited = new boolean[v];
        //记录搜索路径 下标表示访问到的节点 下标对应到值表示是从哪个节点访问到的
        int[] prev = new int[v];
        for (int i = 0; i < v; i++) {
            prev[i] = -1;
        }
        recurDfs(s, t, prev, visited);
        print(prev, s, t);
        System.out.println();
    }

    private void recurDfs(int w, int t, int[] prev, boolean[] visited) {
        if (found) {
            return;
        }
        visited[w] = true;
        if (w == t) {
            found = true;
            return;
        }
        for (int i = 0; i < adj[w].size(); i++) {
            Integer q = adj[w].get(i);
            if (!visited[q]) {
                prev[q] = w;
                recurDfs(q, t, prev, visited);
            }
        }

    }

    /**
     * 查找目标节点的N度节点
     * 1度可以理解为目标的好友，2度可以理解为目标好友的好友，3度则是目标好友的好友的好友
     */
    public List<Integer> findLevelNode(int s, int level) {
        if (isDirect) {
            throw new RuntimeException("有向图暂不支持");
        }
        List<Integer> res = new ArrayList<>();
        if (level == 0) {
            res.add(s);
            return res;
        }
        //记录已访问节点数据
        boolean[] visited = new boolean[v];
        //广度优先按层访问 记录当前层还没被访问的节点
        Queue<Integer> queue = new LinkedList<>();
        //记录每个节点到起始节点的距离
        int[] len = new int[v];
        for (int i = 0; i < v; i++) {
            len[i] = -1;
        }
        visited[s] = true;
        queue.add(s);
        len[s] = 0;
        while (queue.size() != 0) {
            Integer cur = queue.poll();
            for (int i = 0; i < adj[cur].size(); i++) {
                Integer q = adj[cur].get(i);
                if (!visited[q]) {
                    len[q] = len[cur] + 1;
                    if (len[q] == level) {
                        res.add(q);
                    } else if (len[q] > level) {
                        return res;
                    }
                    queue.add(q);
                    visited[q] = true;
                }
            }
        }
        return res;
    }
}