package com.sunghyun.football.domain.member.infrastructure.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtProvider {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expirationTime}")
    private Long expirationTime;

    public String generateAccessToken(String email){
        long now = System.currentTimeMillis();

        Claims claims = Jwts.claims();
        claims.put("email",email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims parseAccessToken(String token){
        String accessTokenWithoutPrefix = token.split(" ")[1];
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(accessTokenWithoutPrefix)
                .getBody()
                ;
    }

    public String getEmail(String token){
        Claims claims = parseAccessToken(token);
        return claims.get("email",String.class);
    }
}
