package _98_发布订阅模式._02_JDK包中的发布订阅工具;

import java.util.EventObject;

/**
 * Date: 2025/5/20
 * <p>
 * Description: 事件监听器
 *
 * @author 乌柏
 */
public interface EventListener<E extends EventObject> {

    void onEvent(EventObject event);
}
