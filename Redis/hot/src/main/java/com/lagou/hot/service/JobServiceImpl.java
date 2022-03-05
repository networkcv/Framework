package com.lagou.hot.service;

import com.lagou.hot.dao.JobDao;
import com.lagou.hot.po.TJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobServiceImpl implements  JobService{
    @Autowired
    private JobDao dao;

    @Override
    public List<TJob> selectAll() {
        return dao.selectAll();
    }
}
