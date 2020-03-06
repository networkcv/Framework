package com.lwj.netty._9_dubborpc.consumer;

import com.lwj.netty._9_dubborpc.netty.NettyClient;
import com.lwj.netty._9_dubborpc.publicinterface.HelloService;

/**
 * create by lwj on 2020/3/6
 */
public class ClientBootstrap {
    public static void main(String[] args)  {
        HelloService helloService = (HelloService) new NettyClient().getBean(HelloService.class, "HelloService#hello#");
        System.out.println(helloService.hello(""));
        System.out.println(helloService.hello("jack"));

    }
}
