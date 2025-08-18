package com.sunghyun.football.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sunghyun.football.global.exception.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseDto<T> {
    private String code;
    private String msg;
    private T data; //있는 경우 세팅w

    public static ApiResponseDto toResponse(final ErrorType errorType) {
        return new ApiResponseDto(errorType);
    }

    public static <T> ApiResponseDto<T> toResponse(final ErrorType errorType, final T data) {
        return new ApiResponseDto(errorType,data);
    }

    private ApiResponseDto(final ErrorType errorType, final T data){
        this.code = errorType.getCode();
        this.msg = errorType.getMsg();
        this.data = data;
    }

    private ApiResponseDto(final ErrorType errorType){
        this.code = errorType.getCode();
        this.msg = errorType.getMsg();
    }
}
