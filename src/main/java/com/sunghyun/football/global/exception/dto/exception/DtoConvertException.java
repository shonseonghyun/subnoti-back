package com.sunghyun.football.global.exception.dto.exception;

import com.sunghyun.football.global.exception.AppException;
import com.sunghyun.football.global.exception.ErrorType;

public class DtoConvertException extends AppException {

    public DtoConvertException(ErrorType errorType) {
        super(errorType);
    }
}
