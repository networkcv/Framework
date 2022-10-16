一、数据库,二、数据表,三、约束,四、索引,五、增删改数据表,六、查询数据表
七、外键，八、多表查询，九、子查询，十、事务，十二、视图，十三、数据库的高级操作
开机后在DOS窗口下输入 services.msc打开服务系统，启动MySQL。
或net start mysql/net stop mysql

一、数据库

1.创建数据库
CREATE　DATABASE [mydb] CHARACTER SET [utf8] COLLATE [utf8_bin]; 
指定字符集为gbk， 校队规则为 utf8_bin.

2.查询数据库
SHOW DATABASES;	//显示所有数据库
SHOW CREATE DATABASE [mydb] //查询创建数据库的属性

3.修改数据库
ALTER DATABASE [mydb] CHARACTER SET [utf8] COLLATE [utf8_bin];
修改字符集和校对规则.

4.删除数据库
DROP DATABASE [mydb];

5.选择数据库
USE [mydb]	切换数据库
select DATABASE();	查询当前使用的数据库

二、数据表

1.创建表
CREATE TABLE [table](
id int PRIMARY KEY(主键约束) AUTO_INCREMENT(自动增长),
name varchar(20),
gender char(1),
birthday date,
entry_date date,
job varchar(20),
salay double,
resume text
);

2.查看表
SHOW TABLES;	//显示所有表。
DESC [table];	//查询表结构。
SHOW CREATE TABLE [table]; //查询创建数据表的语句

3.修改表
ALTER TABLE [table] ADD [column(列)] [datatype];	//增加列
ALTER TABLE [table] MODIFY [column] [datatype];		//修改列
ALTER TABLE [table] DROP [column];	//删除列
RENAME TABLE [oldtable] TO [newtable];	//修改表名
ALTER TABLE [table] CHARACTER SET [utf8];	//修改字符集名
ALTER TABLE [table] CHANGE [column] [oldname] [newname];   //修改列名

4.删除表
DROP ATBLE [table];	//删除表 

三、约束

1.主键约束
单字段  id int primary key,
多字段  primary key(id,name),

2.非空约束
id int not null,

3.唯一约束
id int unique,

4.默认约束
id int default 0,

5.字段自动增长
auto_increment

四、索引

[unique|fulltext|spatial] index|key [别名] (字段名([长度]) [asc|desc])
1.普通索引
CREATE TABLE [table](
id int PRIMARY KEY(主键约束) AUTO_INCREMENT(自动增长),
name varchar(20),
gender char(1),
birthday date,
entry_date date,
job varchar(20),
salay double,
resume text,
index(id)
);

2.唯一索引
CREATE TABLE [table](
id int PRIMARY KEY(主键约束) AUTO_INCREMENT(自动增长),
name varchar(20),
gender char(1),
birthday date,
entry_date date,
job varchar(20),
salay double,
resume text,
unique index unique_id(别名) (id，ASC)
);

3.全文索引	只能用于字符型(char varchar text)
CREATE TABLE [table](
id int PRIMARY KEY(主键约束) AUTO_INCREMENT(自动增长),
name varchar(20),
gender char(1),
birthday date,
entry_date date,
job varchar(20),
salay double,
resume text,
fulltext index fulltext_id (id)
)engine=myisam;

4.单列索引
CREATE TABLE [table](
id int PRIMARY KEY(主键约束) AUTO_INCREMENT(自动增长),
name varchar(20),
gender char(1),
birthday date,
entry_date date,
job varchar(20),
salay double,
resume text,
 index single_name (name(20))
);

5.多列索引
CREATE TABLE [table](
id int PRIMARY KEY(主键约束) AUTO_INCREMENT(自动增长),
name varchar(20),
gender char(1),
birthday date,
entry_date date,
job varchar(20),
salay double,
resume text,
 index multi_name (id,sname(20))
);

6.在存在的表上创建索引 
create [unique|fulltext|spatial] index [索引名] on [表名] (字段名([长度]) [asc|desc])

7.删除索引
alter table [表名] drop index [索引名]

drop index [索引名] on [表名] 

五、增删改数据表

1.insert 
	insert into [table] ([column],[columan]) values ([value,value]); 

	insert into [table] values ([value,value]); 
