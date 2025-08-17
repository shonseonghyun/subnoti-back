package com.sunghyun.football.domain.member.api;

import com.sunghyun.football.domain.member.application.dto.TokenResDto;
import com.sunghyun.football.domain.member.infrastructure.auth.jwt.JwtProvider;
import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.response.ApiResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthApi {
    private final JwtProvider jwtProvider;

    @PostMapping("/reissue")
    public ApiResponseDto issueNewAccessToken(@RequestBody Map<String,String> map){
        final String refreshToken = map.get("refreshToken");

        String newAccessToken = jwtProvider.generateNewAccessTokenWithRefreshToken(refreshToken);
//        String newAccessToken = jwtProvider.generateNewAccessTokenWithRefreshToken(refreshToken);

        return ApiResponseDto.toResponse(ErrorType.SUCCESS,new TokenResDto(newAccessToken,refreshToken));
    }
}
