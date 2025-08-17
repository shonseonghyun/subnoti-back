package com.sunghyun.football.domain.noti.domain.enums;

import com.sunghyun.football.global.exception.ErrorType;
import com.sunghyun.football.global.exception.noti.exception.SendFlgNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SendFlg {
    NOT_SEND("N","send Noti Not Yet"),
    MANAGER_FREE_ACTIVATE_SEND("A","send Noti Active Sub"),
    INACTIVATE_SEND("I","send Noti InActive Sub");

    private final String flg;
    private final String desc;

    public static SendFlg getSendFlg(String value){
        return Arrays.stream(SendFlg.values())
                .filter(sendFlg -> sendFlg.getFlg().equals(value))
                .findFirst()
                .orElseThrow(()->new SendFlgNotFoundException(ErrorType.SEND_FLG_NOT_FOUND));
    }

}
