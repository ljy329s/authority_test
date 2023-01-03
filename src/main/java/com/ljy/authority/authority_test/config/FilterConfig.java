package com.ljy.authority.authority_test.config;

import com.ljy.authority.authority_test.Filter.MyFilter1;
import com.ljy.authority.authority_test.Filter.MyFilter2;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 필터 관리 SecurityConfig사이에 안적고 여기서 적는 방법
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<MyFilter1> filter1(){
        FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
        bean.addUrlPatterns("/*");//이 필터가 허용하는 url
        bean.setOrder(0);//필터 실행 순서! 낮은번호가 가장 먼저 실행된다.
        return bean;
    }


    @Bean
    public FilterRegistrationBean<MyFilter2> filter2(){
        FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
        bean.addUrlPatterns("/*");//이 필터가 허용하는 url
        bean.setOrder(1);//필터 실행 순서! 낮은번호가 가장 먼저 실행된다.
        return bean;
    }

    /**
     * 알아둘점
     * SecurityConfig가 다 실행된 이후 여기서 설정한 필터가 실행된다
     * 시큐리티 필터체인이 커스텀 필터보다 먼저 동작하기때문
     * 먼저 실행하고 싶으면 시큐리티 컨피그에서 http.addFilterBefore를 사용해서 필터체인에서 가장먼저 시작되는 필터보다 먼저 동작하게 설정하면된다.
     * SecurityContextPersistenceFiler(필터체인 가장 앞에 있는필터)
     * 
     * 그리고 시큐리티기가 다 동작한 이후 FilterConfig에 설정해둔 순서대로 필터가 동작
     */
}
