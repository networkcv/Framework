package com.lwj.repository;

import com.lwj.DO.Employee;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * create by lwj on 2020/4/9
 */
public interface EmployeePagingAndSortingRepository extends PagingAndSortingRepository<Employee, Integer> {
}
