package com.lwj.repository;

import com.lwj.DO.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * create by lwj on 2020/4/9
 */
public interface EmployeeJpaSpecificationExecutorRepository extends JpaRepository<Employee,Integer> , JpaSpecificationExecutor<Employee> {
}
