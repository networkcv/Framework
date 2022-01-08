package _99_observer_pattern.demo1;

/**
 * create by lwj on 2019/1/22
 */
public class Client {
    public static void main(String[] args){
        Subject subject =new ConcreteSubject();
        Observer observerA=new ObserverA();
        Observer observerB=new ObserverA();
        Observer observerC=new ObserverA();
        subject.register(observerA);
        subject.register(observerB);
        subject.register(observerC);
        System.out.println(((ObserverA) observerA).getMyState());
        System.out.println(((ObserverA) observerB).getMyState());
        System.out.println(((ObserverA) observerC).getMyState());        System.out.println(((ObserverA) observerA).getMyState());
        ((ConcreteSubject) subject).setState(999);
        System.out.println(((ObserverA) observerA).getMyState());
        System.out.println(((ObserverA) observerB).getMyState());
        System.out.println(((ObserverA) observerC).getMyState());
    }
}
