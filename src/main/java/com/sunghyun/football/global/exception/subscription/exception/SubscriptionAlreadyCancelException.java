package com.sunghyun.football.global.exception.subscription.exception;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.subscription.SubscriptionException;

public class SubscriptionAlreadyCancelException extends SubscriptionException {
    public SubscriptionAlreadyCancelException(ErrorType errorType) {
        super(errorType);
    }
}
