[ go入门指南](http://www.topgoer.com/)

# 一、快速入门

## 程序入口

1.必须是 main 包：package main

2.必须是 main 方法：func main（）

3.文件名不一定是 main.go

## 编写测试程序

1.源码文件以 _test 结尾：xxx_test.go

2.测试方法名以 Test 开头：func TestXXX（t *testing.T）{...}

# 二、运算符

## 算术运算符

Go 没有前置的 ++、-- 操作

## 位运算符

Go 增加了 &^ 按位清零

```go
1 &^ 0  = 1
1 &^ 1  = 0
0 &^ 1  = 0
0 &^ 0  = 0
```

# 三、标识符

## 可见性

1. 声明在函数内部，是函数的本地值，类似private
2. 在函数外部，是对当前包可见(包内所有.go文件都可见)的全局值，类似protect
3. 在函数外部且首字母大写是所有包可见的全局值,类似public

## 四种主要声明方式

​    var（声明变量）, const（声明常量）, type（声明类型） ,func（声明函数）

# 四、流程控制

-  switch 不用加 break
-  没有 while
-  支持 goto

# 五、数据类型

## 常量

```go
const {
  Monday = iota +1
  Tuesday
  Wednesday
}
const {
  Read = 1 << iota
  Write
}
```



## 基本数据类型

默认使用的 int 是 int64，默认使用的 float 是 float64

基本数据类型的转换需要强制转换，大范围转小范围时不会报错，而是当作溢出处理，如下：

```go
var a int16 = 257
println(int8(a))
// output
1
```

类型的预定义值在 math 包中，如 math.MaxInt64 

Go 中包含指针，用法和 C 语言类似，但 Go 不支持指针运算

```go
a := 1 
aPtr := &a 
aPtr = aPtr +1 //错误
```

## 数组

### 数组的声明

```go
var a [3] int //声明并初始化为默认值
a[0] = 1
b := [3]int {1,2,3} //声明同时初始化
d := [...]int {1,2,3} 
c := [2][2] int{{1,2},{3,4}} //多维数组初始化	 

```

### 数组的遍历

```go
for idx , e:=range arr{	//idx代表索引 e代表元素
 	...
}
for _ , e:=range arr{	//_ 代表不使用这个，但知道这里有个值
 	...
}
```

### 数组的截取

arr [开始的索引（包含）: 结束的索引（不包含）]

```go
a := [...] int {1,2,3,4,5}
a [1:2] //2
a [1:3] //2,3
a [1:len(a)] //2,3,4,5
a [1:] //2,3,4,5
a [:3] //1,2,3
```

### 数组的比较

在 Java 中数组是引用类型，不是值类型，所以 == 比较的是数组的引用，而不是比较里边的值。Go 中相同维数且含有相同个数元素的数组才可以比较，当每个元素都想同时才相等。

## 切片

### 切片的初始化

其中 len 个元素会被初始化为默认值，未初始化元素不可访问。Java中数组在创建时会将全部元素初始化。

![image-20210313165726604](https://pic.networkcv.top/2021/03/13/image-20210313165726604.png)

```go
var slice1 [] int
slice2 := [] int {1,2,3}
slice3 := make([]int,3,5) 
```

可变长的数组，以2的幂指数倍进行自动扩容，注意扩容后的地址引用会发生变化，不过 append（） 会返回新的切片地址

```go
func TestSliceInsert(t *testing.T) {
	var a []int
	b := append(a, 1)
	t.Log(a)
	t.Log(b)
}
```

### 切片共享存储结构

<img src="https://pic.networkcv.top/2021/03/14/image-20210314105743622.png" alt="image-20210314105743622" style="zoom: 50%;" />

### 切片的比较

切片只能和 nil 进行比较，不能和其他的切片进行比较。

## Map

### Map的声明

```go

func TestMapInit(t *testing.T) {
	map1 :=map[string]int {"one": 1,"two":2,"three":3}
	map2 :=map[string]string{}
	map3 :=make(map[string]string,10/* initial capacity*/)
	map2["one"]="1"
	t.Log(map1)
	t.Log(map2)
	t.Log(map3)
}
```

### Map元素的访问

与其他主要编程语言的差异，在访问的 key 不存在时，仍然会返回存储val的默认值，因此不会发生空指针异常

```go
func TestMapAccess(t *testing.T) {
	map1 := map[string]int{}
	//map1["a"]=0
	if val, exist := map1["a"]; exist {
		t.Log("存在a的值是：", val)
	} else {
		t.Logf("不存在a值，返回默认%d!", val)
	}
}
```

### Map的遍历

```go
func TestTravelMap(t *testing.T){
	m1 := map[int]int {1:1,2:4,3:8}
	for k,v :=range m1{
		t.Log(k,v)
	}
}
```



### Map与工厂模式

```go
func TestMapFactory(t *testing.T) {
	m := map[int]func(op int) int{}
	m[1] = func(op int) int {
		return op
	}
	m[2] = func(op int) int {
		return op*op
	}
	t.Log(m[1](3))
	t.Log(m[2](3))
}
```

### Map实现set

Go 内置集合中没有 Set 实现，可以使用 map[type]bool 代替

```go
func TestMapForSet(t *testing.T) {
	mapSet := map[int]bool{}
	mapSet[1] = true
	n := 1
	if mapSet[n] {
		t.Logf("%d is existing", n)
	} else {
		t.Logf("%d is not existing", n)
	}
	t.Log(len(mapSet))
	delete(mapSet, 1)
	t.Log(len(mapSet))
}
```

## 字符串

### Unicode 与 UFT8

1.Unicode 是一种字符集（code point）

2.UTF8 是 unicode 的存储实现（转换为字符序列的规则 ）

3.Go 中提供了 rune 函数，来获取字符串的字符切片

```go
func TestStringRune(t *testing.T) {
	s := "a"
	slice := []rune(s)
	t.Logf("'%[1]c' Unicode 十进制 %[1]d 十六进制 %[1]x", slice[0])
	t.Logf("'%[1]c' UTF8 十进制 %[1]d 十六进制 %[1]x", s[0])
	s2 := "中"
	slice2 := []rune(s2)
	t.Logf("'中' Unicode 十六进制 %[1]x", slice2)
	t.Logf("'中' UTF8  十六进制 %[1]x", s2)
	t.Logf("'中' UTF8下保存的第一个字节   十六进制 %[1]x",s2[0])
	t.Logf("'中' UTF8下保存的第二个字节   十六进制 %[1]x",s2[1])
	t.Logf("'中' UTF8下保存的第三个字节   十六进制 %[1]x",s2[2])
	/**
	  	go_test.go:104: 'a' Unicode 十进制 97 十六进制 61
	    go_test.go:105: 'a' UTF8 十进制 97 十六进制 61
	    go_test.go:108: '中' Unicode 十六进制 [4e2d]
	    go_test.go:109: '中' UTF8  十六进制 e4b8ad
	    go_test.go:110: '中' UTF8下保存的第一个字节   十六进制 e4
	    go_test.go:111: '中' UTF8下保存的第二个字节   十六进制 b8
	    go_test.go:112: '中' UTF8下保存的第三个字节   十六进制 ad
	*/
}
```

编码与存储

<img src="https://pic.networkcv.top/2021/03/14/image-20210314172601493.png" alt="image-20210314172601493" style="zoom: 50%;" />

### 输出与格式化

- %c：单个字符
- %s：字符串（Go 中不支持）
- %b：二进制整数
- %d：十进制整数
- %x：十六进制整数
- %u：无符号十进制数
- %T：类型

### 字符串的特性

1.string 是数据类型，不是引用类型或指针类型

2.string 是只读的 byte slice，len 函数获取的是它包含的 byte 数。Go的字符使用 UTF-8 编码标识Unicode文本，字母占一个字节，汉字占3个字节

```go
	var s string
	s = "hello"
	t.Log(len(s)) //5
	//s[1]='1' // Cannot assign to s[1]
	s = "中"
	t.Log(len(s)) //3
```

3.string 的 byte 数组可以存放任何数据

```go
	var s string
	s = "\x30" //十六进制的48
	t.Log(s)
	s = "\x61"//十六进制的97
	t.Log(s)
```

4.字符串类型在初始化的时候是一个空串，不能用 str == nil 来判断是否有进行赋值



### 字符串常用方法

```go
func TestStringsFunc(t *testing.T) {
	s := "a,b,c"
	split := strings.Split(s, ",")
	join := strings.Join(split, ":")
	t.Log(reflect.TypeOf(split), split)
	t.Log(reflect.TypeOf(join), join)
}
```

# 六、函数

1. 可以有多个返回值

2. 所有参数都是值传递：slice，map，channel 会有传引用的错觉

3. 函数可以作为变量的值，因此可以当作参数和返回值

   ```go
   func returnMultiRandomValues() (int, int) {
   	time.Sleep(time.Second * 2)
   	return rand.Intn(10), rand.Intn(20)
   }
   
   func timeSpent(inner func() (int, int)) func() (int, int) {
   	return func() (int, int) {
   		start := time.Now()
   		i1, i2 := inner()
   		fmt.Println("time spent", time.Since(start).Seconds())
   		return i1, i2
   	}
   }
   func TestFunc(t *testing.T) {
   	fn := timeSpent(returnMultiRandomValues)
   	//v1, v2 := returnMultiRandomValues()
   	v3, v4 := fn()
   	//t.Log(v1, v2)
   	t.Log(v3, v4)
   }
   ```

4. defer 函数 

   延时执行的函数，类似于 Java 中的 finally 语句块，可以用来执行释放资源或锁

   ```go
   func TestDeferFunc(t *testing.T) {
   	defer func() {
   		t.Log("clean")
   	}()
   	t.Log("do")
   	panic("Fatal error")
   	/**
   	    go_test.go:162: do
   	    go_test.go:160: clean
   	--- FAIL: TestDeferFunc (0.00s)
   	panic: Fatal error [recovered]
   		panic: Fatal error
   	*/
   }
   ```

# 七、面向对象

Go 既算面向对象的语言也不算，Go 中不支持继承。

## 封装

### 数据封装

封装，也就是结构体的定义

```go
type Employee struct {
	Id   string
	Name string
	Age  int
}
```

### 实例创建及初始化

```go
func TestObjectInit(t *testing.T) {
	employee := Employee{"0", "jack", 20}
	employee2 := Employee{Name: "jack2", Age: 22}
	employee3 := Employee{}
	employee3.Name ="jack3"
	employee4 := & Employee{}
	employee5 := new(Employee)	//这里返回的是引用/指针，相当于 employee4:= & Employee{}
	employee5.Name = "jack5"	//与其他语言的差异，通过实例的指针访问成员不需要使用 ->
	employee5.Age = 25
	t.Log("employee,",employee)
	t.Log("employee2",employee2)
	t.Logf("employee3 %T",employee3)
	t.Logf("employee4 %T",employee4)
	t.Logf("employee5 %T",employee5)
	/*
	   go_7_test.go:22: employee, {0 jack 20}
	   go_7_test.go:23: employee2 { jack2 22}
	   go_7_test.go:24: employee3 main.Employee
	   go_7_test.go:25: employee4 *main.Employee
	   go_7_test.go:26: employee5 *main.Employee
	 */
}
```

### 行为（方法）封装

```go
//第一种定义方式在市里对应方法被调用时，实例的成员会进行值复制
func (e Employee) String1()  {
	println("string1 employee address",unsafe.Pointer(&e))
	println(" string1 employee.name address",unsafe.Pointer(&e.Name))
	//return fmt.Sprintf("ID:%s Name:%s Age:%d", e.Id, e.Name, e.Age)
}

//通常情况下为了避免内存拷贝，我们使用第二种定义方式
func (e *Employee) String2()  {
	println("string2 employee address",unsafe.Pointer(&e))
	println("string2 employee.name address",unsafe.Pointer(&e.Name))
	//return fmt.Sprintf("ID:%s Name:%s Age:%d", e.Id, e.Name, e.Age)
}

func TestObjectAddress(t *testing.T) {
	employee := Employee{"0", "jack", 20}
	println("TestObjectFunc employee address", unsafe.Pointer(&employee))
	println("TestObjectFunc employee.name address", unsafe.Pointer(&employee.Name))
	employee.String1()
	employee.String2()
	/*
		TestObjectFunc employee address 0xc000044720
		TestObjectFunc employee.name address 0xc000044730
		string1 employee address 0xc000044748
		string1 employee.name address 0xc000044758
		string2 employee address 0xc000044710
		string2 employee.name address 0xc000044730
	*/
}
```

## 接口

### 接口实现

1. 接口为非入侵式，实现不依赖于接口定义
2. 所有接口的定义可以包含在接口使用者包内

Duck Type式接口实现

```go
//接口定义
type Programmer interface{
  WriteHelloWorld() string
}
// 接口实现
type GoProgrammerImpl struct{1 
}
func (p *GoProgrammerImpl) WriteHelloWorld() string{
  return "helloWorld"
}

func TestObjectFunc(t *testing.T) {
	var p Programmer = new(GoProgrammerImpl)
	t.Log(p.WriteHelloWorld())
}
```

### 接口变量

<img src="https://pic.networkcv.top/2021/03/14/image-20210314223342324.png" alt="image-20210314223342324" style="zoom:50%;" />

### 自定义类型

- type IntConvertionFunc func(n int) int
- type MyPoint int 

### 空接口与断言

- 空接口可以表示任何类型，类似于 Java 中的 Object

- 通过断言来将空接口转换为制定类型

  ```go
  v,ok :=p.(int) //ok=true 时转换成功
  ```

  ```go
  func doSomething(p interface{}) {
  	//if i, ok := p.(int); ok {
  	//	println("int:", i)
  	//}
  	//if i, ok := p.(string); ok {
  	//	println("string:", i)
  	//}
  	switch v := p.(type) {
  	case int:
  		println("int:", v)
  	case string:
  		println("string:", v)
  	default:
  		println("unKnow")
  	}
  }
  
  func TestEmptyInterface(t *testing.T) {
  	doSomething(10)
  	doSomething("10")
  	doSomething(10.1)
  	/*
  		int: 10
  		string: 10
  		unKnow
  	*/
  }
  ```

### Go 接口的最佳实践

- 倾向于使用小的接口定义，很多接口只包含一个方法

  ```go
  type Reader interface{
    Read(p [] byte)(n int,err error)
  }
  type Writer interface{
    Write(p [] byte)(n int,err error)
  }
  ```

- 较大的接口的定义，可以由多个小接口定义组合而成

  ```go
  type ReadWriter interface{
    Reader
    Writer
  }
  ```

- 只依赖于必要功能的最小接口

  ```go
  func StoreData(reader Reader) error{
    ...
  }
  ```



## 继承

Go 不支持继承，不能用父类引用指向子类对象。

不过可以在结构体中引用其他结构体来复用方法。

```go
type Pet struct {
}

func (p *Pet) Speak() {
	println("...")
}
func (p *Pet) SpeakTo(name string) {
	p.Speak()
	println(name)
}

type Dog struct {
	Pet
}

func (d *Dog) Speak() {
	println("Wang!")
}

func TestObjectExtend(t *testing.T) {
	dog := Dog{}
	dog.SpeakTo("1")
	dog.Speak()
	/*
		...
		1
		Wang!
	*/
}
```

## 多态

多态是可以支持的。

```go
type Animal interface {
	Speak()
}

type Cat struct {
}

func (c *Cat) Speak() {
	println("喵!")
}

func AnimalSpeak(animal Animal) {
	animal.Speak()
	fmt.Printf("%T \n", animal)
}

func TestObjectExtendFunc(t *testing.T) {
	cat := new(Cat)
	//c := Cat{}
	//AnimalSpeak(c)	//只能传 *Cat 指针类型 
	AnimalSpeak(cat)
}
```

# 八、Go的错误机制

1. 没有异常机制

2. error  类型实现了 error 接口

   ```go
   type error interface{
     Error() string
   }
   ```

3. 可以通过 errors.New 来快速创建错误实例

   ```go
   errors.New("...")
   ```

## panic 与 os.Exit

- panic 用于不可恢复的错误
- panic 推出前会执行 defer 指定的内容
- os.Exit 退出时不会调用 defer 指定的函数
- os.Exit 退出时不会输出当前调用栈信息

## recover

```go
defer func(){
  if err := recover(); err !=nil{
    //恢复错误
  }
}
```

# 九、包 

大写才能被包外访问

## Init 方法

- 在 main 被执行前，所有依赖的 package 的init 方法都会被执行
- 不同包的 init 函数按照包导入的依赖关系决定执行顺序
- 每个包可以有多个 init 函数
- 包的每个源文件也可以有多个 init 函数，会按定义顺序执行

## package

1. 通过 go get 来获取远程依赖， get -u 强制从网络更新远程依赖
2. go get 对于代码在 Github 上的组织形式，直接以代码路径开始，不要有 src

## 依赖管理

1.5之后，提供了vendor 这种解决方案，来解决无法使用指定版本包的问题

查找依赖包路径的顺序如下：

1. 当前包下的 vendor 目录
2. 向上级目录查找，直到找到 src 下的 vendor 目录
3. 在 GOPATH 下面查找依赖包
4. 在 GOROOT 目录下查找

# 十、并发

## Thrad 与 Groutine

### 1.创建时默认的 stack 的大小

- JDK5 以后 Java Thread stack 默认为1M

- Groutine 的 stack 初始化大小为2k

### 2.和 KSE（kernel Space Entity，系统线程）的对应关系

- Java Thread 是 1：1

- Groutine 是 M：N

  <img src="https://pic.networkcv.top/2021/03/15/image-20210315203941615.png" alt="image-20210315203941615" style="zoom:50%;" />

```go
func TestGroutine(t *testing.T) {
	for i := 0; i < 10; i++ {
		go func(i int) {
			println(i)
		}(i)
	}
	time.Sleep(time.Second*2)
}
```

## 共享内存并发机制

### Mutex 和 RWLock

这两者位于 package sync 下

线程不安全的计数器

```go
func TestCounterThreadUnSafe(t *testing.T){
	count := 0
	for i:=0;i<5000;i++ {
		go func() {
			count++
		}()
	}
	t.Log(count)
	/*
	  go_10_test.go:24: 4586
	*/
}
```

线程安全的计数器

```go
func TestCounterThreadSafe(t *testing.T){
	var mut sync.Mutex
	count := 0
	for i:=0;i<5000;i++ {
		go func() {
			defer func() {
				mut.Unlock()
			}()
			mut.Lock()
			count++
		}()
	}
	time.Sleep(time.Second*1) //这里需要手动休眠主线程，等待协程执行完毕
	t.Log(count)
	/*
	   go_10_test.go:45: 5000
	*/
}
```

### WaitGroup

同样位于 sync 包下，类似于 CountDownLatch，不用手动指定主线程的等待时间

```go
func TestCounterWaitGroup(t *testing.T) {
	var wg sync.WaitGroup
	var mut sync.Mutex
	count := 0
	for i := 0; i < 5000; i++ {
		wg.Add(1)
		go func() {
			defer func() {
				wg.Done()
				mut.Unlock()
			}()
			mut.Lock()
			count++
		}()
	}
	wg.Wait()
	t.Log(count)
}
```

## CSP

### <img src="https://pic.networkcv.top/2021/03/15/image-20210315211008813.png" alt="image-20210315211008813" style="zoom:50%;" />

```go

func service() string {
	time.Sleep(time.Millisecond * 500)
	return "service() ok"
}

func otherWork() string {
	time.Sleep(time.Millisecond * 1000)
	return "otherWork() ok"
}

func TestChannel(t *testing.T) {
	result := service()
	otherWork()
	t.Log(result)
}

func AsyncService() chan string{
	//retChan := make(chan string) //这样声明的channel是阻塞的，只有等到取出chan中的值，协程才释放
  retChan := make(chan string,1) //buffedChan，非阻塞的，service() 执行完协程立刻释放
	//var retChan string	//这里只是需要可以返回的地址，方便后边到这个地址取返回值
	go func() {
		res := service()
		println("returned result")
		retChan <- res
    println("service exist")
		//retChan = res
	}()
	return retChan
}

func TestAsyncChannel(t *testing.T) {
	result := AsyncService()
	otherWork()
	t.Log(<-result)
}
```

## 多路选择和超时控制

### 多渠道的选择

```go
select {
  case ret:= <-retCh1:
 		 t.Logf("result %s",ret)
  case ret:= <-retCh2:
 		 t.Logf("result %s",ret)
  default:
  t.Error("NO one returned")
}
```

### 超时控制

```go
select {
  case ret:= <-retCh1:
 		 t.Logf("result %s",ret)
  case <-time.After(time.Second * 1):
	  t.Error("time out")
}
```

## channel 的关闭和广播

### channel 的关闭

- 向关闭的 channel 发送数据，会导致 panic
- v，ok <- channerl   ok 为 bool 值，true 表示正常接受，false 表示通道关闭
- 所有的 channel 接受者都会在 channel 关闭时，立刻从阻塞等待中返回且 ok 值为 fasle。这个广播机制常被用来向多个订阅者同时发送信号。

## Context 与任务的取消

### Context

- 根 Context : 通过 context.Background（）创建

- 子 Context : context.WithCancel（parentContext）创建

  ctx，cancel := context.WithCancel（context.Background()）

- 当前 Context     被取消时，基于它的子 context 都会被取消

- 接收取消通知 <- ctx.Done()

```go
func isCancelled(ctx context.Context) bool {
	select {
	case <-ctx.Done():
		return true
	default:
		return false
	}
}

func TestCancel(t *testing.T) {
	ctx, cancelFunc := context.WithCancel(context.Background())
	for i := 0; i < 5; i++ {
		go func(i int, ctx context.Context) {
			for {
				if isCancelled(ctx) {
					break
				}
				time.Sleep(time.Millisecond * 5)
			}
			println(i, "Cancelled")
		}(i, ctx)
	}
	cancelFunc()
	time.Sleep(time.Millisecond * 5)
}
```

## 只执行一次

```go
type Singleton struct{
}

var singleInstance *Singleton
var once sync.Once

func GetSingletonObj() *Singleton{
	once.Do(func(){
		println("create obj")
		singleInstance = new(Singleton)
	})
	return singleInstance
}

func TestGetSingletonObj(t *testing.T){
	var wg sync.WaitGroup
	for i:=0 ;i<10;i++{
		wg.Add(1)
		go func(){
			obj := GetSingletonObj()
			fmt.Printf("%d\n",unsafe.Pointer(obj))
			wg.Done()
		}()
	}
	wg.Wait()
}
```

