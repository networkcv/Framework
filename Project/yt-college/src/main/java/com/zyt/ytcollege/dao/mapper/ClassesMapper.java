package com.zyt.ytcollege.dao.mapper;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.ClassesDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * create by lwj on 2020/3/14
 */
@Mapper
public interface ClassesMapper {

    @Select("select * from classes where teacher_id=#{id}")
    Page<ClassesDO> selectClassesByTid(int id);
}
