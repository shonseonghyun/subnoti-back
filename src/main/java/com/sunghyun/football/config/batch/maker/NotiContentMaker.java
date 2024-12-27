package com.sunghyun.football.config.batch.maker;

import com.sunghyun.football.domain.noti.domain.enums.ActiveType;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import com.sunghyun.football.domain.noti.infrastructure.entity.FreeSubNotiEntity;

public enum NotiContentMaker implements NotiContentMakerInterface{
    FreeSub;


    @Override
    public String getSubject(FreeSubNotiEntity freeSubNotiEntity, FreeSubType freeSubType, ActiveType activeType) {
        final String matchName = freeSubNotiEntity.getMatchName();
        final String month = freeSubNotiEntity.getStartDt().substring(4,6);
        final String date = freeSubNotiEntity.getStartDt().substring(6);
        final String hour = freeSubNotiEntity.getStartTm().substring(0,2);
        final String minutes = freeSubNotiEntity.getStartTm().substring(2);
        return String.format("%s %s/%s %s:%s %s %s",matchName,month,date,hour,minutes,freeSubType.getDesc(),activeType.getDesc());
    }

    @Override
    public String getContent(FreeSubNotiEntity freeSubNotiEntity, FreeSubType freeSubType, ActiveType activeType) {
        final String matchName = freeSubNotiEntity.getMatchName();
        final String month = freeSubNotiEntity.getStartDt().substring(4,6);
        final String date = freeSubNotiEntity.getStartDt().substring(6);
        final String hour = freeSubNotiEntity.getStartTm().substring(0,2);
        final String minutes = freeSubNotiEntity.getStartTm().substring(2);
        return String.format("%s %s/%s %s:%s %s %s \n 해당 매치 바로가기 https://www.plabfootball.com/match/%s/",matchName,month,date,hour,minutes,freeSubType.getDesc(),activeType.getDesc(),freeSubNotiEntity.getMatchNo());
    }
}
