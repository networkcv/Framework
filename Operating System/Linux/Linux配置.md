# brew介绍

brew是一个软件包管理工具,类似于centos下的yum或者ubuntu下的apt-get,非常方便,免去了自己手动编译安装的不便
 　brew 安装目录 /usr/local/Cellar
 　brew 配置目录 /usr/local/etc
 　brew 命令目录 /usr/local/bin   注:homebrew在安装完成后自动在/usr/local/bin加个软连接，所以平常都是用这个路径

brew search 软件名，如brew search wget //搜索软件
 brew install 软件名，如brew install wget//安装软件
 brew remove 软件名，如brew remove wget//卸载软件



# brew换源

[为brew/git/pip设置代理&为brew正确换源终极版](https://segmentfault.com/a/1190000019758638)



# bash_profile 和 zshrc 文件的区别？

.bash_profile 中修改环境变量只对当前窗口有效，而且需要 source ~/.bash_profile才能使用
.zshrc 则相当于 windows 的开机启动的环境变量
你也可以在 .zshrc 文件中加一行 source .bash_profile 解决需要 source 才能使用的问题





## 解释与编译

编程语言没有编译型和解释型的区别，只能说某个语言常见的执行方式为*编译成新代码执行*或*解释器解释执行*
编译器的输入是A语言的源代码，而输出是B语言；比如C++，被编译成汇编语言；
解释器的输入是A语言的源代码，它直接执行A语言；一般解释器的内部实现是一个编译器加一个虚拟机，编译器把输入语言编译成中间语言，虚拟机直接执行中间语言。

## terminal

一个程序，是界面上打开的黑框框本身，比如 xterm、kvt 等。shell 运行于其中。

## shell 概念

shell 是一个命令行解释器，顾名思义就是机器外面的一层壳，用于人机交互，只要是人与电脑之间交互的接口，就可以称为 shell。表现为其作用是用户输入一条命令，shell 就立即解释执行一条。不局限于系统、语言等概念、操作方式和表现方式等。 比如我们平时在黑框框里输入命令，叫 command-line interface (CLI)；在屏幕上点点点，叫graphical user interface (GUI)

## Interactive 和 Non-interactive

Interactive，如果你打开 terminal，在里面输入 bash 代码，回车得到输出，你就是在运行一个 Interactive shell，它的特征是可以让用户输入，然后直接把输出打到界面上；如果你运行一个包含了若干行的 shell 脚本，这些 shell 代码就运行在Non-interactive shell 中。

## Login 和 Non-login

login shell 是指登录系统后所获得的顶层 shell，比如最常用的 ssh 登录，登录完后得到一个 login shell
如果已经登录了桌面电脑，打开 terminal 进入的 shell 就是 Non-login shell。

## 类型

常见的 shell 解释器有 sh、bash这两种，其他的 ksh、csh 和 zsh 等是不常见的。Mac OS 中默认安装了以上所有类型，Windows 需要自行安装，Linux 更不用说了。就像上面说的，只要一门语言有解释器，就可以作为 shell 使用。比如Java 有第三方解释器 Jshell，PHP有 PHP Shell。如果你用过 windows，那你对 cmd 这个词一定不陌生，它是 windows shell，官方名称叫做 command interpreter。

## bash

Bash 是最常见的 shell，Mac 中默认 shell 就是 bash。
[bash官网这篇文章]描述了唤起 bash shell 时加载的不同文件：login shell 加载 \~/.bash_profile ，而non-login shell 加载 \~/.bashrc 。

## zsh

很多人的 mac 中会使用 zsh 而不是 bash，一大半是因为 oh-my-zsh 这个配置集，它兼容 bash，还有自动补全等好用的功能。

zsh 的配置文件\~/.zshrc

Mac OS 中，终端默认使用的shell脚本是zsh，不是bash。会导致报错如下：

```
WARNING: this script is deprecated, please see git-completion.zsh
```

但是 git 并没有兼容zsh，所以想用git 补全 得用回 bash。

具体操作步骤  终端 --> 偏好设置 --> 通用 --> Shell的打开方式，选中【命令（完整的路径）】，设置为：/bin/bash。

## 配置 shell

如上所说，shell 在启动时都会去找配置文件，然后运行它。你安装的一些脚本，如果想让它能够全局运行，就需要在配置文件中设置路径。有过设置路径后还是不管用的经历吗？多半是因为把配置写在了错误的配置文件里。** 应该在配置shell（最常见的是配置默认命令）之前，使用 echo $SHELL，确认自己现在用的是什么shell后，再去编辑对应的配置文件 **



