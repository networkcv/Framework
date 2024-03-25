package com.lwj._00_AST.other.automation.vo;

import cn.hutool.core.collection.CollectionUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
public class FieldVO {

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 全限定名
     */
    private String genericQualifiedName;

    /**
     * 字段类型信息，字段类型为基础类型时会自动转为包装类型
     */
    private ClassVO fieldType;

    /**
     * 泛型类型
     */
    private List<ClassVO> genericsTypes;

    /**
     * 所属类的类型信息
     */
    private ClassVO curClassType;

    /**
     * 字段注释
     */
    private String describe;

    /**
     * 判断是否是引用类型
     *
     * @return boolean
     */
    public boolean isReferenceType() {
        return fieldType.isReferenceType();
    }

    /**
     * 判断是否有多个元素
     *
     * @return boolean
     */
    public boolean hasMultiElement() {
        return fieldType.isCollection();
    }

    /**
     * 判断是否指定泛化类型
     */
    public boolean hasGenericType() {
        return CollectionUtil.isNotEmpty(genericsTypes);
    }


}
