### 

## vim

- **Ctrl + A**：将光标移到命令行或文本行的开头（A 表示 "Beginning"，即移动到行首）。
- **Ctrl + E**：将光标移到命令行或文本行的末尾（E 表示 "End"，即移动到行尾）。
- **Ctrl + F**：向前移动一个字符（F 表示 "Forward"，即向前移动）。
- **Ctrl + B**：向后移动一个字符（B 表示 "Backward"，即向后移动）。
- **Ctrl + L**：清屏并将当前行移到屏幕顶部（L 表示 "Clear"，即清除屏幕）。
- **Ctrl + U**：删除从光标位置到行首的所有字符（U 表示 "Delete"，即删除）。
- **Ctrl + K**：删除从光标位置到行尾的所有字符（K 表示 "Kill"，即清除）。
- **Ctrl + Y**：粘贴删除的文本到光标位置（Y 表示 "Yank"，即粘贴）。
- **Ctrl + D**：在空白行上按下表示退出终端（D 表示 "Delete"，即删除）；在非空行上按下表示删除光标后的字符。
- **Ctrl + C**：终止当前正在运行的命令（C 表示 "Cancel"，即取消）。
- **Ctrl + Z**：将当前进程挂起并放入后台（Z 表示 "Suspend"，即挂起）。
- **Tab**：自动补全命令或文件名。
- **Ctrl + R**：反向搜索命令历史记录，按下后可以开始输入关键字来查找之前执行过的命令。
- **Ctrl + H**：删除光标前的字符（H 表示 "Backspace"）。
- **Ctrl + W**：删除光标前的单词（W 表示 "Word"）。
- **Ctrl + N**：在命令历史记录中向后滚动（N 表示 "Next"，即下一个）。
- **Ctrl + P**：在命令历史记录中向前滚动（P 表示 "Previous"，即上一个）。
- **Ctrl + S**：暂停屏幕输出（S 表示 "Stop"），按下一次可恢复屏幕输出。
- **Ctrl +Q**：恢复被 Ctrl + S 暂停的屏幕输出（Q 表示 "Continue"）。
- **Ctrl +T**：交换光标前两个字符的位置（T 表示 "Transpose"，即交换）。
- **Ctrl +X, Ctrl +E**：打开当前命令行在编辑器中进行编辑，编辑完成后会执行编辑后的命令。



## man

`man` 命令是一个非常有用的命令，当你不会使用某个`Linux` 命令时，可以使用`man` 来查看其帮助文档。`man` 命令查到的手册来源于目录`/usr/share/man/`，可以看到该目录下有9 个这样的目录：

```shell
ls /usr/share/man/

man1
man2
man3
man4
man5
man6
man7
man8
man9
```

`man` 手册分9 种，用数字`[1-9]` 来区分，分别代表的含义如下（可以用`man man` 命令来查看）：

<img src="img/Linux指南/image-20230404152845055.png" alt="image-20230404152845055" style="zoom:50%;" />

可以通过设置不同的数字来查看不同的手册，如下：

```text
man 1 系统命令名
man 2 系统函数名
man 3 库函数名
```

如果没有带这个数字，则会从`1` 查到`9`，直到查到第一个为止。

**.man**文件是一个Unix风格（只有LF换行符）的纯文本文件，包含一个命令或其他项目的描述。它的格式是使用传统的 "troff "文本格式化工具。在GNU/Linux机器上，全系统的manpages通常位于"/usr/share/man "目录下，按语言分类并打包成gzip（.gz）档案。**.man**后缀本身通常只用于第三方的manpage文件。任何**.man**文件都可以用文本编辑器轻松打开。

## groff

groff（GUN roff） 是 roff 排版系统现在最常用的实现，其它实现有troff、nroff、ditroff等等。尽管历史悠久，roff当前还在广泛使用中。UNIX系统的man手册页、很多软件书籍和标准是用roff来写的。

[Groff概览](https://www.chungkwong.cc/groff.html)

[使用 groff 编写 man 手册页](https://linux.cn/article-9122-1.html)

[Unix, Linux 和MacOS](https://juejin.cn/post/6844903841901576199)



## cat

显示最后一屏内容	

cat 文件名	 cat 是由第一行到最后一行连续显示在屏幕上，而 tac 则是由最后一行到第一行反向在屏幕上显示出来



## tail

[tail命令详解](https://www.myfreax.com/linux-tail-command/)

查看文件的后几行	

tail -10 文件名	

持续输出某个文件的后续变化

tail -f  文件名



## more

`more` 命令用于查看Linux 文件的内容，用`more filename` 打开一个文件后，可以控制`向上`或`向下`翻滚页面。

回车 往下滚一行

空格 往下滚一页

Ctrl  + B 往上滚一页



## mkdir 

mkdir -p /a/b 可以创建多级文件夹



## telnet

在linux和centos下，我们就直接可以用telnet命令来测试端口是否畅通。具体用法：telnet 指定的IP或者 域名 端口号.

```bash
比如：telnet 192.168.1.102 3306

内网 telnet 127.0.0.1 8090
```

在Mac os 下

```bash
nc -vz -w 2 192.168.1.104 3306
```



