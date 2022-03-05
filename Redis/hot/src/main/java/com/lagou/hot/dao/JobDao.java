package com.lagou.hot.dao;

import com.lagou.hot.po.TJob;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface JobDao {
    public List<TJob> selectAll();
}
