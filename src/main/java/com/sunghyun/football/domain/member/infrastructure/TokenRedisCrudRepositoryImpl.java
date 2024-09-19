package com.sunghyun.football.domain.member.infrastructure;

import com.sunghyun.football.domain.member.domain.RefreshTokenRedis;
import com.sunghyun.football.domain.member.domain.repository.TokenRepository;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.member.auth.jwt.JwtNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class TokenRedisCrudRepositoryImpl implements TokenRepository {
    private final TokenCrudRepository tokenCrudRepository;

    @Override
    public void save(RefreshTokenRedis refreshTokenRedis) {
        tokenCrudRepository.save(refreshTokenRedis);

    }

    @Override
    public RefreshTokenRedis findByRefreshToken(String refreshToken) {
        return tokenCrudRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()->new JwtNotFoundException(ErrorCode.JWT_NOT_FOUND));
    }
}
