---
title: "Git使用指南"
date: 2020-7-30
category:
  - DevTools
tags:
  - Git
  - DevTools
---

# Git基础配置

## 查看Git配置

> git config \-\-list	\-\-[ local |  global | system ]	
>

loccal global system 对应git中从小到大3个域，分别对应 本地 | 全局 | 全系统

**信息保存位置：**

- 本地信息：.git/config
- 全局信息：~/.gitconfig

**优先级：**

- 就近原则：本地信息级别优先于全局信息，二者都有时采用项目级别的签名
- 如果只有全局信息，就以全局信息为准
- 二者都没有，不允许

## 配置Git基本信息

> git config \-\-global user.name 'networkcavalry ' 
>
> git config -\-global user.email 'networkcavalry@gmail.com'  

## Git工作原理

- 工作区：.git隐藏目录加所在的目录即为工作区，一般为项目目录
- 暂存区：用于临时存储的区域（git add提交的区域）
- 版本库：用于保存和管理项目版本的区域

**注意：.git隐藏目录是git的本地库，git就是依赖这个目录来完成对项目的管理，切记千万不要删除.git目录中的任何东西。git可以管理.git所在目录的所有文件以及嵌套的子文件等。无法管理.git所在目录之外的目录**

# Git本地常用操作

## 创建Git本地仓库

**1.对已有项目代码加入Git管理**

cd 项目代码所在文件夹  

> git init

**2.新建项目直接使用Git**

> git init <your_project>

创建后需要使用 cd 命令进入该项目的文件夹中

**3.从已有的代码仓库中克隆到本地**

- SSH的方式（推荐使用）

>  git clone git@github.com:networkcv/gittest.git

- Http的方式

>  git clone https://github.com/networkcv/gittest.git

**4.克隆裸仓到本地**

裸仓是指不克隆工作区，只克隆 **.git**文件，通常裸仓的命名都是以.git 结尾的，Github中给我们提供的也都是裸仓。

裸仓只能进行 push 和 pull，不能执行其他的git指令。

从本地另外一个地方克隆裸仓

> git clone -\-bare file:///c/Users/Administrator/Desktop/gittest/gittest/.git

从远端仓库克隆裸仓

>git clone -\-bare git@github.com:networkcv/gittest.git

## 提交到Git本地仓库

**1.将变更文件加入暂存区**

- 未加入Git版本追踪的情况

> git status

我们可以使用这个命令来查看当前仓库所有文件的变更状态

```
$ git status

No commits yet

Untracked files:
  (use "git add <file>..." to include in what will be committed)
        a.txt

nothing added to commit but untracked files present (use "git add" to track)
```

- 将单个文件加入git版本追踪

> git add a.txt

- 将所有文件加入git版本追踪

> git add -A	

- 将已被git追踪的变更文件加入暂存区

> git add -u

- 加入版本追踪后

```
$ git status

No commits yet

Changes to be committed:
  (use "git rm --cached <file>..." to unstage)
        new file:   a.txt
```

上例我们使用 `git add` 命令将文件加入git的版本控制当中，这只是它功能的一部分，该命令实际是将 **工作区** 已跟踪和未跟踪文件的更改添加到 **暂存区**中。

我们平时对文件的修改会自动保存到工作区中，然后我们再将工作区的内容保存到暂存区中，也就是我们刚才的操作，接下来需要将暂存区中的更改提交到本地仓库。

**2.加入暂存区后就可以提交到本地仓库了**

- 输入提交消息，描述此次提交是做了什么

>  git commit -m 'git add  test' 

- 每次都要先保存到暂存区再提交太麻烦了，`commit` 命令后加上 `-a` 参数可以帮我们省略这一步

> git commit -a

```
$ git commit -am 'git add test'
[master (root-commit) 3eee705] git add test
 1 file changed, 1 insertion(+)
 create mode 100644 a.txt
```

-  查看提交的记录  

>  git log

``` git
$ git log
commit 3eee70588edfa4b90a0edd2e25321c195917c854 (HEAD -> master)
Author: networkcavalry <networkcavalry@gmail.com>
Date:   Fri Jul 31 22:37:51 2020 +0800

    git add test
```

## 修改删除文件

- 删除文件

> git rm a.txt

- 修改文件名

> mv a.txt b.txt

> git add b.txt

> git rm a.txt

上面这种方式需要写三条git命令，比较繁琐。git为我们提供了更便捷的命令

> git mv a.txt b.txt

## 查看历史版本

-    查看历史版本(默认当前所在分支)  

