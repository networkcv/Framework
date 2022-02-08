package com.lagou.edu.controller;

import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @author 应癫
 */
@Controller
@RequestMapping("/demo")
public class DemoController  {


    @RequestMapping("/handle01")
    public String handle01(String name,Map<String,Object> model) {
		System.out.println("++++++++handler业务逻辑处理中....");
        Date date = new Date();
        model.put("date",date);
        return "success";
    }
}
