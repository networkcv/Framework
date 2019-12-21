package _21_并发._07_JDK并发包.semaphore;

import org.junit.Test;

import java.util.concurrent.Semaphore;

/**
 * create by lwj on 2019/12/21
 */
public class SemaphoreTest {
    @Test
    public void test() throws InterruptedException {
        Semaphore semaphore = new Semaphore(10);
        semaphore.acquire();
        semaphore.release();
    }
}
