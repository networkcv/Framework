package com.lwj._99_other.bioDemo.server;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * create by lwj on 2020/4/4
 */
public class SocketServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(9999);
        System.out.println("服务器已启动");
//        Socket accept = serverSocket.accept();
//        System.out.println("已连接 " + accept.getInetAddress());
//        while (true) {
//            InputStream inputStream = accept.getInputStream();
//            byte[] bytes = new byte[1024];
//            int read = inputStream.read(bytes);
//            if (read != -1)
//                System.out.println("读到了：" + new String(bytes));
//            OutputStream outputStream = accept.getOutputStream();
//            outputStream.write("别看了，睡吧".getBytes());


//        }

    }
}
