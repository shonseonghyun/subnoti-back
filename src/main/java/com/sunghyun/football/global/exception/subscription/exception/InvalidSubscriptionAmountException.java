package com.sunghyun.football.global.exception.subscription.exception;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.subscription.SubscriptionException;

public class InvalidSubscriptionAmountException extends SubscriptionException {
    public InvalidSubscriptionAmountException(ErrorType errorType) {
        super(errorType);
    }
}
