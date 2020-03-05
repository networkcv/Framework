package com.lwj.netty._2_groupchat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * create by lwj on 2020/3/3
 */
public class GroupChatServerHandler extends SimpleChannelInboundHandler<String> {
    //定义一个channel组，管理所有的channel
    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    //handlerAdded 表示连接建立，一旦连接，第一个被执行
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        //将该客户加入的信息推送给其他客户端
        channelGroup.add(channel);
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 加入聊天\n");
    }

    @Override
    //表示channel 处于活动状态，提示 xx 上线
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 上线");
    }

    @Override
    //表示channel 处于不活动的状态，表示 xx离线
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + " 离线");
    }

    @Override
    //断开连接
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[客户端]" + channel.remoteAddress() + " 离开聊天\n");
        //将该客户加入的信息推送给其他客户端
        channelGroup.remove(channel);
        System.out.println("channelGroup size" + channelGroup.size());
    }


    @Override
    //读取数据
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        //当前Channel
        Channel channel = ctx.channel();
        channelGroup.forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush("[客户 " + channel.remoteAddress() + "]:" + msg + "\n");
            } else {
                ch.writeAndFlush("[自己]:" + msg + "\n");
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ChannelFuture close = ctx.close();
    }
}
