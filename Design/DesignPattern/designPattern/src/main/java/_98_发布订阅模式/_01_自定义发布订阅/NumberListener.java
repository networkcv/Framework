package _98_发布订阅模式._01_自定义发布订阅;

/**
 * Date: 2025/5/20
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class NumberListener implements EventListener {
    @Override
    public void onEvent(Event event) {
        System.out.println("NumberListener " + event.source());
    }
}
