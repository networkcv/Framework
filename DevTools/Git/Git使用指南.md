[TOC]

# GIT查询手册

### GIT工作原理

- 工作区：.git隐藏目录加所在的目录即为工作区，一般为项目目录
- 暂存区：用于临时存储的区域（git add提交的区域）
- 版本库：用于保存和管理项目版本的区域

**注意：.git隐藏目录是git的本地库，git就是依赖这个目录来完成对项目的管理，切记千万不要删除.git目录中的任何东西。git可以管理.git所在目录的所有文件以及嵌套的子文件等。无法管理.git所在目录之外的目录**

---

### GIT使用流程

> **本地仓库使用的==最基本==流程**

- 创建项目目录
- 使用git init初始化项目目录为git可以管理的项目
- 开始写代码等工作
- git add把当前工作区完成的工作内容添加到暂存区
- git commit把当前暂存区的内容提交到版本库中

> **远程仓库使用的==最基本==流程**

- 创建远程仓库（在github、码云等）
- 使用 git remote 命令把远程库和本地库关联起来
- 使用 git push 把本地库保存的内容提交到远程库

> **注意：以上流程只为梳理git基本命令的使用过程，不适用于正式项目的使用流程，正式项目应该使用更加完善和保险的流程。**

---

### GIT配置相关指令

> **第一次安装完成git后，我们在全局环境下配置基本信息：我是谁？**

#### git config: 查看、配置git信息

```javascript
/* 查看配置信息 */
git config -l

/* 查看全局配置信息( local | global | system 分别对应 本地 | 全局 | 全系统) */
git config --(global|local|system) --list

/* 配置本地信息(用户名和密码)：尽在当前本地范围内有效 */
git config user.name 'xxx'
git config user.email 'xxx@xx.xx'

/* 配置全局信息(用户名和密码)：在登录当前操作系统的用户范围内有效 */
git config --global user.name 'xxx'
git config --global user.name 'xxx@xx.xx'

/*
	优先级：
		1.就近原则：本地信息级别优先于全局信息，二者都有时采用项目级别的签名
		2.如果只有全局信息，就以全局信息为准
		3.二者都没有，不允许
		
	信息保存位置：
		本地信息：.git/config
		全局信息：~/.gitconfig
		
	注意：这里设置的用户名和邮箱与登录远程代码托管中心(github等)的账号、密码没有任何关系。
*/

```

### GIT本地常用命令汇总

#### git init: 初始化仓库

```javascript
//初始化git仓库
git init
```

#### git add：工作区内容添加到暂存区

````javascript
/* 把指定路径的目录或文件中的修改添加到暂存区，如果路径是目录，则把该目录下所有的文件包括子文件中的修改全部添加到暂存区 */
git add <路径>

/*无参数默认为将修改操作的文件和未跟踪新添加的文件添加到git系统的暂存区，不包括删除*/
git add .

/*-u == --update,表示将已跟踪文件中的修改和删除的文件添加到暂存区，不包括新增加的文件，注意这些被删除的文件被加入到暂存区再被提交并推送到服务器的版本库之后这个文件就会从git系统中消失*/
git add -u .

/*-A == --all,表示将所有的已跟踪的文件的修改与删除和新增的未跟踪的文件都添加到暂存区*/
git add -A .
````

#### git commit：暂存区内容提交到版本库

```javascript
/* 把暂存区的内容提交到版本库，-m表示需要添加注释，"xxx"表示注释的内容 */
git commit -m"xxx"

/* 修改最近一次commit的message */
git commit --amend
```

#### gitk: git提供的一个gui工具，可以很清晰的查看搜索提交历史及git相关操作。

#### git log：查看提交历史

