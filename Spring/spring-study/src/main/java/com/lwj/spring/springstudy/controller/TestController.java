package com.lwj.spring.springstudy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by lwj on 2020/2/15
 */
@RequestMapping
@RestController
public class TestController {
    @RequestMapping("test")
    public String test1() {
        return "ok";
    }
}
