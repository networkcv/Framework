package _98_发布订阅模式._01_自定义发布订阅;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 2025/5/20
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class EventBus implements EventPublisher {

    private final List<EventListener> listenerList = new ArrayList<>();

    @Override
    public void publish(Event event) {
        listenerList.forEach(l -> l.onEvent(event));

    }

    @Override
    public void subscribe(EventListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void unsubscribe(EventListener listener) {
        listenerList.remove(listener);
    }


}
