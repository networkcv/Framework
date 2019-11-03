select * from emp e left join dept d on e.deptno=d.deptno;
select * from emp;
select * from dept;
select * from emp e right join dept d on e.deptno=d.deptno;
select * from emp e inner join dept d on e.deptno=d.deptno;
select * from dept d inner join emp e on d.deptno=e.deptno;
select * from emp e left join dept d on e.deptno=d.deptno where d.deptno is null;
insert into emp(EMPNO,ENAME,JOB) values(7896,'chjg','boss');
insert into emp(empno,ename,job) values(1234,'chjg1','boss1');/*sql语句不区分大小写，值区分大小写*/
update emp e set e.deptno=(99) where e.ename='chjg';
delete from emp e where e.ename='chjg';
select * from emp e right join dept d on e.deptno=d.deptno where e.deptno is null;/*右边的全部，也就是右边的独有加上他的共有*/
select count(empno)from emp;/*统计*/
select *  from tab;/*查看当前用户下的表*/
SELECT * FROM emp WHERE sal BETWEEN 1500 AND 3000;/*日期函数中也可以使用*/
SELECT * FROM emp WHERE ename LIKE '_M%';/*模糊查询，_匹配一个任意的字符，%匹配任意长度的字符*/
SELECT * FROM emp WHERE ename LIKE '%';
SELECT * FROM emp WHERE sal <> 1500;
SELECT * FROM emp WHERE sal !=1500;
SELECT * FROM emp ORDER BY deptno ASC;/*默认正序 ASC ，倒叙 DESC*/
SELECT * FROM emp ORDER BY deptno,sal ASC;/*可以多列排序*/
SELECT 1 FROM dual;/*获取常量，数字，日期用伪表*/
SELECT UPPER('chjg')FROM dual;/*小写变大写*/
SELECT LOWER ('CHJG')FROM dual;/*大写变小写*/
SELECT INITCAP('chjg')FROM dual;/*首字母大写*/
SELECT CONCAT(UPPER('hello'),INITCAP('chjg')) FROM dual;/*字符串的连接*/
SELECT 'hello'||'world' FROM dual;/*函数concat不能跨数据库，双竖线可以，具有兼容性*/
SELECT SUBSTR('helloworldchjg',1,8) FROM dual;/*索引从1开始*/
SELECT SUBSTR('helloworldchjg',0,8) FROM dual;/*索引从1开始*/
SELECT SUBSTR('helloworldchjg',8)FROM dual;/*一个数字的话就是从这个索引开始到结束*/
SELECT LENGTH('helloworldchjg')FROM dual;/*字符串的长度统计*/
SELECT REPLACE('helloworld','l','chjg')FROM dual;/*第一个是源字符串，第二个是被替换的字符串，第三个是替换的字符串,
当有多个同样的被替换的字符串，都会被替换。*/
/*数值函数*/


SELECT ROUND(12.78)FROM dual;/*四舍五入*/
SELECT ROUND(12.78654,2) FROM dual;/*四舍五入，保留两位小数*/
SELECT TRUNC(12.79951)FROM dual;/*直接去掉小数部分*/
SELECT TRUNC(12.7555,2)FROM dual;/*保留小数点后两位，但是不会四舍五入*/
SELECT MOD(10,3)FROM dual;/*取余*/



/*日期函数*/
SELECT  SYSDATE FROM dual;
SELECT TRUNC((SYSDATE - hiredate)/7 )FROM emp;/*sysdate 系统日期。
日期-日期 得到的是天数。*/
SELECT ename,TRUNC(months_between(SYSDATE,hiredate))FROM emp;/* months_between  函数，获取两个时间段内的月数*/
SELECT add_months(SYSDATE,3)FROM dual;/*满12进一位*/
SELECT next_day(SYSDATE,'星期五')FROM dual;/*下一个日期*/
SELECT last_day(SYSDATE)FROM dual;/*本月最后一天*/




/*转换函数*/
SELECT ENAME,
       TO_CHAR(HIREDATE, 'yyyy') 年,
       TO_CHAR(HIREDATE, 'mm') 月,
       TO_CHAR(HIREDATE, 'dd') 日
  FROM EMP;
SELECT hiredate FROM emp;
SELECT to_char(SYSDATE,'yyyy-mm-dd HH24:mi:ss')FROM dual;/*24小时制*/
SELECT to_char(SYSDATE,'yyyy-mm-dd HH:mi:ss')FROM dual;/*普通12小时制*/
SELECT ename, to_char(hiredate,'yyyy-mm-dd')FROM emp;/*格式化转换日期不足10的会有前导0*/
SELECT ename,to_char(hiredate,'fmyyyy-mm-dd')FROM emp;/*添加fm可以解决前导零的问题*/



