package com.lwj._00_AST.other.automation.vo;

/**
 * Date: 2024/3/14
 * <p>
 * Description:
 *
 * @author 乌柏
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Description:
 *
 * @author jack
 * @date 2021/7/13 5:15 下午
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocVO {

    private List<FieldVO> fieldVOList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldVO {
        /**
         * 属性标识
         */
        private String fieldName;

        /**
         * 属性类型
         */
        private String fieldType;


        /**
         * 属性注释
         */
        private String describe;

    }
}
