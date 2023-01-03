package com.ljy.authority.authority_test.Filter;


import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;


public class MyFilter2 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("필터2");
        chain.doFilter(request,response);//필터체인에 등록 만든필터에서 끝내지 않고 이어서 동작하기 위해 필터체인에 걸어줘야함
                                        //SecurityConfig에 http.addFilterAfter혹은 httpFilterBefor에 걸어야함
    }
}
