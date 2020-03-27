[HashMap 源码详细分析(JDK1.8)](https://segmentfault.com/a/1190000012926722)


## HashMap 源码解析

```java
final float loadFactor;		//负载因子，用来反应HashMap数组桶的使用情况
transient Node<K,V>[] table;	//存放键值对的散列表
int threshold;	//阈值 存放键值对的最大值，超过阈值就会扩容 threshold = capacity * loadFactor。
int initialCapacity;	//HashMap 初始容量
```

```java
public HashMap() {
    this.loadFactor = DEFAULT_LOAD_FACTOR; // all other fields defaulted
}

public HashMap(int initialCapacity, float loadFactor) {
    if (initialCapacity < 0)
        throw new IllegalArgumentException("Illegal initial capacity: " +
                                           initialCapacity);
    if (initialCapacity > MAXIMUM_CAPACITY)
        initialCapacity = MAXIMUM_CAPACITY;
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
        throw new IllegalArgumentException("Illegal load factor: " +
                                           loadFactor);
    this.loadFactor = loadFactor;
    this.threshold = tableSizeFor(initialCapacity);
}
```

自定义容量的阈值 threshold 其实是通过 `tableSiz1eFor(int cap)` 来定义的，该方法是找到大于或等于 cap （容量）的最小2次幂。

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

### get（）

HashMap 的查找操作比较简单，查找步骤与原理篇介绍一致，即先定位键值对所在的桶的位置，然后再对链表或红黑树进行查找。通过这两步即可完成查找，该操作相关代码如下：

```java
public V get(Object key) {
    Node<K,V> e;
    return (e = getNode(hash(key), key)) == null ? null : e.value;
}

static final int hash(Object key) {
    int h;
     //高16位与低16位，进行异或运算，以此增大低位的随机性
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}

final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
    // 1. 定位键值对所在桶的位置
    if ((tab = table) != null && (n = tab.length) > 0 &&
        (first = tab[(n - 1) & hash]) != null) {
        if (first.hash == hash && // always check first node
            ((k = first.key) == key || (key != null && key.equals(k))))
            return first;
        if ((e = first.next) != null) {
            // 2. 如果 first 是 TreeNode 类型，则调用黑红树查找方法
            if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
                
            // 2. 对链表进行查找
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

### put（）

```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}

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
        // 这里是对应数组下标位置不为空的情况，保存的可能是链表也可能是红黑树
        Node<K,V> e; K k;
        // hash不相等，则equals一定不等，hash值相等，equals不一定相等，equals相等 hash值一定相等
        // 这里单独判断对应桶的第一个节点
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            // 链表的情况，遍历链表，如果找到key相同则break退出，否则添加节点到链表最后
            // 添加完成后判断节点个数是否超过树化的阈值(8)，超过则对链表树化
            for (int binCount = 0; ; ++binCount) {
                // 循环找到对应下标的最后一个节点，用e来指向
                if ((e = p.next) == null) {
                    //新建节点并添加到该节点后面
                    p.next = newNode(hash, key, value, null);
                    // 当添加了第九个节点后树化该链表
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                // 用于定位链表
                if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k))))
                    break;
                p = e;
            }
        }
        // 用于返回旧值和修改已有的值
        if (e != null) { // existing mapping for key
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                // 对定位的键值对赋新值
                e.value = value 
            afterNodeAccess(e);
            return oldValue;
        }
    }
	// 修改次数递增    
    ++modCount;
    // 判断已添加的键值对数量是否大于扩容的阈值，阈值为12时，添加了第13个元素后才会进行扩容
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
        // 超出了可容纳的最大容量
        if (oldCap >= MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return oldTab;
        }
        // 容量扩大一倍，阈值也随着扩大一倍，每次容量都是2的幂指数个
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
    // 如果之前的数组不为空的话，需要将键值对从旧数组迁移到新数组
    if (oldTab != null) {
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            // 每次循环开始e都指向数组下标的位置
            if ((e = oldTab[j]) != null) {
                // 清空对应数组下标的引用，help GC
                oldTab[j] = null;
                //对应数组下标只有一个元素
                if (e.next == null)
                    //将hash值与新的数组数量进行 & 操作，定位到桶的位置
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
// 树化链表 将普通节点链表转换成成树形节点链表
final void treeifyBin(Node<K,V>[] tab, int hash) {
    int n, index; Node<K,V> e;
    // 虽然进入树化方法，但还是会根据数组长度EIFY_CAPACITY（64）来判断是否树化
    // 优先进行扩容而不是树化，只有当数组长度大于64时才需要树化
    if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
        resize();
    else if ((e = tab[index = (n - 1) & hash]) != null) {
        // hd 为头节点（head），tl 为尾节点（tail）
        TreeNode<K,V> hd = null, tl = null;
        do {
            // 将普通节点替换成树形节点
            TreeNode<K,V> p = replacementTreeNode(e, null);
            if (tl == null)
                hd = p;
            else {
                p.prev = tl;
                tl.next = p;
            }
            tl = p;
        } while ((e = e.next) != null);	// 将普通链表转成树形节点链表
        if ((tab[index] = hd) != null)
            // 将树形链表转换成红黑树
            hd.treeify(tab);
    }
}
```

在扩容过程中，树化要满足两个条件：

1. 链表长度大于等于 TREEIFY_THRESHOLD
2. 桶数组容量大于等于 MIN_TREEIFY_CAPACITY

第一个条件比较好理解，这里就不说了。这里来说说加入第二个条件的原因，参考原因如下：

当桶数组容量比较小时，键值对节点 hash 的碰撞率可能会比较高，进而导致链表长度较长。这个时候应该优先扩容，而不是立马树化。毕竟高碰撞率是因为桶数组容量较小引起的，这个是主因。容量小时，优先扩容可以避免一些列的不必要的树化过程。同时，桶容量较小时，扩容会比较频繁，扩容时需要拆分红黑树并重新映射。所以在桶容量比较小的情况下，将长链表转成红黑树是一件吃力不讨好的事。

小结：

1. 当桶数组 table 为空时，通过扩容的方式初始化 table
2. 查找要插入的键值对是否已经存在，存在的话根据条件判断是否用新值替换旧值
3. 如果不存在，则将键值对链入链表中，并根据链表长度8及数组长度64决定是否将链表转为红黑树
4. 判断键值对数量是否大于阈值，大于的话则进行扩容操作