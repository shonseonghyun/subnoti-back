package com.sunghyun.football.domain.member.domain.repository;

import com.sunghyun.football.domain.member.domain.RefreshTokenRedis;

public interface TokenRedisRepository {
    void save(RefreshTokenRedis refreshTokenRedis);
    RefreshTokenRedis findByRefreshToken(String refreshToken);
}
