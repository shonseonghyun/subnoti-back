package com.sunghyun.football.global.noti.message.build.builder;

import com.sunghyun.football.global.noti.message.build.MessageBuilder;
import com.sunghyun.football.global.noti.message.build.dto.FreeSubNotiMessageDto;
import com.sunghyun.football.global.noti.type.NotiType;
import org.springframework.stereotype.Component;

@Component
public class FreeSubNotiMessageBuilder implements MessageBuilder<FreeSubNotiMessageDto> {

    @Override
    public String buildSubject(FreeSubNotiMessageDto dto) {
        final Long matchNo = dto.getMatchNo();
        final String matchName = dto.getMatchName();
        final String month = dto.getStartDt().substring(4,6);
        final String date = dto.getStartDt().substring(6);
        final String hour = dto.getStartTm().substring(0,2);
        final String minutes = dto.getStartTm().substring(2);
        final String freeSubTypeDesc = dto.getFreeSubType().getDesc();
        final String activeTypeDesc = dto.getActiveType().getDesc();

        return String.format("%s %s/%s %s:%s %s %s",
                matchName,month,date,hour,minutes,freeSubTypeDesc,activeTypeDesc
        );
    }

    @Override
    public String buildContent(FreeSubNotiMessageDto dto) {
        final Long matchNo = dto.getMatchNo();
        final String matchName = dto.getMatchName();
        final String month = dto.getStartDt().substring(4,6);
        final String date = dto.getStartDt().substring(6);
        final String hour = dto.getStartTm().substring(0,2);
        final String minutes = dto.getStartTm().substring(2);
        final String freeSubTypeDesc = dto.getFreeSubType().getDesc();
        final String activeTypeDesc = dto.getActiveType().getDesc();

        return String.format("%s %s/%s %s:%s %s %s \n 해당 매치 바로가기 https://www.plabfootball.com/match/%s/",
                matchName,month,date,hour,minutes,freeSubTypeDesc,activeTypeDesc,matchNo
        );
    }

    @Override
    public NotiType getNotiType() {
        return NotiType.NOTI_FREE_SUB;
    }
}
