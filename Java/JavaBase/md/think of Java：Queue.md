# Queue

Queue是java中实现队列的接口，它总共只有6个方法，我们一般只用其中3个就可以了。Queue的实现类有LinkedList和PriorityQueue。最常用的实现类是LinkedList。

Queue的6个方法分类：

压入元素(添加)：add()、offer()
相同：未超出容量，从队尾压入元素，返回压入的那个元素。
区别：在超出容量时，add()方法会对抛出异常，offer()返回false

弹出元素(删除)：remove()、poll()
相同：容量大于0的时候，删除并返回队头被删除的那个元素。
区别：在容量为0的时候，remove()会抛出异常，poll()返回false

获取队头元素(不删除)：element()、peek()
相同：容量大于0的时候，都返回队头元素。但是不删除。
区别：容量为0的时候，element()会抛出异常，peek()返回null。

队列除了基本的 Collection 操作外，还提供特有的插入、提取和检查操作(如上)。每个方法都存在两种形式：一种抛出异常（操作失败时），另一种返回一个特殊值（null 或 false，具体取决于操作）。插入操作的后一种形式是用于专门为有容量限制的 Queue 实现设计的；在大多数实现中，插入操作不会失败。

|      | **抛出异常** | **返回特殊值** |
| ---- | ------------ | -------------- |
| 插入 | add(e)       | offer(e)       |
| 删除 | remove()     | poll()         |
| 检查 | element()    | peek()         |


队列通常（但并非一定）以 FIFO（先进先出）的方式排序各个元素。不过优先级队列和 LIFO 队列（或堆栈）例外，前者根据提供的比较器或元素的自然顺序对元素进行排序，后者按 LIFO（后进先出）的方式对元素进行排序。无论使用哪种排序方式，队列的头 都是调用 remove() 或 poll() 所移除的元素。在 FIFO 队列中，所有的新元素都插入队列的末尾。其他种类的队列可能使用不同的元素放置规则。每个 Queue 实现必须指定其顺序属性。

如果可能，offer 方法可插入一个元素，否则返回 false。这与 Collection.add 方法不同，该方法只能通过抛出未经检查的异常使添加元素失败。offer 方法设计用于正常的失败情况，而不是出现异常的情况，例如在容量固定（有界）的队列中。

remove() 和 poll() 方法可移除和返回队列的头。到底从队列中移除哪个元素是队列排序策略的功能，而该策略在各种实现中是不同的。remove() 和 poll() 方法仅在队列为空时其行为有所不同：remove() 方法抛出一个异常，而 poll() 方法则返回 null。

element() 和 peek() 返回但不移除队列的头。

Queue 接口并未定义阻塞队列的方法，而这在并发编程中是很常见的。BlockingQueue 接口定义了那些等待元素出现或等待队列中有可用空间的方法，这些方法扩展了此接口。

Queue 实现通常不允许插入 null 元素，尽管某些实现（如 LinkedList）并不禁止插入 null。即使在允许 null 的实现中，也不应该将 null 插入到 Queue 中，因为 null 也用作 poll 方法的一个特殊返回值，表明队列不包含元素。

Queue 实现通常未定义 equals 和 hashCode 方法的基于元素的版本，而是从 Object 类继承了基于身份的版本，因为对于具有相同元素但有不同排序属性的队列而言，基于元素的相等性并非总是定义良好的

# Deque

Deque是一个双端队列接口，继承自Queue接口，Deque的实现类是LinkedList、ArrayDeque、LinkedBlockingDeque，其中LinkedList是最常用的。

Deque有三种用途：

1. 普通队列(一端进另一端出):

   ```java
   Queue queue = new LinkedList()或Deque deque = new LinkedList()
   ```

2. 双端队列(两端都可进出)

   ```java
   Deque deque = new LinkedList()
   ```

3. 堆栈

   ```java
   Deque deque = new LinkedList()
   ```

>  注意：Java堆栈Stack类已经过时，Java官方推荐使用Deque替代Stack使用。Deque堆栈操作方法：push()、pop()、peek()。

Deque是一个线性collection，支持在两端插入和移除元素。名称 deque 是“double ended queue（双端队列）”的缩写，通常读为“deck”。大多数 Deque 实现对于它们能够包含的元素数没有固定限制，但此接口既支持有容量限制的双端队列，也支持没有固定大小限制的双端队列。

此接口定义在双端队列两端访问元素的方法。提供插入、移除和检查元素的方法。每种方法都存在两种形式：一种形式在操作失败时抛出异常，另一种形式返回一个特殊值（null 或 false，具体取决于操作）。插入操作的后一种形式是专为使用有容量限制的 Deque 实现设计的；在大多数实现中，插入操作不能失败。

下表总结了上述 12 种方法：

|          | **第一个元素 (头部)** | **第一个元素 (头部)** | **最后一个元素 (尾部)** | **最后一个元素 (尾部)** |
| :------: | :-------------------: | :-------------------: | :---------------------: | :---------------------: |
|          |      *抛出异常*       |       *特殊值*        |       *抛出异常*        |        *特殊值*         |
| **插入** |      addFirst(e)      |     offerFirst(e)     |       addLast(e)        |      offerLast(e)       |
| **删除** |     removeFirst()     |      pollFirst()      |      removeLast()       |       pollLast()        |
| **检查** |      getFirst()       |      peekFirst()      |        getLast()        |       peekLast()        |

Deque接口扩展(继承)了 Queue 接口。在将双端队列用作队列时，将得到 FIFO（先进先出）行为。将元素添加到双端队列的末尾，从双端队列的开头移除元素。从 Queue 接口继承的方法完全等效于 Deque 方法，如下表所示：

| **Queue方法** | **等效Deque方法** |
| :-----------: | :---------------: |
|    add(e)     |    addLast(e)     |
|   offer(e)    |   offerLast(e)    |
|   remove()    |   removeFirst()   |
|    poll()     |    pollFirst()    |
|   element()   |    getFirst()     |
|    peek()     |    peekFirst()    |


双端队列也可用作 LIFO（后进先出）堆栈。应优先使用此接口而不是遗留 Stack 类。在将双端队列用作堆栈时，元素被推入双端队列的开头并从双端队列开头弹出。堆栈方法完全等效于 Deque 方法，如下表所示：

| **堆栈方法** | **等效Deque方法** |
| :----------: | :---------------: |
|   push(e)    |    addFirst(e)    |
|    pop()     |   removeFirst()   |
|    peek()    |    peekFirst()    |

