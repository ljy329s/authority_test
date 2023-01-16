package com.ljy.authority.authority_test.model.common;

import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;

public interface JwtYmlInterface {
    
        final String SECRET = "VlwEyVBsYt9V7zq57TejMnVUyzblYcfPQye08f7MGVA9XkHa"; // 우리 서버만 알고 있는 비밀값
        final String TOKEN_PREFIX = "Bearer ";
        final String HEADER_STRING = "Authorization";
    
    //test를 위한 임시
    /**
     * 엑세스토큰 만료시간 : 1분
     */
     final long ACCESS_TOKEN_TIME = Duration.ofMinutes(5).toMillis();//만료시간 30분
    
    /**
     //     * 리프레시토큰 만료시간 : 5분
     //     */
    final long REFRESH_TOKEN_TIME = Duration.ofMinutes(5).toMillis();
}
