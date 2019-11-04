常用的Oracle的数据库对象介绍：表、视图、序列、索引、同义词、约束、存储过程、存储函数、包和包体、触发器

视图    view
    create or replace view empinfoview
    as
        select e.empno,e.ename,e.sal,e.sal*12 annsal,d.dname
        from emp e,dept d
        where e.deptno=d.deptno;
        with read only;
    ORA-01031: 权限不足    grant create view to scott  授权创建视图

    desc empinfoview
    名称                                      是否为空? 类型
    ----------------------------------------- -------- ----------------------------
    EMPNO                                     NOT NULL NUMBER(4)
    ENAME                                              VARCHAR2(10)
    SAL                                                NUMBER(7,2)
    ANNSAL                                             NUMBER
    DNAME                                              VARCHAR2(14)

    select * from empinfoview;

    EMPNO ENAME             SAL     ANNSAL DNAME                               
    ------ ---------- ---------- ---------- --------------                      
    7369 SMITH             800       9600 RESEARCH                            
    7499 ALLEN            1600      19200 SALES                               
    7521 WARD             1250      15000 SALES                               



序列    sequence
    create sequence myseq;

    create table testseq (tid number,tname varchar2(20));

    select myseq.nextval from dual;    序列的下个一值

    NEXTVAL                                                                      
    ----------                                                                      
            1                                                                      

    select myseq.currval from dual;    序列的当前值

    CURRVAL                                                                      
    ----------                                                                      
            1                                                                      

    insert into  testseq values(myseq.nextval,'aaa');
    insert into  testseq values(myseq.nextval,'aaa');
    insert into  testseq values(myseq.nextval,'aaa');
    insert into  testseq values(myseq.nextval,'aaa');

    select * from testseq;

        TID TNAME                                                                
    ---------- --------------------                                                 
            2 aaa                                                                  
            3 aaa                                                                  
            4 aaa                                                                  
            5 aaa                                                                  


索引    index
    SQL的执行计划
    explain plan for select * from emp where deptno=10;
    select * from table(dbms_xplan.display);

    --------------------------------------------------------------------------      
    | Id  | Operation         | Name | Rows  | Bytes | Cost (%CPU)| Time     |      
    --------------------------------------------------------------------------      
    |   0 | SELECT STATEMENT  |      |     3 |   261 |     3   (0)| 00:00:01 |      
    |*  1 |  TABLE ACCESS FULL| EMP  |     3 |   261 |     3   (0)| 00:00:01 |      
    --------------------------------------------------------------------------      
                                                                                    

    创建目录（索引）
    create index myindex on emp(deptno);

    explain plan for select * from emp where deptno=10;
    select * from table(dbms_xplan.display);
                                                                                    
    | Id  | Operation                   | Name    | Rows  | Bytes | Cost (%CPU)| Time     |                                                                         
    --------------------------------------------------------------------------------
    |   0 | SELECT STATEMENT            |         |     3 |   261 |     2   (0)| 00:00:01 |                                                                         
    |   1 |  TABLE ACCESS BY INDEX ROWID| EMP     |     3 |   261 |     2   (0)| 00:00:01 |                                                                         
    |*  2 |   INDEX RANGE SCAN          | MYINDEX |     3 |       |     1   (0)| 00:00:01 |                                                                         


同义词(别名)    synonym
    show user
    USER 为 "SCOTT"

    select count(*) from hr.employees;

    为hr.employees起别名  ---> 同义词

    create synonym hremp for hr.employees;

    select count(*) from hremp;

    COUNT(*)                                                                                                                                                                                              
    ----------                                                                                                                                                                                              
        107                                                                                                                                                                                              


约束    constraint
    create table student(
        sid number constraint student_pk primary key,
        sname varchar2(20) constraint student_name_notnull not null,
        gender varchar2(2) constraint student_gender check (gender in ('男','女')),
        email varchar2(40) constraint student_email_unique unique
                        constraint student_email_notnull not null,
    deptno number constraint student_fk references dept(deptno) on delete set null
   );

    check   检查
    create table test3(
        tid number,
        tname varchar2(20),
        gender varchar2(2) check (gender in ('男','女')),
        sal  number check (sal > 0)
    );

