

# Nginx、OpenResty、Kong 的概念和区别联系

Nginx、OpenRestry、Kong 这三个项目关系比较紧密：

- Nginx 是模块化设计的反向代理软件，C语言开发；
- OpenResty 是以 Nginx 为核心的 Web 开发平台，可以解析执行 Lua 脚本
- Kong 是 OpenResty 的一个应用，是一个 API 网关，具有API管理和请求代理的功能。



## Nginx

Nginx 是一个高性能的 HTTP 和反向代理 web 服务器，同时也提供了 IMAP/POP3/SMTP 服务。Nginx 是由伊戈尔·赛索耶夫为俄罗斯访问量第二的 Rambler.ru 站点（俄文：Рамблер）开发的，第一个公开版本0.1.0发布于2004年10月4日。

Nginx 是一个高性能的 Web 和反向代理服务器, 它具有有很多非常优越的特性:

作为 Web 服务器：相比 Apache，Nginx 使用更少的资源，支持更多的并发连接，体现更高的效率，这点使 Nginx 尤其受到虚拟主机提供商的欢迎。能够支持高达 50,000 个并发连接数的响应，感谢 Nginx 为我们选择了 epoll and kqueue 作为开发模型.

作为负载均衡服务器：Nginx 既可以在内部直接支持 Rails 和 PHP，也可以支持作为 HTTP代理服务器 对外进行服务。Nginx 用 C 编写, 不论是系统资源开销还是 CPU 使用效率都比 Perlbal 要好的多。

作为邮件代理服务器: Nginx 同时也是一个非常优秀的邮件代理服务器（最早开发这个产品的目的之一也是作为邮件代理服务器），Last.fm 描述了成功并且美妙的使用经验。

Nginx 安装非常的简单，配置文件 非常简洁（还能够支持perl语法），Bugs非常少的服务器: Nginx 启动特别容易，并且几乎可以做到7*24不间断运行，即使运行数个月也不需要重新启动。你还能够在 不间断服务的情况下进行软件版本的升级。

## OpenResty

OpenResty® 是一个基于 Nginx 与 Lua 的高性能 Web 平台，其内部集成了大量精良的 Lua 库、第三方模块以及大多数的依赖项。用于方便地搭建能够处理超高并发、扩展性极高的动态 Web 应用、Web 服务和动态网关。

OpenResty® 通过汇聚各种设计精良的 Nginx 模块（主要由 OpenResty 团队自主开发），从而将 Nginx 有效地变成一个强大的通用 Web 应用平台。这样，Web 开发人员和系统工程师可以使用 Lua 脚本语言调动 Nginx 支持的各种 C 以及 Lua 模块，快速构造出足以胜任 10K 乃至 1000K 以上单机并发连接的高性能 Web 应用系统。

OpenResty® 的目标是让你的Web服务直接跑在 Nginx 服务内部，充分利用 Nginx 的非阻塞 I/O 模型，不仅仅对 HTTP 客户端请求,甚至于对远程后端诸如 MySQL、PostgreSQL、Memcached 以及 Redis 等都进行一致的高性能响应。

## Kong

Kong 是 API 管理的强大效率工具。对需要从事 API 管理的广大开发员来说，它是最出色的工具之一。Kong 是开源工具，具有可扩展性和模块性，可以在任何一种基础设施上运行。多年来，Kong 一直在支持优秀的开发项目，比如 Mashape（世界上规模最大的API市场）。最棒的是，Kong得到了强大的 Nginx 的支持。

Kong的主要特性

- Kong 的扩展性：只要增添更多的服务器实例，它就能横向扩展，毫无问题，那样你可以支持更多流量，同时确保网络延迟很短。
- Kong 的灵活性：它可以部署在单个或多个数据中心环境的私有云或公有云上。它还支持大多数流行的操作系统，比如Linux、Mac和Windows。Kong包括许多实用技巧，以便针对大多数现代平台完成安装和配置工作。
- Kong 的模块性：它可以与新的插件协同运行，扩展基本功能。可将你的API与许多不同的插件整合起来，以增强安全、分析、验证、日志及/或监测机制。最好的例子之一就是Nginx Plus插件（https://getkong.org/plugins/nginx-plus-monitoring/），该插件提供了服务器实时监测机制，以获得关于负载和请求的度量指标和统计数字。
- Kong 的生态：虽然 Kong 是开源工具，可供每个人免费使用，但你也能获得企业版，企业版通过电子邮件、电话和聊天提供了快速支持，此外还提供初始安装、从第三方API管理工具来迁移、紧急补丁、热修复程序及更多特性。





# [API网关Kong学习笔记](https://www.lijiaocn.com/%E9%A1%B9%E7%9B%AE/2018/09/29/nginx-openresty-kong.html)

# [Openresty官网](https://openresty.org/cn/installation.html)

通过 Lua 扩展 NGINX 实现的可伸缩的 Web 平台

​	