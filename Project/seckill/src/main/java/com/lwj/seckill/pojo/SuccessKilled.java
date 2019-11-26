package com.lwj.seckill.pojo;

import lombok.Data;

import java.util.Date;

/**
 * create by lwj on 2019/11/26
 */
@Data
public class SuccessKilled {
    private long seckillId;
    private long userPhone;
    private short state;
    private Date createTime;

    //多对一
    private Seckill seckill;
}
