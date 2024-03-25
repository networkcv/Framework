package com.lwj._00_AST.other.automation;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Date: 2024/3/14
 * <p>
 * Description:
 *
 * @author 乌柏
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Parameter {
    /**
     * 名称
     **/
    private String name;
    /**
     * 是否基本数据类型
     **/
    private Boolean basic = true;
    /**
     * 数据类型
     **/
    private String dataType;
    /**
     * 版本号
     **/
    private Integer version;
    /**
     * 展示名
     **/
    private String displayName;
    /**
     * 长度
     **/
    private Integer length = 0;
    /**
     * 精度
     **/
    private Integer precision = 0;
    /**
     * 系统参数key
     **/
    private String systemKey;
    /**
     * 默认值
     **/
    private String defaultValue;
    /**
     * 示例值
     **/
    private String sample;
    /**
     * 描述
     **/
    private String desc;
    /**
     * 是否必填
     **/
    private Boolean necessary = true;
    /**
     * 是否生成文档
     **/
    private Boolean doc = true;
    /**
     * 隐私级别
     **/
    private String privacyLevel = "NONE";
    /**
     * 是否外网展示
     **/
    private Boolean open = true;
    /**
     * 是否值映射
     **/
    private Boolean valueMapping = false;
    /**
     * 值映射变量名
     **/
    private String valueMappingKey;
    /**
     * 子参数
     **/
    private List<Parameter> children;

    /**
     * 是否需要加密 0 ：否，1：是
     */
    private Integer needEncrypt;

    /**
     * 是否需要解密 0 ：否，1：是
     */
    private Integer needDecrypt;

}