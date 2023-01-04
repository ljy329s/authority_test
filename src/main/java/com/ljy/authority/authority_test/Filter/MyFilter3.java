package com.ljy.authority.authority_test.Filter;


import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 필터3 시큐리티가 동작하기 전에 실행
 */

//@Component
//@Order(-1) //FilterConfig에서 순서를 정하지 않고 어노테이션으로 순서 정할수있다 이땐 꼭 Component어노테이션도 적어줘야함
public class MyFilter3 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        //테스트 위함 /다운캐스팅해서
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        //1차 요청메소드 파악 무조건 post일때 동작하는 메서드
        if(req.getMethod().equals("POST")){
            //메소드가 post이면 그때만동작
            String headerAuth = req.getHeader("Authorization");
            System.out.println(headerAuth);
            System.out.println("필터3");
            //2차 토큰내부 문자열 파악
            if(headerAuth.equals("cos")){
                chain.doFilter(req,res);
            }else {//post요청인데 토큰이 불일치 한다면
                PrintWriter out = res.getWriter();
                out.println("인증안됨");
            }
        }else{
            PrintWriter out = res.getWriter();
            out.println("No!!!post");
        }


        //chain.doFilter(req,res);//필터체인에 등록 만든필터에서 끝내지 않고 이어서 동작하기 위해 필터체인에 걸어줘야함
                                        //SecurityConfig에 http.addFilterAfter혹은 httpFilterBefor에 걸어야함
    }
}
