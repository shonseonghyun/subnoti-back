package com.sunghyun.football.domain.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken",timeToLive = 6 /* *14 */ )
//@RedisHash(value = "refreshToken",timeToLive = 60 * 60 * 24 /* *14 */ )
@AllArgsConstructor
@Getter
public class RefreshTokenRedis {

    @Id
    private String refreshToken;

    private String email;

//    @TimeToLive
//    private Long expiration;
}

