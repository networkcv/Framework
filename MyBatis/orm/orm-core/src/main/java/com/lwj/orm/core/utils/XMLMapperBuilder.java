package com.lwj.orm.core.utils;

import com.google.common.collect.Maps;
import com.lwj.orm.core.model.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Date: 2021/10/28
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class XMLMapperBuilder {
    public Map<String, MappedStatement> parseMapper(InputStream inputStream) throws DocumentException, ClassNotFoundException {
        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();
        Map<String, MappedStatement> mappedStatementMap = Maps.newHashMap();

        String namespace = rootElement.attributeValue("namespace");

        List<Element> select = rootElement.selectNodes("select");

        for (Element element : select) {
            //id的值
            String id = element.attributeValue("id");
            String parameterType = element.attributeValue("parameterType");
            String resultType = element.attributeValue("resultType");
            //输⼊参数class
            Class<?> parameterTypeClass = getClassType(parameterType);
            //返回结果class
            Class<?> resultTypeClass = getClassType(resultType);
            //statementId
            String key = namespace + "." + id;
            //sql语句
            String textTrim = element.getTextTrim();
            //封装 mappedStatement
            MappedStatement mappedStatement = new MappedStatement();
            mappedStatement.setId(id);
            mappedStatement.setSql(textTrim);
            mappedStatement.setParameterType(parameterTypeClass);
            mappedStatement.setResultType(resultTypeClass);
            //填充 configuration
            mappedStatementMap.put(key, mappedStatement);
        }
        return mappedStatementMap;
    }

    private Class<?> getClassType(String parameterType) throws ClassNotFoundException {
        if (Objects.isNull(parameterType)) {
            return null;
        }
        return Class.forName(parameterType);
    }
}
