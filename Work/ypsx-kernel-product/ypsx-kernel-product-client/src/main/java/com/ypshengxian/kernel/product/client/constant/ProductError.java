package com.ypshengxian.kernel.product.client.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductError {

    /**
     * 系统错误码
     */
    SYSTEM_ERROR(-10000, "系统异常"),
    QPS_HIGH(-10001, "请求太快，请稍后在试"),
    USER_NOT_LOGIN(-10002, "用户未登录"),
    PARAM_EMPTY(-10003, "参数%s为空"),
    PARAM_EXIST(-10004, "%s已存在"),
    PARAM_ERROR(-10005,"参数%s异常"),
    PAGE_INFO_EMPTY(-10006, "分页信息为空"),
    QUERY_SIZE_TOO_MUCH(-10007, "查询数量超过限制%s条"),

    /**
     * 属性错误码
     */
    PROPERTY_EXIST(-20000, "属性已经存在"),
    PROPERTY_NOT_EXIST(-20001, "属性不存在"),
    VALUE_EXIST(-20002, "值已经存在"),
    VALUE_NOT_EXIST(-20003, "值不存在"),

    /**
     * 类目错误码
     */
    CATEGORY_ID_EXIST(-30000, "类目编码已经存在"),
    CATEGORY_NAME_EXIST(-30001, "类目名称已经存在"),
    CATEGORY_NOT_EXIST(-30002, "类目不存在"),
    PARENT_CATEGORY_DISABLE(-30003, "上级类目被禁用,无法编辑"),

    /**
     * 品牌错误码
     */
    BRAND_NAME_EXIST(-40000, "品牌名称已经存在"),
    BRAND_EXIT(-40001, "该品牌已存在，无需再创建"),

    /**
     * 商品属性错误码
     */
    PROPERTY_VALUE_EXIST(-50000, "该商品属性值已存在"),
    ;

    private final int code;

    private final String message;

    public String getMessage(String extraMsg) {
        return null == extraMsg || extraMsg.length() == 0 ?
                this.message : String.format(this.message, extraMsg);
    }
}
