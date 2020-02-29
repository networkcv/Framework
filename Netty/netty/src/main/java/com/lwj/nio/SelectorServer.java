package com.lwj.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * create by lwj on 2020/2/29
 */
public class SelectorServer {
    /**
     * 服务器端的NIO监听程序
     */
    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 创建Selector
        Selector selector = Selector.open();
        // 绑定ip
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为不阻塞
        serverSocketChannel.configureBlocking(false);
        // 将ServerSocketChannel注册到selector。指定关心的事件为OP_ACCEPT，
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 当有关心的事件发生时，会返回这个SelectionKey，通过SelectionKey可以拿到Channel
        while (true) {
            // Selector监听，等于0说明此时没有事件发生。
            if (selector.select(1000) == 0) {
                System.out.println("server monitor 1s");
                continue;
            }
            Set<SelectionKey> keys = selector.selectedKeys();   //获取所有已注册的Channel中发生事件的SelectionKey，
//            Set<SelectionKey> keys1 = selector.keys();    // 获取所有已注册的Channel的SelectionKey
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                // 获得SocketChannel，此处的accept不会阻塞
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 此处socketChannel也要设置为非阻塞模式
                    socketChannel.configureBlocking(false);
                    // 注册Selector。第三个参数是连接的对象，通过SelectionKey可以连接到这个对象
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("client has connect , socketChannel: " + socketChannel.hashCode());
                }
                if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                    socketChannel.read(byteBuffer);
                    System.out.println("client :" + new String(byteBuffer.array()));
                }
                // 手动删除避免重复
                iterator.remove();
            }

        }
    }
}
