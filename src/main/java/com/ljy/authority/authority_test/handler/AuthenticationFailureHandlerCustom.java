package com.ljy.authority.authority_test.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Authentication 즉 인증에 실패했을경우 실행되는 핸들러
 * 단순히 서버에 로그를 남기고 클라이언트에게 Unauthorized(401) error 반환
 */

@Slf4j
public class AuthenticationFailureHandlerCustom implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.warn("Authentication Error: {}", exception.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
