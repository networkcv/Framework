package com.zyt.ytcollege.controller;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.EvaluateDO;
import com.zyt.ytcollege.service.EvaluateService;
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
@RequestMapping("e")
public class EvaluateController {
    @Autowired
    private EvaluateService evaluateService;

    @GetMapping("/evaluates/{type}/{id}")
    public Page<EvaluateDO> findEvaluate(@PathVariable int type, @PathVariable int id, Paging paging) {
        return evaluateService.findEvaluate(type,id,paging);
    }
}
