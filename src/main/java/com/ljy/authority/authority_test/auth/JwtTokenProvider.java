package com.ljy.authority.authority_test.auth;

// 토큰 생성, 검증

import com.ljy.authority.authority_test.model.common.JwtYml;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
 
    private final Key key;
    //private final JwtYml jwtYml;
    
    //String secretKey = jwtYml.getSecretKey();
    public JwtTokenProvider(){
        byte[] keyBytes = Decoders.BASE64.decode("");
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }
    

    
}