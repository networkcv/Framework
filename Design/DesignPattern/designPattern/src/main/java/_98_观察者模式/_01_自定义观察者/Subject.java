package _98_观察者模式._01_自定义观察者;


import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2025/5/20
 * <p>
 * Description: 用于被观察的主题
 *
 * @author 乌柏
 */
public class Subject {

    protected List<Observer> list = new ArrayList<>();

    public void register(Observer observer) {
        list.add(observer);
    }

    public void unregister(Observer observer) {
        list.remove(observer);
    }

    public void notifyObservers(String message) {
        for (Observer observer : list) {
            observer.notified(message);
        }
    }
}
