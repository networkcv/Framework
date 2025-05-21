package _98_发布订阅模式._02_JDK包中的发布订阅工具;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * Date: 2025/5/20
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class EventBus {

    private final List<EventListener<EventObject>> listenerList = new ArrayList<>();

    public void publish(EventObject event) {
        listenerList.forEach(l -> l.onEvent(event));

    }

    public void subscribe(EventListener<EventObject> listener) {
        listenerList.add(listener);
    }

    public void unsubscribe(EventListener<EventObject> listener) {
        listenerList.remove(listener);
    }


}
