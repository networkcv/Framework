package com.lwj.seckill.mapper;

import com.lwj.seckill.pojo.Seckill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Scope;

import java.util.Date;
import java.util.List;

/**
 * create by lwj on 2019/11/26
 */
@Mapper
public interface SeckillDao {
    //减库存
    int  reduceNumber(long seckillId, Date killTime);
    //根据id查秒杀对象
    Seckill queryById(long seckillId);
    //根据偏移量查询秒杀商品列表
    List<Seckill> queryAll(int offset,int limit);

    @Select("select * from seckill limit 0,1")
    Seckill test();
}
