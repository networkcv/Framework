package com.lwj.seckill.service.Impl;

import com.lwj.seckill.cache.RedisDao;
import com.lwj.seckill.dto.Exposer;
import com.lwj.seckill.dto.SeckillExecution;
import com.lwj.seckill.enums.SeckillStatEnum;
import com.lwj.seckill.exception.RepeatKillException;
import com.lwj.seckill.exception.SeckillCloseException;
import com.lwj.seckill.exception.SeckillException;
import com.lwj.seckill.mapper.SeckillDao;
import com.lwj.seckill.mapper.SuccessKilledDao;
import com.lwj.seckill.pojo.Seckill;
import com.lwj.seckill.pojo.SuccessKilled;
import com.lwj.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * create by lwj on 2019/11/27
 */
@Service
@Slf4j
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private RedisDao<Seckill> redisDao;
    @Autowired
    private SuccessKilledDao successKilledDao;

    private final String salt = "dasfdfij1o34j21o4rv;dc,z()U2r12nr";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //优化点：缓存优化
        //1.访问redis
        Seckill seckill = redisDao.getValue(seckillId+"");
        if (seckill == null) {
            //2.访问数据库
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            }
            //3.更新redis
            redisDao.set(seckill.getSeckillId()+"",seckill);
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date currentTime = new Date();
        if (currentTime.getTime() < startTime.getTime()
                || currentTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, currentTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    @Override
    @Transactional
    /**
     * 使用注解控制事务方法的优点：
     * 1：开发团队达成一致性约定，明确标注事务方法的编程风格
     * 2：保证事务方法的执行时间尽可能短，不要穿插其他网络操作，RPC/HTTP请求或者剥离到事务方法外部
     * 3：不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        Date date = new Date();
        try {
            //执行秒杀逻辑
            int updateCount = seckillDao.reduceNumber(seckillId, date);
            if (updateCount <= 0) {
                throw new SeckillCloseException("seckill is closed");
            } else {
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                if (insertCount <= 0) {
                    throw new RepeatKillException("seckill repeated");
                } else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            //所有编译期异常转换为运行期异常，Spring只会对运行期异常进行回滚操作
            log.error(e.getMessage(), e);
            throw new SeckillException("seckill inner error" + e.getMessage());
        }
    }

    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            return new SeckillExecution(seckillId, SeckillStatEnum.DATE_REWRITE);
        }
        Date killTime = new Date();
        Map<String, Object> map = new HashMap<>();
        map.put("seckillId", seckillId);
        map.put("phone", userPhone);
        map.put("killTime", killTime);
        map.put("result", null);
        // 执行储存过程,result被复制
        seckillDao.killByProcedure(map);
        // 获取result
        int result = MapUtils.getInteger(map, "result", -2);
        if (result == 1) {
            SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
            return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
        } else {
            return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(result));
        }
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + salt;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
