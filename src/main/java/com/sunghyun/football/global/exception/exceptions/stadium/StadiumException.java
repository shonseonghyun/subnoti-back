package com.sunghyun.football.global.exception.exceptions.stadium;

import com.sunghyun.football.global.exception.AppException;
import com.sunghyun.football.global.exception.ErrorCode;

public class StadiumException extends AppException {
    public StadiumException(ErrorCode errorCode) {
        super(errorCode);
    }
}
