package lwj.bk._06_从尾到头打印链表;

import lwj.bk._00_Utils.ListNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * create by lwj on 2019/2/6
 */
public class Test1 {
    public static void main(String[] args){
        ListNode n1=new ListNode(1);
        ListNode n2=new ListNode(2);
        ListNode n3=new ListNode(3);
        ListNode n4=new ListNode(4);
        n1.next=n2;
        n2.next=n3;
        n3.next=n4;
//        List<Integer> list = resversePrint(n1);
        List<Integer> list = resversePrint1(n1);
        for (Integer l : list) {
            System.out.print(l+" ");
        }
    }
    static List<Integer> list = new ArrayList<>();

    //使用栈实现
    public static List<Integer> resversePrint(ListNode head){
        Stack stack=new Stack();
        while(head!=null){
            stack.push(head.val);
            head=head.next;
        }
        while(!stack.isEmpty()){
            list.add((Integer) stack.pop());
        }
        return list;
    }

    //递归本质是个栈结构，可以使用栈实现,如果链表过长，不建议使用递归
    public static List<Integer> resversePrint1(ListNode node){
        if(node!=null){
            resversePrint1(node.next);
            list.add(node.val);
        }
        return list;
    }
}
