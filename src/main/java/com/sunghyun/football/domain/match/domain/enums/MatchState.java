package com.sunghyun.football.domain.match.domain.enums;

import com.sunghyun.football.domain.stadium.enums.EnumMapperType;
import com.sunghyun.football.global.exception.ErrorCode;
import com.sunghyun.football.global.exception.exceptions.match.MatchStateNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum MatchState implements EnumMapperType {
    MATCH_REG_MANAGER_BEFORE(0,"매니저 등록 전"),
    MATCH_AVAILABLE(1,"신청 가능"),
    MATCH_ALMOST_DONE(2,"마감 임박"),
    MATCH_END(3,"마감");

    private final Integer matchStateCode;
    private final String desc;

    public static MatchState getMatchState(Integer value){
        return Arrays.stream(MatchState.values())
                .filter(matchState -> matchState.getMatchStateCode().equals(value))
                .findFirst()
                .orElseThrow(()->new MatchStateNotFoundException(ErrorCode.MATCH_STATE_NOT_FOUND));
    }

    @Override
    public String getName() {
        return name();
    }

}
