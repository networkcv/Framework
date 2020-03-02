package com.lwj.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * create by lwj on 2020/2/29
 */
public class SelectorClient {
    public static void main(String[] args) throws IOException {

        SocketChannel socketChannel = null;
        //得到一个网络通道
        socketChannel = socketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //提供服务器的IP和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        //连接服务器
        if(!socketChannel.connect(inetSocketAddress)){
            while(!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作");
            }
        }

        //连接成功，发送数据
        String str="hello";
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        socketChannel.write(byteBuffer);
        System.in.read();


    }
}
