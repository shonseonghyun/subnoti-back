package com.sunghyun.football.domain.noti.infrastructure.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sunghyun.football.domain.noti.domain.FreeSubNoti;
import com.sunghyun.football.domain.noti.domain.enums.FreeSubType;
import com.sunghyun.football.domain.noti.domain.repository.FreeSubNotiRepository;
import com.sunghyun.football.domain.noti.infrastructure.entity.FreeSubNotiEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.sunghyun.football.domain.noti.infrastructure.entity.QFreeSubNotiEntity.freeSubNotiEntity;

@Repository
@RequiredArgsConstructor
public class FreeSubNotiRepositoryImpl implements FreeSubNotiRepository {
    private final SpringJpaMatchFreeSubNotiRepository springJpaMatchFreeSubNotiRepository;
    private final JPAQueryFactory jpaQueryFactory;

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
    public List<FreeSubNoti> getFreeSubNotiesByDate(Long memberNo, String selectedDt, Long notiNo, int pageSize) {
        
        //no < 파라미터를 첫 페이지에선 사용하지 않기 위한 동적쿼리
        BooleanBuilder dynamicLtId = new BooleanBuilder();

        if(notiNo != null){
            dynamicLtId.and(freeSubNotiEntity.notiNo.lt(notiNo));
        }

        List<FreeSubNotiEntity> entityList = jpaQueryFactory
                .selectFrom(freeSubNotiEntity)
                .where(
                    ltNotiNo(notiNo),
                    (freeSubNotiEntity.memberNo.eq(memberNo))
                    .and(freeSubNotiEntity.startDt.eq(selectedDt))
                )
                .orderBy(freeSubNotiEntity.notiNo.desc())
                .limit(pageSize)
                .fetch();

        return entityList
                .stream()
                .map(FreeSubNotiEntity::toModel)
                .toList();
    }

    private BooleanExpression ltNotiNo(Long notiNo){
        if(notiNo == null){
            return null; // BooleanExpression 자리에 null이 반환되면 조건문에서 자동으로 제거된다
        }
        return freeSubNotiEntity.notiNo.lt(notiNo);
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
