package com.zyt.ytcollege.dao.mapper;

import com.zyt.ytcollege.dao.DO.StudentDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * create by lwj on 2020/3/15
 */
@Mapper
public interface StudentMapper {
    StudentDO selectStudent(StudentDO studentQuery);

    int insertStudent(StudentDO student);


}
