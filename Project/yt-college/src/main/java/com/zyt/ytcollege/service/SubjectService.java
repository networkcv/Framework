package com.zyt.ytcollege.service;

import com.github.pagehelper.PageInfo;
import com.zyt.ytcollege.dao.DO.SubjectDO;
import com.zyt.ytcollege.util.JsonMsg;
import com.zyt.ytcollege.util.Paging;

/**
 * create by lwj on 2020/3/14
 */
public interface SubjectService {
    JsonMsg saveSubject(SubjectDO subjectDO);

    JsonMsg editSubject(SubjectDO subjectDO);

    JsonMsg removeSubjectById(int id);

    SubjectDO findSubjectById(int id);

    PageInfo<SubjectDO> findAllSubject(Paging paging, SubjectDO subjectDO);
}
