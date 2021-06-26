package com.ypshengxian.kernel.product.client.message;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MerchantChangeDTO {
    private Integer type;//消息类型
    private Long merchantId;//商家id
    private String operator;//操作人
    private Long categoryId;//类目id
    private List<Long> brandIdList;//品牌idList
    private Long brandId;//品牌id
}