> git log

- 查看历史版本的简要信息

>git log -\-oneline    

```git
$ git log --oneline
4813a70 (HEAD -> master) git rename test
c7f3937 commit test
3eee705 git add test
```

- 查看最新的两条简要信息 

> git log -n2 -\-oneline 

```git
$ git log -n2 --oneline
4813a70 (HEAD -> master) git rename test
c7f3937 commit test
```

- 查看指定分支的历史版本

> git log <branch_name>

- 查看所有分支的信息

> git log -\-all    

- 图形化查看分支继承 信息  

> git log -\-all -\-graph 

- 通过GUI查看分支信息

> gitk	-\-all



## 修改commit的message

该操作建议在未push到远端的本地commit中使用，以下两种方式本质上是对commit进行变基。变基对本地的commit没有影响，而对远端的commit进行变基时，其他人可能已经拉取了旧的commit，这样等其他人再拉取变基后的commit时，则对方会在本地进行merge，如果其想保持本地单分支的话可以在merge后进行rebase，将分支合并到主干上。

由于这套组合操作比较常用，IDEA的git图形界面则在拉取时就提供了rebase的选型，这样就拉倒本地merge后就会自动合并了到主干了。

-  修改最近一次commit的message  

>  git commit --amend 

- 修改之前的commit的message  

```
$ git log -n3 --pretty=oneline
496ecae9e9213380d0fbb0abcd6ff77e714ea2de (HEAD -> master, origin/master) git add 'e'
d07b3597d1b0cce082d1af4b80f07c18e7f1189b git add 'd'
dc6dff7515c28219711d7c4e0e1c837892664421 git add 'c'
```

比如我们要修改中间那次commit的message，由于rebase是变基，所以要选择它的下边的”地基“，也就是它的前一次commit，我们使用 `git rebase -i/--interactive（交互） 版本号` 这个命令。

> $ git rebase -i dc6dff7515c28219711d7c4e0e1c837892664421

执行命令后，会进入交互式的操作中，会打开你本地的文本编辑器来进行rebase。

```
// pick d07b359 git add 'd'  这个commit就是我们此次要修改的目标，这行实际是要删掉的
// 修改为下面的样子后，r 表示 reword 具体的命令解释可以参考下面的注视，修改后保存，关闭
r	d07b359 git add 'd'
pick 496ecae git add 'e'

# Rebase dc6dff7..496ecae onto dc6dff7 (2 commands)
#
# Commands:
# p, pick <commit> = use commit
// 保留该commit文件，只修改提交信息
# r, reword <commit> = use commit, but edit the commit message
# e, edit <commit> = use commit, but stop for amending
# s, squash <commit> = use commit, but meld into previous commit
# f, fixup <commit> = like "squash", but discard this commit's log message
# x, exec <command> = run command (the rest of the line) using shell
# b, break = stop here (continue rebase later with 'git rebase --continue')
# d, drop <commit> = remove commit
# l, label <label> = label current HEAD with a name
# t, reset <label> = reset HEAD to a label
# m, merge [-C <commit> | -c <commit>] <label> [# <oneline>]
# .       create a merge commit using the original merge commit's
# .       message (or the oneline, if no original merge commit was
# .       specified). Use -c <commit> to reword the commit message.
#
# These lines can be re-ordered; they are executed from top to bottom.
#
# If you remove a line here THAT COMMIT WILL BE LOST.
#
# However, if you remove everything, the rebase will be aborted.
#
# Note that empty commits are commented out
```

关闭上面的文件后，会重新弹出一个文本编辑界面，这里我们可以写修改后的message，修改完保存关闭就ok了。

查看修改后的结果。 

```git
$ git log -n3 --pretty=oneline
3c92d6cf53a40fee11040e8e7cf2c062958a0d26 (HEAD -> master) git add 'e'
55da8e298eb3fdec229141afe49e669dbc2fa9b1 git add 'd' and edit commit message
dc6dff7515c28219711d7c4e0e1c837892664421 git add 'c'
```

## 合并多个commit

这个操作也是通过 `rebase` 这个指令，同样对前一次的commit进行操作。

- 合并多个连续的commit成一个commit

>  git rebase -i b584c15c7b658fb6c4a3427eb567ab2fedf659d4

```
// 将三次commit合并到最后一次
pick dc6dff7 git add 'c'
s 55da8e2 git add 'd' and edit commit message
s 3c92d6c git add 'e'

# Rebase b584c15..3c92d6c onto b584c15 (3 commands)
#
# Commands:
# p, pick <commit> = use commit
# r, reword <commit> = use commit, but edit the commit message
# e, edit <commit> = use commit, but stop for amending
//保留该commit，但将其合并到前一个commit  
# s, squash <commit> = use commit, but meld into previous commit
...
```

