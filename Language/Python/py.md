# 并发编程

线程是真正被CPU调度的最小单位，进程是为线程提供资源的单位（同样也是资源分配的最小单位）。

## fork 和 spawn 

不同系统创建子进程的方式不一样。Linux系统fork，win:spawn，mac支持：fork和spawn （python3.8默认设置spawn）

python在创建子进程的操作必须要在main函数里，不然会报错。

 mac手动指定fork创建进程的方式，也可以解决。

```python
multiprocessing.set_start_method('fork')
```



首先fork和spawn都是构建子进程的不同方式，区别在于：

fork：除了必要的启动资源外，其他变量，包，数据等都继承自父进程，并且是copy-on-write的，也就是共享了父进程的一些内存页，因此启动较快，但是由于大部分都用的父进程数据，所以是不安全的进程。

spawn：从头构建一个子进程，父进程的数据等拷贝到子进程空间内，拥有自己的Python解释器，所以需要重新加载一遍父进程的包，因此启动较慢，由于数据都是自己的，安全性较高。

## GIL锁

GIL，全局解释器锁（Global Interpreter Lock），是CPython中，让一个进程同一时刻只能有一个线程可以被CPU调用。无法充分利用多核CPU的性能。但可以用于IO密集的运算场景。

因此如果想利用CPU的多核优势，也就是CPU密集型运算适合多进程开发（即使资源开销大），如果是IO密集型的话可以使用多线程的方式，或者协程。



## 线程锁

加锁的另外一种方式

```python
with lock:
	# 自动加锁和释放锁
```

**Lock 和 RLock**

Lock 和 RLock 都是 threading模块下的类，使用上基本相似。

两者区别是RLock 支持可重入锁。



## 协程

python 除了支持进程、线程，还支持协程（使用asyncio），这种需要 手动的 await 来将控制权交还给 event loop。

但有些情况下，单纯的协程也不好用，例如一个需要长时间处理的计算任务不着急可以慢慢做，还有一些小的需要在低延迟时间下解决的响应任务， 如果不做任何处理，  协程可能会陷入到计算任务中出不来，需要低延迟的响应任务无法得到及时响应。

当然，现在可以在 asyncio 中使用 run_in_executor来解决这个问题。





# 商城项目实战

前后端不分离的开发模式，有助于SEO

后端框架采用 Django + Jinja2模版引擎，整体由后端完成渲染。

前端框架采用 Vue.js，局部部分由前端完成渲染。



## 项目整体架构

![img](img/py/18项目架构设计.png)



- Uwsgi 服务器类似于 Tomcat Web服务器
- Celery 类似于Java 的 Quartz

## Django

创建Django项目

```django
django-admin startproject mall
```

切换到 manage.py 目录下启动项目

```python
 python manage.py runserver
```









