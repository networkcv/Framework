package com.lwj.netty._9_dubborpc.provider;

import com.lwj.netty._9_dubborpc.netty.NettyServer;

/**
 * create by lwj on 2020/3/6
 */
//ServerBootStrap 会启动一个服务提供者，就是NettyServer
public class ServerBootstrap {
    public static void main(String[] args){
        NettyServer.startServer("127.0.0.1",8888);
    }
}
