package com.ljy.authority.authority_test.config;

public interface JwtProperties {
    String SECRET = "ddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddd"; // 우리 서버만 알고 있는 비밀값
    int EXPIRATION_TIME = 6000;
    String TOKEN_PREFIX = "Bearer";
    String HEADER_STRING = "Authorization";
}
