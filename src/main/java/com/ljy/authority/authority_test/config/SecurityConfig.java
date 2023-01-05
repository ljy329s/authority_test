package com.ljy.authority.authority_test.config;

import com.ljy.authority.authority_test.handler.AuthenticationFailureHandlerCustom;
import com.ljy.authority.authority_test.handler.AuthenticationSuccessHandlerCustom;
import com.ljy.authority.authority_test.model.repository.UserRepository;
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
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity // 시큐리티 활성화 -> 기본 스프링 필터체인에 등록
@EnableGlobalAuthentication
@RequiredArgsConstructor
public class SecurityConfig {

    private final CorsConfig corsConfig;//CorsConfig 필터?메소드를 빈으로 등록했기에

    private final UserRepository userRepository;
    
    private final AuthenticationConfiguration authenticationConfiguration;
//    private final AccessDeniedHandler accessDeniedHandler;
    
  //  private final AuthenticationEntryPoint authenticationEntryPoint;

    //정적자원에 대해서는 Security 설정을 적용하지 않는다.
    
    /**
     * 인가 과정에서 JwtAuthorizationFilter 가 JWT 를 파싱하면서 토큰이 없거나 유효하지 않으면 authenticationEntryPoint 에서,
     * 권한 scope 가 적절하지 않은 요청이면 AccessDeniedHandler 에서 처리하도록 한다.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);


        // http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);//뭐가 실행되기 전에 필터를 탈껀지 (FilterConfig에서 설정하기위해 주석)//SecurityContextPersistenceFilter.class Deplecate됐다. 다른방법..
        return http
                    .csrf().disable()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//세션을 사용하지 않겠다
                    .and()
//                    .formLogin().disable()//폼로그인을 사용하지 않겠다. jwt방식을 할거니까
//            .formLogin()ßß
//        .defaultSuccessUrl("/home")
//            .and()
            .httpBasic().disable()//기본적인 http방식을 안씀.일반적인 루트가 아닌 다른방식으로 요청시 거절, headerdp id , pw가 아닌 token을 달고 간다. 그래서 basic(id,비번을 들고 요청)이 아닌 bearer(토큰을 들고요청) 사용.
                    //.addFilterBefore(exceptionHandlerFilter(), LogoutFilter.class)// 이 필터는 좀 더 구현에대해 많이 생각해봐야 할 것 같아서 일단 주석처리함
                    .exceptionHandling()
                    //.authenticationEntryPoint(authenticationEntryPoint)
                    .and()
                    .apply(new MyCustomDsl()) // 커스텀 필터 등록
                    //.addFilter(corsFilter)//이렇게 등록하면 cors정책에서 피할수있다.CorsConfig에서 만든 필터 cross orgin요청이 와도 다 허용
                    //.addFilter(new JwtAuthenticationFilter(authenticationManager))//이필터쓸때 꼭 넘겨야하는 파라미터 AuthenticationManger! 얘가 로그인을 진행하는 필터이기 때문
                    .and()
                    .authorizeRequests(authorize -> authorize.antMatchers("/","/**").permitAll()
                            . antMatchers("/api/vi/user/**")
                            .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                            .antMatchers("/api/v1/manager/**")
                            .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                            .antMatchers("/api/v1/admin/**")
                            .access("hasRole('ROLE_ADMIN')")
                            .anyRequest().permitAll())
                .build();

    }


    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);//꼭 넘겨야하는 파라미터 AuthenticationManger! 얘가 로그인을 진행하는 필터이기 때문
            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, userRepository));
        }
    }
    
    
//    @Bean
//    ExceptionHandlerFilter exceptionHandlerFilter() {
//        return new ExceptionHandlerFilter();
//    }
    
    
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
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
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManagerBean());
        jwtAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler());
        jwtAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return jwtAuthenticationFilter;
    }
    
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() throws Exception {
        return new JwtAuthorizationFilter(authenticationManagerBean(), userRepository);
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
//상태코드 Unauthorized(401)는 이름에서 많이들 오해를 하지만 인가실패가 아닌 인증단계에서 에러가 있을때 주로 사용된다.
//Forbidden(403)이 인가단계에서 실패하였을때 사용되는데 주로 사용자 권한의 scope와 요청에 필요한 권한이 일치하지 않을때 사용된다.
/**
 * formLogin을 사용하지 않고 disabled 하는이유
 * form으로 로그인하면 javascript로 서버에 id , pw를 요청한다.
 * 이때 javascript로 요청할경우 서버에 클라이언트가 갖고 있는 쿠키를 전송하지 못한다.
 * 대부분의 서버는 httpOnly값이 ture로 설정되어있기 때문이다.
 * 그렇다고 false로 하자니 javaScript로 요청시 보안상 좋지않아서 formLogin을 쓰지 않는다고 함
 */
}
