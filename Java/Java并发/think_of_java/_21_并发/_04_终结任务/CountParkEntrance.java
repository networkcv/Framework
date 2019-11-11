package think_of_java._21_并发._04_终结任务;

import java.util.Random;

/**
 * create by lwj on 2019/11/11
 */
public class CountParkEntrance {
    public static class Count {
        private int count = 0;
        private Random random = new Random(47);

        public synchronized int increment() {
            int temp = count;
            if (random.nextBoolean())
                Thread.yield();
            return count = ++temp;
        }

        public synchronized int value() {
            return count;
        }
    }
}