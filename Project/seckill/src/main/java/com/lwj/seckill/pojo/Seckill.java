package com.lwj.seckill.pojo;

import lombok.Data;

import java.util.Date;

/**
 * create by lwj on 2019/11/26
 */
@Data
public class Seckill {
    private long seckillId;
    private String name;
    private int number;
    private Date startTime;
    private Date endTime;
    private Date crateTime;
}
