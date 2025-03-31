package com.sunghyun.football.domain.noti.infrastructure.repository;

import com.sunghyun.football.domain.noti.domain.FreeSubNoti;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import com.sunghyun.football.domain.noti.domain.repository.FreeSubNotiRepository;
import com.sunghyun.football.domain.noti.infrastructure.entity.FreeSubNotiEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FreeSubNotiRepositoryImpl implements FreeSubNotiRepository {

    private final SpringJpaMatchFreeSubNotiRepository springJpaMatchFreeSubNotiRepository;

    @Override
    public FreeSubNoti save(FreeSubNoti freeSubNoti) {
        return springJpaMatchFreeSubNotiRepository.save(FreeSubNotiEntity.from(freeSubNoti)).toModel();
    }

    @Override
    public List<FreeSubType> getFreeSubTypes(String email, Long matchNo) {
        return springJpaMatchFreeSubNotiRepository.findByEmailAndMatchNo(email,matchNo)
                .stream()
                .map(FreeSubNotiEntity::getSubType)
                .toList();
    }

    @Override
    public List<FreeSubNoti> getFreeSubNoties(Long memberNo,String nowDt) {
        return springJpaMatchFreeSubNotiRepository.findByMemberNoAndStartDtAfterToday(memberNo,nowDt)
                .stream()
                .map(FreeSubNotiEntity::toModel)
                .toList();
    }

    @Override
    public List<FreeSubNoti> getFreeSubNotiesByDate(Long memberNo, String selectedDt) {
        return springJpaMatchFreeSubNotiRepository.findByMemberNoAndStartDt(memberNo,selectedDt)
                .stream()
                .map(FreeSubNotiEntity::toModel)
                .toList();
    }

    @Override
    public List<String> getNotiRegDtsByDt(Long memberNo, String startDt, String endDt) {
        return springJpaMatchFreeSubNotiRepository.findDatesByMemberNoAndDates(memberNo,startDt,endDt);
    }

    @Override
    public void delFreeSubNoti(Long notiNo) {
        springJpaMatchFreeSubNotiRepository.deleteById(notiNo);
    }
}