2.update 
	update [table] set [column]=[value] where [条件];

	update [table] set [column]=[value],[column]=[value] where [条件];
3.delete 
	delete from [table] where [条件]

4.truncate 
	truncate [table] 摧毁旧表并创建结构一样的新表

六、查询数据表

1.普通查询 select [distinct(去掉重复的)] *|column1,column2.. from [table];
	seclect name,english from table;
	
	select name,english+10,math+10 from table;
	
	select name,english+math from table;
	
	select name as 姓名,english+math as 总成绩 from table;
	select name   姓名,english+math  总成绩 from table;	效果同上

2.带有where的过滤查询
	select * from exam where name='张三';

	select * from exam where english>90;
	
	select * from exam where english between 80 and 100;	//在80到100之间
	
	select * from exam where english in (75,76,77);	//是75，76，77中的一个
	
	select * from exam where name like '张%';	//姓张   %多个通配字  _只代表一个通配字符 
	
	select * from exam where english > 80 and math >70;

3.排序查询 位于句尾

	select [column1,column2..] from [table] order by [column(表中的列名|AS后的列名)] [asc(升序)|desc(降序)]

4.聚合函数
	<1> count() 用来统计行数
	     select count(*) from [table];

	     select count(*) from [table] where math>80;
	
	<2> sum() 求符合条件的某列的和值
	     select sum(*) from [table];	
	
	     select sum(english),sum(math) from [table];
	
	     select sum(chinese)/count(*) as 全班语文平均分 from [table]; 
	<3> avg() 求复合条件列的平均值
	     select avg(math) from [table];
	
	<4> max(),min() 求复合条件的最大最小值
	     select max(chinese+math+english) from [table];

5.分组查询  分组后可以想象为摞在了一起，只显示最上边的记录  
	select product,sum(price) from orders group by product;

	where的过滤只能在分组前进行，
	having可以在分组后过滤。
	
	查询总价大于100元的商品名称
	select product ,sum(price) from orders group by product having sum(price)>100;
	
	查询单价小于100而总价大于100的商品的名称
	select product,sum(price) from orders where price<100 group by product having sum(price)>100;

6.部分查询 limit(限制)
	select [column1]..|* from [table] limit [offset](开始查询的位置),[记录数];
	select * from table limit 0,5; 从第0个开始 往后查5个

七、外键

	外键：可以明确的声明表示表与表之间关系的字段的参照关系，
	      使数据库帮我们维护这种关系，这种键就叫做表与表之间声明了一个外键。
	      在后续的操作中由于新增修改删除造成了破坏外键的约束的状况发生时，
	      数据库会检测到这种操作从而阻止这类操作。
1.在创建表的时候添加外键
	foreign key(ordersid) references order(id);

	create table dept(
	id int primary key auto_increment,
	name varchar(40)
	);
	
	insert into dept values	(null,'财务部');
	
	create table emp(
	id int primary key auto_increment,
	name varchar(40),
	dept_id int,
	foreign key (id) references dept(id)
	);
2.对已有表添加外键
	alter table 表名 add constraint [aaa(外键名)] foreign key([外键字段名]) references 外表表名(主键字段名)
	
	alter table emp add constraint aaa_id foreign key(dept_id) references dept(id);
3.删除外键
	alter table 表名 drop foreign key 外键名;

八、多表查询

	1.笛卡尔积查询:是两张表相乘的结果，其中包含大量错误数据。
	select * from dept,emp;
	
	2.内连接查询:查询出左边表有且右边表也有点记录
	
	select * from dept,emp where dept.id = emp.dept_id;
	select * from dept inner join emp on dept.id = emp.dept_id; 
	
	3.外连接查询
	
	左外连接查询:在内连接的基础上增加左边表有而右边表没有的
	select * from dept left join emp on dept.id = emp.dept_id;
	
	右外连接查询:在内连接的基础上增加左边表有而右边表没有的
	select * from dept right join emp on dept.id = emp.dept_id;
	
	全外连接查询:在内连接的基础上增加左边表有而右边表没有的
	select * from dept full join emp on dept.id = emp.dept_id;	#mysql暂不支持全外连接！
	
	select * from dept left join emp on dept.id = emp.dept_id;
	union
	select * from dept right join emp on dept.id = emp.dept_id;	#mysql可以union左外和右外，模拟全外连接
	
	查询4号部门的名称和员工姓名
	select dpet.name 部门,emp.name 员工 from dept inner join emp on dept.id=emp.dept_id where dept.id=4;

