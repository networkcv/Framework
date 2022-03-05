package com.lagou.hot.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.cache.LoadingCache;
import com.lagou.hot.cache.GuavaService;
import com.lagou.hot.cache.RedisService;
import com.lagou.hot.po.TJob;
import com.lagou.hot.service.JobService;
import com.lagou.hot.util.CommUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/job")
public class JobController2 {


    @Autowired
    private GuavaService gservice;

    LoadingCache<String, Object> cache=null;

    @Autowired
    private RedisService rs;

    @RequestMapping(value = "init")
    public String init() throws ExecutionException {
        cache = gservice.init();
        cache.get("HOTLIST");
        return "SUC";
    }


    @RequestMapping(value = "getcache")
    public List<TJob> getJobs() throws Exception {


        //返回的数据
        List<TJob> dlist = new ArrayList<>();

        //读取列表
        Object glist = gservice.get("HOTLIST",cache);
    //    Object glist = cache.getIfPresent("HOTLIST");
        //本地缓存有
        if (glist != null&&((List)glist).size()!=0) {
            for (Object o : (List) glist) {
                dlist.add((TJob) o);
            }

        }

        return dlist;

    }


}
