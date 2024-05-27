package com.lwj.algo.prattle.nk._7_用两个栈来实现队列;

/**
 * create by lwj on 2018/9/30
 */
//进队列 把所有的数字都压入stack1， 出队列 把stack1中的数字pop压入stack2，然后在pop
import java.util.Stack;

public class Test1 {
    Stack<Integer> stack1 = new Stack<Integer>();
    Stack<Integer> stack2 = new Stack<Integer>();

    public void push(int node) {
        stack1.push(node);

    }

    public int pop() {
        while(!stack1.isEmpty()){
            stack2.push(stack1.pop());
        }
        int pop=stack2.pop();
        while(!stack2.isEmpty()){
            stack1.push(stack2.pop());
        }
        return pop;
    }
    public static void main(String[] args){
        Test1 stack = new Test1();
        stack.push(1);
        stack.push(2);
        int i1 = stack.pop();
        stack.push(3);
        int i2 = stack.pop();
        int i3 = stack.pop();
        System.out.println(i1+" "+i2+" "+i3);

    }

}