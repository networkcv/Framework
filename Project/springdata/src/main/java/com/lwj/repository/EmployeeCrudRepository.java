package com.lwj.repository;

import com.lwj.DO.Employee;
import org.springframework.data.repository.CrudRepository;

/**
 * create by lwj on 2020/4/9
 */
public interface EmployeeCrudRepository extends CrudRepository<Employee, Integer> {

}
