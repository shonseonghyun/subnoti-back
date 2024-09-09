package com.sunghyun.football.domain.member.domain.enums;

import com.sunghyun.football.domain.stadium.enums.EnumMapperType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum MemberLevelCategory implements EnumMapperType {
    ROOKIE("루키", Arrays.asList(MemberLevelType.ROOKIE)),
    STARTER("스타터", Arrays.asList(MemberLevelType.STARTER1,MemberLevelType.STARTER2,MemberLevelType.STARTER3)),
    BEGINNER("비기너", Arrays.asList(MemberLevelType.BEGINNER1,MemberLevelType.BEGINNER2,MemberLevelType.BEGINNER3)),
    AMATEUR("아마추어", Arrays.asList(MemberLevelType.AMATEUR1,MemberLevelType.AMATEUR2,MemberLevelType.AMATEUR3)),
    SEMIPRO("세미프로", Arrays.asList(MemberLevelType.SEMIPRO1,MemberLevelType.SEMIPRO2,MemberLevelType.SEMIPRO3)),
    PRO("프로", Arrays.asList(MemberLevelType.PRO1,MemberLevelType.PRO2,MemberLevelType.PRO3))
    ;

    private String desc;
    private List<MemberLevelType> levelTypes;


    @Override
    public String getName() {
        return name();
    }
}
