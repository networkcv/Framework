package _98_观察者模式._02_JDK包中的观察者工具类;


/**
 * create by lwj on 2019/1/23
 */
public class Client {
    public static void main(String[] args) {
        Subject subject = new Subject();
        ObserverA observerA = new ObserverA();
        ObserverB observerB = new ObserverB();
        subject.addObserver(observerA);
        subject.addObserver(observerB);
        subject.notifyObservers("hello");
    }
}
