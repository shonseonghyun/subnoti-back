package com.sunghyun.football.global.exception.exceptions.member;

import com.sunghyun.football.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class JoinException extends RuntimeException{
    private final ErrorCode errorCode;
}
