package com.sunghyun.football.domain.member.infrastructure.auth.custom.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.response.ApiResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper om;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        //httpStatus를 건드리지 못하기에 아래로 개선하였다.
//        om.writeValue(response.getOutputStream(), ApiResponseDto.toResponse(ErrorType.EMAIL_NOT_FOUND));
        final String code = exception.getMessage();
        ErrorType errorType = ErrorType.getErrorCodeEnum(code);

        ApiResponseDto apiResponseDto =ApiResponseDto.toResponse(errorType);
        String jsonErrorResponse = om.writeValueAsString(apiResponseDto);

        response.setStatus(errorType.getHttpStatus().value());
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE); // application/json
        response.getWriter().write(jsonErrorResponse);
    }
}