```javascript
/*不带参数，默认输出详细的提交信息*/
git log

/* --all表示显示所有的branch，这里也可以选择，比如我指向显示分支ABC的关系，则将--all替换位branchA branchB branchC */
git log --all

/*--pretty=oneline参数表示简化提交信息为一行显示*/
git log --pretty=oneline

/*--stat参数表示在 git log 的基础上输出文件增删改的统计数据*/
git log --stat

/*-p参数表示控制输出每个commit具体修改的内容，输出的形式以diff的形式给出*/
git log -p

/* 仅显示commitID的前几个字符，而非全部字符 */
git log --abbrev-commit

/* 以相对于当前的时间展示提交历史 */
git log --relative-date

/* 展示提交历史前面加入简单的分支图，区分每次提交历史 */
git log --graph

/*这个命令用来输出汇总信息，以作者进行分类。*/
git shortlog
git shortlog -s  //可以用来统计每个作者的commit数量
git shortlog -n  //可以用来统计的量进行倒序排列

/*加--author用来过滤commit,限定输出给定的用户*/
git log --author

/*-n参数表示输出记录的数量，n表示具体输出的条数，例如：“git log -2”表示要输出两条*/
git log -n

/*限定指定日期范围的log*/
git log --after '日期'
git log --before '日期'

/*--decoreate参数用来控制log输出时，显示对应commit所属的branch和tag信息*/
git log --decoreate

/*查看提交历史以便确定要退回到哪个版本*/
git reflog
```

#### git reset：版本回退

> **注意：在git中，用HEAD表示当前版本，HEAD^表示上个版本，HEAD^^表示上上个版本，以此类推。当版本数过多时可以使用HEAD~n,n表示回退的版本数量。**

![alt gitReset图示](C:\Users\admin\Desktop\gitReset图示.png)

```javascript
/*
[HEAD~1|commitID]表示可以使用HEAD表示回退的版本，也可以使用commitID表示回退版本（commitID为版本号）:
	HEAD		表示当前版本，即最新提交
	HEAD^	 	表示上个版本
	HEAD^^		表示上上个版本
	......		以此类推
	HEAD~100 	表示往上100个版本
*/
/*回退一个版本，且会将暂存区的内容和本地已提交的内容全部恢复到未暂存的状态，不影响原来本地文件(未提交的也不受影响)。-mixed为默认参数，当没有参数的时候参数默认设为-mixed*/
git reset (-mixed) [HEAD~1|commitID]

/*回退一个版本，不清空暂存区，将已提交的内容恢复到暂存区，不影响原来本地的文件(未提交的也不受影响)*/
git reset --soft [HEAD~1|commitID]

/*回退一个版本，清空暂存区，将已提交的内容的版本恢复到本地，本地的文件也将被恢复的版本替换(慎用)*/
git reset --hard [HEAD~1|commitID]
```

#### git checkout: 放弃修改

```javascript
/*撤销工作区的修改，如果没有"--",就会变成切换分支的命令*/
git checkout -- <file>
/*
1.如果工作区的内容修改后还没添加到暂存区，此命令就是就是修改到和版本库一模一样的状态
2.如果工作区的内容修改后已经添加到暂存区，然后又做了修改，此时执行此命令就会回到添加到暂存区后的状态
原理：用版本库里的版本替换工作区的版本，无论工作区是修改还是删除，都可以“一键还原”。
*/
```

#### git rm：删除文件

```javascript
/* 删除一个文件，并会被记录下来，但是无法删除已经提交到版本库的文件 */
git rm

/* --cached参数表示只删除仓库中的文件，而保存本地文件 */
git rm -r --cached 文件1

/* 若想删除版本库中的文件，可按如下步骤： */
git rm 文件1
git commit -m"删除文件1"

/* 如果删错了，想恢复，可执行如下指令： */
git checkout -- 文件1
注意：该指令的作用是使用版本库中的内容替换工作区的内容，因此如果文件删错了，只会恢复到版本库中的内容。
```

#### git diff: 比较文件间的不同

```javascript
/* 当工作区有改动，暂存区为空，diff的对比是“工作区与最后一次commit提交的仓库的共同文件的对比”；当工作区有改动，暂存区不为空，diff对比的是“工作区与暂存区的共同文件” */
git diff

/* 显示暂存区(已经add但未commit文件)和最后一次commit(HEAD)之间的所有不相同文件的增删改 */
git diff --cached || git diff --staged

/* 显示工作区和暂存区与最后一次commit之间的所有不同文件的增删改 */
git diff HEAD

/* 可以查看最近一次提交的版本与往过去时间线前数X个的版本之间的所有工作区和暂存区与最后一次commit之间定义文件之间的增删改 */
git diff HEAD~X || git diff HEAD^^^

/* 比较两个分支上最后 commit 的内容的差别 */
git diff 分支1 分支2

/* 显示出所有有差异的文件(不详细,没有对比内容) */
git diff 分支1 分支2 --stat
```



