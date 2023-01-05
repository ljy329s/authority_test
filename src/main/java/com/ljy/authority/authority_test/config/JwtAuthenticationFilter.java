package com.ljy.authority.authority_test.config;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.ljy.authority.authority_test.auth.PrincipalDetails;
import com.ljy.authority.authority_test.config.JwtProperties;
import com.ljy.authority.authority_test.model.domain.Login;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;


/**
 * JwtAuthenticationFilter : 인증단계를 처리하는 필터
 * SpringSecurity 의 Authentication(인증) 역할을 하는 UsernamePasswordAuthenticationFilter 상속받아 구현
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    private final AuthenticationManager authenticationManager;
    
    // Authentication 객체 만들어서 리턴 => 의존 : AuthenticationManager
    // 인증 요청시에 실행되는 함수 => /login
    
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
        
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        
        // request에 있는 username과 password를 파싱해서 자바 Object로 받기
        ObjectMapper om = new ObjectMapper();
        try {
            Login login = om.readValue(request.getInputStream(), Login.class);
            // 유저네임패스워드 토큰 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword());
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // JWT Token 생성해서 response 에 담아주기
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        
        String jwtToken = JWT.create()
            .withSubject(principalDetails.getUsername())
            .withExpiresAt(new Date(System.currentTimeMillis() + (6000 * 10)))//10분 만료시간
            .withClaim("username", principalDetails.getUser().getUsername())//토큰에 담을내용
            .withClaim("roles", principalDetails.getUser().getRoles())//토큰에 담을내용
            .sign(Algorithm.HMAC512(JwtProperties.SECRET));//(Algorithm.HMAC512(JwtProperties.SECRET)// 임의로 값을 넣어둔 파일임 yml에서 가져오는 시크릿키로 바꿔주기
        System.out.println("jwt토큰 : "+ jwtToken);
        response.addHeader(JwtProperties.HEADER_STRING,JwtProperties.TOKEN_PREFIX + jwtToken);
        this.getSuccessHandler().onAuthenticationSuccess(request,response,authResult);
    }
    /**
     * 이랗게 구현하게 되면 클라이언트가 POST /login 요청을 했을때  attemptAuthentication 메소드가 동작한다.
     * 메소드 안에서 ObjectMapper 를 이용해서 request 에 담겨있는 사용자 정보를 객체에 담는다(Login 객체);
     * 스프링 시큐리티에서 login 정보를 알수있게 token 화 시키고 해당 token 으로 authenticationManager 를 이용하여 인증을 진행한다.
     * authenticationManager.authenticate 메소드는 PrincipalDetailService 에 userRepository.selectUser(username) 메소드를 호출하여
     * db정보와 요청정보가 일치하는지 확인한다.
     * 만약 두정보가 일치하지 않아 실패한다면 Authen
     */
    
}
