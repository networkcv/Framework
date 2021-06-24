package com.ypshengxian.kernel.product.client.message;

import lombok.Builder;
import lombok.Data;

/**
 * Date: 2021/3/22
 * <p>
 * Description: 品牌消息
 *
 * @author liuWangjie
 */
@Data
@Builder
public class BrandMsgDTO {
    /**
     * 品牌id
     */
    private Long brandId;
    /**
     * 类目名称
     */
    private String brandName;
    /**
     * 品牌状态 1-启用 2-不启用
     */
    private Integer status;
    /**
     * 操作人
     */
    private String operator;
}
