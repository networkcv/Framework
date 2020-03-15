package com.zyt.ytcollege.service;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.EvaluateDO;
import com.zyt.ytcollege.util.Paging;

/**
 * create by lwj on 2020/3/14
 */
public interface EvaluateService {
    Page<EvaluateDO> findEvaluate(int type, int id, Paging paging);
}
