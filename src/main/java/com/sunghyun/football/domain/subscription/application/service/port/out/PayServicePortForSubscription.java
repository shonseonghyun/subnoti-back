package com.sunghyun.football.domain.subscription.application.service.port.out;

public interface PayServicePortForSubscription {
    void refund(final Long subscriptionNo);
}
