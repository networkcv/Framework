package com.ypshengxian.kernel.product.client.constant;

/**
 * Date: 2021/3/19
 * <p>
 * Description: 品牌状态 1-启用 2-禁用
 *
 * @author liuWangjie
 */
public enum BrandStatus {
    DEFAULT(0, "缺省"),
    ENABLE(1, "启用"),
    DISABLE(2, "禁用");

    private final Integer val;

    private final String desc;

    BrandStatus(Integer val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public int getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }

    public static BrandStatus getEnumByValue(int val) {
        for (BrandStatus leafFlag : BrandStatus.values()) {
            if (leafFlag.getVal() == val) {
                return leafFlag;
            }
        }
        return DEFAULT;
    }

    public static BrandStatus getEnumByDescribe(String desc) {
        for (BrandStatus value : BrandStatus.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return DEFAULT;
    }
}
