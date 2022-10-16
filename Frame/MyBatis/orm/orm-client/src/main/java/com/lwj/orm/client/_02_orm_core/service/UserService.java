package com.lwj.orm.client._02_orm_core.service;

import com.lwj.orm.client._02_orm_core.model.User;
import com.lwj.orm.core.DefaultSqlSessionFactory;
import com.lwj.orm.core.SqlSession;
import com.lwj.orm.core.model.Configuration;
import com.lwj.orm.core.utils.Resources;
import com.lwj.orm.core.utils.XmlConfigBuilder;
import lombok.SneakyThrows;
import org.junit.Test;

/**
 * Date: 2021/11/5
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class UserService {

    @SneakyThrows
    @Test
    public void getUser() {
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parseConfiguration(Resources.getResourcesAsStream("sqlMapConfig.xml"));
        DefaultSqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = User.builder()
                .id(1)
                .username("tom")
                .build();
        User u=sqlSession.selectOne("user.selectOne", user);
        System.out.println(u);
    }
}
