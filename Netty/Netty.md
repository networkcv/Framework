## 1. Netty 概述

Netty 是由JBOSS提供的一个Java开源框架。

Netty 是一个 **异步** 的、**基于事件驱动** 、底层通过包装 **NIO** 来实现的  **网络** 应用框架，

在分布式系统中，各个节点之间需要远程服务调用，高性能的RPC框架必不可少，Netty作为异步高性能的通信框架，往往作为基础通信组件被这些RPC框架使用，如阿里的分布式服务框架Dubbo的RPC框架使用的Dubbo协议进行节点间通信，Dubbo协议默认使用Netty作为基础通信组件，用于实现各进程节点之间的内部通信。

## 2. I/O 模型

I/O 模型的简单理解：就是用怎样的通道进行数据的发送和接收，很大程度上决定了程序通信的性能。

### 2.1 BIO

BIO（Blocking I/O）：**同步阻塞**（传统阻塞型），服务器实现模式为一个网络连接对应一个线程，即服务端收到客户端的连接请求时，就启动一个线程进行处理，如果这个连接在保持的状态的下不做任何事情，就会造成不必要的线程开销，如果在读的时候没有读到东西，会发生阻塞。

![](D:\study\Framework\Netty\img\1582880020(1).jpg)

**BIO编程的简单流程：**

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
```



- 使用dos 命令  telnet localhost/127.0.0.1  666
- 使用` Ctrl + ] `  `send str` 回车发送字符串到服务端

**NIO 问题分析**：

- 每个请求都需要创建独立的线程，与对应的客户端进行数据Read，业务处理，数据Write。
- 当并发数较大时，需要 **创建大量线程来处理连接**，系统资源占用较大。
-  连接建立后，如果当前线程暂时没有数据可读，则线程就阻塞在Read操作上，造成线程资源浪费。



### 2.2 NIO

NIO（non -blocking IO）：**同步非阻塞** ，服务器实现模式为一个线程处理多个连接请求，即客户端发送的连接请求都会注册到多路复用器（Selector 选择器）上，多路复用器轮询到连接有I/O请求就进行处理。JDK1.4开始支持，NIO相关类都在java.nio包及子包下。

![](D:\study\Framework\Netty\img\1582880379(1).jpg)

NIO 有三大核心部分：Channel（通道）、Buffer（缓冲区）、Secletor（选择器）：

NIO 是面向缓冲区的，数据读取到一个它稍后处理的缓冲区，需要时可在缓冲区中前后移动，这样就增加了处理过程中的灵活性，使用它可以提供非阻塞式的高性能伸缩网络。

Java NIO 的非阻塞模式，使一个线程从某个通道发送请求或者读取数据，但是它仅能得到目前可用的数据，如果目前没有数据可用时，就什么都不会获取，而不是保持线程的阻塞，所以直至读到数据之前，该线程可以继续做任何事情。非阻塞写也是如此，一个线程请求写入一些数据到某通道，但不需要等待它完全写入，这个线程同时可以去做别的事情。通俗理解为：NIO 可以做到一个线程来处理多个请求的操作。

HTTP2.0使用了多路复用的技术，做到同个连接并发处理多个请求，而且并发请求的数量比HTTP1.1打了好几个数量级。

 **BIO和 NIO 的比较**：

- BIO 以流的方式处理数据，而NIO 以缓存区的方式处理数据，缓冲区IO效率比流IO高很多。
- BIO 是阻塞的，NIO 是非阻塞的。
- BIO 基于字节流和字符流操作，而NIO基于 Channel（通道）和 Buffer（缓冲区）进行操作，数据总是从通道读到缓冲区中，或者是从缓冲区写到通道中。Selector（选择器）用于监听多个通道的事件（如：连接请求，数据到达等），因此一个线程就可以监听多个客户端通道。

**NIO 三大核心关系：**

- Selector 对应一个线程，但一个线程对应多个Channel。
- 每个Channel 都对应一个Buffer。
- 每个线程切换到哪个Channel由事件（Event ）决定，Selector 会根据不同的事件，在各个 Channel 上切换
- Buffer 本身就一个内存块，底层是一个数组。
- NIO 的数据的读取写入是通过Buffer，Buffer 可以读也可以写，需要 flip（） 切换，  而 BIO 要么是输出流，要么是输入流，不能双向。
- Channel 也是双向的，可以返回底层操作系统的情况，比如Linux，底层的操作系统通道就是双向的。

### 2.3 AIO

AIO（NIO2）：异步非阻塞，AIO引入异步通道的概念，采用了Proactor模式，简化了程序编写，有效的请求才启动线程，它的特点是由操作系统完成后才通知服务端程序启动线程去处理，一般适用于连接数较多且连接时间较长的应用。JDK7开始支持。

### 2.2 BIO、NIO、AIO使用场景分析

- BIO 方式适用于连接数目比较小且固定的架构，这种方式对服务器资源要求比较高，唯一好处就是程序简单易理解。
- NIO 方式适用于连接数目多且连接比较短（轻操作）的架构，比如聊天服务器，弹幕系统，服务间通讯等，程序实现比较复杂。
- AIO 方式适用于连接数目多且连接比较长（重操作）的架构，比如相册服务器，充分调用OS参与并发操作，编程比较复杂，

## 3. 深入了解 NIO 

### 3.1 Buffer

基本介绍

缓冲区（Buffer）：缓冲区本质上是一个可以读写数据的内存块，可以理解成一个容器对象（含数组），该对象提供了一组方法，可以轻松的使用缓冲区，并且该对象内置了一些机制，能够追踪和记录缓冲区的状态变化情况，Channel提供了从文件、网络读取数据的渠道，但是读取或写入的数据都必须经由Buffer。

NIO程序 《- data -》  缓冲区  《- Channel -》 文件

该类为抽象类,有CharBuffer、IntBuffer、LongBuffer等7个抽象子类，不包含boolean类型  
在Buffer其中定义了缓冲区的共有特性和对其的操作方法   
**0  <=  mark  <=  position  <=  limit  <=  capacity**  

间接缓冲区：CharBuffer <=> JVM的中间缓冲区 <=> 硬盘  
直接缓冲区：CharBuffer <=> 硬盘

Buffer方法列表：

|               方法               |                             作用                             |
| :------------------------------: | :----------------------------------------------------------: |
|          int capacity()          |             返回 缓冲区的 capacity (容量) 总大小             |
|           int limit()            |                   返回 limit (限制) 的大小                   |
|    Buffer limit(int newLimit)    |   设置 limit  其所在的索引位置及后边的位置无法被读取或写入   |
|          int position()          |                     返回 position (位置)                     |
| Buffer position(int newPosition) |                设置下一次读取或写入的索引位置                |
|          Buffer mark()           |                  mark (标记)缓冲区当前位置                   |
|          Buffer reset()          |                   使 position 回到标记位置                   |
|         int remaining()          |               返回当前位置与limit之间的元素数                |
|      boolean hasRemaining()      |           返回当前位置与limit之间的是否有剩余元素            |
|       boolean isReadOnly()       |                 返回该缓冲区是否为只读缓冲区                 |
|        boolean isDirect()        |                 返回该缓冲区是否为直接缓冲区                 |
|          Buffer clear()          | 一切为默认，清理缓冲区的标志位，不会清理数据，<br />mark=-1，posistion=0,limit=capacity |
|        boolean hasArray()        | 判读此缓冲区是否具有可访问的底层实现数组，<br />间接缓冲区返回true，直接缓冲区返回false |
|        int arrayOffset()         | 返回此缓冲区的底层实现数组中第一个缓冲区元素的偏移量 <br />这个值在文档中标注为“可选操作”，子类也可以不处该值 |
|          Buffer flip()           | 当向缓冲区中存储数据，然后再从缓冲区是中读取这些数据时，<br />就是flip()方法的最佳时机  反转此缓冲区.首先将限制设置为当前位置,<br />然后将位置设置为0,如果已定义标记,则丢弃该标记. |

- rewind()  侧重于“重新”，重新读取、重新写入时可以使用
- clear()   侧重于“还原一切状态” 新的数据存储前使用
- flip()    侧重于“subString截取” 对最近存储数据的读取

**ByteBuffer**

Buffer的子类(例如ByteBuffer)对Buffer中的方法进行重写及补充，并添加产生对应缓冲区实例的静态方法  
具体的数据也是保存在该抽象类中  final char[] hb  

static ByteBuffer allocate(int capacity)  

根据capacity创建一个固定大小的间接缓冲区，具有底层实现数组，缓冲区类型为HeapByteBuffer  

static ByteBuffer allocateDirect(int capacity)   

根据capacity创建一个固定大小的直接缓冲区，缓冲区实际类型为DirectByteBuffer  
使用allocateDirect()创建ByteBuffer缓冲区时,capacity 指的是字节的个数  
而创建IntBuffer缓冲区时,capacity指的是int值得数目，转换成字节的话，还需乘以4  
直接缓冲区是直接与操作系统直接通信，间接缓冲区在内部对byte[]hb 字节数组操作，在JVM堆中进行数据处理

如何释放直接缓冲区的内存  
- 手动释放内存  
    通过反射调用DirectByteBuffer的cleaner,获取执行清理方法的对象，调用该对象的clean方法,立即回收内存
- 交给JVM来处理  
    不做处理，等待JVM的GC垃圾回器进行垃圾回收

> static ByteBuffer swap(byte[] arr)  

将传入数组封装为对应类型的间接缓冲区，使用数组作为储存空间，通过该数组的其他引用修改该数组，会影响缓冲区中的数据，反之亦然
> static ByteBuffer swap(byte[] array,int offset,int length)

capacity 为 array.length  posistion 为 offset  limit 为 offset+length 
    
> ByteBuffer put()/put(char[] src)/put(byte[]src,int offset,int length)

向缓冲区中添加数据,offset 为数组的偏移量

> ByteBuffer get()/get(char [] src)/get(byte[]dst,int offset,int length)

从缓冲区position所在索引位置取出数据,offset 为数组的偏移量

> put(int index, byte b) 和  get(int index)

写入/读取指定索引位置的值

> byte[] array()

将缓冲区中的元素，存入对应数组返回

> ByteBuffer slice()

创建新的字节缓冲区，其内容为调用者的缓冲区内容的共享子序列，新缓冲区从调用者缓冲区的当前位置开始

> ByteBuffer duplicate()

创建共享此缓冲区内容的新的字节缓冲区，修改双方亦可见，capacity、position、limit两者相同但独立

> CharBuffer asCharBuffer()

转为字符缓冲区，capacity和limit减半

> ByteBuffer asReadOnlyBuffer()

创建只读缓冲区，共用内容，但制度缓冲区不能做修改

> compact()

压缩此缓冲区，将缓冲区的当前位置和限制之间的字节复制到缓冲区的开始处

> equals()

比较两个缓冲区position与limit之间的数据是否完全一样

> compareTo()

如果在开始与结束的范围之间有一个字节不同，折返回两者的减数
如果在开始与结束的范围之间每个字节都相同，则返回两者remaining()的减数


#### 将数量大于10的数组元素，循环放入小于10的缓冲区中

```java
public void test(){
    byte[] byteArrayIn1 ={1,2,3,4,5,6,7,8,9,10,11,12}
    ByteBuffer bytebuffer = ByteBuffer.allocate(10);
    int getArrayIndex = 0;
    while( getArrayIndex < byteArrayIn1.length){
        int readLength = Math.min(bytebuffer.ramaining(),byteArrayIn1.length-getArrayIndex);
        bytebuffer.put(byteArrayIn1,getArrayIndex,readLength);
        bytebuffer.flip();
        byte [] getArray = bytebuffer.array();
        for(int i=0;i<bytebuffer.limit();i++){
            System.out.print(getArray[i]+ "" );
        }
        getArrayIndex = getArrayIndex +readLength;
        System.out.print();
        bytebuffer.clear();
    }
}
1 2 3 4 5 6 7 8 9 10
11 12
```

#### 将数量大于10的缓冲区，循环取出放入小于10的数组中

```java
public void test () {
    byte [] byteArrayIn = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
    ByteBuffer bytebuffer = ByteBuffer.wrap (byteArrayIn);
    byte[] byteArrayOut = new byte [5];
    while (bytebuffer.hasRemaining()){
        int readLength =Math.min(bytebuffer.remaining (), byteArrayOut.length);
        bytebuffer.get (byteArrayout, o, readLength);
        for (int i0; i i++) {
            System. out.print (byteArrayout [i] +"");
        }
        System. out.println();
    }
}
1 2 3 4 5
6 7 8 9 10
11 12
```

### 3.2 Channerl

基本介绍

NIO的通道类似于流，但也有如下区别：

- 通道可以同时进行读写，而流（Stream）只能读或者只能写
- 通道可以实现异步读写数据
- 通道可以从缓冲区读数据，也可以写数据。
- Channel 在 NIO 中是一个接口 `public interface Channel extends Closeable(){}`。
- 常用 Channel 类 FileChannel、DatagramChannel、ServerSocketChannel 和 SocketChannel。
- FileChannel 用于文件的数据读写，DatagramChannel 用于UDP的数据读写，ServerSocketChannel 和 SocketChannel 用于TCP的数据读写。 
- 请求先到服务器，服务器的ServerSocketChannelImpl对该客户端生成一个SocketChannelImpl，之后通过该Channel与服务端进行通讯。

FileChannel 类的常用方法：

```java
public int read(ByteBuffer dst)  	//从通道读取数据放到缓冲区中
public int write(ByteBuffer src)	//把缓冲区的数据写到通道中
public long transferFrom(ReadableByteBuffer src ,long position ,long count) 
//从目标通道中复制数据到当前通道
public long transferTo(long position ,long count ,ReadableByteBuffer target)   
//把数据从当前通道复制给目标通道
```

示例：本地文件写

```java
public static void main(String[] args) throws IOException {
    //先定义输出流
    FileOutputStream fileOutputStream = new FileOutputStream("c://a.txt");
    //通过该流获取对应的Channel
    FileChannel channel = fileOutputStream.getChannel();
    //创建一个ByteBuffer
    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    //将str放入Buffer，并进行flip
    byteBuffer.put("hello".getBytes()).flip();
    //将缓冲区的内容写到Channel
    int write = channel.write(byteBuffer);
    //关闭输出流
    fileOutputStream.close();
}
```

示例：本地文件读

### 3.3 


