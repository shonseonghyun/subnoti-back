package com.sunghyun.football.global.exception.pay.exception;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.pay.PayException;

public class UnavailablePaymentMethodException extends PayException {
    public UnavailablePaymentMethodException(ErrorType errorType) {
        super(errorType);
    }
}
