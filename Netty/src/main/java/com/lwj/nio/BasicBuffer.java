package com.lwj.nio;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * create by lwj on 2020/2/28
 */
public class BasicBuffer {
    @Test
    public void test() throws IOException {
        //先定义输出流
        FileOutputStream fileOutputStream = new FileOutputStream("a.txt");
        //通过该流获取对应的Channel
        FileChannel channel = fileOutputStream.getChannel();
        //创建一个ByteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //将str放入Buffer，并进行flip
        byteBuffer.put("hello".getBytes()).flip();
        //将缓冲区的内容写到Channel
        int write = channel.write(byteBuffer);
        //关闭输出流
        fileOutputStream.close();
    }

    @Test
    public void test1() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("a.txt");
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        channel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }

    @Test
    public void test2() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("a.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("b.txt");
        FileChannel inputStreamChannel = fileInputStream.getChannel();
        FileChannel outputStreamChannel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (true) {
            byteBuffer.clear();
            int write = inputStreamChannel.read(byteBuffer);
            if (write == -1) {
                break;
            } else {
                byteBuffer.flip();
                outputStreamChannel.write(byteBuffer);
            }
        }
        fileInputStream.close();
        fileOutputStream.close();


    }
}
