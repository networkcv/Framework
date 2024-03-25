package com.lwj._00_AST.other.automation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date: 2024/3/21
 * <p>
 * Description:
 *
 * @author 乌柏
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParsePrepare {

    /**
     * 目标类
     */
    private Class<?> targetClass;

    /**
     * 解析范围
     */
    private Scope scope;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Scope {

        /**
         * 解析的包范围
         */
        private String packageScope;


    }
}
