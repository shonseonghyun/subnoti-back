package com.sunghyun.football.domain.stadium.infrastructure;

import com.sunghyun.football.domain.stadium.infrastructure.entity.StadiumImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringJpaStadiumImageRepository extends JpaRepository<StadiumImageEntity,Long> {
}
