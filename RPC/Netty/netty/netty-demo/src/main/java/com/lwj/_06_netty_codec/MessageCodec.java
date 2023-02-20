package com.lwj._06_netty_codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * Date: 2021/12/31
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class MessageCodec extends MessageToMessageCodec<ByteBuf, String> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String msg, List<Object> list) throws Exception {
        System.out.println("正在编码");
        list.add(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("正在解码");
        list.add(byteBuf.toString(CharsetUtil.UTF_8));
    }
}
