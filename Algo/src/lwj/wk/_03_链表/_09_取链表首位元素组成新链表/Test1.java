package lwj.wk._03_链表._09_取链表首位元素组成新链表;

import lwj.wk._03_链表._00_util.Node;
import org.junit.Test;

/**
 * create by lwj on 2019/3/15
 */
public class Test1 {
    public static void main(String[] args) {
        Node n1 = new Node(1);
        Node n2 = new Node(2);
        Node n3 = new Node(3);
        Node n4 = new Node(4);
        Node n5 = new Node(5);
        n1.next = n2;
        n2.next = n3;
//        n3.next = n4;
//        n4.next=n5;
        Node head = fun(null);
        while (head != null) {
            System.out.println(head.data);
            head = head.next;
        }
    }
    //通过36行断链，简化合并的判断
    private static Node fun(Node head) {
        if(head==null)return null;
        //取链表的中间结点
        Node q = head;
        Node s = head;
        while(q.next!=null&&q.next.next!=null){
            q=q.next.next;
            s=s.next;
        }
        //断链
        q=s.next;
        s.next=null;
        s=q;
        //反转中间节点后的节点
        Node pre=null;
        Node current =null;
        while(s!=null){
            current=s;   //保留当前结点
            s=s.next;    //找到下次循环的结点
            current.next=pre;   //反转当前结点
            pre=current;    //重置前驱结点
        }
        //合并链表返回头节点
        Node head1=head;
        Node head2=current;
        Node res=new Node(-1);
        Node tmp=res;
        int count=0;
        while (head2!=null||head1!=null){
            if(count++%2==0){
                tmp.next=head1;
                head1=head1.next;
            }else {
                tmp.next=head2;
                head2=head2.next;
            }
            tmp=tmp.next;
        }
        return res.next;
    }

    private static Node fun1(Node head) {
        //取链表的中间结点
        //结点个数为奇数的话为中间结点 ，结点个数为偶数的话为中间考前的节点
        Node q = head;
        Node s = head;
        while (q.next != null && q.next.next != null) {
            q = q.next.next;
            s = s.next;
        }
        s = s.next;
        //反转中间节点后的节点
        Node pre = null;
        Node current = null;
        while (s != null) {
            current = s;
            s = s.next;
            current.next = pre;
            pre = current;
        }
        Node head1 = head;
        Node head2 = current;
        //合并链表返回头节点
        Node res = new Node(-1);
        Node tmp = res;
        int count = 0;
        while (head2 != null) {
            if (count++ % 2 == 0) {
                tmp.next = head1;
                head1 = head1.next;
            } else {
                tmp.next = head2;
                head2 = head2.next;
            }
            tmp = tmp.next;
        }
        tmp.next = head1; //手动处理长链中的最后一个元素
        head1.next = null;
        return res.next;
    }

    @Test
    public void fun12(){
        int a=15%2;
        int b=15&1;
        System.out.println(a);
        System.out.println(b);
    }


}

