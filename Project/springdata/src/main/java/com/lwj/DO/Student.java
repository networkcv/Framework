package com.lwj.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * create by lwj on 2020/4/8
 */

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Student {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private Integer age;


    public Student(String name, Integer age) {
        this.name = name;
        this.age = age;
    }
}
