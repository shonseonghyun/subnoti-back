package com.sunghyun.football.domain.noti.domain.enums;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.noti.exception.ActiveTypeNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ActiveType {
    ACTIVE("A","활성화"),
    INACTIVE("I","비활성화")
    ;

    private final String type;
    private final String desc;

    public static ActiveType getActiveType(String value){
        return Arrays.stream(ActiveType.values())
                .filter(activeType -> activeType.getType().equals(value))
                .findFirst()
                .orElseThrow(()->new ActiveTypeNotFoundException(ErrorType.ACTIVE_TYPE_NOT_FOUND));
    }
}
