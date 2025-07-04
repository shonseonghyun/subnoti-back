package com.sunghyun.football.domain.member.domain.enums;

import com.sunghyun.football.domain.enumMapper.enums.EnumMapperType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender implements EnumMapperType
{
    MALE("남성"),
    FEMALE("여성")
    ;

    private final String desc;

    @Override
    public String getName() {
        return name();
    }
}
