package lwj.nk._5_从尾到头打印链表;

import java.util.ArrayList;
import java.util.List;

/**
 * create by lwj on 2018/9/27
 */
//题目：输入个链表的头结点，从尾到头反过来打印出每个结点的值。
//使用栈完成
public class Test1 {
    public static void main(String[] args){
      ListNode n1=new ListNode(1);
      ListNode n2=new ListNode(2);
      ListNode n3=new ListNode(3);
      ListNode n4=new ListNode(4);
      n1.next=n2;
      n2.next=n3;
      n3.next=n4;
      Stack stack=new Stack(n1);
      ListNode node=stack.pop();
      while (node!=null){
          System.out.print(node+" ");
          node= stack.pop();
      }

    }

}

class ListNode {
    int val;
    ListNode next;

    public ListNode(int val) {
        this.val = val;
    }

    public String toString() {
        return val + "";
    }
}

class Stack {
    private List<ListNode> list = new ArrayList();

    public void push(ListNode head) {
        ListNode node = head;
        while (node != null) {
            list.add(node);
            node = node.next;
        }
    }
    public ListNode pop(){
        while(list.size()>0){
            return list.remove(list.size()-1);
        }
        return null;
    }

    public Stack(ListNode listNode){
        push(listNode);
    }

}
