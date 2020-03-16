package com.zyt.ytcollege.dao.mapper;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.ApplyDO;
import com.zyt.ytcollege.service.DTO.ApplyDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * create by lwj on 2020/3/14
 */
@Mapper
public interface ApplyMapper {

    int insertApply(ApplyDO applyDO);

    int updateApply(ApplyDO applyDO);

    @Delete("delete from apply where id=#{id}")
    int removeApplyById(int id);

    Page<ApplyDO> selectAll(ApplyDTO applyDTO);

    @Select("select * from apply where id=#{id}")
    ApplyDO selectApplyById(int id);
}
