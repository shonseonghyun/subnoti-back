package com.sunghyun.football.domain.member.domain.repository;

import com.sunghyun.football.domain.member.domain.RefreshTokenRedis;

public interface TokenRepository {
    void save(RefreshTokenRedis refreshTokenRedis);
    RefreshTokenRedis findByRefreshToken(String refreshToken);
}
