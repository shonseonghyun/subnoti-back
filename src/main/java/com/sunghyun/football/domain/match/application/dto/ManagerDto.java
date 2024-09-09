package com.sunghyun.football.domain.match.application.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagerDto {
    private Long memberNo;
    private String managerName;
}
