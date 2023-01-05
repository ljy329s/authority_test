package com.ljy.authority.authority_test.config;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ljy.authority.authority_test.auth.PrincipalDetails;
import com.ljy.authority.authority_test.model.domain.Users;
import com.ljy.authority.authority_test.model.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 인가처리

/**
 * Authorization 곧 인가를 위한 JwtAuthorizationFilter 는 BasicAuthenticationFilter 를 상속받아 구현
 * 먼저 헤더에 발급했던 토큰이 있는지 확인한다. 토큰이 없으면 별도 처리없이 해당 필터를 넘긴다.
 * 이경우 해당 요청은 아무런 권한을 얻지 못하게 되고 만약 해당 요청에 특정 권한이 요구된다면 springSecurity의 다른 여러 필터들을 거쳐
 * 결국 예외를 던지고 AuthenticationEntryPoint에서 해당 예외를 처리하게된다.
 */
@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;
    
    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
    
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        try {
            String jwtToken = request.getHeader(JwtProperties.HEADER_STRING).replace(JwtProperties.TOKEN_PREFIX, "");
            
            //토큰이 있다면 해당 토큰을 확인하는 절차를 가진다.
            //만약 토큰을 verify하는데 실패한다면 JWTVerificationException 던져 해당 요청역시 존재하지 않는경우와 동일하게 처리된다.
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512("secretKey")).build().verify(jwtToken);
    
            // 토큰 검증 (이게 인증이기 때문에 AuthenticationManager도 필요 없음)
            // 내가 SecurityContext에 직접 접근해서 세션을 만들때 자동으로 UserDetailsService에 있는 loadByUsername이 호출됨.
            String userId = decodedJWT.getClaim("username").toString();
            Users user = userRepository.selectUser(userId);
            if (user == null) {
                new JWTVerificationException("유효하지 않은 토큰입니다");
            }
            // 인증은 토큰 검증시 끝. 인증을 하기 위해서가 아닌 스프링 시큐리티가 수행해주는 권한 처리를 위해
            // 토큰을 만들어서 Authentication 객체를 강제로 만듬
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());//principalDetails, //나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                                                                                                            // null 패스워드는 모르니까 null 처리, 어차피 지금 인증하는게 아니니까!!
            // 토큰이 유효하다면 인증객체를 만들고 강제로 시큐리티의 세션에 접근하여 값 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            /**
             * 세션에 값을 저장한다면 Stateless 라는 jwt의 장점이 사라진다고 생각할수있지만
             * 세션에 저장되는 인증정보는 권한확인용으로만 사용되고
             * 해당요청이 완료되면 더이상 사용되지 않고 사라지기 때문에 문제 발생이 없다.
             *
             * spring security 다른 필터들에서 시큐리티 세션에 저장된 인증정보와
             * securityConfig 에서 설정한 각 요청들에 대한 필요 권한을 비교해보고
             * 권한이 일치하지 않으면 예외를 던져 AccessDeniedHandler 에서 처리하게 한다
             * 권한검증을 통과한다면 컨트롤러까지 전달될것
             */
            
            
        }catch (JWTVerificationException e){
            System.out.println("파싱실패");
            log.warn("[JwtAuthorizationFilter]token 파싱실패 : {}",e.getMessage());
        }
        chain.doFilter(request, response);
    }

}
