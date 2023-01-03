package com.ljy.authority.authority_test.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
public class SecurityConfig{

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        

        setAntMatcher(http,"ROLE_");//메소드로 권한 검증

        http.csrf().disable();
        http.httpBasic().disable()//일반적인 루트가 아닌 다른방식으로 요청시 거절, headerdp id , pw가 아닌 token을 달고 간다. 그래서 basic이 아닌 bearer 사용.
            .authorizeRequests()//요청에 대한 사용권한 체크
            .antMatchers().permitAll()//임시로 모든 권한 허용
            .and()
            .formLogin()
            .defaultSuccessUrl("/", true)
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//세션사용 막음
        
            //usernamePasswordAuthenticationFilter에 도달하기전에 커스텀한 필터를 먼저 동작시킴
            
        
            return http.build();
    }


    //이후 시큐리티 사이에서 작용하는 필터로 변경해주기
    private void setAntMatcher(HttpSecurity http, String role) {
    }
}
