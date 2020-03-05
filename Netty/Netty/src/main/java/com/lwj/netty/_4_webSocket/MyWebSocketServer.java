package com.lwj.netty._4_webSocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * create by lwj on 2020/3/3
 */
public class MyWebSocketServer {
    public void run() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //因为基于http协议，使用http的编码解码器
                            pipeline.addLast(new HttpServerCodec());
                            //是以块方式写，添加 ChunkedWriterHandler 处理
                            pipeline.addLast(new ChunkedWriteHandler());

                            /*
                            1.http在传输过程中是分段的，HttpObjectAggregator ，将多个http多个段聚合起来
                            2.这就是为什么，当浏览器发送大量数据时，会发生多次http请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));

                            /*
                            1.对应websocket,它的数据是以 帧(frame) 形式传递
                            2.可以看到WebSocketFrame 下面有六个子类
                            3.浏览器请求时，ws://localhost:7000/xxx 表示请求的uri
                            4.WebSocketServerProtocolHandler 核心功能将http协议升级为 ws协议，保持长连接
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/"));

                            pipeline.addLast(new MyTextWebSocketFrameHandler());
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new MyWebSocketServer().run();
    }
}
