package com.sunghyun.football.domain.member.application.port.out;

import com.sunghyun.football.domain.member.application.dto.SubscriptionInfo;

public interface SubscriptionServicePortForMember {
    SubscriptionInfo getSubscription(final Long memberNo,final String today);
}
