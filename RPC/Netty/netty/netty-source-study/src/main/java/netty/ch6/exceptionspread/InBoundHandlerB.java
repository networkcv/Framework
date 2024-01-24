package netty.ch6.exceptionspread;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.ch6.BusinessException;

/**
 * @author
 */
public class InBoundHandlerB extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        throw new BusinessException("from InBoundHandlerB");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("InBoundHandlerB.exceptionCaught()");

        ctx.fireExceptionCaught(cause);
    }
}
