package com.ypshengxian.kernel.product.client.constant;

/**
 *
 */
public enum BusinessType {

    DEFAULT(0, "默认"),
    DJ(1, "到家"),
    QGBY(2, "全国包邮"),
    SX(3, "生鲜"),
    YPB(4, "谊批宝"),
    WM(5, "外卖"),
    ;

    private final Integer code;
    private final String desc;

    BusinessType(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public static BusinessType getEnumByValue(int val) {
        for (BusinessType value : BusinessType.values()) {
            if (value.getCode() == val) {
                return value;
            }
        }
        return DEFAULT;
    }
}
