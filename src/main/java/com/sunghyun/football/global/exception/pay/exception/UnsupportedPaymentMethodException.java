package com.sunghyun.football.global.exception.pay.exception;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.pay.PayException;

public class UnsupportedPaymentMethodException extends PayException {
    public UnsupportedPaymentMethodException(ErrorType errorType) {
        super(errorType);
    }
}
