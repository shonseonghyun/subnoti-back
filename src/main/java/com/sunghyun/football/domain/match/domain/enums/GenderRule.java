package com.sunghyun.football.domain.match.domain.enums;

import com.sunghyun.football.domain.member.domain.enums.Gender;
import com.sunghyun.football.domain.stadium.enums.EnumMapperType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GenderRule implements EnumMapperType {
    MALE(0,Gender.MALE,"남자 제한"),
    FEMALE(1,Gender.FEMALE,"여자 제한"),
    ALL(2,null,"제한 없음")
    ;

    private final Integer code;
    private final Gender gender;
    private final String desc;

    public static boolean isAvailableApply(GenderRule genderRule, Gender gender) {
        if(genderRule.equals(GenderRule.ALL)){
            return true;
        }
        return gender.equals(genderRule.getGender());
    }

    @Override
    public String getName() {
        return name();
    }
}
