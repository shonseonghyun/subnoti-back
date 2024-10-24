package com.sunghyun.football.domain.noti.infrastructure.repository;

import com.sunghyun.football.domain.member.infrastructure.entity.MemberEntity;
import com.sunghyun.football.domain.noti.domain.MatchFreeSubNoti;
import com.sunghyun.football.domain.noti.domain.repository.FreeSubNotiRepository;
import com.sunghyun.football.domain.noti.infrastructure.entity.MatchFreeSubNotiEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FreeSubNotiRepositoryImpl implements FreeSubNotiRepository {

    private final SpringJpaMatchFreeSubNotiRepository springJpaMatchFreeSubNotiRepository;

    @Override
    public MatchFreeSubNoti save(MatchFreeSubNoti freeSubNoti) {
        return springJpaMatchFreeSubNotiRepository.save(MatchFreeSubNotiEntity.from(freeSubNoti)).toModel();
    }

    @Override
    public Optional<MatchFreeSubNoti> getFreeSubNoti(String email, Long matchNo) {
        return springJpaMatchFreeSubNotiRepository.findByEmailAndMatchNo(email,matchNo).map(MatchFreeSubNotiEntity::toModel);
    }
}
