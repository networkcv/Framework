[HashMap 源码详细分析(JDK1.8)](https://segmentfault.com/a/1190000012926722)

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

  


## HashMap 源码解析

```java
final float loadFactor;		//负载因子
transient Node<K,V>[] table;	//存放键值对的散列表
int threshold;	//存放键值对的阈值，超过阈值就会扩容
```

### put（）

```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}
```

```java
static final int hash(Object key) {
    int h;
     //高16位与低16位，进行异或运算，以此增大低位的随机性
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

计算hash值的方法，这里就是可以有一个key为null的原因，^ 异或，运算规则为不同为1，相同为0，1^0 = 1 , 1^1 = 0 , 0^1 = 1 , 0^0 = 0。

这里为什么要进行异或运算？原因在于后边需要根据hash值进行取模运算，通过余数来定位数组的下标，我们通常可以使用 `%` 来进行取模，但其实还有更高效的方式，就是通过位运算来取模，如 `17%16=1` 使用位运算 `17&(16-1)=1`。

```java
       0000  0000  0000  0000  0001  0001
&      0000  0000  0000  0000  0000  1111
------------------------------------------
       0000  0000  0000  0000  0000  0001
```

但使用这种方法有一个限制就是其中一个二进制必须是掩码，也就是二进制都是由1组成，这里也解释了为什么HashMap的数组长度总是2的幂指数个，这样是为了每次通过hash定位的数组下标所做的优化。

那和高16位与低16位进行异或运算又有什么关系呢？

```java
       0010  1101  0111  1100  1000  0101
&      0000  0000  0000  0000  0000  1111
------------------------------------------
       0000  0000  0000  0000  0000  0101
```

通过上例可以看出，由于数组的长度开始比较小，因此每次hash值都是后几位在进行 `&` 运算，这样后几位会形成规律性重复问题，而这时通过高16位与低16位进行异或运算，低位混合了高位的特性，这样最后几位的随机性就增大了，结果就是键值对会更加均匀的分散到各个数组桶中，而不是挤在一个数组桶中。

```java
final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
               boolean evict) {
    Node<K,V>[] tab; Node<K,V> p; int n, i;
    // 这里的table就是HashMap存放数据的数组，在第一次添加键值对的时候才会初始化HashMap
    if ((tab = table) == null || (n = tab.length) == 0)
		// rezise()是HashMap中很重要的一个方法，它是用来重新计算数组大小并扩容
        n = (tab = resize()).length;
    /*
     * 这里就用到了上面说过的通过位运算取模，这里使用的数组长度n，被很巧妙的赋值了
     * 在上面判断table是否为空时， || 有个特点，如果前边为true，后边的表达式就不会计算了
     * 若table不为null，会在 n = tab.length中赋值，table为空，会将新数组的大小赋给n
     * 如果数组在该下标位置为null，则代表没有存储元素，则将新键值对节点的引用存入该位置
     */
    if ((p = tab[i = (n - 1) & hash]) == null)
        // Node<K,V> newNode(int hash, K key, V value, Node<K,V> next)
        tab[i] = newNode(hash, key, value, null);
    else {
        /// 这里是对应数组下标位置不为空的情况，保存的可能是链表也可能是红黑树
        Node<K,V> e; K k;
        // hash不相等，则equals一定不等，hash值相等，equals不一定相等，equals相等 hash值一定相等
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            //链表的情况
            for (int binCount = 0; ; ++binCount) {
                //对应数组下标位置只有一个键值对
                if ((e = p.next) == null) {
                    //新建节点并添加到该节点后面
                    p.next = newNode(hash, key, value, null);
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        //树化该链表
                        treeifyBin(tab, hash);
                    break;
                }
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        // 用于返回旧值
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e);
            return oldValue;
        }
    }
	// 修改次数递增    
    ++modCount;
    // 判断已添加的键值对数量是否大于扩容的阈值
    if (++size > threshold)
        // 重新计算size，进行扩容
        resize();
    afterNodeInsertion(evict);
    return null;
}
```

```java
// 重新计算大小，扩容
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    if (oldCap > 0) {
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY)
            newThr = oldThr << 1; // double threshold
    }
    else if (oldThr > 0) // initial capacity was placed in threshold
        newCap = oldThr;
    else {
        // 初始化数组的时候会走到这里，DEFAULT_INITIAL_CAPACITY 默认为16
        newCap = DEFAULT_INITIAL_CAPACITY;	
        // DEFAULT_LOAD_FACTOR默认为0.75 所以newThr（新阈值）为12
        newThr = (int)( DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    if (newThr == 0) {
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                  (int)ft : Integer.MAX_VALUE);
    }
    // 设置新阈值
    threshold = newThr;
    // 创建新的数组大小
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab;
    // 如果之前的数组不为空的话，需要将键值对从老数组迁移到新数组
    if (oldTab != null) {
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {
                oldTab[j] = null;
                if (e.next == null)
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { // preserve order
                    Node<K,V> loHead = null, loTail = null;
                    Node<K,V> hiHead = null, hiTail = null;
                    Node<K,V> next;
                    do {
                        next = e.next;
                        if ((e.hash & oldCap) == 0) {
                            if (loTail == null)
                                loHead = e;
                            else
                                loTail.next = e;
                            loTail = e;
                        }
                        else {
                            if (hiTail == null)
                                hiHead = e;
                            else
                                hiTail.next = e;
                            hiTail = e;
                        }
                    } while ((e = next) != null);
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        newTab[j + oldCap] = hiHead;
                    }
                }
            }
        }
    }
    //返回新数组的引用
    return newTab;
}
```



```java
// 树化链表
final void treeifyBin(Node<K,V>[] tab, int hash) {
    int n, index; Node<K,V> e;
    if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
        resize();
    else if ((e = tab[index = (n - 1) & hash]) != null) {
        TreeNode<K,V> hd = null, tl = null;
        do {
            TreeNode<K,V> p = replacementTreeNode(e, null);
            if (tl == null)
                hd = p;
            else {
                p.prev = tl;
                tl.next = p;
            }
            tl = p;
        } while ((e = e.next) != null);
        if ((tab[index] = hd) != null)
            hd.treeify(tab);
    }
}
```