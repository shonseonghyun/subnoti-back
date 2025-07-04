//package com.sunghyun.football.domain.match.application;
//
//import com.sunghyun.football.domain.match.domain.repository.ViewCountRepository;
//import io.netty.util.internal.StringUtil;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.redis.core.HashOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class ViewCountService {
//
//    private final ViewCountRepository viewCountRepository;
//    private final RedisTemplate redisTemplate;
//    final String viewCountsKey = "ViewCounts";
//    final String ViewKeyPrefix = "View:";
//
//    // Redis에 조회수 저장
//    // key=> view:{matchNo} ex. key: viewCounts / value: {1(matchNo):19(조회수), 2(matchNo):20}
//
//    // 회원은 memberNo로 중복여부 판별
//    // 비회원은 Ip 로 중복여부 판별
//
//    // 그러면 redis에서 어떤 자료구조형을 써야할지 확인해본다.
//
//    public void regMatch(Long matchNo) {
//        HashOperations<String,String,String> hashOperations = redisTemplate.opsForHash();
//        if(StringUtil.isNullOrEmpty(hashOperations.get(viewCountsKey,matchNo.toString()))){
//            hashOperations.put(viewCountsKey,matchNo.toString(),"0");
//        }
//    }
//
//    public int addViewCountProcess(final Long matchNo, final Long memberNo, final String ipAddress){
//        HashOperations<String,String,String> hashOperations =redisTemplate.opsForHash();
//        String viewKey = ViewKeyPrefix+matchNo;
//        int viewCounts = Integer.parseInt(hashOperations.get(viewCountsKey,matchNo.toString()));
//
//        //특정 유저(회원,비회원) 처음 본 match인 경우
//         if(!isViewed(viewKey,memberNo,ipAddress)){
//            viewCounts = addViewCount(viewCounts,matchNo);
//            addViewUser(viewKey,memberNo,ipAddress);
//        }
//
//        return viewCounts;
//    }
//
//    private int addViewCount(int viewCounts,Long matchNo){
//        HashOperations<String,String,String> hashOperations =redisTemplate.opsForHash();
//        viewCounts = viewCounts+1;
//        hashOperations.put(viewCountsKey,matchNo.toString(),String.valueOf(viewCounts));
//        return viewCounts;
//    }
//
//    private void addViewUser(String viewKey,Long memberNo,String ipAddress){
//        if(memberNo==null){
//            redisTemplate.opsForSet().add(viewKey,ipAddress
//            );
//        }else{
//            redisTemplate.opsForSet().add(viewKey,memberNo.toString());
//        }
//        redisTemplate.expire(viewKey,30000, TimeUnit.MILLISECONDS);
//    }
//
//    private boolean isViewed(final String viewKey,final Long memberNo, final String ipAddress){
//        if(memberNo==null){
//            return redisTemplate.opsForSet().isMember(viewKey,ipAddress);
//        }else{
//            return redisTemplate.opsForSet().isMember(viewKey,memberNo.toString());
//        }
//    }
//
//    public Map<Long, Integer> getAllViewCounts() {
//        Set<String> keys = redisTemplate.opsForHash().keys(viewCountsKey);
//
//        if(keys==null){
//            return new HashMap<>();
//        }
//
//        return keys.stream().collect(Collectors.toMap(
//                key->Long.parseLong(key),
//                key-> Integer.parseInt((String)redisTemplate.opsForHash().get(viewCountsKey,key))
//        ));
//    }
//
//    public void resetViewCounts(){
//        Set<String> keys = redisTemplate.opsForHash().keys(viewCountsKey);
//
//        if(keys==null){
//            return ;
//        }
//
//        keys.forEach(key->{
//            redisTemplate.opsForHash().put(viewCountsKey,key,"0");
//        });
//    }
//
////    public Long extractMatchNoFromRedis(){
////        return null;
////    }
//}
