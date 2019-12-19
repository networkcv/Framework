package _21_并发._07_JDK并发包.readWriteLock;

import org.junit.Test;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * create by lwj on 2019/12/19
 * 锁降级示例
 */
public class ReadWriteLock2 {
    ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    final Lock r = readWriteLock.readLock();
    final Lock w = readWriteLock.writeLock();
    volatile boolean cacheValid;

    @Test
    public void processCachedData() {
        r.lock();           //获取读锁
        if (!cacheValid){   //缓存失效
            r.unlock();     //先释放读锁，不允许锁升级
            w.lock();
            try {
                if (!cacheValid){   //再次检查缓存状态，
//                  cache= ...
                    cacheValid =true;   //完成缓存更新
                }
                r.lock();   //释放写锁前，降级为读锁
            }finally {
                w.unlock(); //释放写锁
            }
        }
        try{
//            ...     //执行业务逻辑
        }finally {
            r.unlock();
        }
    }

}
