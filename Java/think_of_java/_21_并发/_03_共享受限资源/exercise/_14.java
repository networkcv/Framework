package think_of_java._21_并发._03_共享受限资源.exercise;

import org.junit.Test;
import think_of_java._21_并发._00_util.BaseUtil;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/9
 * 创建一个程序，生成多个timer对象，这些对象在 定时时间到达后执行某些操作
 */
public class _14 {
    @Test
    public void test() {
        try {
            BaseUtil.exec(() -> {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println(Thread.currentThread().getName() + " schedule is done");
                    }
                }, 1000);
            }, 10000);
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
