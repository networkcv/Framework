package com.zyt.ytcollege.controller;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.ClassroomDO;
import com.zyt.ytcollege.service.ClassroomService;
import com.zyt.ytcollege.util.JsonMsg;
import com.zyt.ytcollege.util.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * create by lwj on 2020/3/15
 */
@RestController
@RequestMapping("cr")
public class ClassroomController {
    @Autowired
    private ClassroomService classroomService;

    @PostMapping("classroom")
    public JsonMsg saveClassroom(ClassroomDO classRoomDO) {

        return classroomService.saveClassroom(classRoomDO);
    }

    @DeleteMapping("classroom/{id}")
    public JsonMsg removeClassroom(@PathVariable int id) {
        return classroomService.removeClassroomById(id);
    }

    @PutMapping("classroom")
    public JsonMsg editClassroom(ClassroomDO classRoomDO) {
        return classroomService.editClassroom(classRoomDO);
    }

    @GetMapping("/classroom/{id}")
    public ClassroomDO findClassroom(@PathVariable int id) {
        return classroomService.findClassroomById(id);
    }

    @GetMapping("classrooms")
    public Page<ClassroomDO> list(Paging paging, ClassroomDO classRoomDO) {
        return classroomService.findAllClassroom(paging, classRoomDO);
    }

}
