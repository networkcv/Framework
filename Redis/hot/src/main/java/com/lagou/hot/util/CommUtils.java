package com.lagou.hot.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lagou.hot.po.TJob;

import java.util.ArrayList;
import java.util.List;

public final class CommUtils {
    public static List<String> objToJson(List<TJob> olist) {
        List<String> jlist = new ArrayList<>();
        for (TJob job : olist) {
            jlist.add(JSON.toJSONString(job));
        }
        return jlist;
    }

    public static List<Object> jsonToObj(List<Object> jlist, Class clazz) {
        List<Object> olist = new ArrayList<>();
        for (Object json : jlist) {
            olist.add(JSONObject.parseObject(json.toString(), clazz));

        }
        return olist;
    }

    
}
