package com.lwj.orm.core.utils;

import com.alibaba.druid.pool.DruidDataSource;
import com.lwj.orm.core.model.Configuration;
import com.lwj.orm.core.model.MappedStatement;
import lombok.SneakyThrows;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Date: 2021/10/27
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class XmlConfigBuilder {

    @SneakyThrows
    public Configuration parseConfiguration(InputStream inputStream) {
        Document document = new SAXReader().read(inputStream);
        //<configuration>
        Element rootElement = document.getRootElement();

        List<Element> propertyElements = rootElement.selectNodes("//property");

        Properties properties = new Properties();
        for (Element propertyElement : propertyElements) {

            String name = propertyElement.attributeValue("name");

            String value = propertyElement.attributeValue("value");

            properties.setProperty(name, value);
        }

        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(properties.getProperty("driverClass"));
        dataSource.setUrl(properties.getProperty("jdbcUrl"));
        dataSource.setUsername(properties.getProperty("username"));
        dataSource.setPassword(properties.getProperty("password"));

        //填充configuration
        Configuration configuration = new Configuration();
        configuration.setDataSource(dataSource);

        //mapper
        List<Element> mapperElements = rootElement.selectNodes("//mapper");

        XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder();

        for (Element mapperElement : mapperElements) {
            String mapperPath = mapperElement.attributeValue("resource");
            InputStream resourceAsSteam = Resources.getResourcesAsStream(mapperPath);
            Map<String, MappedStatement> mapperStatementMap = xmlMapperBuilder.parseMapper(resourceAsSteam);
            configuration.getMapperStatementMap().putAll(mapperStatementMap);
        }

        return configuration;
    }
}
