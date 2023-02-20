package com.lwj._02_channel;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * Date: 2021/12/28
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class NioClient {
    public static void main(String[] args) throws Exception {
        SocketChannel sc = SocketChannel.open();
        System.out.println("客户端发起连接");
        sc.connect(new InetSocketAddress("localhost", 9999));
        sc.write(ByteBuffer.wrap("你好".getBytes(StandardCharsets.UTF_8)));
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int read = sc.read(byteBuffer);
        System.out.println(new String(byteBuffer.array(), 0, read, StandardCharsets.UTF_8));
    }
}
