package lwj.bk._09_用两个栈实现队列.用两个队列实现栈;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * create by lwj on 2019/2/10
 */
public class MyStack {
    private Queue q1=new PriorityQueue();
    private Queue q2=new PriorityQueue();
    public void push(Integer num){
        if( q1.isEmpty()&&q2.isEmpty()){
        q1.add(num);
        }else if(q1.isEmpty()){
            q2.add(num);
        }else {
            q1.add(num);
        }
    }
    public Integer pop(){
        if( q1.isEmpty()&&q2.isEmpty()){
            throw new RuntimeException("没有值了");
        }else if(q1.isEmpty()){
            while(q2.size()>1){
                q1.add(q2.remove());
            }
            return (Integer)q2.remove();
        }else {
            while(q1.size()>1){
                q2.add(q1.remove());
            }
            return (Integer)q1.remove();
        }
    }

}
