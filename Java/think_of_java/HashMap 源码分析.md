几个注意的点：

- HashMap允许key和value为null，但只能有一个key为null，是因为hash(null)返回结果为0。

- JDK8之前，底层为数组+链表，JDK8之后，底层为数组+链表+红黑树。

- 构造中只对一些字段初始化，如initialCapacity（初始容量）、loadFactor（负载因子）、threshold（阈值），并没有真正创建存储键值对的容器，在执行插入操作的时候，才会去初始化该容器，阈值是由容量乘以负载因子计算而来，即`threshold = capacity * loadFactor`。

- threshold 其实是通过 `tableSizeFor(int cap)` 来定义的，该方法是找到大于或等于 cap 的最小2的多少次方

  ```java
  static final int tableSizeFor(int cap) {
      int n = cap - 1;
      n |= n >>> 1;
      n |= n >>> 2;
      n |= n >>> 4;
      n |= n >>> 8;
      n |= n >>> 16;
      return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
  }
  ```

- 查找，hash%n=hash & (n-1)

  ```java
      final Node<K,V> getNode(int hash, Object key) {
          Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
          if ((tab = table) != null && (n = tab.length) > 0 &&
              (first = tab[(n - 1) & hash]) != null) {
              if (first.hash == hash && // always check first node
                  ((k = first.key) == key || (key != null && key.equals(k))))
                  return first;
              if ((e = first.next) != null) {
                  if (first instanceof TreeNode)
                      return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                  do {
                      if (e.hash == hash &&
                          ((k = e.key) == key || (key != null && key.equals(k))))
                          return e;
                  } while ((e = e.next) != null);
              }
          }
          return null;
      }
  ```

- 计算hash值的方法，这里就是key可以为null的原因，^ 异或，运算规则为1^0 = 1 , 1^1 = 0 , 0^1 = 1 , 0^0 = 0

  ```java
  static final int hash(Object key) {
      int h;
      //高16位与低16位，进行异或运算，以此增大低位的随机性
      return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
  }
  ```

## 



https://segmentfault.com/a/1190000012926722