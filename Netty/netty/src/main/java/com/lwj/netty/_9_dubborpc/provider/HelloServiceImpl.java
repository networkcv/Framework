package com.lwj.netty._9_dubborpc.provider;

import com.lwj.netty._9_dubborpc.publicinterface.HelloService;

/**
 * create by lwj on 2020/3/6
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        System.out.println("收到客户端消息= " + name);
        if (!name.equals("")) {
            return "你好" + name;
        } else {
            return "请输入你的名字";
        }
    }
}
