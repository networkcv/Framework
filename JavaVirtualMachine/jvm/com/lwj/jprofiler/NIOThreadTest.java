package com.lwj.jprofiler;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * Date: 2022/9/6
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class NIOThreadTest {
    public static void main(String[] args) {
        File file = new File("");
        file.deleteOnExit();
        new Thread(() -> {
            ServerSocketChannel ssc = null;
            try {
                ssc = ServerSocketChannel.open();

                ssc.configureBlocking(false);
                ssc.bind(new InetSocketAddress(8088));
                Selector selector = Selector.open();
                ssc.register(selector, SelectionKey.OP_ACCEPT);
                int select = selector.select();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "nio").start();
    }
}
