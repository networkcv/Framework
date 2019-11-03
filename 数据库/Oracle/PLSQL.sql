--引用型变量: 查询并打印7839的姓名和薪水
    declare
      --定义变量保存姓名和薪水
      --pename varchar2(20);
      --psal   number;
      pename emp.ename%type;
      psal   emp.sal%type;
    begin
      --得到7839的姓名和薪水
      select ename,sal into pename,psal from emp where empno=7839;
      --打印
      dbms_output.put_line(pename||'的薪水是'||psal);
    end;


--记录型变量: 查询并打印7839的姓名和薪水
    declare
      --定义记录型变量：代表一行
      emp_rec emp%rowtype;
    begin
      select * into emp_rec from emp where empno=7839;
      dbms_output.put_line(emp_rec.ename||'的薪水是'||emp_rec.sal);
    end;

--接受键盘输入，if判断
    -- 判断用户从键盘输入的数字
    --变量num：是一个地址值，在该地址上保存了输入的值
    set serveroutput on
    accept num prompt '请输入一个数字';
    declare 
      --定义变量保存输入 的数字
      pnum number := &num;
    begin
      if pnum = 0 then dbms_output.put_line('您输入的是0');
        elsif pnum = 1 then dbms_output.put_line('您输入的是1');
        elsif pnum = 2 then dbms_output.put_line('您输入的是2');
        else dbms_output.put_line('其他数字');
      end if;
    end;    


--循环打印1~10
    declare 
      -- 定义变量
      pnum number := 1;
    begin
      loop
        --退休条件
        exit when pnum > 10;
        --打印
        dbms_output.put_line(pnum);
        --加一
        pnum := pnum + 1;
      end loop;
    end;

--光标
    -- 查询并打印员工的姓名和薪水 
    /*光标的属性： %isopen   %rowcount(影响的行数)
                %found    %notfound             */
    declare 
      --定义光标（游标）
      cursor cemp is select ename,sal from emp;
      pename emp.ename%type;
      psal   emp.sal%type;
    begin
      --打开
      open cemp;
      loop
          --取当前记录
          fetch cemp into pename,psal;
          --exit when 没有取到记录;
          exit when cemp%notfound;
          dbms_output.put_line(pename||'的薪水是'||psal);
      end loop;
      --关闭
      close cemp;
    end;   


-- 给员工涨工资，总裁1000 经理800 其他400
    declare 
      --定义光标
      cursor cemp is select empno,job from emp;
      pempno emp.empno%type;
      pjob   emp.job%type;
    begin
      rollback;  
      --打开光标
      open cemp;  
      loop
          --取一个员工
          fetch cemp into pempno,pjob;
          exit when cemp%notfound;
          --判断职位
          if pjob = 'PRESIDENT' then update emp set sal=sal+1000 where empno=pempno;
              elsif pjob = 'MANAGER' then update emp set sal=sal+800 where empno=pempno;
              else update emp set sal=sal+400 where empno=pempno;
          end if;
      end loop;
      --关闭光标
      close cemp;
      --提交  ----> why?: 事务 ACID
      commit;
      dbms_output.put_line('完成');
    end;    

--光标带参数
    -- 查询某个部门的员工姓名
    declare 
      --形参
      cursor cemp(dno number) is select ename from emp where deptno=dno;
      pename emp.ename%type;
    begin
      --实参
      open cemp(20);
      loop
            fetch cemp into pename;
            exit when cemp%notfound;
            dbms_output.put_line(pename);
      end loop;
      close cemp;
    end;    

  
--自定义例外
    -- 查询50号部门的员工
    declare 
      cursor cemp  is select ename from emp where deptno=50;
      pename emp.ename%type;
      no_emp_found exception;
    begin
      open cemp;
      --取第一条记录
      fetch cemp into pename;
      if cemp%notfound then
        --抛出例外
        raise no_emp_found;
      end if;
      --进程：pmon进程(proccesss monitor)
      close cemp;
    exception
      when no_emp_found then dbms_output.put_line('没有找到员工');
      when others then dbms_output.put_line('其他例外');
    end;    

--统计每年入职的人数
    /*
    1、SQL语句
    select to_char(hiredate,'yyyy') from emp;
    ---> 集合 ---> 光标 ---> 循环---> 退出: notfound

    2、变量：（*）初始值  （*）最终如何得到
    每年入职的人数
    count80 number := 0;
    count81 number := 0;
    count82 number := 0;
    count87 number := 0;
    */
    declare 
      --定义光标
      cursor cemp is select to_char(hiredate,'yyyy') from emp;
      phiredate varchar2(4);
      
      --每年入职的人数
      count80 number := 0;
      count81 number := 0;
      count82 number := 0;
      count87 number := 0;
    begin
      --打开光标
      open cemp;
      loop
        --取一个员工的入职年份
        fetch cemp into phiredate;
        exit when cemp%notfound;
        
        --判断年份是哪一年
        if phiredate = '1980' then count80:=count80+1;
          elsif phiredate = '1981' then count81:=count81+1;
          elsif phiredate = '1982' then count82:=count82+1;
          else count87:=count87+1;
        end if;
      end loop;
      
      --关闭光标
      close cemp;
      
      --输出
      dbms_output.put_line('Total:'||(count80+count81+count82+count87));
      dbms_output.put_line('1980:'|| count80);
      dbms_output.put_line('1981:'|| count81);
      dbms_output.put_line('1982:'|| count82);
      dbms_output.put_line('1987:'|| count87);
    end;    

--每个人涨10%的工资，但所有人每月工资总和不能超过5万
    /*
    1、SQL语句
    selet empno,sal from emp order by sal;
    ---> 光标  ---> 循环  ---> 退出：1. 总额>5w   2. notfound

    2、变量：（*）初始值  （*）最终如何得到
    涨工资的人数: countEmp number := 0;
    涨后的工资总额：salTotal number;
    (1)select sum(sal) into salTotal from emp;
    (2)涨后=涨前 + sal *0.1

    练习： 人数：8    总额:50205.325
    */
    declare
        cursor cemp is select empno,sal from emp order by sal;
        pempno emp.empno%type;
        psal   emp.sal%type;
        
        --涨工资的人数: 
        countEmp number := 0;
        --涨后的工资总额：
        salTotal number;
    begin
        --得到工资总额的初始值
        select sum(sal) into salTotal from emp;
        
        open cemp;
        loop
            -- 1. 总额 >5w
            exit when salTotal > 50000;
            --取一个员工
            fetch cemp into pempno,psal;
            --2. notfound
            exit when cemp%notfound;
            
            --涨工资
            update emp set sal=sal*1.1 where empno=pempno;
            --人数+1
            countEmp := countEmp +1;
            --涨后=涨前 + sal *0.1
            salTotal := salTotal + psal * 0.1;

        end loop;
        close cemp;
        
        commit;
        dbms_output.put_line('人数：'||countEmp||'    总额:'||salTotal);
    end;