package netty.ch3;

import io.netty.channel.DefaultSelectStrategyFactory;
import io.netty.channel.SelectStrategy;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.RejectedExecutionHandler;
import io.netty.util.concurrent.RejectedExecutionHandlers;
import io.netty.util.concurrent.ThreadPerTaskExecutor;

import java.lang.reflect.Constructor;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.TimeUnit;

/**
 * Date: 2024/2/1
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class CustomerNioEventLoopDemo {
    public static void main(String[] args) throws Exception {
        NioEventLoopGroup parent = new NioEventLoopGroup(1);
        ThreadPerTaskExecutor executor = new ThreadPerTaskExecutor(new DefaultThreadFactory("wb-test-pool"));
        SelectorProvider provider = SelectorProvider.provider();
        SelectStrategy selectStrategy = DefaultSelectStrategyFactory.INSTANCE.newSelectStrategy();
        RejectedExecutionHandler reject = RejectedExecutionHandlers.reject();
        Constructor<?>[] constructors = NioEventLoop.class.getDeclaredConstructors();
        Constructor<?> constructor = constructors[0];
        constructor.setAccessible(true);
        NioEventLoop nioEventLoop = (NioEventLoop) constructor.newInstance(parent, executor, provider, selectStrategy, reject);
        nioEventLoop.execute(CustomerNioEventLoopDemo::sleep);

    }

    private static void sleep() {
        try {
            int num = 1;
            TimeUnit.SECONDS.sleep(num);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
