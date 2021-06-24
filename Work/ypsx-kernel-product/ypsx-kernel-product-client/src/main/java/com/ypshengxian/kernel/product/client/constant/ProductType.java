package com.ypshengxian.kernel.product.client.constant;

public enum ProductType {

    DEFAULT(0, ""),

    REAL_GOODS(1, "实物商品"),

    CONSUMABLE_GOODS(2, "物料耗材"),

    VIRTUAL_GOODS(3, "虚拟商品"),

    GIFT_GOODS(4, "礼品卡");

    private final Integer val;

    private final String desc;

    ProductType(Integer val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public int getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }

    public static ProductType getEnumByValue(int val) {
        for (ProductType value : ProductType.values()) {
            if (value.getVal() == val) {
                return value;
            }
        }
        return DEFAULT;
    }

    public static ProductType getEnumByDescribe(String desc) {
        for (ProductType value : ProductType.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return DEFAULT;
    }
}
