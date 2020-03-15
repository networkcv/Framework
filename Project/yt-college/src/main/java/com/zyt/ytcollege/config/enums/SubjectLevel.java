package com.zyt.ytcollege.config.enums;

/**
 * create by lwj on 2020/3/14
 * 课程阶段
 */
public enum SubjectLevel {

    EV3_1(1, "EV3初级"), EV3_2(2, "EV3中级"), EV3_3(3, "EV3高级"),
    SCRATCH_1(4, "SCRATCH初级"), SCRATCH_2(5, "SCRATCH中级"), SCRATCH_3(6, "SCRATCH高级"),
    C_1(7, "C++初级"), C_2(8, "C++中级"), C_3(9, "C++高级");
    public final int level;
    public final String name;

    SubjectLevel(int level, String name) {
        this.level = level;
        this.name = name;
    }
}
