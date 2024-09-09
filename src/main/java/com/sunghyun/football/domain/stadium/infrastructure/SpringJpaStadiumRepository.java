package com.sunghyun.football.domain.stadium.infrastructure;

import com.sunghyun.football.domain.stadium.infrastructure.entity.StadiumEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringJpaStadiumRepository extends JpaRepository<StadiumEntity,Long> {
    Optional<StadiumEntity> findByStadiumNo(Long stadiumNo);

    @Override
    void deleteById(Long stadiumNo);
}
