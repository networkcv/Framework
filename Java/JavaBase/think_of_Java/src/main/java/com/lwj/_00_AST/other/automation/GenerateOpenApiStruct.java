package com.lwj._00_AST.other.automation;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.lwj._00_AST.other.automation.test.Student;
import com.lwj._00_AST.other.automation.vo.ClassVO;
import com.lwj._00_AST.other.automation.vo.FieldVO;
import com.lwj._100_utils.JsonUtil;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GenerateOpenApiStruct {

    public static void main(String[] args) {
        ClassVO classVO = ClassParser.parse(Student.class, "com");
        assert classVO != null;
        System.out.println("解析结果:");
        System.out.println(JsonUtil.toJson(assemble(classVO.getFields())));
    }

    private static List<Parameter> assemble(List<FieldVO> fieldList) {
        if (CollectionUtil.isEmpty(fieldList)) {
            return Lists.newArrayList();
        }
        return fieldList.stream().map(GenerateOpenApiStruct::assemble).collect(Collectors.toList());
    }


    private static Parameter assemble(FieldVO field) {
        String fieldName = field.getFieldName();
        if (fieldName.equals("serialVersionUID")) {
            return null;
        }
        ClassVO fieldFieldType = field.getFieldType();
        if (Objects.isNull(fieldFieldType)) {
            return null;
        }
        String fieldType = fieldFieldType.getGenericQualifiedName();


        String describe = field.getDescribe();
        Set<String> basicTypeMap = Sets.newHashSet();
        basicTypeMap.add("java.lang.Integer");
        basicTypeMap.add("java.lang.Long");
        basicTypeMap.add("java.lang.String");
        Parameter parameter = new Parameter();
        boolean isBasic = basicTypeMap.contains(fieldType);
        //字段标识
        parameter.setName(fieldName);
        //是否基础类型
//        parameter.setBasic(isBasic);
        parameter.setBasic(true);
        //基础类型为类全名，非基础类型为类名（需提前导入到API类中），否则用Object代替
        parameter.setDataType(isBasic ? fieldType : "Object");
        //是否必填
        parameter.setNecessary(true);
        //字段名称
        parameter.setDisplayName(describe);
        //字段长度
        parameter.setLength(200);
        //示例值,测试的时候使用
        parameter.setSample("1");
        //是否外网展示
        parameter.setOpen(true);
        //是否生成文档
        parameter.setDoc(true);
        //系统参数
        if (fieldName.equals("sysCompanyId") || fieldName.equals("sysShopId")) {
            parameter.setSystemKey(fieldName);
        }
        //字段描述
        parameter.setDesc(describe);
        //字段默认值
        parameter.setDefaultValue(null);
        //子参数
        List<Parameter> tmpChildList = null;
        //是引用类型 但不是集合类型 也不是String类 用引用类型结构体
        if (field.isReferenceType() && !field.hasMultiElement() && !field.getFieldType().getClazz().getSimpleName().equals("String")) {
            tmpChildList = Lists.newArrayList(assemble(fieldFieldType.getFields()));
        }
        //是引用类型 是集合 且 指定了泛型 用泛型的结构体
        if (field.isReferenceType() && field.hasMultiElement() && field.hasGenericType()) {
            tmpChildList = assemble(field.getGenericsTypes().get(0).getFields());
        }
        parameter.setChildren(tmpChildList);
        return parameter;
    }
}


