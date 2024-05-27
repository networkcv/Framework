package com.lwj.algo.prattle.bk._09_用两个栈实现队列;

import java.util.Stack;

/**
 * create by lwj on 2019/2/9
 */
public class MyQueue {
    private Stack s1 =new Stack();
    private Stack s2 =new Stack();
    public void appendTail(String s){
        s1.push(s);
    }
    public String deleteHead(){
        if(s2.empty()){
            while(!s1.empty()){
                s2.push(s1.pop());
            }
        }
        if(s2.size()==0)
            throw new RuntimeException("queue is empty");
        return (String) s2.pop();
    }
}
