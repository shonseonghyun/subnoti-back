package com.sunghyun.football.global.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunghyun.football.global.response.ApiResponseDto;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.method.MethodValidationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final ObjectMapper om;

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({AppException.class})
    public ApiResponseDto handleAppException(final AppException e){
        log.warn("[AppException] {}", e.getErrorCode().getMessage());
        return ApiResponseDto.toResponse(e.getErrorCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ApiResponseDto handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e){
        log.warn("[MethodArgumentTypeMismatchException] {} 필드 인입값이 올바르지 않습니다. 실제 인입된 값: {}",e.getName(),e.getValue());
        return ApiResponseDto.toResponse(ErrorCode.INVALID_PARAMETER);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("MethodArgumentNotValidException happened");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDto.toResponse(ErrorCode.INVALID_PARAMETER));
    }

    //@Pattern
    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("MethodArgumentNotValidException happened");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDto.toResponse(ErrorCode.INVALID_PARAMETER));
    }

    @Override
    protected ResponseEntity<Object> handleMethodValidationException(MethodValidationException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleMethodValidationException(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("HttpMessageNotReadableException happened");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseDto.toResponse(ErrorCode.INVALID_PARAMETER));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({FeignException.class})
    public ResponseEntity handleFeignException(final FeignException e) throws JsonProcessingException {
        log.error("여기에 다 잡혓나?");
        if(e.status()==HttpStatus.METHOD_NOT_ALLOWED.value()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.toResponse(ErrorCode.METHOD_NOT_ALLOWED));
        }

        else if(e.status()==HttpStatus.NOT_FOUND.value()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseDto.toResponse(ErrorCode.MATCH_NOT_FOUND));
        }

        String responseJson = e.contentUTF8();
        Map<String, String> responseMap = om.readValue(responseJson, Map.class);

        return ResponseEntity
                .status(e.status())
                .body(responseMap);
    }
 }
