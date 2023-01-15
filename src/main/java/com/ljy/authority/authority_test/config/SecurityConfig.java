package com.ljy.authority.authority_test.config;

import com.ljy.authority.authority_test.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@EnableGlobalAuthentication
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;//CorsConfig 필터?메소드를 빈으로 등록했기에

    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);


        // http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);//뭐가 실행되기 전에 필터를 탈껀지 (FilterConfig에서 설정하기위해 주석)//SecurityContextPersistenceFilter.class Deplecate됐다. 다른방법..
        return http
                    .addFilter(corsConfig.corsFilter())
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션을 사용하지 않겠다
                    .and()
                    .formLogin().disable()//폼로그인을 사용하지 않겠다. jwt방식을 할거니까
                    .httpBasic().disable()//기본적인 http방식을 안씀.일반적인 루트가 아닌 다른방식으로 요청시 거절, headerdp id , pw가 아닌 token을 달고 간다. 그래서 basic(id,비번을 들고 요청)이 아닌 bearer(토큰을 들고요청) 사용.
                .addFilter(new JwtAuthenticationFilter(authenticationManager))
                .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository))

                //.apply(new MyCustomDsl()) // 커스텀 필터 등록
                    //.addFilter(corsFilter)//이렇게 등록하면 cors정책에서 피할수있다.CorsConfig에서 만든 필터 cross orgin요청이 와도 다 허용
                    //.addFilter(new JwtAuthenticationFilter(authenticationManager))//이필터쓸때 꼭 넘겨야하는 파라미터 AuthenticationManger! 얘가 로그인을 진행하는 필터이기 때문
                    .and()
                    .authorizeRequests()
                            . antMatchers("/api/vi/user/**")
                            .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                            .antMatchers("/api/v1/manager/**")
                            .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                            .antMatchers("/api/v1/admin/**")
                            .access("hasRole('ROLE_ADMIN')")
                            .anyRequest().permitAll();
                http.build();

    }


//    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
//            http
//                    .addFilter(corsConfig.corsFilter())
//                    .addFilter(new JwtAuthenticationFilter(authenticationManager))
//                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
//        }
//    }

}