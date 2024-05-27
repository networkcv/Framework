package com.lwj.algo.prattle.nk._23_栈的压入弹出序列;

import java.util.Stack;

/**
 * create by lwj on 2018/10/30
 */
public class Test3 {
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
        //当 栈顶元素和弹出系列的指向元素不相等时 向栈中压入入栈数组
        while(popIndex<pop.length){
            while(pushIndex<push.length&&(stack.isEmpty()||stack.peek()!=pop[popIndex])){//这里需要考虑pushIndex下标越界问题，及stack为空时peek()报错
                stack.push(push[pushIndex]);
                pushIndex++;
            }
            if(stack.peek()==pop[popIndex]){//当栈顶元素和弹出系列的指向元素相等时 出栈
                stack.pop();
                popIndex++;
            }else {
                return false;  //当走到这里的时候说明入栈数组已经没有了，只能出栈了，但栈顶元素和弹出序列的指向元素不相等，说明不可能是
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
