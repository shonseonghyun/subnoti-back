package com.sunghyun.football.domain.member.infrastructure.auth.custom.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.member.auth.jwt.JwtException;
import com.sunghyun.football.global.response.ApiResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper om = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        }catch (JwtException e){
            final ErrorCode errorCode = e.getErrorCode();

            ApiResponseDto apiResponseDto =ApiResponseDto.toResponse(errorCode);
            String jsonErrorResponse = om.writeValueAsString(apiResponseDto);

            response.setStatus(errorCode.getHttpStatus().value());
            response.setCharacterEncoding("utf-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE); // application/json
            response.getWriter().write(jsonErrorResponse);
        }
    }
}
