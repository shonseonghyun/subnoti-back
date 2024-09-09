package com.sunghyun.football.global.exception.exceptions.stadium;

import com.sunghyun.football.global.exception.ErrorCode;

public class StadiumImageExceedException extends StadiumException{
    public StadiumImageExceedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
