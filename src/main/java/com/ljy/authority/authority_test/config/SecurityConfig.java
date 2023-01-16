package com.ljy.authority.authority_test.config;

import com.ljy.authority.authority_test.handler.AuthenticationFailureHandlerCustom;
import com.ljy.authority.authority_test.handler.AuthenticationSuccessHandlerCustom;
import com.ljy.authority.authority_test.model.common.JwtYml;
import com.ljy.authority.authority_test.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@EnableGlobalAuthentication
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final CorsConfig corsConfig;//CorsConfig 필터?메소드를 빈으로 등록했기에
    
    private final UserRepository userRepository;
    
    private final AuthenticationConfiguration authenticationConfiguration;
    
    private final JwtYml jwtYml;
    
    /**
     * 인가 과정에서 JwtAuthorizationFilter 가 JWT 를 파싱하면서 토큰이 없거나 유효하지 않으면 authenticationEntryPoint 에서,
     * 권한 scope 가 적절하지 않은 요청이면 AccessDeniedHandler 에서 처리하도록 한다.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //정적자원에 대해서는 Security 설정을 적용하지 않는다.
        return http
            .addFilter(corsConfig.corsFilter()) //cors정책에서 피할수있다.CorsConfig에서 만든 필터사용
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션을 사용하지 않겠다
            .and()
            .formLogin().disable()//폼로그인을 사용하지 않겠다. jwt방식을 할거니까
            .httpBasic().disable()//기본적인 http방식을 안씀.일반적인 루트가 아닌 다른방식으로 요청시 거절, header 에 id , pw가 아닌 token을 달고 간다. 그래서 basic(id,비번을 들고 요청)이 아닌 bearer(토큰을 들고요청) 사용.
            .apply(new MyCustomDsl()) // 커스텀 필터 등록
            .and()
            .authorizeRequests()
            .antMatchers(HttpMethod.POST,"/product").access("hasRole('ADMIN')")//post로 들어온 /product 메서드일 경우에는 관리자 권한만 접속 가능하도록ss
            .antMatchers(HttpMethod.GET,"/product").access("hasRole('USER')")
            .antMatchers(HttpMethod.PATCH,"/product").access("hasRole('USER')")
            .antMatchers(HttpMethod.DELETE,"/product").access("hasRole('USER')")
            .antMatchers().permitAll()//나머지는 전체 허용
            .and()
            .build();
        
    }
    
    /**
     * 커스텀필터
     */
    
    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);//꼭 넘겨야하는 파라미터 AuthenticationManger! 얘가 로그인을 진행하는 필터이기 때문
            http
                .addFilter(new JwtAuthenticationFilter(authenticationManager))//인증처리
                .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));//인가처리
        }
    }
    
    
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new AuthenticationSuccessHandlerCustom();
    }
    
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new AuthenticationFailureHandlerCustom();
    }
    
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        jwtAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return jwtAuthenticationFilter;
    }
    
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManager(), userRepository);
    }
    
    /**
     * exceptionHandelrFilter 는 filter 에서 터지는 exception 들을 효율적으로 관리하기 위해 만든 filter 이다. 일단 주석처리
     *
     * ExceptionResolver 를 호출하는 DispatcherServlet 앞단에 filter 들이 위치하기 때문에
     *
     * filter 들의 exception 들을 처리할 수 있는 별도의 장치로 구현
     *
     * security 에서 적용되는 filter 중에 거의 제일 앞에 있는 LogoutFilter 앞에 적용시켜
     *
     * filter 에서 터지는 거의 모든 exception 를 관리할 수 있도록 한것.
     */

/**
 * formLogin을 사용하지 않고 disabled 하는이유
 * form으로 로그인하면 javascript로 서버에 id , pw를 요청한다.
 * 이때 javascript로 요청할경우 서버에 클라이언트가 갖고 있는 쿠키를 전송하지 못한다.
 * 대부분의 서버는 httpOnly값이 ture로 설정되어있기 때문이다.
 * 그렇다고 false로 하자니 javaScript로 요청시 보안상 좋지않아서 formLogin을 쓰지 않는다고 함
 */
}
