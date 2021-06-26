package com.ypshengxian.kernel.product.client.message;

import java.util.Arrays;

/**
 * Date: 2021/3/8
 * <p>
 * Description:事件类型
 *
 * @author liuWangjie
 */
public enum EventType {
    TYPE_CREATE_CATEGORY(1, "新增类目"),
    TYPE_UPDATE_CATEGORY(2, "更新类目"),
    TYPE_DISABLE_BRAND(3,"禁用品牌"),
    TYPE_BRAND_NAME_CHANGE(4,"品牌名称变更"),

    TYPE_CREATE_MERCHANT_ITEM(1, "创建商家商品"),
    TYPE_UPDATE_MERCHANT_ITEM(2, "更新商家商品"),
    TYPE_CREATE_SHOP_ITEM(3, "创建营业部商品"),
    TYPE_UPDATE_SHOP_ITEM(4, "更新营业部商品"),
    TYPE_CHANGE_SHOP_ITEM_STATUS(5, "修改营业部商品上下架状态"),
    TYPE_CHANGE_ITEM_STOCK(6, "商品库存修改"),
    TYPE_PUSH_MERCHANT_ITEM_STATUS(7, "商家商品触发状态变更"),
    TYPE_CREATE_MERCHANT_ITEM_STATUS(8, "商家商品创建"),
    TYPE_CREATE_MERCHANT_ITEM_UPDATE_IMG_URL(9, "创建商家商品转换图片url"),
    TYPE_CREATE_PRODUCT(12, "创建商品基础资料"),
    TYPE_UPDATE_PRODUCT(13, "更新商品基础资料"),
    TYPE_CREATE_SG_MERCHANT_ITEM(14, "创建SG商家商品"),
    TYPE_UPDATE_SG_MERCHANT_ITEM(15, "更新SG商家商品"),
    TYPE_CHANGE_SG_MERCHANT_ITEM_STATUS(16, "更新SG商家商品状态"),
    TYPE_MERCHANT_ITEM_TO_SHOP(17, "平台化商家商品下发营业部"),
    TYPE_MERCHANT_ITEM_FEN_DANG(18, "ERP商家商品分档消息"),
    TYPE_MERCHANT_ITEM_ADD_MULTI_UNIT(19, "商家商品新增单位"),
    TYPE_CREATE_SG_SHOP_ITEM(20, "创建SG地点商品"),
    TYPE_UPDATE_SG_SHOP_ITEM(21, "更新SG地点商品"),
    TYPE_CHANGE_SG_SHOP_ITEM_STATUS(22, "更新SG地点商品状态"),
    TYPE_PRICE_MODIFY_EFFECTIVE(23, "价格调整生效"),
    TYPE_ES_CREATE(24, "创建商品通知ES"),
    TYPE_ES_UPDATE(25, "更新商品通知ES"),
    TYPE_UPDATE_CATEGORY_OFFSHELF_AND_OFFSALE(26, "禁用类目后下架停售"),
    TYPE_UPDATE_BRAND_OFFSHELF_AND_OFFSALE(27, "禁用品牌后下架停售"),
    TYPE_HOME_ACTIVITY_CREATE(28, "创建商品发送消息给首页活动"),
    TYPE_HOME_ACTIVITY_UPDATE(29, "更新商品发送消息给首页活动"),
    TYPE_HOME_ACTIVITY_SHELF(30, "上下架商品发送消息给首页活动"),
    TYPE_CHANGE_SHELF_STATUS(31, "上下架商品发送消息"),
    TYPE_UPDATE_ITEM_ALIAS(32, "修改商家商品别名"),
    TYPE_CREATE_TAKEAWAY_ITEM_SKU(33, "创建门店外卖商品"),
    TYPE_UPDATE_TAKEAWAY_ITEM_SKU(34, "修改门店外卖商品"),
    TYPE_UPDATE_TAKEAWAY_ITEM_PRICE(35, "修改门店外卖商品价格"),
    TYPE_UPDATE_TAKEAWAY_ITEM_IMG(36, "修改门店外卖商品主图"),
    TYPE_TAKEAWAY_MERCHANT_ITEM_ADD_MULTI_UNIT(37, "新增地点外卖商品规格"),
    TYPE_UPDATE_SG_PRODUCT_INFO(38, "更新主档商品信息"),
    TYPE_CHECK_CATEGORY_BOX_INFO(41, "档案类目箱子更新时触发类目箱子异常检查"),

    ;
    private final Integer type;

    private final String desc;

    EventType(Integer type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }


    public static EventType getByType(int value) {
        return Arrays.stream(EventType.values())
                .filter(eventType -> eventType.getType() == value)
                .findFirst()
                .orElse(null);
    }
}

