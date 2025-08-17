package com.sunghyun.football.domain.pay.application.port.out;

public interface SubscriptionServicePortForPay {
    Long createSubscription(final Long memberNo, final int amount, String today);
}
