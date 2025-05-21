package _98_观察者模式._02_JDK包中的观察者工具类;

import java.util.Observable;
import java.util.Observer;

/**
 * Date: 2025/5/20
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class ObserverB implements Observer {
    @Override
    public void update(Observable o, Object arg) {
        System.out.println("ObserverB update " + arg);
    }
}
