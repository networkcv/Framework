package com.lwj.mapper;

import com.lwj.pojo.Role;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Date: 2021/11/16
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public interface RoleMapper {
    @Select("select * from role where uid = #{uid}")
    List<Role> selectRoleByUid(Integer uid);
}
