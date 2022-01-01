package com.lwj._05_netty_encode_decode;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * Date: 2021/12/30
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class MessageEncoder extends MessageToMessageEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String msg, List<Object> list) throws Exception {
        System.out.println("正在编码");
        list.add(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
    }
}
