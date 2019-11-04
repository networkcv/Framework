rem PL/SQL Developer Test Script

set feedback off
set autoprint off

rem Execute PL/SQL Block
-- 打印1~10
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
/
