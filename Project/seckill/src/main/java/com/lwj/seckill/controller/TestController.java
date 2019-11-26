package com.lwj.seckill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * create by lwj on 2019/11/26
 */
@Controller
public class TestController {

    @RequestMapping("test")
    @ResponseBody
    public String test1() {
        return "ok";
    }
}
