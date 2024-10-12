package com.lwj.orm.core;


import com.google.common.collect.Lists;
import com.lwj.orm.core.model.BoundSql;
import com.lwj.orm.core.model.Configuration;
import com.lwj.orm.core.model.MappedStatement;
import com.lwj.orm.core.parse.GenericTokenParser;
import com.lwj.orm.core.parse.ParameterMapping;
import com.lwj.orm.core.parse.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.List;

/**
 * Date: 2021/10/28
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class SimpleExecutor implements Executor {

    private Connection connection = null;

    @Override
    public <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object[] param) throws Exception {
        //1.注册驱动 获取连接
        connection = configuration.getDataSource().getConnection();

        //2.获取sql语句 select * from user where id = #{id} and username = #{username}
        //  转换sql语句 select * from user where id = ？ and username = ？
        //  同时获取占位符的名称（id 和 username）
        BoundSql boundsql = getBoundSql(mappedStatement.getSql());

        //3.获取预处理对象
        PreparedStatement preparedStatement = connection.prepareStatement(boundsql.getSqlParsed());

        //4.设置参数
        Class<?> parameterTypeClass = mappedStatement.getParameterType();
        List<ParameterMapping> parameterMappings = boundsql.getParameterMappings();
            for (int i = 0; i < parameterMappings.size(); i++) {
                String content = parameterMappings.get(i).getContent();
                Field field = parameterTypeClass.getDeclaredField(content);
                field.setAccessible(true);
                Object o = field.get(param[0]);
                preparedStatement.setObject(i + 1, o);
            }

        //5.执行sql
        ResultSet resultSet = preparedStatement.executeQuery();

        //6.封装返回结果集
        List<Object> resultList = Lists.newArrayList();
        Class<?> resultType = mappedStatement.getResultType();

        while (resultSet.next()) {
            Object result = resultType.newInstance();
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                String columnName = metaData.getColumnName(i);
                Object value = resultSet.getObject(columnName);
                PropertyDescriptor propertyDescriptor = new PropertyDescriptor(columnName, resultType);
                propertyDescriptor.getWriteMethod().invoke(result, value);
            }
            resultList.add(result);
        }
        return (List<E>) resultList;
    }

    @Override
    public void close() throws SQLException {
    }

    /**
     * 完成对#{}的解析工作，1.将#{}使用？进行代替，2.解析出#{}里边的值进行存储
     */
    public BoundSql getBoundSql(String sql) {
        //标记处理类：配合通⽤标记解析器GenericTokenParser类完成对配置⽂件等的解析⼯作
        //其中TokenHandler主要完成处理
        //GenericTokenParser :通⽤的标记解析器，完成了代码⽚段中的占位符的解析，然后再根 据给定的标记处理器(TokenHandler)来进⾏表达式的处理 //三个参数：分别为openToken (开始标记)、closeToken (结束标记)、handler (标记 处 理器)
        // GenericTokenParser genericTokenParser = new GenericTokenParser("# {", "}", parameterMappingTokenHandler); String parse = genericTokenParser.parse(sql);
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser genericTokenParser = new GenericTokenParser("#{", "}", parameterMappingTokenHandler);
        //解析过后的sql
        String parseSql = genericTokenParser.parse(sql);
        //#{}里解析出来的参数名称
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        return new BoundSql(parseSql, parameterMappings);
    }

}