## 1. Netty 概述

Netty 是由JBOSS提供的一个Java开源框架。

Netty 是一个 **异步** 的、**基于事件驱动** 的  **网络** 应用框架。

在分布式系统中，各个节点之间需要远程服务调用，高性能的RPC框架必不可少，Netty作为异步高性能的通信框架，往往作为基础通信组件被这些RPC框架使用，如阿里的分布式服务框架Dubbo的RPC框架使用的Dubbo协议进行节点间通信，Dubbo协议默认使用Netty作为基础通信组件，用于实现各进程节点之间的内部通信。

## 2. I/O 模型

I/O 模型的简单理解：就是用怎样的通道进行数据的发送和接收，很大程度上决定了程序通信的性能。

### 2.1 BIO

BIO（Blocking I/O）：**同步阻塞**（传统阻塞型），服务器实现模式为一个网络连接对应一个线程，即服务端收到客户端的连接请求时，就启动一个线程进行处理，如果这个连接在保持的状态的下不做任何事情，就会造成不必要的线程开销，如果在读的时候没有读到东西，会发生阻塞。

![](D:\study\Framework\Netty\img\1582880020(1).jpg)

BIO编程的简单流程：

- 服务端启动一个ServerSocket
- 客户端启动Socket对服务器进行通信（默认情况下服务端需要对每个客户建立一个线程与之通讯）。
- 客户端发出请求后，判断服务器是否有线程响应，如果没有则会一直等待至请求超时，或者收到服务端拒绝连接的响应。
- 如果有响应，客户端线程会等待请求结束后，再继续执行。

附上代码：

```java
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
        try {
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();
            int len;
            while (true) {
                len = inputStream.read(bytes);
                if (len != -1 && !new String(bytes, 0, len).equals("exit")) {
                    System.out.println(new String(bytes, 0, len));
                } else {
                    break;
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
```



- 使用dos 命令  telnet localhost/127.0.0.1  666
- 



### 2.2 NIO

NIO：**同步非阻塞** ，服务器实现模式为一个线程处理多个连接请求，即客户端发送的连接请求都会注册到多路复用器（Selector 选择器）上，多路复用器轮询到连接有I/O请求就进行处理。

![](D:\study\Framework\Netty\img\1582880379(1).jpg)



### 2.3 AIO

AIO（NIO2）：异步非阻塞，AIO引入异步通道的概念，采用了Proactor模式，简化了程序编写，有效的请求才启动线程，它的特点是由操作系统完成后才通知服务端程序启动线程去处理，一般适用于连接数较多且连接时间较长的应用。

### 2.2 BIO、NIO、AIO使用场景分析

- BIO 方式适用于连接数目比较小且固定的架构，这种方式对服务器资源要求比较高，唯一好处就是程序简单易理解。
- NIO 方式适用于连接数目多且连接比较短（轻操作）的架构，比如聊天服务器，弹幕系统，服务间通讯等，程序实现比较复杂。JDK1.4开始支持。
- AIO 方式适用于连接数目多且连接比较长（重操作）的架构，比如相册服务器，充分调用OS参与并发操作，编程比较复杂，JDK7开始支持。

## 