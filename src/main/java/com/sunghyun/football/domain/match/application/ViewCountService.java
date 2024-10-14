package com.sunghyun.football.domain.match.application;

import com.sunghyun.football.domain.match.domain.repository.ViewCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViewCountService {

    private final ViewCountRepository viewCountRepository;
    private final RedisTemplate redisTemplate;
    final String viewCountsKey = "ViewCounts";

    // Redis에 조회수 저장
    // key=> view:{matchNo} ex. key: viewCounts / value: {1(matchNo):19(조회수), 2(matchNo):20}

    // 회원은 memberNo로 중복여부 판별
    // 비회원은 음...Ip 로 중복여부 판별

    // 그러면 redis에서 어떤 자료구조형을 써야할지 확인해본다.

    public void regMatch(Long matchNo) {
        HashOperations<String,String,String> hashOperations =redisTemplate.opsForHash();
        hashOperations.put(viewCountsKey,matchNo.toString(),"0");
    }

    public int addViewCountHash(final Long matchNo
//            , final Long memberNo, final String ipAddress
    ){
        int viewCounts ;
        HashOperations<String,String,String> hashOperations =redisTemplate.opsForHash();

        Optional<String> viewCountsOptional = Optional.ofNullable(hashOperations.get(viewCountsKey,matchNo.toString()));


        if(viewCountsOptional.isPresent()){
            log.info("redis 저장소 내 matchNo {} 조회수 저장되어 있어 조회 수 증가 진행",matchNo);
            viewCounts = Integer.parseInt(viewCountsOptional.get())+1;
            hashOperations.put(viewCountsKey,matchNo.toString(),String.valueOf(viewCounts));
        }else{
            log.info("redis 저장소 내 matchNo {} 조회수 저장되어 있지 않아 조회 과정에서 삽입 진행",matchNo);
            viewCounts=1;
            hashOperations.put(viewCountsKey,matchNo.toString(),"1");
        }

        return viewCounts;
    }

    public void addViewCountList(final Long matchNo
//            , final Long memberNo, final String ipAddress
    ){
        final String key = "ViewCountsList";
        ListOperations<String,String> listOperations =redisTemplate.opsForList();
//        listOperations.
//        Optional<String> viewCountsOptional = Optional.ofNullable(hashOperations.get(key,matchNo.toString()));
//        log.info("matchNo {} 조회수: {}",matchNo,viewCounts);


//        if(viewCountsOptional.isPresent()){
//            hashOperations.put(key,matchNo.toString(),String.valueOf(Integer.parseInt(viewCountsOptional.get())+1));
//        }else{
//            hashOperations.put(key,matchNo.toString(),"0");
//        }
    }


}
