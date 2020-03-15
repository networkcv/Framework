package com.zyt.ytcollege.dao.mapper;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.ClassroomDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * create by lwj on 2020/3/14
 */
@Mapper
public interface ClassroomMapper {

    int insertClassroom(ClassroomDO ClassroomDO);

    int updateClassroom(ClassroomDO ClassroomDO);

    @Delete("delete from Classroom where id=#{id}")
    int removeClassroomById(int id);

    Page<ClassroomDO> selectAll(ClassroomDO ClassroomDO);

    @Select("select * from Classroom where id=#{id}")
    ClassroomDO selectClassroomById(int id);
}
