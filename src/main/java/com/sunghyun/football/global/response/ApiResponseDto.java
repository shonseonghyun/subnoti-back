package com.sunghyun.football.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sunghyun.football.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto<T> {
    private String code; //errcode의 code
    private String msg; //errcode의 desc
    private T data; //있는 경우 세팅

    public static ApiResponseDto toResponse(final ErrorCode errorCode) {
        ApiResponseDto apiResponseDto = new ApiResponseDto(errorCode);
        return apiResponseDto;
    }

    public static <T> ApiResponseDto<T> toResponse(final ErrorCode errorCode, T data) {
        ApiResponseDto apiResponseDto = new ApiResponseDto(errorCode,data);
        return apiResponseDto;
    }

    private ApiResponseDto(final ErrorCode errorCode , final T data){
        this.code = errorCode.getCode();
        this.msg = errorCode.getMessage();
        this.data = data;
    }

    private ApiResponseDto(final ErrorCode errorCode){
        this.code = errorCode.getCode();
        this.msg = errorCode.getMessage();
    }
}
