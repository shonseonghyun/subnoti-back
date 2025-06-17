package com.sunghyun.football.config.batch.maker;

import com.sunghyun.football.domain.noti.domain.FreeSubNoti;
import com.sunghyun.football.domain.noti.domain.enums.ActiveType;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;

public interface NotiContentMakerInterface {
    String getSubject(FreeSubNoti freeSubNoti, FreeSubType freeSubType, ActiveType activeType);
    String getContent(FreeSubNoti freeSubNoti, FreeSubType freeSubType, ActiveType activeType);
}
