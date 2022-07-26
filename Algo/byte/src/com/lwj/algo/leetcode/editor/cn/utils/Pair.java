package com.lwj.algo.leetcode.editor.cn.utils;

/**
 * Date: 2022/7/26
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class Pair<K1, K2> {

    private final K1 left;

    public K1 getLeft() {
        return left;
    }

    private final K2 right;

    public K2 getRight() {
        return right;
    }

    public Pair(K1 key, K2 value) {
        this.left = key;
        this.right = value;
    }

    @Override
    public String toString() {
        return left + "=" + right;
    }

    @Override
    public int hashCode() {
        return left.hashCode() * 13 + (right == null ? 0 : right.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof Pair) {
            Pair pair = (Pair) o;
            if (left != null ? !left.equals(pair.left) : pair.left != null)
                return false;
            if (right != null ? !right.equals(pair.right) : pair.right != null)
                return false;
            return true;
        }
        return false;
    }
}
