### brew介绍

brew是一个软件包管理工具,类似于centos下的yum或者ubuntu下的apt-get,非常方便,免去了自己手动编译安装的不便
 　brew 安装目录 /usr/local/Cellar
 　brew 配置目录 /usr/local/etc
 　brew 命令目录 /usr/local/bin   注:homebrew在安装完成后自动在/usr/local/bin加个软连接，所以平常都是用这个路径

brew search 软件名，如brew search wget //搜索软件
 brew install 软件名，如brew install wget//安装软件
 brew remove 软件名，如brew remove wget//卸载软件



### brew换源

[为brew/git/pip设置代理&为brew正确换源终极版](https://segmentfault.com/a/1190000019758638)



### bash_profile 和 zshrc 文件的区别？

.bash_profile 中修改环境变量只对当前窗口有效，而且需要 source ~/.bash_profile才能使用
.zshrc 则相当于 windows 的开机启动的环境变量
你也可以在 .zshrc 文件中加一行 source .bash_profile 解决需要 source 才能使用的问题





