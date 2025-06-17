package com.sunghyun.football.domain.noti.domain;

import com.sunghyun.football.domain.noti.domain.enums.ActiveType;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreeSubNotiHistory implements Comparable<FreeSubNotiHistory>{
    private Long historyNo;

    private ActiveType activeType;

    private FreeSubType subType;

    private String sendDt;

    private String sendTm;

    @Override
    public int compareTo(FreeSubNotiHistory o) {
        if(o.getHistoryNo()<historyNo){
            return -1;
        }
        else if(o.getHistoryNo()>historyNo){
            return 1;
        }
        return 0;
    }
}
