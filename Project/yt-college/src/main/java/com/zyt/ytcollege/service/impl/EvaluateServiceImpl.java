package com.zyt.ytcollege.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zyt.ytcollege.dao.DO.EvaluateDO;
import com.zyt.ytcollege.dao.mapper.EvaluateMapper;
import com.zyt.ytcollege.service.EvaluateService;
import com.zyt.ytcollege.util.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by lwj on 2020/3/14
 */
@Service
public class EvaluateServiceImpl implements EvaluateService {
    @Autowired
    private EvaluateMapper evaluateDao;

    @Override
    public Page<EvaluateDO> findEvaluate(int type, int id, Paging paging) {
        Page<EvaluateDO> evaluateDOs;
        PageHelper.startPage(paging.getPageNum(), paging.getPageSize());
        if (type == 0) {
            evaluateDOs=evaluateDao.selectEvaluateByTypeAndTid(type, id);
        }else {
            evaluateDOs=evaluateDao.selectEvaluateByTypeAndSid(type, id);
        }
        return evaluateDOs;
    }
}
