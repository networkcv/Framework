

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

JDK 7 引入了 Asynchronous I/O，即 AIO。在进行 I/O 编程中，常用到两种模式：Reactor和 Proactor。Java 的 NIO 就是 Reactor，当有事件触发时，服务器端得到通知，进行相应的处理

AIO 即 NIO2.0，叫做异步不阻塞的 IO。AIO 引入异步通道的概念，采用了 Proactor 模式，简化了程序编写，有效的请求才启动线程，它的特点是先由操作系统完成后才通知服务端程序启动线程去处理，一般适用于连接数较多且连接时间较长的应用

目前 AIO 还没有广泛应用，Netty 也是基于NIO, 而不是AIO， 

AIO原理与Linux系统IO介绍 ：[http](http://www.52im.net/thread-306-1-1.html)[://](http://www.52im.net/thread-306-1-1.html)[www.52im.net/thread-306-1-1.html](http://www.52im.net/thread-306-1-1.html) 



### 2.4 IO 模型使用场景

- BIO 方式适用于连接数目比较小且固定的架构，这种方式对服务器资源要求比较高，唯一好处就是程序简单易理解。
- NIO 方式适用于连接数目多且连接比较短（轻操作）的架构，比如聊天服务器，弹幕系统，服务间通讯等，程序实现比较复杂。
- AIO 方式适用于连接数目多且连接比较长（重操作）的架构，比如相册服务器，充分调用OS参与并发操作，编程比较复杂。

### 2.5 阻塞类型理解

1. **同步阻塞**：到理发店理发，就一直等理发师，直到轮到自己理发。
2. **同步非阻塞**：到理发店理发，发现前面有其它人理发，给理发师说下，先干其他事情，一会过来看是否轮到自己.
3. **异步非阻塞**：给理发师打电话，让理发师上门服务，自己干其它事情，理发师自己来家给你理发

## 3. 深入了解 NIO 

### 3.1 Buffer

#### 3.1.1 基本介绍

缓冲区（Buffer）：缓冲区本质上是一个可以读写数据的内存块，可以理解成一个容器对象（含数组），该对象提供了一组方法，可以轻松的使用缓冲区，并且该对象内置了一些机制，能够追踪和记录缓冲区的状态变化情况，Channel提供了从文件、网络读取数据的渠道，但是读取或写入的数据都必须经由Buffer。

NIO程序 《- data -》  缓冲区  《- Channel -》 文件

该类为抽象类,有CharBuffer、IntBuffer、LongBuffer等7个抽象子类，不包含boolean类型  
在Buffer其中定义了缓冲区的共有特性和对其的操作方法   
**0  <=  mark  <=  position  <=  limit  <=  capacity**  

间接缓冲区：CharBuffer <=> JVM的中间缓冲区 <=> 硬盘  
直接缓冲区：CharBuffer <=> 硬盘

#### 3.1.2 Buffer方法列表

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
|             rewind()             |          侧重于“重新”，重新读取、重新写入时可以使用          |

**rewind（）、clear（）、flip（）比较：**

- rewind()  侧重于“重新”，重新读取、重新写入时可以使用
- clear()   侧重于“还原一切状态” 新的数据存储前使用
- flip()    侧重于“subString截取” 对最近存储数据的读取

#### 3.1.3 ByteBuffer基本介绍

Buffer的子类(例如ByteBuffer)对Buffer中的方法进行重写及补充，并添加产生对应缓冲区实例的静态方法  
具体的数据也是保存在该抽象类中  final char[] hb  

static ByteBuffer allocate(int capacity)  

根据capacity创建一个固定大小的间接缓冲区，具有底层实现数组，缓冲区类型为HeapByteBuffer  

static ByteBuffer allocateDirect(int capacity)   

根据capacity创建一个固定大小的直接缓冲区，缓冲区实际类型为DirectByteBuffer  
使用allocateDirect()创建ByteBuffer缓冲区时,capacity 指的是字节的个数  
而创建IntBuffer缓冲区时,capacity指的是int值得数目，转换成字节的话，还需乘以4  
直接缓冲区是直接与操作系统直接通信，间接缓冲区在内部对byte[]hb 字节数组操作，在JVM堆中进行数据处理

NIO还提供了MappedByteBuffer 抽象类（底层是DirectByteBuffer），可以让文件直接在内存（堆外内存）中进行修改，由NIO来完成同步到文件。

```java
public void test2() throws IOException {
    RandomAccessFile randomAccessFile = new RandomAccessFile("a.txt","rw");
    FileChannel channel = randomAccessFile.getChannel();
    /*
            (MapMode mode)FileChannel.MapMode.READ_WRITE  读写模式
            (long position) 0   可以直接修改内存的起始位置
            (long size) 5   映射到内存的大小

         */
    MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
    mappedByteBuffer.put(0, (byte) 'H');
    randomAccessFile.close();
}
```

如何释放直接缓冲区的内存  ：
- 手动释放内存  
    通过反射调用DirectByteBuffer的cleaner,获取执行清理方法的对象，调用该对象的clean方法,立即回收内存
    
- 交给JVM来处理  
    不做处理，等待JVM的GC垃圾回器进行垃圾回收
    
    

#### 3.1.4 ByteBuffer 方法列表

|                                                              |                                                              |
| :----------------------------------------------------------: | ------------------------------------------------------------ |
|              static ByteBuffer swap(byte[] arr)              | 将传入数组封装为对应类型的间接缓冲区，使用数组作为储存空间，通过该数组的其他引用修改该数组，会影响缓冲区中的数据，反之亦然 |
|  static ByteBuffer swap(byte[] array,int offset,int length)  | capacity 为 array.length  posistion 为 offset  limit 为 offset+length |
| ByteBuffer put()/put(char[] src)/put(byte[]src,int offset,int length) | 向缓冲区中添加数据,offset 为数组的偏移量                     |
| ByteBuffer get()/get(char [] src)/get(byte[]dst,int offset,int length) | 从缓冲区position所在索引位置取出数据,offset 为数组的偏移量   |
|          put(int index, byte b) 和  get(int index)           | 写入/读取指定索引位置的值                                    |
|                        byte[] array()                        | 将缓冲区中的元素，存入对应数组返回                           |
|                      ByteBuffer slice()                      | 创建新的字节缓冲区，其内容为调用者的缓冲区内容的共享子序列，新缓冲区从调用者缓冲区的当前位置开始 |
|                    ByteBuffer duplicate()                    | 创建共享此缓冲区内容的新的字节缓冲区，修改双方亦可见，capacity、position、limit两者相同但独立 |
|                  CharBuffer asCharBuffer()                   | 转为字符缓冲区，capacity和limit减半                          |
|                ByteBuffer asReadOnlyBuffer()                 | 创建只读缓冲区，共用内容，但制度缓冲区不能做修改             |
|                          compact()                           | 压缩此缓冲区，将缓冲区的当前位置和限制之间的字节复制到缓冲区的开始处 |
|                           equals()                           | 比较两个缓冲区position与limit之间的数据是否完全一样          |
|                         compareTo()                          | 如果在开始与结束的范围之间有一个字节不同，折返回两者的减数<br/>如果在开始与结束的范围之间每个字节都相同，则返回两者remaining()的减数 |
|                ByteBuffer wrap(byte[] array)                 | 将一个字节数组包装到一个ByteBuffer中返回                     |

#### 3.1.5 操作示例

将数量大于10的数组元素，循环放入小于10的缓冲区中

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

将数量大于10的缓冲区，循环取出放入小于10的数组中

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

#### 3.2.1 基本介绍

NIO的通道类似于流，但也有如下区别：

- 通道可以同时进行读写，而流（Stream）只能读或者只能写
- 通道可以实现异步读写数据
- 通道可以从缓冲区读数据，也可以写数据。
- Channel 在 NIO 中是一个接口 `public interface Channel extends Closeable(){}`。
- 常用 Channel 类 FileChannel、DatagramChannel、ServerSocketChannel 和 SocketChannel。
- FileChannel 用于文件的数据读写，DatagramChannel 用于UDP的数据读写，ServerSocketChannel 和 SocketChannel 用于TCP的数据读写。 
- 请求先到服务器，服务器的ServerSocketChannelImpl对该客户端生成一个SocketChannelImpl，之后通过该Channel与服务端进行通讯。

#### 3.2.2 FileChannel 类的常用方法

```java
//从 通道 读出数据到 缓冲区，对通道进行读操作，读出的数据输出到dst中
public int read(ByteBuffer dst) 
//把 缓冲区 写入数据到  通道，对通道进行写操作，要写入的数据存放在src中
public int write(ByteBuffer src)
//从目标通道中复制数据到当前通道
public long transferFrom(ReadableByteBuffer src ,long position ,long count) 
//把数据从当前通道复制给目标通道
public long transferTo(long position ,long count ,ReadableByteBuffer target)   
    
```

#### 3.2.3 示例1：本地文件写

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

#### 3.2.4 示例2：本地文件读

```java
    public void test1() throws IOException {
        FileInputStream fileInputStream = new FileInputStream("a.txt");
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        channel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
    }
```

#### 3.2.5 示例3：文件拷贝1

```java
public void test2() throws IOException {
    FileInputStream fileInputStream = new FileInputStream("a.txt");
    FileOutputStream fileOutputStream = new FileOutputStream("b.txt");
    FileChannel inputStreamChannel = fileInputStream.getChannel();
    FileChannel outputStreamChannel = fileOutputStream.getChannel();
    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    while (true) {
        byteBuffer.clear(); // 1
        int write = inputStreamChannel.read(byteBuffer); //
        if (write == -1) {
            break;
        } else {
            byteBuffer.flip();
            outputStreamChannel.write(byteBuffer);
            //              byteBuffer.flip();  2
            //				操作 1 和 2 必须保留一个，因为write操作后，position与limit重合，需要对position进行复位才能进行后续操作
        }
    }
    fileInputStream.close();
    fileOutputStream.close();
}
```

#### 3.2.6 示例4：文件拷贝

```java
public void test4() throws IOException {
    //resource在发布时会打包到classpath下
    URL resource = getClass().getClassLoader().getResource("girl.png");
    FileInputStream fileInputStream = new FileInputStream(resource.getFile());
    FileChannel inputStreamChannel = fileInputStream.getChannel();
    FileOutputStream fileOutputStream = new FileOutputStream("mm.jpg");
    FileChannel outputStreamChannel = fileOutputStream.getChannel();
    //        	inputStreamChannel.transferTo(0,inputStreamChannel.size(),outputStreamChannel);
    outputStreamChannel.transferFrom(inputStreamChannel,0,inputStreamChannel.size());
    inputStreamChannel.close();
    outputStreamChannel.close();
    fileInputStream.close();
    fileOutputStream.close();
}
```



### 3.3  Selector



- Java 的 NIO，用非阻塞的 IO 方式。可以用一个线程，处理多个的客户端连接，就会使用到Selector（选择器）

- Selector 能够检测多个注册的通道上是否有事件发生（注意 Channel 以事件的方式可以注册到同一个Selector），如果有事件发生，便获取事件然后针对每个事件进行相应的处理。这样就可以只用一个单线程去管理多个通道，也就是管理多个连接和请求。
- 只有在 连接/通道 真正有读写事件发生时，才会进行读写，就大大地减少了系统开销，并且不必为每个连接都创建一个线程，不用去维护多个线程
- 避免了多线程之间的上下文切换导致的开销

要点说明：

- Netty 的 IO 线程 NioEventLoop 聚合了 Selector(选择器，也叫多路复用器)，可以同时并发处理成百上千个客户端连接。
- 当线程从某客户端 Socket 通道进行读写数据时，若没有数据可用时，该线程可以进行其他任务。
- 线程通常将非阻塞 IO 的空闲时间用于在其他通道上执行 IO 操作，所以单独的线程可以管理多个输入和输出通道。
- 由于读写操作都是非阻塞的，这就可以充分提升 IO 线程的运行效率，避免由于频繁 I/O 阻塞导致的线程挂起。
- 一个 I/O 线程可以并发处理 N 个客户端连接和读写操作，这从根本上解决了传统同步阻塞 I/O 一连接一线程模型，架构的性能、弹性伸缩能力和可靠性都得到了极大的提升。

Selector 类是一个抽象类，常用的方法和说明如下：

public abstract class Selector implements Closeable {}

- public static Selector open（）；	//得到一个选择器对象
- public abstract int select() throws IOException;	//阻塞的，一直监听直到注册的Channel中有事件返回，并将Selectionkey加入内部集合
- public abstract Selector wakeup()；	//唤醒selector
- public abstract int selectNow() throws IOException;	//不阻塞，立马返还
- public int select （long timeout）;	//监控所有注册的通道，当其中有 IO 操作可以进行时，将对应的SelectionKey 加入到内部集合中并返回，参数用来设置超时时间
- public Set<SelectionKey> selectedKeys（）;	//从内部集合中得到所有的 SelectionKey 



#### 3.3.1 NIO 非阻塞式网络编程原理分析图

![](D:\study\Framework\Netty\img\NIO 非阻塞式网络编程原理分析图.jpg)

- 当客户端连接时，会通过ServerSocketChannel 得到 SocketChannel
- selector 开始监听，通过 阻塞的select（）或者非阻塞的 select（timeout）方法，返回有事件发生的通道个数，这也就是为什么select方法返回的是int值。
- 将socketChannel 对象注册到 Selecotor 上，register（Selector sel ，int ops），一个Seletor可以注册多个Channel
- 注册后返回一个SelectionKey，这个key 会被该Selector 保存到内部的集合中
- 进一步得到所有发生事件的SelectionKey
- 再通过 SelectionKey 中的方法 channel() 反向获取 SocketChannel 
- 可以通过得到的Channel，完成业务处理

#### 3.3.2 ServerSocketChannel 类

在服务端监听新的客户端Socket连接

```java
public abstract class ServerSocketChannel extends AbstractSelectableChannel
	implements NetworkChannel{
    public static ServerSocketChannel open();//得到一个ServerSocketChannel通道
 	public final ServerSocketChannel bind(SocketAddress local); //设置服务器信息（ip和端口）
    public final SelectableChannel configureBlocking(boolean block);//设置阻塞或非阻塞模式，取值false表示采用非阻塞模式
    public abstract SocketChannel accept(); //接受一个连接，返回代表这个链接的通道对象
    public final SelectionKey register(Selector sel, int ops);//注册一个选择器并设置监听事件
     

}
```

#### 3.3.3 SocketChannel 类

网络IO通道，具体负责进行读写操作，NIO把缓冲区的数据写入通道，或者把通道里的数据读到缓冲区。

```java
public abstract class SocketChannel extends AbstractSelectableChannel  implements ByteChannel, ScatteringByteChannel, GatheringByteChannel, NetworkChannel{
public static SocketChannel open();//得到一个 SocketChannel 通道
public final SelectableChannel configureBlocking(boolean block);//设置阻塞或非阻塞模式，取值 false 表示采用非阻塞模式
public boolean connect(SocketAddress remote);//连接服务器
public boolean finishConnect();//如果上面的方法连接失败，接下来就要通过该方法完成连接操作
public int write(ByteBuffer src);//往通道里写数据
public int read(ByteBuffer dst);//从通道里读数据
public final SelectionKey register(Selector sel, int ops, Object att);//注册一个选择器并设置监听事件，最后一个参数可以设置共享数据
public final void close();//关闭通道
}

```

#### 3.3.4 SelectionKey 类

```java

public abstract class SelectionKey {
    protected SelectionKey() { }
    
    //返回与SelectionKey对应的channel对象
    public abstract SelectableChannel channel();

    //返回生成该key的Selector对象
    public abstract Selector selector();

    public abstract boolean isValid();

    public abstract void cancel();

    public abstract int interestOps();

    public abstract SelectionKey interestOps(int ops);

    public abstract int readyOps();

    public static final int OP_READ = 1 << 0;	//读操作 1
    public static final int OP_WRITE = 1 << 2;	//写操作 4
    public static final int OP_CONNECT = 1 << 3; //连接已建立 8
    public static final int OP_ACCEPT = 1 << 4;	//有新的网络可以连接 16

    //判断操作类型的方法
    public final boolean isReadable() 
    public final boolean isWritable() 
    public final boolean isConnectable() 
    public final boolean isAcceptable() 

    //用来存放共享缓冲区
    private volatile Object attachment = null;
   
    //可以将一个缓冲区绑定到Key上
    public final Object attach(Object ob) {
        return attachmentUpdater.getAndSet(this, ob);
    }
	
    //返回该Key对应的缓冲区
    public final Object attachment() {
        return attachment;
    }

}
```



#### 3.3.5 NIO小程序

服务器端的NIO监听程序：

```java
	/**
     * 服务器端的NIO监听程序
     */
    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 创建Selector
        Selector selector = Selector.open();
        // 绑定ip
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为不阻塞
        serverSocketChannel.configureBlocking(false);
        // 将ServerSocketChannel注册到selector。指定关心的事件为OP_ACCEPT，
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 当有关心的事件发生时，会返回这个SelectionKey，通过SelectionKey可以拿到Channel
        while (true) {
            // Selector监听，等于0说明此时没有事件发生。
            if (selector.select(1000) == 0) {
                System.out.println("server monitor 1s");
                continue;
            }
            Set<SelectionKey> keys = selector.selectedKeys();   //获取所有已注册的Channel中发生事件的SelectionKey，
//            Set<SelectionKey> keys1 = selector.keys();    // 获取所有已注册的Channel的SelectionKey
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                // 获得SocketChannel，此处的accept不会阻塞
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 此处socketChannel也要设置为非阻塞模式
                    socketChannel.configureBlocking(false);
                    // 注册Selector。第三个参数是连接的对象，通过SelectionKey可以连接到这个对象
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println("client has connect , socketChannel: " + socketChannel.hashCode());
                }
                if (key.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) key.channel();
                    ByteBuffer byteBuffer = (ByteBuffer) key.attachment();
                    socketChannel.read(byteBuffer);
                    System.out.println("client :" + new String(byteBuffer.array()));
                }
                // 手动删除避免重复
                iterator.remove();
            }

        }
    }
```



客户端

```java
 public static void main(String[] args) throws IOException {
        //得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //设置非阻塞
        socketChannel.configureBlocking(false);
        //提供服务器的IP和端口
        InetSocketAddress inetSocketAddress = new InetSocketAddress("127.0.0.1", 6666);
        //连接服务器
        if(!socketChannel.connect(inetSocketAddress)){
            while(!socketChannel.finishConnect()){
                System.out.println("因为连接需要时间，客户端不会阻塞，可以做其他工作");
            }
        }
        //连接成功，发送数据
        String str="hello";
        ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
        socketChannel.write(byteBuffer);
        System.in.read();
    }
```

#### 3.3.6 群聊系统

iterator.remove(); 和 key.cancel();的区别？

服务端代码：

```java
/**
 * create by lwj on 2020/3/1
 */
public class ChatServer {
    public Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int PORT = 6667;

    public ChatServer() {
        try {
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            listenChannel.socket().bind(new InetSocketAddress(PORT));
            listenChannel.configureBlocking(false);
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            System.out.println("server start fail");
        }
    }

    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.listen();
    }

    private void listen() {
        System.out.println("server start ok");
        try {
            while (true) {
                int select = selector.select();
                if (select > 0) {
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        //监听到accept
                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = listenChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector, SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress() + " on line");
                        }
                        if (key.isReadable()) {
                            readData(key);
                        }
                        //当前的key 删除，防止重复处理
                        iterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                System.out.println("server close");
                listenChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void readData(SelectionKey key) {
        SocketChannel socketChannel = null;
        ByteBuffer byteBuffer;
        try {
            socketChannel = (SocketChannel) key.channel();
            byteBuffer = ByteBuffer.allocate(1024);
            int readSize = socketChannel.read(byteBuffer);
            if (readSize > 0) {
                String msg = new String(byteBuffer.array());
                System.out.println("from client " + socketChannel.getRemoteAddress() + " :" + msg);
                //消息转发给其他客户端
                sendMsgToOtherClient(msg, key);
            }
        } catch (IOException e) {
            try {
                System.out.println(socketChannel.getRemoteAddress() + " off line");
                key.cancel();
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void sendMsgToOtherClient(String msg, SelectionKey srcKey) throws IOException {
        Channel channel;
        SocketChannel targetChannel;
        for (SelectionKey key : selector.keys()) {
            //这里既要排除自己，还要排除SeverSocketChannel
            if (key != srcKey && (channel = key.channel()) instanceof SocketChannel) {
                targetChannel = (SocketChannel) channel;
                targetChannel.write(ByteBuffer.wrap(msg.getBytes()));
            }

        }
    }
}

```



客户端代码：

```java
/**
 * create by lwj on 2020/3/1
 */
public class ChatClient {
    //定义相关的属性
    private final String HOST = "127.0.0.1"; // 服务器的ip
    private final int PORT = 6667; //服务器端口
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    public ChatClient() throws IOException {
        selector = Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        username = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username + " is ok...");
    }

    public static void main(String[] args) throws IOException {
        ChatClient chatClient = new ChatClient();
        new Thread(() -> {
            while (true) {
                chatClient.readInfo();
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String str = scanner.nextLine();
            chatClient.sendInfo(str);
        }

    }

    private void readInfo() {
        try {
            int select = selector.select();
            if (select > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        socketChannel.read(byteBuffer);
                        System.out.println(new String(byteBuffer.array()));
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            System.out.println("server is close");
        }
    }

    private void sendInfo(String msg) throws IOException {
        msg = username + " 说：" + msg;
        socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
    }


}

```



### 3.4  NIO与零拷贝

参考链接： https://juejin.im/post/5c1c532551882579520b1f47

- 零拷贝是网络编程的关键，很多性能优化都离不开，

- Java程序中，常用的零拷贝有mmap（内存映射）和sendFile。

- 零拷贝从操作系统角度来看，是没有CPU拷贝。

  

mmap和sendFile的区别：

1. mmap适合小数据量读写；sendFile适合大文件传输
2. mmap需要4次上下文切换，3次数据拷贝；sendFile 需要 3 次上下文切换，最少 2 次数据拷贝。
3. 1)sendFile 可以利用 DMA 方式，减少 CPU 拷贝，mmap 则不能（必须从内核拷贝到 Socket 缓冲区）。

