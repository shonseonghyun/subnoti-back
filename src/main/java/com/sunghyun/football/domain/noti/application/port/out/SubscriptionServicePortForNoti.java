package com.sunghyun.football.domain.noti.application.port.out;

public interface SubscriptionServicePortForNoti {
    void use(final Long memberNo,final String today);
}
