package com.lwj.mapper;

import com.lwj.pojo.Order;
import com.lwj.pojo.User;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Date: 2021/11/16
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public interface OrdersMapper {

    @Select("select * from orders where id=#{id}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "account", column = "account"),
            @Result(property = "user", column = "uid", javaType = User.class, one = @One(select = "com.lwj.mapper.UserMapper.selectUserById"))
    })
    Order selectOrderById(Integer id);

    @Select("select * from orders where uid=#{uid}")
    List<Order> selectOrdersByUid(Integer uid);

}
