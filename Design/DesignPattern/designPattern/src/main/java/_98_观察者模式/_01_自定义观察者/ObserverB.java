package _98_观察者模式._01_自定义观察者;

/**
 * Date: 2025/5/20
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class ObserverB implements Observer {
    @Override
    public void notified(String message) {
        System.out.println("ObserverB notified " + message);
    }
}
