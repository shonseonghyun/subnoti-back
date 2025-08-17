package com.sunghyun.football.global.exception.subscription.exception;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.subscription.SubscriptionException;

public class SubscriptionNotFoundException extends SubscriptionException {
    public SubscriptionNotFoundException(ErrorType errorType) {
        super(errorType);
    }
}
