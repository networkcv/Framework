package _98_发布订阅模式._01_自定义发布订阅;

/**
 * Date: 2025/5/20
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class Client {
    public static void main(String[] args) {
        EventBus eventBus = new EventBus();
        NumberListener numberListener = new NumberListener();
        eventBus.subscribe(numberListener);
        RandomIntPublisher randomIntPublisher = new RandomIntPublisher(eventBus);
        randomIntPublisher.start();
    }
}