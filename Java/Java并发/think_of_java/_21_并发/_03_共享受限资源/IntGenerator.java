package think_of_java._21_并发._03_共享受限资源;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * create by lwj on 2019/11/9
 * 抽象类，生成一个int，还有一个标志状态canceld
 */
public abstract class IntGenerator {
   public ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

    private volatile boolean canceld = false;

    public abstract int next();

    public void cancel() {
        canceld = true;
    }

    public boolean isCanceld() {
        return canceld;
    }

}
