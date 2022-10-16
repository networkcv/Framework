## Swing

### windowClosed函数和windowClosing函数说明

**windowClosed**：当调用dispose时，执行该函数。是“The window is closed”的缩写。因为它表示被动，而非过去完成时。所以这个函数从来不会在windowClosing之后被调用。Swing也没有必要在关闭窗体的时候调用两个函数。

**windowClosing**：当窗体关闭时，执行该函数。确切地说，是“The window closes itself”的缩写，并加上-ing后辍，变成动名词形态。另外“The window is closing”（正在进行时）在某些情况下也说得通，比如设置了默认关闭。但是如果必须要在windowClosing中加入System.Exit(0)的话，正在进行时的解释就说不通了，因为调用这个函数的时候，窗体还没有关闭，函数退出后，窗体可能还不会关闭。由于它表示主动，而非正在进行时，所以swing不会在调用windowClosed之前调用这个函数，也没有必要调用这个函数。

Swing中：

点击窗体右上角的“X”时，调用windowClosing，不调用windowClosed。

使用另一个线程调用dispose关闭窗体时，调用windowClosed，不调用windowClosing。

```java
22:24:40.978 [AWT-EventQueue-0] INFO com.lwj.GameFrame.MyGameFrame - closed
```

JavaFX中：

JavaFX中这两个函数代表先后顺序。考虑到词义的误解，以及主动关闭与被动关闭的操作并不会有什么区别，这里不再区分主动关闭和被动关闭。关闭时会调用setOnWindowClosing中的方法对象，随后还会再次调用setOnWindowClosed中的方法对象。JavaFX中一般只需要用setOnWindowClosing就可以了。
