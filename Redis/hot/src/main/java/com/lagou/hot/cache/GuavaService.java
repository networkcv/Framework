package com.lagou.hot.cache;

import com.google.common.cache.*;
import com.google.common.util.concurrent.ListenableFuture;
import com.lagou.hot.dao.JobDao;
import com.lagou.hot.po.TJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Service
public class GuavaService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private RedisService rs;

    @Autowired
    private JobDao jdao;

    /**
     * 初始化只调用一次
     * @return
     */
    public LoadingCache<String, Object> init() {
        LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
                // 最大3个       //同时支持CPU核数线程写缓存
                .maximumSize(3).concurrencyLevel(Runtime.getRuntime().availableProcessors())
                // 本地缓存失效  为了数据的同步
                .expireAfterWrite(5,TimeUnit.SECONDS)
                .recordStats()
                .refreshAfterWrite(3,TimeUnit.SECONDS)
                .build(
                        new CacheLoader<String, Object>() {
            @Override
            public Object load(String s) throws Exception {
                //回调 回源
                return rs.getHotList();
            }
/*
                            @Override
                            public ListenableFuture<Object> reload(String key, Object oldValue) throws Exception {

                                return super.reload(key, oldValue);
                            }

 */
                        }
                );
        return cache;
    }

    /**
     * 缓存失效
     * @param key
     * @param cache
     * @return
     * @throws Exception
     */
    public Object get(String key, LoadingCache cache) throws Exception {

        Object value = cache.get(key, new Callable() {
            @Override
            public Object call() throws Exception {
                //回调 回源
                return rs.getHotList();
            }
        });
        return value;

    }

}
