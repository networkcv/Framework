package com.lwj.netty._9_dubborpc.netty;

import com.lwj.netty._9_dubborpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * create by lwj on 2020/3/6
 */
public class NettyServiceHandler extends SimpleChannelInboundHandler<String> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //获取客户端发送的消息，并调用服务
        String res = null;
        System.out.println("msg= " + msg);
        if (msg.startsWith("HelloService#hello#")) {
            res = new HelloServiceImpl().hello(msg.substring(msg.lastIndexOf("#") + 1));
        }
        ctx.writeAndFlush(res);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println(cause.getMessage());
        ctx.close();
    }
}
