package com.sunghyun.football.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

//@RedisHash(value = "refreshToken",timeToLive = 60 /* *14 */ )
@RedisHash(value = "refreshToken",timeToLive = 60 * 60 * 24 * 2)
@AllArgsConstructor
@Getter
public class RefreshTokenRedis {

    @Id
    private String refreshToken;

    private String email;

//    @TimeToLive
//    private Long expiration;
}