- 合并多个不连续的commit成一个commit

比如将 `496ecae9e9` 和 `dc6dff7515` 合并成一个commit

```
$ git log --pretty=oneline  -n3
496ecae9e9213380d0fbb0abcd6ff77e714ea2de (HEAD -> master, origin/master) git add 'e'
d07b3597d1b0cce082d1af4b80f07c18e7f1189b git add 'd'
dc6dff7515c28219711d7c4e0e1c837892664421 git add 'c'
```

> $ git rebase -i b584c15c7b658fb6c4a3427eb567ab2fedf659d4

```
pick dc6dff7 git add 'c'
// 因为 squash 只会把commit压入前一条commit中，所以需要调换一下顺序
s 496ecae git add 'e'
pick d07b359 git add 'd'

# Rebase b584c15..496ecae onto b584c15 (3 commands)
#
# Commands:
# p, pick <commit> = use commit
# r, reword <commit> = use commit, but edit the commit message
# e, edit <commit> = use commit, but stop for amending
# s, squash <commit> = use commit, but meld into previous commit
```

-  完成间隔commmit的合并

> git rebase -\-continue  

- 如果发生错误，使用该命令可以回到合并之前

> git rebase -\-abort

## 忽略已提交的文件

```
git rm --cached  <file path> 
```

## 查看内容变更

- 工作区比暂存区多出了哪些变化

>git diff 

- 只看该文件  ,注意`--`和文件名中间有空格

>  git diff -\- 文件名

- 比较暂存区和head之间的变化  

> git diff -\-cached    

- 比较两个版本的变化，新的放后边

> git diff 403f3fc02c6 d659e047fdfa  

- 只关注两个版本中某个文件的变化

> git diff  403f3fc02c6 d659e047fdfa   -\- a.txt

- 比较当前Head和Head的父亲的变化  

> git diff head head~1  

## 撤销相关操作 

reset 可以指定commit的版本号，指定reset后操作就包含了从该版本号之后的变更内容。

- 将暂存区的操作撤销回工作区  

> git reset 

- 撤销工作区和暂存区的所有操作  

> git reset -\-hard 

- 删除该版本号之后的所有commit   

> git reset -\-hard  版本号

- 将暂存区的操作撤销回工作区  ，并将暂存区恢复到head版本  

> git reset head

- 将暂存区某个文件的操作撤销回工作区  ，并将暂存区中的该文件恢复到head版本  

> git reset head -\- fileName

- 放弃工作区内的修改，恢复到暂存区的对应的文件

> git checkout -\- fileName 

**补充知识：**

![img](img/Git使用指南/webp)

Git中存在三个区域：

**Working Tree**：工作区，当前的工作区域，当被git管理的文件变更时，变更会先记录到工作区。

**Index/Stage** ：暂存区，和git stash命令暂存的地方不一样。使用git add xx，就可以将xx添加近Stage里面。

**Repository** ：本地仓库，对应图中的history区域，即使用git commit提交后的结果。

实际工作中，暂存区用到的还是比较少的，我们进行了变更后，直接从工作区提交到本地仓库中了，像内容比对这类操作都是针对commit来做的，弱化了暂存区的功能。在IDEA中可以设置 always add file，将所有对工作区的操作自动保存到暂存区中。



reset --hard：重置工作区，暂存区和本地仓库到指定位置的commit。

reset --soft：保留工作目录，并把重置 HEAD 所带来的新的差异放进暂存区

reset  --mixed：保留工作区，将暂存区的内容混合放到工作区，并清空暂存区。--mixed 是默认参数




## 暂存未提交的修改

把未提交的修改临时保存起来，同时将工作区和暂存区恢复清空。等到方便的时候，再将这些内容进行恢复，继续之前的工作。

- 将当前所做操作临时保存  

> git stash  

-   查看临时保存的集合  

> git stash list

- 只取出list中最后保存的，不删除，也可以指定某一个

> git stash apply 

-  取出并删除list中的最后保存的，也可以指定某一个

> git stash pop  

## 分支相关操作

- 查看分支

> git branch 

- 显示分支的详细信息

> git branch -v |-\-verbose 

- 列出远程跟踪和本地分支  

> git branch -a |-\-all  

- 切换分支  

> git checkout 分支名   

- 删除分支  

