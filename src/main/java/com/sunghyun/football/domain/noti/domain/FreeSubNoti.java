package com.sunghyun.football.domain.noti.domain;

import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FreeSubNoti {
    private Long notiNo;

    private Long memberNo;

    private String email;

    private Long matchNo;

//    private SendFlg sendFlg;

    private String matchName;

    private String startDt;

    private String startTm;

//    private String endTm;

    private FreeSubType subType;

//    private LastType
}
