package lwj.nk._1_链表的中间结点;


import lwj.nk._0_Utils.ListNode;

/**
 * create by lwj on 2018/10/5
 */
public class Test1 {
    public static void main(String[] args){
        int [] arr={1,2,3,4,5,6};
        ListNode head = ListNode.arrayToListNode(arr);
        System.out.println(findMid(head));
        System.out.println(findMid1(head));
    }
    private static ListNode findMid(ListNode head) {
        ListNode first=head;
        ListNode slow=head;
        while(first!=null){
            if(first.next==null){
                return slow;    ////链表个数为单数的话，从这里返回
            }
            first=first.next.next;
            slow=slow.next;
        }
        return slow;    //链表个数为双数的话，从这里返回
    }

    private static ListNode findMid1(ListNode head) {
        ListNode before=head;
        ListNode after=head;
        while(before!=null){
            if(before.next==null){
                return after;
            }
            before=before.next.next;
            after=after.next;
        }
        return after;
    }

}
