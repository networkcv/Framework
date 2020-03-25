默认空参构造一个size为0的数组，也可以构造一个指定大小的数组。

```java
List<String> list0 = new ArrayList<>(11);
List<String> list = new ArrayList<>();
```

### add (E e)

```java
public boolean add(E e) {
    // 在添加元素之前，会先通过 ensureCapacityInternal() 来确认内部容量。
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    elementData[size++] = e;
    return true;
}
```

```java
private void ensureCapacityInternal(int minCapacity) {
    // 如果此时的 elementData 数组元素为0，则会在 添加后的元素个数 与 默认容量10 之间取最大值作为确定的容量。
    if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
        minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
    }
	// 接着将确定后的最小容量 minCapacity 传入 ensureExplicitCapacity()。
    ensureExplicitCapacity(minCapacity);
}
```

```java
private void ensureExplicitCapacity(int minCapacity) {
    // modify count 用来记录集合元素是否被修改过，用于保证容器的线程安全
    modCount++;
    // 新添元素后数组容量大于当前数组的最大长度时才需要扩容
    if (minCapacity - elementData.length > 0)
        // 真正扩容的方法
        grow(minCapacity);
}
```

```java
private void grow(int minCapacity) {
    // overflow-conscious code
    // 先获取当前元素个数
    int oldCapacity = elementData.length;
    // newCapacity =  oldCapacity+(oldCapacity/2)  
    // newCapacity扩容为之前的1.5倍
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    if (newCapacity - minCapacity < 0)
		// 因为oldCapacity初始为0，所以newCapacity×1.5还是0
        // 所以这里相当于设置初始的扩容最小为10
        newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    // 底层使用本地方法 arraycopy 完成数组拷贝
    //arraycopy(Object src,  int  srcPos, Object dest, int destPos, int length);
    //这里传入 newCapacity 是为了指定创建新数组的容量大小
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```

### addAll (Collection<? extends E>  c)

```java
public boolean addAll(Collection<? extends E> c) {
    Object[] a = c.toArray();
    // 要添加集合的元素个数
    int numNew = a.length;
    // 确保数组容量足够，不够会自动扩容
    ensureCapacityInternal(size + numNew);  // Increments modCount
    // a：源数组 0：源数组起始位置 elementData：目标数组 size：目标数组起始位置 numNew：复制个数
    System.arraycopy(a, 0, elementData, size, numNew);
    size += numNew;
    return numNew != 0;
}
```

### add (int index, E element) 

```java
public void add(int index, E element) {
    // 检查传入的索引值是否正确
    rangeCheckForAdd(index);
    // 同上，检查数组内部容量
    ensureCapacityInternal(size + 1);  // Increments modCount!!
    // 巧妙的使用 arraycopy 完成数组元素移动
    System.arraycopy(elementData, index, elementData, index + 1,
                     size - index);
    //将元素插入到指定位置
    elementData[index] = element;
    //集合元素个数++
    size++;
}
```

```java
private void rangeCheckForAdd(int index) {
    //合法索引位置 为从0到size，添加到size等同于追加到末尾
    if (index > size || index < 0)
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}
```

### get (int index) 

```java
public E get(int index) {
    // 检查索引
    rangeCheck(index);
    // 返回指定索引位置的元素
    return elementData(index);
}
```

```java
private void rangeCheck(int index) {
    // 注意这里是 索引 与 数组元素比较，如果index小于0 数组会抛出异常
    if (index >= size)
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}
```

```java
E elementData(int index) {
    // 通过数组支持随机访问的特性，返回指定索引位置的元素
    return (E) elementData[index];
}
```

### indexOf (Object o) 

```java
public int indexOf(Object o) {
    //比较使用的是 equals 方法，因此需要单独比较null
    if (o == null) {
        for (int i = 0; i < size; i++)
            if (elementData[i]==null)
                return i;
    } else {
        for (int i = 0; i < size; i++)
            if (o.equals(elementData[i]))
                return i;
    }
    // 找不到返回-1
    return -1;
}
```

### set (int index, E element) 

```java
public E set(int index, E element) {
		// 检查index是否小于size，如果不是抛异常
        rangeCheck(index);
        E oldValue = elementData(index);
        elementData[index] = element;
        // 覆盖ArrayList中index上的元素。
        return oldValue;
        // 返回被覆盖的元素。
    }
private void rangeCheck(int index) {
    if (index >= size)
        throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
}

```

### clone () 

```java
public Object clone() {
    try {
        //这里用到了泛型的通配符
        ArrayList<?> v = (ArrayList<?>) super.clone();
        v.elementData = Arrays.copyOf(elementData, size);
        v.modCount = 0;
        return v;
    } catch (CloneNotSupportedException e) {
        // this shouldn't happen, since we are Cloneable
        throw new InternalError(e);
    }
}
```

### remove (int index) 

```java
public E remove(int index) {
    rangeCheck(index);
    modCount++;
    // 获取指定索引位置的元素 用于返回
    E oldValue = elementData(index);
    int numMoved = size - index - 1;
    if (numMoved > 0)
        // 进行数组的移动，这里就体现出链表的好处了
        System.arraycopy(elementData, index+1, elementData, index,
                         numMoved);
    elementData[--size] = null; // clear to let GC do its work
    return oldValue;
}
```

### removeRange (int fromIndex, int toIndex) 

```java
protected void removeRange(int fromIndex, int toIndex) {
    modCount++;
    int numMoved = size - toIndex;
    System.arraycopy(elementData, toIndex, elementData, fromIndex,
                     numMoved);
    // clear to let GC do its work
    int newSize = size - (toIndex-fromIndex);
    for (int i = newSize; i < size; i++) {
        elementData[i] = null;
    }
    size = newSize;
}
```

### clear() 

```java
public void clear() {
    modCount++;
    // clear to let GC do its work
    for (int i = 0; i < size; i++)
        elementData[i] = null;

    size = 0;
}
```

### trimToSize () 

```java
public void trimToSize() {
	// 去除数组中冗余的空间，返回一个length和size相等的数组
    modCount++;
    if (size < elementData.length) {
        elementData = (size == 0)
          ? EMPTY_ELEMENTDATA
          : Arrays.copyOf(elementData, size);
    }
}
```