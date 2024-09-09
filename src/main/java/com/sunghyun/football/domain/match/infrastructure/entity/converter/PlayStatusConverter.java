package com.sunghyun.football.domain.match.infrastructure.entity.converter;

import com.sunghyun.football.domain.match.domain.enums.PlayStatus;
import jakarta.persistence.AttributeConverter;

public class PlayStatusConverter implements AttributeConverter<PlayStatus,Integer> {
    @Override
    public Integer convertToDatabaseColumn(PlayStatus attribute) {
        return attribute.getPlayStatusCode();
    }

    @Override
    public PlayStatus convertToEntityAttribute(Integer dbData) {
        return PlayStatus.getPlayStatus(dbData);
    }
}