---



### GIT远程库相关指令

#### git remote：查看、关联远程库等

> .git目录中的config文件中有远程仓库的关联配置

```javascript
/* 查看已关联的远程库的名称 */
git remote

/* 查看已关联的远程仓库的详细信息，信息内容包括可以抓取和推送的origin的地址，如果没有推送权限，就看不到push的地址 */
git remote -v

/* 添加新的远程仓库关联,origin为远程库的名字，默认是origin，可以自行修改。#git_url是远程库的url地址，可以采用http协议或ssh(git)协议（ssh具体看后面） */
git remote add origin #git_url

/* 删除远程库的关联 */
git remote remove origin

/* 修改远程库的关联：将关联的远程库使用的协议由http改为ssh */
git remote set-url origin #git_url
或者
git remote remove origin
git remote add origin #gir_url
```

#### git push：将本地版本库的分支推送到远程服务器上对应的分支

```javascript
/* 基本格式 */
git push <远程主机名> <本地分支名>:<远程分支名>

/* 将本地master分支推送到远程库origin的master分支，如果master不存在，则会被新建 */
git push origin master

/* 如果省略了本地分支名，表示删除指定的远程分支，等同于推送一个空的本地分支到远程分支 */
git push origin :master
等同于
git push origin --delete master

/* 删除远程库的master分支。如果当前分支与远程分支之间存在追踪关系，则本地分支和远程分支都可以省略。 */
git push origin --delete master

/* 将当前分支推送到origin远程库的对应分支上 */
git push origin

/* 如果当前分支与远程分支之间存在追踪关系，那么本地分支和远程分支都可以省略 */
git push

/*若当前分支与多个主机存在追踪关系，可以使用-u参数选定一个默认主机，这样后面就可以不加任何参数使用git push了*/
git push -u origin master

/* 不带任何参数的git push,默认只推送当前分支，这叫simple方式。此外，还有一种matching方式，会推送所有有对应的远程分支的本地分支。GIT 2.0版本之前，默认采用matching方法，现在改为默认采用simple方式。如果要修改这个配置，可以采用git config命令 */
git config --global push.default matching
或者
git config --global push.default simple

/* --all表示不管是否存在对应的远程分支，将本地的所有分支都推送到远程主机 */
git push --all origin
注意：如果远程主机的版本比本地版本更新，推送时Git会报错，要求先在本地做git pull合并差异，然后再推送到远程主机。如果想要强制推送，可以使用--force  (切记千万千万千万千万千万千万谨慎使用这个参数)
git push --force origin

/* git push默认不会推送标签(tag)，若想推送标签，应使用--tags参数*/
git push origin --tags
```

#### git clone：克隆远程仓库到本地

```javascript
/* 克隆远程库到本地 */
git clone #git_url

/* 克隆远程库到本地，并重新命名 */
git clone #git_url newName

/* 克隆远程库的一个指定分支到本地 */
git clone -b 分支名 #git_url
```

#### git fetch：将远程库的最新内容全部取回本地

```javascript
/* 将远程库的更新全部取回到本地 */
git fetch origin

/* 将远程库指定分支的更新全部取回到本地 */
git fetch origin 分支名

/* 取回更新后，会返回一个FETCH_HEAD，指的是在远程库上拉去下来的最新内容的分支，可以在本地通过它查看刚取回的更新信息 */
git log -p FETCH_HEAD
```

#### git pull：将远程库的最新内容全部取回本地并合并

> git pull 等价于：git fetch origin 分支名 + git merge FETCH_HEAD

```javascript
/* 将远程库的某个分支的更新取回，并与本地指定的分支合并 */
git pull origin 远程分支名:本地分支名

/* 如果远程分支是与当前本地分支合并，则冒号后面的部分可以省略 */
git pull origin 远程分支名
```

#### git rebase：把本地未push的分叉提交历史整理成直线

> rebase的目的是使得我们在查看历史提交的变化时更容易，因为分叉的提交需要三方对比。

---

### GIT分支管理相关指令

> 每次提交时，GIT都会把它们串成一条时间线，这条时间线就是一个分支。在前面的版本回退中提到的HEAD指向的就是当前分支。

#### git branch：创建、查看、删除分支

