package com.zyt.ytcollege.controller;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.ClassesDO;
import com.zyt.ytcollege.service.ClassesService;
import com.zyt.ytcollege.util.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by lwj on 2020/3/14
 */
@RestController
@RequestMapping("c")
public class ClassesController {
    @Autowired
    private ClassesService classesService;

    @GetMapping("/classes/tid/{id}")
    public Page<ClassesDO> chargeClasses(@PathVariable int id, Paging paging) {
        return classesService.findByTid(id,paging);
    }

}
