package com.lwj._03_selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

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
            int select = selector.select(3000);
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
                    //处理监听事件
                    handleAccept(ssc, selector);
                }
                if (selectionKey.isReadable()) {
                    //处理读事件
                    handleRead(selectionKey);
                }
                if (selectionKey.isWritable()) {
                    //处理写事件
                    handleWrite(selectionKey);
                }
                //  从事件集合中删除对应的事件, 因为防止二次处理.
                iterator.remove();
            }
        }
    }

    private static void handleAccept(ServerSocketChannel ssc, Selector selector) throws IOException {
        SocketChannel socketChannel = ssc.accept();
        // 9. 得到客户端通道, 并将通道注册到选择器上, 并指定监听事件为OP_READ
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        //  10. 判断是否是客户端读就绪事件SelectionKey.isReadable()
    }

    private static void handleRead(SelectionKey selectionKey) throws IOException {
        //  11. 得到客户端通道, 读取数据到缓冲区
        SocketChannel channel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        StringBuilder sb = new StringBuilder();
        int read;
        while ((read = channel.read(byteBuffer)) > 0) {
            sb.append(new String(byteBuffer.array(), 0, read, StandardCharsets.UTF_8));
            byteBuffer.flip();
        }
        System.out.println("receive msg: " + sb);


        //  12. 给客户端回写数据
        channel.write(ByteBuffer.wrap(("echo: " + sb).getBytes(StandardCharsets.UTF_8)));
    }


    private static void handleWrite(SelectionKey selectionKey) {
    }

}
