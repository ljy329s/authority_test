package com.ljy.authority.authority_test.Filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 예외를 핸들링하는 Filter
 * ExpiredJwtException, JwtException, IllegalArgumentException 중 하나의 예외가 발생했을 경우 에러 코드와 메시지를 세팅한 Response 를 응답하는 예시
 * 여기서 만든 필터를 시큐리티 필터 상위에서 핸들링하도 추가해줘야한다
 */
//public class ExceptionHandlerFilter extends OncePerRequestFilter {
//    @Override
//    protected void doFilterInternal(
//        HttpServletRequest request,
//        HttpServletResponse response,
//        FilterChain filterChain
//    ) throws ServletException, IOException {
//
//        try {
//            filterChain.doFilter(request, response);
//        } catch (ExpiredJwtException e) {
//            //토큰의 유효기간 만료
//            setErrorResponse(response, ErrorCode.TOKEN_EXPIRED);
//        } catch (JwtException | IllegalArgumentException e) {
//            //유효하지 않은 토큰
//            setErrorResponse(response, ErrorCode.INVALID_TOKEN);
//        }
//    }
//
//    private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        response.setStatus(errorCode.getHttpStatus().value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
//        try {
//            response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
//        } catch (IOException e) {
//            e.printStackTrace();
//       }
 //  }
    
//    @Data
//    public static class ErrorResponse {
//        private final Integer code;
//        private final String message;
//  //  }
//}
