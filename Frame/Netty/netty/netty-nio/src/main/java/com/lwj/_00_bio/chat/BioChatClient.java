package com.lwj._00_bio.chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Date: 2022/1/26
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class BioChatClient {
    private Socket socket;

    public static void main(String[] args) {
        for (int i = 1; i < 2; i++) {
//            new Thread(() -> {
            BioChatClient bioChatClient = new BioChatClient();
            bioChatClient.connect();
//            bioChatClient.send("客户端消息" + i);
            Scanner scanner = new Scanner(System.in);
            new Thread(bioChatClient::readFromServer).start();
//            while (true) {
//                String inputStr = scanner.nextLine();
//                bioChatClient.send(inputStr);
                bioChatClient.send("inputStr");
//            }
//            }).start();
//            bioChatClient.close();
        }
    }


    private void connect() {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress("localhost", 8888));
            System.out.println("client 连接成功");
        } catch (IOException e) {
            System.out.println("bio client error");
            e.printStackTrace();
        }
    }

    private void send(String msg) {
        OutputStream outputStream;
        try {
            outputStream = socket.getOutputStream();
            byte[] msgBytes = msg.getBytes(StandardCharsets.UTF_8);
            outputStream.write(msgBytes);
            outputStream.flush();
//            System.out.println("client msg 发送成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFromServer() {
        try {
            InputStream is = socket.getInputStream();
            while (true) {
                byte[] msgByte = new byte[1024];
                int readLen;
                while ((readLen = is.read(msgByte)) > 0) {
                    String msg = new String(msgByte, 0, readLen, StandardCharsets.UTF_8);
//                    System.out.println("msg from server-" + ":[" + msg + "]");
                    System.out.println(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
    }

}