> git branch -D 分支名  

- 从指定版本或分支创建一个新分支  

> git checkout -b 新分支名 版本号|已有分支名     

```
$ git checkout -b temp master
Switched to a new branch 'temp'

$ git checkout -b temp2 434c99ba962
Switched to a new branch 'temp2'
```

- 将当前分支与该分支合并  

> git merge branchName 

- 将两个无关的分支进行合并

> git merge --allow-unrelated-histories  origin/master 

## 分离头指针开发

- detached head 分离头指针，相当于创建了一个临时分支来使用，切回主分支后需要手动保存，不然会被git清除，具体操作如下：

> git checkout 版本号

```
$ git checkout 4813a70f369340d6ac18bb719dd05c1575788fb
Note: switching to '4813a70f369340d6ac18bb719dd05c1575788fb'.

You are in 'detached HEAD' state. You can look around, make experimental
changes and commit them, and you can discard any commits you make in this
state without impacting any branches by switching back to a branch.

If you want to create a new branch to retain commits you create, you may
do so (now or later) by using -c with the switch command. Example:

  git switch -c <new-branch-name>

Or undo this operation with:

  git switch -

Turn off this advice by setting config variable advice.detachedHead to false

HEAD is now at 4813a70 git rename test

```

- 查看一下分离头指针后的分支状态

```
$ git branch
* (HEAD detached from 4813a70)
  master
  temp
```

```
$ git status
HEAD detached from 4813a70
Untracked files:
  (use "git add <file>..." to include in what will be committed)
        .idea/

nothing added to commit but untracked files present (use "git add" to track)
```

- 当我们需要切回master，git会提醒你临时的branch如果以后还想使用，则需要尽快对其建立新的分支

```git
$ git checkout  master
Warning: you are leaving 1 commit behind, not connected to
any of your branches:

  bc8be3e add c to b.txt

If you want to keep it by creating a new branch, this may be a good time
to do so with:

 git branch <new-branch-name> bc8be3e

Switched to branch 'master'
Your branch is based on 'origin/master', but the upstream is gone.
  (use "git branch --unset-upstream" to fixup)
```

- 对刚才的临时分支建立新的分支

> git branch <new-branch-name> bc8be3e

# Git远端常用操作

前面我们讲了git在本地的各种操作，单人开发中遇到的各个场景，而在实际工作中，一定是多人合作开发，这就涉及到对远端代码的推送和拉取操作了。

## 常见的传输协议

