package com.sunghyun.football.global.exception.pay;

import com.sunghyun.football.global.exception.AppException;
import com.sunghyun.football.global.exception.ErrorType;

public class PayException extends AppException {
    public PayException(ErrorType errorType) {
        super(errorType);
    }
}
