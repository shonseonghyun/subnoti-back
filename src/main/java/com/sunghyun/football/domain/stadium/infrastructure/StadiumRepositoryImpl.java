package com.sunghyun.football.domain.stadium.infrastructure;

import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sunghyun.football.domain.stadium.domain.Stadium;
import com.sunghyun.football.domain.stadium.domain.repository.StadiumRepository;
import com.sunghyun.football.domain.stadium.infrastructure.entity.StadiumEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StadiumRepositoryImpl implements StadiumRepository {
    private final JPAQueryFactory jpaQueryFactory;
    private final SpringJpaStadiumRepository springJpaStadiumRepository;


    @Override
    public Stadium save(Stadium stadium) {
        return springJpaStadiumRepository.save(StadiumEntity.from(stadium)).toModel();
    }

    @Override
    public Optional<Stadium> findByStadiumNo(Long stadiumNo) {
        return springJpaStadiumRepository.findByStadiumNo(stadiumNo).map(StadiumEntity::toModel);
    }

    @Override
    public void delete(Long stadiumNo) {
        springJpaStadiumRepository.deleteById(stadiumNo);
    }

}
