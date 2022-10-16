参考博客 ：

[HashMap 源码详细分析(JDK1.8)](https://segmentfault.com/a/1190000012926722)

[《Java 8系列之重新认识HashMap》](https://zhuanlan.zhihu.com/p/21673805)

## HashMap 源码解析

基于Java 8的源码

```java
final float loadFactor;		//负载因子，用来反应HashMap数组桶的使用情况
transient Node<K,V>[] table;	//存放键值对的散列表
int threshold;	//阈值 存放键值对的最大值，超过阈值就会扩容 threshold = capacity * loadFactor。
int initialCapacity;	//HashMap 初始容量
```

### 构造方法

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

### 插入

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
       /*
        * 这里单独判断对应桶的首节点，可以理解为桶位置的首节点是存在数组中，该节点后续的节点存在链表中
        * hash不相等，则equals一定不等，hash值相等，equals不一定相等，equals相等 hash值一定相等
        * 这里的判断也很有意思，充分应用了 && 和 || 的特性，首先判断hash值是否相等，在hash值相等的基础上
        * 再去通过key的地址和equals去判断，因为我们在使用HashMap的时候，务必要将作为键的实体hashcode和
        * equals方法,如果不重写hashcode方法，换个对象则会导致无法取出存入map的键值对，因为通过hash计算
        * 桶的位置不对，而不重写equals方法也会导致无法取值对应的键值对。
        */
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            e = p;
        // 如果桶中的节点类型是TreeNode，则调用红黑树的插入方法
        else if (p instanceof TreeNode)
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else {
            // 链表的情况，遍历链表，如果找到key相同则break退出，否则添加节点到链表最后
            // 添加完成后判断链表节点个数（不含数组桶位置的第一个节点）是否超过树化的阈值(8)，超过则对链表树化
            for (int binCount = 0; ; ++binCount) {
                // 循环找到对应下标的最后一个节点，用e来指向
                if ((e = p.next) == null) {
                    //新建节点并添加到该节点后面
                    p.next = newNode(hash, key, value, null);
                    // 当某一个桶位置添加了第九个节点后树化该链表的方法
                    // 这里减去首节点是因为该节点不需要遍历链表就可以得到
                    if (binCount >= TREEIFY_THRESHOLD - 1) // -1 for 1st
                        treeifyBin(tab, hash);
                    break;
                }
                // 用于判断对应节点是否已存在，详细内容参考上面的介绍
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

小结：

1. 当桶数组 table 为空时，通过扩容的方式初始化 table
2. 查找要插入的键值对是否已经存在，存在的话根据条件判断是否用新值替换旧值
3. 如果不存在，则将键值对链入链表中，并根据链表长度8及数组长度64决定是否将链表转为红黑树
4. 判断键值对数量是否大于阈值，大于的话则进行扩容操作

###  扩容

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
        //调用HashMap(int initialCapacity) ,当时没有创建数组，也没有用成员变量保存initialCapacity，而是调用一个tableSizeFor()方法来根据传入的容量计算出大于等于该容量的最小二次幂，并保存到threshold中，这里的oldThr就是之前的计算结果，这里将其直接用作新数组的初始大小，并在后面创建数组
        newCap = oldThr;
    else {
        // 调用HashMap的无参构造后，初始化数组的时候会走到这里，DEFAULT_INITIAL_CAPACITY 默认为16
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
    // 迁移的过程是一个桶一个桶的迁移
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
                    // 重新映射时，需要对红黑树进行拆分
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                else { // preserve order
                    /*
                     *  遍历链表，并将链表节点按原顺序进行分组
                     * loHead 指向 hash & oldCap =0 的节点
                     * hiHead 指向 hash & oldCap !=0 的节点
                     * 这里什么要用oldCap进行&操作 而不是之前的 cap-1 呢？
                     * 原因在于在数组扩容后，键值对的位置需要重新计算，但不是所有的元素都会移动，
                     * 需要移动的只有与原数组容量进行 &操作后不为0的元素
                     * loHead 代表的是lowHead hiHead 代表的是HighHead 
                     * 这里的高低对应的是在数组中桶下标的高低
                     * loHead是位置不变的，hiHead是位置要调整的，新位置 = oldCap + 原位置
                     */
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
                    // 将分组后的链表映射到新桶中
                    if (loTail != null) {
                        loTail.next = null;
                        newTab[j] = loHead;
                    }
                    if (hiTail != null) {
                        hiTail.next = null;
                        //新位置 = oldCap + 原位置
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

小结：

1. 计算新桶数组的容量 newCap 和新阈值 newThr
2. 根据计算出的 newCap 创建新的桶数组，桶数组 table 也是在这里进行初始化的
3. 将键值对节点重新映射到新的桶数组里。如果节点是 TreeNode 类型，则需要拆分红黑树。如果是普通节点，则节点按原顺序进行分组。

### 链表树化

```java
/**
 * 当桶数组容量小于该值时，优先进行扩容，而不是树化
 */
static final int MIN_TREEIFY_CAPACITY = 64;

static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
        TreeNode<K,V> parent;  // red-black tree links
        TreeNode<K,V> left;
        TreeNode<K,V> right;
        TreeNode<K,V> prev;    // needed to unlink next upon deletion
        boolean red;
        TreeNode(int hash, K key, V val, Node<K,V> next) {
            super(hash, key, val, next);
        }
}

// 树化链表 将普通节点链表转换成成树形节点链表
final void treeifyBin(Node<K,V>[] tab, int hash) {
    int n, index; Node<K,V> e;
    // 虽然进入树化方法，但还是会根据数组长度EIFY_CAPACITY（64）来判断是否树化
    // 优先进行扩容而不是树化，只有当数组长度在64以上时才需要树化
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

TreeNode<K,V> replacementTreeNode(Node<K,V> p, Node<K,V> next) {
    return new TreeNode<>(p.hash, p.key, p.value, next);
}
```

在扩容过程中，树化要满足两个条件：

1. 链表长度大于等于 TREEIFY_THRESHOLD
2. 桶数组容量大于等于 MIN_TREEIFY_CAPACITY

第一个条件比较好理解，这里就不说了。这里来说说加入第二个条件的原因，参考原因如下：

当桶数组容量比较小时，键值对节点 hash 的碰撞率可能会比较高，进而导致链表长度较长。这个时候应该优先扩容，而不是立马树化。毕竟高碰撞率是因为桶数组容量较小引起的，这个是主因。容量小时，优先扩容可以避免一些列的不必要的树化过程。同时，桶容量较小时，扩容会比较频繁，扩容时需要拆分红黑树并重新映射。所以在桶容量比较小的情况下，将长链表转成红黑树是一件吃力不讨好的事。

**将树形链表转换成红黑树**

```java
final void treeify(Node<K,V>[] tab) {
    TreeNode<K,V> root = null;
    for (TreeNode<K,V> x = this, next; x != null; x = next) {
        next = (TreeNode<K,V>)x.next;
        x.left = x.right = null;
        if (root == null) {
            x.parent = null;
            x.red = false;
            root = x;
        }
        else {
            K k = x.key;
            int h = x.hash;
            Class<?> kc = null;
            for (TreeNode<K,V> p = root;;) {
                int dir, ph;
                K pk = p.key;
                if ((ph = p.hash) > h)
                    dir = -1;
                else if (ph < h)
                    dir = 1;
                else if ((kc == null &&
                          (kc = comparableClassFor(k)) == null) ||
                         (dir = compareComparables(kc, k, pk)) == 0)
                    dir = tieBreakOrder(k, pk);

                TreeNode<K,V> xp = p;
                if ((p = (dir <= 0) ? p.left : p.right) == null) {
                    x.parent = xp;
                    if (dir <= 0)
                        xp.left = x;
                    else
                        xp.right = x;
                    root = balanceInsertion(root, x);
                    break;
                }
            }
        }
    }
    moveRootToFront(tab, root);
}
```

![img](img/693998858-5a659719ba5df_articlex.jfif)

HashMap 在设计之初，并没有考虑到以后会引入红黑树进行优化。所以并没有像 TreeMap 那样，要求键类实现 comparable 接口或提供相应的比较器。但由于树化过程需要比较两个键对象的大小，在键类没有实现 comparable 接口的情况下，怎么比较键与键之间的大小了就成了一个棘手的问题。为了解决这个问题，HashMap 是做了三步处理，确保可以比较出两个键的大小，如下：

1. 比较键与键之间 hash 的大小，如果 hash 相同，继续往下比较
2. 检测键类是否实现了 Comparable 接口，如果实现调用 compareTo 方法进行比较
3. 如果仍未比较出大小，就需要进行仲裁了，仲裁方法为 tieBreakOrder

![img](img/1319480885-5a659719a15dc_articlex.jfif)

橙色的箭头表示 TreeNode 的 next 引用。由于空间有限，prev 引用未画出。可以看出，链表转成红黑树后，原链表的顺序仍然会被引用仍被保留了（红黑树的根节点会被移动到链表的第一位），我们仍然可以按遍历链表的方式去遍历上面的红黑树。这样的结构为后面红黑树的切分以及红黑树转成链表做好了铺垫，我们继续往下分析。

### 红黑树拆分

扩容后，普通节点需要重新映射，红黑树节点也不例外。按照一般的思路，我们可以先把红黑树转成链表，之后再重新映射链表即可。这种处理方式是大家比较容易想到的，但这样做会损失一定的效率。不同于上面的处理方式，HashMap 实现的思路则是上好佳。如上节所说，在将普通链表转成红黑树时，HashMap 通过两个额外的引用 next 和 prev 保留了原链表的节点顺序。这样再对红黑树进行重新映射时，完全可以按照映射链表的方式进行。这样就避免了将红黑树转成链表后再进行映射，无形中提高了效率。

```java
// 红黑树转链表阈值
static final int UNTREEIFY_THRESHOLD = 6;

final void split(HashMap<K,V> map, Node<K,V>[] tab, int index, int bit) {
    TreeNode<K,V> b = this;
    // Relink into lo and hi lists, preserving order
    TreeNode<K,V> loHead = null, loTail = null;
    TreeNode<K,V> hiHead = null, hiTail = null;
    // lowCount highCount
    int lc = 0, hc = 0;
    /* 
     * 红黑树节点仍然保留了 next 引用，故仍可以按链表方式遍历红黑树。
     * 下面的循环是对红黑树节点进行分组，与链表分组的代码类似
     */
    for (TreeNode<K,V> e = b, next; e != null; e = next) {
        next = (TreeNode<K,V>)e.next;
        e.next = null;
        if ((e.hash & bit) == 0) {
            if ((e.prev = loTail) == null)
                loHead = e;
            else
                loTail.next = e;
            loTail = e;
            ++lc;
        }
        else {
            if ((e.prev = hiTail) == null)
                hiHead = e;
            else
                hiTail.next = e;
            hiTail = e;
            ++hc;
        }
    }
    // 下边这两部分是判断红黑树在分组后是否需要进行链化
    // 如之前的红黑树有10个元素，现在resize后重新有5个元素要到新的桶去，这样这两个链都不满足
    // 成为红黑树的阈值 (8)，因此会对分组后的链表根据 红黑树链化的阈值6来决定是否链化
    if (loHead != null) {
        // 如果 loHead 不为空，且链表长度小于等于 6，则将红黑树转成链表
        if (lc <= UNTREEIFY_THRESHOLD)
            tab[index] = loHead.untreeify(map);
        else {
            tab[index] = loHead;
            /* 
             * hiHead == null 时，表明扩容后，
             * 所有节点仍在原位置，树结构不变，无需重新树化
             */
            if (hiHead != null) // (else is already treeified)
                loHead.treeify(tab);
        }
    }
    // 与上面类似
    if (hiHead != null) {
        if (hc <= UNTREEIFY_THRESHOLD)
            tab[index + bit] = hiHead.untreeify(map);
        else {
            tab[index + bit] = hiHead;
            if (loHead != null)
                hiHead.treeify(tab);
        }
    }
}
```
**红黑树链化**

前面说过，红黑树中仍然保留了原链表节点顺序。有了这个前提，再将红黑树转成链表就简单多了，仅需将 TreeNode 链表转成 Node 类型的链表即可。相关代码如下：

```java
final Node<K,V> untreeify(HashMap<K,V> map) {
    Node<K,V> hd = null, tl = null;
    for (Node<K,V> q = this; q != null; q = q.next) {
        Node<K,V> p = map.replacementNode(q, null);
        if (tl == null)
            hd = p;
        else
            tl.next = p;
        tl = p;
    }
    return hd;
}

Node<K,V> replacementNode(Node<K,V> p, Node<K,V> next) {
    return new Node<>(p.hash, p.key, p.value, next);
}
```

### 查询

HashMap 的查找操作比较简单，查找步骤与插入相似，即先定位键值对所在的桶的位置，然后再对链表或红黑树进行查找。通过这两步即可完成查找，该操作相关代码如下：

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

### 删除

HashMap 的删除操作并不复杂，仅需三个步骤即可完成。第一步是定位桶位置，第二步遍历链表并找到键值相等的节点，第三步删除节点。相关源码如下：

```java
public V remove(Object key) {
    Node<K,V> e;
    return (e = removeNode(hash(key), key, null, false, true)) == null ?
        null : e.value;
}

final Node<K,V> removeNode(int hash, Object key, Object value,
                           boolean matchValue, boolean movable) {
    Node<K,V>[] tab; Node<K,V> p; int n, index;
    if ((tab = table) != null && (n = tab.length) > 0 &&
        // 1. 定位桶位置
        (p = tab[index = (n - 1) & hash]) != null) {
        Node<K,V> node = null, e; K k; V v;
        // 如果键的值与链表第一个节点相等，则将 node 指向该节点
        if (p.hash == hash &&
            ((k = p.key) == key || (key != null && key.equals(k))))
            node = p;
        else if ((e = p.next) != null) {  
            // 2. 如果是 TreeNode 类型，调用红黑树的查找逻辑定位待删除节点
            if (p instanceof TreeNode)
                node = ((TreeNode<K,V>)p).getTreeNode(hash, key);
            else {
                // 2. 遍历链表，找到待删除节点
                do {
                    if (e.hash == hash &&
                        ((k = e.key) == key ||
                         (key != null && key.equals(k)))) {
                        node = e;
                        break;
                    }
                    p = e;
                } while ((e = e.next) != null);
            }
        }
        
        // 3. 删除节点，并修复链表或红黑树
        if (node != null && (!matchValue || (v = node.value) == value ||
                             (value != null && value.equals(v)))) {
            if (node instanceof TreeNode)
                ((TreeNode<K,V>)node).removeTreeNode(this, tab, movable);
            else if (node == p)
                tab[index] = node.next;
            else
                p.next = node.next;
            ++modCount;
            --size;
            afterNodeRemoval(node);
            return node;
        }
    }
    return null;
}
```

## Java 1.7 中的HashMap

先提一下1.7中HashMap的扩容机制，在1.7中还没有引入红黑树，在resize方法中，会使用链表的头插法进行新老数组桶的拷贝，而1.7 中HashMap的多线程问题就是发生在这个地方。

这里通过代码简单介绍一下，链表的头插法。

```java
public void test() {
    Node node1 = new Node(1);
    Node node2 = new Node(2);
    Node node3 = new Node(3);
    node1.next = node2;
    node2.next = node3;

    Node[] oldTable = new Node[3];
    oldTable[1] = node1;
    System.out.println(Arrays.asList(oldTable));

    Node[] newTable = new Node[3];
    // 头插法 画图可以更好理解
    Node node = oldTable[1];
    Node next ;
    while(node!=null){
        next=node.next;
        node.next=newTable[1];
        newTable[1]=node;
        node=next;
    }
    System.out.println(Arrays.asList(newTable));
}
```

对比尾插法，可以看出头插法的最大好处是，插入节点时不需要便利到链表的末尾，直接插入即可，不足之处在于，头插法会反转链表的顺序。

###  扩容造成死循环分析过程

https://www.jianshu.com/p/4d1cad21853b



###  扩容造成数据丢失分析过程



## 总结

HashMap是基于拉链法实现的一个散列表，内部由数组、链表和红黑树实现。

数组的默认初始容量为16，默认的负载因子（loadFactor）是0.75，默认阈值为12，threshold = capacity * loadFactor，负载因子是用来描述HashMap的最大存放比率，长度为16的散列表也可以插入无限个节点，会对所有节点的hash值与桶的个数（16）取模，来将它们插入到不同桶位置的链表中，但这样的做法会使得HashMap效率低下，定位到具体桶的时间复制度为O(1)，但是后续查找长链表的时间复制度就变成了O(n)，因此使用负载因子这个比率来确定决定数组是否扩容的阈值（threshold），这样数组的长度随着插入节点数动态的增长，理想的情况下，每个数组桶均匀的存储，如160个节点保存在16个桶中，每个保存10个节点，但实际由于hashcode的不确定性，有的桶可能一个都没有，有的桶可能存放了20个（比较极端的例子），为了保证HashMap整体能保持O（1）的时间复杂度，因此会在达到一个比例时进行扩容，因此如果查询频繁的情况下，可以适当的牺牲空间，将负载因子调小。

容量每次都是2的幂指数，即可以使用 hash & (n-1) 优化定位的效率，又可以在扩容时方便计算新位置

**插入**

1. 当数组桶table为空时，会通过 resize（）来进行初始化
2. 通过hash定位到数组桶的位置，如果该位置为空则直接插入
3. 不为空的情况，则可能发生hash冲突，需要进一步判断
   1. 当前位置第一个节点的key是否与要新增的key为同一个，是的话就变成了修改操作，将值进行替换
   2. 判断当前节点类型是否为树形节点，如果是树形节点则需要进行红黑树的插入操作 putTreeVal（）
   3. 走到这里意味着当前节点是普通的链表节点类型，遍历到链表的末尾，插入元素，如果链表的长度超过8，则会调用 treeifBin（）进行树化操作，在遍历途中如果遇到key相同的情况，则中断遍历，当作修改操作
4. 递增容器修改次数modCount，并判断当前Map的存储元素数是否超过阈值，超过则需 resize（）扩容

**扩容**

1. 计算新桶数组的容量 newCap 和新阈值 newThr，新容量总是2的幂指数，新阈值为 newCap * loadFactor
2. 根据计算出的 newCap 创建新的桶数组，桶数组 table 也是在这里进行初始化的
3. 如果旧的 table 不为空，则需进行复制。将键值对节点重新映射到新的桶数组里。如果节点是 TreeNode 类型，则需要拆分红黑树 split（）。如果是普通节点，则节点按原顺序进行分组，根据桶位置是否变化分成两条链表，由于数组容量变化的特殊性，桶位置变化的链表中节点的新位置都是 旧的桶位置+ 旧的桶数组容量，这样就很容易的将链表重新映射到新的桶数组中，而且不改变原有的节点相对顺序。

**树化**

1. 链表长度大于树化阈值（8）时进入该方法
2. 进一步判断是否需要树化，判断当前数组桶的长度是否大于 64，小于64则需要的是扩容而不是树化
3. 将链表节点转换为树形节点
4. 调用 treeify（）真正进行树化操作，由于key需要进行比较，才能插入到红黑树的有效位置
   1. 比较键与键之间 hash 的大小，如果 hash 相同，继续往下比较
   2. 检测键类是否实现了 Comparable 接口，如果实现调用 compareTo 方法进行比较
   3. 如果仍未比较出大小，就需要进行仲裁了，仲裁方法为 tieBreakOrder
5. 在链表被转成红黑树后，原链表的 next指针 和 prev指针 的指向仍然会被保留，这样仍然可以按照遍历链表的方式去遍历红黑树，这样为红黑树的拆分和红黑树的链化做好了铺垫

**红黑树拆分**

1. 由于在树化的时候保留了链表的next指针和prev指针，所以我们可以把红黑树当作链表来处理，因此拆分的过程和链表分组类似，根据桶位置是否变化来分组
2. 对分组后的两个链表进行是否需要链化的判断，这是因为拆分后的红黑树节点数可能不足8个，如果红黑树的个数小于等于6个则进行链化的操作，否则对分组后的链表重新树化

**查询**

1. 先定位键值对所在的桶的位置
2. 判断该桶位置的第一个节点是否为我们要找的
3. 然后再对链表或红黑树进行查找