package com.zyt.ytcollege.dao.DO;

import lombok.Data;

/**
 * create by lwj on 2020/3/14
 */
@Data
public class SubjectDO {
    private Integer id;
    private String name;
    private Integer hours;          //总课时
    private Integer subjectLevel;   //课程阶段
    private Integer state;          //课程状态 0-上线 1-下线 2-待发布
    private Double cost;        //课程费用
}
