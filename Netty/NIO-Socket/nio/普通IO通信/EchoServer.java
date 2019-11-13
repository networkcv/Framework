package nio.普通IO通信;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * create by lwj on 2019/11/13
 */
public class EchoServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println(clientSocket.getRemoteSocketAddress() + " connect!");
            new Thread(() -> {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter printWriter = new PrintWriter(clientSocket.getOutputStream(), true);
                    long start = System.currentTimeMillis();
                    String inputLine;
                    while ((inputLine = bufferedReader.readLine()) != null) {
                        printWriter.println(inputLine);
                    }
                    long end = System.currentTimeMillis();
                    System.out.println("spend " + (end - start) + " ms");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}
