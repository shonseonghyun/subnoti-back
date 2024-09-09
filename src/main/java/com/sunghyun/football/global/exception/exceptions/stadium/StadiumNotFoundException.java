package com.sunghyun.football.global.exception.exceptions.stadium;

import com.sunghyun.football.global.exception.ErrorCode;

public class StadiumNotFoundException extends StadiumException {
    public StadiumNotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
