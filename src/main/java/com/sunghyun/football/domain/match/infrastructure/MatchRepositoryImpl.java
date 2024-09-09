package com.sunghyun.football.domain.match.infrastructure;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sunghyun.football.domain.match.domain.dto.SearchMatchesReqDto;
import com.sunghyun.football.domain.match.domain.Match;
import com.sunghyun.football.domain.match.domain.repository.MatchRepository;
import com.sunghyun.football.domain.match.infrastructure.entity.MatchEntity;
import com.sunghyun.football.domain.match.infrastructure.entity.QMatchEntity;
import com.sunghyun.football.domain.match.infrastructure.entity.QMatchPlayerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MatchRepositoryImpl implements MatchRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final SpringJpaMatchRepository springJpaMatchRepository;
    private final SpringJpaMatchPlayerRepository springJpaMatchPlayerRepository;


    @Override
    public Match save(Match match) {
        return springJpaMatchRepository.save(MatchEntity.from(match)).toModel();
    }

    @Override
    public Optional<Match> findByMatchNo(Long matchNo) {
        return springJpaMatchRepository.findByMatchNo(matchNo).map(MatchEntity::toModel);
    }

    @Override
    public void deleteMatch(Long matchNo) {
        springJpaMatchRepository.deleteById(matchNo);
    }

    @Override
    public Match deletePlayer(Match match, Long playerNo) {
        Match savedMatch = springJpaMatchRepository.save(MatchEntity.from(match)).toModel();
        //oneToMany 단방향이기에 직접 삭제
        springJpaMatchPlayerRepository.deleteById(playerNo);
        return savedMatch;
    }

    @Override
    public int findMatchPlayerByMemberNoAndPlayDt(Long memberNo, String startDt, String startTm) {
        Long total = jpaQueryFactory.select(QMatchEntity.matchEntity.count())
                .from(QMatchEntity.matchEntity)
                .join(QMatchEntity.matchEntity.players, QMatchPlayerEntity.matchPlayerEntity)
                .where(
                        QMatchEntity.matchEntity.startDt.eq(startDt),
                        QMatchEntity.matchEntity.startTm.eq(startTm),
                        QMatchPlayerEntity.matchPlayerEntity.memberNo.eq(memberNo)
                )
                .fetchOne();

        return Math.toIntExact(total);
    }

    @Override
    public int findByStadiumNoAndPlayDt(Long stadiumNo, String startDt, String startTm) {
        Long total = jpaQueryFactory.select(QMatchEntity.matchEntity.count())
                .from(QMatchEntity.matchEntity)
                .where(
                        QMatchEntity.matchEntity.startDt.eq(startDt),
                        QMatchEntity.matchEntity.startTm.eq(startTm),
                        QMatchEntity.matchEntity.stadiumNo.eq(stadiumNo)
                )
                .fetchOne()
                ;
        return Math.toIntExact(total) ;
    }

    @Override
    public List<Match> findAll() {
        return springJpaMatchRepository.findAll().stream()
                .map(MatchEntity::toModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Match> findAllByConditions(SearchMatchesReqDto searchMatchesReqDto) {
        List<Match> result = jpaQueryFactory.selectFrom(QMatchEntity.matchEntity)
                .where(
                    eqStartDt(searchMatchesReqDto.getStartDt()),
                    eqStadiumNo(searchMatchesReqDto.getStadiumNo())
                )
                .orderBy(QMatchEntity.matchEntity.startTm.asc())
                .fetch()
                .stream().map(MatchEntity::toModel)
                .collect(Collectors.toList());
                ;
        return result;
//        return springJpaMatchRepository.findAllByStartDt(startDt).stream()
//                .map(MatchEntity::toModel)
//                .collect(Collectors.toList());
    }

    private BooleanExpression eqStartDt(String startDt) {
        if(startDt == null || startDt.isEmpty()) {
            return null;
        }
        return QMatchEntity.matchEntity.startDt.eq(startDt);
    }

    private BooleanExpression eqStadiumNo(Long stadiumNo) {
        if(stadiumNo == null) {
            return null;
        }
        return QMatchEntity.matchEntity.stadiumNo.eq(stadiumNo);
    }



}
