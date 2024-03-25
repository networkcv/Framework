package com.lwj._00_AST.other.automation.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Date: 2024/3/17
 * <p>
 * Description:
 *
 * @author 乌柏
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassVO {

    /**
     * 类名
     */
    private String className;

    /**
     * 全限定名
     */
    private String genericQualifiedName;

    /**
     * 类型
     */
    private Class<?> clazz;

    /**
     * 字段列表
     */
    private List<FieldVO> fields;

    /**
     * 判断当前类型是否是集合
     *
     * @return boolean
     */
    public boolean isCollection() {
        return Collection.class.isAssignableFrom(clazz);
    }

    /**
     * 判断当前类型是否是List
     *
     * @return boolean
     */
    public boolean isList() {
        return List.class.isAssignableFrom(clazz);
    }

    /**
     * 判断当前类型是否是Set
     *
     * @return boolean
     */
    public boolean isSet() {
        return Set.class.isAssignableFrom(clazz);
    }

    /**
     * 判断是否是引用类型
     *
     * @return boolean
     */
    public boolean isReferenceType() {
        try {
            return !((Class<?>) clazz.getField("TYPE").get(null)).isPrimitive();
        } catch (Exception e) {
            return true;
        }
    }

}
