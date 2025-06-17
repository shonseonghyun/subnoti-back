package com.sunghyun.football.domain.noti.infrastructure.entity;

import com.sunghyun.football.domain.noti.domain.FreeSubNoti;
import com.sunghyun.football.domain.noti.domain.converter.FreeSubTypeConverter;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@Table(name = "FreeSubNotiReq")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FreeSubNotiEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notiNo;

    private Long memberNo;

    private String email;

    private Long matchNo;

    private String matchName;

    private String startDt;

    private String startTm;


    @Convert(converter = FreeSubTypeConverter.class)
    private FreeSubType subType;

    @OneToMany(cascade = {CascadeType.MERGE}
//            ,mappedBy = "matchFreeSubNotiEntity"
            ,fetch = FetchType.LAZY
    )
    @JoinColumn(name = "notiNo")
    private List<FreeSubNotiHistoryEntity> freeSubNotiHistories;

    public static FreeSubNotiEntity from(FreeSubNoti freeSubNoti){
        FreeSubNotiEntity freeSubNotiEntity = new FreeSubNotiEntity();
        freeSubNotiEntity.notiNo=freeSubNoti.getNotiNo();
        freeSubNotiEntity.memberNo=freeSubNoti.getMemberNo();
        freeSubNotiEntity.email=freeSubNoti.getEmail();
        freeSubNotiEntity.matchNo=freeSubNoti.getMatchNo();
        freeSubNotiEntity.matchName= freeSubNoti.getMatchName();
        freeSubNotiEntity.startDt=freeSubNoti.getStartDt();
        freeSubNotiEntity.startTm=freeSubNoti.getStartTm();
        freeSubNotiEntity.subType=freeSubNoti.getSubType();
        freeSubNotiEntity.freeSubNotiHistories=freeSubNoti.getFreeSubNotiHistories().stream().map(FreeSubNotiHistoryEntity::from).toList();
        return freeSubNotiEntity;
    }

    public FreeSubNoti toModel(){
        return FreeSubNoti.builder()
                .notiNo(notiNo)
                .memberNo(memberNo)
                .email(email)
                .matchNo(matchNo)
                .matchName(matchName)
                .startDt(startDt)
                .startTm(startTm)
                .subType(subType)
                .freeSubNotiHistories(freeSubNotiHistories.stream().map(FreeSubNotiHistoryEntity::toModel).toList())
                .build();
    }
}
