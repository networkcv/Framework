package com.lwj.seckill.cache;

/**
 * create by lwj on 2019/11/28
 */

public interface RedisDao<T> {
    boolean set(final String key, Object value);

    boolean set(final String key, Object value, Long expireTime);

    boolean existsKey(final String key);

    <T> T getValue(final String key, Class<T> type);

    void removeKey(final String key);

    void remove(final String... keys);

    void removePattern(final String pattern);

    <T> T getValue(final String key);
}

