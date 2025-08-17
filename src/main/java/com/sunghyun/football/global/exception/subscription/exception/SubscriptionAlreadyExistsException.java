package com.sunghyun.football.global.exception.subscription.exception;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.subscription.SubscriptionException;

public class SubscriptionAlreadyExistsException extends SubscriptionException {
    public SubscriptionAlreadyExistsException(ErrorType errorType) {
        super(errorType);
    }
}
