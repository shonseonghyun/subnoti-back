package com.sunghyun.football.domain.member.domain.repository;

import com.sunghyun.football.domain.member.domain.RefreshTokenRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<RefreshTokenRedis,String> {
//    Optional<RefreshToken> findByEmail(String email);
    Optional<RefreshTokenRedis> findByRefreshToken(String refreshToken);
}
