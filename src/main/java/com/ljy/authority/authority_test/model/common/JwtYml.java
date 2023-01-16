package com.ljy.authority.authority_test.model.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Getter
@Component
public class JwtYml {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.response-header}")
    private String jwtHeader;

    @Value("${jwt.prefix}")
    private String jwtTokenPrefix;

    //test를 위한 임시
    /**
     * 엑세스토큰 만료시간 : 1분
     */
    private long accessTokenValidTime = Duration.ofMinutes(5).toMillis();//만료시간 30분

    /**
     //     * 리프레시토큰 만료시간 : 5분
     //     */
    private long refreshTokenValidTime = Duration.ofMinutes(5).toMillis();
   
    
}
