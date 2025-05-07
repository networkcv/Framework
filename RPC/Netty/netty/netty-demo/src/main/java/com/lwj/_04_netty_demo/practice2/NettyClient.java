package com.lwj._04_netty_demo.practice2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.concurrent.TimeUnit;

/**
 * Date: 2025/5/7
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class NettyClient {
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ByteBuf delimiter = Unpooled.buffer();
                        delimiter.writeBytes("#$#".getBytes());
                        ch.pipeline()
//                                .addLast(new LineBasedFrameDecoder(1024))
                                .addLast(new DelimiterBasedFrameDecoder(1024, true, true, delimiter))
                                .addLast(new StringEncoder())
                                .addLast(new StringDecoder())
                                .addLast(new SimpleChannelInboundHandler<String>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                        System.out.println(msg);
                                    }
                                });
                    }
                });
        ChannelFuture connectFuture = bootstrap.connect("localhost", 9999);
        connectFuture.addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("客户端连接成功");
                connectFuture.channel().eventLoop().scheduleAtFixedRate(() -> {
//                    connectFuture.channel().writeAndFlush("hello " + System.currentTimeMillis() + "\n" + "hello " + System.currentTimeMillis() + "\n");
                    connectFuture.channel().writeAndFlush("hello " + System.currentTimeMillis() + "#$#" + "hello " + System.currentTimeMillis() + "#$#");
                }, 0, 1, TimeUnit.SECONDS);
            } else {
                System.out.println("客户端连接失败");
            }
        });
    }
}
