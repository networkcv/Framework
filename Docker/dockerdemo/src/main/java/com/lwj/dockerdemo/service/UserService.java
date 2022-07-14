package com.lwj.dockerdemo.service;

import com.lwj.dockerdemo.entity.Tbuser;

import java.util.List;

public interface UserService {

    List<Tbuser> queryUsers();

    Tbuser login(Tbuser user);
    Tbuser queryUserByid(Integer userid);
}
