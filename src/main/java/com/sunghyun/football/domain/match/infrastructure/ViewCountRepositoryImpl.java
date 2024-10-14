package com.sunghyun.football.domain.match.infrastructure;

import com.sunghyun.football.domain.match.domain.repository.ViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ViewCountRepositoryImpl implements ViewCountRepository {

    private final RedisTemplate redisTemplate;

//    public void save()
}
