package com.lwj.jprofiler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Date: 2022/9/6
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class BIOThreadTest {
    public static void main(String[] args) throws IOException {
        System.out.println("ok");
        ServerSocket serverSocket = new ServerSocket(8080);
        Socket accept = serverSocket.accept();
    }
}