九、子查询

	1.in关键字的子查询
	查询员工年龄为20岁的部门
	select name from dept where did in (select did from emp wher age=20);
	not in 作用刚好与in相反
	
	2.exists关键字的子查询  相当于boolean，有值返回true反之返回false
	查询emp表中是否存在年龄大于21岁的员工，存在的话则查询dept表的所有记录
	select * from dept where exeists (select * from emp where age>21);
	
	3.带any关键字的子查询	只要满足内部表达式的任意一个条件，就返回一个外层查询结果
	select * from dept where did>any(select did from emp);
	
	4.带all关键字的子查询	要同时满足内部表达式的所有一个条件，才返回一个外层查询结果
	select * from dept where did>any(select did from emp);
	
	5.带比较运算符的子查询
	查询赵四的部门名称
	select dname from dept where did = (select did from emp where name='赵四')

十、事务 

	事务控制
	    事务：逻辑上的一组操作，要么一起完成要么一起不完成
	    start transaction;	开启事务，这条语句之后的sql语句处于同一事务中，并不立即影响数据库
	    commit;	提交事务， 事务中的sql语句对数据库起作用
	    rollback;	回滚事务，取消这个事务，这个事务不会对数据库产生影响
	    
	    start transaction;
	    update [table] set money=money-100 where name='a';
	    update [table] set money=money+100 where name='b';
	    commit;/rollback;  提交或回滚
	
	事务四大特性(ACID)数据库设计者负责：
	    原子性：
		事务是一组不可分割的单位，要么同时成功要么同时不成功
	    一致性：
		事务前后的数据完整性应该保持一致
		（数据库的完整性：如果数据库在某个时间点下，所有的数据都符合所有的约束，则称数据库符合完整性）
	    隔离性：
		多个用户并发访问数据库时，一个事务不能被其他事务所干扰，多个并发的事务之间的数据要互相隔离
	    持久性：
		事务一旦被提交，则对数据库中的改变就是永久的，不可逆。
	
	隔离性：
		本质就是多个线程操作一个资源造成的多线程并发安全问题，加锁可以保证隔离性，但会造成数据库性能下降
		
		两个事务并发修改：必须隔离
		两个事务并发查询：不用隔离
	
		一个修改，一个查询：
	
		  脏读：一个事务读取到另一个事务未提交的数据
		   -------
		   a  1000
		   b  1000
		   -------
			a: 
			start transaction;
			update account set money = money-100 where name ='a';
			update account set money = money+100 where name ='b';
			------
				b:
				start transaction;
				select * from account;
					------
					a 900
					b 1100
					-------
				commit;
			a:
			rollback;
		 
		  不可重复读：一个事务多次读取同一条记录，读取的结果不相同(一个事务读取到另一个事务已经提交的数据)
		   -----------------
		   a  1000 1000 1000
		   -----------------
		   b:
			start transaction;
			select 活期 from account where name ='a';
			select 定期 from account where name ='a';
			select 固定 from account where name ='a';
			-------
			a：
				start transaction;
				update account set 活期=活期-1000 where name='a';
		   		comit;
			------- 
		   select 活期+定期+固定 from account where name = 'a';
		   commit;
	
		  虚读(幻读)问题:一个事务多次查询整表达数据，由于其他事务的update，导致多次查询记录条数不同	
	四大隔离级别  不同的隔离级别防止不同的问题
	    read uncommitted 不做隔离，具有脏读，不可重复读，虚读问题
	    read committed 可以防止脏读 ，不能防止不可重复读，虚读问题
	    repeatable read 可以防止脏读，不可重复读，不能防止虚读问题
	    serializable 数据库运行在串行化未实现，所有都没问题，但是性能比较低 
	
	set [] transaction isolation level ...;
	select @@tx_isolation;	查询当前数据库的隔离级别
	mysql 默认使用 repeatable read

十一、存储过程

