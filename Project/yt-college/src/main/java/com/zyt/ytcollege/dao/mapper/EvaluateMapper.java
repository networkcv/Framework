package com.zyt.ytcollege.dao.mapper;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.EvaluateDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * create by lwj on 2020/3/14
 */
@Mapper
public interface EvaluateMapper {
    @Select("select * from evaluate where type=#{type} and teacher_id =#{id} ")
    Page<EvaluateDO> selectEvaluateByTypeAndTid(int type, int id);

    @Select("select * from evaluate where type=#{type} and student_id =#{id} ")
    Page<EvaluateDO> selectEvaluateByTypeAndSid(int type, int id);
}
