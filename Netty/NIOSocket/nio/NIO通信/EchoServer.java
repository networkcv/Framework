package Netty.NIOSocket.nio.NIO通信;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;

/**
 * create by lwj on 2019/11/13
 */
public class EchoServer {
    public static void main(String[] args) throws IOException {
        AbstractSelector selector = SelectorProvider.provider().openSelector();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);

        InetSocketAddress inetSocketAddress = new InetSocketAddress(8888);
        ssc.socket().bind(inetSocketAddress);

        SelectionKey acceptKey = ssc.register(selector, SelectionKey.OP_ACCEPT);

    }

}
