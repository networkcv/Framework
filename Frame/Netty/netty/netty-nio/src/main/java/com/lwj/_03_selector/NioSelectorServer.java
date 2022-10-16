package com.lwj._03_selector;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Date: 2021/12/29
 * <p>/
 * Description:
 *
 * @author liuWangjie
 */
public class NioSelectorServer {
    public static void main(String[] args) throws Exception {
        //  1. 打开一个服务端通道
        ServerSocketChannel ssc = ServerSocketChannel.open();
        //  2. 绑定对应的端口号
        ssc.bind(new InetSocketAddress(8888));
        //  3. 通道默认是阻塞的，需要设置为非阻塞
        ssc.configureBlocking(false);
        //  4. 创建选择器
        Selector selector = Selector.open();
        //  5. 将服务端通道注册到选择器上, 并指定注册监听的事件为OP_ACCEPT
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        //  6. 检查选择器是否有事件
        while (true) {
            int select = selector.select(1000);
            if (select == 0) {
                System.out.println("暂时没有事件");
                continue;
            }
            //  7. 获取事件集合
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                //  8. 判断事件是否是客户端连接事件SelectionKey.isAcceptable()
                if (selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = ssc.accept();
                    // 9. 得到客户端通道, 并将通道注册到选择器上, 并指定监听事件为OP_READ
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    //  10. 判断是否是客户端读就绪事件SelectionKey.isReadable()
                }
                if (selectionKey.isReadable()) {
                    //  11. 得到客户端通道, 读取数据到缓冲区
                    SocketChannel channel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int read = channel.read(byteBuffer);
                    if (read > 0) {
                        System.out.println(new String(byteBuffer.array(), 0, read, StandardCharsets.UTF_8));
                    }
                    //  12. 给客户端回写数据
                    channel.write(ByteBuffer.wrap("server msg".getBytes(StandardCharsets.UTF_8)));
                    channel.close();
                }
                //  13. 从集合中删除对应的事件, 因为防止二次处理.
                iterator.remove();
            }
        }
    }
}
