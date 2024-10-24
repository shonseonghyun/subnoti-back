package com.sunghyun.football.domain.noti.domain.repository;

import com.sunghyun.football.domain.noti.domain.MatchFreeSubNoti;
import com.sunghyun.football.repository.TestRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class FreeSubNotiRepositoryTest extends TestRepository {

    private final String email = "test@naver.com";
    private final Long matchNo=12L;

    @Autowired
    private FreeSubNotiRepository freeSubNotiRepository;

    @Test
    void 서브알림대상매치등록(){
        //given
        MatchFreeSubNoti freeSubNoti= MatchFreeSubNoti.builder()
                .email(email)
                .matchNo(matchNo)
                .build();

        //when
        MatchFreeSubNoti savedFreeSubNoti1 =freeSubNotiRepository.save(freeSubNoti);

        //then
        Assertions.assertThat(savedFreeSubNoti1.getNotiNo()).isNotNull();
        Assertions.assertThat(savedFreeSubNoti1.getEmail()).isEqualTo(email);
        Assertions.assertThat(savedFreeSubNoti1.getMatchNo()).isEqualTo(matchNo);

    }

}