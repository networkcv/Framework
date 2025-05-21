package _98_发布订阅模式._01_自定义发布订阅;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Date: 2025/5/20
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class RandomIntPublisher extends Thread {

    EventBus eventBus;

    public RandomIntPublisher(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Override
    public void run() {
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Random random = new Random();
            int i = random.nextInt(100);
            RandomIntEvent randomIntEvent = new RandomIntEvent(i);
            eventBus.publish(randomIntEvent);
        }
    }
}
