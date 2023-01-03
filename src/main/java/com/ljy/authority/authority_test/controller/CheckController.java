package com.ljy.authority.authority_test.controller;

import org.springframework.web.bind.annotation.*;

public class CheckController {
    @GetMapping("/products")
    public String selectProduct(){
        System.out.println("조회");
        return "<h1>조회</h1>";
    }
    @PostMapping("/products")
    public String insertProduct(){
        System.out.println("등록");
        return"<h1>등록</h1>";
    }
    
    @PatchMapping("products")
    public String updateProduct(){
        System.out.println("수정");
        return "<h1>수정</h1>";
    }
    
    @DeleteMapping("/products")
    public String product(){
        System.out.println("삭제");
        return "<h1>삭제</h1>";
    }
    
}
