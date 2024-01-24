package netty.ch6.exceptionspread;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.ch6.BusinessException;

public class ExceptionCaughtHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // ..

        if (cause instanceof BusinessException) {
            System.out.println("BusinessException");
        }
    }
}
