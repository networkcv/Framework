package _98_发布订阅模式._02_JDK包中的发布订阅工具;

import java.util.EventObject;

/**
 * Date: 2025/5/21
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class RandomIntEvent extends EventObject {

    private static final long serialVersionUID = -839184056839887609L;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public RandomIntEvent(Object source) {
        super(source);
    }
}
