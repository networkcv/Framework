package com.zyt.ytcollege.dao.mapper;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.TeacherDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * create by lwj on 2020/3/14
 */
@Mapper
public interface TeacherMapper {

    int updateTeacher(TeacherDO teacherDO);

    @Delete("delete from teacher where id=#{id}")
    int removeTeacherById(int id);

    @Select("select * from teacher where id=#{id}")
    TeacherDO selectTeacherById(int id);

    @Select("select * from teacher where phone=#{phone}")
    TeacherDO selectTeacherByPhone(String phone);

    Page<TeacherDO> selectAll(TeacherDO teacherDO);

    int insertTeacher(TeacherDO teacherDO);
}
