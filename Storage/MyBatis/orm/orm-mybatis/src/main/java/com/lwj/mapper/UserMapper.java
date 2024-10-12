package com.lwj.mapper;

import com.lwj.pojo.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

import static org.apache.ibatis.mapping.FetchType.LAZY;

/**
 * Date: 2021/11/16
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
//@CacheNamespace(implementation = RedisCache.class)
//@CacheNamespace()
public interface UserMapper {

    @Select("select * from user where id = #{id}")
    User selectUserById(Integer id);

    @Select("select * from user where id = #{id}")
    @Results({
            @Result(id = true, property = "id", column = "id"),
            @Result(property = "username", column = "username"),
            @Result(property = "orderList", column = "id", javaType = List.class, many = @Many(select = "com.lwj.mapper.OrdersMapper.selectOrdersByUid", fetchType = LAZY))
    })
    User selectUserWithOrders(Integer id);

    @Select("select * from user")
    List<User> selectAll();

    @Update("update user  set username=#{username} where id =#{id}")
    Integer updateUser(User user);


}
