package com.zyt.ytcollege.service;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.ApplyDO;
import com.zyt.ytcollege.util.JsonMsg;
import com.zyt.ytcollege.util.Paging;

/**
 * create by lwj on 2020/3/14
 */
public interface ApplyService {
    JsonMsg saveApply(ApplyDO applyDO);

    JsonMsg editApply(ApplyDO applyDO);

    JsonMsg removeApplyById(int id);

    ApplyDO findApplyById(int id);

    Page<ApplyDO> findAllApply(Paging paging, ApplyDO applyDO);
}
