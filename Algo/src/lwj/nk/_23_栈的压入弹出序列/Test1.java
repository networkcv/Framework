package lwj.nk._23_栈的压入弹出序列;

import java.util.Stack;

/**
 * create by lwj on 2018/10/30
 */
public class Test1 {
    //输入两个整数序列，第一个序列表示栈的压入顺序，请判断第二个序列是否可能为该栈的弹出顺序。
    // 假设压入栈的所有数字均不相等。例如序列1,2,3,4,5是某栈的压入顺序，
    // 序列4,5,3,2,1是该压栈序列对应的一个弹出序列，但4,3,5,1,2就不可能是该压栈序列的弹出序列。（注意：这两个序列的长度是相等的）
    public static boolean IsPopOrder(int[] push, int[] pop) {
        if (push == null || pop == null || push.length == 0 || pop.length == 0 || push.length != pop.length) {
            return false;
        }
        Stack<Integer> stack = new Stack<>();
        stack.push(push[0]);
        int pushNum = 1;//用于记录入栈数组元素的处理位置
        int popNum = 0;//用于记录出栈数组元素的处理位置
        while (popNum < pop.length) {
            if (!stack.isEmpty()&&stack.peek() == pop[popNum]) {
                stack.pop();
                popNum++;
                if (popNum == pushNum && popNum == pop.length) {
                    return true;
                }
            } else {
                if(pushNum==push.length){
                    return false;
                }
                stack.push(push[pushNum]);
                pushNum++;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int[] a = {6,1, 2, 3, 4, 5};
        int[] b = {6,4, 5, 3, 2, 1};
        int[] c = {4, 3, 5, 1, 2};
        System.out.println(IsPopOrder(a, b));
        System.out.println(IsPopOrder(a, c));
    }
}
