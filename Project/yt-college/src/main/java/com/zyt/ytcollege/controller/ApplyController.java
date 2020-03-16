package com.zyt.ytcollege.controller;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.ApplyDO;
import com.zyt.ytcollege.service.ApplyService;
import com.zyt.ytcollege.service.DTO.ApplyDTO;
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
    @PostMapping("apply")
    public JsonMsg saveApply(ApplyDO ApplyDO){
        return applyService.saveApply(ApplyDO);
    }

    @DeleteMapping("apply/{id}")
    public JsonMsg removeApply(@PathVariable int id){
        return applyService.removeApplyById(id);
    }

    @PutMapping("apply")
    public JsonMsg editApply(ApplyDO ApplyDO){
        return applyService.editApply(ApplyDO);
    }

    @GetMapping("apply/{id}")
    public ApplyDO findApply(@PathVariable int id){
        return applyService.findApplyById(id);
    }

    @GetMapping("applys")
    public Page<ApplyDO> list(Paging paging, ApplyDTO applyDTO,ApplyDO applyDO){
        applyDTO.setApplyDO(applyDO);
        return applyService.findAllApply(paging,applyDTO);
    }

    

}
