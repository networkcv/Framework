package com.lagou.hot.cache;


import com.alibaba.fastjson.JSON;
import com.lagou.hot.po.TJob;
import com.lagou.hot.service.JobService;
import com.lagou.hot.util.CommUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private JobService jobService;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public List<TJob> getHotList(){
        //返回的数据
        List<TJob> dlist = new ArrayList<>();

        //读缓存
        List<Object> clist = redisTemplate.opsForList().range("HOTLIST", 0, -1);

        //缓存里没有
            if (clist == null || clist.size() == 0) {
            //读源并设置到缓存中
            dlist = jobService.selectAll();
            for (TJob job : dlist) {
                System.out.println(JSON.toJSONString(job));
                redisTemplate.opsForList().leftPush("HOTLIST", JSON.toJSONString(job));

            }
            //超时5秒
            redisTemplate.expire("HOTLIST",5, TimeUnit.SECONDS);


        }
        //返回缓存中的数据
        else {

            for (Object o : CommUtils.jsonToObj(clist, TJob.class)) {
                dlist.add((TJob) o);
            }

        }
        return dlist;
    }


}
