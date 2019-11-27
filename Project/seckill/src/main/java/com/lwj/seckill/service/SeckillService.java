package com.lwj.seckill.service;

import com.lwj.seckill.dto.Exposer;
import com.lwj.seckill.dto.SeckillExecution;
import com.lwj.seckill.exception.RepeatKillException;
import com.lwj.seckill.exception.SeckillCloseException;
import com.lwj.seckill.exception.SeckillException;
import com.lwj.seckill.pojo.Seckill;

import java.util.List;

/**
 * 业务接口：站在使用者的角度设计接口
 * 三个方面，方法定义粒度，参数，返回类型(return类型/异常)
 * create by lwj on 2019/11/27
 */
public interface SeckillService {
    //查询所有秒杀记录
    List<Seckill> getSeckillList();

    //查询单个秒杀记录
    Seckill getById(long seckillId);

    //秒杀开启是输出秒杀接口地址，否则输出系统时间和秒杀时间
     Exposer exportSeckillUrl(long seckillId);

     //执行秒杀操作
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
     throws SeckillException, RepeatKillException, SeckillCloseException;

}
