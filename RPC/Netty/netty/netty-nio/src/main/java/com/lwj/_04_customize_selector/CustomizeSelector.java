package com.lwj._04_customize_selector;

import java.io.IOException;
import java.net.ProtocolFamily;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Pipe;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;

/**
 * Date: 2025/6/27
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class CustomizeSelector extends SelectorProvider {
    @Override
    public DatagramChannel openDatagramChannel() throws IOException {
        return null;
    }

    @Override
    public DatagramChannel openDatagramChannel(ProtocolFamily family) throws IOException {
        return null;
    }

    @Override
    public Pipe openPipe() throws IOException {
        return null;
    }

    @Override
    public AbstractSelector openSelector() throws IOException {
        return null;
    }

    @Override
    public ServerSocketChannel openServerSocketChannel() throws IOException {
        return null;
    }

    @Override
    public SocketChannel openSocketChannel() throws IOException {
        return null;
    }
}
