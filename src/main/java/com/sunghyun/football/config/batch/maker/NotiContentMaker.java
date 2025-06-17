package com.sunghyun.football.config.batch.maker;

import com.sunghyun.football.domain.noti.domain.FreeSubNoti;
import com.sunghyun.football.domain.noti.domain.enums.ActiveType;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;

public enum NotiContentMaker implements NotiContentMakerInterface{
    FreeSub;


    @Override
    public String getSubject(FreeSubNoti freeSubNoti, FreeSubType freeSubType, ActiveType activeType) {
        final String matchName = freeSubNoti.getMatchName();
        final String month = freeSubNoti.getStartDt().substring(4,6);
        final String date = freeSubNoti.getStartDt().substring(6);
        final String hour = freeSubNoti.getStartTm().substring(0,2);
        final String minutes = freeSubNoti.getStartTm().substring(2);
        return String.format("%s %s/%s %s:%s %s %s",matchName,month,date,hour,minutes,freeSubType.getDesc(),activeType.getDesc());
    }

    @Override
    public String getContent(FreeSubNoti freeSubNoti, FreeSubType freeSubType, ActiveType activeType) {
        final String matchName = freeSubNoti.getMatchName();
        final String month = freeSubNoti.getStartDt().substring(4,6);
        final String date = freeSubNoti.getStartDt().substring(6);
        final String hour = freeSubNoti.getStartTm().substring(0,2);
        final String minutes = freeSubNoti.getStartTm().substring(2);
        return String.format("%s %s/%s %s:%s %s %s \n 해당 매치 바로가기 https://www.plabfootball.com/match/%s/",matchName,month,date,hour,minutes,freeSubType.getDesc(),activeType.getDesc(),freeSubNoti.getMatchNo());
    }
}
