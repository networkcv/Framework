package com.lwj._100_utils;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.TypeReference;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.hutool.core.text.CharSequenceUtil.EMPTY;

/**
 * Date: 2023/3/10
 * <p>
 * Description: json工具
 *
 * @author 子恺
 */
@Slf4j
public class JsonUtil {


    private JsonUtil() {
    }

    public static String toJson(Object obj) {
        try {
            if (Objects.isNull(obj)) {
                return EMPTY;
            }
            return JSON.toJSONString(obj);
        } catch (Exception e) {
            log.error("JsonUtil.toJson error:{}", obj.toString(), e);
            throw e;
        }
    }

    public static <T> T toObject(String json, Class<T> clazz) {
        try {
            if (org.apache.commons.lang.StringUtils.isBlank((json))) {
                return null;
            }
            return JSON.parseObject(json, clazz);
        } catch (Exception e) {
            log.error("JsonUtil.fromJson error:{}", json, e);
            throw e;
        }
    }


    public static <T> T toObject(String json, Class<T> clazz, JSONReader.Feature... features) {
        try {
            return JSON.parseObject(json, clazz, features);
        } catch (Exception e) {
            log.error("JsonUtil.fromJson error:{}", json, e);
            throw e;
        }
    }

    public static <K, V> Map<K, V> toMap(String json) {
        try {
            return JSON.parseObject(json, new TypeReference<HashMap<K, V>>() {
            });
        } catch (Exception e) {
            log.error("JsonUtil.fromJson error:{}", json, e);
            throw e;
        }
    }

    public static <T> List<T> toList(String json, Class<T> clazz) {
        try {
            if (StringUtils.isBlank((json))) {
                return Lists.newArrayList();
            }
            return JSON.parseArray(json, clazz);
        } catch (Exception e) {
            log.error("JsonUtil.toList error:{}", json, e);
            throw e;
        }
    }

}
