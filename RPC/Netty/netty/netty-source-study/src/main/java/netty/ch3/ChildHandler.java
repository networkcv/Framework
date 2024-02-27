package netty.ch3;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import netty.ch6.AuthHandler;


/**
 * Date: 2024/2/27
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class ChildHandler extends ChannelInitializer<SocketChannel> {
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        super.handlerAdded(ctx);
        System.out.println("ClientCandlerAdded");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("ClientChannelActive");
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ch.pipeline().addLast(new AuthHandler());
        //..
    }
}