![mark](http://pic.networkcv.top//network/20200813/SGbAyVMJHqoq.png?imageslim)

直观区别：哑协议传输进度不可见，智能传输协议可见。

传输速度：智能协议比哑协议传输速度快。

<br>

如果我们在执行些复杂且有危险性指令的时候，我们可以先将其备份到远端仓库，这里的远端仓库既可以是服务器上的仓库，也可以是本地其他位置的仓库。

## 路由相关操作

- 查看路由信息

> git remote -v

- 添加路由信息

> git remote add local file://c/Users/Administrator/bk/gittest.git

> git remote add origin git@github.com:networkcv/gittest.git

## 拉取合并远端相关操作

- 只从远端拉取对应的分支，不进行其他操作

> git fetch origin master

- 将指定分支与当前分支进行合并

> git merge origin/master

- 如果远端的分支和本地的分支完全没有关联，是两个不相干的树，也可以进行合并

> git merge -\-allow-unrelated-histories orgin/master

合并完成后可能会出现下面这样的情况。

![mark](http://pic.networkcv.top//network/20200813/tW6XSolv7Blr.png?imageslim)

我们来详细分析一下，最初的commit的是 **git rebase all**,本地在此基础上增加了 **add f** 这个commit，而远端的 **Create c.txt** 可能是其他同事同样基于 **git rebase all** 这个commit进行的修改，并且推送到了远端的代码仓库，可以看到 remotes/origin/master 指向他新提交的commit。

在我们执行 fetch 操作后，只会将本地分支中远端master的指向ref同步，并不会将该ref指向的对象同步到本地，只有执行merge时，才会将ref指向的对象同步到本地的 .git 文件中。

在发生merge后，会生成一个新的commit，这个commit中会有两个 parent，分别是 **Create c.txt** 和 **add f** 对应的版本号，这是因为**Create c.txt** 中的tree有指向 c.txt的这个blog，而**add f** 完全没有和 c.txt相关的信息，它里边只有关于 add f 的修改。因此只有将两份commit拼凑在一起才是完整的仓库状态，但也因此会出现分叉。

如果我们想消除这种分叉，使版本以一条直线的方式来进行变更，这样可以更好的去观察版本之间的变化。我们可以使用之前有提到的 **git reabse -i** 命令来进行变基。在变基中，其实就是选取了两个commit中的一个来做外另外一个的parent，被选为parent的commit不用做什么变更，假设 **add f** 这个commit作为parent，**Create c.txt** 作为children，而作为Children的commit需要做一些变动。

1. 将其parent的指向从原来的**git rebase all** 修改为 **add f**
2. 将parent的tree中的内容复制到children的tree中，如果遇到了共同修改的文件，则会发生冲突，需要我们手动的去解决，将两边的变更点进行整合和取舍然后保存到children的tree中

这样就完成了变基。

- 直接使用 pull

  pull命令其实就是先fetch然后再merge。merge之后我们也可以进行rebase使其版本变化显示为一条直线。

# 其他

## 探秘.Git目录

HEAD 中的内容是指向当前工作分支的头部

config 存放的是 用户 local 的配置

refs 中存放的是分支（heads）和标签（tags）的引用

objects 中存放的是上边那些引用真正指向的位置，文件的每次变更都会存在这里。

objects 中的文件如下：

```
$ ll
total 0
drwxr-xr-x 1 Administrator 197121 0  7月 31 22:37 08/
drwxr-xr-x 1 Administrator 197121 0  8月  1 00:24 18/
drwxr-xr-x 1 Administrator 197121 0  8月  1 00:45 3b/
drwxr-xr-x 1 Administrator 197121 0  7月 31 22:37 3e/
drwxr-xr-x 1 Administrator 197121 0  8月  1 00:12 42/
drwxr-xr-x 1 Administrator 197121 0  8月  1 00:50 43/
drwxr-xr-x 1 Administrator 197121 0  8月  1 00:33 48/
drwxr-xr-x 1 Administrator 197121 0  8月  1 00:33 4e/
drwxr-xr-x 1 Administrator 197121 0  7月 31 21:21 78/
drwxr-xr-x 1 Administrator 197121 0  8月  1 00:50 83/
drwxr-xr-x 1 Administrator 197121 0  8月  1 00:48 bf/
drwxr-xr-x 1 Administrator 197121 0  8月  1 00:24 c7/
drwxr-xr-x 1 Administrator 197121 0  8月  1 00:50 da/
drwxr-xr-x 1 Administrator 197121 0  8月  1 00:45 ee/
drwxr-xr-x 1 Administrator 197121 0  7月 31 00:18 info/
drwxr-xr-x 1 Administrator 197121 0  7月 31 00:18 pack/
```

每个commit都会对应一个tree，在tree中记录了这次提交时整个项目的快照，tree中可以包含blob也可以包含其他的tree

而 objects 中存放的就是这些tree，为了方便查找，git 将按照tree名称的前两个字母进行分类，如上图所示，进入除了 info/ 和 pack/ 之外的文件夹中，看到的就是去掉前两个字母的tree名称，下例中该tree的完整名称是  `dab7902aa19375da590c0349bbc0a126e240f4d7`，一定要注意加上文件夹名称才是完整的tree名

```
Administrator@NetworkCavalry MINGW64 ~/Desktop/gittest/gittest/.git/objects/da (GIT_DIR!)
$ ll
total 1
-r--r--r-- 1 Administrator 197121 84  8月  1 00:50 b7902aa19375da590c0349bbc0a126e240f4d7
```

可以通过下面的命令来查看objects文件夹中对象	的内容或类型

- 查看类型

> git cat-file -t tree_name

```
$ git cat-file -t dab7902aa19375da590c0349bbc0a126e240f4d7
tree
```

- 查看内容

> git cat-file -p tree_name

```
$ git cat-file -p dab7902aa1
100644 blob 83dc1bd167cb7668a8f7c29697460eca5b606c6d    .gitignore
100644 blob 422c2b7ab3b3c668038da977e4e93a5fc623169c    b.txt
```



## [Git的commit、tree、blob对象](./git对象.jpg)

- 每次提交都会创建一个commit对象，包含tree、parent、author、committer对象信息  
- tree对象为可以理解为文件夹，文件夹中包含文件(blob)或文件夹(tree)  
- blob对象为文件对象，git中将相同内容的文件看作同一个blob对象

![mark](http://pic.networkcv.top//network/20200813/Aig7asliLcLz.png?imageslim)

![mark](http://pic.networkcv.top//network/20200813/5tU6wOdieTdY.png?imageslim)