NIO的零拷贝由`transferTo()`方法实现。transferTo()方法将数据从**FileChannel**对象传送到可写的字节通道（如Socket Channel等）。在内部实现中，由native方法`transferTo0()`来实现，它依赖底层操作系统的支持。在UNIX和Linux系统中，调用这个方法将会引起sendfile()系统调用。在linux下一个transferTo 方法就可以完成传输，在windows 下 一次调用 transferTo 只能发送8m ，就需要分段传输文件。



使用场景一般是：

- 文件较大，读写较慢，追求速度

- JVM内存不足，不能加载太大数据

- 内存带宽不够，即存在其他程序或线程存在大量的IO操作，导致带宽本来就小

  

**NIO的直接内存**

首先，它的作用位置处于传统IO（BIO）与零拷贝之间，为何这么说？

- 传统IO，可以把磁盘的文件经过内核空间，读到JVM空间，然后进行**各种操作**，最后再写到磁盘或是发送到网络，效率较慢但支持数据文件操作。
- 零拷贝则是直接在内核空间完成文件读取并转到磁盘（或发送到网络）。由于它没有读取文件数据到JVM这一环，因此程序无法操作该文件数据，尽管效率很高！

而直接内存则介于两者之间，效率一般且**可操作文件数据**。直接内存（mmap技术）将文件直接映射到内核空间的内存，返回一个**操作地址**（address），它解决了文件数据需要拷贝到JVM才能进行操作的窘境。而是直接在内核空间直接进行操作，**省去了内核空间拷贝到用户空间**这一步操作。

NIO的直接内存是由`MappedByteBuffer`实现的。核心即是`map()`方法，该方法把文件映射到内存中，获得内存地址addr，然后通过这个addr构造MappedByteBuffer类，以暴露各种文件操作API。

由于MappedByteBuffer申请的是堆外内存，因此**不受Minor GC控制**，只能在发生Full GC时才能被回收。而`DirectByteBuffer`改善了这一情况，它是MappedByteBuffer类的子类，同时它实现了DirectBuffer接口，维护一个Cleaner对象来完成内存回收。因此它既可以通过**Full GC**来回收内存，也可以调用`clean()`方法来进行回收。

另外，**直接内存**的大小可通过jvm参数来设置：`-XX:MaxDirectMemorySize`。

NIO的MappedByteBuffer还有一个兄弟叫做`HeapByteBuffer`。顾名思义，它用来在堆中申请内存，本质是一个数组。由于它位于堆中，因此可受GC管控，易于回收。



