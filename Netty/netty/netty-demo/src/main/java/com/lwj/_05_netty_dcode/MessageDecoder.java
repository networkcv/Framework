package com.lwj._05_netty_dcode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * Date: 2021/12/30
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("正在解码");
        list.add(byteBuf.toString(CharsetUtil.UTF_8));
    }
}
