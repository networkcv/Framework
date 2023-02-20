package com.lwj._11_netty_protobuf;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Date: 2022/1/20
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class SubscribeServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeProto.SubscribeReq subscribeReq = (SubscribeProto.SubscribeReq) msg;
        if ("jack".equalsIgnoreCase(subscribeReq.getName()))
            System.out.println("server accept client subscribe req :" + subscribeReq);
        ctx.writeAndFlush(createRes(subscribeReq.getId()));
    }

    private SubscribeProto.SubscribeRes createRes(int id) {
        return SubscribeProto.SubscribeRes.newBuilder()
                .setId(id)
                .setResCode("200")
                .setDesc("ok")
                .build();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
