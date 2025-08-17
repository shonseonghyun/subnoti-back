package com.sunghyun.football.global.exception.subscription;

import com.sunghyun.football.global.exception.AppException;
import com.sunghyun.football.global.exception.ErrorType;

public class SubscriptionException extends AppException {
    public SubscriptionException(ErrorType errorType) {
        super(errorType);
    }
}
