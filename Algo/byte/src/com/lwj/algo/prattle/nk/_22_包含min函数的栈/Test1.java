package com.lwj.algo.prattle.nk._22_包含min函数的栈;

import java.util.ArrayList;
import java.util.List;

/**
 * create by lwj on 2018/10/25
 */
public class Test1 {
    //定义栈的数据结构，请在该类型中实现一个能够得到栈中所含最小元素的min函数（时间复杂度应为O（1））。
    List<Integer> list = new ArrayList();
    List<Integer> minStack=new ArrayList<>();
    private int size = -1;
    private int min = Integer.MAX_VALUE;

    public void push(int node) {
        list.add(node);
        size++;
        if (node < min) {
            min = node;
        }
        if(minStack.size()==0){
            minStack.add(node);
        }else{
            if(node<=minStack.get(minStack.size()-1))
                minStack.add(node);
        }

    }

    public int pop()  {
        if (size == -1) {
            try {
                throw new Exception("无元素");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Integer remove = list.remove(size);
        size--;
        if(remove==minStack.get(minStack.size()-1)){
            minStack.remove(minStack.get(minStack.size()-1));
        }
        return remove;
    }

    public int top() {
        return list.get(size);
    }

    public int min() {
        if (size == -1) {
            try {
                throw new Exception("无元素");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return minStack.get(minStack.get(minStack.size()-1));
    }

    public static void main(String[] args){
        Test1 test1 = new Test1();
        test1.push(5);
        test1.push(3);
        test1.push(1);
        test1.pop();
//        System.out.println(test1.top());
        test1.push(2);
        test1.push(4);
        System.out.println(test1.min());
    }
}
