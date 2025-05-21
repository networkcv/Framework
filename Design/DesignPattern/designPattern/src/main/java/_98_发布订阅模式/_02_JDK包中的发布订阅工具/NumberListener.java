package _98_发布订阅模式._02_JDK包中的发布订阅工具;


import java.util.EventObject;

/**
 * Date: 2025/5/20
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class NumberListener implements EventListener<EventObject> {

    @Override
    public void onEvent(EventObject event) {
        System.out.println("NumberListener " + event.getSource());
    }
}