存储过程    procedure
    --打印Hello World
    --创建存储过程
        create or replace procedure sayhelloworld
        as
        --说明部分
        begin
        dbms_output.put_line('Hello World');
        end;

    --调用存储过程：
        1、exec sayhelloworld();
        2、begin
            sayhelloworld();
            sayhelloworld();
        end;
        /

    --给指定的员工涨100，并且打印涨前和涨后的薪水
        create or replace procedure raiseSalary(eno in number)
        is
            --定义变量保存涨前的薪水
            psal emp.sal%type;
        begin
            --得到涨前的薪水
            select sal into psal from emp where empno=eno;
            
            --涨100
            update emp set sal=sal+100 where empno=eno;
            
            --这里不用commit 谁调用谁提交
            
            dbms_output.put_line('涨前:'||psal||'   涨后：'||(psal+100));
        end raiseSalary;
        /

    --查询某个员工的姓名 薪水和职位
        /*
        1、查询某个员工的所有信息 ---> out参数太多
        2、查询某个部门中的所有员工信息 ----> 返回的是集合
        */

        create or replace procedure queryEmpInformation(eno in number,
                                                        pename out varchar2,
                                                        psal   out number,
                                                        pjob   out varchar2)
        is
        begin
        
        select ename,sal,job into pename,psal,pjob from emp where empno=eno;                                             

        end queryEmpInformation;

存储函数    function
    --查询某个员工的年收入
        create or replace function queryEmpIncome(eno in number) 
        return number
        is
            --定义变量保存月薪和奖金
            psal emp.sal%type;
            pcomm emp.comm%type;
        begin
            --得到月薪和奖金
            select sal,comm into psal,pcomm from emp where empno=eno; 
            
            --返回年收入
            return psal*12+nvl(pcomm,0);

        end queryEmpIncome;       

包和包体    package and package body
    --2、查询某个部门中的所有员工信息 ----> 返回的是集合
        create or replace package mypackage is

            type empcursor is ref cursor;
            procedure queryEmpList(dno in number,empList out empcursor);

        end mypackage;
        
        create or replace package body mypackage is

            procedure queryEmpList(dno in number,empList out empcursor)
            as
            begin
                
                open empList for select * from emp where deptno=dno;
            
            end;

        end mypackage;

触发器      trigger
    每当成功插入新员工后，自动打印“成功插入新员工”

    create trigger firsttrigger
    after insert
    on emp
    declare
    begin
    dbms_output.put_line('成功插入新员工');
    end;
    /

    强制审计
    标准审计（配置）
    基于值的审计
    细粒度审计
    管理员审计


    /*
    实施复杂的安全性检查
    禁止在非工作时间 插入新员工

    1、周末:  to_char(sysdate,'day') in ('星期六','星期日')
    2、上班前 下班后：to_number(to_char(sysdate,'hh24')) not between 9 and 17
    */
    create or replace trigger securityemp
    before insert
    on emp
    begin
    if to_char(sysdate,'day') in ('星期六','星期日','星期五') or 
        to_number(to_char(sysdate,'hh24')) not between 9 and 17 then
        --禁止insert
        raise_application_error(-20001,'禁止在非工作时间插入新员工');
    end if;
    
    end securityemp;
    /

    /*
    数据的确认
    涨后的薪水不能少于涨前的薪水
    */
    create or replace trigger checksalary
    before update
    on emp
    for each row
    begin
        --if 涨后的薪水 < 涨前的薪水 then
        if :new.sal < :old.sal then
        raise_application_error(-20002,'涨后的薪水不能少于涨前的薪水。涨前:'||:old.sal||'   涨后:'||:new.sal);
        end if;
    end checksalary;
    /
