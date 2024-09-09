package com.sunghyun.football.domain.match.infrastructure.entity.converter;

import com.sunghyun.football.domain.member.domain.enums.MemberLevelType;
import jakarta.persistence.AttributeConverter;

public class MemberLevelTypeConverter implements AttributeConverter<MemberLevelType,Integer> {
    @Override
    public Integer convertToDatabaseColumn(MemberLevelType attribute) {
        return attribute.getLevelNum();
    }

    @Override
    public MemberLevelType convertToEntityAttribute(Integer dbData) {
        return MemberLevelType.getMemberLevel(dbData);
    }
}
