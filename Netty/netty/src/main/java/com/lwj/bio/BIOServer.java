package com.lwj.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * create by lwj on 2020/2/28
 */
public class BIOServer {
    public static void main(String[] args) throws IOException {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = new ServerSocket(666);
        System.out.println("server start");
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("link a Client");
            threadPool.submit(() -> {
                handler(socket);
            });
        }
    }

    public static void handler(Socket socket) {
        System.out.println("currentThread ID: "+Thread.currentThread().getId());
        byte[] bytes;
        InputStream inputStream;
        try {
            bytes = new byte[1024];
            inputStream = socket.getInputStream();
            int len;
            while ((len = inputStream.read(bytes)) != -1) {
                System.out.println("currentThread ID: "+Thread.currentThread().getId()+"wait  read ");
                String str = new String(bytes, 0, len);
                if ("exit".equals(str)) {
                    break;
                } else {
                    System.out.println(new String(bytes, 0, len));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
