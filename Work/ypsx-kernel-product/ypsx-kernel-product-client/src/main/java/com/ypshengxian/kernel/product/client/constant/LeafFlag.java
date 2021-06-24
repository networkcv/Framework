package com.ypshengxian.kernel.product.client.constant;

/**
 * Date: 2021/3/8
 * <p>
 * Description: 是否叶子节点 1-非叶子节点 2-叶子节点
 *
 * @author liuWangjie
 */
public enum LeafFlag {
    DEFAULT(0, "缺省"),
    NOT_LEAF_FLAG(1, "非叶子节点"),
    LEAF_FLAG(2, "叶子节点");

    private final Integer val;

    private final String desc;

    LeafFlag(Integer val, String desc) {
        this.val = val;
        this.desc = desc;
    }

    public int getVal() {
        return val;
    }

    public String getDesc() {
        return desc;
    }

    public static LeafFlag getEnumByValue(int val) {
        for (LeafFlag leafFlag : LeafFlag.values()) {
            if (leafFlag.getVal() == val) {
                return leafFlag;
            }
        }
        return DEFAULT;
    }

    public static LeafFlag getEnumByDescribe(String desc) {
        for (LeafFlag value : LeafFlag.values()) {
            if (value.getDesc().equals(desc)) {
                return value;
            }
        }
        return DEFAULT;
    }
}
