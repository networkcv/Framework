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

GIL，全局解释器锁（Global Interpreter Lock），是CPython中，让一个进程同一时刻只能有一个线程可以被CPU调用。

因此如果想利用CPU的多核优势，也就是CPU密集型运算适合多进程开发（即使资源开销大），如果是IO密集型的话可以使用多线程的方式。









