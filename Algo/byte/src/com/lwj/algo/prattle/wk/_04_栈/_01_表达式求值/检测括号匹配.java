package com.lwj.algo.prattle.wk._04_栈._01_表达式求值;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class 检测括号匹配 {
    //合法括号只有(){}[]
    // 从左往右扫描字符串，扫描到左括号压入栈中，扫描到右括号取栈顶元素相比较，如果不匹配或栈空 则返回false，扫描完后栈不为空也返回false
    public static void main(String[] args) {
        String s1 = "[{}()]"; //正常
        String s2 = "[{}()";  //缺右括号
        String s3 = "{}()]";  //缺左括号
        String s4 = "<[{}()]>";   //有非法括号
        String s5 = "[{}(])"; //括号位置不匹配
        System.out.println(scanBracket(s1));
        System.out.println(scanBracket(s2));
        System.out.println(scanBracket(s3));
        System.out.println(scanBracket(s4));
        System.out.println(scanBracket(s5));

    }
    public static boolean scanBracket(String s){
        if(s==null||s.equals("")||s.length() % 2 == 1) return false;
        Stack<Character> stack=new Stack();
        for(int i=0;i<s.length();i++){
            Character tmp=s.charAt(i);
            if(stack.isEmpty()){    //由于stack为空时 peek()会抛出异常
                stack.push(tmp);
            }else if(tmp=='}'&&stack.peek()=='{'||tmp==')'&&stack.peek()=='('||tmp==']'&&stack.peek()=='['){
                stack.pop();
            }else
                stack.push(tmp);
        }
        if(stack.isEmpty())
            return true;
        else
            return false;
    }

    private static boolean scanBracket1(String s) {
        if(s==null||s.equals("")||s.length() % 2 == 1) return false;
        Map<Character,Character> map=new HashMap();
        map.put('}','{');
        map.put(']','[');
        map.put(')','(');
        Stack<Character> leftStack = new Stack();
        for (int i=0;i<s.length();i++){
            Character tmp=s.charAt(i);
            if(tmp=='{'||tmp=='('||tmp=='['){
                leftStack.push(tmp);
            }else if(tmp=='}'||tmp==')'||tmp==']'){
                if(leftStack.size()!=0){
                    if(map.get(tmp)==leftStack.peek()){
                        leftStack.pop();
                        continue;
                    }else {
                        return false;
                    }
                }else {
                    return false;
                }
            }else {
                return false;
            }
        }
        if(leftStack.size()!=0)
            return false;
        else
            return true;
    }
}
