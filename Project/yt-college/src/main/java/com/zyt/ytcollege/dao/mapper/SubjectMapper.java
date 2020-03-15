package com.zyt.ytcollege.dao.mapper;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.SubjectDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * create by lwj on 2020/3/14
 */
@Mapper
public interface SubjectMapper {

    int insertSubject(SubjectDO subjectDO);

    int updateSubject(SubjectDO subjectDO);

    @Delete("delete from subject where id=#{id}")
    int removeSubjectById(int id);

    Page<SubjectDO> selectAll(SubjectDO subjectDO);

    @Select("select * from subject where id=#{id}")
    SubjectDO selectSubjectById(int id);
}
