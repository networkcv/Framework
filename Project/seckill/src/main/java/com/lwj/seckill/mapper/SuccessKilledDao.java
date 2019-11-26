package com.lwj.seckill.mapper;

import com.lwj.seckill.pojo.SuccessKilled;
import org.apache.ibatis.annotations.Mapper;

/**
 * create by lwj on 2019/11/26
 */
public interface SuccessKilledDao {
    //插入购买明细，可过滤重复
    int intsertSuccessKilled(long seckillId,long userPhone);

    //根据id查询SuccessKilled 并携带秒杀对象实体
    SuccessKilled queryByIdWithSeckill(long seckillId);
}
