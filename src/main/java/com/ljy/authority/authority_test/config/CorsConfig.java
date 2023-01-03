package com.ljy.authority.authority_test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 이렇게 작성만 하는건 의미가 없고 필터에 걸어줘야한다.
 * SecurityConfig에서 addFilter()로 등록
 */
@Configuration
public class CorsConfig {

    /**
     * 스프링프레임워크가 가지고 있는 cors필터
     */
    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);//내서버가 응답할때 json을 자바스크립트에서 처리할수있게 할지를 설정하는것(엑시옥스나 아작스 같은경우! 프론트에서 요청할때 받아주는걸 허용) 만약 false로 하면 프론트에서 요청해도 받을수없다
        config.addAllowedOrigin("*");//모든 ip에 응답을 허용하겠다.
        config.addAllowedHeader("*");//모든 헤더를 응답을 허용
        config.addAllowedMethod("*");//모든 post,get,delte, patch등 메소드 다 허용
        source.registerCorsConfiguration("/api/**",config);//소스에다가 등록 /api/** 로 들어오는 모든 주소는 이 컨피그 설정을 따라라

        return new CorsFilter(source);
    }
}
