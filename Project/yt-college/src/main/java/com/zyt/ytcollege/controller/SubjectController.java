package com.zyt.ytcollege.controller;

import com.github.pagehelper.PageInfo;
import com.zyt.ytcollege.dao.DO.SubjectDO;
import com.zyt.ytcollege.service.SubjectService;
import com.zyt.ytcollege.util.JsonMsg;
import com.zyt.ytcollege.util.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * create by lwj on 2020/3/14
 */
@RestController
@RequestMapping("su")
public class SubjectController {
    @Autowired
    private SubjectService subjectService;

    /**
     * 添加课程
     *
     * @param subjectDO 提供
     */
    @PostMapping("subject")
    public JsonMsg saveSubject(SubjectDO subjectDO) {
        return subjectService.saveSubject(subjectDO);
    }

    @DeleteMapping("subject/{id}")
    public JsonMsg removeSubject(@PathVariable int id) {
        return subjectService.removeSubjectById(id);
    }

    @PutMapping("subject")
    public JsonMsg editSubject(SubjectDO subjectDO) {
        return subjectService.editSubject(subjectDO);
    }

    @GetMapping("/subject/{id}")
    public SubjectDO findSubject(@PathVariable int id) {
        return subjectService.findSubjectById(id);
    }

    @GetMapping("subjects")
    public PageInfo<SubjectDO> list(Paging paging, SubjectDO subjectDO) {
        return subjectService.findAllSubject(paging, subjectDO);
    }

}
