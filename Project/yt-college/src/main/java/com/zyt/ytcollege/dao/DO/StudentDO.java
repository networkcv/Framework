package com.zyt.ytcollege.dao.DO;

import lombok.Data;

/**
 * create by lwj on 2020/3/14
 */
@Data
public class StudentDO {
    private int id;
    private String name;
    private int sex;
    private int age;
    private String phone;
    private String phone2;
    private int subjectId;
    private int score;
    private int referrer;
}
