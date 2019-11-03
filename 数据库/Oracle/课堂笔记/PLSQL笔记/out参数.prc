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
/
