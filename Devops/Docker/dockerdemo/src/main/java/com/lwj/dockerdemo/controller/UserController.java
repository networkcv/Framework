package com.lwj.dockerdemo.controller;

import com.lwj.dockerdemo.entity.Tbuser;
import com.lwj.dockerdemo.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/sayHello")
    public String sayHello() {
        return "springboot docker hello!!!";
    }

    @GetMapping("/users")
    public List<Tbuser> queryUsers() {
        return this.userService.queryUsers();
    }

    @PostMapping("/login")
    public String login(@RequestBody Tbuser tbuser) {
        if(this.userService.login(tbuser)!=null){
            return "登录成功!";
        }else{
            return "对不起，用户名或者密码错误!";
        }
    }

    @GetMapping("/queryUserByid/{userid}")
    public Tbuser queryUserByid(@PathVariable("userid") int userid) {
        return this.userService.queryUserByid(userid);
    }
}
