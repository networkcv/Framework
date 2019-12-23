---
layout: post
title: "计算机网络——应用层"
subtitle: ''
author: "Networkcv"
header-style: text
tags:
  - Network
---

## -[计算机网络](https://networkcv.github.io/2019/08/29/master/)
- #### [物理层](https://networkcv.github.io/2019/08/29/network-1-PhysicalLayer/)
- #### [数据链路层](https://networkcv.github.io/2019/08/29/network-2-DataLinkLayer/)
- #### [网络层](https://networkcv.github.io/2019/08/29/network-3-NetworkLayer/)
- #### [传输层](https://networkcv.github.io/2019/08/29/network-4-TransportLayer/)
- #### [应用层](https://networkcv.github.io/2019/08/29/network-5-ApplicationLayer/)

# 应用层
#### DNS
**DNS服务器作用**   

负责解析域名，将域名解析为IP  
>nslookup 查看当前DNS域名解析服务器  

222.222.222.222     电信DNS  
8.8.8.8     谷歌DNS   

**域名**  
- 顶级域名 com(企业) edu(教育) cn(中国) org(组织) gov(政府)  
- 二级域名 tonghuan.com   
- 三级域名 www.tognhuan.com mail.toghuan.com 

**DNS服务器应用**
- 1.解析内网自己未在网上注册的域名
- 2.降低到Internet的域名解析流量
- 3.域环境

#### DHCP 动态主机配置
静态IP地址  
动态IP地址

#### FTP 文件传输协议     
- 使用21端口建立TCP控制连接，用于发送FTP命令信息  
- 先用21端口连接控制协议，然后协调端口号建立数据连接  
  TCP数据连接 netstat -n 查看端口  默认使用被动模式
- 主动模式：FTP客户端告诉FTP服务器使用什么端口（一般为20端口）侦听  
    　　　　　FTP服务器和FTP客户端的这个端口建立连接   
　　　　　防火墙只需要打开20、21端口即可                 
- 被动模式：FTP服务端在指定范围内（1040左右）的某一个端口被动等待客户端发起连接  
    每传一个文件都会建立一个会话  
防火墙只打开20、21，则无法下载，因为指定范围内端口可能没有开放  
**FTP传输模式**  
文本模式：ASCII模式，以文本序列传输数据  
二进制模式：Binary模式，以二进制序列传输数据

#### TELNET 远程终端协议 
可以通过telnet客户端，远程连接服务端，需要服务端的账号和密码，远程操控服务端  
telnet 192.168.10.1 8080 也可以用来做端口扫描工具，测试目标主机的端口是否开放使用23端口   
net user han a1! /add  添加用户  
net localgroup administrators han /add  添加到管理员组  
win7后windows默认不安装该服务，防止黑客入侵  

#### RDP 远程桌面协议 Remote Desktop Protocol

#### HTTP 超文本传输协议  
**http web** 
- 使用web代理服务器访问网站  
通过代理服务器的缓存机制来节省内网带宽
- 使用web代理绕过防火墙    
翻墙软件实现原理  
- 使用代理防止ip追踪
