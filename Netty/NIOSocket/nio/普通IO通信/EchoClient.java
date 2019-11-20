package Netty.NIOSocket.nio.普通IO通信;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/13
 */
public class EchoClient {
    public static void main(String[] args) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            cachedThreadPool.execute(() -> {
                try {
                    Socket client = new Socket();
                    client.connect(new InetSocketAddress("localhost", 8888));
                    PrintWriter printWriter = new PrintWriter(client.getOutputStream(), true);
                    printWriter.print("H");
                    TimeUnit.MILLISECONDS.sleep(100);
                    printWriter.print("e");
                    TimeUnit.MILLISECONDS.sleep(100);
                    printWriter.print("l");
                    TimeUnit.MILLISECONDS.sleep(100);
                    printWriter.print("l");
                    TimeUnit.MILLISECONDS.sleep(100);
                    printWriter.print("o");
                    TimeUnit.MILLISECONDS.sleep(100);
                    printWriter.println();
                    printWriter.flush();

//                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
//                    String s = bufferedReader.readLine();
//                    System.out.println(s);

                    InputStream inputStream = client.getInputStream();
                    byte[] bytes = new byte[1204];
                    while (inputStream.read(bytes) != -1) {
                        System.out.println(new String(bytes));
                    }


                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
