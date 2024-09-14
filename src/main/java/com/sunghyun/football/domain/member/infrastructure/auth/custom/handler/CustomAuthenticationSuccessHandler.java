package com.sunghyun.football.domain.member.infrastructure.auth.custom.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.response.ApiResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper om;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        //토큰 생성
        final String accessToken="accessToken";
        final String refreshToken="refreshToken";


        //유저 정보와 토큰 응답 필수
        /*
            유저 정보는 body 담아 전달
            토큰은 쿠키를 통하여 전달
        */
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // application/json

        //토큰 쿠키
        Cookie cookieForAccessToken  = new Cookie("accessToken",accessToken);
        Cookie cookieForRefreshToken  = new Cookie("refreshToken",refreshToken);

        response.addCookie(cookieForAccessToken);
        response.addCookie(cookieForRefreshToken);

        //유저정보body
        om.writeValue(response.getOutputStream(), ApiResponseDto.toResponse(ErrorCode.SUCCESS));



    }
}
