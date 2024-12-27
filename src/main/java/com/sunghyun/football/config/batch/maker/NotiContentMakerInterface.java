package com.sunghyun.football.config.batch.maker;

import com.sunghyun.football.domain.noti.domain.enums.ActiveType;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import com.sunghyun.football.domain.noti.infrastructure.entity.FreeSubNotiEntity;

public interface NotiContentMakerInterface {
    String getSubject(FreeSubNotiEntity freeSubNotiEntity, FreeSubType freeSubType, ActiveType activeType);
    String getContent(FreeSubNotiEntity freeSubNotiEntity, FreeSubType freeSubType, ActiveType activeType);
}
