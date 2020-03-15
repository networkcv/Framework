package com.zyt.ytcollege.dao.DO;

import lombok.Data;

/**
 * create by lwj on 2020/3/15
 */
@Data
public class ApplyDO {
    private Integer id;
    private Integer type;          //报名类型 0-体验报名 1-缴费报名
    private Integer paymentId;     //支付明细id
    private Integer paymentState;  //支付状态  1-未支付 2-已支付 3-已退款
    private Integer studentId;     //学生id
    private String studentName;    //学生姓名
    private Integer studentAge;     //学生年龄
    private String studentPhone;   //学生联系方式
    private Integer subjectId;     //课程id
    private String subjectName;    //课程名称
    private Integer subjectLevel;  //课程阶段
    private Double subjectCost;    //课程价目
    private Double realCost;       //实付金额
    private String date;           //报名时间
    private Integer isFirst;       //是否续保 0-否 1-是
    private Integer referrer;      //介绍人
    private Integer state;         //处理状态 0-待处理 1-已处理
}