```javascript
/* 创建指定分支 */
git branch 分支名

/* 查看本地已存在的分支 */
git branch

/* 查看远程库的分支列表 */
git branch -r

/* 查看所有分支列表，包括本地和远程 */
git branch -a

/* 查看本地分支对应的远程分支 */
git branch -vv

/* 删除指定分支 */
git branch -d dev

/* 如果分支中有一些未merge的提交，使用上一个命令会删除失败，此时可以使用下面的命令强制删除分支 */
git branch -D 分支名

/* 为分支重命名 */
git branch -m oldName newName

/* 本地关联远程分支 */
git branch --set-upstream-to=origin/分支名 本地分支名
```

#### git checkout：切换分支

```javascript
/* 切换到指定分支 */
git checkout 分支名

/* 创建并切换分支，如果分支存在便切换分支 */
git checkout -b 分支名
```

#### git switch：创建、切换分支

> git checkout不仅可以切换分支还是撤销修改，同一指令却做不同的事情，容易让人迷惑，因此，分支的操作建议使用git switch。

```javascript
/* 创建并切换分支，如果分支存在便切换分支 */
git switch -c 分支名

/* 切换到指定分支 */
git switch 分支名
```

#### git merge：合并分支

```javascript
/* 合并指定分支到当前分支 */
git merge 分支名

/*当使用上面指令合并分支时，GIT会使用Fast forward模式，但在这种模式下，删除分支后，会丢失分支信息。如果要强制禁用Fast forward模式，GIT就会在merge时生成一个新的commit，这样，从分支历史上就可以看出分支信息。--no-ff参数表示禁用Fast forward模式*/
git merge --no-ff -m"xxx" 分支名
注意：因为使用上面的方式合并要创建一个新的commit，所以要加上-m参数，把描述写进去。

/* 禁用Fast forward模式的合并会有记录，使用git log可以查看出曾经做过合并。而Fast forward合并就看不出来曾经做过合并*/
```

#### git stash：将当前的工作状态保存到git栈，在需要的时候再恢复

```javascript
/* 备份当前的工作区的内容，将当前的工作状态保存到git栈，从最近的一次提交中读取相关内容，让工作区保证和上次提交的内容一致 */
git stash

/* 恢复git栈中最新的一个stash，并且会在git栈中删除该stash，最好是在git栈中只有一条stash时使用 */
git stash pop

/* 查看所有被stash的文件列表 */
git stash list

/* 恢复git栈中指定的stash文件，但是git栈中不删除该stash */
git stash apply stash@{n} //n表示git栈中恢复的stash文件的坐标，从0开始

/* 删除git栈中指定的stash文件，默认删除最新的进度 */
git stash drop stash@{n} //n表示git栈中删除的stash文件的坐标，从0开始

/* 清空git栈 */
git stash clear

/* 显示指定stash文件的内容 */
git stash show stash@{n}
```

#### git cherry-pick：“提炼”提交

```javascript
/* 复制一个特定的提交到当前分支 */
git cherry-pick commitID   //commitID表示提交的版本号
```

---

### GIT标签管理

#### git tag：创建、查看标签

```javascript
/* 为当前分支创建新标签，例如：git tag v1.0 */
git tag 标签名

/* 查看所有标签名（按字母顺序排序） */
git tag v1.0

/* 为过去的提交打上标签 */
git tag 标签名 commidID

/* 推送指定标签到远程 */
git push origin 标签名

/* 一次性推送全部尚未推送到远程的本地标签 */
git push origin --tags

/* 查看标签信息 */
git show 标签名

/* 创建带有说明的标签，-a指定标签名，-m指定说明文字 */
git tag -a 标签名 -m "xxxx" commitID

/* 删除标签（创建的标签都只存储在本地，不会自动推送到远程。所以，打错的标签可以在本地安全删除。） */
git tag -d 标签名

/* 删除远程库的标签 */
git push origin :refs/tags/标签名
```

> **==注意：标签总是和某个commit挂钩。如果commit既出现在master分支，又出现在dev分支，那么在这两个分支上都可以看到这个标签。==**

---

### GIT使用攻略

#### 单分支使用攻略

- [单分支使用攻略](C:\Users\admin\Desktop\单分支使用攻略.pdf)

  

  



 