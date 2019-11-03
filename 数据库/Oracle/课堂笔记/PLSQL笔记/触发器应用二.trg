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
