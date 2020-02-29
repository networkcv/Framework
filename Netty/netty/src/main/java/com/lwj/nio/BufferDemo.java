package com.lwj.nio;

import org.junit.Test;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * create by lwj on 2020/2/28
 */
public class BufferDemo {
    @Test
    public void test() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        //byteBuffer存储这些基本类型的本质，其实是put(byte[]),先进行类型转换，再存到buffer中
        // 如int类型会转为长度为4的byte[]，存的时候position会加四位
        byteBuffer.putChar((char) 1);
        byteBuffer.put((byte) 1);
        byteBuffer.putShort((short) 1);
        byteBuffer.putInt(1);
        byteBuffer.putLong(1L);
        byteBuffer.putFloat(1f);
        byteBuffer.putDouble(1d);
        System.out.println();
        System.out.println(byteBuffer); //java.nio.HeapByteBuffer[pos=29 lim=1024 cap=1024]
        ByteBuffer byteBuffer1 = byteBuffer.asReadOnlyBuffer(); //转换为只读buffer
        System.out.println(byteBuffer1);    //java.nio.HeapByteBufferR[pos=29 lim=1024 cap=1024]
        byteBuffer1.putInt(1);  //java.nio.ReadOnlyBufferException 只读Buffer无法进行写操作
    }

    /**
     * NIO还提供了MappedByteBuffer 抽象类（底层是DirectByteBuffer），
     * 可以让文件直接在内存（堆外内存）中进行修改，由NIO来完成同步到文件。
     */
    @Test
    public void test2() throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile("a.txt", "rw");
        FileChannel channel = randomAccessFile.getChannel();
        /*
            (MapMode mode)FileChannel.MapMode.READ_WRITE  读写模式
            (long position) 0   可以直接修改内存的起始位置
            (long size) 5   映射到内存的大小

         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        mappedByteBuffer.put(0, (byte) 'H');
        randomAccessFile.close();
    }

    /**
     * NIO 还支持 通过多个Buffer (即 Buffer 数组) 完成读写操作，即 Scattering 和 Gathering
     * Scattering：将数据写入到buffer时，可以采用buffer数组，依次写入
     * Gathering：从buffer读取数据时，可以采用buffer数组，依次读取
     */
    @Test
    public void test3() throws IOException {
        //创建一个ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //创建SocketAddress，用于描述端口信息
        SocketAddress socketAddress = new InetSocketAddress(6666);
        //为ServerSocketChannel设置监听端口，并启动
        serverSocketChannel.socket().bind(socketAddress);

        //创建buffer数组
        ByteBuffer[] byteBuffers = new ByteBuffer[2];
        byteBuffers[0] = ByteBuffer.allocate(5);
        byteBuffers[1] = ByteBuffer.allocate(3);

        //等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8;

        while (true) {
            //将Channel的数据读到Buffer
            int byteRead = 0;
            while (byteRead < messageLength) {
                long read = socketChannel.read(byteBuffers);
                byteRead += read;
                System.out.println("byteRead= " + byteRead);
                Arrays.stream(byteBuffers).forEach(b -> System.out.println("position= " + b.position() + " limit=" + b.limit()));
            }
            Arrays.stream(byteBuffers).forEach(Buffer::flip);

            //将Buffer中的数据写到Channel
            long byteWrite = 0;
            while (byteWrite < messageLength) {
                long write = socketChannel.write(byteBuffers);
                byteWrite += write;
            }

            Arrays.stream(byteBuffers).forEach(Buffer::clear);

            System.out.println("byteRead= "+byteRead+"byteWrite= "+byteWrite);
        }

    }

    @Test
    public void test4() {

    }
}
