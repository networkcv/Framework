package com.lwj._99_other.bioDemo.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * create by lwj on 2020/4/4
 */
public class SocketClient {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("192.168.0.104", 9999);
//        OutputStream outputStream = socket.getOutputStream();
//        InputStream inputStream = socket.getInputStream();
//        byte[] bytes = new byte[1024];
//        int len;
//        while (true) {
//            System.out.println("请输入你要发送的消息：");
//            Scanner scanner = new Scanner(System.in);
//            String s = scanner.nextLine();
//            outputStream.write(s.getBytes());
//            while ((len=inputStream.read(bytes))!=-1){
//                System.out.println(new String(bytes));
//            }
//    }


    }
}
