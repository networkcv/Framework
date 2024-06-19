import os
import socket
import threading
from time import sleep


def dispos_client(client_socket: socket, ip_port):
    print(client_socket)
    print(ip_port)
    while True:
        sleep(1)
        # 接收客户端发送的数据, 这次接收数据的最大字节数是1024
        recv_data = service_client_socket.recv(4096)
        print(recv_data)
        # 获取数据的长度
        if len(recv_data) == 0:
            service_client_socket.close()
            return
        request_list = recv_data.decode().split(" ", maxsplit=2)
        request_path = request_list[1]
        # 对二进制数据进行解码
        recv_content = recv_data.decode()
        print("接收客户端的数据为:", recv_content)
        # 准备发送的数据
        # send_data = f"ok, 收到 {recv_content}\r\n"

        if request_path == "/":
            path = "index1.html"
        else:
            path = "index2.html"
        # 发送网页
        with open(path, 'rb') as file:
            # 发送数据给客户端
            file_data = file.read()

        # 响应行
        response_line = "HTTP/1.1 200 OK\r\n"
        # 响应头
        response_header = "Server: PWS1.0\r\n"
        # 响应体
        response_body = file_data

        # 拼接响应报文
        response_data = (response_line + response_header + "\r\n").encode("utf-8") + response_body
        # 发送数据
        client_socket.send(response_data)


if __name__ == '__main__':
    # 创建tcp服务端套接字
    tcp_server_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # 设置端口号复用，让程序退出端口号立即释放
    tcp_server_socket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, True)
    # 给程序绑定端口号
    tcp_server_socket.bind(("", 9000))
    # 设置监听
    # 128:最大等待建立连接的个数， 提示： 目前是单任务的服务端，同一时刻只能服务与一个客户端，后续使用多任务能够让服务端同时服务与多个客户端，
    # 不需要让客户端进行等待建立连接
    # listen后的这个套接字只负责接收客户端连接请求，不能收发消息，收发消息使用返回的这个新套接字来完成
    tcp_server_socket.listen(128)
    # 等待客户端建立连接的请求, 只有客户端和服务端建立连接成功代码才会解阻塞，代码才能继续往下执行
    # 1. 专门和客户端通信的套接字： service_client_socket
    # 2. 客户端的ip地址和端口号： ip_port
    while True:
        service_client_socket, ip_port = tcp_server_socket.accept()
        # 代码执行到此说明连接建立成功
        print("客户端的ip地址和端口号:", ip_port)
        threading.Thread(target=dispos_client, args=(service_client_socket, ip_port)).start()
    service_client_socket.close()
    # 关闭服务端的套接字, 终止和客户端提供建立连接请求的服务
    tcp_server_socket.close()
