package com.ljy.authority.authority_test.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.authority.authority_test.auth.PrincipalDetails;
import com.ljy.authority.authority_test.model.domain.Login;
import com.ljy.authority.authority_test.model.domain.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authentication 즉 인증에 성공했을때 실행되는 핸들러
 */
public class AuthenticationSuccessHandlerCustom implements AuthenticationSuccessHandler {
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        Users user = principalDetails.getUser();// 로그인 요청이 성공할때 클라이언트가 받을 데이터를 담는 dto를 만들어서 달아주자 지금은 임시로 UsersDto 를 사용 JWT payload에 해당 정보들을 담을 수 있지만 일단은 response body에 해당 정보를 담게끔 구현하였다.)
        convertObjectToJson(user);
        
   
    }
    public String convertObjectToJson(Object object)throws JsonProcessingException{
        
           if(object == null){
            System.out.println("로그인실패");
            return null;
        }
        
        ObjectMapper om = new ObjectMapper();//ObjectMapper 는 jackson 라이브러리
        return om.writeValueAsString(object);
        
    }
}