十二、视图 (其实是虚拟的表，为了更直观的查看数据，操作视图实质是操作数据表)
	1.单表上创建视图
		create table student(
		s_id int(3),
		name varchar(20),
		math float,
		chinese float
		);
		insert into student values(1,'tom',80,78);
		insert into student values(2,'jack',80,80);
		insert into student values(3,'lucy',97,95);

		创建表视图包含 数学 语文  数学+语文
		create (or replace(对已创建表达替换)) view view_stu  as select math,chinese,math+chinese from student;
		select * from view_stu;
	
		create view view_stu2 (数学，语文，总分) as select math,chinese,math+chinese from student;
		select * from view_stu;
	
	2.多表创建视图
		create table student(
		s_id int(3),
		name varchar(20),
		math float,
		chinese float
		);
		insert into student values(1,'tom',80,78);
		insert into student values(2,'jack',80,80);
		insert into student values(3,'lucy',97,95);
	
		create table stu_info(
		s_id int(3),
		class varchar(20),
		addr varchar(20)
		);
		insert into stu_info values(1,'二班','安徽');
		insert into stu_info values(2,'三班','重庆');
		insert into stu_info values(3,'一班','山东');
	
		创建表视图包含 数学 语文  数学+语文
		create (or replace(对已创建表达替换)) view view_stu  as select math,chinese,math+chinese from student;
		select * from view_stu;
	
		create view view_stu2 (数学，语文，总分) as select math,chinese,math+chinese from student;
		select * from view_stu; 
	
		创建视图包含 编号 学生的姓名 和班级
		create view stu_class (编号，姓名，班级) as
		select student.s_id,student.name,stu_info.class from student,stu_info where student.s_id=stu_info.s_id;
	
	3.查看视图
		desc(describe) 表名;	查看表结构
		
		show table status like '(视图名)' ;
	
		show create view 视图名;
	
	4.修改视图
		create or replace view 视图名 as 查询语句; 创建修改
	
		alter view 视图名 as 查询语句


​	
	5.更新视图
		update 视图名 set chinees=100;
	
		insert into student values(4,'lili',50,40);
	
		delete from viw_stu where chinese =30;
	
	6.删除视图
		drop view view_name

十三、数据库的高级操作
	备份数据库 
		cmd下	mysqldump -u root -p 要备份的数据库名 > c:/1.sql 末尾不能加封号
	恢复数据库  只能恢复数据库中的表，要先创建数据库
		cmd下	mysql -u root -p 要恢复的数据库名 < c:/1.sql	末尾不能加封号
		mysql下	创建数据库，进入数据库 source c:/1.sql;	  1.sql文件中的数据库名要和新创建的数据库名一致
	创建用户
		(1)grant select on mydb.* to 'huochewang(账号)'@'localhost' identified by 'abc123(密码)';

		(2)create user 'username'@'hostname' identified by 'password';
		
		(3)insert into mysql.user(host,user,password,ssl_cipher,x509_issuer,x509_subject)
		values ('localhost','huochewang3',password('abc123'),'','','');
		flush privileges; 刷新权限表
	
	删除用户
		drop user 'huochewang'@'localhost';
	
		delete from mysql.user where host='local' and user='huochewang2';
		flush privileges; 刷新权限表
	
	修改用户密码
		(1)修改root用户的密码
			mysqladmin -u root  -p password newroot(新密码)
			在C:\documents and settings\当前windows用户的目录运行语句
	
			update mysql.user set password=password('abc') where user='root' and host='localhost';
			flush privileges; 刷新权限表
	
			用root用户本身登录
			set password=password('123');
		(2)使用root用户修改普通用户密码
			grant usafe on *.* to 'username'@'localhost' identified by '123';
			
			update mysql.user set password=password('abc') where user='username' and host='hostname';
			flush privileges; 刷新权限表
	
			set password for 'username'@'hostname'=password('123');
		(3)普通用户修改自己的密码
			普通用户登录到数据库
			set password=password('123');
	授予权限
		用grant语句创建一个新用户，user4 123 有insert select 权限并使用 with grant option
		grant insert,select on *.* to 'user4'@'localhost' identified by '123' with grant option;
	
	查看权限
		show grants for 'root'@'localhost';
			
	收回权限
		收回user4的insert权限
		revoke insert on *.* from 'user'@'localhost';
	
		收回user4的所有权限
		revoke all privileges, grant option from 'user4'@'localhost';