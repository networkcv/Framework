package com.ypshengxian.kernel.product.client.constant;

public enum WeightType {

    DEFAULT(0, "默认"),

    WEIGHT_TYPE_YES(1, "散称"),

    WEIGHT_TYPE_NO(2, "标品"),

    WEIGHT_TYPE_PCS(3, "PCS");

    private final Integer val;

    private final String desc;

    WeightType(Integer val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public int getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }

    public static WeightType getEnumByValue(int val) {
        for (WeightType value : WeightType.values()) {
            if (value.getVal() == val) {
                return value;
            }
        }
        return DEFAULT;
    }

    public static WeightType getEnumByDescribe(String desc) {
        for (WeightType value : WeightType.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return DEFAULT;
    }
}
