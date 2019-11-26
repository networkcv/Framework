package com.lwj.seckill.dao;

import com.lwj.seckill.pojo.Seckill;

import java.util.Date;
import java.util.List;

/**
 * create by lwj on 2019/11/26
 */
public interface SeckillDao {
    //减库存
    int  reduceNumer(long seckillId, Date killTime);
    //根据id查秒杀对象
    Seckill queryById(long seckillId);
    //根据偏移量查询秒杀商品列表
    List<Seckill> queryAll(int offet,int limit);
}
