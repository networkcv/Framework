package _98_发布订阅模式._01_自定义发布订阅;

/**
 * Date: 2025/5/20
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class RandomIntEvent implements Event {

    int number;

    public RandomIntEvent(int number) {
        this.number = number;
    }


    @Override
    public long timestamp() {
        return System.currentTimeMillis();
    }

    @Override
    public Object source() {
        return number;
    }
}
