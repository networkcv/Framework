package com.lwj.DO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

/**
 * create by lwj on 2020/4/8
 * 雇员 先开发实体类，再自动生成数据表
 */
@Entity
@Data
@ToString
@NoArgsConstructor
@Table(name = "tmp_employee")
public class Employee {
    @Id
    @GeneratedValue
    private Integer employeeId;
    @Column(length = 30)
    private String name;
    @Column(nullable = false)
    private Integer age;

    public Employee(String name,Integer age) {
        this.name=name;
        this.age=age;
    }

}
