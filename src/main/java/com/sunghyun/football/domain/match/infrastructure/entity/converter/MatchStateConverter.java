package com.sunghyun.football.domain.match.infrastructure.entity.converter;

import com.sunghyun.football.domain.match.domain.enums.MatchState;
import jakarta.persistence.AttributeConverter;

public class MatchStateConverter implements AttributeConverter<MatchState,Integer> {
    @Override
    public Integer convertToDatabaseColumn(MatchState attribute) {
        return attribute.getMatchStateCode();
    }

    @Override
    public MatchState convertToEntityAttribute(Integer dbData) {
        return MatchState.getMatchState(dbData);
    }
}
