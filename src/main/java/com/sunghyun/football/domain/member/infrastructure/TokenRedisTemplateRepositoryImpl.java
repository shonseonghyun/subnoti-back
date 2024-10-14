package com.sunghyun.football.domain.member.infrastructure;

import com.sunghyun.football.domain.member.domain.RefreshTokenRedis;
import com.sunghyun.football.domain.member.domain.repository.TokenRepository;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.member.auth.jwt.JwtNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
public class TokenRedisTemplateRepositoryImpl implements TokenRepository {
    private final RedisTemplate redisTemplate;

    public void save(final RefreshTokenRedis refreshTokenRedis){
        ValueOperations<String, String> valueOperations= redisTemplate.opsForValue();
        valueOperations.set(refreshTokenRedis.getRefreshToken(),refreshTokenRedis.getEmail());
        redisTemplate.expire(refreshTokenRedis.getRefreshToken(), 60L, TimeUnit.SECONDS);
    }

    @Override
    public RefreshTokenRedis findByRefreshToken(String refreshToken) {
        ValueOperations<String, String> valueOperations= redisTemplate.opsForValue();
        String email = valueOperations.get(refreshToken);
        Long remainMs = redisTemplate.getExpire(refreshToken); //토큰 만료까지 남은 ms, ttl만료 시 -2 리턴, ttl이 걸려있지않다면 -1

        if(Objects.isNull(email)){
            throw new JwtNotFoundException(ErrorCode.JWT_NOT_FOUND);
        }

        return new RefreshTokenRedis(refreshToken,email);
    }


}
