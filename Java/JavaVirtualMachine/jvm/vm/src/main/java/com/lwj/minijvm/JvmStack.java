package com.lwj.minijvm;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Date: 2025/5/7
 * <p>
 * Description: 虚拟机栈
 *
 * @author 乌柏
 */
public class JvmStack {

    private final Deque<StackFrame> stack = new ArrayDeque<>();


    public StackFrame peek() {
        return stack.peek();
    }

    public void push(StackFrame stackFrame) {
        stack.push(stackFrame);
    }

    public StackFrame pop() {
        return stack.pop();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

}
