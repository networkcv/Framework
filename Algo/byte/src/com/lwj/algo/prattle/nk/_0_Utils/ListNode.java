package com.lwj.algo.prattle.nk._0_Utils;

/**
 * create by lwj on 2018/10/4
 */
public class ListNode {
    public int val;
    public ListNode next;

    public ListNode(int val) {
        this.val = val;
    }
    public ListNode(){};

    public String toString() {
        return val + "";
    }

    public static void printListNode(ListNode head){
            while(head!=null){
                System.out.print(head);
                if(head.next!=null){
                System.out.print("->");
                }
                head=head.next;
            }
            System.out.println();
    }
    public static  ListNode arrayToListNode(int [] array){
        ListNode pre =new ListNode(array[0]);
        ListNode root=pre;
        for(int i=1;i<array.length;i++){
             pre.next =new ListNode(array[i]);
             pre=pre.next;

        }
        return root;
    }
}
