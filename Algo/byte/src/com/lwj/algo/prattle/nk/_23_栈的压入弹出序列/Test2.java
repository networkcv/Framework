package com.lwj.algo.prattle.nk._23_栈的压入弹出序列;

import java.util.Stack;

/**
 * create by lwj on 2018/10/30
 */
public class Test2 {
    //输入两个整数序列，第一个序列表示栈的压入顺序，请判断第二个序列是否可能为该栈的弹出顺序。
    // 假设压入栈的所有数字均不相等。例如序列1,2,3,4,5是某栈的压入顺序，
    // 序列4,5,3,2,1是该压栈序列对应的一个弹出序列，但4,3,5,1,2就不可能是该压栈序列的弹出序列。（注意：这两个序列的长度是相等的）
    public static boolean IsPopOrder(int[] push, int[] pop) {
        if (push == null || pop == null || push.length == 0 || pop.length == 0 || push.length != pop.length) {
            return false;
        }
        Stack<Integer> stack = new Stack<>();
        // 用于记录入栈数组元素的处理位置
        int pushIndex = 0;
        // 用于记录出栈数组元素的处理位置
        int popIndex = 0;
        // 如果还有出栈元素要处理
        while (popIndex < pop.length) {
            // 入栈元素还未全部入栈的条件下，如果栈为空，或者栈顶的元素不与当前处理的相等，则一直进行栈操作，
            // 直到入栈元素全部入栈或者找到了一个与当出栈元素相等的元素
            while (pushIndex < push.length && (stack.isEmpty() || stack.peek() != pop[popIndex])) {
                // 入栈数组中的元素入栈
                stack.push(push[pushIndex]);
                // 指向下一个要处理的入栈元素
                pushIndex++;
            }

            // 如果在上一步的入栈过程中找到了与出栈的元素相等的元素
            if (stack.peek() == pop[popIndex]) {
                // 将元素出栈
                stack.pop();
                // 处理下一个出栈元素
                popIndex++;
            }
            // 如果没有找到与出栈元素相等的元素，说明这个出栈顺序是不合法的
            // 就返回false
            else {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 3, 4, 5};
        int[] b = {4, 5, 3, 2, 1};
        int[] c = {4, 3, 5, 1, 2};
        System.out.println(IsPopOrder(a, b));
        System.out.println(IsPopOrder(a, c));
    }
}
