//<p>实现一个MyQueue类，该类用两个栈来实现一个队列。</p>
//<br><p><strong>示例：</strong><pre>MyQueue queue = new MyQueue();<br><br>queue.push(1);<br>queue.push(2);<br>queue.peek();  // 返回 1<br>queue.pop();   // 返回 1<br>queue.empty(); // 返回 false</br></br></br></br></br></br></pre></p><br><p><strong>说明：</strong><br>
//    <ul>
//     <li>你只能使用标准的栈操作 -- 也就是只有 <code>push to top</code>, <code>peek/pop from top</code>, <code>size</code> 和 <code>is empty</code> 操作是合法的。</li>
//     <li>你所使用的语言也许不支持栈。你可以使用 list 或者 deque（双端队列）来模拟一个栈，只要是标准的栈操作即可。</li>
//     <li>假设所有操作都是有效的 （例如，一个空的队列不会调用 pop 或者 peek 操作）。</li>
//    </ul></br></p></br></br>
//
//<div><div>Related Topics</div><div><li>栈</li><li>设计</li><li>队列</li></div></div><br><div><li>👍 58</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import java.util.LinkedList;

class ImplementQueueUsingStacksLcci {
    public static void main(String[] args) {
//        MyQueue myQueue = new MyQueue();
//        myQueue.push(1);
//        myQueue.push(2);
//        System.out.println(myQueue.peek()); // 返回 1
//        System.out.println(myQueue.pop()); // 返回 1
//        System.out.println(myQueue.empty()); // 返回 false

    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class MyQueue {
        private LinkedList<Integer> inputStack;
        private LinkedList<Integer> outputStack;

        /**
         * Initialize your data structure here.
         */
        public MyQueue() {
            inputStack = new LinkedList<>();
            outputStack = new LinkedList<>();
        }

        /**
         * Push element x to the back of queue.
         */
        public void push(int x) {
            inputStack.push(x);
        }

        /**
         * Removes the element from in front of queue and returns that element.
         */
        public int pop() {
            while (!inputStack.isEmpty()) {
                outputStack.add(inputStack.removeLast());
            }
            return outputStack.removeFirst();
        }

        /**
         * Get the front element.
         */
        public int peek() {
            if (!outputStack.isEmpty()) {
                return outputStack.peekFirst();
            }
            return inputStack.peekLast();
        }

        /**
         * Returns whether the queue is empty.
         */
        public boolean empty() {
            return inputStack.isEmpty() && outputStack.isEmpty();

        }
    }

/**
 * Your MyQueue object will be instantiated and called as such:
 * MyQueue obj = new MyQueue();
 * obj.push(x);
 * int param_2 = obj.pop();
 * int param_3 = obj.peek();
 * boolean param_4 = obj.empty();
 */
//leetcode submit region end(Prohibit modification and deletion)

}