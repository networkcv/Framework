package com.lwj.dockerdemo.user;

import com.lwj.dockerdemo.DockerdemoApplicationTests;
import com.lwj.dockerdemo.entity.Tbuser;
import com.lwj.dockerdemo.service.UserService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

public class TestUser extends DockerdemoApplicationTests {

    @Resource
    private UserService user;


    @Test
    public void testTbuserInit() {

    }


    @Test
    public void testQueryUsers() {
        this.user.queryUsers();
    }

    @Test
    public void testLogin() {
        Tbuser u = new Tbuser();
        u.setUsername("admin");
        u.setPassword("1234");
        Tbuser tbuser = this.user.login(u);
    }

    @Test
    public void testQueryUserById() {
        int userid=3;
        Tbuser user = this.user.queryUserByid(userid);
    }
}
