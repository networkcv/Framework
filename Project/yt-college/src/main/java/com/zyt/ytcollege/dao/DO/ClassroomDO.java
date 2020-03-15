package com.zyt.ytcollege.dao.DO;

import lombok.Data;

/**
 * create by lwj on 2020/3/15
 */
@Data
public class ClassroomDO {
    private Integer id;
    private String name;        //名称
    private Integer max;        //最大使用人数
    private String address;     //地址
    private String remark;      //备注
    private Integer state;      //开放状态 0-未开放 1-已开放

}
