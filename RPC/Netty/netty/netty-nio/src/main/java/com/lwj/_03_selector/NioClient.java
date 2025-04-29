package com.lwj._03_selector;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * Date: 2021/12/28
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class NioClient {
    public static void main(String[] args) throws Exception {
        new Thread(NioClient::sendHello, "tom").start();
        new Thread(NioClient::sendHello, "jack").start();
        TimeUnit.HOURS.sleep(1);
    }

    private static void sendHello() {
        String name = Thread.currentThread().getName();
        SocketChannel sc = null;
        try {
            sc = SocketChannel.open();
            System.out.println("客户端" + name + "发起连接");
            sc.connect(new InetSocketAddress("localhost", 8888));
            for (int i = 0; i < 5; i++) {
                sc.write(ByteBuffer.wrap((name + "say-hello" + i + " ").getBytes(StandardCharsets.UTF_8)));
            }
            TimeUnit.SECONDS.sleep(1);
            for (int i = 5; i < 10; i++) {
                sc.write(ByteBuffer.wrap((name + "say-hello" + i + " ").getBytes(StandardCharsets.UTF_8)));
            }
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int read = sc.read(byteBuffer);
            System.out.println(new String(byteBuffer.array(), 0, read, StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
