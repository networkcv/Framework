package com.lwj.sell.utils;


import com.lwj.sell.enums.CodeEnum;

/**
 * Created by lwj
 * 2020-04-10 18:36
 */
public class EnumUtil {

    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T each: enumClass.getEnumConstants()) {
            if (code.equals(each.getCode())) {
                return each;
            }
        }
        return null;
    }
}
