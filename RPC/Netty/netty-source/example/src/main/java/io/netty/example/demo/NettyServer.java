package io.netty.example.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Netty服务端
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //1.创建bossGroup线程组: 处理网络事件--连接事件 线程数默认为: 2 * 处理器线程数
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //2.创建workerGroup线程组: 处理网络事件--读写事件 2 * 处理器线程数
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //3.创建服务端启动助手
        ServerBootstrap bootstrap = new ServerBootstrap();
        //4.设置bossGroup线程组和workerGroup线程组
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)//5.设置服务端通道实现
                .option(ChannelOption.SO_BACKLOG, 128)//6.参数设置-设置线程队列中等待连接个数
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)//6.参数设置-设置活跃状态,child是设置workerGroup
                .childHandler(new ChannelInitializer<SocketChannel>() {//7.创建一个通道初始化对象
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
//                        ch.pipeline().addLast(new NettyServerOutHandle());
                        //8.向pipeline中添加自定义业务处理handler
                        ch.pipeline().addLast(new NettyServerHandle());
                    }
                });
        //9.启动服务端并绑定端口,同时将异步改为同步
        ChannelFuture future = bootstrap.bind(9999);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    System.out.println("端口绑定成功!");
                } else {
                    System.out.println("端口绑定失败!");
                }

            }
        });
        System.out.println("服务器启动成功....");
        //10.关闭通道(并不是真正意义上的关闭,而是监听通道关闭状态)和关闭连接池
        future.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
