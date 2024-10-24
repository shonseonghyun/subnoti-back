package com.sunghyun.football.domain.noti.domain;

import com.sunghyun.football.domain.noti.domain.enums.SendFlg;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
//@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchFreeSubNoti {
    private Long notiNo;

    private String email;

    private Long matchNo;

    private SendFlg sendFlg;

    private String matchName;

    private String startDt;

    private String startTm;

    private String endTm;

//    private Long userNo;
//
//    private String endDt;
//
//    private String endTm;
//
//    private SendFlg sendFlg;
//
//    private String reqDt;
//
//    private String reqTm;
}
