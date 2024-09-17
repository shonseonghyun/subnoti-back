package com.sunghyun.football.domain.member.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResDto {
    private String accessToken;
    private String refreshToken;
}
