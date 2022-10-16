package com.lwj.mapper;

import com.lwj.mapper.OrdersMapper;
import com.lwj.pojo.Order;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

/**
 * Date: 2021/11/16
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class OrdersMapperInterfaceTest {
    private OrdersMapper ordersMapper;

    @Before
    public void before() throws IOException {
        InputStream resourceAsStream = Resources.getResourceAsStream("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        ordersMapper = sqlSession.getMapper(OrdersMapper.class);
    }


    @Test
    public void selectOrderByIdTest() {
        Order order = ordersMapper.selectOrderById(1);
        System.out.println(order);
    }
}
