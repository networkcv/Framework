/*
 * Copyright (C) 2009-2018 Hangzhou ypsx Technology Co., Ltd. All rights reserved
 */
package com.ypshengxian.kernel.product.client.constant;

/**
 * Date: 2021/3/8
 * <p>
 * Description: 类目状态 1-启用 2-禁用
 *
 * @author liuWangjie
 */
public enum CategoryStatus {

    DEFAULT(0, "缺省"),
    ENABLE(1, "启用"),
    DISABLE(2, "禁用");
    private final int val;
    private final String desc;

    CategoryStatus(int val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public int getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }

    public static CategoryStatus getEnumByValue(int val) {

        for (CategoryStatus value : CategoryStatus.values()) {

            if (value.getVal() == val) {
                return value;
            }
        }
        return DEFAULT;
    }

}

