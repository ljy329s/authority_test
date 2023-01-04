package com.ljy.authority.authority_test.controller;

import com.ljy.authority.authority_test.config.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    
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
    
    //로그인
    
    
    
}
