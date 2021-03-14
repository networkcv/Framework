[go入门指南](http://www.topgoer.com/)

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

## 继承

Go 不支持继承，不过可以在结构体中引用其他结构体来调用对方方法

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
```

