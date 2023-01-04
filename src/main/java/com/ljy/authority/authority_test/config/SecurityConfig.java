package com.ljy.authority.authority_test.config;

import com.ljy.authority.authority_test.Filter.MyFilter1;
import com.ljy.authority.authority_test.Filter.MyFilter3;
import com.ljy.authority.authority_test.Filter.MyFilter4;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalAuthentication
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsFilter corsFilter;//CorsConfig 필터?메소드를 빈으로 등록했기에

    private final JwtTokenProvider jwtTokenProvider;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        
        // http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);//뭐가 실행되기 전에 필터를 탈껀지 (FilterConfig에서 설정하기위해 주석)//SecurityContextPersistenceFilter.class Deplecate됐다. 다른방법..
        http.csrf().disable();
        http.addFilterBefore(new MyFilter4(), BasicAuthenticationFilter.class);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션을 사용하지 않겠다
                .and()
                .addFilter(corsFilter)//이렇게 등록하면 cors정책에서 피할수있다.CorsConfig에서 만든 필터 cross orgin요청이 와도 다 허용
                .formLogin().disable()//폼로그인을 사용하지 않겠다. jwt방식을 할거니까
                .httpBasic().disable()//기본적인 http방식을 안씀.일반적인 루트가 아닌 다른방식으로 요청시 거절, headerdp id , pw가 아닌 token을 달고 간다. 그래서 basic(id,비번을 들고 요청)이 아닌 bearer(토큰을 들고요청) 사용.
                //.addFilter(new JwtAuthenticationFilter(authenticationManager()))//이필터쓸때 꼭 넘겨야하는 파라미터 AuthenticationManger! 얘가 로그인을 진행하는 필터이기 때문
              //  .apply(new MyCustomDsl()) // 커스텀 필터 등록
                //.and()
                .authorizeRequests()//요청에 대한 사용권한 체크
                .antMatchers("/api/v1/user/**").access("hasRole('ROLE_CUD') or hasRole('ROLE_MASTER')")
                .antMatchers("/api/**").access("hasRole('ROLE_MASTER')")
                .anyRequest().permitAll();
        return http.build();

    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
    //http.addFilterBefore(new MyFilter1(),BasicAuthenticationFilter.class)
    //BasicAuthenticationFilter가 동작하기 전에 MyFilter1이 동작
    //시큐리티컨피그에서 안적어주고 FilterConfig에서 설정하는 방법도 있다.

//    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
//            http
//                    .addFilter(new JwtAuthenticationFilter(authenticationManager));
//        }
//    }
}
