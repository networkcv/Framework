package _98_观察者模式._01_自定义观察者;

/**
 * Date: 2025/5/20
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class Client {
    public static void main(String[] args) {
        ObserverA observerA = new ObserverA();
        ObserverB observerB = new ObserverB();
        Subject subject = new Subject();
        subject.register(observerA);
        subject.register(observerB);
        subject.notifyObservers("hello");
        subject.unregister(observerB);
        subject.notifyObservers("world");
    }
}