SELECT ename,sal FROM emp;
SELECT ename,to_char(sal,'999,999')FROM emp;
SELECT ename,to_char(sal,'$999,999')FROM emp;
SELECT ename,to_char(sal,'l999,999')FROM emp;/*l=local 本地的意思*/

/*to_number数字转换*/
SELECT to_number('10')+to_number('10')FROM dual;/*转换的字符必须是数字类型的*/

/*日期转换*/
SELECT to_date('1997-02-10 12:30:56','yyyy-mm-dd HH24:mi:ss')FROM dual;/*常用，重点*/

/*通用函数*/
SELECT ename ,sal,NVL(sal,0)pcomm FROM emp;

SELECT ename ,sal*12+NVL(comm,0) FROM emp;/*nvl 空值取你设置的参数，并且参数必须存在
如果oracle第一个参数为空那么显示第二个参数的值，如果第一个参数的值不为空，则显示第一个参数本来的值。
*/
/*decode函数 类似于if else*/
SELECT DECODE(9,1,'我是1',2,'我是2','其他')FROM dual;/*第一个参数是需要被判断的参数*/
SELECT ENAME,
       DECODE(JOB, 'CLERK', '业务员','SALESMAN','销售','boss')
FROM EMP;


/*case when 和decode功能相同*/
SELECT ename ,(CASE WHEN job='CLERK'THEN '业务员'
                  WHEN job='SALEMAN'THEN '销售'
                  ELSE '无业'
                  END)cjob
 FROM EMP; 


/*多表联合查询*/
----外连接，做连接查询其中一张表要查询全量的数据时(不会因为另外一张表的数据关联而被筛选掉) 外连接分左右连接
----全量表在左边就是左连接，在右边就是右连接
SELECT * FROM dept d,emp e WHERE d.deptno=e.deptno(+);/*外连接，在全量表的对面表添加'+'*/

SELECT E.EMPNO, E.ENAME, E1.EMPNO, E1.ENAME
  FROM EMP E, EMP E1
 WHERE E.MGR= E1.EMPNO(+);

/* join 相当于’，‘逗号  on 相当于where*/
SELECT * FROM emp e JOIN dept d ON e.deptno=d.deptno;


/*分组函数*/

SELECT COUNT(empno) empnum FROM emp;

SELECT ename FROM emp e WHERE e.sal=(SELECT MIN(sal) FROM emp);/*min函数不能直接放在等号的后面，应该作为一个查询的子语句*/
/*max()   同理 */

/*avg()平均函数*/
SELECT ROUND(AVG(sal)) avsal FROM emp;/*四舍五入的函数可以嵌套 平均函数*/ /*取整函数  trunc */
SELECT MOD(SUM(sal),32)FROM emp WHERE emp.deptno=20;/*取余函数嵌套一个求和函数*/


SELECT *  FROM emp ORDER BY deptno;

SELECT COUNT(empno) FROM emp GROUP BY(deptno);
SELECT AVG(sal)abgsal,COUNT(empno) ,deptno FROM emp GROUP BY(deptno);

/*ORACLE中不能识别主键*/
/*分组统计的时候如果除了有分组函数以外，结果列必须是group by 后面的列，也就是说后面有的列前面才能有
后面没有的前面绝对不可以有*/

SELECT COUNT(*),deptno FROM emp GROUP BY deptno;/*group by 分组前面的列的结果集必须在后面的group by 中有定义*/
SELECT COUNT(*) FROM emp;
SELECT COUNT(*), d.deptno ,d.dname FROM emp e,dept d WHERE e.deptno=d.deptno GROUP BY d.dname,d.deptno; /*group by 前面要分组的列后面必须要有定义*/
SELECT COUNT(*) empnum,d.deptno,d.dname FROM emp e,dept d WHERE e.deptno=d.deptno GROUP BY d.dname,d.deptno HAVING COUNT(*)>5;/*按部门分组，并且部门人数大于5的*/


/*查出平均工资大于2000 的部门 */
/*SELECT AVG(sal),d.dname FROM emp e,dept d GROUP BY d.dname HAVING AVG(sal)>2000;*/
/*SELECT AVG(sal),d.dname FROM emp e,dept d WHERE e.deptno=d.deptno GROUP BY d.dname HAVING AVG(sal)>2000;*/
SELECT AVG(sal),e.deptno FROM emp e,dept d WHERE e.deptno=d.deptno GROUP BY e.deptno HAVING AVG(sal)>2000;
SELECT AVG(sal),e.deptno FROM emp e,dept d GROUP BY e.deptno HAVING AVG(sal)>2000;

/*显示非销售人员工作名称以及从事同一工作的员工的月工资的总和，
并且要满足从事同一工作月工资总和大于5000，结果按月工资总和的升序排列。*/
 SELECT e.job,SUM(e.sal) FROM emp e WHERE e.job!='SALESMAN'GROUP BY e.job HAVING SUM(e.sal)>5000 ;
 
