package com.sunghyun.football.domain.noti.domain.converter;

import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import jakarta.persistence.AttributeConverter;

public class FreeSubTypeConverter implements AttributeConverter<FreeSubType,String> {
    @Override
    public String convertToDatabaseColumn(FreeSubType attribute) {
        return attribute.getType();
    }

    @Override
    public FreeSubType convertToEntityAttribute(String dbData) {
        return FreeSubType.getFreeSubType(dbData);
    }
}
