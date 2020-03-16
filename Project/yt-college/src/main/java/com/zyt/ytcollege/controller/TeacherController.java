package com.zyt.ytcollege.controller;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.TeacherDO;
import com.zyt.ytcollege.service.TeacherService;
import com.zyt.ytcollege.util.JsonMsg;
import com.zyt.ytcollege.util.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * create by lwj on 2020/3/14
 */
@RestController
@RequestMapping("t")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @PostMapping("teacher")
    public JsonMsg saveTeacher(TeacherDO teacherDO){
        return teacherService.saveTeacher(teacherDO);
    }

    @DeleteMapping("teacher/{id}")
    public JsonMsg removeTeacher(@PathVariable int id){
        return teacherService.removeTeacherById(id);
    }

    @PutMapping("teacher")
    public JsonMsg editTeacher(TeacherDO teacherDO){
        return teacherService.editTeacher(teacherDO);
    }

    @GetMapping("/teacher/{id}")
    public TeacherDO findTeacher(@PathVariable int id){
        return teacherService.findTeacherById(id);
    }

    @GetMapping("teachers")
    public Page<TeacherDO> list(Paging paging,TeacherDO teacherDO){
        return teacherService.findAllTeacher(paging,teacherDO);
    }

    @GetMapping("login")
    public JsonMsg login(TeacherDO teacherDO){
        return teacherService.login(teacherDO);
    }

}
