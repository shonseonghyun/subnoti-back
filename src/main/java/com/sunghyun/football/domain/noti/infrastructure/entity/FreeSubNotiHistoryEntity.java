package com.sunghyun.football.domain.noti.infrastructure.entity;

import com.sunghyun.football.domain.noti.domain.FreeSubNotiHistory;
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
public class FreeSubNotiHistoryEntity{

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

    public static FreeSubNotiHistoryEntity from(FreeSubNotiHistory freeSubNotiHistory){
        FreeSubNotiHistoryEntity freeSubNotiHistoryEntity = new FreeSubNotiHistoryEntity();
        freeSubNotiHistoryEntity.historyNo = freeSubNotiHistory.getHistoryNo();
        freeSubNotiHistoryEntity.activeType=freeSubNotiHistory.getActiveType();
        freeSubNotiHistoryEntity.subType=freeSubNotiHistory.getSubType();
        freeSubNotiHistoryEntity.sendDt=freeSubNotiHistory.getSendDt();
        freeSubNotiHistoryEntity.sendTm=freeSubNotiHistory.getSendTm();
        return freeSubNotiHistoryEntity;
    }

    public FreeSubNotiHistory toModel() {
        return FreeSubNotiHistory.builder()
                .historyNo(historyNo)
                .activeType(activeType)
                .subType(subType)
                .sendDt(sendDt)
                .sendTm(sendTm)
                .build()
                ;
    }
}
