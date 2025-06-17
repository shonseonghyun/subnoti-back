package com.sunghyun.football.domain.noti.domain;

import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import lombok.*;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FreeSubNoti {
    private Long notiNo;

    private Long memberNo;

    private String email;

    private Long matchNo;

    private String matchName;

    private String startDt;

    private String startTm;

    private FreeSubType subType;

    private List<FreeSubNotiHistory> freeSubNotiHistories;
}
