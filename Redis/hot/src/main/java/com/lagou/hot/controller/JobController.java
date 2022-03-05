package com.lagou.hot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.cache.LoadingCache;
import com.lagou.hot.cache.GuavaService;
import com.lagou.hot.cache.RedisService;
import com.lagou.hot.po.TJob;
import com.lagou.hot.service.JobService;
import com.lagou.hot.util.CommUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@EnableAutoConfiguration
@RequestMapping(value = "/job")
public class JobController {

    @Autowired
    private RedisService rs;


    @RequestMapping(value = "getredis")
    public List<TJob> getJobs() throws Exception {
       return rs.getHotList();
    }


}
