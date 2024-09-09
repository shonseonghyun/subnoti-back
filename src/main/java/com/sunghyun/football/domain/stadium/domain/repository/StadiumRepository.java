package com.sunghyun.football.domain.stadium.domain.repository;

import com.sunghyun.football.domain.stadium.domain.Stadium;

import java.util.Optional;

public interface StadiumRepository {
    Stadium save(Stadium stadium);
    Optional<Stadium> findByStadiumNo(Long stadiumNo);
    void delete(Long stadiumNo);
}
