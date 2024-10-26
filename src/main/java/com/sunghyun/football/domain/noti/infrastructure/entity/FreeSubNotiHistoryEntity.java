package com.sunghyun.football.domain.noti.infrastructure.entity;

import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "FreeSubNotiHistory")
@NoArgsConstructor
@Getter
public class FreeSubNotiHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyNo;

    private String ActiveType;

    private FreeSubType subType;

    private String sendDt;

    private String sendTm;
}
