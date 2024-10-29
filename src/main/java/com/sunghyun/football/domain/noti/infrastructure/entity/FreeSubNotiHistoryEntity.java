package com.sunghyun.football.domain.noti.infrastructure.entity;

import com.sunghyun.football.domain.noti.domain.converter.ActiveTypeConverter;
import com.sunghyun.football.domain.noti.domain.converter.FreeSubTypeConverter;
import com.sunghyun.football.domain.noti.domain.enums.ActiveType;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FreeSubNotiHistory")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class FreeSubNotiHistoryEntity implements Comparable<FreeSubNotiHistoryEntity>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyNo;

    @Convert(converter = ActiveTypeConverter.class)
    private ActiveType activeType;

    @Convert(converter = FreeSubTypeConverter.class)
    private FreeSubType subType;

//    @ManyToOne
//    @JoinColumn(name = "notiNo")
//    private FreeSubNotiEntity matchFreeSubNotiEntity;

    private String sendDt;

    private String sendTm;

    @Override
    public int compareTo(FreeSubNotiHistoryEntity o) {
        if(o.getHistoryNo()<historyNo){
            return -1;
        }
        else if(o.getHistoryNo()>historyNo){
            return 1;
        }
        return 0;
    }
}
