package com.ljy.authority.authority_test.controller;

import org.springframework.web.bind.annotation.*;


/**
 * 권한별로 인가 처리가 잘 되는지 확인용 컨트롤러
 */
@RestController
public class CheckController {
    @GetMapping("/products")
    public String selectProduct(){
        System.out.println("조회");
        return "<h1>조회</h1>";
    }
    @PostMapping("/products")//admin에게만 권한
    public String insertProduct(){
        System.out.println("등록");
        return"<h1>등록</h1>";
    }
    
    @PatchMapping("/products")
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
