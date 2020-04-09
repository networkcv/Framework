package com.lwj.repository;

import com.lwj.DO.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * create by lwj on 2020/4/9
 */
public interface EmployeeJpaRepository extends JpaRepository<Employee, Integer> {

}
