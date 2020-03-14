package com.zyt.ytcollege.controller;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.TeacherDO;
import com.zyt.ytcollege.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by lwj on 2020/3/14
 */
@RestController
@RequestMapping("t")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @GetMapping("/teachers")
    public Page<TeacherDO> list(int pageNum, int pageSize) {
        return teacherService.list(pageNum, pageSize);
    }


}
