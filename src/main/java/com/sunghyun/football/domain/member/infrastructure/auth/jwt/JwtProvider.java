package com.sunghyun.football.domain.member.infrastructure.auth.jwt;

import com.sunghyun.football.domain.member.domain.RefreshTokenRedis;
import com.sunghyun.football.domain.member.domain.repository.TokenRedisRepository;
import com.sunghyun.football.domain.member.domain.repository.TokenRepository;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.member.auth.jwt.JwtExpiredException;
import com.sunghyun.football.global.exception.exceptions.member.auth.jwt.JwtNotFoundException;
import com.sunghyun.football.global.exception.exceptions.member.auth.jwt.JwtParseException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expirationTime}")
    private Long accessTokenExpirationTime;

    @Value("${jwt.refresh.expirationTime}")
    private Long refreshTokenExpirationTime;

//    private final TokenRepository tokenRepository;
    private final TokenRedisRepository tokenRepository;

//    public boolean checkRefreshTokenExist(final String refreshToken){
//        Optional<RefreshTokenRedis> token = tokenRepository.findByRefreshToken(refreshToken);
//        if(token.isPresent()){
//            log.info("token 존재");
//        }
//        else{
//            log.info("token 존재하지 않음");
//        }
//        return true;
//    }

    public String generateAccessToken(final String email){
        long now = System.currentTimeMillis();

        Claims claims = Jwts.claims();
        claims.put("email",email);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + accessTokenExpirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Claims parseAccessToken(final String token){
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

    public String getEmail(final String token){
        Claims claims = parseAccessToken(token);
        return claims.get("email",String.class);
    }

    public String generateRefreshToken() {
        long now = System.currentTimeMillis();

        Claims claims = Jwts.claims();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + refreshTokenExpirationTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String generateNewAccessTokenWithRefreshToken(final String refreshToken) {
        final RefreshTokenRedis refreshTokenRedis = tokenRepository.findByRefreshToken(refreshToken);
        final String email = refreshTokenRedis.getEmail();

        log.info("리프래쉬 토큰을 통한 유저 이메일 추출 [이메일: {}]",email);

        String newAccessToken = generateAccessToken(email);

        log.info("New AccessToken is issued [{}]", newAccessToken);

        return newAccessToken;
    }
}
