package com.sunghyun.football.domain.member.infrastructure.auth.custom.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.football.domain.member.domain.RefreshTokenRedis;
import com.sunghyun.football.domain.member.domain.repository.TokenRepository;
import com.sunghyun.football.domain.member.infrastructure.auth.UserDetails.CustomUserDetails;
import com.sunghyun.football.domain.member.infrastructure.auth.dto.MemberLoginResDto;
import com.sunghyun.football.domain.member.infrastructure.auth.jwt.JwtProvider;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.response.ApiResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Value("${IP_ADDRESS:'localhost'}")
    String domain;

    private final ObjectMapper om;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        final String email = customUserDetails.getUsername();
        log.info("로그인 성공 [email:{}]",email);

        //토큰 생성
        final String accessToken=jwtProvider.generateAccessToken(email);
        final String refreshToken=jwtProvider.generateRefreshToken();

        log.info("AccessToken is issued [{}]", accessToken);
        log.info("RefreshToken is issued [{}]", refreshToken);

        //유저 정보와 토큰 응답 필수
        /*
            유저 정보는 body 담아 전달
            토큰은 쿠키를 통하여 전달
        */
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // application/json

        //토큰 쿠키
        Cookie cookieForAccessToken  = new Cookie("accessToken",accessToken);
        Cookie cookieForRefreshToken  = new Cookie("refreshToken",refreshToken);
        cookieForAccessToken.setDomain(domain);
        cookieForRefreshToken.setDomain(domain);
        cookieForAccessToken.setPath("/");
        cookieForRefreshToken.setPath("/");
        response.addCookie(cookieForAccessToken);
        response.addCookie(cookieForRefreshToken);

        //redis를 위한 refresh토큰 객체 생성 및 저장
        tokenRepository.save(new RefreshTokenRedis(refreshToken,email));

        //유저정보 body
        om.writeValue(response.getOutputStream(), ApiResponseDto.toResponse(ErrorCode.SUCCESS, MemberLoginResDto.from(customUserDetails)));    }
}
