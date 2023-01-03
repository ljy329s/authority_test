package com.ljy.authority.authority_test.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/home")
    public String yungjin(){
        System.out.println("메인화면");
        return "<h1>home</h1>";
    }
    @PostMapping("/token")
    public String token(){
        System.out.println("x토큰");
        return "<h1>token</h1>";
    }
}
