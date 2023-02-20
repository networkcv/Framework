package com.lwj._11_netty_protobuf;

import com.google.common.collect.Lists;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Date: 2022/1/20
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class SubscribeClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ctx.write(createReq(i));
        }
        ctx.flush();
    }

    private SubscribeProto.SubscribeReq createReq(int i) {
        return SubscribeProto.SubscribeReq.newBuilder()
                .setId(i)
                .setName("jack")
                .setProductName("iphone13")
                .addAllAddress(Lists.newArrayList("XiAN", "HangZhou"))
                .build();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("receive server msg" + msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
