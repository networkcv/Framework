package com.lwj.orm.core;

import com.lwj.orm.core.model.Configuration;
import com.lwj.orm.core.model.User;
import com.lwj.orm.core.utils.Resources;
import com.lwj.orm.core.utils.XmlConfigBuilder;
import org.junit.Test;

import java.util.List;

/**
 * Date: 2021/10/27
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class XmlConfigBuilderTest {

    @Test
    public void parseConfiguration() throws Exception {
        XmlConfigBuilder xmlConfigBuilder = new XmlConfigBuilder();
        Configuration configuration = xmlConfigBuilder.parseConfiguration(Resources.getResourcesAsStream("sqlMapConfig.xml"));
        DefaultSqlSessionFactory sqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User user = User.builder()
                .id(1)
                .username("tom")
                .build();
        User u =sqlSession.selectOne("user.selectOne", user);
        System.out.println(u);

        List<User> userList =sqlSession.selectList("user.selectList");
        System.out.println(userList);
    }
}