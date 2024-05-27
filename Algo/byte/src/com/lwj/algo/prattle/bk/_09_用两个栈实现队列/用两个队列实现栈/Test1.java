package com.lwj.algo.prattle.bk._09_用两个栈实现队列.用两个队列实现栈;

/**
 * create by lwj on 2019/2/10
 */
public class Test1 {
    public static void main(String[] args) throws InterruptedException {
      MyStack myStack=new MyStack();
      myStack.push(1);
      myStack.push(2);
      myStack.push(3);
      System.out.println(myStack.pop());
      System.out.println(myStack.pop());
      myStack.push(4);
      System.out.println(myStack.pop());
      System.out.println(myStack.pop());
      Thread.sleep(1000);
      System.out.println(myStack.pop());

    }


}
