package com.zyt.ytcollege.dao.DO;

import lombok.Data;

/**
 * create by lwj on 2020/3/14
 */
@Data
public class ClassesDO {
    private int id;
    private int subjectId;      //课程id
    private int teacherId;      //教师id
    private int classroomId;    //教室id
    private int totalHours;     //总课时
    private int finishHours;    //已完成课时
    private String startTime;   //开始时间
    private String endTime;     //结束时间
    private String startDate;   //开始日期
    private String endDate;     //结束日期
    private int state;          //班级状态 0-进行中 1-已结束 2-已中断
}