/*嵌套子查询*/
SELECT e1.empno,e1.ename FROM emp e1 WHERE e1.sal >(SELECT e.sal FROM emp e WHERE e.empno='7654');/*一个查询语句作为另外一个语句的条件*/

/*查询出比雇员7654的工资高，同时从事和7788的工作一样的员工*/

SELECT * FROM emp e WHERE e.sal>(SELECT e1.sal FROM emp e1 WHERE e1.empno=7654) AND e.job=(SELECT e2.job FROM emp e2 WHERE e2.empno=7788);

/* 要求查询每个部门的最低工资和最低工资的雇员和部门名称 */

/* SELECT * FROM emp e2 WHERE e2.sal=(SELECT MIN(e.sal) ,e.deptno FROM emp e GROUP BY e.deptno) a */
SELECT e.sal, e.ename,d.dname FROM emp e,dept d,(SELECT MIN(e.sal)minsal ,e.deptno FROM emp e GROUP BY e.deptno)a WHERE e.sal=a.minsal AND e.deptno=d.deptno;

/*查询出来所有和每个部门最低工资的员工工资相等得人 */
--in关键字尽量要少使用，因为性能比较低，可以使用exists来代替，他的性能比较高
SELECT * FROM emp e WHERE e.sal IN (SELECT MIN(e1.sal) FROM emp e1 GROUP BY e1.deptno); 

/*exists和not exists*/
/*exists 的子查询数是0 则表示整个表达式是false如果大于0 则为true 一般exists子查询一般要和外侧查询关联查询*/
SELECT * FROM dept d WHERE NOT EXISTS(SELECT * FROM emp e WHERE e.deptno=d.deptno);/*查询出有员工的部门*/
/*union 并集操作，不包括重复行。   去除重复的数据*/
SELECT * FROM emp e WHERE e.sal>1000
UNION
SELECT * FROM emp e1 WHERE e1.sal>2000;

/*union all 不去重，只是将两个并集并起来*/
SELECT * FROM emp e WHERE e.sal>1000
UNION ALL
SELECT * FROM emp e1 WHERE e1.sal>2000;

/*要合并的列的数据类型要必须一致，列名可以不一致，两个合并的列数必须一致*/


/*
insert into 表名（列名，。。。。）values(......);
update 表名 set 表名。属性=。。。。，，，，，where 条件
delete from 表名 where 条件 （from可以省略，，，，，mysql中不可以省略）
*/

CREATE TABLE myemp AS SELECT * FROM emp;

/*事物*/
SELECT * FROM myemp;
---增删改都要开启事物，事物必须提交了数据库中的数据才可以真正的改变。
DELETE FROM myemp m WHERE m.empno=1234;
COMMIT;
INSERT INTO myemp(empno,ename,job ) VALUES(1234,'chjg','boss');
COMMIT;
INSERT INTO myemp(empno,ename,job) VALUES(1235,'chjg1','boss');
COMMIT;

DELETE FROM myemp WHERE empno=1234
ROLLBACK;
/*update修改数据的时候数据会被锁住。*/

/*add 添加列
alter table persion add(address varchar2(50));

modify 修改列
alter table persion modify(address varchar2(5));
*/

/*建表语句*/
CREATE TABLE person(
ID NUMBER(10),
p_name VARCHAR(20),
age NUMBER(3),
birthday DATE,
address VARCHAR(50)
)

SELECT * FROM persion;

ALTER TABLE persion MODIFY(address VARCHAR2(50));

INSERT INTO persion VALUES(1,'chjg',21,to_date('1996-02-10','yyyy-mm-dd'),'xian');

/* 截断表 truncate 没有确认，不能回滚 
delete person ；
truncate table person ；
*/
  /*check key 检查约束

  create table person(
  pid number(4),
  gender number(1)，
  constraint person_check_ck check（gender in（1,2））  /* constraint  后面是键的别名*/
  )
  */

/*

create table order_detail(
       detail_id      number(10) ,
       order_id   number(10),
       item_name  varchar2(10),
       quantity   number(10),
      constraint order_detail_detail_id_pk primary key(detail_id),
      constraint order_detail_order_id_fk foreign key(order_id) references orders(order_id)
);

*/

/*rownum 分页
rownum 不支持大于号
*/

SELECT ROWNUM,t.* FROM emp t WHERE ROWNUM <6;

/*分页步骤
1.查询全量的数据
2.用第一次的结果集作为一张表，限定条件是rownum < 结束行号，结果列要把rownum作为结果集
3.以第二步的结果集作为一张表，限定条件是第二步的rownum列大于开始行号，结果列是*
*/

SELECT * FROM (SELECT ROWNUM rw,a.* FROM (SELECT * FROM emp) a WHERE ROWNUM<11) b WHERE b.rw>6;

