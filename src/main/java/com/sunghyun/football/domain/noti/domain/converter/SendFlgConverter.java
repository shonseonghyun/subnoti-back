package com.sunghyun.football.domain.noti.domain.converter;

import com.sunghyun.football.domain.noti.domain.enums.SendFlg;
import jakarta.persistence.AttributeConverter;

public class SendFlgConverter implements AttributeConverter<SendFlg,String> {
    @Override
    public String convertToDatabaseColumn(SendFlg attribute) {
        return attribute.getFlg();
    }

    @Override
    public SendFlg convertToEntityAttribute(String dbData) {
        return SendFlg.getSendFlg(dbData);
    }
}
