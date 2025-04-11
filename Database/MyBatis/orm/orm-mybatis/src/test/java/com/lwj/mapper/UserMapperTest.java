package com.lwj.mapper;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
import java.util.List;

/**
 * Date: 2021/11/16
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@Slf4j
public class UserMapperTest {
    private UserMapper userMapper;

    @Before
    public void before() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        userMapper = sqlSession.getMapper(UserMapper.class);
    }

    @Test
    public void findAll() {
        User user = userMapper.selectUserById(1);
        System.out.println(user);
    }

    @Test
    public void selectUserWithOrders() {
        User user = userMapper.selectUserWithOrders(1);
        System.out.println(user.getClass());
        log.info("selectUserWithOrders username : {}", user.getUsername());
        log.info("selectUserWithOrders orderList : {}", user.getOrderList());
        log.info("selectUserWithOrders user:{}", user);
    }

    @Test
    public void selectAll() {
        PageHelper.startPage(1, 1);
        List<User> users = userMapper.selectAll();
        PageInfo<User> pageInfo = new PageInfo<>(users);
        log.info("pageInfo : {}", pageInfo.getTotal());
    }

    @Test
    public void updateUser() {
        User user = User.builder().id(1).username("sys").build();
        Integer integer = userMapper.updateUser(user);
        System.out.println(integer);
    }
}