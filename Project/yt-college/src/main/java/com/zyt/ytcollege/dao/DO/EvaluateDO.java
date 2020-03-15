package com.zyt.ytcollege.dao.DO;

import lombok.Data;

/**
 * create by lwj on 2020/3/14
 */
@Data
public class EvaluateDO {
    private int id;
    private int studentId;
    private String studentName;
    private int teacherId;
    private String teacherName;
    private int subjectId;
    private String subjectName;
    private int classesId;
    private String classesName;
    private int type;       //评价类型 0-学生对老师的评价 1-老师对学生的评价
    private int value;      //评价分值
    private String remark;  //备注
}
