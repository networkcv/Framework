package com.lwj._00_bio.chat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

/**
 * Date: 2022/1/26
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class BioChatServer {

    private final Integer port;
    private final Vector<Socket> socketList = new Vector<>();

    public BioChatServer(Integer port) {
        this.port = port;
    }

    public static void main(String[] args) {
        BioChatServer bioChatServer = new BioChatServer(8888);
        bioChatServer.start();
    }

    private void start() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("bio chat server start");
            while (true) {
                Socket accept = serverSocket.accept();
                new Thread(() -> {
                    InetSocketAddress inetSocketAddress = (InetSocketAddress) accept.getRemoteSocketAddress();
                    System.out.println("客户端-" + inetSocketAddress.getPort() + "已连接");
                    socketList.add(accept);
                    System.out.println("当前连接数:" + socketList.size());
                    readFormClient(accept);
                }).start();
            }
        } catch (IOException e) {
            System.out.println("bio chat server error");
            e.printStackTrace();
        }
    }

    private void readFormClient(Socket socket) {
        try {
            InputStream is = socket.getInputStream();
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
            byte[] msgByte = new byte[1024];
            int readLen;
            while ((readLen = is.read(msgByte)) > 0) {
                String msg = new String(msgByte, 0, readLen, StandardCharsets.UTF_8);
                System.out.println("msg from client-" + inetSocketAddress.getPort() + ":[" + msg + "]");
                sendToOthers("client-" + inetSocketAddress.getPort() + ":" + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendToOthers(String msg) {
        socketList.stream().filter(socket -> {
            InetSocketAddress inetSocketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
            return !msg.contains(String.valueOf(inetSocketAddress.getPort()));
        }).forEach(socket -> this.sendToClient(socket, msg));
    }

    public void sendToClient(Socket socket, String msg) {
        if (socket == null || msg.isEmpty()) {
            return;
        }
        try {
            OutputStream os = socket.getOutputStream();
            os.write(msg.getBytes(StandardCharsets.UTF_8));
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
