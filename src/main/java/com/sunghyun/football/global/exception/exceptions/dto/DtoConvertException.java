package com.sunghyun.football.global.exception.exceptions.dto;

import com.sunghyun.football.global.exception.AppException;
import com.sunghyun.football.global.exception.ErrorCode;

public class DtoConvertException extends AppException {

    public DtoConvertException(ErrorCode errorCode) {
        super(errorCode);
    }
}
