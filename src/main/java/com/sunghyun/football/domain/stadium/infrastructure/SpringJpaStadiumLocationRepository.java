package com.sunghyun.football.domain.stadium.infrastructure;

import com.sunghyun.football.domain.stadium.infrastructure.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringJpaStadiumLocationRepository extends JpaRepository<LocationEntity,Long> {
}
