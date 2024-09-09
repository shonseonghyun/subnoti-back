package com.sunghyun.football.global.exception.exceptions.match;

import com.sunghyun.football.global.exception.AppException;
import com.sunghyun.football.global.exception.ErrorCode;

public class MatchException extends AppException {
    public MatchException(ErrorCode errorCode) {
        super(errorCode);
    }
}
