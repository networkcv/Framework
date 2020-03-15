package com.zyt.ytcollege.dao.DO;

import lombok.Data;

/**
 * create by lwj on 2020/3/14
 */
@Data
public class StudentDO {
    private Integer id;
    private String name;
    private Integer sex;
    private Integer age;
    private String phone;
    private String phone2;
    private Integer subjectId;  //当前学习课程id
    private String subjectName; //当前学习课程名称
    private Integer score;      //积分
    private Integer referrer;   //介绍人
}
