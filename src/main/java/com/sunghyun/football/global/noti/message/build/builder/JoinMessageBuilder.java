package com.sunghyun.football.global.noti.message.build.builder;

import com.sunghyun.football.global.noti.message.build.MessageBuilder;
import com.sunghyun.football.global.noti.message.build.dto.JoinMessageDto;
import com.sunghyun.football.global.noti.type.NotiType;
import org.springframework.stereotype.Component;

@Component
public class JoinMessageBuilder implements MessageBuilder<JoinMessageDto> {
    @Override
    public String buildSubject(JoinMessageDto dto) {
        return String.format("[회원가입] %s 님! \n 회원가입을 축하합니다!", dto.getName());
    }

    @Override
    public String buildContent(JoinMessageDto dto) {
        return String.format("[회원가입] %s 님! \n 회원가입을 축하합니다!", dto.getName());
    }

    @Override
    public NotiType getNotiType() {
        return NotiType.NOTI_JOIN;
    }
}
