package com.sunghyun.football.domain.noti.domain.converter;

import com.sunghyun.football.domain.noti.domain.enums.ActiveType;
import jakarta.persistence.AttributeConverter;

public class ActiveTypeConverter implements AttributeConverter<ActiveType,String> {
    @Override
    public String convertToDatabaseColumn(ActiveType attribute) {
        return attribute.getType();
    }

    @Override
    public ActiveType convertToEntityAttribute(String dbData) {
        return ActiveType.getActiveType(dbData);
    }
}
