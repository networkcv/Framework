package com.lwj.orm.core.utils;

import com.lwj.orm.core.DefaultSqlSessionFactory;
import com.lwj.orm.core.SqlSessionFactory;
import com.lwj.orm.core.model.Configuration;
import lombok.Data;

import java.io.InputStream;

/**
 * Date: 2021/10/27
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@Data
public class SqlSessionFactoryBuilder {

    private final Configuration configuration;

    public SqlSessionFactoryBuilder() {
        this.configuration = new Configuration();
    }

    public SqlSessionFactory build(InputStream inputStream) {
        Configuration configuration;
        try {
            //1.解析配置文件
            configuration = new XmlConfigBuilder().parseConfiguration(inputStream);

            //2.创建SqlSessionFactory
            return new DefaultSqlSessionFactory(configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
