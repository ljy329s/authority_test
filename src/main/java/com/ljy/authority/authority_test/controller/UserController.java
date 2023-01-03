package com.ljy.authority.authority_test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/")
    public String yungjin(){
        System.out.println("메인화면");
        return "<h1>hello</h1>";
    }
}
