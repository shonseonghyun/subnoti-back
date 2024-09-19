package com.sunghyun.football.domain.member.infrastructure;

import com.sunghyun.football.domain.member.domain.RefreshTokenRedis;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenCrudRepository extends CrudRepository<RefreshTokenRedis,String> {
    Optional<RefreshTokenRedis> findByRefreshToken(String refreshToken);
}
