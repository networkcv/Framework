package lwj.bk._09_用两个栈实现队列;

/**
 * create by lwj on 2019/2/8
 */
public class Test1 {
    public static void main(String[] args) throws InterruptedException {
        MyQueue mq = new MyQueue();
        mq.appendTail("快速排序");
        mq.appendTail("b");
        mq.appendTail("c");
        System.out.println(mq.deleteHead());
        mq.appendTail("d");
        System.out.println(mq.deleteHead());
        System.out.println(mq.deleteHead());
        System.out.println(mq.deleteHead());
        Thread.sleep(1000);
        System.out.println(mq.deleteHead());
    }
}
