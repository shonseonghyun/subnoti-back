package com.sunghyun.football.domain.member.infrastructure.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtProvider {
    private final String secretKey= "secretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKeysecretKey";

    private final Long tokenValidTime = 1000 * 60 * 24 *30 * 60 * 24L;

    public String generateAccessToken(String email){
        long now = System.currentTimeMillis();

        Claims claims = Jwts.claims();
        claims.put("email",email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now - tokenValidTime))
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
