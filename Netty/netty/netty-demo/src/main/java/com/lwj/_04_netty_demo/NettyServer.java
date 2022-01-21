package com.lwj._04_netty_demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Date: 2021/12/29
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        //1. 创建bossGroup线程组: 处理网络事件--连接事件
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        //2. 创建workerGroup线程组: 处理网络事件--读写事件
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        //3. 创建服务端启动助手
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        ChannelFuture channelFuture = serverBootstrap.group(bossGroup, workGroup)//4. 设置bossGroup线程组和workerGroup线程组
                .channel(NioServerSocketChannel.class)//5. 设置服务端通道实现为NIO
                .option(ChannelOption.SO_BACKLOG, 128)//6. 参数设置
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {//7. 创建一个通道初始化对象
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //8. 向pipeline中添加自定义业务处理handler
                        socketChannel.pipeline().addLast(new NettyServerHandler());
                    }
                })
                .bind(8888).sync();//9. 启动服务端并绑定端口,同时将异步改为同步
        System.out.println("server is ok");
        channelFuture.channel().closeFuture().sync();
        //10. 关闭通道和关闭连接池
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }
}
