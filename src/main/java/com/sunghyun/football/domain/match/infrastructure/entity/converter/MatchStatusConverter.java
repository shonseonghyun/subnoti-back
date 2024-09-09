package com.sunghyun.football.domain.match.infrastructure.entity.converter;

import com.sunghyun.football.domain.match.domain.enums.MatchStatus;
import jakarta.persistence.AttributeConverter;

public class MatchStatusConverter implements AttributeConverter<MatchStatus,Integer> {
    @Override
    public Integer convertToDatabaseColumn(MatchStatus attribute) {
        return attribute.getMatchStatusCode();
    }

    @Override
    public MatchStatus convertToEntityAttribute(Integer dbData) {
        return MatchStatus.getMatchStatus(dbData);
    }
}
