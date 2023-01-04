package com.ljy.authority.authority_test.config;

// 토큰 생성, 검증
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

       public String createJwt(int id){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam("type","jwt")
                .claim("id", id)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+1*(1000*60*60*24*365)))
               // .signWith(SignatureAlgorithm.HS256, SecretKey.JWT_SECRET_KEY)
                .compact();
    }
}