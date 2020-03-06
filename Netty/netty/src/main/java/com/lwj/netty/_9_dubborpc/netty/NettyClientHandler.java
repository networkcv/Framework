package com.lwj.netty._9_dubborpc.netty;

import com.sun.org.apache.xerces.internal.impl.dv.dtd.NMTOKENDatatypeValidator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.Callable;

/**
 * create by lwj on 2020/3/6
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<String> implements Callable<String> {
    private ChannelHandlerContext channelHandlerContext;
    private String res;
    private String parameter;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        channelHandlerContext = ctx;
    }

    @Override
    protected synchronized void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        res = msg;
        notifyAll();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    //被代理对象调用，发送数据给服务器，然后进入wait，在channelRead0接收到数据时再将其唤醒。
    @Override
    public synchronized String call() throws Exception {
        channelHandlerContext.writeAndFlush(parameter);
        wait();
        return res;
    }

    public void setParameter(String name) {
        parameter = name;
    }
}
