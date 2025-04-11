package com.lwj.mapper;

import com.lwj.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Date: 2021/11/17
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@Slf4j
public class CacheTest {
    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void before() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
    }

    @Test
    public void firstLevelCacheTest() {
        //需要关闭二级缓存测试
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        User user = mapper.selectUserById(1);
//        sqlSession.clearCache();
        User user2 = mapper.selectUserById(1);
        System.out.println(user == user2);
    }

    @Test
    public void firstLevelCacheInconsistent() {
        SqlSession sqlSession1 = sqlSessionFactory.openSession(true);
        SqlSession sqlSession2 = sqlSessionFactory.openSession(true);
        UserMapper mapper1 = sqlSession1.getMapper(UserMapper.class);
        UserMapper mapper2 = sqlSession2.getMapper(UserMapper.class);
        User user1 = mapper1.selectUserById(1);
        User user2 = mapper2.selectUserById(1);
        log.info("user1:{}", user1);
        log.info("user2:{}", user2);
        mapper2.updateUser(User.builder().id(1).username("tom").build());
        sqlSession2.commit();
        User user11 = mapper1.selectUserById(1);
        User user22 = mapper2.selectUserById(1);
        System.out.println(user1 == user11);
        log.info("user11:{}", user11);
        log.info("user22:{}", user22);
    }

    @Test
    public void SecondLevelCacheTest() {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        SqlSession sqlSession2 = sqlSessionFactory.openSession();
        UserMapper mapper = sqlSession.getMapper(UserMapper.class);
        UserMapper mapper2 = sqlSession2.getMapper(UserMapper.class);
        User user = mapper.selectUserById(1);
        sqlSession.commit();
        User user2 = mapper2.selectUserById(1);
        System.out.println(user == user2);
    }
}
