package com.zyt.ytcollege.dao.DO;

import lombok.Data;

/**
 * create by lwj on 2020/3/14
 */
@Data
public class TeacherDO {
    private int id;
    private String name;
    private int sex;    //0-女 1-男
    private int age;
    private String phone;
    private String password;    //登录密码
    private int post;   //岗级 enum YTPost
    private int level;  //职级 enum YTLevel
    private int totalHours; //当前学期总课时
    private int numbers;    //当前学期学生总数
    private double score;  //教学质量评分
    private double renewalRate; //续保率
    private int isDelete;   //是否在职 0-在职 1-离职
}
