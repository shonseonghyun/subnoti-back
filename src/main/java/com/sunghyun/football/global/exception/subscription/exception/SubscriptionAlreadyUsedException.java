package com.sunghyun.football.global.exception.subscription.exception;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.subscription.SubscriptionException;

public class SubscriptionAlreadyUsedException extends SubscriptionException {
    public SubscriptionAlreadyUsedException(ErrorType errorType) {
        super(errorType);
    }
}
