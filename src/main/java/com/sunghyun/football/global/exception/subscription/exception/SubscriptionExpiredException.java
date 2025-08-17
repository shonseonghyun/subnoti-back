package com.sunghyun.football.global.exception.subscription.exception;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.subscription.SubscriptionException;

public class SubscriptionExpiredException extends SubscriptionException {
    public SubscriptionExpiredException(ErrorType errorType) {
        super(errorType);
    }
}
