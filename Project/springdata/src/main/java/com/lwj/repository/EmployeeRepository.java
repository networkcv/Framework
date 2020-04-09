package com.lwj.repository;

import com.lwj.DO.Employee;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * create by lwj on 2020/4/9
 */
@RepositoryDefinition(domainClass = Employee.class, idClass = Integer.class)
//public interface EmployeeRepository extends Repository<Employee, Integer> {
public interface EmployeeRepository {
    Employee findByName(String name);

    // where name like ?% and age <= ?
    List<Employee> findByNameStartingWithAndAgeLessThanEqual(String name, Integer integer);

    // where name like %? and age > ?
    List<Employee> findByNameEndingWithAndAgeGreaterThan(String name, Integer integer);

    // where name in (?,?...)
    List<Employee> findByNameIn(List<String> names);

    // 注意这里的 Employee 是类名，而不是表名
    @Query("select o from Employee o where o.employeeId =(select max(o.employeeId) from Employee)")
    Employee getEmployeeByMaxId();

    @Query(value = "select o from Employee  o where o.age=?1 and o.name=?2 ")
    Employee myQueryEmployeeByNameAndAge(Integer age, String name);

    @Query(value = "select o from Employee  o where o.name=:name and o.age=:age")
    Employee myQueryEmployeeByNameAndAge2(@Param("name") String name, @Param("age") Integer age);

    @Query(nativeQuery = true, value = "select * from employee where age > :age ")
    List<Employee> myQueryNative(@Param("age") Integer age);

    @Modifying
    @Transactional
    @Query("update Employee set age=:age where employeeId =:id")
    void updateById(@Param("id") Integer id, @Param("age") Integer age);
}
