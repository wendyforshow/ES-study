package com.kh.search.controller;

import com.kh.search.model.KhApi;
import com.kh.search.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 所在的包名: com.kh.search.controller
 * 所在的项目名：kh-search
 *
 * @Author:xukh
 * @Description:
 * @Date: Created in 19:19 2018/6/13
 */
@RestController
public class TestController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/")
    public KhApi s(){
        return userService.getOne();
    }
}
