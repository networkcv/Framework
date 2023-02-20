package com.lwj._05_netty_decode;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Date: 2021/12/29
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class NettyClient {
    public static void main(String[] args) throws InterruptedException {
        //1. 创建线程组
        NioEventLoopGroup group = new NioEventLoopGroup();
        //2. 创建客户端启动助手
        Bootstrap bootstrap = new Bootstrap();

        ChannelFuture channelFuture = bootstrap.group(group)//3. 设置线程组
                .channel(NioSocketChannel.class)//4. 设置客户端通道实现为NIO
                .handler(new ChannelInitializer<SocketChannel>() {//5. 创建一个通道初始化对象
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(new MessageEncoder());
                        socketChannel.pipeline().addLast(new MessageDecoder());
                        socketChannel.pipeline().addLast(new NettyClientHandler());//6. 向pipeline中添加自定义业务处理handler
                    }
                })
                //7. 启动客户端,等待连接服务端,同时将异步改为同步
                .connect("localhost", 8888).sync();

        //8. 关闭通道和关闭连接池
        channelFuture.channel().closeFuture().sync();
        group.shutdownGracefully();
    }
}
