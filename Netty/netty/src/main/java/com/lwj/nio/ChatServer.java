package com.lwj.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * create by lwj on 2020/3/1
 */
public class ChatServer {
    public Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    public ChatServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            System.out.println("server start fail");
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.listen();
    }

    private void listen() {
        System.out.println("server start ok");
        try {
            while (true) {
                int select = selector.select();
                if (select > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        //监听到accept
                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() + " on line");
                        }
                        if (key.isReadable()) {
                            readData(key);
                        }
                        //当前的key 删除，防止重复处理
                        iterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("server close");
                listenChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void readData(SelectionKey key) {
        SocketChannel socketChannel = null;
        ByteBuffer byteBuffer;
        try {
            socketChannel = (SocketChannel) key.channel();
            byteBuffer = ByteBuffer.allocate(1024);
            int readSize = socketChannel.read(byteBuffer);
            if (readSize > 0) {
                String msg = new String(byteBuffer.array());
                System.out.println("from client " + socketChannel.getRemoteAddress() + " :" + msg);
                //消息转发给其他客户端
                sendMsgToOtherClient(msg, key);
            }
        } catch (IOException e) {
            try {
                System.out.println(socketChannel.getRemoteAddress() + " off line");
                key.cancel();
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sendMsgToOtherClient(String msg, SelectionKey srcKey) throws IOException {
        Channel channel;
        SocketChannel targetChannel;
        for (SelectionKey key : selector.keys()) {
            //这里既要排除自己，还要排除SeverSocketChannel
            if (key != srcKey && (channel = key.channel()) instanceof SocketChannel) {
                targetChannel = (SocketChannel) channel;
                targetChannel.write(ByteBuffer.wrap(msg.getBytes()));
            }

        }
    }
}
