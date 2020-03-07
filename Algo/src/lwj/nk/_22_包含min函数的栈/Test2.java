package lwj.nk._22_包含min函数的栈;

import java.util.Stack;

/**
 * create by lwj on 2018/10/30
 */
public class Test2 {
    Stack<Integer> stack1 = new Stack();
    Stack<Integer> stack2 = new Stack();
    public void push(int node){
        stack1.push(node);
        if(stack2.isEmpty())
            stack2.add(node);
        else{
            if(node<=stack2.peek()){
                stack2.add(node);
            }
        }
    }
    public int pop(){
        Integer pop = stack1.pop();
        if(pop==stack2.peek())
            stack2.pop();
        return pop;
    }

    public int min(){
        return stack2.peek();
    }
}
