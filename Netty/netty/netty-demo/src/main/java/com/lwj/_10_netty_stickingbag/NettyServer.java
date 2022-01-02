package com.lwj._10_netty_stickingbag;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

/**
 * Netty服务端
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //1. 创建bossGroup线程组: 处理网络事件--连接事件
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //2. 创建workerGroup线程组: 处理网络事件--读写事件 2*处理器线程数
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //3. 创建服务端启动助手
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //4. 设置bossGroup线程组和workerGroup线程组
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class) //5. 设置服务端通道实现为NIO
                .option(ChannelOption.SO_BACKLOG, 128)//6. 参数设置
                .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)//6. 参数设置
                .childHandler(new ChannelInitializer<SocketChannel>() { //7. 创建一个通道初始化对象
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        //添加解码器
                        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                        //8. 向pipeline中添加自定义业务处理handler
                        ch.pipeline().addLast(new NettyServerHandler());
                    }
                });
        //9. 启动服务端并绑定端口,同时将异步改为同步
        ChannelFuture future = serverBootstrap.bind(9999);
        System.out.println("服务端启动成功.");
        //10. 关闭通道(并不是真正意义上关闭,而是监听通道关闭的状态)和关闭连接池
        future.channel().closeFuture().sync();
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }
}
