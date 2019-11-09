package think_of_java._21_并发._03_共享受限资源;

import org.junit.Test;
import think_of_java._21_并发._00_util.BaseUtil;

import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/9
 */
public class _11 implements Runnable {
    public int int1;
    private int int2;

    //使用 synchronized 解决并发的不确定状态
    public synchronized void disposeData() {
//    public void disposeData() {
        int1++;
        Thread.yield();
        int1++;
        Thread.yield();
        int1++;
        Thread.yield();
        int2++;
        Thread.yield();
        int2++;
    }

    @Override
    public void run() {
        disposeData();
    }

    @Test
    public void test() {
        _11 t = new _11();
        BaseUtil.exec(t, 100);
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t.int1);
        System.out.println(t.int2);
    }
}
