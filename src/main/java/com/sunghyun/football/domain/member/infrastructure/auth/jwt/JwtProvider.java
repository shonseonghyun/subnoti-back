package com.sunghyun.football.domain.member.infrastructure.auth.jwt;

import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.member.auth.jwt.JwtExpiredException;
import com.sunghyun.football.global.exception.exceptions.member.auth.jwt.JwtParseException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
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
        if(!token.startsWith("Bearer")){
            log.info("토큰 Bearer 형식으로 이루어져 있지 않습니다.");
            throw new JwtParseException(ErrorCode.JWT_PARSE_FAIL);
        }
        String accessTokenWithoutPrefix = token.split(" ")[1];
        try{
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(accessTokenWithoutPrefix)
                    .getBody()
                    ;
        }catch(SignatureException | MalformedJwtException |IllegalArgumentException e){
            log.error("토큰 형식이 올바르지 않습니다.");
            throw new JwtParseException(ErrorCode.JWT_PARSE_FAIL);
        }catch(ExpiredJwtException e){
            log.error("토큰 유효기간 만료");
            throw new JwtExpiredException(ErrorCode.JWT_EXPIRED);
        }
    }

    public String getEmail(String token){
        Claims claims = parseAccessToken(token);
        return claims.get("email",String.class);
    }
}
