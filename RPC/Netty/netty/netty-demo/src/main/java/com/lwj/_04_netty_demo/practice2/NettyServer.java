package com.lwj._04_netty_demo.practice2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Date: 2025/5/7
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class NettyServer {
    public static void main(String[] args) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(2);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        ByteBuf delimiter = Unpooled.buffer();
                        delimiter.writeBytes("#$#".getBytes());
                        ch.pipeline()
                                //netty提供的按行解析器，一定要放在字符串解码器前，先分割字节数组再将字节数组转化为字符串
//                                .addLast(new LineBasedFrameDecoder(1024))
                                //基于分隔符的数据帧解析器
                                .addLast(new DelimiterBasedFrameDecoder(1024, true, true, delimiter))
                                .addLast(new StringEncoder())
                                .addLast(new StringDecoder())
                                .addLast(new SimpleChannelInboundHandler<String>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                        System.out.println(msg);
                                        //调用ctx的writeAndFlush后，msg不再经历后续的handler，会从当前handler直接向前遍历返回
//                                        ctx.writeAndFlush()
                                        ctx.channel().writeAndFlush(msg + " word" + "#$#");
                                        ctx.fireChannelRead(msg);
                                    }
                                });
                    }
                });
        ChannelFuture bindFuture = serverBootstrap.bind(9999);
        bindFuture.addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("服务端启动成功");
            } else {
                System.out.println("服务端启动失败");
            }
        });
    }
}