/*视图，就是封装一条复杂的语句

create view as 语句

grant connect,resource,dba to scott; //sys用户给scott用户赋予权限
*/
SELECT * FROM emp e WHERE e.deptno=20;
---创建视图
/*创建视图的时候查询的sql不能有重复的列名*/
CREATE VIEW view_20 AS SELECT * FROM emp e WHERE e.deptno=20;

select * from view_20;/*方便查询*/
--创建和覆盖视图
CREATE OR REPLACE VIEW view_d20 AS SELECT * FROM myemp t WHERE t.deptno=20;

SELECT  * FROM emp;
/*修改视图其实是修改对应的表中的数据，所以视图不建议修改*/
UPDATE view_d20 t SET t.ename='史密斯' WHERE t.empno=7369;

SELECT * FROM myemp;

/*创建只读视图*/

CREATE OR REPLACE VIEW view_a20 AS SELECT * FROM myemp e WHERE e.deptno=20 WITH READ ONLY; 

UPDATE view_a20 t SET t.ename='史密斯' WHERE t.empno=7369; 

/*序列
序列并没有绑定任何的表，理论上是可以给任何表使用。
*/
CREATE SEQUENCE seqpersonid;
--下一个值
SELECT seqpersonid.nextval FROM dual;
---当前的值
SELECT seqpersonid.currval FROM dual;
SELECT * FROM person;
INSERT INTO person VALUES(seqpersonid.nextval,'kkkk',23,to_date('1998-02-10','yyyy-mm-dd'),'shanxi');
/*索引，是一种数据结构，可以降低io的次数 
create index 索引名 on 表名（列名）
*/

/*plsql
sql语言的处理分支功能
DECLARE
pname varchar2(10);
BEGIN
  pname :='zhangsan';
  dbms_output.put_line(pname);
EXCEPTION

END;
*/
/*
基本数据类型
*/
DECLARE
pname varchar2(10);
page NUMBER(3):=20;
BEGIN
  pname :='zhangsan';
  dbms_output.put_line(pname);
  dbms_output.put_line(page);
END;

/*引用数据类型*/
DECLARE
pname myemp.ename%TYPE;
BEGIN
  SELECT t.ename INTO pname FROM myemp t WHERE t.empno=7369;
  dbms_output.put_line(pname);
  END;
  /*记录型变量，对应java中的对象的类变量*/
  
  DECLARE
prec myemp%ROWTYPE;
BEGIN
  SELECT * INTO prec FROM myemp t WHERE t.empno=7369;
  dbms_output.put_line(prec.ename ||'    '||prec.sal);
  END;
  
/*
if 分支
  1.
if 条件 then 语句1；
  语句2；
  end if ；
2.
if 条件 then 语句序列1；
  else 语句序列2
    
3.
if 条件 then 语句序列1；
  elsif 语句序列2 then
  else
    语句
    end if；
    end
*/

DECLARE 
pnum NUMBER(4):=&NUM;
BEGIN
  IF pnum<5 THEN 
    dbms_output.put_line('小于5');
    END IF;
    END;
    
    
    
    
DECLARE 
pnum NUMBER(4):=&NUM;
BEGIN
  IF pnum=1 THEN 
    dbms_output.put_line('我是1');
    ELSE
      dbms_output.put_line('我不是1');
      END IF;
    END;
    
    
DECLARE 
pnum NUMBER(4):=&NUM;
BEGIN
  IF pnum=1 THEN 
    dbms_output.put_line('我是1');
    ELSIF pnum=2 THEN 
      dbms_output.put_line('我是2');
      ELSE
        dbms_output.put_line('其他');
      END IF;
    END;
    
    
    /* loop 循环
    declare
     begin 
       while 条件语句 loop
	
end loop;

    
    */
    
    DECLARE
    total NUMBER(4):=0;
    BEGIN
      while  total<100 LOOP
	    total:=total+1;
      dbms_output.put_line(total);
end loop;
END;

/*游标相当于java中的集合
连接字符串一定要是||双竖线

*/
DECLARE
CURSOR c1 IS SELECT * FROM emp;
prec emp%ROWTYPE;
BEGIN
  OPEN c1;
  LOOP
    FETCH c1
    INTO prec;
    EXIT WHEN c1%NOTFOUND;
    dbms_output.put_line(prec.empno||'  '||prec.ename);
    END LOOP;
    CLOSE c1;
    END;
    


/*存储过程   一段sql语句集 procedure     事物的提交要放在调用端来做*/
----可以有多个返回值，使用输出参数
CREATE OR REPLACE  PROCEDURE helloworld AS
BEGIN 
  dbms_output.put_line('hello,world');
  END;

/*测试存储过程 */
BEGIN 
helloworld;
  END;


/*存储函数function 有返回值*/

