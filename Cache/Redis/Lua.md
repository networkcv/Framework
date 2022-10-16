## 1.Lua

Lua 是一个小巧的脚本语言，由标准C编写而成，几乎在所有操作系统和平台上都可以编译、运行。因此它没有提供强大的库，这是由它的定位决定的。所以Lua不适合作为开发独立应用程序的语言。

在Redis中使用Lua有很多好处，它可以减少网络开销，把多个操作一次性打包执行。Lua脚本天生支持原子性的操作，避免开启第三方事务，提高了性能。代码重用也是一个重要的好处，写好的代码会被加载到Redis内存中，可以供其他客户端使用，减少重复劳动。Lua脚本使用C语言写成的，执行速度很快，并天然具有可移植性，也是代码重用的很好体现。一个完整的Lua解释器，不过200k，在目前所有脚本引擎中，Lua的速度是最快的。这一切都决定了Lua是作为嵌入式脚本的最佳选择。

- **轻量级**: 它用标准C语言编写并以源代码形式开放，编译后仅仅一百余K，可以很方便的嵌入别的程序里。
- **可扩展**: Lua提供了非常易于使用的扩展接口和机制：由宿主语言(通常是C或C++)提供这些功能，Lua可以使用它们，就像是本来就内置的功能一样。
- **其它特性**
  - 支持面向过程(procedure-oriented)编程和函数式编程(functional programming)；
  - 自动内存管理；只提供了一种通用类型的表（table），用它可以实现数组，哈希表，集合，对象；
  - 语言内置模式匹配；闭包(closure)；函数也可以看做一个值；提供多线程（协同进程，并非操作系统所支持的线程）支持；
  - 通过闭包和table可以很方便地支持面向对象编程所需要的一些关键机制，比如数据抽象，虚函数，继承和重载等。

## 2.Lua的安装

**方式一**

mac 上可以直接使用 brewhome 进行安装

```
brew install lua
```

**方式二**

```
curl -R -O http://www.lua.org/ftp/lua-5.3.0.tar.gz
tar zxf lua-5.3.0.tar.gz
cd lua-5.3.0
make macosx test
make install
```



安装好之后，执行 **lua** 命令就可以进入lua的交互命令行模式。

```
networkcavalry@NetworkCavalry-MBP / % lua
Lua 5.2.3  Copyright (C) 1994-2013 Lua.org, PUC-Rio
> 
```

## 3.Lua的基本语法

[lua基本语法学习链接](https://www.runoob.com/lua/lua-basic-syntax.html)

[redis在使用lua脚本以及实现redis分布式锁](https://blog.csdn.net/zjcjava/article/details/84842115?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromBaidu-1.control&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromBaidu-1.control)

[lua book](https://github.com/52fhy/lua-book)



## 4.cjson的安装

前面有提到 lua 本身不提供类库，在编写脚本时，我们常常会需要对 Json 进行转换，这个时候就需要一个工具包来为我们提供该功能，**cjson** 就可以很好的实现这一点，它可以将字符串转换为Lua的类型。

1. 将 **lua-cjson-2.1.0.tar.gz** 下载后进行解压 

   ```
   tar zxvf lua-cjson-2.1.0.tar.gz [-C /usr/local]
   ```

2. 进入解压目录修改 Makefile 文件

   ```
   ##### Build defaults #####
   LUA_VERSION =       5.4.2     修改为lua的版本，lua -v 查看版本
   TARGET =            cjson.so
   PREFIX =            /usr/local	
   #CFLAGS =            -g -Wall -pedantic -fno-inline
   CFLAGS =            -O3 -Wall -pedantic -DNDEBUG
   CJSON_CFLAGS =      -fpic
   #CJSON_LDFLAGS =     -shared
   CJSON_LDFLAGS =     -bundle -undefined dynamic_lookup
   LUA_INCLUDE_DIR =   $(PREFIX)/include
   LUA_CMODULE_DIR =   $(PREFIX)/lib/lua/$(LUA_VERSION)
   LUA_MODULE_DIR =    $(PREFIX)/share/lua/$(LUA_VERSION)
   LUA_BIN_DIR =       $(PREFIX)/bin
   ```

3. 在该目录下 进行 **编译** 和 **安装**

   ```
   networkcavalry@NetworkCavalry-MBP lua-cjson-2.1.0 % make
   networkcavalry@NetworkCavalry-MBP lua-cjson-2.1.0 % make install
   ```

   操作完成后进行检查， **/usr/local/lib/lua/5.2** 目录下存在 **cjson.so** 这个文件，如果没有的话，去cJson的解压目录进行手动复制。

这样就可以在本地使用cjson 进行 encode 和 decode了

```lua
local cjson = require "cjson"
local retTable = {};    --最终产生json的表

--顺序数值
local intDatas = {};
intDatas[1] = 100;
intDatas[2] = "100";

--数组
local aryDatas = {};
aryDatas[1] = {};
aryDatas[1]["键11"] = "值11";
aryDatas[1]["键12"] = "值12";
aryDatas[2] = {};
aryDatas[2]["键21"] = "值21";
aryDatas[2]["键22"] = "值22";

--对Table赋值
retTable["键1"] = "值1";
retTable[2] = 123;
retTable["int_datas"] = intDatas;
retTable["aryDatas"] = aryDatas;

--将表数据编码成json字符串
local jsonStr = cjson.encode(retTable);
print(jsonStr);

return retTable;

```



## 5.ZeroBrane Studio 安装

ZeroBrane Studio 是一个轻量级的开源的 Lua IDE，支持 语法突出显示，代码分析器，实时编码 和 断点调试。

进行安装目录，执行命令打开IDE

```
networkcavalry@NetworkCavalry-MBP ZeroBraneStudio-1.90 % ./zbstudio.sh 
```

**注意事项：**

执行前需要进行配置

![image-20210124230845841](https://pic.networkcv.top/2021/01/24/image-20210124230845841.png)

```
path.lua="/usr/local/bin/lua"
```

## 6.Lua应用场景

### 1.Lua实现限流

### 2.Lua实现分布式锁

### 3.Lua脚本结合Redis使用

## 7.Lua Redis [debug]()

```
redis-cli -h r-uf6y01md4e3nrelhaq.redis.rds.aliyuncs.com -p 6379 -a ETyqgTgsK85TGK0h --ldb --eval ~/a.lua  price_pick_up_skuId_metas:{1001009} price_pick_up_skuId_metas:{1001009}:100001 , 1612144900001
```

https://cloud.tencent.com/developer/article/1683207