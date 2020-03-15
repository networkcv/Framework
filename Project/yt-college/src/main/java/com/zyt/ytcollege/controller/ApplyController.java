package com.zyt.ytcollege.controller;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.ApplyDO;
import com.zyt.ytcollege.service.ApplyService;
import com.zyt.ytcollege.util.JsonMsg;
import com.zyt.ytcollege.util.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * create by lwj on 2020/3/14
 */
@RestController
@RequestMapping("ap")
public class ApplyController {
    @Autowired
    private ApplyService applyService;
    @PostMapping("Apply")
    public JsonMsg saveApply(ApplyDO ApplyDO){
        return applyService.saveApply(ApplyDO);
    }

    @DeleteMapping("Apply/{id}")
    public JsonMsg removeApply(@PathVariable int id){
        return applyService.removeApplyById(id);
    }

    @PutMapping("Apply")
    public JsonMsg editApply(ApplyDO ApplyDO){
        return applyService.editApply(ApplyDO);
    }

    @GetMapping("/Apply/{id}")
    public ApplyDO findApply(@PathVariable int id){
        return applyService.findApplyById(id);
    }

    @GetMapping("Applys")
    public Page<ApplyDO> list(Paging paging,ApplyDO ApplyDO){
        return applyService.findAllApply(paging,ApplyDO);
    }

}
